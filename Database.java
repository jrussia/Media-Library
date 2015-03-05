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
	 * Add a movie to the database
	 * 
	 * @params 	movie 
	 * 			conn
	 */
	public static void addMovie(Movie movie, Connection conn) {
		int author = DBController.lookup(conn, movie.getAuthor(), table.AUTHOR);
		int genre = DBController.lookup(conn, movie.getGenre(), table.GENRE);
		int language = DBController.lookup(conn, movie.getLanguage(), table.LANGUAGE);
		int country = DBController.lookup(conn, movie.getLanguage(), table.COUNTRY);
		DBController.addMovie(conn, movie.getISBN(), movie.getTitle(), movie.getCover(), movie.getYear(), 
				movie.getLength(), movie.getPlot(), movie.getCast(), author, genre, language, country);
	}
	

	/**
	 * Add a CD to the database
	 * 
	 * @params 	cd 
	 * 			conn
	 */
	public static void addCD(CD cd, Connection conn) {
		int author = DBController.lookup(conn, cd.getAuthor(), table.AUTHOR);
		DBController.addCD(conn, cd.getISBN(), cd.getTitle(), cd.getCover(), author);
	}
	
	/**
	 * Add a book to the database
	 * 
	 * @params 	book 
	 * 			conn
	 */
	public static void addBook(Book book, Connection conn) {
		int author = DBController.lookup(conn, book.getAuthor(), table.AUTHOR);
		DBController.addBook(conn, book.getISBN(), book.getTitle(), book.getYear(), book.getPlot(), book.getLength(),
				book.getCover(), author);
	}
	
	/**
	 * Update media in the database
	 * @throws Exception 
	 * @params 	media 
	 * 			conn
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
	
	private static void updateBook(Book book, Connection conn) {
		int author = DBController.lookup(conn, book.getAuthor(), table.AUTHOR);
		DBController.updateBook(conn, book.getISBN(), book.getTitle(), book.getYear(), book.getPlot(), book.getLength(), 
				book.getCover(), author, book.getId());
	}
	
	private static void updateCD(CD cd, Connection conn) {
		int author = DBController.lookup(conn, cd.getAuthor(), table.AUTHOR);
		DBController.updateCD(conn, cd.getISBN(), cd.getTitle(), cd.getCover(), author, cd.getId());
	}
	
	private static void updateMovie(Movie movie, Connection conn) {
		int author = DBController.lookup(conn, movie.getAuthor(), table.AUTHOR);
		int genre = DBController.lookup(conn, movie.getGenre(), table.AUTHOR);
		int language = DBController.lookup(conn, movie.getLanguage(), table.AUTHOR);
		int country = DBController.lookup(conn, movie.getCountry(), table.AUTHOR);
		DBController.updateMovie(conn, movie.getISBN(), movie.getTitle(), movie.getYear(), movie.getPlot(), movie.getCast(), 
				movie.getLength(), movie.getCover(), author, genre, language, country, movie.getId());
	}
	
	/**
	 * Delete media from the database
	 * @throws Exception 
	 * @params 	media 
	 * 			conn
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
