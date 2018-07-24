/**
 * One option to test this would be to make TestMe4's functions take parmeters
 * of BadGlobal.
 * 
 * If we did that, we could use Extract Interface easily.
 * 
 * But, given that there's a lot of usage, maybe we need another way.
 * 
 * Let's use Extract Interace PLUS IntroduceStaticSetter 
 */
public class TestMe2 {

	public int testA() {
		BadGlobal.getInstance().doThing("A");
		return 1;
	}

	public int testB() {
		BadGlobal.getInstance().doThing("B");
		return 2;
	}

	public int testC() {
		BadGlobal.getInstance().doThing("C");
		return 3;
	}
}
