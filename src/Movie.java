package media;



public class Movie extends Media {

    private String year;
    private String plot;
    private String cast;
    private String length;
    private String language;
    private String country;
    
    public Movie(String title, String author, String isbn, String genre, byte[] cover, String year, String plot, String cast, String length, String language, String country){
    	super(title, author, isbn, genre, cover);
    	this.year = year;
    	this.plot = plot;
    	this.cast = cast;
    	this.length = length;
    	this.language = language;
    	this.cover = cover;
    }
    
    public Movie(int id, String title, String author, String isbn, String genre, byte[] cover, String year, String plot, String cast, String length, String language, String country){
    	super(id, title, author, isbn, genre, cover);
    	this.year = year;
    	this.plot = plot;
    	this.cast = cast;
    	this.length = length;
    	this.language = language;
    	this.cover = cover;
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

    public String getCast() {
        return cast;
    }

    public void setCast(String cast) {
        this.cast = cast;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
    public String[] toArray(){
    	return new String[]{isbn, title, author, genre, year, length, language, country, cast, plot};
    }
    
    public String toString(){
    	String movieString = "ISBN: " + this.isbn + "\n"
    			+ "Title: " + this.title + "\n"
    			+ "Director: " + this.author + "\n"
    			+ "Genre: " + this.genre + "\n"
    			+ "Year: " + this.year + "\n"
    			+ "Length: " + this.length + "\n"
    			+ "Language: " + this.language + "\n"
    			+ "Country: " + this.country + "\n"
    			+ "Cast: " + this.cast + "\n"
    			+ "Plot: " + this.plot;
    	return movieString;
    }
}

