import static org.junit.Assert.*;
import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;


public class TestMe2Test {

	TestMe2 object;
	
	@Before
	public void setUp()
	{
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
