package de.bxservice.edi.imp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.util.ValueNamePair;

import de.bxservice.edi.model.MEDILine;
import de.bxservice.edi.model.MEDISection;

public class EDIImportLineHandler {

	private Iterator<String> lines;
	private String currentLine; 

	public EDIImportLineHandler(List<String> ediLines) {
		lines = ediLines.iterator();
		nextEDILine();
	}

	public String nextEDILine() {
		currentLine = "";
		if (lines.hasNext()) {
			currentLine = removeLastCharAndSpaces(lines.next());
		}
		return currentLine;
	}
	
	public String getCurrentFileLine() {
		return currentLine;
	}
	
	public boolean isParseableLine(String configLine) {
		String initialConfigTag = getTag(configLine); 
		String initialLineTag = getTag(currentLine);
		return configLine.contains(EDISyntaxHelper.TOKEN_DELIMITER) && initialConfigTag.equals(initialLineTag);
	}
	
	private String getTag(String line) {
		return line.substring(0, line.indexOf(EDISyntaxHelper.SEGMENT_TAG_SEPARATOR));
	}

	private String removeLastCharAndSpaces(String str) {
		return str.substring(0, str.length() - 1).trim();
	}
	
	public boolean isValidSegment(String segment) {
		return currentLine.equals(segment);
	}
	
	public List<ValueNamePair> parseSection(MEDISection section) {
		List<ValueNamePair> columnNameValues = new ArrayList<ValueNamePair>();
		parseLinesIntoList(columnNameValues, section);
		return columnNameValues;
	}
	
	private void parseLinesIntoList(List<ValueNamePair> columnNameValues, MEDISection section) {
		for (MEDILine line : section.getLines()) {
			columnNameValues.addAll(parseEDILine(line));
		}
	}

	public boolean hasMoreDetailLines(MEDILine line) {
		String ediFormatLine = line.getMsgText();
		String initialSyntax = getLiteralStringBeforeToken(ediFormatLine);
		return getCurrentFileLine().startsWith(initialSyntax);
	}
	
	public List<ValueNamePair> parseEDILine(MEDILine line) {
		List<ValueNamePair> columnNameValues = new ArrayList<ValueNamePair>();
		
		//Loop through file as well
		String ediFormatLine = line.getMsgText();
		
		//Repeated code
		if (isParseableLine(ediFormatLine)) {
			columnNameValues = parseLine(ediFormatLine);
		} else if (!isValidSegment(ediFormatLine)) { //Add optional conditional
			if (!line.isBXS_IsOptional())
				throw new AdempiereException("Line: cannot be parsed. Expected: " + ediFormatLine + " - Actual: " + getCurrentFileLine());
			return columnNameValues; // break and do not go to next line
		}
		nextEDILine();
		
		return columnNameValues;
	}

	public List<ValueNamePair> parseLine(String ediFileLine) {
		List<ValueNamePair> columnNameValues = new ArrayList<ValueNamePair>();
		ValueNamePair columnNameValue;
		
		String currentFileLine = currentLine;
		String ediConfigurationLine = ediFileLine;

		while (hasToken(ediConfigurationLine)) {
			//TODO: refactor submethod
			String initialSyntax = getLiteralStringBeforeToken(ediConfigurationLine);
			ediConfigurationLine = getSubstringAfterLiteral(ediConfigurationLine, initialSyntax);

			String columnName = getColumnName(ediConfigurationLine);
			String token = EDISyntaxHelper.TOKEN_DELIMITER+columnName+EDISyntaxHelper.TOKEN_DELIMITER; //TODO: Utils method to add @@
			ediConfigurationLine = getSubstringAfterLiteral(ediConfigurationLine, token);

			String nextToken = null;
			if (hasToken(ediConfigurationLine))
				nextToken = getLiteralStringBeforeToken(ediConfigurationLine);

			if (currentFileLine.startsWith(initialSyntax)) {
				currentFileLine = getSubstringAfterLiteral(currentFileLine, initialSyntax);
				String value = getValue(currentFileLine, nextToken);
				columnNameValue = new ValueNamePair(value, columnName);
				columnNameValues.add(columnNameValue);
				
				currentFileLine = getSubstringAfterLiteral(currentFileLine, value); //Rename
			}
		}
		
		return columnNameValues;
	}
	
	private String getValue(String currentFileLine, String nextToken) {
		int separatorIndex = nextToken != null ? currentFileLine.indexOf(nextToken) : 
										 EDISyntaxHelper.getNextDataElementIndex(currentFileLine);
		return currentFileLine.substring(0, separatorIndex);
	}
	
	private boolean hasToken(String line) {
		return line.indexOf(EDISyntaxHelper.TOKEN_DELIMITER) > 0;
	}

	private String getLiteralStringBeforeToken(String line) { //Check EDI delimiters better
		return line.substring(0, line.indexOf(EDISyntaxHelper.TOKEN_DELIMITER));
	}
	
	private String getSubstringAfterLiteral(String originalString, String literal) {
		return originalString.substring(originalString.indexOf(literal) + literal.length());
	}
	
	private String getColumnName(String line) {
		String inStr = line;
		int i = inStr.indexOf(EDISyntaxHelper.TOKEN_DELIMITER); // If no @
		inStr = inStr.substring(i+1, inStr.length());	// from first @
		int j = inStr.indexOf(EDISyntaxHelper.TOKEN_DELIMITER);						// next @
		if (j < 0) {									// no second tag
			throw new AdempiereException("EDI Line wronly configured. It needs to have a closing @: " + line);
		}

		return inStr.substring(0, j);
	}
}
