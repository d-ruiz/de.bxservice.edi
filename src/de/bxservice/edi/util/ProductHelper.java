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

import org.compiere.model.MProduct;
import org.compiere.model.Query;
import org.compiere.util.Env;

public class ProductHelper {
	
	public static final String PRODUCT_COLUMNNAME = "M_Product_ID";
	public static final String PRODUCTNAME_COLUMNNAME = "ProductName";
	public static final String QTYENTERED_COLUMNNAME = "QtyEntered";
	
	public static MProduct getProductFromValue(String value) {
		final String whereClause = "Value=?";
		return getProduct(whereClause, value);
	}
	
	public static MProduct getProductFromName(String value) {
		final String whereClause = "Name=?";
		return getProduct(whereClause, value);
	}
	
	private static MProduct getProduct(String whereClause, String parameter) {
		return new Query(Env.getCtx(), MProduct.Table_Name, whereClause, null)
				.setParameters(parameter)
				.setClient_ID()
				.setOnlyActiveRecords(true)
				.firstOnly();
	}

}
