import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;


public class TestMe1Test {
	
	/**
	 * I make a fake class for my testing only.
	 * 
	 * NOTE this works because TestMe1 is the class I'd like to test.
	 * I don't care about testing CantConstructInTest
	 * 
	 * @author hewner
	 *
	 */
	public static class FakeCantConstructInTest implements CantConstructInTest {

		// UGLY!  But this just for test purposes.
		public boolean returnForIsTrue;
		
		@Override
		public boolean isTrue() {
			return this.returnForIsTrue;
		}
		
	}
	
	@Test
	public void testFunction1() {
		TestMe1 object = new TestMe1(null);
		Assert.assertEquals(1, object.function1());
	}

	@Test
	public void testFunction2() {
		FakeCantConstructInTest param = new FakeCantConstructInTest();
		param.returnForIsTrue = true;
		TestMe1 object = new TestMe1(param);
		Assert.assertEquals(1, object.function2());
		param.returnForIsTrue = false;
		object = new TestMe1(param);
		Assert.assertEquals(2, object.function2());
		
	}

}
