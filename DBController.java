package media;

import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/*
 * CSC 478
 * Team2
 * DBController.java
 * Purpose: Provide SQLite layer to save, update, and delete media entries.
 * 
 * @author Karissa (Nash) Stisser, Jeremy Egner, Yuji Tsuzuki
 * @version 0.2.0 3/5/2015
 */
// TODO: close all of the statements, look up the right way to do this without leaving connections open
public class DBController {

	/**
	 * Constructor
	 */
	public DBController(/* Database db */) {
		// this.db = db;
	}
	
	/**
	 * Add a movie to the database
	 * @throws SQLException 
	 * 
	 * @params 	movie 
	 * 			conn
	 */
	public static void addMovie(Connection conn, String ISBN, String title, String cover, String year,
			String length, String plot, String cast, int author, int genre, int language, int country) throws SQLException {
		
		//Class.forName("org.sqlite.JDBC"); // TODO: is this necessary?
		PreparedStatement stat = null;
		
		stat = conn.prepareStatement("insert into movie "
			+ "(movie_id, 	movie_isbn, 	movie_title, 	movie_cover_filepath, 	movie_year, 		movie_length_minutes,"
			+ "movie_plot, 	movie_cast, 	movie_author_id, movie_genre_id, 		movie_language_id, 	movie_country_id)"
			+ " values "
			+ "(?,	 		?,				?,				?,						?,					?,"
			+ "?,			?,				?,				?,						?,					?)");
		
		stat.setNull(1, java.sql.Types.INTEGER);
		stat.setString(2, ISBN);
		stat.setString(3, title);
		stat.setString(4, cover);
		stat.setString(5, year);
		stat.setString(6, length);
		stat.setString(7, plot);
		stat.setString(8, cast);
		stat.setInt(9, author);
		stat.setInt(10, genre);
		stat.setInt(11, language);
		stat.setInt(12, country);
		
		stat.execute();
	}

	/**
	 * Add a CD to the database
	 * @throws SQLException 
	 * 
	 * @params 	cd 
	 * 			conn
	 */
	public static void addCD(Connection conn, String ISBN, String title, String cover, int author) throws SQLException {
		
		//Class.forName("org.sqlite.JDBC"); // TODO: is this necessary?
		PreparedStatement stat = null;
		
		stat = conn.prepareStatement("insert into cd "
			+ "(cd_id, 		cd_isbn,		cd_title,		cd_cover_filepath,		cd_author_id)"
			+ " values "
			+ "(?,	 		?,				?,				?,						?)");
		
		stat.setNull(1, java.sql.Types.INTEGER);
		stat.setString(2, ISBN);
		stat.setString(3, title);
		stat.setString(4, cover);
		stat.setInt(5, author);
		
		stat.execute();
	}
	
	/**
	 * Add a book to the database
	 * @throws SQLException 
	 * 
	 * @params 	book 
	 * 			conn
	 */
	public static void addBook(Connection conn, String ISBN, String title, String year, String plot, 
			String length, String cover, int author) throws SQLException {
		
		//Class.forName("org.sqlite.JDBC");
		PreparedStatement stat = null;
		
		stat = conn.prepareStatement("insert into book "
			+ "(book_id, 	book_isbn, 		book_title, 	book_year, 		book_plot,		book_length_pages,"
			+ "book_cover_filepath,			book_author_id)"
			+ " values "
			+ "(?,	 		?,				?,				?,				?,				?,"
			+ "?,							?)");
		
		stat.setNull(1, java.sql.Types.INTEGER);
		stat.setString(2, ISBN);
		stat.setString(3, title);
		stat.setString(4, year);
		stat.setString(5, plot);
		stat.setString(6, length);
		stat.setString(7, cover);
		stat.setInt(8, author);
		
		stat.execute();
	}

	/**
	 * Update media in the database
	 * @throws SQLException 
	 * 
	 * @params 	media 
	 * 			conn
	 */
	public static void updateBook(Connection conn, String isbn, String title, String year, String plot,
			String length, String cover, int author, int id) throws SQLException {
		// TODO: Verify that we need this
		//	Class.forName("org.sqlite.JDBC");
		
		PreparedStatement stat = null;
		stat = conn.prepareStatement("update book set "
				+ "book_isbn = ?,"
				+ "book_title = ?,"
				+ "book_year = ?,"
				+ "book_plot = ?,"
				+ "book_length_pages = ?,"
				+ "book_cover_filepath = ?,"
				+ "book_author_id = ? "
				+ "where book_id = ?");
		
		stat.setString(1, isbn);
		stat.setString(2, title);
		stat.setString(3, year);
		stat.setString(4, plot);
		stat.setString(5, length);
		stat.setString(6, cover);
		stat.setInt(7, author);
		stat.setInt(8, id);
		
		stat.execute();
	}
	
