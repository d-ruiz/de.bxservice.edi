package de.bxservice.edi.imp;

import java.util.List;

import org.compiere.model.MColumn;
import org.compiere.util.ValueNamePair;

public interface POSerializer {

	void setPOValues(List<ValueNamePair> columnsAndValues);
	void setPOValue(MColumn column, Object value);
	void setNonColumnValues(String propertyName, String value);
	
}
