package media;

public class EANMovieNode extends XMLNode {

	public EANMovieNode(String xml) throws Exception {
		e = getElementFromXml(xml, "product");
	}

	public String getTitle() {
		return e.getElementsByTagName("product").item(0).getTextContent();
	}
	
}
