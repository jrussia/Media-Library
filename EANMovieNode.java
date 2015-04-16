package media;

/***
 * CSC 478
 * Team2
 * EANMovieNode.java
 * Purpose: XML Node for eandata.com
 * 
 * @author Karissa (Nash) Stisser, Jeremy Egner, Yuji Tsuzuki
 * @version 0.2.0 4/15/2015
 */
public class EANMovieNode extends XMLNode {

	/**
	 * 
	 * @param xml
	 * @throws Exception
	 */
	public EANMovieNode(String xml) throws Exception {
		e = getElementFromXml(xml, "product");
	}

	/**
	 * 
	 * @return
	 */
	public String getTitle() {
		return e.getElementsByTagName("product").item(0).getTextContent();
	}
	
}
