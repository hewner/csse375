import static org.junit.Assert.*;
import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;


public class TestMe2Test {

	class FakeBadGlobal implements BadGlobaInterface {

		public void doThing(String s) {
			// do nothing, unless we need something special for our tests	
		}
	}
	
	TestMe2 object;
	
	@Before
	public void setUp()
	{
		BadGlobal.TEST_ONLY_setInstance(new FakeBadGlobal());
		object = new TestMe2();
	}
	
	@Test
	public void testTestA() {
		Assert.assertEquals(1, object.testA());
	}

	@Test
	public void testTestB() {
		Assert.assertEquals(2, object.testB());
	}

	@Test
	public void testTestC() {
		Assert.assertEquals(3, object.testC());
	}

}
