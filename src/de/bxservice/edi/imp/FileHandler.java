/**********************************************************************
 * This file is part of iDempiere ERP Open Source                      *
 * http://www.idempiere.org                                            *
 *                                                                     *
 * Copyright (C) Contributors                                          *
 *                                                                     *
 * This program is free software; you can redistribute it and/or       *
 * modify it under the terms of the GNU General Public License         *
 * as published by the Free Software Foundation; either version 2      *
 * of the License, or (at your option) any later version.              *
 *                                                                     *
 * This program is distributed in the hope that it will be useful,     *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of      *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the        *
 * GNU General Public License for more details.                        *
 *                                                                     *
 * You should have received a copy of the GNU General Public License   *
 * along with this program; if not, write to the Free Software         *
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,          *
 * MA 02110-1301, USA.                                                 *
 *                                                                     *
 * Contributors:                                                       *
 * - Diego Ruiz - BX Service GmbH                                      *
 **********************************************************************/
package de.bxservice.edi.imp;

import java.util.ArrayList;
import java.util.List;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.util.ValueNamePair;

import de.bxservice.edi.model.MEDIFormat;
import de.bxservice.edi.model.MEDILine;
import de.bxservice.edi.model.MEDISection;

public class FileHandler {
	
	private MEDIFormat ediFormat;
	private FileLineHandler ediLinesHandler;
	private MessageCreator orderMessageCreator = new MessageCreator();
	private OrderCreator orderCreator;
	private int AD_Org_ID;
	private String headerMessageReference;
	
	public FileHandler(MEDIFormat ediFormat, int AD_Org_ID, int C_DocType_ID, int M_Warehouse_ID, String trxName) {
		this.ediFormat = ediFormat;
		this.AD_Org_ID = AD_Org_ID;
		orderCreator = new OrderCreator(AD_Org_ID, C_DocType_ID, M_Warehouse_ID, trxName);
	}
	
	public void parseFileLines(List<String> ediLines) {
		checkFileValidity(ediLines);
		ediLinesHandler = new FileLineHandler(ediLines);

		parseInterchangeHeader();
		parseMessages();
		parseInterchageFooter();
		
		orderCreator.setEDIAdditionalInfo(orderMessageCreator.getMessage());
		orderCreator.setEDIErrorMessageAndStatus();
	}
	
	private void checkFileValidity(List<String> ediLines) {
		if (ediLines == null || ediLines.size() == 0)
			throw new AdempiereException("No lines");
		
		for (String line : ediLines) {
			if (!isValidLine(line))
				throw new AdempiereException("Invalid line: " + line);
		}
	}
	
	private boolean isValidLine(String line) {
		String lastCharachter = line.substring(line.length() - 1);
		return ediFormat.getSegmentTerminator().equals(lastCharachter);
	}
	
	private void parseInterchangeHeader() {
		MEDISection headerSection = ediFormat.getInterchangeHeader();
		List<ValueNamePair> columnNameValues = parseSection(headerSection);
		checkValidGLN(EDIDataHelper.getGLNProperty(columnNameValues));
		headerMessageReference = EDIDataHelper.getMessageReferenceProperty(columnNameValues);
		orderMessageCreator.appendValues(columnNameValues);
	}

	/**
	 * Checks if the gln received in the file corresponds
	 * to the gln from the client running the process 
	 * **/
	private void checkValidGLN(String gln) {
		if (!EDIDataHelper.isValidGLN(gln, AD_Org_ID))
			throw new AdempiereException("Invalid EDI file. The receiver GLN does not match the client's GLN.");
	}
	
	private void parseMessages() {
		parseMessageHeader(); // while new order. TODO: Same as details with UNH+MessageNumber
		parseMessageDetail();
		parseMessageSummary(); //every new order
	}
	
	private void parseMessageHeader() {
		MEDISection msgHeader = ediFormat.getMessageHeader();
		List<ValueNamePair> columnNameValues = parseSection(msgHeader);
		checkValidMessageType(EDIDataHelper.getMessageTypeProperty(columnNameValues));
		orderMessageCreator.appendValues(columnNameValues);
		orderCreator.createOrderHeader(columnNameValues);
	}
	
	private void checkValidMessageType(String fileMessageType) {
		if (!ediFormat.getMessageType().equals(fileMessageType))
			throw new AdempiereException("Invalid EDI file. The message type does not match the EDI format.");
	}
	
	private void parseMessageDetail() {
		MEDISection msgDetail = ediFormat.getMessageDetail();
		MessageCreator lineMessageCreator;
		do {
			List<ValueNamePair> columnNameValues = parseSection(msgDetail);
			lineMessageCreator = new MessageCreator();
			lineMessageCreator.appendValues(columnNameValues);
			orderCreator.createLine(columnNameValues, lineMessageCreator.getMessage());
		} while(isSectionCompleted(msgDetail));
	}
	
	private boolean isSectionCompleted(MEDISection section) {
		MEDILine firstLine = section.getLines().get(0);
		return ediLinesHandler.hasMoreDetailLines(firstLine);
	}
	
	private void parseMessageSummary() {
		//TODO: Check Total segments?
		MEDISection msgSummary = ediFormat.getMessageSummary();
		List<ValueNamePair> columnNameValues = parseSection(msgSummary);
		orderMessageCreator.appendValues(columnNameValues);
	}
	
	private void parseInterchageFooter() {
		//TODO: Close properly, check file is correct
		MEDISection trailerSection = ediFormat.getInterchangeTrailer();
		List<ValueNamePair> columnNameValues = parseSection(trailerSection);
		String footerMsgReference = EDIDataHelper.getMessageReferenceProperty(columnNameValues);
		checkMessageReferenceValidity(footerMsgReference);
		orderMessageCreator.appendValues(columnNameValues);
	}
	
	private void checkMessageReferenceValidity(String footerMsgReference) {
		if (!headerMessageReference.equals(footerMsgReference))
			throw new AdempiereException("Nachrichtreferenze from header and footer are different. Invalid file.");
	}
	
	public List<ValueNamePair> parseSection(MEDISection section) {
		List<ValueNamePair> columnNameValues = new ArrayList<ValueNamePair>();
		parseLinesIntoList(columnNameValues, section);
		return columnNameValues;
	}
	
	private void parseLinesIntoList(List<ValueNamePair> columnNameValues, MEDISection section) {
		for (MEDILine line : section.getLines()) {
			addParsedLineIntoList(columnNameValues, line);
		}
	}

	private void addParsedLineIntoList(List<ValueNamePair> columnNameValues, MEDILine line) {
		String fileLine = ediLinesHandler.getCurrentFileLine();
		LineParser lineParser = new LineParser(line, fileLine);
		if (lineParser.isParseableLine()) {
			columnNameValues.addAll(lineParser.parseLine());
		} else {
			lineParser.checkLineValidity();
		}
		
		if (lineParser.moveToNextFileLine())
			ediLinesHandler.nextEDILine();
	}
}
