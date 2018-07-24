import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;

public class TestMe3Test {

	@Test
	public void testGetTruth() {
		TestMe1Test.FakeCantConstructInTest fake = new TestMe1Test.FakeCantConstructInTest();
		fake.returnForIsTrue = true;
		TestMe3 object = TestMe3.constructForTEST(fake);
		Assert.assertTrue(object.getTruth());
	}

}
