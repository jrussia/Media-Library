package media;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Node;
import org.xml.sax.SAXException;


public class InternetLookup {

	/**
	 * Example input:
	 * 	ISBN - "0439708184"
	 * Example XML response:
	 *  <?xml version="1.0" encoding="UTF-8"?>
		<ISBNdb server_time="2015-03-24T01:59:44Z">
			<BookList total_results="1" page_size="10" page_number="1" shown_results="1">
				<BookData book_id="harry_potter_and_the_sorcerers_stone_a19" isbn="0439708184" isbn13="9780439708180">
					<Title>Harry Potter and the sorcerer's stone</Title>
					<TitleLong></TitleLong>
					<AuthorsText>by J. K. Rowling; illustrations by Mary GrandPr?</AuthorsText>
					<PublisherText publisher_id="scholastic">New York : Scholastic, 1999, c1997.</PublisherText>
					<Details change_time="2008-12-24T16:36:29Z" price_time="2012-12-28T05:02:05Z" edition_info="" language="eng" physical_description_text="312 p. : ill. ; 20 cm." lcc_number="" dewey_decimal_normalized="" dewey_decimal="[Fic]" />
					<Summary>Rescued from the outrageous neglect of his aunt and uncle, a young boy with a great destiny proves his worth while attending Hogwarts School for Wizards and Witches.</Summary>
					<Notes>"First Scholastic trade paperback printing, September 1999"--T.p. verso.Sequel: Harry Potter and the Chamber of Secrets.</Notes>
					<UrlsText></UrlsText>
					<AwardsText></AwardsText>
					<Authors>
						<Person person_id="grandprn_mary">GrandPr?, Mary</Person>
					</Authors>
				</BookData>
			</BookList>
		</ISBNdb>
	 * @param ISBN
	 * @return
	 * @throws Exception 
	 */
	public static Book lookupBook(String ISBN) throws Exception {
		//lookup ISBN on openLibrary
		// load the user's API key 
		// String key = getAPIKey();
		String key = "M9RSJ8EI";
		String request = "http://www.isbndb.com/api/books.xml?access_key="+key+"&results=texts&results=authors&results=details&index1=isbn&value1="+ISBN;
		String response = getResponse(request);
		return getBookfromXML(response);	
	}
	
	private static Book getBookfromXML(String xmlStr) throws Exception {
		ISBNDBNode node = new ISBNDBNode(xmlStr);
		String title = node.getTitle();
		String isbn = node.getISBN();
		String year = node.getYear();
		String length = node.getLength();
		String genre = node.getGenre();
		String author = node.getAuthor();
		String plot = node.getPlot();
		byte[] cover = node.getCover();
		return new Book(title, author, isbn, genre, cover, year, plot, length);
	}
	

	/**
	 * Submit a request to an external API.
	 * @param request	URI to request a response from
	 * @return	the response received
	 * @throws IOException
	 */
	private static String getResponse(String request) throws IOException {
		URL requestURL = new URL(request);
		HttpURLConnection conn = (HttpURLConnection) requestURL.openConnection();
		
		if (conn.getResponseCode() != 200) {
			throw new IOException(conn.getResponseMessage());
		}
		
		BufferedReader rd = new BufferedReader(
				new InputStreamReader(conn.getInputStream()));
		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = rd.readLine()) != null) {
			sb.append(line);
		}
		
		conn.disconnect();	
		return sb.toString();
	}
	
	public static Movie lookupMovie(String UPC) {
		return null;
	}
	
	public static CD lookupCD(String ISBN) {
		return null;
	}
	
	public static void main(String[] args) throws Exception {
		Book b = lookupBook("0439708184");
	}
}
