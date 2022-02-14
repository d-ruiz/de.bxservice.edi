package de.bxservice.edi.imp;

import static de.bxservice.edi.util.ProductHelper.PRODUCTNAME_COLUMNNAME;
import static de.bxservice.edi.util.ProductHelper.PRODUCT_COLUMNNAME;
import static de.bxservice.edi.util.ProductHelper.QTYENTERED_COLUMNNAME;

import java.math.BigDecimal;

import org.compiere.model.MColumn;
import org.compiere.model.MOrderLine;
import org.compiere.model.MProduct;

import de.bxservice.edi.util.ProductHelper;

public class OrderLineSerializer extends AbstractSerializer {

	public OrderLineSerializer(MOrderLine po) {
		super(po);
	}
	
	public MOrderLine getPO() {
		return (MOrderLine) currentPO;
	}

	@Override
	public void setPOValue(MColumn column, Object value) {
		String columnName = column.getColumnName();
		
		switch(columnName) {
		case PRODUCT_COLUMNNAME:
			getPO().setProduct((MProduct) value);
			break;
		case QTYENTERED_COLUMNNAME:
			getPO().setQty((BigDecimal) value);
			break;
		default:
			super.setPOValue(column, value);
		}
	}

	@Override
	public void setNonColumnValues(String propertyName, String value) {
		if (isOverrideProduct(propertyName)) {
			MProduct product = ProductHelper.getProductFromName(value);
			getPO().setProduct((MProduct) product);
		} else {
			///Special things like knNummer / Address ..
			//Has Order Line EDI Additional Info???
		}
	}
	
	private boolean isOverrideProduct(String columnName) {
		return PRODUCTNAME_COLUMNNAME.equals(columnName) && getPO().getM_Product_ID() <= 0; 
	}
}
