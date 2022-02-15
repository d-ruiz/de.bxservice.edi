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
package de.bxservice.edi.model;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.model.PO;
import org.compiere.model.Query;
import org.compiere.util.Env;

public class MEDIBPartner extends X_BXS_EDI_BPartner {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4288003311559512892L;
	
	public static final String DEFAULT_SEQ_NO = "1";

	public MEDIBPartner(Properties ctx, int BXS_EDI_BPartner_ID, String trxName) {
		super(ctx, BXS_EDI_BPartner_ID, trxName);
	}
	
	public MEDIBPartner(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}
	
	private static MEDIBPartner get(int BXS_EDIFormat_ID, PO po) {
		String whereClause = COLUMNNAME_BXS_EDIFormat_ID + " =? AND " + COLUMNNAME_C_BPartner_ID + " =? AND "
				+ COLUMNNAME_AD_Table_ID + " =?";
		
		return new Query(Env.getCtx(), Table_Name, whereClause, null)
				.setParameters(BXS_EDIFormat_ID, po.get_Value(COLUMNNAME_C_BPartner_ID), po.get_Table_ID())
				.setOnlyActiveRecords(true)
				.first();
	}
	
	public static MEDIBPartner get(int C_BPartner_ID, int AD_Table_ID) {
		String whereClause = COLUMNNAME_C_BPartner_ID + " =? AND "
				+ COLUMNNAME_AD_Table_ID + " =?";
		
		return new Query(Env.getCtx(), Table_Name, whereClause, null)
				.setParameters(C_BPartner_ID, AD_Table_ID)
				.setOnlyActiveRecords(true)
				.first();
	}
	
	public static String consumeNextMessageReferenceSeq(int BXS_EDIFormat_ID, PO po) {
		MEDIBPartner ediBPartner = get(BXS_EDIFormat_ID, po);
		return ediBPartner != null ? ediBPartner.consumeNextReferenceSeq() : DEFAULT_SEQ_NO;
	}
	
	public String consumeNextReferenceSeq() {
		String nextRef = String.valueOf(getCurrentNext());
		setCurrentNext(getCurrentNext() + getIncrementNo());
		saveEx();
		return nextRef;
	}

}
