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
 * @version 0.0.4 3/3/2015
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
	 * 
	 * @params 	media 
	 * 			conn
	 */
	public static void update(Media media, Connection conn) {
		// TODO: Verify that we need this
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		Statement stat;
		// TODO: where is the best place to cache conn?
		// TODO: Probably throw this up to the GUI
		try {
			stat = conn.createStatement();
		// TODO: We should escape these values
		// TODO: Check the return value on this
		if (media instanceof Book) {
			// TODO: I don't think this is the right way to generalize this
			Book book = (Book) media;
			
				stat.execute("update BOOK set " +
						"BOOK_ISBN = " + book.getISBN() + "," +
						"BOOK_TITLE = " + stringify(book.getTitle()) + "," +
						"BOOK_YEAR = " + stringify(book.getYear()) + "," +
						"BOOK_PLOT = " + stringify(book.getPlot()) + "," +
						"BOOK_LENGTH_PAGES = " + stringify(book.getLength()) + "," +
						"BOOK_COVER_FILEPATH = " + stringify(book.getCover()) + "," +
						// TODO: look this up
						"BOOK_AUTHOR_ID = " + book.getAuthor() +
						// Not sure if book_id should be a string
						"where BOOK_ID = " + book.getId()
				);
		
		}
		if (media instanceof CD) {
			// TODO: We should escape these values
			// TODO: Check the return value on this
				// TODO: I don't think this is the right way to generalize this
				CD cd = (CD) media;
			
					stat.execute("update CD set " +
							"CD_ISBN = " + stringify(cd.getISBN()) + "," +
							"CD_TITLE = " + stringify(cd.getTitle()) + "," +
							"CD_COVER = " + stringify(cd.getCover()) + "," +
							// TODO: look this up
							"CD_AUTHOR_ID = " + cd.getAuthor() +
							"where CD_ID = " + cd.getId()
							);
			
		}
			
		if (media instanceof Movie) {
			// TODO: We should escape these values
			// TODO: Check the return value on this
			if (media instanceof Movie) {
				// TODO: I don't think this is the right way to generalize this
				Movie movie = (Movie) media;
				
					stat.execute("update MOVIE set " +
							"MOVIE_ISBN = " + stringify(movie.getISBN()) + "," +
							"MOVIE_TITLE = " + stringify(movie.getTitle()) + "," +
							"MOVIE_YEAR = " + stringify(movie.getYear()) + "," +
							"MOVIE_PLOT = " + stringify(movie.getPlot()) + "," +
							"MOVIE_CAST = " + stringify(movie.getCast()) + "," +
							"MOVIE_LENGTH_MINUTES = " + stringify(movie.getLength()) + "," +
							"MOVIE_COVER_FILEPATH = " + stringify(movie.getCover()) + "," +
							// TODO: look these up
							"MOVIE_AUTHOR_ID = " + movie.getAuthor() +
							"MOVIE_GENRE_ID = " + movie.getGenre() +
							"MOVIE_LANGUAGE_ID = " + movie.getLanguage() +
							"MOVIE_COUNTRY_ID = " + movie.getCountry() +
							"where MOVIE_ID = " + movie.getId()
							);
				
				}					
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Delete media from the database
	 * 
	 * @params media conn
	 */
	public static void delete(Media media, Connection conn) {
		// TODO: Verify that we need this
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		Statement stat;
		// TODO: where is the best place to cache conn?
		try {
			stat = conn.createStatement();

		// TODO: We should escape these values
		// TODO: Check the return value on this
		if (media instanceof Book) {
			Book book = (Book) media;
			try {
				stat.execute("delete from BOOK " +
						"where BOOK_ID = " + book.getId());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (media instanceof CD) {
			CD cd = (CD) media;
			try {
				stat.execute("delete from CD " +
						"where CD_ID = " + cd.getId());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (media instanceof Movie) {
			Movie movie = (Movie) media;
			try {
				stat.execute("delete from MOVIE " +
						"where MOVIE_ID = " + movie.getId());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

