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
package de.bxservice.edi.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.compiere.model.MColumn;
import org.compiere.util.DisplayType;

public class DateTypeConverter implements ITypeConverter<Date> {
	
	public static final String DATE_PATTERN = "yyyyMMdd";
	public static final String TIME_PATTERN = "HH:mm:ss'Z'";
	public static final String DATETIME_PATTERN = "yyyyMMdd'T'HH:mm:ss'Z'";

	@Override
	public Timestamp fromEDIValue(MColumn column, String value) {
		int displayType = column.getAD_Reference_ID();
		String pattern = getPattern(displayType);
		
		if (DisplayType.isDate(displayType) && pattern != null && value != null) {
			Date parsed = null;
			try {
				parsed = new SimpleDateFormat(pattern).parse(value);
			} catch (ParseException e) {
				return null;
			}
			return new Timestamp(parsed.getTime());
		} else {
			addErrorMessage("No converter for column: " + column.getName());
			return null;
		}
	}
	
	/**
	 * Returns an ISO-8601 format pattern according to the display type.
	 * @param displayType Display Type
	 * @return formatting pattern
	 */
	private String getPattern(int displayType) {
		if (displayType == DisplayType.Date)
			return DATE_PATTERN;
		else if (displayType == DisplayType.Time)
			return TIME_PATTERN;
		else if (displayType == DisplayType.DateTime)
			return DATETIME_PATTERN;
		else
			return null;
	}

}
