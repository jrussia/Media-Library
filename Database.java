package media;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/***
 * CSC 478
 * Team2
 * Database.java
 * Purpose: Provide API layer to save, update, and delete media entries.
 * 
 * @author Karissa (Nash) Stisser, Jeremy Egner, Yuji Tsuzuki
 * @version 0.2.0 4/15/2015
 */
public class Database {
	
	enum table {
		AUTHOR, GENRE, LANGUAGE, COUNTRY
	}
	
	/**
	 * 
	 * @param movie
	 * @return
	 */
	// TODO: remove conn parameter?
	public static int addMovie(Movie movie) {
		Integer author, genre, language, country;
		try {
			author = DBController.lookup(movie.getAuthor(), table.AUTHOR);
			genre = DBController.lookup(movie.getGenre(), table.GENRE);
			language = DBController.lookup(movie.getLanguage(), table.LANGUAGE);
			country = DBController.lookup(movie.getCountry(), table.COUNTRY);
		
			DBController.addMovie(movie.getISBN(), movie.getTitle(), movie.getCover(), movie.getYear(), 
				movie.getLength(), movie.getPlot(), movie.getCast(), author, genre, language, country);
		} catch (SQLException e) {
			return 1;
		}
		return 0;
	}
	

	/**
	 * 
	 * @param cd
	 * @return
	 */
	public static int addCD(CD cd) {
		Integer author, genre;
		try {
			author = DBController.lookup(cd.getAuthor(), table.AUTHOR);
			genre = DBController.lookup(cd.getGenre(), table.GENRE);
			DBController.addCD(cd.getISBN(), cd.getTitle(), genre, cd.getCover(), author);
		} catch (SQLException e) {
			return 1;
		}
		return 0;
	}
	
	/**
	 * 
	 * @param book
	 * @return
	 */
	public static int addBook(Book book) {
		Integer author, genre;
		try {
		author = DBController.lookup(book.getAuthor(), table.AUTHOR);
		genre = DBController.lookup(book.getGenre(), table.GENRE);
		DBController.addBook(book.getISBN(), book.getTitle(), book.getYear(), book.getPlot(), book.getLength(),
				book.getCover(), genre, author);
		} catch (SQLException e) {
			return 1;
		}
		return 0;
	}
	
	/**
	 * 
	 * @param media
	 * @throws Exception
	 */
	public static int update(Media media) {
		try {
			if (media instanceof Book)
				updateBook((Book) media);
			else if (media instanceof CD)
				updateCD((CD) media);
			else if (media instanceof Movie)
				updateMovie((Movie) media);
			else // should never reach this point unless we add new media types
				throw new java.lang.Exception("Unknown media type when trying to update.");
			} catch (SQLException se) {
				return 1;
			} catch (Exception e) {
				return 2;
			}
		return 0;
	}
	
	/**
	 * 
	 * @param media
	 */
	public static void delete(Media media) throws Exception {
		if (media instanceof Book)
			DBController.deleteBook(media.getId());
		else if (media instanceof CD)
			DBController.deleteCD(media.getId());
		else if (media instanceof Movie)
			DBController.deleteMovie(media.getId());
		else // should never reach this point unless we add new media types
			throw new java.lang.Exception("Unknown media type when trying to delete.");
	}
	
	/**
	 * 
	 * @param book
	 * @throws SQLException 
	 */
	private static void updateBook(Book book) throws SQLException {
		Integer author, genre;
		author = DBController.lookup(book.getAuthor(), table.AUTHOR);
		genre = DBController.lookup(book.getGenre(), table.GENRE);
		DBController.updateBook(book.getISBN(), book.getTitle(), book.getYear(), book.getPlot(), book.getLength(), 
				book.getCover(), genre, author, book.getId());
	}
	
	/**
	 * 
	 * @param cd
	 * @throws SQLException 
	 */
	private static void updateCD(CD cd) throws SQLException {
		Integer author, genre;
		author = DBController.lookup(cd.getAuthor(), table.AUTHOR);
		genre = DBController.lookup(cd.getGenre(), table.GENRE);
		DBController.updateCD(cd.getISBN(), cd.getTitle(), genre, cd.getCover(), author, cd.getId());
	}
	
	/**
	 * 
	 * @param movie
	 * @throws SQLException 
	 */
	private static void updateMovie(Movie movie) throws SQLException {
		Integer author = DBController.lookup(movie.getAuthor(), table.AUTHOR);
		Integer genre = DBController.lookup(movie.getGenre(), table.AUTHOR);
		Integer language = DBController.lookup(movie.getLanguage(), table.AUTHOR);
		Integer country = DBController.lookup(movie.getCountry(), table.AUTHOR);
		// TODO: should the movie object come with an id, or should we look it up (same for other objects)
		DBController.updateMovie(movie.getISBN(), movie.getTitle(), movie.getYear(), movie.getPlot(), movie.getCast(), 
				movie.getLength(), movie.getCover(), author, genre, language, country, movie.getId());
	}
}
