import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;


public class TestMe1Test {
		
	@Test
	public void testFunction1() {
		CantConstructInTest param = new CantConstructInTest(true);
		TestMe1 object = new TestMe1(param);
		Assert.assertEquals(1, object.function1());
	}

	@Test
	public void testFunction2() {
		CantConstructInTest param = new CantConstructInTest(true);
		TestMe1 object = new TestMe1(param);
		Assert.assertEquals(1, object.function2());
		param = new CantConstructInTest(false);
		object = new TestMe1(param);
		Assert.assertEquals(2, object.function2());
		
	}

}
