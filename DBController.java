package media;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/***
 * CSC 478
 * Team2
 * DBController.java
 * Purpose: Provide SQLite layer to save, update, and delete media entries.
 * 
 * @author Karissa (Nash) Stisser, Jeremy Egner, Yuji Tsuzuki
 * @version 0.2.0 4/15/2015
 */
public class DBController {

	static ConfigReader cr = new ConfigReader();
	private static String databaseFilePath = cr.getValue("dbfilepath");
	
	/**
	 * Create a connection to the database.
	 * 
	 * @return	the connection
	 */
	public static Connection connect(){
		String fullPath = "jdbc:sqlite:" + databaseFilePath;
		final Connection conn;
		
		try {
			Class.forName("org.sqlite.JDBC");
		} 
		catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}

		try {
			conn = DriverManager.getConnection(fullPath);
			return conn;
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	/**
	 * Add a movie to the database
	 * (Requirements 1.2.0, 1.2.1, 1.2.2, 1.2.3, 1.2.4, 1.2.5, 1.2.6, 1.2.7,
	 * 	1.2.8, 1.2.9, 1.3.0, 2.0.0, 2.3.0)
	 * 
	 * @param UPC		UPC code
	 * @param title		title of movie
	 * @param cover		cover image
	 * @param year		year released
	 * @param length	length of film in minutes
	 * @param plot		plot of movie
	 * @param cast		cast, as a string
	 * @param director	director
	 * @param genre		genre
	 * @param language	language
	 * @param country	country of origin
	 * @throws SQLException	if we were unable to add the movie
	 */
	public static void addMovie(String UPC, String title, byte[] cover, String year,
			String length, String plot, String cast, Integer director, Integer genre, Integer language, Integer country) throws SQLException {
		
		Connection conn = null;
		try {
			conn = connect();
		
			PreparedStatement stat = null;
		
			stat = conn.prepareStatement("insert into movie "
					+ "(movie_id, 	movie_isbn, 	movie_title, 	movie_cover, 			movie_year, 		movie_length_minutes,"
					+ "movie_plot, 	movie_cast, 	movie_author_id, movie_genre_id, 		movie_language_id, 	movie_country_id)"
					+ " values "
					+ "(?,	 		?,				?,				?,						?,					?,"
					+ "?,			?,				?,				?,						?,					?)");
		
			stat.setNull(1, java.sql.Types.INTEGER);
			stat.setString(2, UPC);
			stat.setString(3, title);
			stat.setBytes(4, cover);
			stat.setString(5, year);
			stat.setString(6, length);
			stat.setString(7, plot);
			stat.setString(8, cast);
			setIntOrNull(stat, 9, director);
			setIntOrNull(stat, 10, genre);
			setIntOrNull(stat, 11, language);
			setIntOrNull(stat, 12, country);
		
			stat.execute();
			System.out.println("Done!");
			} 
		finally {
			if (conn != null)
				conn.close();
		}
	}

	/**
	 * Add a CD to the database.
	 * (Requirements 1.4.0, 1.4.1, 1.4.2, 1.4.3, 1.4.4, 2.0.0, 2.2.0)
	 * 
	 * @param UPC		UPC code
	 * @param title		title of CD
	 * @param cover		cover image
	 * @param artist	artist or group
	 * @throws SQLException	if we're unable to add the CD
	 */
	public static void addCD(String UPC, String title, Integer genre, byte[] cover, Integer artist) throws SQLException {
		Connection conn = null;
		
		try {
			conn = connect();
			PreparedStatement stat = null;
		
			stat = conn.prepareStatement("insert into cd "
					+ "(cd_id, 		cd_isbn,		cd_title,		cd_genre_id,			cd_cover,				cd_author_id)"
					+ " values "
					+ "(?,	 		?,				?,				?,						?,						?)");
		
			stat.setNull(1, java.sql.Types.INTEGER);
			stat.setString(2, UPC);
			stat.setString(3, title);
			setIntOrNull(stat, 4, genre);
			stat.setBytes(5, cover);
			setIntOrNull(stat, 6, artist);
		
			stat.execute();
		} 
		finally {
			if (conn != null)
				conn.close();
		}
	}
	
	/**
	 * Add a book to the database.
	 * (Requirements 1.3.0, 1.3.1, 1.3.2, 1.3.3, 1.3.4, 1.3.5, 1.3.6, 1.3.7,
	 * 	2.o.o, 2.1.0)
	 * 
	 * @param ISBN		ISBN code
	 * @param title		book title
	 * @param year		year published
	 * @param plot		plot
	 * @param length	length in pages
	 * @param cover		cover image
	 * @param author	author
	 * @throws SQLException	if we're unable to add the book
	 */
	public static void addBook(String ISBN, String title, String year, String plot, 
			String length, byte[] cover, Integer genre, Integer author) throws SQLException {
		
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = connect();
		
			stat = conn.prepareStatement("insert into book "
					+ "(book_id, 	book_isbn, 		book_title, 	book_year, 		book_plot,		book_length_pages,"
					+ "book_cover,					book_genre_id,	book_author_id)"
					+ " values "
					+ "(?,	 		?,				?,				?,				?,				?,"
					+ "?,							?,				?)");
		
			stat.setNull(1, java.sql.Types.INTEGER);
			stat.setString(2, ISBN);
			stat.setString(3, title);
			stat.setString(4, year);
			stat.setString(5, plot);
			stat.setString(6, length);
			stat.setBytes(7, cover);
			setIntOrNull(stat, 8, genre);
			setIntOrNull(stat, 9, author);
		
			stat.execute();
		} 
		finally {
			if (conn != null)
				conn.close();
		}
	}

