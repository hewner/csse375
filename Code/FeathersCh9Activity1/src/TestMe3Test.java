import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;


public class TestMe3Test {

	@Test
	public void testGetTruth() {
		TestMe3 object = new TestMe3();
		Assert.assertTrue(object.getTruth());
	}

}
