package media;



public class Book extends Media {

    private String year;
    private String plot;
    private String length;

    public Book(String title, String author, String isbn, String genre, String coverFilepath, String year, String plot, String cast, String length, String cover, String language, String country){
    	super(title, author, isbn, genre, coverFilepath);
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
    	return new String[]{coverFilepath, isbn, title, author, genre, year, length, plot};
    }
}

