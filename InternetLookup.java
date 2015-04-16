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
/***
 * CSC 478
 * Team2
 * InternetLookup.java
 * Purpose: Provide API to look up media values based on ISBN or UPC.
 * 
 * @author Karissa (Nash) Stisser, Jeremy Egner, Yuji Tsuzuki
 * @version 0.2.0 4/15/2015
 */
public class InternetLookup {
	
	/**
	 * @param ISBN	the ISBN string to look up
	 * @return the book requested, or null if no such book was found
	 * @throws IOException 
	 */
	public static Book lookupBook(String ISBN) throws IOException {
		ConfigReader cr = new ConfigReader();
		String key = cr.getValue("ISBNKey");
		String request = "http://www.isbndb.com/api/books.xml?access_key=" + key + "&results=texts&results=authors&results=details&index1=isbn&value1=" + ISBN;
		String response = getResponse(request);
		Book b = null;
		try {
			b = getBookfromXML(response);
		} catch (Exception e) {
			b = null;
		}
		return b;	
	}
	
	/**
	 * 
	 * @param xmlStr
	 * @return
	 * @throws Exception
	 */
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
	public static Movie[] lookupMovie(String UPC) throws Exception {
		ConfigReader cr = new ConfigReader();
		String eanKey = cr.getValue("EANKey");
		String request = "http://eandata.com/feed/?v=3&keycode=" + eanKey + "&mode=xml&find=" + UPC;
		String response = getResponse(request);
		EANMovieNode eanNode = new EANMovieNode(response);
		String title = eanNode.getTitle();
		String rtKey = cr.getValue("RTKey");
		String requestRt = encodeQuery("http://api.rottentomatoes.com/api/public/v1.0/movies.json?apikey=" + rtKey + "&q=" + title);
		String responseRt = getResponse(requestRt);
		RottenNode rNode = new RottenNode(responseRt, UPC);
		return rNode.getTopTen();
	}
	
	public static String getRottenIDResponse(String id) throws IOException {
		ConfigReader cr = new ConfigReader();
		String rtKey = cr.getValue("RTKey");
		String requestRt = encodeQuery("http://api.rottentomatoes.com/api/public/v1.0/movies/" + id + ".json.json?apikey=" + rtKey);
		return getResponse(requestRt);
	}
	
	private static String encodeQuery(String string) {
		String newString = string.replace(" ", "%20");
		return newString;
	}

	/**
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
		return getCDfromXML(response);
	}
	
	/**
	 * 
	 * @param xmlStr
	 * @return
	 */
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
