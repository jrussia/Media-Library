package media;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.w3c.dom.Element;

/***
 * CSC 478
 * Team2
 * ISBNDBNode.java
 * Purpose: XML Node for ISBNDB.com.
 * (Requirements 1.3.0 - 1.3.7)
 * 
 * @author Karissa (Nash) Stisser, Jeremy Egner, Yuji Tsuzuki
 * @version 0.2.0 4/15/2015
 */
public class ISBNDBNode extends XMLNode {
	
	// Store the unique genre strings returned from ISBN
	private static final HashMap<String, String> genres = new HashMap<String, String>();
	
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
	 * 
	 * @param xml	ISBNDB.com XML response in string format
	 * @throws Exception	[1] The ISBN returned no results
	 * 						[2] Unexpected error
	 */
	public ISBNDBNode(String xml) throws Exception {
		e = getElementFromXml(xml, "BookData");
	}

	/**
	 * Get the book's title.
	 * (Requirement 1.2.1)
	 * 
	 * @return	The book's title, if we can get it, otherwise an empty string
	 */
	public String getTitle() {
		String title = "";
		try {
			title = e.getElementsByTagName("Title").item(0).getTextContent();
		} catch (Exception e) {
			// return an empty string
		}
		return title;
	}

	/**
	 * Get the book's ISBN number
	 * 
	 * @return	The book's ISBN number. ISBN13 if available, otherwise ISBN10. If we can't find it,
	 * 			return an empty string.
	 */
	public String getISBN() {
		String isbn = "";
		if (e.getAttribute("isbn13") != "")
			isbn = e.getAttribute("isbn13");
		else
			isbn = e.getAttribute("isbn");
		return isbn;
	}

	/**
	 * Get the book's copyright year
	 * (Requirement 1.2.2)
	 * 
	 * @return	copyright year if we can parse it, otherwise an empty string 
	 */
	public String getYear() {
		String publisher = "";
		try {
			publisher = parseYearFromPublisher(e.getElementsByTagName("PublisherText").item(0).getTextContent());
		} catch (Exception e) {
			// return an empty string
		}
		return publisher;
	}
	

	/**
	 * Get the book's length
	 * (Requirement 1.2.3)
	 * 
	 * @return	the book's length in pages if we can parse it, otherwise an empty string  
	 */
	public String getLength() {
		String length = "";
		try {
			Element details = getElemFromTag("Details");
			length = parseLengthFromDetails(details.getAttribute("physical_description_text"));
		} catch (Exception e) {
			// return an empty string
		}
		return length;
	}

	/**
	 * Get the book's genre
	 * (Requirement 1.3.6)
	 * 
	 * @return	book's genre if we can parse it, otherwise return an empty string
	 */
	public String getGenre() {
		String genre = "";
		try {
			Element details = getElemFromTag("Details");
			getGenreFromDetails(details.getAttribute("dewey_decimal"));
		} catch (Exception e) {
			// return an empty string
		}
		return genre;
	}
	
	/**
	 * Get the book's author
	 * (Requirement 1.3.5)
	 * 
	 * @return	The book's author if we can parse it, otherwise an empty string
	 */
	public String getAuthor() {
		String author = "";
		try {
			author = parseAuthorFromAuthorsText(e.getElementsByTagName("AuthorsText").item(0).getTextContent());
		} catch (Exception e) {
			// return an empty string
		}
		return author;
	}
	
	/**
	 * Get the book's plot
	 * (Requirement 1.3.3)
	 * 
	 * @return	The book's plot if we can parse it, otherwise an empty string
	 */
	public String getPlot() {
		String plot = "";
		try {
			plot = e.getElementsByTagName("Summary").item(0).getTextContent();
		} catch (Exception e) {
			// return an empty string
		}
		return plot;
	}

	/**
	 * Get the book's cover
	 * (Requirement 1.3.7)
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
	 * Get the published year from text content
	 * (Requirement 1.3.2)
	 * 
	 * @param textContent	"<city> : <publisher>, <year>, c<copyright year>."
	 * @return	copyright year
	 * @throws Exception 
	 */
	// may be too fragile, maybe regex would be better? need to test more.
	private String parseYearFromPublisher(String textContent) throws Exception {
		String year = "";
		Pattern yearPattern = Pattern.compile("\\d{4}");
		Matcher m = yearPattern.matcher(textContent);
		if (m.find()) {
			year = m.group(0);
		}
		else {
			year = parseYearFromEditionInfo(getElemFromTag("Details").getAttribute("edition_info")); 
		}
		return year;
	}

	/**
	 * Parse the year out of text content. Will find the first year in the parameter formatted
	 * as XXXX, where each X is a number 0-9.
	 * (Requirement 1.3.2)
	 * 
	 * @param textContent	the text string to parse
	 * @return	the year as a string if we're able to parse it, otherwise an empty string
	 */
	private String parseYearFromEditionInfo(String textContent) {
		String year = "";
		Pattern yearPattern = Pattern.compile("\\d{4}");
		Matcher m = yearPattern.matcher(textContent);
		if (m.find()) {
			year = m.group(0);
		}
		return year;
	}

	/**
	 * Get the length, in pages, of a book, if it appears in the string parmameter as "X p.",
	 * where X is an integer
	 * (Requirement 1.3.4)
	 * 
	 * @param attribute		"<pages> p. : <illustrated?> ; <thickness> cm."
	 * @return	pages as a string, if we're able to parse it, otherwise return an empty string
	 */
	private String parseLengthFromDetails(String attribute) {
		String length = "";
		try {
			length = attribute.split(" p.")[0];
		} catch (Exception e) {
			// return an empty string;
		}
		return length;
	}

	/**
	 * Parse the genre from a string where genre appears in brackets, as in "[Genre]".
	 * If the string does not contain brackets, this will return the Dewey attribute via getDewey()
	 * (Requirement 1.3.6)
	 * 
	 * @param attribute		"[<Genre Abbreviation>]"
	 * @return	the genre if it's possible to parse out, otherwise returns an empty string
	 */
	private String getGenreFromDetails(String attribute) {
		String genre;
		if (attribute.contains("["))
			genre = genres.get(attribute);
		else
			genre = getDewey(attribute);
		
		if (genre == null)
			genre = "";
		return genre;
	}
	
	/**
	 * Get a simple dewey decimal category (genre) for the passed string parameter,
	 * where the dewey decimal number is in the following string: "Dewey attribute: /X.Y" where X
	 * is the dewey decimal value you want to look for.
	 * (Requirement 1.3.6)
	 * 
	 * @param attribute	the string to parse
	 * @return	the dewey decimal category (genre) in the string if possible, otherwise returns an empty string
	 */
	private String getDewey(String attribute) {
		String dewey = "";
			try {
				dewey = deweySimple.get(attribute.split("\\.")[0].split("/")[0].substring(0,  1));
			} catch (Exception e) {
				// just return an empty string
			}
		return dewey;
	}

	/**
	 * Parse the author from XML data.
	 * (Requirement 1.2.6)
	 * 
	 * @param textContent	"by <author>; illustrations by <illustrator>"
	 * @return		the author
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
}
