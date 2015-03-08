package media;



public class CD extends Media{

    public CD(String title, String author, String isbn, String genre, byte[] cover){
    	super(title, author, isbn, genre, cover);
    }
    
    
    public String[] toArray(){
    	return new String[]{isbn, author, title, genre};
    }
}

