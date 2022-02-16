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

import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.util.Env;
import org.compiere.util.Util;
import org.compiere.util.ValueNamePair;

public class OrderCreator {
	
	private static final String ISEDI_COLUMNNAME = "BAY_isEDI";
	private static final String EDIINFORMATION_COLUMNNAME = "BAY_EDIAdditionalInfo";
	private static final String EDIERROR_COLUMNNAME = "BAY_EDIError";
	
	private MOrder order;
	private POSerializer serializer;
	
	public void createOrderHeader(List<ValueNamePair> headerValues) {
		createOrderWithEDIProperties();
		serializer = new OrderSerializer(order);
		serializer.setPOValues(headerValues);
	}
	
	private void createOrderWithEDIProperties() {
		order = new MOrder(Env.getCtx(), 0, null); //TODO: Get trx from process
		order.set_ValueOfColumn(ISEDI_COLUMNNAME, true);
		order.setC_DocTypeTarget_ID(); //TODO: Waren-Lieferschein
	}
	
	public void createLine(List<ValueNamePair> detailValues) {
		MOrderLine orderLine = new MOrderLine(order);
		serializer = new OrderLineSerializer(orderLine);
		serializer.setPOValues(detailValues);
	}

	public void setEDIAdditionalInfo(String message) {
		order.set_ValueOfColumn(EDIINFORMATION_COLUMNNAME, message);
		order.saveEx();
	}
	
	public void setEDIErrorMessage() {
		EDIErrorMessage errorMessage = EDIErrorMessage.getInstance();
		String ediErrors = errorMessage.flushErrorMessage();
		if (!Util.isEmpty(ediErrors)) {
			order.set_ValueOfColumn(EDIERROR_COLUMNNAME, ediErrors);
			order.saveEx();
		}
	}
}