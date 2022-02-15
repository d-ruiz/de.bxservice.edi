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

import java.util.List;

import org.compiere.util.ValueNamePair;

public class MessageCreator {
	
	private final static String PROPERTY_SEPARATOR = "; ";
	private StringBuilder message = new StringBuilder();
	
	
	public void appendValues(List<ValueNamePair> valueNamePairs) {
		valueNamePairs.forEach(valueNamePair -> appendValue(valueNamePair.getName(), valueNamePair.getValue()));
	}
	
	public void appendValue(String propertyName, String value) {
		message.append(propertyName)
			.append("=")
			.append(value)
			.append(PROPERTY_SEPARATOR);
	}
	
	public String getMessage() {
		return message.toString();
	}

}
