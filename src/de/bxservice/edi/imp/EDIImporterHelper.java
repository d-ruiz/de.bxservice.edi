package de.bxservice.edi.imp;

import java.util.List;

import org.adempiere.exceptions.AdempiereException;

import de.bxservice.edi.model.MEDIFormat;
import de.bxservice.edi.model.MEDILine;
import de.bxservice.edi.model.MEDISection;

public class EDIImporterHelper {
	
	private final static String DEFAULT_SEPARATOR = "'"; 
	private String lineSeparator;
	private MEDIFormat ediFormat;
	private EDIImportLineHandler ediLinesHandler;
	
	public EDIImporterHelper(MEDIFormat ediFormat) {
		this.ediFormat = ediFormat;
		setLineSeparator();
	}
	
	private void setLineSeparator() {
		lineSeparator = ediFormat.getEDI_LineSeparator() != null ? ediFormat.getEDI_LineSeparator() : DEFAULT_SEPARATOR;
	}
	
	public void importLines(List<String> ediLines) {
		checkFileValidity(ediLines);
		ediLinesHandler = new EDIImportLineHandler(ediLines);
		
		//Loop in case of multiple orders
		checkMessages();
		
		/*for (ValueNamePair vl : ediLinesHandler.getColumnNameValues()) {
			System.out.println(vl.getName() + " = " + vl.getValue());
		}*/
		//Attach File to Order or Process instance
	}
	
	private void checkFileValidity(List<String> ediLines) {
		if (ediLines == null || ediLines.size() == 0)
			throw new AdempiereException("No lines");
		
		for (String line : ediLines) {
			if (!isValidLine(line))
				throw new AdempiereException("Invalid line: " + line);
		}
	}
	
	private void checkMessages() {
		List<MEDISection> msgSections = ediFormat.getMessageSections();
		
		for (MEDISection msgSection : msgSections) {
			for (MEDILine line : msgSection.getLines()) {
				String ediFormatLine = line.getMsgText();
				System.out.println(ediLinesHandler.getCurrentFileLine() + " - " + ediFormatLine); //break line

				if (ediLinesHandler.isParseableLine(ediFormatLine)) { 
					ediLinesHandler.parseLine(ediFormatLine);
				} else if (!ediLinesHandler.isValidSegment(ediFormatLine)) { //Add optional conditional
					if (!line.isBXS_IsOptional())
						throw new AdempiereException("Line: cannot be parsed: " + ediFormatLine + " - " + ediLinesHandler.getCurrentFileLine()); // Expected x found y
					continue;
				}
				ediLinesHandler.nextEDILine();
			}
		}
	}
	
	private boolean isValidLine(String line) {
		String lastCharachter = line.substring(line.length() - 1);
		return lineSeparator.equals(lastCharachter);
	}
	
	// DocumentParser? 
	// One class that manages the iteration, retrieving a line and removing the ' at the end
	//
	// One class that does the matching and checking if it's alright
	// One class that creates the corresponding value
	// Inform if it fails, mark such orders as failing orders 
}
