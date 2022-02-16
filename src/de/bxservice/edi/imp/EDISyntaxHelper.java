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

import org.adempiere.exceptions.AdempiereException;

public class EDISyntaxHelper {
	
	public final static String DEFAULT_SEGMENT_TERMINATOR = "'";
	public static final String SEGMENT_TAG_SEPARATOR = "+";
	private static final String COMPONENT_DATA_SEPARATOR = ":";
	private static final char RELEASE_CHARACHTER = '?';
	
	public static final String TOKEN_DELIMITER = "@";
	
	public static int getNextDataElementIndex(String ediLine) {
		int separatorIndex = getDataSeparatorIndex(ediLine, 0);
		if (separatorIndex < 0)
			separatorIndex = getSegmentSeparatorIndex(ediLine, 0);

		return separatorIndex < 0 ? ediLine.length() : separatorIndex;
	}
	
	private static int getDataSeparatorIndex(String ediLine, int fromIndex) {
		return getSeparatorIndex(ediLine, fromIndex, COMPONENT_DATA_SEPARATOR);
	}
	
	private static int getSegmentSeparatorIndex(String ediLine, int fromIndex) {
		return getSeparatorIndex(ediLine, fromIndex, SEGMENT_TAG_SEPARATOR);
	}
	
	private static int getSeparatorIndex(String ediLine, int fromIndex, String separatorCharachter) {
		int index = ediLine.indexOf(separatorCharachter, fromIndex);
		if (index != -1 && isCharachterReleased(ediLine, index))
			return getSeparatorIndex(ediLine, index+1, separatorCharachter);
		
		return index;
	}
	
	private static boolean isCharachterReleased(String ediLine, int index) {
		return ediLine.charAt(index-1) == RELEASE_CHARACHTER;
	}
	
	public static boolean hasToken(String line) {
		return line.indexOf(TOKEN_DELIMITER) > 0;
	}
	
	public static String getLiteralStringBeforeToken(String line) { //Check EDI delimiters better
		return line.substring(0, line.indexOf(TOKEN_DELIMITER));
	}
	
	public static String getSubstringAfterLiteral(String originalString, String literal) {
		return originalString.substring(originalString.indexOf(literal) + literal.length());
	}
	
	public static String getColumnName(String line) {
		String inStr = line;
		int i = inStr.indexOf(TOKEN_DELIMITER); // If no @
		inStr = inStr.substring(i+1, inStr.length());	// from first @
		int j = inStr.indexOf(TOKEN_DELIMITER);						// next @
		if (j < 0) {									// no second tag
			throw new AdempiereException("EDI Line wronly configured. It needs to have a closing @: " + line);
		}

		return inStr.substring(0, j);
	}
	
	public static String getValue(String currentFileLine, String nextToken) {
		int separatorIndex = nextToken != null ? currentFileLine.indexOf(nextToken) : 
										 getNextDataElementIndex(currentFileLine);
		return currentFileLine.substring(0, separatorIndex);
	}
	
	public static String getTokenString(String columnName) {
		return TOKEN_DELIMITER + columnName + TOKEN_DELIMITER;
	}

}
