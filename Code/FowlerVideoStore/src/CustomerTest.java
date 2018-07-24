import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;


public class CustomerTest {

	private Customer cust;
	
	@Before
	public void setUp() throws Exception {
		cust = new Customer("Buffalo");
	}

	@Test
	public void testStatementRegular() {
		Movie m1 = new Movie("Star Wars");
		Rental r1 = new Rental(m1, 1);
		cust.addRental(r1);
		assertEquals("Rental Record for Buffalo\n\tStar Wars\t2.00\nAmount owed is 2.00\nYou earned 1 frequent renter points", cust.statement());
		Rental r2 = new Rental(new Movie("Citizen Kane"),3);
		cust.addRental(r2);
		assertEquals("Rental Record for Buffalo\n\tStar Wars\t2.00\n\tCitizen Kane\t3.50\nAmount owed is 5.50\nYou earned 2 frequent renter points", cust.statement());
	}

	@Test
	public void testStatementNewRelease() {
		Movie m1 = new Movie("Star Wars Remastered Again", Movie.Type.NEW_RELEASE);
		Rental r1 = new Rental(m1, 2);
		cust.addRental(r1);
		assertEquals("Rental Record for Buffalo\n\tStar Wars Remastered Again\t6.00\nAmount owed is 6.00\nYou earned 2 frequent renter points", cust.statement());
	}

	@Test
	public void testStatementChildrens() {
		Movie m1 = new Movie("Lego Movie", Movie.Type.CHILDRENS);
		Rental r1 = new Rental(m1, 4);
		cust.addRental(r1);
		assertEquals("Rental Record for Buffalo\n\tLego Movie\t3.00\nAmount owed is 3.00\nYou earned 1 frequent renter points", cust.statement());
	}

	
}
