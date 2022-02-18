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

import org.compiere.model.MColumn;
import org.compiere.model.MTable;
import org.compiere.model.PO;
import org.compiere.util.ValueNamePair;

import de.bxservice.edi.util.TypeConverterUtils;

public abstract class AbstractSerializer implements POSerializer {
	
	protected PO currentPO;
	protected MTable currentTable;
	
	public AbstractSerializer(PO po) {
		currentPO = po;
		setCurrentTable();
	}
	
	private void setCurrentTable() {
		currentTable = MTable.get(currentPO.get_Table_ID());
	}
	
	public PO getPO() {
		return currentPO;
	}
	
	public void setPOValues(List<ValueNamePair> columnsAndValues) {
		if (columnsAndValues == null || columnsAndValues.isEmpty())
			return;
		
		for (ValueNamePair property : columnsAndValues) {
			setPOValue(property);
		}
		currentPO.saveEx();
	}

	private void setPOValue(ValueNamePair property) {
		String columnName = getColumnName(property.getName());
		String columnValue = property.getValue();

		if (columnName.startsWith("EDI"))
			return;

		MColumn column = currentTable.getColumn(columnName);
		if (column == null) {
			setNonColumnValues(columnName, columnValue);
		} else if (isValidAndUpdatableColumn(column)) {
			Object value = TypeConverterUtils.fromEDIValue(column, columnValue);
			setPOValue(column, value);
		}
	}

	private String getColumnName(String propertyName) {
		String columnName = propertyName;
		if (columnName.indexOf('<') > 0) //Remove format part
			columnName = columnName.substring(0, columnName.indexOf('<'));
		return columnName;
	}

	private boolean isValidAndUpdatableColumn(MColumn column) {
		return column != null && !isReadOnlyColumn(column);
	}
	
	private boolean isReadOnlyColumn(MColumn column) {
		return column.isSecure() || column.isEncrypted() || column.isVirtualColumn();
	}
	
	public void setPOValue(MColumn column, Object value) {
		currentPO.set_ValueOfColumn(column.getAD_Column_ID(), value);
	}
}
