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
// TODO: add business logic to a higher level class? Or to one that extends this?
public class DBController {

	// Database db; // if we need to store a reference to the database

	/**
	 * Constructor
	 */
	public DBController(/* Database db */) {
		// this.db = db;
	}
	
	/**
	 * Stringify a string for database entry
	 * 
	 * @params	string
	 */
	private static String stringify(String s) {
		return "'" + s + "'";
	}

	/**
	 * Add a movie to the database
	 * 
	 * @params 	movie 
	 * 			conn
	 */
	public static void addMovie(Movie movie, Connection conn) {
		// TODO: Verify that we need this
		try {
			Class.forName("org.sqlite.JDBC");
		
		Statement stat;
		// TODO: where is the best place to cache conn, here, or in the calling function?
		stat = conn.createStatement();
		// TODO: We should escape these values
		// TODO: Check the return value on this?
		// TODO: Specify column names? so as to avoid
		String sql = "insert into MOVIE " + "values (" +
				null + "," +
				stringify(movie.getISBN()) + "," +
				stringify(movie.getTitle()) + "," + 
				stringify(movie.getCover()) + "," + 
				stringify(movie.getYear()) + "," + 
				stringify(movie.getLength()) + "," + 
				stringify(movie.getPlot()) + "," +
				stringify(movie.getCast()) + "," + 
				stringify(movie.getAuthor()) + "," +
				stringify(movie.getGenre()) + "," + 
				stringify(movie.getLanguage()) + "," +
				stringify(movie.getCountry()) + 
				")";
		stat.execute(sql);
	}
	catch (ClassNotFoundException e1) {
		e1.printStackTrace();
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}

	/**
	 * Add a CD to the database
	 * 
	 * @params cd conn
	 */
	public static void addCD(CD cd, Connection conn) {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		Statement stat;
		// TODO: where is the best place to cache conn?
		// TODO: Probably throw this up to the GUI
		try {
			stat = conn.createStatement();
		// TODO: We should escape these values
		// TODO: Check the return value on this
		
			stat.execute("insert into CD " + "values (" +
					null +
					stringify(cd.getTitle()) + "," + 
					stringify(cd.getCover()) + "," + 
					//CD.getYear() + "," + 
					stringify(cd.getAuthor()) + "," +
					//movie.getGenre() + "," + 
					stringify(cd.getISBN()) +
					")"
					);
		
	} catch (SQLException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	}
	/**
	 * Add a book to the database
	 * 
	 * @params book conn
	 */
	public static void addBook(Book book, Connection conn) {
		// TODO: Verify that we need this
				try {
					Class.forName("org.sqlite.JDBC");
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				}
				Statement stat;
				// TODO: where is the best place to cache conn?
				// TODO: probably throw this up to the GUI
				try {
					stat = conn.createStatement();
				
				// TODO: We should escape these values
				// TODO: Check the return value on this
				
					stat.execute("insert into MOVIE values (" +
							null +
							stringify(book.getTitle()) + "," + 
							stringify(book.getCover()) + "," + 
							stringify(book.getYear()) + "," + 
							stringify(book.getLength()) + "," + 
							stringify(book.getPlot()) + "," + 
							stringify(book.getAuthor()) + "," + 
							stringify(book.getISBN()) + "," +
							")"
							);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	}

	/**
	 * Update media in the database
	 * 
	 * @params media conn
	 */
	public static void update(Media media, Connection conn) {
		// TODO: Verify that we need this
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		Statement stat;
		// TODO: where is the best place to cache conn?
		// TODO: Probably throw this up to the GUI
		try {
			stat = conn.createStatement();
		// TODO: We should escape these values
		// TODO: Check the return value on this
		if (media instanceof Book) {
			// TODO: I don't think this is the right way to generalize this
			Book book = (Book) media;
			
				stat.execute("update BOOK set " +
						"BOOK_ISBN = " + stringify(book.getISBN()) + "," +
						"BOOK_TITLE = " + stringify(book.getTitle()) + "," +
						"BOOK_YEAR = " + stringify(book.getYear()) + "," +
						"BOOK_PLOT = " + stringify(book.getPlot()) + "," +
						"BOOK_LENGTH_PAGES = " + stringify(book.getLength()) + "," +
						"BOOK_COVER_FILEPATH = " + stringify(book.getCover()) + "," +
						"BOOK_AUTHOR_ID = " + stringify(book.getAuthor()) +
						// Not sure if book_id should be a string
						"where BOOK_ID = " + stringify(book.getId())
				);
		
		}
		if (media instanceof CD) {
			// TODO: We should escape these values
			// TODO: Check the return value on this
				// TODO: I don't think this is the right way to generalize this
				CD cd = (CD) media;
			
					stat.execute("update CD set " +
							"CD_ISBN = " + stringify(cd.getISBN()) + "," +
							"CD_TITLE = " + stringify(cd.getTitle()) + "," +
							"CD_COVER = " + stringify(cd.getCover()) + "," +
							"CD_AUTHOR_ID = " + stringify(cd.getAuthor()) +
							"where CD_ID = " + stringify(cd.getId())
							);
			
		}
			
		if (media instanceof Movie) {
			// TODO: We should escape these values
			// TODO: Check the return value on this
			if (media instanceof Movie) {
				// TODO: I don't think this is the right way to generalize this
				Movie movie = (Movie) media;
				
					stat.execute("update MOVIE set " +
							"MOVIE_ISBN = " + stringify(movie.getISBN()) + "," +
							"MOVIE_TITLE = " + stringify(movie.getTitle()) + "," +
							"MOVIE_YEAR = " + stringify(movie.getYear()) + "," +
							"MOVIE_PLOT = " + stringify(movie.getPlot()) + "," +
							"MOVIE_CAST = " + stringify(movie.getCast()) + "," +
							"MOVIE_LENGTH_MINUTES = " + stringify(movie.getLength()) + "," +
							"MOVIE_COVER_FILEPATH = " + stringify(movie.getCover()) + "," +
							"MOVIE_AUTHOR_ID = " + stringify(movie.getAuthor()) +
							"MOVIE_GENRE_ID = " + stringify(movie.getGenre()) +
							"MOVIE_LANGUAGE_ID = " + stringify(movie.getLanguage()) +
							"MOVIE_COUNTRY_ID = " + stringify(movie.getCountry()) +
							"where MOVIE_ID = " + stringify(movie.getId())
							);
				
				}					
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Delete media from the database
	 * 
	 * @params media conn
	 */
	public static void delete(Media media, Connection conn) {
		// TODO: Verify that we need this
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		Statement stat;
		// TODO: where is the best place to cache conn?
		try {
			stat = conn.createStatement();

		// TODO: We should escape these values
		// TODO: Check the return value on this
		if (media instanceof Book) {
			Book book = (Book) media;
			try {
				stat.execute("delete from BOOK " +
						"where BOOK_ID = " + book.getId());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (media instanceof CD) {
			CD cd = (CD) media;
			try {
				stat.executeQuery("delete from CD " +
						"where CD_ID = " + cd.getId());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (media instanceof Movie) {
			Movie movie = (Movie) media;
			try {
				stat.executeQuery("delete from MOVIE " +
						"where MOVIE_ID = " + movie.getId());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/** tests
	public static void main(String[] args) {
		try {
			try {
				Class.forName("org.sqlite.JDBC");
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
			}
			Connection conn = DriverManager.getConnection("jdbc:sqlite:C:/Users/jeremy/workspace/Media Library/database.db");
			Movie m = new Movie();
			DBController.addMovie(m,  conn);
			Statement stat = conn.createStatement();
			// TODO: add utils for viewing a database
			// TODO: add utils for viewing the result of a query
			stat.executeQuery("select * from MOVIE");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	} */
}

