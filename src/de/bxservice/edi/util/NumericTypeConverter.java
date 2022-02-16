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

import java.math.BigDecimal;

import org.compiere.model.MColumn;
import org.compiere.util.DisplayType;

public class NumericTypeConverter implements ITypeConverter<Number> {

	@Override
	public Object fromEDIValue(MColumn column, String value) {
		
		if (isInteger(column.getAD_Reference_ID())) {
			return convertToInteger(value);
		} else if (isBigDecimal(column.getAD_Reference_ID())) {
			return convertToBigDecimal(value);
		}

		return null;
	}
	
	private BigDecimal convertToBigDecimal(String value) {
		try {
			return new BigDecimal(value);	
		} catch (NumberFormatException e) {
			e.printStackTrace();
			addErrorMessage("Error converting number: " + value);
			return null;
		}
		
	}
	
	private Integer convertToInteger(String value) {
		try {
			return Integer.parseInt(value);			
		} catch (NumberFormatException e) {
			addErrorMessage("Error converting number: " + value);
			e.printStackTrace();
			return null;
		}

	}
	
	private boolean isInteger(int displayType) {
		return displayType == DisplayType.Integer;
	}
	
	private boolean isBigDecimal(int displayType) {
		return displayType == DisplayType.Amount || displayType == DisplayType.Quantity;
	}

}
