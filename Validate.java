package media;

/*
 * CSC 478
 * Team2
 * Validate.java
 * Purpose: To ensure all strings being entered in the database will process 
 * by adding a \'\\' in front of necessary strings
 * 
 * by Movie, Book, and CD
 * 
 * @author Karissa (Nash) Stisser, Jeremy Egner, Yuji Tsuzuki
 * @version 1.1.0 4/29/15
 */

public class Validate {
	public static Movie checkMovie(Movie movie){
		String title = movie.getTitle();
		
			title = title.replace("'", (""));
		String director = movie.getAuthor();
		
			director = director.replace("'", (""));
		String ISBN = movie.getISBN();
		
			ISBN = ISBN.replace("'", (""));
		String genre = movie.getGenre();
		
			genre = genre.replace("'", (""));
		String year = movie.getYear();
		
			year = year.replace("'", (""));
		String plot = movie.getPlot();
		
			plot = plot.replace("'", (""));
		String cast = movie.getCast();
		
			cast = cast.replace("'", (""));
		String length = movie.getLength();
		
			length = length.replace("'", (""));
		String language = movie.getLanguage();
		
			language = language.replace("'", (""));
		String country = movie.getCountry();
		
			country = country.replace("'", (""));
		Movie movie2 = new Movie(title, director, ISBN, genre, movie.getCover(), year, plot, cast, length, language, country);
		return movie2;
	}
	public static Book checkBook(Book book){
		String title = book.getTitle();
		
		title = title.replace("'", (""));
		System.out.println(title);
		String author = book.getAuthor();
		
		author	= author.replace("'", (""));
		String ISBN = book.getISBN();
		
		ISBN =	ISBN.replace("'", (""));
		String genre = book.getGenre();
		
		genre =	genre.replace("'", (""));
		String year = book.getYear();
		
		year =	year.replace("'", (""));
		String plot = book.getPlot();
		
		plot =	plot.replace("'", (""));
		String length = book.getLength();
		
		length = length.replace("'", (""));
		Book book2 = new Book(title, author, ISBN, genre, book.getCover(), year, plot, length);
		return book2;
	}
	public static CD checkCD(CD cd){
		String album = cd.getTitle();
		
		album =	album.replace("'", (""));
		String artist = cd.getAuthor();
		
		artist = artist.replace("'", (""));
		String ISBN = cd.getISBN();
		
		ISBN = ISBN.replace("'", (""));
		String genre = cd.getGenre();
		
		genre =	genre.replace("'", (""));
		CD cd2 = new CD(album, artist, ISBN, genre, cd.getCover());
		return cd2;
	}
}
