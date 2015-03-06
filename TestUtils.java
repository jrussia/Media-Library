package media;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
 * CSC 478
 * Team2
 * Database.java
 * Purpose: Test utils
 * 
 * @author Karissa (Nash) Stisser, Jeremy Egner, Yuji Tsuzuki
 * @version 0.0.1 3/5/2015
 */
public class TestUtils {
	public static void printMovies() throws SQLException, ClassNotFoundException {
		String format = "%20s";
		System.out.print(String.format(format, "movie_id") + "|");
		System.out.print(String.format(format, "movie_isbn") + "|");
		System.out.print(String.format(format, "movie_title") + "|");
		System.out.print(String.format(format, "movie_cover_filepath") + "|");
		System.out.print(String.format(format, "movie_year") + "|");
		System.out.print(String.format(format, "movie_length_minutes") + "|");
		System.out.print(String.format(format, "movie_plot") + "|");
		System.out.print(String.format(format, "movie_cast") + "|");
		System.out.print(String.format(format, "movie_author_id") + "|");
		System.out.print(String.format(format, "movie_genre_id") + "|");
		System.out.print(String.format(format, "movie_language_id") + "|");
		System.out.print(String.format(format, "movie_country_id") + "|");
		
		Class.forName("org.sqlite.JDBC");
		Connection conn = DriverManager.getConnection("jdbc:sqlite:database.db");
		PreparedStatement stat = conn.prepareStatement("select * from movie");
		
		ResultSet results = stat.executeQuery();
		
		while (results.next()) {
			System.out.println();
			System.out.print(String.format(format, results.getInt(1)) + "|");		// movie_id
			System.out.print(String.format(format, results.getString(2)) + "|");	// movie_isbn
			System.out.print(String.format(format, results.getString(3)) + "|");	// movie_title
			System.out.print(String.format(format, results.getString(4)) + "|");		//	movie_cover_filepath
			System.out.print(String.format(format, results.getString(5)) + "|");		// movie_year
			System.out.print(String.format(format, results.getString(6)) + "|");		// movie_length_minutes
			System.out.print(String.format(format, results.getString(7)) + "|");		// movie_plot
			System.out.print(String.format(format, results.getString(8)) + "|");		// movie_cast
			System.out.print(String.format(format, results.getInt(9)) + "|");		// movie_author_id
			System.out.print(String.format(format, results.getInt(10)) + "|");	// movie_genre_id
			System.out.print(String.format(format, results.getInt(11)) + "|");	// movie_language_id
			System.out.print(String.format(format, results.getInt(12)) + "|");	// movie_country_id
		}
	}
	
	public static void clearMovies() throws SQLException, ClassNotFoundException {
		Class.forName("org.sqlite.JDBC");
		Connection conn = DriverManager.getConnection("jdbc:sqlite:database.db");
		PreparedStatement stat = conn.prepareStatement("delete from movie");
		stat.execute();
	}
	
	/*
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		printMovies();
	}
	*/
}
