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

}
