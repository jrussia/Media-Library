package media;

/*
 * CSC 478
 * Team2
 * Book.java
 * Purpose: To create a Book object, inherited from Media
 * 
 * @author Karissa (Nash) Stisser, Jeremy Egner, Yuji Tsuzuki
 * @version 1.2.0 3/19/15
 */

public class Book extends Media {

    private String year;
    private String plot;
    private String length;

    public Book(int id, String title, String author, String isbn, String genre, byte[] cover, String year, String plot, String length){
    	super(id, title, author, isbn, genre, cover);
    	this.year = year;
    	this.plot = plot;
    	this.length = length;
    }
    
    public Book(String title, String author, String isbn, String genre, byte[] cover, String year, String plot, String length){
    	super(title, author, isbn, genre, cover);
    	this.year = year;
    	this.plot = plot;
    	this.length = length;
    }
    
    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String[] toArray(){
    	return new String[]{isbn, title, author, genre, year, length, plot};
    }
    
    public String toString(){
    	String bookString = "ISBN: " + this.isbn + "\n"
    			+ "Title: " + this.title + "\n"
    			+ "Author: " + this.author + "\n"
    			+ "Genre: " + this.genre + "\n"
    			+ "Year: " + this.year + "\n"
    			+ "Length: " + this.length + "\n"
    			+ "Plot: " + this.plot;
    	return bookString;
    }
}

