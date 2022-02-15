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
package de.bxservice.edi.base;

import java.sql.ResultSet;
import org.adempiere.base.IModelFactory;
import org.compiere.model.PO;
import org.compiere.util.Env;
import org.osgi.service.component.annotations.Component;

import de.bxservice.edi.model.MEDIBPartner;
import de.bxservice.edi.model.MEDIDocType;
import de.bxservice.edi.model.MEDIFormat;
import de.bxservice.edi.model.MEDILine;
import de.bxservice.edi.model.MEDISection;

@Component(
		property= {"service.ranking:Integer=100"},
		service = org.adempiere.base.IModelFactory.class
		)
public class EDIModelFactory implements IModelFactory {

	@Override
	public Class<?> getClass(String tableName) {
		if (MEDIDocType.Table_Name.equals(tableName))
			return MEDIDocType.class;
		if (MEDIFormat.Table_Name.equals(tableName))
			return MEDIFormat.class;
		if (MEDISection.Table_Name.equals(tableName))
			return MEDISection.class;
		if (MEDILine.Table_Name.equals(tableName))
			return MEDILine.class;
		if (MEDIBPartner.Table_Name.equals(tableName))
			return MEDIBPartner.class;
		return null;
	}

	@Override
	public PO getPO(String tableName, int Record_ID, String trxName) {
		if (MEDIDocType.Table_Name.equals(tableName))
			return new MEDIDocType(Env.getCtx(), Record_ID, trxName);
		if (MEDIFormat.Table_Name.equals(tableName))
			return new MEDIFormat(Env.getCtx(), Record_ID, trxName);
		if (MEDISection.Table_Name.equals(tableName))
			return new MEDISection(Env.getCtx(), Record_ID, trxName);
		if (MEDILine.Table_Name.equals(tableName))
			return new MEDILine(Env.getCtx(), Record_ID, trxName);
		if (MEDIBPartner.Table_Name.equals(tableName))
			return new MEDIBPartner(Env.getCtx(), Record_ID, trxName);
		return null;
	}

	@Override
	public PO getPO(String tableName, ResultSet rs, String trxName) {
		if (MEDIDocType.Table_Name.equals(tableName))
			return new MEDIDocType(Env.getCtx(), rs, trxName);
		if (MEDIFormat.Table_Name.equals(tableName))
			return new MEDIFormat(Env.getCtx(), rs, trxName);
		if (MEDISection.Table_Name.equals(tableName))
			return new MEDISection(Env.getCtx(), rs, trxName);
		if (MEDILine.Table_Name.equals(tableName))
			return new MEDILine(Env.getCtx(), rs, trxName);
		if (MEDIBPartner.Table_Name.equals(tableName))
			return new MEDIBPartner(Env.getCtx(), rs, trxName);
		return null;
	}

}
