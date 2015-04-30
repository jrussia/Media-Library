package media;

/***
 * CSC 478
 * Team2
 * EANMovieNode.java
 * Purpose: XML Node for eandata.com
 * (Requirements 1.2.0, 1.2.1, 5.0.0, 5.1.0)
 * 
 * @author Karissa (Nash) Stisser, Jeremy Egner, Yuji Tsuzuki
 * @version 0.2.0 4/21/2015
 */
public class EANMovieNode extends XMLNode {

	/**
	 * Constructor
	 * 
	 * @param xml	the EAN XML to create the node from
	 * @throws Exception	if we were unable to create the node
	 */
	public EANMovieNode(String xml) throws Exception {
		e = getElementFromXml(xml, "product");
	}

	/**
	 * Get the movie's title
	 * (Requirement 1.2.1)
	 * 
	 * @return	the movie's title
	 */
	public String getTitle() {
		return e.getElementsByTagName("product").item(0).getTextContent();
	}
	
}
