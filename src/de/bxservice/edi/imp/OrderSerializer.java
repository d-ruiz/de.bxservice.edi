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

import static de.bxservice.edi.util.BusinessPartnerHelper.BPARTNER_COLUMNNAME;

import org.compiere.model.MBPartner;
import org.compiere.model.MColumn;
import org.compiere.model.MOrder;

public class OrderSerializer extends AbstractSerializer {

	public OrderSerializer(MOrder po) {
		super(po);
	}
	
	public MOrder getPO() {
		return (MOrder) currentPO;
	}

	@Override
	public void setPOValue(MColumn column, Object value) {
		String columnName = column.getColumnName();

		if (BPARTNER_COLUMNNAME.equals(columnName)) {
			getPO().setBPartner((MBPartner) value);
		} else if (value != null)
			super.setPOValue(column, value);
	}

	@Override
	public void setNonColumnValues(String propertyName, String value) {
		///TODO: Address - Special things like knNummer / Address ..
		//Has Order Line EDI Additional Info???
	}

}
