package media;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/*
 * CSC 478
 * Team2
 * Database.java
 * Purpose: Provide database controller to abstract away any logic that needs to happen before making executing sql
 * 
 * @author Karissa (Nash) Stisser, Jeremy Egner, Yuji Tsuzuki
 * @version 0.1.0 3/4/2015
 */
public class Database {
	
	enum table {
		AUTHOR, GENRE, LANGUAGE, COUNTRY
	}
	
	/**
	 * 
	 * @param movie
	 * @param conn
	 * @throws SQLException
	 */

	public static void addMovie(Movie movie, Connection conn) throws SQLException {
		Integer author, genre, language, country;
		author = DBController.lookup(conn, movie.getAuthor(), table.AUTHOR);
		genre = DBController.lookup(conn, movie.getGenre(), table.GENRE);
		language = DBController.lookup(conn, movie.getLanguage(), table.LANGUAGE);
		country = DBController.lookup(conn, movie.getLanguage(), table.COUNTRY);
		
		DBController.addMovie(conn, movie.getISBN(), movie.getTitle(), movie.getCover(), movie.getYear(), 
				movie.getLength(), movie.getPlot(), movie.getCast(), author, genre, language, country);
	}
	

	/**
	 * 
	 * @param cd
	 * @param conn
	 * @throws SQLException
	 */
	public static void addCD(CD cd, Connection conn) throws SQLException {
		Integer author, genre;
		author = DBController.lookup(conn, cd.getAuthor(), table.AUTHOR);
		genre = DBController.lookup(conn, cd.getGenre(), table.GENRE);
		DBController.addCD(conn, cd.getISBN(), cd.getTitle(), genre, cd.getCover(), author);
	}
	
	/**
	 * 
	 * @param book
	 * @param conn
	 * @throws SQLException
	 */

	public static void addBook(Book book, Connection conn) throws SQLException {
		Integer author, genre;
		author = DBController.lookup(conn, book.getAuthor(), table.AUTHOR);
		genre = DBController.lookup(conn, book.getGenre(), table.GENRE);
		DBController.addBook(conn, book.getISBN(), book.getTitle(), book.getYear(), book.getPlot(), book.getLength(),
				book.getCover(), genre, author);
	}
	
	/**
	 * 
	 * @param media
	 * @param conn
	 * @throws Exception
	 */
	public static void update(Media media, Connection conn) throws Exception {
		if (media instanceof Book)
			updateBook((Book) media, conn);
		else if (media instanceof CD)
			updateCD((CD) media, conn);
		else if (media instanceof Movie)
			updateMovie((Movie) media, conn);
		else // should never reach this point unless we add new media types
			throw new java.lang.Exception("Unknown media type when trying to update.");
	}
	
	/**
	 * 
	 * @param book
	 * @param conn
	 * @throws SQLException 
	 */
	private static void updateBook(Book book, Connection conn) throws SQLException {
		Integer author, genre;
		author = DBController.lookup(conn, book.getAuthor(), table.AUTHOR);
		genre = DBController.lookup(conn, book.getGenre(), table.GENRE);
		DBController.updateBook(conn, book.getISBN(), book.getTitle(), book.getYear(), book.getPlot(), book.getLength(), 
				book.getCover(), genre, author, book.getId());
	}
	
	/**
	 * 
	 * @param cd
	 * @param conn
	 * @throws SQLException 
	 */
	private static void updateCD(CD cd, Connection conn) throws SQLException {
		Integer author, genre;
		author = DBController.lookup(conn, cd.getAuthor(), table.AUTHOR);
		genre = DBController.lookup(conn, cd.getGenre(), table.GENRE);
		DBController.updateCD(conn, cd.getISBN(), cd.getTitle(), genre, cd.getCover(), author, cd.getId());
	}
	
	/**
	 * 
	 * @param movie
	 * @param conn
	 * @throws SQLException 
	 */
	private static void updateMovie(Movie movie, Connection conn) throws SQLException {
		Integer author = DBController.lookup(conn, movie.getAuthor(), table.AUTHOR);
		Integer genre = DBController.lookup(conn, movie.getGenre(), table.AUTHOR);
		Integer language = DBController.lookup(conn, movie.getLanguage(), table.AUTHOR);
		Integer country = DBController.lookup(conn, movie.getCountry(), table.AUTHOR);
		// TODO: should the movie object come with an id, or should we look it up (same for other objects)
		DBController.updateMovie(conn, movie.getISBN(), movie.getTitle(), movie.getYear(), movie.getPlot(), movie.getCast(), 
				movie.getLength(), movie.getCover(), author, genre, language, country, movie.getId());
	}
	
	/**
	 * 
	 * @param media
	 * @param conn
	 * @throws Exception
	 */
	public static void delete(Media media, Connection conn) throws Exception {
		if (media instanceof Book)
			DBController.deleteBook(conn, media.getId());
		else if (media instanceof CD)
			DBController.deleteCD(conn, media.getId());
		else if (media instanceof Movie)
			DBController.deleteMovie(conn, media.getId());
		else // should never reach this point unless we add new media types
			throw new java.lang.Exception("Unknown media type when trying to delete.");
	}
}
