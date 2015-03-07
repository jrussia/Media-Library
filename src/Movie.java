package media;



public class Movie extends Media {

    private String year;
    private String plot;
    private String cast;
    private String length;
    private String language;
    private String country;
    
    public Movie(String title, String author, String isbn, String genre, String coverFilepath, String year, String plot, String cast, String length, String cover, String language, String country){
    	super(title, author, isbn, genre, coverFilepath);
    	this.year = year;
    	this.plot = plot;
    	this.cast = cast;
    	this.length = length;
    	this.language = language;
    	this.coverFilepath = coverFilepath;
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
    	return new String[]{coverFilepath, isbn, title, author, genre, year, length, language, country, cast, plot};
    }
}

