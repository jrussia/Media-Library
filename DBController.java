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
	
	// Database db;	// if we need to store a reference to the database
	
	/**
	 * Constructor
	 */
	public DBController(/* Database db */) {
		//	this.db = db;
	}
	
	/**
	 * Add a movie to the database
	 * 
	 * @params	movie
	 * 			conn
	 */
	public static void addMovie(Movie movie, Connection conn) {
		
	}
	
	/**
	 * Add a CD to the database
	 * 
	 * @params	cd
	 * 			conn
	 */
	public static void addCD(CD cd, Connection conn) {
		
	}
	
	/**
	 * Add a book to the database
	 * 
	 * @params	book
	 * 			conn
	 */
	public static void addBook(Book book, Connection conn) {
		
	}
	
	/**
	 * Update media in the database
	 * 
	 * @params	media
	 * 			conn
	 */
	public static void update(Media media, Connection conn) {
		
	}
	
	/**
	 * Delete media from the database
	 * 
	 * @params	media
	 * 			conn
	 */
	public static void delete(Media media, Connection conn) {
		
	}
}
