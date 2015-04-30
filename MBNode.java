package media;

import org.w3c.dom.Element;

/***
 * CSC 478
 * Team2
 * MBNode.java
 * Purpose: XML Node for musicbrainz.org.
 * 
 * @author Karissa (Nash) Stisser, Jeremy Egner, Yuji Tsuzuki
 * @version 0.2.0 4/21/2015
 */
public class MBNode extends XMLNode {

	/**
	 * Constructor
	 * 
	 * @param xml	monsterbrainz.com XML response in string format
	 * @throws Exception	[1] The UPC returned no results
	 * 						[2] Unexpected error
	 */
	public MBNode(String xml) throws Exception {
		e = getElementFromXml(xml, "release");
	}

	/**
	 * Return the UPC stored in this node.
	 * 
	 * @return	the UPC code, as a string
	 */
	public String getUPC() {
		return e.getElementsByTagName("barcode").item(0).getTextContent();
	}

	/**
	 * Return the title stored in this node.
	 * (Requirement 1.4.0, 1.4.1)
	 * 
	 * @return	the title
	 */
	public String getTitle() {
		return e.getElementsByTagName("title").item(0).getTextContent();
	}

	/**
	 * Return the genre stored in this node.
	 * MusicBrainz doesn't really store this data, but sometimes we can get it from the tags.
	 * (Requirements 1.4.0, 1.4.3)
	 * 
	 * @return	the genre, as a string
	 */
	public String getGenre() {
		// TODO See if we can get this from somewhere else
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
	 * Return the CD's cover image.
	 * (Requirements 1.4.0, 1.4.4)
	 * 
	 * @return	the CD's cover image, as a byte array.
	 */
	public byte[] getCover() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Return this node's artist or group.
	 * (Requirements 1.4.0, 1.4.4)
	 * 
	 * @return	the artist, or an empty string if we couldn't load it
	 */
	public String getAuthor() {
		String author = "";
		try {
			Element artist = getElemFromTag("artist-credit");
			author = artist.getElementsByTagName("name").item(0).getTextContent();
		} catch (Exception e) {
			// return an empty string
		}
		return author;
	}
}
