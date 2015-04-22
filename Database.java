package media;

import java.sql.SQLException;

/***
 * CSC 478
 * Team2
 * Database.java
 * Purpose: Provide API layer to save, update, and delete media entries.
 * 
 * @author Karissa (Nash) Stisser, Jeremy Egner, Yuji Tsuzuki
 * @version 0.2.0 4/20/2015
 */
public class Database {
	
	enum table {
		AUTHOR, GENRE, LANGUAGE, COUNTRY
	}
	
	/**
	 * Add a movie to the database.
	 * 
	 * @param movie		The movie to add
	 * @return			0 if the movie was added, 1 if there was an error
	 */
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
	 * Add a CD to the database.
	 * 
	 * @param cd	the CD to add
	 * @return		0 if successful, 1 if there was an error
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
	 * Add a book to the database.
	 * 
	 * @param book	the book to add
	 * @return		0 if the add was successful, 1 if there was an error
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
	 * Update media values in the database.
	 * 
	 * @param media		the media to update.
	 * @return			0 if the update was successful, 1 or 2 if there was an error
	 */
	public static int update(Media media) {
		try {
			if (media instanceof Book)
				updateBook((Book) media);
			else if (media instanceof CD)
				updateCD((CD) media);
			else if (media instanceof Movie)
				updateMovie((Movie) media);
			} catch (SQLException se) {
				return 1;
			} catch (Exception e) {
				return 2;
			}
		return 0;
	}
	
	/**
	 * Delete an item from the database.
	 * @param 	media	the media to delete from the database.
	 */
	public static void delete(Media media) throws Exception {
		if (media instanceof Book)
			DBController.deleteBook(media.getId());
		else if (media instanceof CD)
			DBController.deleteCD(media.getId());
		else if (media instanceof Movie)
			DBController.deleteMovie(media.getId());
	}
	
	/**
	 * Helper method to update a book in the database.
	 * 
	 * @param book 				the book to update
	 * @throws SQLException 	when there was a problem with the database
	 */
	private static void updateBook(Book book) throws SQLException {
		Integer author, genre;
		author = DBController.lookup(book.getAuthor(), table.AUTHOR);
		genre = DBController.lookup(book.getGenre(), table.GENRE);
		DBController.updateBook(book.getISBN(), book.getTitle(), book.getYear(), book.getPlot(), book.getLength(), 
				book.getCover(), genre, author, book.getId());
	}
	
	/**
	 * Helper function to add a CD to the database.
	 * 
	 * @param cd				the CD to add
	 * @throws SQLException 	when there was a problem with the database.
	 */
	private static void updateCD(CD cd) throws SQLException {
		Integer author, genre;
		author = DBController.lookup(cd.getAuthor(), table.AUTHOR);
		genre = DBController.lookup(cd.getGenre(), table.GENRE);
		DBController.updateCD(cd.getISBN(), cd.getTitle(), genre, cd.getCover(), author, cd.getId());
	}
	
	/**
	 * Helper function to add a movie to the database.
	 * 
	 * @param movie				the movie to add
	 * @throws SQLException 	when there was a problem with the database
	 */
	private static void updateMovie(Movie movie) throws SQLException {
		Integer author = DBController.lookup(movie.getAuthor(), table.AUTHOR);
		Integer genre = DBController.lookup(movie.getGenre(), table.AUTHOR);
		Integer language = DBController.lookup(movie.getLanguage(), table.AUTHOR);
		Integer country = DBController.lookup(movie.getCountry(), table.AUTHOR);
		DBController.updateMovie(movie.getISBN(), movie.getTitle(), movie.getYear(), movie.getPlot(), movie.getCast(), 
				movie.getLength(), movie.getCover(), author, genre, language, country, movie.getId());
	}
}
