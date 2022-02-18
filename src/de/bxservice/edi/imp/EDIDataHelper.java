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

import org.compiere.model.MOrg;
import org.compiere.util.ValueNamePair;

public class EDIDataHelper {
	
	private static final String GLN_TOKEN_NAME = "ClientGLN";
	private static final String SENDER_GLN_TOKEN_NAME = "SenderGLN";

	public static boolean isValidGLN(String clientGLN, int AD_Org_ID) {
		return getClientGLN(AD_Org_ID).equals(clientGLN);
	}
	
	public static String getClientGLN(int AD_Org_ID) {
		MOrg organization = MOrg.get(AD_Org_ID);
		String description = organization.getDescription();
		return description != null ? organization.getDescription().substring(4) : "";
	}
	
	public static String getGLNProperty(List<ValueNamePair> columnNameValues) {
		return getProperty(columnNameValues, GLN_TOKEN_NAME);
	}
	
	public static String getSenderGLNProperty(List<ValueNamePair> columnNameValues) {
		return getProperty(columnNameValues, SENDER_GLN_TOKEN_NAME);
	}
	
	public static String getMessageTypeProperty(List<ValueNamePair> columnNameValues) {
		return getProperty(columnNameValues, "EDI_MESSAGE_TYPE"); //Refactor
	}
	
	public static String getMessageReferenceProperty(List<ValueNamePair> columnNameValues) {
		return getProperty(columnNameValues, "EDI_MESSAGE_REFERENCE"); //Refactor
	}
	
	public static String getProperty(List<ValueNamePair> columnNameValues, String propertyName) {
		for (ValueNamePair vnp : columnNameValues) {
			if (propertyName.equals(vnp.getName()))
				return vnp.getValue();
		}
		return "";
	}

}
