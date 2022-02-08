package de.bxservice.edi.imp;

import java.util.List;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.util.ValueNamePair;

import de.bxservice.edi.model.MEDIFormat;
import de.bxservice.edi.model.MEDISection;

public class ImportFileHandler {
	
	private MEDIFormat ediFormat;
	private EDIImportLineHandler ediLinesHandler;
	private ImportMessageCreator messageCreator = new ImportMessageCreator();
	private ImportOrderHandler orderCreator = new ImportOrderHandler();
	
	public ImportFileHandler(MEDIFormat ediFormat) {
		this.ediFormat = ediFormat;
	}
	
	public void parseFileLines(List<String> ediLines) {
		checkFileValidity(ediLines);
		ediLinesHandler = new EDIImportLineHandler(ediLines);

		parseInterchangeHeader();
		parseMessages();
		parseInterchageFooter();
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
		List<ValueNamePair> columnNameValues = ediLinesHandler.parseSection(headerSection);
		checkValidGLN(EDIDataHelper.getGLNProperty(columnNameValues));
		messageCreator.appendValues(columnNameValues);
	}

	/**
	 * Checks if the gln received in the file corresponds
	 * to the gln from the client running the process 
	 * **/
	private void checkValidGLN(String gln) {
		if (!EDIDataHelper.isValidGLN(gln))
			throw new AdempiereException("Invalid EDI file. The receiver GLN does not match the client's GLN.");
	}
	
	private void parseMessages() {
		parseMessageHeader(); // while new order
		parseMessageDetail(); // while new line
		parseMessageSummary(); //every new order
	}
	
	private void parseMessageHeader() {
		MEDISection msgHeader = ediFormat.getMessageHeader();
		List<ValueNamePair> columnNameValues = ediLinesHandler.parseSection(msgHeader);
		checkValidMessageType(EDIDataHelper.getMessageTypeProperty(columnNameValues));
		messageCreator.appendValues(columnNameValues);
		orderCreator.createOrderHeader(columnNameValues);

		//System.out.println(messageCreator.getMessage());
	}
	
	//Move somewhere
	private void checkValidMessageType(String fileMessageType) {
		if (!ediFormat.getMessageType().equals(fileMessageType))
			throw new AdempiereException("Invalid EDI file. The message type does not match the EDI format.");
	}
	
	private void parseMessageDetail() {
		//Same as messageHeader but in a loop with each line
	}
	
	private void parseMessageSummary() {
		//Check totals?
		//
	}
	
	private void parseInterchageFooter() {
		//Close properly, check file is correct
	}
}
