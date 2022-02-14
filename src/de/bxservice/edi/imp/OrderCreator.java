package de.bxservice.edi.imp;

import java.util.List;

import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.util.Env;
import org.compiere.util.ValueNamePair;

public class OrderCreator {
	
	private static final String ISEDI_COLUMNNAME = "BAY_isEDI";
	private static final String EDIINFORMATION_COLUMNNAME = "BAY_EDIAdditionalInfo";
	
	private MOrder order;
	private POSerializer serializer;
	
	public void createOrderHeader(List<ValueNamePair> headerValues) {
		createOrderWithEDIProperties();
		serializer = new OrderSerializer(order);
		serializer.setPOValues(headerValues);
	}
	
	private void createOrderWithEDIProperties() {
		order = new MOrder(Env.getCtx(), 0, null); //TODO: Get trx from process
		order.set_ValueOfColumn(ISEDI_COLUMNNAME, true);
		order.setC_DocTypeTarget_ID();//TODO: Waren-Lieferschein
	}
	
	public void createLine(List<ValueNamePair> detailValues) {
		MOrderLine orderLine = new MOrderLine(order);
		serializer = new OrderLineSerializer(orderLine);
		serializer.setPOValues(detailValues);
	}

	//Move to OrderSerializer probably
	public void setEDIAdditionalInfo(String message) {
		order.set_ValueOfColumn(EDIINFORMATION_COLUMNNAME, message);
		order.saveEx();
	}
}