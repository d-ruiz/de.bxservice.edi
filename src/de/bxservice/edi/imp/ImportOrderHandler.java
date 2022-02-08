package de.bxservice.edi.imp;

import java.util.List;

import org.compiere.model.MColumn;
import org.compiere.model.MOrder;
import org.compiere.model.MTable;
import org.compiere.util.Env;
import org.compiere.util.ValueNamePair;

public class ImportOrderHandler {
	
	private MTable table = MTable.get(MOrder.Table_ID);
	public MOrder order;
	
	public void createOrderHeader(List<ValueNamePair> headerValues) {
		headerValues.forEach(valueNamePair -> System.out.println(valueNamePair.getName() + " = " + valueNamePair.getValue()));
		
		MOrder order = new MOrder(Env.getCtx(), 0, null); //Get trx from process
		MColumn column;
		//if EDI syntax -> nothing
		for (ValueNamePair property : headerValues) {
			if (property.getName().startsWith("EDI"))
				continue;
			
			// if Name = columnname en Order -> Set
			if (isValidColumn(property.getName())) {
				//SetValue to order
				order.set_ValueOfColumn(property.getName(), property.getValue()); //Check data type
				//Check how Rest does it -> Data type and so on
				// <> format properly
				// if value - search for it -> f.i BPartner
			}
		}
	}
	
	private boolean isValidColumn(String columnName) {
		return table.getColumnIndex(columnName) > 0;
	}

}
