package de.bxservice.edi.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.compiere.model.MColumn;
import org.compiere.util.DisplayType;

public class DateTypeConverter implements ITypeConverter<Date> {
	
	public static final String DATE_PATTERN = "yyyyMMdd";
	public static final String TIME_PATTERN = "HH:mm:ss'Z'";
	public static final String DATETIME_PATTERN = "yyyyMMdd'T'HH:mm:ss'Z'";

	@Override
	public Timestamp fromEDIValue(MColumn column, String value) {
		//TODO: Not always comes as yyyymmdd ? 
		int displayType = column.getAD_Reference_ID();
		String pattern = getPattern(displayType);
		
		if (DisplayType.isDate(displayType) && pattern != null && value != null) {
			Date parsed = null;
			try {
				parsed = new SimpleDateFormat(pattern).parse(value);
			} catch (ParseException e) {
				return null;
			}
			return new Timestamp(parsed.getTime());
		} else {
			return null;
		}
	}
	
	/**
	 * Returns an ISO-8601 format pattern according to the display type.
	 * @param displayType Display Type
	 * @return formatting pattern
	 */
	private String getPattern(int displayType) {
		if (displayType == DisplayType.Date)
			return DATE_PATTERN;
		else if (displayType == DisplayType.Time)
			return TIME_PATTERN;
		else if (displayType == DisplayType.DateTime)
			return DATETIME_PATTERN;
		else
			return null;
	}

}
