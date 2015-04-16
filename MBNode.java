package media;

import org.w3c.dom.Element;

/***
 * CSC 478
 * Team2
 * MBNode.java
 * Purpose: XML Node for musicbrainz.org.
 * 
 * @author Karissa (Nash) Stisser, Jeremy Egner, Yuji Tsuzuki
 * @version 0.2.0 4/15/2015
 */
public class MBNode extends XMLNode {

	/**
	 * Constructor
	 * @param xml	monsterbrainz.com XML response in string format
	 * @throws Exception	[1] The UPC returned no results
	 * 						[2] Unexpected error
	 */
	public MBNode(String xml) throws Exception {
		e = getElementFromXml(xml, "release");
	}

	/**
	 * 
	 * @return
	 */
	public String getUPC() {
		// TODO figure out if we need to pad UPCs with 0s
		return e.getElementsByTagName("barcode").item(0).getTextContent();
	}

	/**
	 * 
	 * @return
	 */
	public String getTitle() {
		return e.getElementsByTagName("title").item(0).getTextContent();
	}

	/**
	 * 
	 * @return
	 */
	public String getGenre() {
		// TODO Verify that this is good enough
		// TODO Use a table
		String genre;
		Element tags;
		try {
			tags = getElemFromTag("tag-list");
			genre = tags.getElementsByTagName("tag").item(0).getTextContent();
		} catch (Exception e) {
			genre = "";
		}
		return genre;
	}

	/**
	 * 
	 * @return
	 */
	public byte[] getCover() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getAuthor() throws Exception {
		Element artist = getElemFromTag("artist-credit");
		return artist.getElementsByTagName("name").item(0).getTextContent();
	}

}
