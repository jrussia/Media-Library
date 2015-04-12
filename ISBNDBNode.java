package media;

import java.io.StringReader;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;


//	TODO: add comments
// 	TODO: move public methods to the top
public class ISBNDBNode extends XMLNode {

	private static final HashMap<String, String> genres = new HashMap<String, String>();
	// TODO: Add all genres
	static {
		genres.put("[Fic]", "Fiction");
	}
	
	private static final HashMap<String, String> dewey = new HashMap<String, String>();
	// TODO: Add all genres
	static {
		dewey.put("", "");
		dewey.put("000", "Computer science, information & general works");
		dewey.put("001", "Knowledge");
		dewey.put("002", "The book");
		dewey.put("003", "Systems");
		dewey.put("004", "Computer science");
		dewey.put("005", "Computer programming, programs & data");
		dewey.put("006", "Special computer methods");
		
		dewey.put("650", "Management & public relations");
		dewey.put("651", "Office services");
		dewey.put("652", "Processes of written communication");
		dewey.put("653", "Shorthand");
		dewey.put("657", "Accounting");
		dewey.put("658", "General management");
		dewey.put("659", "Advertising & public relations");
		
		dewey.put("810", "American literature in English");
		dewey.put("811", "American poetry in English");
		dewey.put("812", "American drama in English");
		dewey.put("813", "American fiction in English");
		dewey.put("814", "American essays in English");
		dewey.put("815", "American speeches in English");
		dewey.put("816", "American letters in English");
		dewey.put("817", "American humor & satire in English");
		dewey.put("818", "American miscellaneous writings in English");
		
		dewey.put("890", "Literatures of other specific languages and language families");
		dewey.put("891", "East Indo-European & Celtic literatures");
		dewey.put("892", "Afro-Asiatic literatures");
		dewey.put("893",  "Non-Semitic Afro-Asiatic literatures");
		dewey.put("894", "Literatures of Altaic, Uralic, Hyperborean, Dravidian languages;"
				+ " literatures of miscellaneous languages of Southeast Asia");
		dewey.put("895", "Literatures of East & Southeast Asia");
		dewey.put("896", "African literatures");
		dewey.put("897", "Literatures of North American native languages");
		dewey.put("898", "Literatures of South American native languages");
		dewey.put("899", "Literatures of non-Austronesian languages of Oceania, of Austronesian languages" 
				+ ", of miscellaneous languages");
	}
	
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
	 */
	public String getYear() {
		return parseYearFromPublisher(e.getElementsByTagName("PublisherText").item(0).getTextContent());
	}

	/**
	 * 
	 * @param textContent	"<city> : <publisher>, <year>, c<copyright year>."
	 * @return	copyright year
	 */
	// may be too fragile, maybe regex would be better? need to test more.
	private String parseYearFromPublisher(String textContent) {
		Pattern yearPattern = Pattern.compile("\\d{4}");
		Matcher m = yearPattern.matcher(textContent);
		if (m.find()) {
			return m.group(0);
		}
		return "";
		/*
		// TODO: may need to use edition_info
		String year;
		textContent.
		if (textContent.contains(", c")) {
			String[] ary = textContent.split(", c");
			if (ary.length < 2) year = "";
			else year = ary[1].substring(0, 4);
		}
		else {
			if (textContent.contains("; ")) {
				
			}
		}
		return ary[1].substring(0, 4); // Probably a better way to do this
		*/ 
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
		String genre;
		System.out.println("Genre attribute: " + attribute);
		if (attribute.contains("["))
			genre = genres.get(attribute);
		else
			genre = getDewey(attribute);
		return genre;
	}
	
	private String getDewey(String attribute) {
		System.out.println("Dewey attribute: " + attribute.split("\\.")[0]);
		return dewey.get(attribute.split("\\.")[0].split("/")[0]);
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
