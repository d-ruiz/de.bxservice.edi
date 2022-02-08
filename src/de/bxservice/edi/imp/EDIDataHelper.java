package de.bxservice.edi.imp;

import java.util.List;

import org.compiere.model.MOrg;
import org.compiere.util.Env;
import org.compiere.util.ValueNamePair;

public class EDIDataHelper {
	
	private static final String GLN_TOKEN_NAME = "ClientGLN";
	
	public static boolean isValidGLN(String clientGLN) {
		return getClientGLN().equals(clientGLN);
	}
	
	public static String getClientGLN() {
		MOrg organization = MOrg.get(Env.getAD_Org_ID(Env.getCtx()));
		return organization.getDescription().substring(4);
	}
	
	public static String getGLNProperty(List<ValueNamePair> columnNameValues) {
		return getProperty(columnNameValues, GLN_TOKEN_NAME);
	}
	
	public static String getMessageTypeProperty(List<ValueNamePair> columnNameValues) {
		return getProperty(columnNameValues, "EDI_MESSAGE_TYPE"); //Refactor
	}
	
	public static String getProperty(List<ValueNamePair> columnNameValues, String propertyName) {
		for (ValueNamePair vnp : columnNameValues) {
			if (propertyName.equals(vnp.getName()))
				return vnp.getValue();
		}
		return "";
	}

}
