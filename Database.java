package media;

import java.sql.Connection;
import java.sql.SQLException;

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
		int author = DBController.lookup(conn, movie.getAuthor(), table.AUTHOR);
		int genre = DBController.lookup(conn, movie.getGenre(), table.GENRE);
		int language = DBController.lookup(conn, movie.getLanguage(), table.LANGUAGE);
		int country = DBController.lookup(conn, movie.getLanguage(), table.COUNTRY);
		DBController.addMovie(conn, movie.getISBN(), movie.getTitle(), movie.getCoverFilepath(), movie.getYear(), 
				movie.getLength(), movie.getPlot(), movie.getCast(), author, genre, language, country);
	}
	

	/**
	 * 
	 * @param cd
	 * @param conn
	 * @throws SQLException
	 */
	public static void addCD(CD cd, Connection conn) throws SQLException {
		int author = DBController.lookup(conn, cd.getAuthor(), table.AUTHOR);
		DBController.addCD(conn, cd.getISBN(), cd.getTitle(), cd.getCoverFilepath(), author);
	}
	
	/**
	 * 
	 * @param book
	 * @param conn
	 * @throws SQLException
	 */

	public static void addBook(Book book, Connection conn) throws SQLException {
		int author = DBController.lookup(conn, book.getAuthor(), table.AUTHOR);
		DBController.addBook(conn, book.getISBN(), book.getTitle(), book.getYear(), book.getPlot(), book.getLength(),
				book.getCoverFilepath(), author);
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
	 */
	private static void updateBook(Book book, Connection conn) {
		int author = DBController.lookup(conn, book.getAuthor(), table.AUTHOR);
		DBController.updateBook(conn, book.getISBN(), book.getTitle(), book.getYear(), book.getPlot(), book.getLength(), 
				book.getCoverFilepath(), author, book.getId());
	}
	
	/**
	 * 
	 * @param cd
	 * @param conn
	 */
	private static void updateCD(CD cd, Connection conn) {
		int author = DBController.lookup(conn, cd.getAuthor(), table.AUTHOR);
		DBController.updateCD(conn, cd.getISBN(), cd.getTitle(), cd.getCoverFilepath(), author, cd.getId());
	}
	
	/**
	 * 
	 * @param movie
	 * @param conn
	 */
	private static void updateMovie(Movie movie, Connection conn) {
		int author = DBController.lookup(conn, movie.getAuthor(), table.AUTHOR);
		int genre = DBController.lookup(conn, movie.getGenre(), table.AUTHOR);
		int language = DBController.lookup(conn, movie.getLanguage(), table.AUTHOR);
		int country = DBController.lookup(conn, movie.getCountry(), table.AUTHOR);
		// TODO: should the movie object come with an id, or should we look it up (same for other objects)
		DBController.updateMovie(conn, movie.getISBN(), movie.getTitle(), movie.getYear(), movie.getPlot(), movie.getCast(), 
				movie.getLength(), movie.getCoverFilepath(), author, genre, language, country, movie.getId());
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
