package de.bxservice.edi.util;

import org.compiere.model.MBPartner;
import org.compiere.model.MClientInfo;
import org.compiere.util.Env;

public class BusinessPartnerHelper {
	
	public static final String BPARTNER_COLUMNNAME = "C_BPartner_ID";
	
	public static MBPartner getBPartnerFromValue(String value) {
		MBPartner bPartner = MBPartner.get(Env.getCtx(), value);
		return bPartner != null ? bPartner : getDefaultBPartner();
	}
	
	public static MBPartner getDefaultBPartner() {
		MClientInfo clientInfo = MClientInfo.get(Env.getAD_Client_ID(Env.getCtx()));
		return MBPartner.get(Env.getCtx(), clientInfo.getC_BPartnerCashTrx_ID());
	}

}
