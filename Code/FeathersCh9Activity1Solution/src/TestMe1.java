/**
 * This is a class we want to test. Unfortunately, it's heavily involved with
 * the class CantConstructInTest - which we can't construct in test.
 * 
 * Although this is not a realistic example, there are plenty of real reasons
 * why a class might not be constructable in a unit test. Maybe it creates a gui
 * window, or talks to a DB, or depends on some other horrible class.
 * 
 * Take a look at TestMe1Test to see what the tests we would LIKE to write would
 * look like, if not due to this problematic class.
 * 
 * @author hewner
 * 
 */

public class TestMe1 {

	/*
	 * NOTE THAT even though this looks the same We've actually replaced
	 * CantConstructInTest with an interface.
	 * 
	 * The class that we used to called CantConstuctInTest is now called
	 * called CantConstructInTestImplementation
	 */
	private CantConstructInTest variable;

	public TestMe1(CantConstructInTest param) {
		this.variable = param;
	}

	/**
	 * Testing this function is maybe not so hard. I doesn't actually use our
	 * problematic instance variable. How can we get it under test? Hint: take a
	 * look a Feathers page 111 under the heading "Pass Null"
	 */
	public int function1() {
		return 1;
	}

	/**
	 * This function uses that instance variable - now we need to get fancy. Use
	 * Feather's Extract Interface approach (pg. 362).
	 */
	public int function2() {
		if (this.variable.isTrue()) {
			return 1;
		} else {
			return 2;
		}
	}
}
