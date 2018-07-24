
/*
 * This one is a variation of TestMe1, but toss in ParamterizeConstructor
 */
public class TestMe3 {
	
	CantConstructInTest myField;

	public TestMe3() {
		this(new CantConstructInTestImplmentation(true));
	}
	
	private TestMe3(CantConstructInTest parameter) {
		myField = parameter;
	}
	
	public boolean getTruth() {
		return myField.isTrue();
	}
	
	public static TestMe3 constructForTEST(CantConstructInTest parameter) {
		return new TestMe3(parameter);
	}
}
