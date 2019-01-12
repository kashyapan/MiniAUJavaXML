package utils;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class XMLUtil {
	
	static DocumentBuilder documentBuilder ;
	
	static XMLUtil xmlUtil ; 

	private XMLUtil() throws ParserConfigurationException {
		
		DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
		documentBuilder = documentFactory.newDocumentBuilder();
		
	}

	public static XMLUtil getInstance() throws ParserConfigurationException {
		
		if( xmlUtil == null ) {
			xmlUtil = new XMLUtil() ;
		}
		return xmlUtil ;
		
	}
	
	public DocumentBuilder getDocumentBuilder() {
		return documentBuilder ;
	}

}