	/**
	 * Update a book's details in the database
	 * (Requirements 1.3.0, 1.3.1, 1.3.2, 1.3.3, 1.3.4, 1.3.5, 1.3.6, 1.3.7, 
	 * 	5.0.0, 5.3.0)
	 * 
	 * @param ISBN		ISBN code
	 * @param title		title
	 * @param year		year
	 * @param plot		plot
	 * @param length	length in pages
	 * @param cover		cover image
	 * @param author	author
	 * @param id		database id of the book
	 * @throws SQLException	if we're unable to update the book
	 */
	public static void updateBook(String ISBN, String title, String year, String plot,
			String length, byte[] cover, Integer genre, Integer author, Integer id) throws SQLException {
		Connection conn = null;
		
		try {
			conn = connect();
		
			PreparedStatement stat = null;
			stat = conn.prepareStatement("update book set "
				+ "book_isbn = ?,"
				+ "book_title = ?,"
				+ "book_year = ?,"
				+ "book_plot = ?,"
				+ "book_length_pages = ?,"
				+ "book_cover = ?,"
				+ "book_genre_id = ?,"
				+ "book_author_id = ? "
				+ "where book_id = ?");
		
			stat.setString(1, ISBN);
			stat.setString(2, title);
			stat.setString(3, year);
			stat.setString(4, plot);
			stat.setString(5, length);
			stat.setBytes(6, cover);
			setIntOrNull(stat, 7, genre);
			setIntOrNull(stat, 8, author);
			setIntOrNull(stat, 9, id);
		
			stat.execute();
		} 
		finally {
			if (conn != null)
				conn.close();
		}
	}
	
	/**
	 * Update a CD's details
	 * (Requirements 1.4.0, 1.4.1, 1.4.2, 1.4.3, 1.4.4, 5.0.0, 5.3.0)
	 * @param UPC		UPC code
	 * @param title		CD title
	 * @param genre		genre of music
	 * @param cover		cover image
	 * @param artist	artist or group
	 * @param id		database ID
	 * @throws SQLException	if we're unable to update the CD
	 */
	public static void updateCD(String UPC, String title, Integer genre, byte[] cover, Integer artist, Integer id) throws SQLException {
		Connection conn = null;
		try {
			conn = connect();
		
			PreparedStatement stat = null;
		
			stat = conn.prepareStatement("update cd set "
				+ "cd_isbn = ?,"
				+ "cd_title = ?,"
				+ "cd_genre_id = ?,"
				+ "cd_cover = ?,"
				+ "cd_author_id = ? "
				+ "where cd_id = ?");
		
			stat.setString(1, UPC);
			stat.setString(2, title);
			setIntOrNull(stat, 3, genre);
			stat.setBytes(4, cover);
			setIntOrNull(stat, 5, artist);
			setIntOrNull(stat, 6, id);
			
			stat.execute();
		} 
		finally {
			if (conn != null)
				conn.close();
		}
	}
	
