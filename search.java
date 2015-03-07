package media;

import java.sql.*;
import java.util.LinkedList;

public class search {
	/*return all movies in the database*/
	public static Movie[] getAllMovies(Connection conn){
		LinkedList movieList = new LinkedList();

		try {
			String sql_movie = "SELECT movie_cover_filepath, movie_isbn, movie_title, "
					+ "author.author, genre.genre, movie_year, movie_length_minutes, " 
					+ "language.language, country.country, movie_cast, movie_plot FROM movie"
					+ "INNER JOIN author on author.author_id = movie.movie_author_id"
					+ "INNER JOIN language on language.language_id = movie.movie_language_id"
					+ "INNER JOIN country on country.country_id = movie.movie_country_id"
					+ "INNER JOIN genre on genre.genre_id = movie_genre_id";
			Statement stat_movie = conn.createStatement();
			ResultSet match_movie = stat_movie.executeQuery(sql_movie);
			
			while(match_movie.next()){
				String cover_filepath = match_movie.getString("movie_cover_filepath");
				int isbn = match_movie.getInt("movie_isbn");
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
				Movie movie = new Movie(parameters);
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
	
	/*return all books in the database*/
	public static Book[] getAllBooks(Connection conn){
		LinkedList bookList = new LinkedList();
		try {
			String sql_book = "SELECT book_cover_filepath, book_isbn, book_title, "
					+ "author.author, book.year, book.plot, book_length_pages, genre.genre "
					+ "FROM book INNER JOIN author on author.author_id = book.book_author_id"
					+ "INNER JOIN genre on genre.genre_id = book.book_genre_id";
			Statement stat_book = conn.createStatement();
			ResultSet match_book = stat_book.executeQuery(sql_book);
			
			while(match_book.next()){
				String cover_filepath = match_book.getString("book_cover_filepath");
				int isbn = match_book.getInt("book_isbn");
				String title = match_book.getString("book_title");
				String author = match_book.getString("author");
				String year = match_book.getString("book_year");
				String plot = match_book.getString("book_plot");
				String length = match_book.getString("book_length_pages");
				String genre = match_book.getString("genre");
				
				//TODO put parameters in in the order of the object
				Book book = new Book(parameters);
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
	/*return all cds in the database*/
	public static CD[] getAllCDs(Connection conn){
		LinkedList cdList = new LinkedList();
		
		try {
			String sql_CD = "SELECT cd_cover_filepath, cd_isbn, author.author, cd_title, genre.genre"
					+ "INNER JOIN genre on genre.genre_id = cd_genre_id"
					+ "INNER JOIN author on author.author_id = cd_author_id";
			Statement stat_CD = conn.createStatement();
			ResultSet match_CD = stat_CD.executeQuery(sql_CD);
			
			while(match_CD.next()){
				String cover_filepath = match_CD.getString("cd_cover_filepath");
				int cd_isbn = match_CD.getInt("cd_isbn");
				String author = match_CD.getString("author");
				String title = match_CD.getString("cd_title");
				String genre = match_CD.getString("genre");
				
				//TODO put parameters in in the order of the object
				CD cd = new CD(parameters);
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
	
	public static Media[] searchDatabase(Connection conn){
		
		return null;
	}
}
