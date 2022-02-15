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

import static de.bxservice.edi.util.ProductHelper.PRODUCTNAME_COLUMNNAME;
import static de.bxservice.edi.util.ProductHelper.PRODUCT_COLUMNNAME;
import static de.bxservice.edi.util.ProductHelper.QTYENTERED_COLUMNNAME;

import java.math.BigDecimal;

import org.compiere.model.MColumn;
import org.compiere.model.MOrderLine;
import org.compiere.model.MProduct;

import de.bxservice.edi.util.ProductHelper;

public class OrderLineSerializer extends AbstractSerializer {

	public OrderLineSerializer(MOrderLine po) {
		super(po);
	}
	
	public MOrderLine getPO() {
		return (MOrderLine) currentPO;
	}

	@Override
	public void setPOValue(MColumn column, Object value) {
		String columnName = column.getColumnName();
		
		switch(columnName) {
		case PRODUCT_COLUMNNAME:
			getPO().setProduct((MProduct) value);
			break;
		case QTYENTERED_COLUMNNAME:
			getPO().setQty((BigDecimal) value);
			break;
		default:
			super.setPOValue(column, value);
		}
	}

	@Override
	public void setNonColumnValues(String propertyName, String value) {
		if (isOverrideProduct(propertyName)) {
			MProduct product = ProductHelper.getProductFromName(value);
			getPO().setProduct((MProduct) product);
		} else {
			///Special things like knNummer / Address ..
			//Has Order Line EDI Additional Info???
		}
	}
	
	private boolean isOverrideProduct(String columnName) {
		return PRODUCTNAME_COLUMNNAME.equals(columnName) && getPO().getM_Product_ID() <= 0; 
	}
}
