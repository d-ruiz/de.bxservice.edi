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

import org.compiere.model.MBPartner;
import org.compiere.model.MClientInfo;
import org.compiere.util.Env;

public class BusinessPartnerHelper {
	
	public static final String BPARTNER_COLUMNNAME = "C_BPartner_ID";
	
	public static MBPartner getBPartnerFromValue(String value) {
		MBPartner bPartner = MBPartner.get(Env.getCtx(), value);
		return bPartner != null ? bPartner : getDefaultBPartner();
	}
	
	public static MBPartner getDefaultBPartner() {
		MClientInfo clientInfo = MClientInfo.get(Env.getAD_Client_ID(Env.getCtx()));
		return MBPartner.get(Env.getCtx(), clientInfo.getC_BPartnerCashTrx_ID());
	}

}
