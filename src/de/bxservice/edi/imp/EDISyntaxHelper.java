package de.bxservice.edi.imp;

public class EDISyntaxHelper {
	
	public final static String DEFAULT_SEGMENT_TERMINATOR = "'";
	public static final String SEGMENT_TAG_SEPARATOR = "+";
	private static final String COMPONENT_DATA_SEPARATOR = ":";
	private static final char RELEASE_CHARACHTER = '?';
	
	public static final String TOKEN_DELIMITER = "@";
	
	public static int getNextDataElementIndex(String ediLine) {
		int separatorIndex = getDataSeparatorIndex(ediLine, 0);
		if (separatorIndex < 0)
			separatorIndex = getSegmentSeparatorIndex(ediLine, 0);

		return separatorIndex < 0 ? ediLine.length() : separatorIndex;
	}
	
	private static int getDataSeparatorIndex(String ediLine, int fromIndex) {
		return getSeparatorIndex(ediLine, fromIndex, COMPONENT_DATA_SEPARATOR);
	}
	
	private static int getSegmentSeparatorIndex(String ediLine, int fromIndex) {
		return getSeparatorIndex(ediLine, fromIndex, SEGMENT_TAG_SEPARATOR);
	}
	
	private static int getSeparatorIndex(String ediLine, int fromIndex, String separatorCharachter) {
		int index = ediLine.indexOf(separatorCharachter, fromIndex);
		if (index != -1 && isCharachterReleased(ediLine, index))
			return getSeparatorIndex(ediLine, index+1, separatorCharachter);
		
		return index;
	}
	
	private static boolean isCharachterReleased(String ediLine, int index) {
		return ediLine.charAt(index-1) == RELEASE_CHARACHTER;
	}

}
