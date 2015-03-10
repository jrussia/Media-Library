package media;

import java.sql.*;
import java.util.LinkedList;

/*
 * CSC 478
 * Team2
 * search.java
 * Purpose: To abstract database search functions.
 * 
 * @author Karissa (Nash) Stisser, Jeremy Egner, Yuji Tsuzuki
 * @version 1.1.2 3/7/15
 */

public class search {
	/*Performs a search of the movie table and returns movies for the given sql query*/
	public static Movie[] searchMovies(String sqlStatement, Connection conn){
		LinkedList movieList = new LinkedList();

		try {
			Statement stat_movie = conn.createStatement();
			ResultSet match_movie = stat_movie.executeQuery(sqlStatement);
			
			while(match_movie.next()){
				byte[] cover = match_movie.getBytes("movie_cover");
				String isbn = match_movie.getString("movie_isbn");
				String title = match_movie.getString("movie_title");
				String author = match_movie.getString("author");
				String genre = match_movie.getString("genre");
				String year = match_movie.getString("movie_year");
				String length = match_movie.getString("movie_length_minutes");
				String language = match_movie.getString("language");
				String country = match_movie.getString("country");
				String cast = match_movie.getString("movie_cast");
				String plot = match_movie.getString("movie_plot");
				
				//TODO put parameters in in the order of the object
				Movie movie = new Movie(title, author, isbn, genre, cover, year, plot, cast, length, language, country);
				movieList.add(movie);
			}
			Movie [] movieArray = new Movie[movieList.size()];
			for (int n = 0; n < movieList.size(); n++){
				movieArray[n] = (Movie) movieList.get(n);
			}
			return movieArray;
		}
	catch (SQLException e) {
		e.printStackTrace();
	}
		return null;
	}
	
	public static Movie[] searchMoviesWithID(String sqlStatement, Connection conn){
		LinkedList movieList = new LinkedList();

		try {
			Statement stat_movie = conn.createStatement();
			ResultSet match_movie = stat_movie.executeQuery(sqlStatement);
			
			while(match_movie.next()){
				int id = match_movie.getInt("movie_id");
				byte[] cover = match_movie.getBytes("movie_cover");
				String isbn = match_movie.getString("movie_isbn");
				String title = match_movie.getString("movie_title");
				String author = match_movie.getString("author");
				String genre = match_movie.getString("genre");
				String year = match_movie.getString("movie_year");
				String length = match_movie.getString("movie_length_minutes");
				String language = match_movie.getString("language");
				String country = match_movie.getString("country");
				String cast = match_movie.getString("movie_cast");
				String plot = match_movie.getString("movie_plot");
				
				//TODO put parameters in in the order of the object
				Movie movie = new Movie(id, title, author, isbn, genre, cover, year, plot, cast, length, language, country);
				movieList.add(movie);
			}
			Movie [] movieArray = new Movie[movieList.size()];
			for (int n = 0; n < movieList.size(); n++){
				movieArray[n] = (Movie) movieList.get(n);
			}
			return movieArray;
		}
	catch (SQLException e) {
		e.printStackTrace();
	}
		return null;
	}
	
	/*return all movies in the database*/	
	public static Movie[] getAllMovies(Connection conn){
		String sql_movie = "SELECT movie_cover, movie_isbn, movie_title, "
				+ "author.author, genre.genre, movie_year, movie_length_minutes, " 
				+ "language.language, country.country, movie_cast, movie_plot FROM movie "
				+ "INNER JOIN author on author.author_id = movie.movie_author_id "
				+ "INNER JOIN language on language.language_id = movie.movie_language_id "
				+ "INNER JOIN country on country.country_id = movie.movie_country_id "
				+ "INNER JOIN genre on genre.genre_id = movie_genre_id";
		
		Movie[] movies = searchMovies(sql_movie, conn);
		return movies;
	}
	
