package media;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/***
 * CSC 478
 * Team2
 * XMLNode.java
 * Purpose: Generic XML Node
 * 
 * @author Karissa (Nash) Stisser, Jeremy Egner, Yuji Tsuzuki
 * @version 0.2.0 4/22/2015
 */
public class XMLNode {
	protected Element e;
	protected Document doc;
	
	/**
	 * Create base queryable element from XML
	 * 
	 * @param xml		xaml to parse
	 * @param element	element to extract
	 * @return		 	a queryable element
	 * @throws Exception	if it's unable to construct an element
	 */
	protected Element getElementFromXml(String xml, String element) throws Exception {
		DocumentBuilderFactory docbuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docbuilderFactory.newDocumentBuilder();
		InputSource is = new InputSource(new StringReader(xml));
		doc = docBuilder.parse(is);
		doc.getDocumentElement().normalize();
		
		return getElemFromTag(element);
	}
	
	/**
	 * Lookup an element in an XML node
	 * @param string	The element to look for
	 * @return			The element
	 * @throws Exception	[1] There are no results for the requested element
	 * 						[2] Unexpected error 
	 */
	protected Element getElemFromTag(String string) throws Exception {
		NodeList nodeList = doc.getElementsByTagName(string);
		if (nodeList.getLength() < 1) {
			throw new java.lang.Exception("No items found");
		}
		
		Node n = nodeList.item(0);
		if (n.getNodeType() != Node.ELEMENT_NODE) {
			throw new java.lang.Exception("Unexpected result from database lookup.");
		}
		
		return (Element) n;
	}
}