	/**
	 * Update a movie's details in the database
	 * (Requirements 1.2.0, 1.2.1, 1.2.2, 1.2.3, 1.2.4, 1.2.5, 1.2.6, 1.2.7,
	 * 	1.2.8, 1.2.9, 1.2.10, 5.0.0, 5.3.0)
	 * @param UPC		UPC code
	 * @param title		title
	 * @param year		year released
	 * @param plot		plot
	 * @param cast		cast, as a string
	 * @param length	length, in minutes
	 * @param cover		cover image
	 * @param director	director
	 * @param genre		genre
	 * @param language	language
	 * @param country	country of origin
	 * @param id		database ID
	 * @throws SQLException	if we're unable to update the movie
	 */
	public static void updateMovie(String UPC, String title, String year, String plot,
			String cast, String length, byte[] cover, Integer director, Integer genre, Integer language, Integer country, Integer id) throws SQLException {
		Connection conn = null;
		
		try {
			conn = connect();
		
			PreparedStatement stat = null;	
		
			stat = conn.prepareStatement("update movie set "
				+ "movie_isbn = ?,"
				+ "movie_title = ?,"
				+ "movie_year = ?,"
				+ "movie_plot = ?,"
				+ "movie_cast = ?,"
				+ "movie_length_minutes = ?,"
				+ "movie_cover = ?,"
				+ "movie_author_id = ?,"
				+ "movie_genre_id = ?,"
				+ "movie_language_id = ?,"
				+ "movie_country_id = ? "
				+ "where movie_id = ?");
				
			stat.setString(1, UPC);
			stat.setString(2,  title);
			stat.setString(3,  year);
			stat.setString(4, plot);
			stat.setString(5, cast);
			stat.setString(6, length);
			stat.setBytes(7, cover);
			setIntOrNull(stat, 8, director);
			setIntOrNull(stat, 9, genre);
			setIntOrNull(stat, 10, language);
			setIntOrNull(stat, 11, country);
			setIntOrNull(stat, 12, id);
					
			stat.execute();
		} 
		finally {
			if (conn != null)
				conn.close();
		}
	}


	/**
	 * Delete a book from the database
	 * (Requirements 5.0.0, 5.2.0)
	 * 
	 * @param id	Database id
	 * @throws SQLException	if we're unable to delete the book
	 */
	public static void deleteBook(Integer id) throws SQLException {
		Connection conn = null;
		try {
			conn = connect();
		
			PreparedStatement stat = null;
		
			stat = conn.prepareStatement("delete from book where book_id = ?");
		
			stat.setInt(1, id);
			stat.execute();
		} finally {
			if (conn != null)
				conn.close();
		}
	}
	
	/**
	 * Delete a CD from the database.
	 * (Requirements 5.0.0, 5.2.0)
	 * 
	 * @param id	database ID
	 * @throws SQLException	if we're unable to delete the CD
	 */
	public static void deleteCD(Integer id) throws SQLException {
		PreparedStatement stat = null;
		Connection conn = null;
		try {
			conn = connect();
		
			stat = conn.prepareStatement("delete from cd where cd_id = ?");
		
			stat.setInt(1, id);
			stat.execute();
		} 
		finally {
			if (conn != null)
				conn.close();
		}
	}
	
	/**
	 * Delete a movie from the database
	 * (Requirements 5.0.0, 5.2.0)
	 * @param id	database id
	 * @throws SQLException	if we're unable to delete the movie
	 */
	public static void deleteMovie(Integer id) throws SQLException {
		PreparedStatement stat = null;
		Connection conn = null;
		
		try {
			conn = connect();
		
			stat = conn.prepareStatement("delete from movie where movie_id = ?");
		
			stat.setInt(1, id);
			stat.execute();
		} 
		finally {
			if (conn != null)
				conn.close();
		}
	}
	
