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
 * @version 0.1.1 3/4/2015
 */
public class DBController {

	/**
	 * Constructor
	 */
	public DBController(/* Database db */) {
		// this.db = db;
	}
	
	/**
	 * Stringify a string for database entry
	 * 
	 * @params	string
	 */
	private static String stringify(String s) {
		return "'" + s + "'";
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
			+ "(movie_id, 	movie_isbn, 	movie_title, 	movie_cover_filepath, 	movie_year, 		movie_length_minutes"
			+ "movie_plot, 	movie_cast, 	movie_author_id, movie_genre_id, 		movie_language_id, 	movie_country_id)"
			+ " values "
			+ "(?,	 		?,				?,				?,						?,					?"
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
			+ "(book_id, 	book_isbn, 		book_title, 	book_year, 		book_plot,		book_length_pages"
			+ "book_cover_filepath,			book_author_id)"
			+ " values "
			+ "(?,	 		?,				?,				?,				?,				?"
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
	
	public static int lookup(Connection conn, String s, Database.table t) {
		int result;
		result = tableContainsValue(conn, s, t);
		if (result == 0)
			result = tableAddValue(conn, s, t);
		return result;
	}
	
	private static int tableContainsValue(Connection conn, String s, Database.table t) {
		//TODO: finish
		return 0;
	}
	
	private static int tableAddValue(Connection conn, String s, Database.table t) {
		//TODO: finish
		return 0;
	}
		
	/** tests
	public static void main(String[] args) {
		try {
			try {
				Class.forName("org.sqlite.JDBC");
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
			}
			Connection conn = DriverManager.getConnection("jdbc:sqlite:C:/Users/jeremy/workspace/Media Library/database.db");
			Movie m = new Movie();
			DBController.addMovie(m,  conn);
			Statement stat = conn.createStatement();
			// TODO: add utils for viewing a database
			// TODO: add utils for viewing the result of a query
			stat.executeQuery("select * from MOVIE");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	} */
}

