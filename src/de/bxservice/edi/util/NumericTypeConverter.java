package de.bxservice.edi.util;

import java.math.BigDecimal;

import org.compiere.model.MColumn;
import org.compiere.util.DisplayType;

public class NumericTypeConverter implements ITypeConverter<Number> {

	@Override
	public Object fromEDIValue(MColumn column, String value) {
		
		if (isInteger(column.getAD_Reference_ID())) {
			return convertToInteger(value);
		} else if (isBigDecimal(column.getAD_Reference_ID())) {
			return convertToBigDecimal(value);
		}

		return null;
	}
	
	private BigDecimal convertToBigDecimal(String value) {
		// try //catch
		return new BigDecimal(value);
	}
	
	private Integer convertToInteger(String value) {
		// try //catch
		return Integer.parseInt(value);
	}
	
	private boolean isInteger(int displayType) {
		return displayType == DisplayType.Integer;
	}
	
	private boolean isBigDecimal(int displayType) {
		return displayType == DisplayType.Amount || displayType == DisplayType.Quantity;
	}

}
