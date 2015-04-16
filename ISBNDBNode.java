package media;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;

//	TODO: add comments
// 	TODO: move public methods to the top
/***
 * CSC 478
 * Team2
 * ISBNDBNode.java
 * Purpose: XML Node for ISBNDB.com.
 * 
 * @author Karissa (Nash) Stisser, Jeremy Egner, Yuji Tsuzuki
 * @version 0.2.0 4/15/2015
 */
public class ISBNDBNode extends XMLNode {
	
	// Store the unique genre strings returned from ISBN
	private static final HashMap<String, String> genres = new HashMap<String, String>();
	// TODO: Add all genres
	static {
		genres.put("[Fic]", "Fiction");
	}
	
	// Store simple dewey decimal values
	private static final HashMap<String, String> deweySimple = new HashMap<String, String>();
	static {
		deweySimple.put("", "");
		deweySimple.put("0", "Computer science, knowledge & systems");
		deweySimple.put("1", "Philosophy and psychology");
		deweySimple.put("2", "Religion");
		deweySimple.put("3", "Social sciences");
		deweySimple.put("4", "Language");
		deweySimple.put("5", "Science");
		deweySimple.put("6", "Technology");
		deweySimple.put("7", "Arts & recreation");
		deweySimple.put("8", "Literature");
		deweySimple.put("9", "History & geography");
	}
	
	/**
	 * 
	 * Public methods
	 * 
	 */
	
	/**
	 * Constructor
	 * @param xml	ISBNDB.com XML response in string format
	 * @throws Exception	[1] The ISBN returned no results
	 * 						[2] Unexpected error
	 */
	public ISBNDBNode(String xml) throws Exception {
		e = getElementFromXml(xml, "BookData");
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
	 * @throws Exception 
	 * @throws DOMException 
	 */
	public String getYear() throws DOMException, Exception {
		return parseYearFromPublisher(e.getElementsByTagName("PublisherText").item(0).getTextContent());
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
	 * @return	The book's author
	 */
	public String getAuthor() {
		return parseAuthorFromAuthorsText(e.getElementsByTagName("AuthorsText").item(0).getTextContent());
	}
	
	/**
	 * 
	 * @return	The book's plot
	 */
	public String getPlot() {
		return e.getElementsByTagName("Summary").item(0).getTextContent();
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
	 * Private methods
	 * 
	 */
	
	/**
	 * 
	 * @param textContent	"<city> : <publisher>, <year>, c<copyright year>."
	 * @return	copyright year
	 * @throws Exception 
	 */
	// may be too fragile, maybe regex would be better? need to test more.
	private String parseYearFromPublisher(String textContent) throws Exception {
		Pattern yearPattern = Pattern.compile("\\d{4}");
		Matcher m = yearPattern.matcher(textContent);
		if (m.find()) {
			return m.group(0);
		}
		return parseYearFromEditionInfo(getElemFromTag("Details").getAttribute("edition_info"));
	}

	/**
	 * 
	 * @param textContent
	 * @return
	 */
	private String parseYearFromEditionInfo(String textContent) {
		Pattern yearPattern = Pattern.compile("\\d{4}");
		Matcher m = yearPattern.matcher(textContent);
		if (m.find()) {
			return m.group(0);
		}
		return "";
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
	 * @param attribute		"[<Genre Abbreviation>]"
	 * @return	Genre
	 */
	private String getGenreFromDetails(String attribute) {
		String genre;
		System.out.println("Genre attribute: " + attribute);
		if (attribute.contains("["))
			genre = genres.get(attribute);
		else
			genre = getDewey(attribute);
		return genre;
	}
	
	/**
	 * 
	 * @param attribute
	 * @return
	 */
	private String getDewey(String attribute) {
		System.out.println("Dewey attribute: " + attribute.split("\\.")[0]);
		return deweySimple.get(attribute.split("\\.")[0].split("/")[0].substring(0,  1));
	}

	/**
	 * 
	 * @param textContent	"by <author>; illustrations by <illustrator>"
	 * @return
	 */
	private String parseAuthorFromAuthorsText(String textContent) {
		String author;
		if (textContent.contains("by ")) {
			String[] ary = textContent.split("by ");
			if (ary.length < 2) author = "";
			else author = ary[1].split("; ")[0];
		}
		else {
			author = textContent.split(",")[0];
		}
		return author;
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