	/*Performs a search of the book table and returns books for the given sql query*/
	public static Book[] searchBooks(String sqlStatement, Connection conn){
		LinkedList bookList = new LinkedList();
		try {
			Statement stat_book = conn.createStatement();
			ResultSet match_book = stat_book.executeQuery(sqlStatement);
			
			while(match_book.next()){
				byte[] cover = match_book.getBytes("book_cover");
				String isbn = match_book.getString("book_isbn");
				String title = match_book.getString("book_title");
				String author = match_book.getString("author");
				String year = match_book.getString("book_year");
				String plot = match_book.getString("book_plot");
				String length = match_book.getString("book_length_pages");
				String genre = match_book.getString("genre");
				
				//TODO put parameters in in the order of the object
				Book book = new Book(title, author, isbn, genre, cover, year, plot, length);
				bookList.add(book);
			}
			Book [] bookArray = new Book[bookList.size()];
			for (int n = 0; n < bookList.size(); n++){
				bookArray[n] = (Book) bookList.get(n);
			}
			return bookArray;
		}
	catch (SQLException e) {
		e.printStackTrace();
	}
		return null;
	}
	
	/*Performs a search of the book table and returns books with ID for the given sql query*/
	public static Book[] searchBooksWithID(String sqlStatement, Connection conn){
		LinkedList bookList = new LinkedList();
		try {
			Statement stat_book = conn.createStatement();
			ResultSet match_book = stat_book.executeQuery(sqlStatement);
			
			while(match_book.next()){
				int id = match_book.getInt("book_id");
				byte[] cover = match_book.getBytes("book_cover");
				String isbn = match_book.getString("book_isbn");
				String title = match_book.getString("book_title");
				String author = match_book.getString("author");
				String year = match_book.getString("book_year");
				String plot = match_book.getString("book_plot");
				String length = match_book.getString("book_length_pages");
				String genre = match_book.getString("genre");
				
				//TODO put parameters in in the order of the object
				Book book = new Book(id, title, author, isbn, genre, cover, year, plot, length);
				bookList.add(book);
			}
			Book [] bookArray = new Book[bookList.size()];
			for (int n = 0; n < bookList.size(); n++){
				bookArray[n] = (Book) bookList.get(n);
			}
			return bookArray;
		}
	catch (SQLException e) {
		e.printStackTrace();
	}
		return null;
	}
	
	/*return all books in the database*/
	public static Book[] getAllBooks(Connection conn){
		String sql_book = "SELECT book_cover, book_isbn, book_title, "
				+ "author.author, book_year, book_plot, book_length_pages, genre.genre "
				+ "FROM book INNER JOIN author on author.author_id = book.book_author_id "
				+ "INNER JOIN genre on genre.genre_id = book.book_genre_id";
		
		Book[] books = searchBooks(sql_book, conn);
		return books;
	}
	
	/*Performs a search of the CD table and returns CDs for the given sql query*/
	public static CD[] searchCDs(String sqlStatement, Connection conn){
		LinkedList cdList = new LinkedList();
		
		try {
			Statement stat_CD = conn.createStatement();
			ResultSet match_CD = stat_CD.executeQuery(sqlStatement);
			
			while(match_CD.next()){
				byte[] cover = match_CD.getBytes("cd_cover");
				String isbn = match_CD.getString("cd_isbn");
				String author = match_CD.getString("author");
				String title = match_CD.getString("cd_title");
				String genre = match_CD.getString("genre");
				
				//TODO put parameters in in the order of the object
				CD cd = new CD(title, author, isbn, genre, cover);
				cdList.add(cd);
			}
			
			CD [] CDArray = new CD[cdList.size()];
			for (int n = 0; n < cdList.size(); n++){
				CDArray[n] = (CD) cdList.get(n);
			}
			return CDArray;
		}
	catch (SQLException e) {
		e.printStackTrace();
	}
		return null;
	}
	
