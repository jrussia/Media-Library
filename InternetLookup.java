package media;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Node;
import org.xml.sax.SAXException;

// TODO: how should we handle an internet timeout/failed connections
public class InternetLookup {

	private static final String rtKey = "9jwvxuyjwj85xszw2rfkxmbj";
	
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
	 * @return the book requested, or null if no such book was found
	 * @throws IOException 
	 */
	public static Book lookupBook(String ISBN) throws IOException {
		//lookup ISBN on openLibrary
		// load the user's API key 
		// String key = getAPIKey();
		String key = "M9RSJ8EI";
		String request = "http://www.isbndb.com/api/books.xml?access_key="+key+"&results=texts&results=authors&results=details&index1=isbn&value1="+ISBN;
		String response = getResponse(request);
		//System.out.println(response);
		Book b = null;
		try {
			b = getBookfromXML(response);
		} catch (Exception e) {
			b = null;
		}
		return b;	
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
		conn.setRequestProperty("User-Agent","Media Library Moto/1.0.0 (egner@uis.edu)"); // be a Internet good citizen, not anonymous
		
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
	
	// TODO: rate limit this
	// TODO: hide keys in a config file
	public static Movie[] lookupMovie(String UPC) throws Exception {
		String eanKey = "B3D69949551489CF";
		String request = "http://eandata.com/feed/?v=3&keycode=" + eanKey + "&mode=xml&find=" + UPC;
		String response = getResponse(request);
		//System.out.println(response);
		EANMovieNode eanNode = new EANMovieNode(response);
		if (eanNode == null)
			return null;
		String title = eanNode.getTitle();
		//System.out.println(title);
		
		// search for top 10 matches on RottenTomatoes
		String requestRt = encodeQuery("http://api.rottentomatoes.com/api/public/v1.0/movies.json?apikey=" + rtKey + "&q=" + title);
		String responseRt = getResponse(requestRt);
		//System.out.println(responseRt);
		RottenNode rNode = new RottenNode(responseRt, UPC);
		return rNode.getTopTen();
	}
	
	public static String getRottenIDResponse(String id) throws IOException {
		String requestRt = encodeQuery("http://api.rottentomatoes.com/api/public/v1.0/movies/" + id + ".json.json?apikey=" + rtKey);
		return getResponse(requestRt);
	}
	
	private static String encodeQuery(String string) {
		String newString = string.replace(" ", "%20");
		return newString;
	}

	/**
	 * Example input: "0731454038720"
	 * <?xml version='1.0' encoding='UTF-8'?>
	 * <feed>
	 * 	<status>
	 * 		<version>3.1</version>
	 * 		<code>200</code>
	 * 		<message>free</message>
	 * 		<find>0731454038720</find>
	 * 		<run>0.3706</run>
	 * 	</status>
	 * 	<product>
	 * 		<attributes>
	 * 			<product>Styx - Greatest Hits</product>
	 * 			<description>CD</description>
	 * 			<price_new>5.6900</price_new>
	 * 			<price_new_extra>USD</price_new_extra>
	 * 			<price_new_extra_long>US Dollars</price_new_extra_long>
	 * 			<price_new_extra_id>537</price_new_extra_id>
	 * 			<price_used>0.6600</price_used>
	 * 			<price_used_extra>USD</price_used_extra>
	 * 			<price_used_extra_long>US Dollars</price_used_extra_long>
	 * 			<price_used_extra_id>537</price_used_extra_id>
	 * 			<sku>5373910</sku>
	 * 			<asin_com>B000002G3Y</asin_com>
	 * 			<category>56</category>
	 * 			<category_text>Music</category_text>
	 * 			<category_text_long>Electronics / Photography: A/V Media: Music</category_text_long>
	 * 			<width>5.5500</width>
	 * 			<width_extra>in</width_extra>
	 * 			<width_extra_long>inches</width_extra_long>
	 * 			<width_extra_id>501</width_extra_id>
	 * 			<height>0.4700</height>
	 * 			<height_extra>in</height_extra>
	 * 			<height_extra_long>inches</height_extra_long>
	 * 			<height_extra_id>501</height_extra_id>
	 * 			<length>4.9200</length>
	 * 			<length_extra>in</length_extra>
	 * 			<length_extra_long>inches</length_extra_long>
	 * 			<length_extra_id>501</length_extra_id>
	 * 			<weight>3.5200</weight>
	 * 			<weight_extra>oz</weight_extra>
	 * 			<weight_extra_long>ounces</weight_extra_long>
	 * 			<weight_extra_id>513</weight_extra_id>
	 * 			<long_desc>The first-ever compilation pulled from this Chicago progressive-bubblegum band&#039;s A&amp;M catalog, featuring such album-Rock radio fixtures as Come Sail Away; Lorelei; Babe; Blue Collar Man (Long Nights); The Best of Times; Too Much Time on My Hands; Renegade; Mr. Roboto; Don&#039;t Let It End , and more, plus a 1995 re-recording of Lady !</long_desc>
	 * 			<binding>Audio CD</binding>
	 * 			<similar>0074646569023, 0696998651821, 0827969448922, 0828768588925, 0886973334324</similar>
	 * 			<language>553</language>
	 * 			<language_text>en</language_text>
	 * 			<language_text_long>English</language_text_long>
	 * 		</attributes>
	 * 		<EAN13>0731454038720</EAN13>
	 * 		<UPCA>731454038720</UPCA>
	 * 		<barcode>
	 * 			<EAN13>http://eandata.com/image/0731454038720.png</EAN13>
	 * 			<UPCA>http://eandata.com/image/731454038720.png</UPCA>
	 * 		</barcode>
	 * 		<locked>0</locked>
	 * 		<modified>2015-03-26 15:33:23</modified>
	 * 		<image>http://eandata.com/image/products/073/145/403/0731454038720.jpg</image>
	 * 	</product>
	 * 	<company>
	 * 		<name>Decca Records - The Verve Music Group</name>
	 * 		<logo></logo>
	 * 		<url>www.vervemusicgroup.com</url>
	 * 		<address></address>
	 * 		<phone></phone>
	 * 		<locked>0</locked>
	 * 	</company>
	 * </feed>
	 * 
	 * <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
	 * <metadata created="2015-03-26T21:48:35.908Z" xmlns="http://musicbrainz.org/ns/mmd-2.0#" xmlns:ext="http://musicbrainz.org/ns/ext#-2.0">
	 * <release-list count="1" offset="0">
	 * 	<release id="7034d664-9b8d-4e8a-9b08-8b5f98d42c5d" ext:score="100">
	 * 		<title>Greatest Hits</title>
	 * 		<status>Official</status>
	 * 		<packaging>Jewel Case</packaging>
	 * 		<text-representation>
	 * 			<language>eng</language>
	 * 			<script>Latn</script>
	 * 		</text-representation>
	 * 		<artist-credit>
	 * 			<name-credit>
	 * 				<artist id="835a6d9c-fea0-4a71-ae52-9c4da946433a">
	 * 					<name>Styx</name>
	 * 					<sort-name>Styx</sort-name>
	 * 				</artist>
	 * 			</name-credit>
	 * 		</artist-credit>
	 * 		<release-group id="56b04bbd-6a8d-3894-ab49-c3602d8509a3" type="Compilation">
	 * 			<primary-type>Album</primary-type>
	 * 			<secondary-type-list>
	 * 				<secondary-type>Compilation</secondary-type>
	 * 			</secondary-type-list>
	 * 		</release-group>
	 * 		<date>1995-08-22</date>
	 * 		<country>US</country>
	 * 		<release-event-list><release-event>
	 * 		<date>1995-08-22</date>
	 * 		<area id="489ce91b-6658-3307-9877-795b68554c98">
	 * 			<name>United States</name>
	 * 			<sort-name>United States</sort-name>
	 * 			<iso-3166-1-code-list>
	 * 				<iso-3166-1-code>US</iso-3166-1-code>
	 * 			</iso-3166-1-code-list>
	 * 		</area>
	 * 	</release-event>
	 * </release-event-list>
	 * <barcode>731454038720</barcode>
	 * <asin>B000002G3Y</asin>
	 * <label-info-list>
	 * 	<label-info>
	 * 		<catalog-number>31454 0387 2</catalog-number>
	 * 		<label id="35515729-1f2c-4cc9-9390-9af2764bc56c">
	 * 			<name>A&amp;M Records</name>
	 * 		</label>
	 * 	</label-info>
	 * </label-info-list>
	 * <medium-list count="1">
	 * 	<track-count>16</track-count>
	 * 	<medium>
	 * 		<format>CD</format>
	 * 		<disc-list count="8"/>
	 * 		<track-list count="16"/>
	 * 	</medium>
	 * </medium-list>
	 * <tag-list>
	 * 	<tag count="1">
	 * 	<name>progressive rock</name>
	 * 	</tag>
	 * </tag-list>
	 * </release>
	 * </release-list>
	 * </metadata>
	 * @param UPC
	 * @return a CD if the UPC is found, otherwise returns null
	 * @throws IOException  
	 */
	
	// Lookup CD UPC via eandata.com / musicbrainz
	public static CD lookupCD(String UPC) throws IOException  {
		// TODO: rate limit this request
		// TODO: get covers from eandata.com ?
		String request = "http://musicbrainz.org/ws/2/release/?query=barcode:" + UPC;
		String response = getResponse(request);
		//System.out.println(response);
		return getCDfromXML(response);
	}
	
	private static CD getCDfromXML(String xmlStr) {
		CD cd;
		try {
			MBNode node = new MBNode(xmlStr);
			String upc = node.getUPC();
			String title = node.getTitle();
			String genre = node.getGenre();
			byte[] cover = node.getCover();
			String author = node.getAuthor();
			cd = new CD (title, author, upc, genre, cover);
		}
		catch (Exception e) {
			cd = null;
		}
		return cd;
	}

	/**
	 * Tests
	 * @param args
	 * @throws Exception
	
	public static void main(String[] args) throws Exception {
		/*Book b = lookupBook("9780131395312");
		System.out.println("Title: " + b.getTitle());
		System.out.println("Genre: " + b.getGenre());
		System.out.println("Cover: " + b.getCover());
		// TODO: parse authors tags first
		System.out.println("Author: " + b.getAuthor());
		System.out.println("Plot: " + b.getPlot());
		System.out.println("Year: " + b.getYear());
		
		CD cd = lookupCD("012414170422"); // Styx greatest hits
		System.out.println("UPC: " + cd.getISBN());
		System.out.println("Title: " + cd.getTitle());
		// TODO: can we get genre from somewhere else if they're available?
		System.out.println("Genre: " + cd.getGenre());
		System.out.println("Cover: " + cd.getCover());
		System.out.println("Author: " + cd.getAuthor());
		
		
		Movie[] movies = lookupMovie("043396040151"); // Crash
		for (int i = 0; i < movies.length; i++) {
			System.out.println("Title: " + movies[i].getTitle());
			System.out.println("Country: " + movies[i].getCountry());
			System.out.println("Cast: " + movies[i].getCast());
			System.out.println("Year: " + movies[i].getYear());
			System.out.println("Language: " +movies[i].getLanguage());
			System.out.println("Genre: " + movies[i].getGenre());
			System.out.println("Director: " + movies[i].getAuthor());
			System.out.println("Length: " + movies[i].getLength());
			System.out.println("Plot: " + movies[i].getPlot());
		}
		
	}
	*/
	
}
