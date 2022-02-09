package de.bxservice.edi.imp;

import java.util.List;

import org.compiere.model.MColumn;
import org.compiere.model.MOrder;
import org.compiere.model.MTable;
import org.compiere.util.Env;
import org.compiere.util.ValueNamePair;

import de.bxservice.edi.util.TypeConverterUtils;

public class ImportOrderSerializer {
	
	private MTable table = MTable.get(MOrder.Table_ID);
	public MOrder order;
	
	public void createOrderHeader(List<ValueNamePair> headerValues) {
		headerValues.forEach(valueNamePair -> System.out.println(valueNamePair.getName() + " = " + valueNamePair.getValue()));
		
		MOrder order = new MOrder(Env.getCtx(), 0, null); //Get trx from process

		MColumn column;
		String columnName;
		String columnValue;
		//if EDI syntax -> nothing
		for (ValueNamePair property : headerValues) {
			columnName = getColumnName(property.getName());
			columnValue = property.getValue();
			if (columnName.startsWith("EDI"))
				continue;

			column = table.getColumn(columnName);
			// if Name = columnname en Order -> Set
			if (column != null) {
				if (isReadOnlyColumn(column))
					continue;
				
				Object value = TypeConverterUtils.fromEDIValue(column, columnValue);
				if (value != null)
					order.set_ValueOfColumn(column.getAD_Column_ID(), value);
				//SetValue to order
				//order.set_ValueOfColumn(property.getName(), property.getValue()); //Check data type
				//Check how Rest does it -> Data type and so on
				// <> format properly
				// if value - search for it -> f.i BPartner
			} else { //Special things like knNummer / Address ..
				
			}
		}
		
		//If no Business Partner set default
	}
	
	private String getColumnName(String propertyName) {
		String columnName = propertyName;
		if (columnName.indexOf('<') > 0) //Remove format part
			columnName = columnName.substring(0, columnName.indexOf('<'));
		return columnName;
	}
	
	private boolean isReadOnlyColumn(MColumn column) {
		return column.isSecure() || column.isEncrypted() || column.isVirtualColumn();
	}
	
}
