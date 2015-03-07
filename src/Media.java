package media;



public class Media {

    protected String title;
    protected String author;
    protected String  isbn;
    protected String genre;
    protected String coverFilepath;

    public Media(String title, String author, String isbn, String genre, String coverFilepath){
    	this.title = title;
    	this.author = author;
    	this.isbn = isbn;
    	this.genre = genre;
    	this.coverFilepath = coverFilepath;
    }
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getISBN() {
        return isbn;
    }

    public void setISBN(String isbn) {
        this.isbn = isbn;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
    
    public String getCoverFilepath() {
        return coverFilepath;
    }

    public void setCoverFilepath(String coverFilepath) {
        this.coverFilepath = coverFilepath;
    }
}
