package de.bxservice.edi.util;

import org.compiere.model.MProduct;
import org.compiere.model.Query;
import org.compiere.util.Env;

public class ProductHelper {
	
	public static final String PRODUCT_COLUMNNAME = "M_Product_ID";
	public static final String PRODUCTNAME_COLUMNNAME = "ProductName";
	public static final String QTYENTERED_COLUMNNAME = "QtyEntered";
	
	public static MProduct getProductFromValue(String value) {
		final String whereClause = "Value=?";
		return getProduct(whereClause, value);
	}
	
	public static MProduct getProductFromName(String value) {
		final String whereClause = "Name=?";
		return getProduct(whereClause, value);
	}
	
	private static MProduct getProduct(String whereClause, String parameter) {
		return new Query(Env.getCtx(), MProduct.Table_Name, whereClause, null)
				.setParameters(parameter)
				.setClient_ID()
				.setOnlyActiveRecords(true)
				.firstOnly();
	}

}
