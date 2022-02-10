package de.bxservice.edi.util;

import static de.bxservice.edi.util.BusinessPartnerHelper.BPARTNER_COLUMNNAME;
import org.compiere.model.MColumn;

public class LookupTypeConverter implements ITypeConverter<Object> {
	

	@Override
	public Object fromEDIValue(MColumn column, String value) {
		System.out.println(column.getName() + " - Lookup: " + value);
		if (BPARTNER_COLUMNNAME.equals(column.getColumnName())) {
			return BusinessPartnerHelper.getBPartnerFromValue(value);
		}
	
		return null;
	}

}
