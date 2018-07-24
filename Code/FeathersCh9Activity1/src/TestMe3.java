/**
 * This one is a variation of TestMe1, but toss in ParamterizeConstructor
 * 
 * In my solution, I figured out a way to set things up so that my bogus
 * constructor can't be called accidentally (hint: it involved a static method).
 * But a wilful idiot still could do the wrong thing
 */
public class TestMe3 {

	CantConstructInTest myField;

	public TestMe3() {
		myField = new CantConstructInTest(true);
	}

	public boolean getTruth() {
		return myField.isTrue();
	}
}
