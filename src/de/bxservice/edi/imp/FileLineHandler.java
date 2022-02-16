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

import java.util.Iterator;
import java.util.List;

import de.bxservice.edi.model.MEDILine;

public class FileLineHandler {

	private Iterator<String> lines;
	private String currentLine; 

	public FileLineHandler(List<String> ediLines) {
		lines = ediLines.iterator();
		nextEDILine();
	}

	public String nextEDILine() {
		currentLine = "";
		if (lines.hasNext()) {
			currentLine = removeLastCharAndSpaces(lines.next());
		}
		return currentLine;
	}
	
	public String getCurrentFileLine() {
		return currentLine;
	}
	
	private String removeLastCharAndSpaces(String str) {
		return str.substring(0, str.length() - 1).trim();
	}
	
	public boolean hasMoreDetailLines(MEDILine line) {
		String ediFormatLine = line.getMsgText();
		String initialSyntax = EDISyntaxHelper.getLiteralStringBeforeToken(ediFormatLine);
		return getCurrentFileLine().startsWith(initialSyntax);
	}
}
