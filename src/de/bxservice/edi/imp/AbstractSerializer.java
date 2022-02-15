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

		currentPO.set_ValueOfColumn("Description", "EDI Order"); // TODO: remove
		currentPO.saveEx();
	}

	private void setPOValue(ValueNamePair property) {
		String columnName = getColumnName(property.getName());
		String columnValue = property.getValue();
		//if EDI syntax -> nothing -> Refactor
		if (columnName.startsWith("EDI")) //TODO: Save EDI columns in the edi additional info column  
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
