package media;



public class CD extends Media{

    public CD(String title, String author, String isbn, String genre, byte[] cover){
    	super(title, author, isbn, genre, cover);
    }
    
    public CD(int id, String title, String author, String isbn, String genre, byte[] cover){
    	super(id, title, author, isbn, genre, cover);
    }
    
    public String[] toArray(){
    	return new String[]{isbn, author, title, genre};
    }
}

