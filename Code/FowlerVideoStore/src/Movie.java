
/**
 * This is a simple data class (currently) to store a single movie
 * in the video store.  
 * 
 * @author wilkin, hewner, and of course Martin Fowler
 *
 */
public class Movie {
	
	public static enum Type { CHILDRENS, REGULAR, NEW_RELEASE};
	
	private String title;
	private Type priceCode;
	
	public Movie(String title) {
		this(title, Type.REGULAR);
	}
	
	public Movie(String title, Type priceCode) {
		this.title = title;
		this.priceCode = priceCode;
	}
	
	public Type getPriceCode() {
		return this.priceCode;
	}
	
	public void setPriceCode(Type incomingPriceCode) {
		this.priceCode = incomingPriceCode;
	}
	
	public String getTitle() {
		return this.title;
	}

}
