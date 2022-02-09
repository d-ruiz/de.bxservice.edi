package de.bxservice.edi.util;

import org.compiere.model.MColumn;

public class LookupTypeConverter implements ITypeConverter<Object> {

	@Override
	public Object fromEDIValue(MColumn column, String value) {
		System.out.println(column.getName() + " - Lookup: " + value);

		return null;
	}

}