	/*Performs a search of the CD table and returns CDs for the given sql query*/
	public static CD[] searchCDsWithID(String sqlStatement, Connection conn){
		LinkedList cdList = new LinkedList();
		
		try {
			Statement stat_CD = conn.createStatement();
			ResultSet match_CD = stat_CD.executeQuery(sqlStatement);
			
			while(match_CD.next()){
				int id = match_CD.getInt("cd_id");
				byte[] cover = match_CD.getBytes("cd_cover");
				String isbn = match_CD.getString("cd_isbn");
				String author = match_CD.getString("author");
				String title = match_CD.getString("cd_title");
				String genre = match_CD.getString("genre");
				
				//TODO put parameters in in the order of the object
				CD cd = new CD(id, title, author, isbn, genre, cover);
				cdList.add(cd);
			}
			
			CD [] CDArray = new CD[cdList.size()];
			for (int n = 0; n < cdList.size(); n++){
				CDArray[n] = (CD) cdList.get(n);
			}
			return CDArray;
		}
	catch (SQLException e) {
		e.printStackTrace();
	}
		return null;
	}
	
	
	/*return all cds in the database*/
	public static CD[] getAllCDs(Connection conn){
		
		String sql_CD = "SELECT cd_cover, cd_isbn, cd_title, genre.genre, author.author "
				+ "from cd INNER JOIN genre on genre.genre_id = cd_genre_id "
				+ "INNER JOIN author on author.author_id = cd_author_id";
		
		CD[] cds = searchCDs(sql_CD, conn);
		return cds;
	}
	
