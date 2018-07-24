/**
 * This class represents a customer renting a movie.
 * 
 * @author wilkin, hewner, and of course Martin Fowler
 *
 */
public class Rental {
	
	private Movie movie;
	private int daysRented;
	
	public Rental(Movie movie, int daysRented) {
		this.movie = movie;
		this.daysRented = daysRented;
	}
	
	public int getDaysRented() {
		return this.daysRented;
	}
	
	public Movie getMovie() {
		return this.movie;
	}

}
