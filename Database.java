package media;

import java.sql.Connection;

/*
 * CSC 478
 * Team2
 * Database.java
 * Purpose: Provide database controller to abstract away any logic that needs to happen before making executing sql
 * 
 * @author Karissa (Nash) Stisser, Jeremy Egner, Yuji Tsuzuki
 * @version 0.0.2 3/3/2015
 */
public class Database {
	
	/**
	 * Add a movie to the database
	 * 
	 * @params 	movie 
	 * 			conn
	 */
	public static void addMovie(Movie movie, Connection conn) {
		int author = 1;//TODO:movie.getAuthor();
		int genre = 1;//TODO:movie.getGenre();
		int language = 1;//TODO:movie.getLanguage();
		int country = 1;//TODO:movie.getCountry()
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
		int author = 1;//TODO:cd.getAuthor();
		DBController.addCD(conn, cd.getISBN(), cd.getTitle(), cd.getCover(), author);
	}
	
	/**
	 * Add a book to the database
	 * 
	 * @params 	book 
	 * 			conn
	 */
	public static void addBook(Book book, Connection conn) {
		int author = 1;//TODO:book.getAuthor();
		DBController.addBook(conn, book.getISBN(), book.getTitle(), book.getYear(), book.getPlot(), book.getLength(),
				book.getCover(), author);
	}
	
	/**
	 * Update media in the database
	 * 
	 * @params 	media 
	 * 			conn
	 */
	public static void update(Media media, Connection conn) {
		DBController.update(media, conn);
	}
	
	/**
	 * Delete media from the database
	 * 
	 * @params 	media 
	 * 			conn
	 */
	public static void delete(Media media, Connection conn) {
		DBController.delete(media, conn);
	}
}