	/*Search the database */
	public static Media[][] searchDatabase(String searchString, Connection conn){
		searchString = searchString.toLowerCase();
		String sql_movie = "SELECT movie_cover, movie_isbn, movie_title, "
				+ "author.author, genre.genre, movie_year, movie_length_minutes, " 
				+ "language.language, country.country, movie_cast, movie_plot FROM movie "
				+ "INNER JOIN author on author.author_id = movie.movie_author_id "
				+ "INNER JOIN language on language.language_id = movie.movie_language_id "
				+ "INNER JOIN country on country.country_id = movie.movie_country_id "
				+ "INNER JOIN genre on genre.genre_id = movie_genre_id "
				+ "WHERE movie_isbn LIKE '%" + searchString + "%' "
				+ "OR movie_title LIKE '%" + searchString + "%' "
				+ "OR author.author LIKE '%" + searchString + "%' "
				+ "OR genre.genre LIKE '%" + searchString + "%' "
				+ "OR movie_year LIKE '%" + searchString + "%' "
				+ "OR movie_length_minutes LIKE '%" + searchString + "%' "
				+ "OR language.language LIKE '%" + searchString + "%' "
				+ "OR country.country LIKE '%" + searchString + "%' "
				+ "OR movie_cast LIKE '%" + searchString + "%' "
				+ "OR movie_plot LIKE '%" + searchString + "%'";	
		
		String sql_book = "SELECT book_cover, book_isbn, book_title, "
				+ "author.author, book_year, book_plot, book_length_pages, genre.genre "
				+ "FROM book INNER JOIN author on author.author_id = book.book_author_id "
				+ "INNER JOIN genre on genre.genre_id = book.book_genre_id "
				+ "WHERE book_isbn LIKE '%" + searchString + "%' "
				+ "OR book_title LIKE '%" + searchString + "%' "
				+ "OR author.author LIKE '%" + searchString + "%' "
				+ "OR book_year LIKE '%" + searchString + "%' "
				+ "OR book_plot LIKE '%" + searchString + "%'";
		
		String sql_CD = "SELECT cd_cover, cd_isbn, author.author, cd_title, genre.genre from cd "
				+ "INNER JOIN genre on genre.genre_id = cd_genre_id "
				+ "INNER JOIN author on author.author_id = cd_author_id "
				+ "WHERE cd_isbn LIKE '%" + searchString + "%' "
				+ "OR author.author LIKE '%" + searchString + "%' "
				+ "OR cd_title LIKE '%" + searchString + "%' "
				+ "OR genre.genre LIKE '%" + searchString + "%'";		
		
		Movie[] movies = searchMovies(sql_movie, conn);
		Book[] books = searchBooks(sql_book, conn);
		CD[] cds = searchCDs(sql_CD, conn);
		Media[][] media = {movies, books, cds};
		return media;
	}
	
public static Media searchByISBN(String ISBN, Connection conn){
	String sql_movie = "SELECT movie_id, movie_cover, movie_isbn, movie_title, "
			+ "author.author, genre.genre, movie_year, movie_length_minutes, " 
			+ "language.language, country.country, movie_cast, movie_plot FROM movie "
			+ "INNER JOIN author on author.author_id = movie.movie_author_id "
			+ "INNER JOIN language on language.language_id = movie.movie_language_id "
			+ "INNER JOIN country on country.country_id = movie.movie_country_id "
			+ "INNER JOIN genre on genre.genre_id = movie_genre_id "
			+ "WHERE movie_isbn = '" + ISBN + "'";	
	
	String sql_book = "SELECT book_id, book_cover, book_isbn, book_title, "
			+ "author.author, book_year, book_plot, book_length_pages, genre.genre "
			+ "FROM book INNER JOIN author on author.author_id = book.book_author_id "
			+ "INNER JOIN genre on genre.genre_id = book.book_genre_id "
			+ "WHERE book_isbn = '" + ISBN + "'";
	
	String sql_CD = "SELECT cd_id, cd_cover, cd_isbn, author.author, cd_title, genre.genre from cd"
			+ "INNER JOIN genre on genre.genre_id = cd_genre_id "
			+ "INNER JOIN author on author.author_id = cd_author_id "
			+ "WHERE cd_isbn = '" + ISBN + "'";
	
	Movie[] movies = searchMoviesWithID(sql_movie, conn);
	Book[] books = searchBooksWithID(sql_book, conn);
	CD[] cds = searchCDsWithID(sql_CD, conn);
	
	if(movies.length > 0)
		return movies[0];
	else if(books.length > 0)
		return books[0];
	else if(cds.length > 0)
		return cds[0];
	else
		return null;
	}

public static Boolean checkForDuplicate(String match, String column, String media_type, Connection conn){
	match = match.toLowerCase();
	String sqlCountry = "SELECT country FROM country WHERE country = '" + match + "'";
	String sqlLanguage = "SELECT language FROM language WHERE language = '" + match + "'";
	String sqlAuthor = "SELECT author FROM author WHERE author = '" + match + "' and media_type = '" + media_type + "'";
	String sqlGenre = "SELECT genre FROM genre WHERE genre = '" + match + "' and media_type = '" + media_type + "'";
	LinkedList<String> list = new LinkedList<String>();
	System.out.println("checking" + match + column);
	try {
		if(column.equals("country")){
			Statement stat = conn.createStatement();
			ResultSet results = stat.executeQuery(sqlCountry);
			
			while(results.next()){
				list.add("match");
			}
		}
		else if(column.equals("language")){
			Statement stat = conn.createStatement();
			ResultSet results = stat.executeQuery(sqlLanguage);
			
			while(results.next()){
				list.add("match");
			}
		}
		else if(column.equals("author")){
			Statement stat = conn.createStatement();
			ResultSet results = stat.executeQuery(sqlAuthor);
			
			while(results.next()){
				list.add("match");
			}
		}
		else if(column.equals("genre")){
			Statement stat = conn.createStatement();
			ResultSet results = stat.executeQuery(sqlGenre);
			
			while(results.next()){
				list.add("match");
			}
		}
		else
			return null;
	}
	catch (SQLException e) {
		e.printStackTrace();
	}
	if(list.size() > 0)
		return true;
	else
		return false;
}
}
