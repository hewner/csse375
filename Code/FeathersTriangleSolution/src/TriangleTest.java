import static org.junit.Assert.*;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;

import junit.framework.Assert;

import org.junit.Test;


public class TriangleTest {

	private class TestingOnlyTriangle extends Triangle {

		public ArrayList<Point2D.Double> lines;

		public TestingOnlyTriangle(int x, int y, int base, int height, double angle) {
			super(x, y, base, height);
			this.angleRadians = Math.toRadians(angle);
			this.lines = new ArrayList<Point2D.Double>();
		}

		@Override
		protected void drawLine(Graphics2D graphics2, Double center,
				Double base1) {
			lines.add(center);
			lines.add(base1);
		}

		@Override
		protected double openDialog(String text) {
			return 0;
		}

		
	}
	
	@Test
	public void testDrawOn() {
		TestingOnlyTriangle triangle = new TestingOnlyTriangle(0, 0, 50, 100, 0);
		triangle.drawOn(null);
		
		ArrayList<Point2D.Double> lines = new ArrayList<Point2D.Double>();
		lines.add(new Point2D.Double(0, 0));
		lines.add(new Point2D.Double(100, -25));
		lines.add(new Point2D.Double(0, 0));
		lines.add(new Point2D.Double(100, 25));
		lines.add(new Point2D.Double(100, -25));
		lines.add(new Point2D.Double(100, 25));
		
		Assert.assertEquals(lines, triangle.lines);
	}

}
