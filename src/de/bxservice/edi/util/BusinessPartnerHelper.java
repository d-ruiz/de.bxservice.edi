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

import org.compiere.model.I_C_BPartner;
import org.compiere.model.MBPartner;
import org.compiere.model.MClientInfo;
import org.compiere.model.Query;
import org.compiere.model.X_C_BP_Relation;
import org.compiere.util.Env;

import de.bxservice.edi.imp.EDIErrorMessage;

public class BusinessPartnerHelper {
	
	public static final String BPARTNER_COLUMNNAME = "C_BPartner_ID";
	
	public static MBPartner getBPartnerFromValue(String value) {
		MBPartner bPartner = MBPartner.get(Env.getCtx(), value);
		if (bPartner == null)
			addErrorMessage(value);

		return bPartner != null ? bPartner : getDefaultBPartner();
	}
	
	public static MBPartner getDefaultBPartner() {
		MClientInfo clientInfo = MClientInfo.get(Env.getAD_Client_ID(Env.getCtx()));
		return MBPartner.get(Env.getCtx(), clientInfo.getC_BPartnerCashTrx_ID());
	}
	
	private static void addErrorMessage(String value) {
		EDIErrorMessage.appendErrorMessage("Business Partner with value: " + value + " not found.");
	}
	
	public static MBPartner getBusinessPartnerFromGLN(String gln) {
		final String whereClause = "gln=?";
		MBPartner retValue = new Query(Env.getCtx(), I_C_BPartner.Table_Name, whereClause, null)
				.setParameters(gln)
				.setClient_ID()
				.setOnlyActiveRecords(true)
				.firstOnly();
		return retValue;
	}
	
	public static boolean isValidPartnerRelation(MBPartner glnSender, MBPartner receiver) {
		X_C_BP_Relation bpRelation = getPartnerRelation(glnSender, receiver);
		return bpRelation != null;
	}
	
	public static int getBillPartnerLocationID(MBPartner glnSender, MBPartner receiver) {
		X_C_BP_Relation bpRelation = getPartnerRelation(glnSender, receiver);
		return bpRelation.getC_BPartnerRelation_Location_ID();
	}
	
	public static X_C_BP_Relation getPartnerRelation(MBPartner glnSender, MBPartner receiver) {
		String whereClause = X_C_BP_Relation.COLUMNNAME_C_BPartner_ID + " = ? AND " + 
				X_C_BP_Relation.COLUMNNAME_C_BPartnerRelation_ID + " = ?";
		
		return new Query(Env.getCtx(), X_C_BP_Relation.Table_Name, whereClause, null)
				.setParameters(receiver.getC_BPartner_ID(), glnSender.getC_BPartner_ID())
				.setOnlyActiveRecords(true)
				.first();
	}
}
