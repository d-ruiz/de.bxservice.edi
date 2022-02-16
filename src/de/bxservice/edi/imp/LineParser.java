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

import de.bxservice.edi.model.MEDILine;

public class LineParser {
	
	private	String configurationLine;
	private String fileLine;
	private boolean isOptional;
	
	public LineParser(MEDILine ediConfigurationLine, String fileLine) {
		configurationLine = ediConfigurationLine.getMsgText();
		isOptional = ediConfigurationLine.isBXS_IsOptional();
		this.fileLine = fileLine;
	}

	public List<ValueNamePair> parseLine() {
		return isParseableLine() ? parseLineValues() : new ArrayList<ValueNamePair>(); 
	}
	
	public boolean isParseableLine() {
		String initialConfigTag = getTag(configurationLine); 
		String initialLineTag = getTag(fileLine);
		return configurationLine.contains(EDISyntaxHelper.TOKEN_DELIMITER) && initialConfigTag.equals(initialLineTag);
	}
	
	private String getTag(String line) {
		return line.substring(0, line.indexOf(EDISyntaxHelper.SEGMENT_TAG_SEPARATOR));
	}
	
	public void checkLineValidity() {
		if (!isFileLineEqualConfigurationLine() && !isOptional)
			throw new AdempiereException("Line: cannot be parsed. Expected: " + configurationLine + " - Actual: " + fileLine);
	}
	
	public boolean moveToNextFileLine() {
		return isParseableLine() || (!isParseableLine() && isFileLineEqualConfigurationLine());
	}
	
	public boolean isFileLineEqualConfigurationLine() {
		return fileLine.equals(configurationLine);
	}

	private List<ValueNamePair> parseLineValues() {
		List<ValueNamePair> columnNameValues = new ArrayList<ValueNamePair>();
		ValueNamePair columnNameValue;
		
		String currentFileLine = fileLine;
		String ediConfigurationLine = configurationLine;

		while (EDISyntaxHelper.hasToken(ediConfigurationLine)) {
			//TODO: refactor submethod
			String initialSyntax = EDISyntaxHelper.getLiteralStringBeforeToken(ediConfigurationLine);
			ediConfigurationLine = EDISyntaxHelper.getSubstringAfterLiteral(ediConfigurationLine, initialSyntax);

			String columnName = EDISyntaxHelper.getColumnName(ediConfigurationLine);
			String token = EDISyntaxHelper.getTokenString(columnName);
			ediConfigurationLine = EDISyntaxHelper.getSubstringAfterLiteral(ediConfigurationLine, token);

			String nextToken = null;
			if (EDISyntaxHelper.hasToken(ediConfigurationLine))
				nextToken = EDISyntaxHelper.getLiteralStringBeforeToken(ediConfigurationLine);

			if (currentFileLine.startsWith(initialSyntax)) {
				currentFileLine = EDISyntaxHelper.getSubstringAfterLiteral(currentFileLine, initialSyntax);
				String value = EDISyntaxHelper.getValue(currentFileLine, nextToken);
				columnNameValue = new ValueNamePair(value, columnName);
				columnNameValues.add(columnNameValue);

				currentFileLine = EDISyntaxHelper.getSubstringAfterLiteral(currentFileLine, value);
			}
		}
		
		return columnNameValues;
	}
}
