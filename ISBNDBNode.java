package media;

import java.io.StringReader;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;


//	TODO: add comments
// 	TODO: move public methods to the top
public class ISBNDBNode {
	private Element e;
	private static final HashMap<String, String> genres = new HashMap<String, String>();
	// TODO: Add all genres
	static {
		genres.put("[Fic]", "Fiction");
	}
	Document doc;
	
	/**
	 * Constructor
	 * @param xml	ISBNDB.com XML response in string format
	 * @throws Exception	[1] The ISBN returned no results
	 * 						[2] Unexpected error
	 */
	public ISBNDBNode(String xml) throws Exception {
		e = getElementFromXml(xml);
	}

	/**
	 * Create base queryable element from XML
	 * @param xml	from constructor
	 * @return		queryable element
	 * @throws Exception	from getElemFromTag
	 */
	private Element getElementFromXml(String xml) throws Exception {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		InputSource is = new InputSource(new StringReader(xml));
		doc = dBuilder.parse(is);
		doc.getDocumentElement().normalize();
		
		return getElemFromTag("BookData");
	}

	/**
	 * Lookup an element in an XML node
	 * @param string	The element to look for
	 * @return			The element
	 * @throws Exception	[1] There are no results for the requested element
	 * 						[2] Unexpected error 
	 */
	private Element getElemFromTag(String string) throws Exception {
		NodeList nodeList = doc.getElementsByTagName(string);
		if (nodeList.getLength() < 1) {
			throw new java.lang.Exception("No books found matching ISBN");
		}
		
		Node n = nodeList.item(0);
		if (n.getNodeType() != Node.ELEMENT_NODE) {
			throw new java.lang.Exception("Unexpected result from database lookup.");
		}
		
		return (Element) n;
	}

	/**
	 * 
	 * @return	The book's title
	 */
	public String getTitle() {
		//TODO: add sentence case
		return e.getElementsByTagName("Title").item(0).getTextContent();
	}

	/**
	 * 
	 * @return	The book's ISBN number. ISBN13 if available, otherwise ISBN10
	 */
	public String getISBN() {
		String isbn = null;
		if (e.getAttribute("isbn13") != "")
			isbn = e.getAttribute("isbn13");
		else
			isbn = e.getAttribute("isbn");
		return isbn;
	}

	/**
	 * 
	 * @return	copyright year
	 */
	public String getYear() {
		return parseYearFromPublisher(e.getElementsByTagName("PublisherText").item(0).getTextContent());
	}

	/**
	 * 
	 * @param textContent	"City : Publisher, Year, cCopyRightYear."
	 * @return	CopyRightYear
	 */
	// may be too fragile, maybe regex would be better? need to test more.
	private String parseYearFromPublisher(String textContent) {
		return textContent.split(", c")[1].substring(0, 4); // Probably a better way to do this 
	}

	/**
	 * 
	 * @return	the book's length
	 * @throws Exception	[1] There are isn't a Details element in the XML
	 * 						[2] Unexpected error 
	 */
	public String getLength() throws Exception {
		Element details = getElemFromTag("Details");
		return parseLengthFromDetails(details.getAttribute("physical_description_text"));
	}

	/**
	 * 
	 * @param attribute		"<pages> p. : <illustrated?> ; <thickness> cm."
	 * @return	pages
	 */
	private String parseLengthFromDetails(String attribute) {
		return attribute.split(" p.")[0];
	}

	/**
	 * 
	 * @return	book's genre
	 * @throws Exception	[1] There are isn't a Details element in the XML
	 * 						[2] Unexpected error
	 */
	public String getGenre() throws Exception {
		Element details = getElemFromTag("Details");
		return getGenreFromDetails(details.getAttribute("dewey_decimal"));
	}

	/**
	 * 
	 * @param attribute		"[<Genre Abbreviation>]"
	 * @return	Genre
	 */
	private String getGenreFromDetails(String attribute) {
		return genres.get(attribute);
	}

	/**
	 * 
	 * @return	The book's author
	 */
	public String getAuthor() {
		return parseAuthorFromAuthorsText(e.getElementsByTagName("AuthorsText").item(0).getTextContent());
	}

	/**
	 * 
	 * @param textContent	"by <author>; illustrations by <illustrator>"
	 * @return
	 */
	private String parseAuthorFromAuthorsText(String textContent) {
		return textContent.split("by ")[1].split("; ")[0];
	}

	/**
	 * 
	 * @return	The book's plot
	 */
	public String getPlot() {
		return e.getElementsByTagName("Summary").item(0).getTextContent();
	}

	public byte[] getCover() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Tests
	 * @throws Exception
	 * TODO: Test multiple ISBNs, different books, etc. 
	
	public static void main(String[] args) throws Exception {
		String testXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><ISBNdb server_time=\"2015-03-26T18:27:44Z\"><BookList total_results=\"1\" page_size=\"10\" page_number=\"1\" shown_results=\"1\"><BookData book_id=\"harry_potter_and_the_sorcerers_stone_a19\" isbn=\"0439708184\" isbn13=\"9780439708180\"><Title>Harry Potter and the sorcerer's stone</Title><TitleLong></TitleLong><AuthorsText>by J. K. Rowling; illustrations by Mary GrandPr?</AuthorsText><PublisherText publisher_id=\"scholastic\">New York : Scholastic, 1999, c1997.</PublisherText><Details change_time=\"2008-12-24T16:36:29Z\" price_time=\"2012-12-28T05:02:05Z\" edition_info=\"\" language=\"eng\" physical_description_text=\"312 p. : ill. ; 20 cm.\" lcc_number=\"\" dewey_decimal_normalized=\"\" dewey_decimal=\"[Fic]\" /><Summary>Rescued from the outrageous neglect of his aunt and uncle, a young boy with a great destiny proves his worth while attending Hogwarts School for Wizards and Witches.</Summary><Notes>\"First Scholastic trade paperback printing, September 1999\"--T.p. verso.Sequel: Harry Potter and the Chamber of Secrets.</Notes><UrlsText></UrlsText><AwardsText></AwardsText><Authors><Person person_id=\"grandprn_mary\">GrandPr?, Mary</Person></Authors></BookData></BookList></ISBNdb>";
		ISBNDBNode n = new ISBNDBNode(testXml);
		System.out.println("Testing books...");
		System.out.println("Author: "+ n.getAuthor());
		System.out.println("Genre: " + n.getGenre());
		System.out.println("ISBN: " + n.getISBN());
		System.out.println("Length: " + n.getLength());
		System.out.println("Plot: " + n.getPlot());
		System.out.println("Title: " + n.getTitle());
		System.out.println("Year: " + n.getYear());
		//TODO: add cover test
	}
	*/
	
}
