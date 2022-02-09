package de.bxservice.edi.util;

import static org.compiere.util.DisplayType.Location;

import org.compiere.model.MColumn;
import org.compiere.util.DisplayType;

public class TypeConverterUtils {

	@SuppressWarnings("rawtypes")
	public static Object fromEDIValue(MColumn column, String value) {

		ITypeConverter typeConverter = getTypeConverter(column.getAD_Reference_ID(), value);

		if (typeConverter != null) {
			return typeConverter.fromEDIValue(column, value);
		} else if (value != null && DisplayType.isText(column.getAD_Reference_ID())) {
			return value;
		} else {
			return null;
		}
	}

	@SuppressWarnings("rawtypes")
	private static ITypeConverter getTypeConverter(int displayType, String value) {
		ITypeConverter typeConverter = null;

		if (DisplayType.isNumeric(displayType)) {
			typeConverter = new NumericTypeConverter();
		} else if (DisplayType.isDate(displayType)) {
			typeConverter = new DateTypeConverter();
		} else if (displayType == Location
				|| DisplayType.isLookup(displayType)) {
			return new LookupTypeConverter();
		}
		return typeConverter;
	}
}
