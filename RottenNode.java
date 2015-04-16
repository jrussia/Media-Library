package media;

import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/***
 * CSC 478
 * Team2
 * RottenNode.java
 * Purpose: XML Node for rottentomatoes.com.
 * 
 * @author Karissa (Nash) Stisser, Jeremy Egner, Yuji Tsuzuki
 * @version 0.2.0 4/15/2015
 */
public class RottenNode {
	JSONObject json;
	String upc;
	public RottenNode(String responseRt, String UPC) throws ParseException {
		UPC = upc;
		JSONParser parser = new JSONParser();
		json = (JSONObject) parser.parse(responseRt);
	}
	// needs rate limiting to increase the number of responses
	/**
	 * 
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 */
	public Movie[] getTopTen() throws IOException, ParseException {
		long total = (long) json.get("total");
		if (total > 4)
			total = 4;
		Movie[] movies = new Movie[(int) total];
		JSONArray jsonMovies = (JSONArray) json.get("movies");
		for (int i = 0; i < total; i++) {
			String isbn = upc;
			String title = (String) ((JSONObject) jsonMovies.get(i)).get("title");
			byte[] cover = null;
			String year = ((JSONObject) jsonMovies.get(i)).get("year").toString();
			String length = ((JSONObject) jsonMovies.get(i)).get("runtime").toString();
			JSONObject moreInfo = getMoreInfo(((JSONObject) jsonMovies.get(i)).get("id").toString());
			String plot = getSynopsis(moreInfo);
			String cast = getCast(moreInfo);
			String author = getDirector(moreInfo);
			String language = getLanguage(moreInfo);
			String country = getCountry(moreInfo);
			String genre = getGenre(moreInfo);
			movies[i] = new Movie(title, author, isbn, genre, cover, year, plot, cast, length, language, country);
		}
		
		/** testing
		for (int j = 0; j < movies.length; j++) {
			System.out.println("Title: " + movies[j].getTitle());
			System.out.println("Year: " + movies[j].getYear());
			System.out.println("Length: " + movies[j].getLength());
			System.out.println("Genre: " + movies[j].getGenre());
			System.out.println("Director: " + movies[j].getAuthor());
			System.out.println("Cast: " + movies[j].getCast());
		}
		*/
		return movies;
	}

	// could be more than one, just take the first
	/**
	 * 
	 * @param moreInfo
	 * @return
	 */
	private String getGenre(JSONObject moreInfo) {
		JSONArray ary = (JSONArray) moreInfo.get("genres");
		return ((String) ary.get(0));
	}

	// Rotten tomatoes doesn't do country
	/**
	 * 
	 * @param moreInfo
	 * @return
	 */
	private String getCountry(JSONObject moreInfo) {
		return "";
	}

	// Rotten tomatoes doesn't do language
	/**
	 * 
	 * @param moreInfo
	 * @return
	 */
	private String getLanguage(JSONObject moreInfo) {
		return "";
	}

	/**
	 * 
	 * @param moreInfo
	 * @return
	 */
	private String getSynopsis(JSONObject moreInfo) {
		return (String) moreInfo.get("synopsis");
	}

	/**
	 * 
	 * @param id
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 */
	private JSONObject getMoreInfo(String id) throws IOException, ParseException {
		String response = InternetLookup.getRottenIDResponse(id);
		JSONParser parser = new JSONParser();
		return (JSONObject) parser.parse(response);
	}

	// could be more than one, just take the first
	/**
	 * 
	 * @param moreInfo
	 * @return
	 */
	private String getDirector(JSONObject moreInfo) {
		JSONArray ary = (JSONArray) moreInfo.get("abridged_directors");
		return ((JSONObject) ary.get(0)).get("name").toString();
	}

	/**
	 * 
	 * @param moreInfo
	 * @return
	 */
	private String getCast(JSONObject moreInfo) {
		JSONArray ary = (JSONArray) moreInfo.get("abridged_cast");
		String cast = "";
		for (int i = 0; i < ary.size(); i++) {
			cast = cast + ((JSONObject) ary.get(i)).get("name").toString();
			if (i < ary.size() - 1)
				cast = cast + ", ";
		}
		return cast;
	}

}
