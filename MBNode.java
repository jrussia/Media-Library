package media;

import org.w3c.dom.Element;

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

	public String getUPC() {
		// TODO figure out if we need to pad UPCs with 0s
		return e.getElementsByTagName("barcode").item(0).getTextContent();
	}

	public String getTitle() {
		// TODO Auto-generated method stub
		return e.getElementsByTagName("title").item(0).getTextContent();
	}

	public String getGenre() throws Exception {
		// TODO Verify that this is good enough
		// TODO Use a table
		Element tags = getElemFromTag("tag-list");
		return tags.getElementsByTagName("tag").item(0).getTextContent();
	}

	public byte[] getCover() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getAuthor() throws Exception {
		// TODO Auto-generated method stub
		Element artist = getElemFromTag("artist-credit");
		return artist.getElementsByTagName("name").item(0).getTextContent();
	}

}
