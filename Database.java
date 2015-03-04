package media;

import java.sql.Connection;

/*
 * CSC 478
 * Team2
 * DBController.java
 * Purpose: Provide database controller to abstract away any logic that needs to happen before making executing sql
 * 
 * @author Karissa (Nash) Stisser, Jeremy Egner, Yuji Tsuzuki
 * @version 0.0.1 3/3/2015
 */
public class Database {
	
	/**
	 * Add a movie to the database
	 * 
	 * @params 	movie 
	 * 			conn
	 */
	public static void addMovie(Movie movie, Connection conn) {
		DBController.addMovie(movie,  conn);
	}
	
	/**
	 * Add a CD to the database
	 * 
	 * @params 	cd 
	 * 			conn
	 */
	public static void addCD(CD cd, Connection conn) {
		DBController.addCD(cd, conn);
	}
	
	/**
	 * Add a book to the database
	 * 
	 * @params 	book 
	 * 			conn
	 */
	public static void addBook(Book book, Connection conn) {
		DBController.addBook(book, conn);
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
