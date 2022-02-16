package de.bxservice.edi.imp;

public class EDIErrorMessage {

    private static EDIErrorMessage instance;
    private static StringBuilder errorMessage = new StringBuilder();
    
    private EDIErrorMessage(){}

    public static EDIErrorMessage getInstance(){
        if(instance == null){
            instance = new EDIErrorMessage();
        }
        return instance;
    }
    
    public static void appendErrorMessage(String msg) {
    	errorMessage.append(msg).append(" ;");
    }
    
    public String flushErrorMessage() {
    	String errMsg = getErrorMessage();
    	errorMessage = new StringBuilder();
    	return errMsg;
    }
    
    public String getErrorMessage() {
    	return errorMessage.toString();
    }
}
