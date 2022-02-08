package de.bxservice.edi.imp;

import java.util.List;

import org.compiere.util.ValueNamePair;

public class ImportMessageCreator {
	
	private final static String PROPERTY_SEPARATOR = "; ";
	private StringBuilder message = new StringBuilder();
	
	
	public void appendValues(List<ValueNamePair> valueNamePairs) {
		valueNamePairs.forEach(valueNamePair -> appendValue(valueNamePair.getName(), valueNamePair.getValue()));
	}
	
	public void appendValue(String propertyName, String value) {
		message.append(propertyName)
			.append("=")
			.append(value)
			.append(PROPERTY_SEPARATOR);
	}
	
	public String getMessage() {
		return message.toString();
	}

}
