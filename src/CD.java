package media;



public class CD extends Media{

    private String cover;

    public CD(String title, String author, String isbn, String genre, String coverFilepath){
    	super(title, author, isbn, genre, coverFilepath);
    }
    

    
    public String[] toArray(){
    	return new String[]{coverFilepath, isbn, author, title, genre};
    }
}