	public static void updateCD(Connection conn, String isbn, String title, String cover, int author, int id) throws SQLException {
		// TODO: Verify that we need this
		//	Class.forName("org.sqlite.JDBC");
			
		PreparedStatement stat = null;
		
		stat = conn.prepareStatement("update cd set "
				+ "cd_isbn = ?,"
				+ "cd_title = ?,"
				+ "cd_cover = ?,"
				+ "cd_author_id = ? "
				+ "where cd_id = ?");
		
		stat.setString(1, isbn);
		stat.setString(2, title);
		stat.setString(3, cover);
		stat.setInt(4, author);
		stat.setInt(5, id);
			
		stat.execute();
	}
	
	public static void updateMovie(Connection conn, String isbn, String title, String year, String plot,
			String cast, String length, String cover, int author, int genre, int language, int country, int id) throws SQLException {
		// TODO: Verify that we need this
		//	Class.forName("org.sqlite.JDBC");
					
		PreparedStatement stat = null;	
		
		stat = conn.prepareStatement("update movie set "
				+ "movie_isbn = ?,"
				+ "movie_title = ?,"
				+ "movie_year = ?,"
				+ "movie_plot = ?,"
				+ "movie_cast = ?,"
				+ "movie_length_minutes = ?,"
				+ "movie_cover_filepath = ?,"
				+ "movie_author_id = ?,"
				+ "movie_genre_id = ?,"
				+ "movie_language_id = ?,"
				+ "movie_country_id = ? "
				+ "where movie_id = ?");
				
		stat.setString(1, isbn);
		stat.setString(2,  title);
		stat.setString(3,  year);
		stat.setString(4, plot);
		stat.setString(5, cast);
		stat.setString(6, length);
		stat.setString(7, cover);
		stat.setInt(8, author);
		stat.setInt(9, genre);
		stat.setInt(10, language);
		stat.setInt(11, country);
		stat.setInt(12, id);
					
		stat.execute();
	}

	/**
	 * Delete media from the database
	 * @throws SQLException 
	 * 
	 * @params media conn
	 */
	public static void deleteBook(Connection conn, int id) throws SQLException {
		// TODO: Verify that we need this
		//Class.forName("org.sqlite.JDBC");
		
		PreparedStatement stat = null;
		
		stat = conn.prepareStatement("delete from book where book_id = ?");
		
		stat.setInt(1, id);
		stat.execute();
	}
	
	public static void deleteCD(Connection conn, int id) throws SQLException {
		PreparedStatement stat = null;
		
		stat = conn.prepareStatement("delete from cd where cd_id = ?");
		
		stat.setInt(1, id);
		stat.execute();
	}
	
	public static void deleteMovie(Connection conn, int id) throws SQLException {
		PreparedStatement stat = null;
		
		stat = conn.prepareStatement("delete from movie where movie_id = ?");
		
		stat.setInt(1, id);
		stat.execute();
	}
	
	public static int lookup(Connection conn, String s, Database.table t) throws SQLException {
		int result;
		result = tableContainsValue(conn, s, t);
		if (result == 0)
			result = tableAddValue(conn, s, t);
		return result;
	}
	
	private static int tableContainsValue(Connection conn, String s, Database.table t) throws SQLException {
		PreparedStatement stat = null;
		int resultId;
		
		stat = conn.prepareStatement("select ? from ? where ? = ?");
		
		switch (t) {
		case AUTHOR:
			stat.setString(1, "author_id");
			stat.setString(2, "author");
			stat.setString(3, "author");
			break;
		case COUNTRY:
			stat.setString(1, "country_id");
			stat.setString(2, "country");
			stat.setString(3, "country");
			break;
		case GENRE:
			stat.setString(1, "genre_id");
			stat.setString(2, "genre");
			stat.setString(3, "genre");
			break;
		case LANGUAGE:
			stat.setString(1, "language_id");
			stat.setString(2, "language");
			stat.setString(3, "language");
			break;
		}
		stat.setString(4, s);
		
		ResultSet results = stat.executeQuery();
		
		resultId = results.getInt(1);
		
		return resultId;
	}
	
	private static int tableAddValue(Connection conn, String s, Database.table t) throws SQLException {
		PreparedStatement stat = conn.prepareStatement("insert into ? (?) values (?)");
		
		switch (t) {
		case AUTHOR:
			stat.setString(1, "author");
			stat.setString(2, "author");
			break;
		case COUNTRY:
			stat.setString(1, "country");
			stat.setString(2, "country");
			break;
		case GENRE:
			stat.setString(1, "genre");
			stat.setString(2, "genre");
			break;
		case LANGUAGE:
			stat.setString(1, "language");
			stat.setString(2, "language");
			break;
		}
		
		ResultSet results = stat.executeQuery();
		
		// TODO: does insert really return any values?
		return results.getInt(1);
	}
		
	/*
	public static void main(String[] args) throws SQLException, ClassNotFoundException {
		Class.forName("org.sqlite.JDBC");
		Connection conn = DriverManager.getConnection("jdbc:sqlite:database.db");
		TestUtils.clearMovies();
		// Test adding a movie
		addMovie(conn, "123TESTISBN", "123TESTTITLE", "123TESTCOVER", "123TESTYEAR", "123TESTLENGTH", "123TESTPLOT", "123TESTCAST", 1111, 2222, 3333, 4444);
		TestUtils.printMovies();
	} 
	*/
}

