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
package de.bxservice.edi.process;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MAttachment;
import org.compiere.model.MOrder;
import org.compiere.model.MPInstance;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.AdempiereUserError;
import org.compiere.util.Msg;

import de.bxservice.edi.imp.FileHandler;
import de.bxservice.edi.model.MEDIFormat;

public class ImportEDIOrders extends SvrProcess {
	
	private int EDI_Format_ID = 0;
	private int AD_Org_ID = 0;
	private int C_DocType_ID = 0;
	private int M_Warehouse_ID = 0;
	private String fileName = "";

	@Override
	protected void prepare() {
		for (ProcessInfoParameter para : getParameter()) {
			String name = para.getParameterName();
			if (para.getParameter() == null);
			else if (name.equals("BXS_EDIFormat_ID"))
				EDI_Format_ID = para.getParameterAsInt();
			else if ("FileName".equals(name))
				fileName = para.getParameterAsString();
			else if (name.equals("AD_Org_ID"))
				AD_Org_ID = para.getParameterAsInt();
			else if (name.equals("C_DocTypeTarget_ID"))
				C_DocType_ID = para.getParameterAsInt();
			else if (name.equals("M_Warehouse_ID"))
				M_Warehouse_ID = para.getParameterAsInt();
		}
	}

	@Override
	protected String doIt() throws Exception {
		if (!isValidFile())
			throw new AdempiereUserError(Msg.getMsg(getCtx(), "FileInvalidExtension"));
		
		attachFileToProcessInstance();
		MEDIFormat ediFormat = MEDIFormat.get(EDI_Format_ID);
		List<String> allLines = getFileLines();

		FileHandler fileParser = new FileHandler(ediFormat, AD_Org_ID, C_DocType_ID, M_Warehouse_ID, get_TrxName());
		fileParser.parseFileLines(allLines);
		MOrder order = fileParser.getOrder();
		addBufferLog(order.getC_Order_ID(), order.getDateOrdered(), null, order.getDocumentNo(), order.get_Table_ID(), order.getC_Order_ID());
		return "@OK@";
	}
	
	private List<String> getFileLines() {
		//UTF-8 File: return Files.readAllLines(Paths.get(fileName));
		List<String> fileLines = new ArrayList<String>();
		
		try {
			FileReader fr = new FileReader(fileName);
			BufferedReader reader = new BufferedReader(fr);
			
			String str;
	        while ((str = reader.readLine()) != null) {
	        	fileLines.add(str);
	        }
			reader.close();
		} catch (IOException e) {
            e.printStackTrace();
            throw new AdempiereException("Error reading the file");
		} finally {
		}

        
        return fileLines;
	}
	
	private boolean isValidFile() {
		return fileName.endsWith(".txt");
	}
	
	private void attachFileToProcessInstance() {
		MPInstance mpi = new MPInstance (getCtx(), getAD_PInstance_ID(), get_TrxName());
		MAttachment attachment = mpi.createAttachment();
		attachment.addEntry(new File(fileName));
		attachment.saveEx();
	}

}
