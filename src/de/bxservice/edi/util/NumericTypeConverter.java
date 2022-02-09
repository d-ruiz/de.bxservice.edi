package de.bxservice.edi.util;

import org.compiere.model.MColumn;

public class NumericTypeConverter implements ITypeConverter<Number> {

	@Override
	public Object fromEDIValue(MColumn column, String value) {
		System.out.println(column.getName() + " - Number: " + value);
		return null;
	}

}
