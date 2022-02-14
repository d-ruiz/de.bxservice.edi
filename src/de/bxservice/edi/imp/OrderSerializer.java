package de.bxservice.edi.imp;

import static de.bxservice.edi.util.BusinessPartnerHelper.BPARTNER_COLUMNNAME;

import org.compiere.model.MBPartner;
import org.compiere.model.MColumn;
import org.compiere.model.MOrder;

public class OrderSerializer extends AbstractSerializer {

	public OrderSerializer(MOrder po) {
		super(po);
	}
	
	public MOrder getPO() {
		return (MOrder) currentPO;
	}

	@Override
	public void setPOValue(MColumn column, Object value) {
		String columnName = column.getColumnName();

		if (BPARTNER_COLUMNNAME.equals(columnName)) {
			getPO().setBPartner((MBPartner) value);
		} else if (value != null)
			super.setPOValue(column, value);
	}

	@Override
	public void setNonColumnValues(String propertyName, String value) {
		///Special things like knNummer / Address ..
		//Has Order Line EDI Additional Info???
	}

}
