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

import static org.compiere.util.DisplayType.Location;

import org.compiere.model.MColumn;
import org.compiere.util.DisplayType;

public class TypeConverterUtils {

	@SuppressWarnings("rawtypes")
	public static Object fromEDIValue(MColumn column, String value) {

		ITypeConverter typeConverter = getTypeConverter(column.getAD_Reference_ID(), value);

		if (typeConverter != null) {
			return typeConverter.fromEDIValue(column, value);
		} else if (value != null && DisplayType.isText(column.getAD_Reference_ID())) {
			return value;
		} else {
			return null;
		}
	}

	@SuppressWarnings("rawtypes")
	private static ITypeConverter getTypeConverter(int displayType, String value) {
		ITypeConverter typeConverter = null;

		if (DisplayType.isNumeric(displayType)) {
			typeConverter = new NumericTypeConverter();
		} else if (DisplayType.isDate(displayType)) {
			typeConverter = new DateTypeConverter();
		} else if (displayType == Location
				|| DisplayType.isLookup(displayType)) {
			return new LookupTypeConverter();
		}
		return typeConverter;
	}
}
