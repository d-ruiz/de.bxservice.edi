package de.bxservice.edi.util;

import org.compiere.model.MColumn;

public interface ITypeConverter<T> {

	public Object fromEDIValue(MColumn column, String value);
}
