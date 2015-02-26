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
 * Purpose: Provide controller API to save, update, and delete media entries.
 * 
 * @author Karissa (Nash) Stisser, Jeremy Egner, Yuji Tsuzuki
 * @version 0.0.1 2/23/2015
 */
public class DBController {

	// Database db; // if we need to store a reference to the database

	/**
	 * Constructor
	 */
	public DBController(/* Database db */) {
		// this.db = db;
	}

	/**
	 * Add a movie to the database
	 * 
	 * @params movie conn
	 */
	public static void addMovie(Movie movie, Connection conn) {
		// TODO: Verify that we need this
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		Statement stat;
		// TODO: where is the best place to cache conn?
		stat = conn.createStatement();
		// TODO: We should escape these values
		// TODO: Check the return value on this
		stat.executeQuery("insert into MOVIE" + "values (" +
				movie.getTitle() + "," + 
				movie.getCover() + "," + 
				movie.getYear() + "," + 
				movie.getLength() + "," + 
				movie.getPlot() + "," +
				movie.getCast() + "," + 
				movie.getAuthor() + "," +
				movie.getGenre() + "," + 
				movie.getLanguage() + "," +
				movie.getCountry() + 
				");"
				);
	}

	/**
	 * Add a CD to the database
	 * 
	 * @params cd conn
	 */
	public static void addCD(CD cd, Connection conn) {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		Statement stat;
		// TODO: where is the best place to cache conn?
		stat = conn.createStatement();
		// TODO: We should escape these values
		// TODO: Check the return value on this
		stat.executeQuery("insert into CD" + "values (" +
				CD.getTitle() + "," + 
				CD.getCover() + "," + 
				//CD.getYear() + "," + 
				CD.getAuthor() + "," +
				//movie.getGenre() + "," + 
				CD.getISBN() +
				");"
				);
	}

	/**
	 * Add a book to the database
	 * 
	 * @params book conn
	 */
	public static void addBook(Book book, Connection conn) {
		// TODO: Verify that we need this
				try {
					Class.forName("org.sqlite.JDBC");
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				}
				Statement stat;
				// TODO: where is the best place to cache conn?
				stat = conn.createStatement();
				// TODO: We should escape these values
				// TODO: Check the return value on this
				stat.executeQuery("insert into MOVIE values (" +
						book.getTitle() + "," + 
						book.getCover() + "," + 
						book.getYear() + "," + 
						book.getLength() + "," + 
						book.getPlot() + "," + 
						book.getAuthor() + "," + 
						book.getISBN() + "," +
						");"
						);
	}

	/**
	 * Update media in the database
	 * 
	 * @params media conn
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
		stat = conn.createStatement();
		// TODO: We should escape these values
		// TODO: Check the return value on this
		if (media instanceof Book) {
			// TODO: I don't think this is the right way to generalize this
			media = (Book) media;
			stat.executeQuery("update BOOK set" +
					"BOOK_ISBN = " + media.getISBN() + "," +
					"BOOK_TITLE = " + media.getTitle() + "," +
					"BOOK_YEAR = " + media.getYear() + "," +
					"BOOK_PLOT = " + media.getPlot() + "," +
					"BOOK_LENGTH_PAGES = " + media.getLength() + "," +
					"BOOK_COVER_FILEPATH = " + media.getCover() + "," +
					"BOOK_AUTHOR_ID = " + media.getAuthor() +
					"where BOOK_ID = " + media.getId() +
					";"
			);
		}
		if (media instanceof CD) {
			// TODO: We should escape these values
			// TODO: Check the return value on this
				// TODO: I don't think this is the right way to generalize this
				media = (CD) media;
				stat.executeQuery("update CD set" +
						"CD_ISBN = " + media.getISBN() + "," +
						"CD_TITLE = " + media.getTitle() + "," +
						"CD_COVER = " + media.getCover() + "," +
						"CD_AUTHOR_ID = " + media.getAuthor() +
						"where CD_ID = " + media.getId() +
						";"
						);
		}
			
		if (media instanceof Movie) {
			// TODO: We should escape these values
			// TODO: Check the return value on this
			if (media instanceof Movie) {
				// TODO: I don't think this is the right way to generalize this
				media = (Movie) media;
				stat.executeQuery("update MOVIE set" +
						"MOVIE_ISBN = " + media.getISBN() + "," +
						"MOVIE_TITLE = " + media.getTitle() + "," +
						"MOVIE_YEAR = " + media.getYear() + "," +
						"MOVIE_PLOT = " + media.getPlot() + "," +
						"MOVIE_CAST = " + media.getCast() + "," +
						"MOVIE_LENGTH_MINUTES = " + media.getLength() + "," +
						"MOVIE_COVER_FILEPATH = " + media.getCover() + "," +
						"MOVIE_AUTHOR_ID = " + media.getAuthor() +
						"MOVIE_GENRE_ID = " + media.getGenre() +
						"MOVIE_LANGUAGE_ID = " + media.getLanguage() +
						"MOVIE_COUNTRY_ID = " + media.getCountry() +
						"where MOVIE_ID = " + media.getId() +
						";"
						);					
			}
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
		stat = conn.createStatement();
		// TODO: We should escape these values
		// TODO: Check the return value on this
		if (media instanceof Book) {
			media = (Book) media;
			stat.executeQuery("delete from BOOK" +
					"where BOOK_ID = " + media.getId();
		}
		if (media instanceof CD) {
			media = (CD) media;
			stat.executeQuery("delete from CD" +
					"where CD_ID = " + media.getId();
		}
		if (media instanceof Movie) {
			media = (Movie) media;
			stat.executeQuery("delete from MOVIE" +
					"where MOVIE_ID = " + media.getId();
		}
	}
}
