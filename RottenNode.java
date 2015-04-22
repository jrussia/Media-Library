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
 * @version 0.2.0 4/21/2015
 */
public class RottenNode {
	JSONObject json;
	String upc;
	public RottenNode(String responseRt, String UPC) throws ParseException {
		UPC = upc;
		JSONParser parser = new JSONParser();
		json = (JSONObject) parser.parse(responseRt);
	}
	// TODO: needs rate limiting to increase the number of responses
	/**
	 * Get the top ten matches from Rotten Tomatoes
	 * Note: currently only returns the top four matches due to rate limiting.
	 * 
	 * @return	an array of Movies
	 */
	public Movie[] getTopTen() {
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
			String plot = "", cast = "", author = "", language = "", country = "", genre = "";
			try {
				JSONObject moreInfo = getMoreInfo(((JSONObject) jsonMovies.get(i)).get("id").toString());
				plot = getSynopsis(moreInfo);
				cast = getCast(moreInfo);
				author = getDirector(moreInfo);
				language = getLanguage(moreInfo);
				country = getCountry(moreInfo);
				genre = getGenre(moreInfo);
			} catch (IOException e) {
				// Return empty strings for these fields if we can't load moreInfo.
			} catch (ParseException p) {
				// Return empty strings for these fields if we can't load moreInfo.
			}
			movies[i] = new Movie(title, author, isbn, genre, cover, year, plot, cast, length, language, country);
		}
		return movies;
	}

	// could be more than one, just take the first
	/**
	 * Given an information object, returns the element matching genres
	 * 
	 * @param 	moreInfo	more information XML element from Rotten Tomatoes
	 * @return	the genre, as a string
	 */
	private String getGenre(JSONObject moreInfo) {
		JSONArray ary = (JSONArray) moreInfo.get("genres");
		return ((String) ary.get(0));
	}
	
	// TODO: look this up somewhere else
	/**
	 * Returns the country associated with a movie.
	 * Currently returns an empty string, since Rotten Tomatoes doesn't store country information.
	 * 
	 * @param moreInfo	more information XML element from Rotten Tomatoes
	 * @return	the country of origin, as a string
	 */
	private String getCountry(JSONObject moreInfo) {
		return "";
	}

	// TODO: look this up somewhere else
	/**
	 * Given a more information XML element from Rotten Tomatoes, return the movie's language.
	 * Currently returns an empty string, since it's not supported by RT.
	 * 
	 * @param 	moreInfo	more information XML element from Rotten Tomatoes
	 * @return	the language of the movie
	 */
	private String getLanguage(JSONObject moreInfo) {
		return "";
	}

	/**
	 * Given a more information XML element from Rotten Tomatoes, return the movie's synopsis.
	 * Currently returns an empty string, since it's not supported by RT.
	 * 
	 * @param 	moreInfo
	 * @return	the synopsis of the movie
	 */
	private String getSynopsis(JSONObject moreInfo) {
		return (String) moreInfo.get("synopsis");
	}

	/**
	 * Given a movie ID, get more information from RT.
	 * 
	 * @param id	the	RT movie id to look up
	 * @return		a JSONObject with more information
	 * @throws IOException		if it can't make the connection
	 * @throws ParseException	if it's unable to parse the response
	 */
	private JSONObject getMoreInfo(String id) throws IOException, ParseException {
		String response = InternetLookup.getRottenIDResponse(id);
		JSONParser parser = new JSONParser();
		return (JSONObject) parser.parse(response);
	}

	// could be more than one, just take the first
	/**
	 * Given an RT more information JSON object, return the movie's director.
	 * 
	 * @param moreInfo	the more information JSON object
	 * @return	the director
	 */
	private String getDirector(JSONObject moreInfo) {
		JSONArray ary = (JSONArray) moreInfo.get("abridged_directors");
		return ((JSONObject) ary.get(0)).get("name").toString();
	}

	/**
	 * Given an RT more information JSON object, return a list of the movie's actors
	 * 
	 * @param moreInfo	the more information JSON object
	 * @return	the cast, as a string of comma delimited names
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
