package media;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class ISBNDBNode {
	private Element e;
	Document doc;
	
	public ISBNDBNode(String xml) throws Exception {
		e = getElementFromXml(xml);
	}

	private Element getElementFromXml(String xml) throws Exception {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		InputSource is = new InputSource(new StringReader(xml));
		doc = dBuilder.parse(is);
		doc.getDocumentElement().normalize();
		
		return getElemFromTag("BookData");
	}

	private Element getElemFromTag(String string) throws Exception {
		NodeList nodeList = doc.getElementsByTagName(string);
		if (nodeList.getLength() < 1) {
			throw new java.lang.Exception("No books found matching that ISBN");
		}
		
		Node n = nodeList.item(0);
		if (n.getNodeType() != Node.ELEMENT_NODE) {
			throw new java.lang.Exception("Unexpected result from database lookup.");
		}
		
		return (Element) n;
	}

	public String getTitle() {
		return e.getElementsByTagName("Title").item(0).getTextContent();
	}

	public String getISBN() {
		String isbn = null;
		if (e.getAttribute("isbn13") != "")
			isbn = e.getAttribute("isbn13");
		else
			isbn = e.getAttribute("isbn");
		return isbn;
	}

	public String getYear() {
		return parseYearFromPublisher(e.getElementsByTagName("PublisherText").item(0).getTextContent());
	}

	private String parseYearFromPublisher(String textContent) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getLength() throws Exception {
		Element details = getElemFromTag("Details");
		return parseLengthFromDetails(details.getAttribute("physical_description_text"));
	}

	private String parseLengthFromDetails(String attribute) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getGenre() throws Exception {
		Element details = getElemFromTag("Details");
		return getGenreFromDetails(details.getAttribute("dewey_decimal"));
	}

	private String getGenreFromDetails(String attribute) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getAuthor() {
		return parseAuthorFromAuthorsText(e.getElementsByTagName("AuthorsText").item(0).getTextContent());
	}

	private String parseAuthorFromAuthorsText(String textContent) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getPlot() {
		return e.getElementsByTagName("Summary").item(0).getTextContent();
	}

	public byte[] getCover() {
		// TODO Auto-generated method stub
		return null;
	}
}