	/**
	 * Check if a database table contains a certain value. If not, add that value to the table.
	 * Used to check join tables for existing values.
	 * (Requirements 1.2.6, 1.2.7, 1.3.5, 1.3.6, 1.4.2, 1.4.3)
	 * 
	 * @param value		value to search for
	 * @param table		table to check
	 * @return	the integer id corresponding to the lookup value
	 * @throws SQLException	if we're unable to look up the value
	 */
	public static Integer lookup(String value, Database.table table) throws SQLException {
		Integer result;
		if (value == null)
			return null;
		result = tableContainsValue(value, table);
		if (result == null)
			result = tableAddValue(value, table);
		return result;
	}
	
	/**
	 * Check if a table contains a certain value.
	 * (Requirements 1.2.6, 1.2.7, 1.3.5, 1.3.6, 1.4.2, 1.4.3)
	 * 
	 * @param value		value to look for
	 * @param table		table to check
	 * @return	the key ID of the value in the table, if it exists, otherwise null
	 * @throws SQLException	if we're unable to look up the value
	 */
	private static Integer tableContainsValue(String value, Database.table table) throws SQLException {
		PreparedStatement stat = null;
		String sql = null;
		Integer resultId = null;
		Connection conn = null;
		
		try {
			conn = connect();
		
			switch (table) {
				case AUTHOR:
					sql = "select author_id from author where author = ?";
					break;
				case COUNTRY:
					sql = "select country_id from country where country = ?";
					break;
				case GENRE:
					sql = "select genre_id from genre where genre = ?";
					break;
				case LANGUAGE:
					sql = "select language_id from language where language = ?";
					break;
			}
		
			stat = conn.prepareStatement(sql);
	
			stat.setString(1, value);
		
			ResultSet results = stat.executeQuery();
		
			results.next();
			resultId = results.getInt(1);
		} 
		finally {
			if (conn != null)
				conn.close();
		}
		
		return resultId;
	}
	
	/**
	 * Adds a value to a database table. Only used for lookup tables with two columns, one
	 * being the key ID and the other being the value.
	 * (Requirements 1.2.6, 1.2.7, 1.3.5, 1.3.6, 1.4.2, 1.4.3)
	 * 
	 * @param value		the value to add to the table
	 * @param table		the table to add to
	 * @return	the key ID of the value added
	 * @throws SQLException	if we're unable to add the value
	 */
	private static Integer tableAddValue(String value, Database.table table) throws SQLException {
		PreparedStatement stat = null;
		String sql = null;		
		Connection conn = null;
		
		try {
			conn = connect();
		
			switch (table) {
				case AUTHOR:
					sql = "insert into author (author_id, author) values (null, ?)";
					break;
				case COUNTRY:
					sql = "insert into country (country_id, country) values (null, ?)";
					break;
				case GENRE:
					sql = "insert into genre (genre_id, genre) values (null, ?)";
					break;
				case LANGUAGE:
					sql = "insert into language (language_id, language) values (null, ?)";
					break;
			}
		
			stat = conn.prepareStatement(sql);
			stat.setString(1,  value);
		
			stat.execute();
		}
		finally {
			if (conn != null)
				conn.close();
		}
		
		return tableContainsValue(value, table);
	}
	
	/**
	 * Helper function to set a value to int or null depending on the value.
	 * (Requirements 1.2.0, 1.3.0, 1.4.0)
	 * 
	 * @param stat		the prepared statement to append this value to
	 * @param place		the place in the statement to append the value
	 * @param value		the value to append
	 * @throws SQLException	if we're unable to set the value
	 */
	private static void setIntOrNull(PreparedStatement stat, int place, Integer value) throws SQLException {
		if (value == null)
			stat.setNull(place, java.sql.Types.INTEGER) ;
		else 
			stat.setInt(place, value);
	}
}

