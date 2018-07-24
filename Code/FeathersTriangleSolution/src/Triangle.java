import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import javax.swing.JOptionPane;

/**
 * This class represents an isosceles triangle.
 * 
 * Your goal is to be able to write test cases to ensure that the triangle is getting draw in the correct position by drawOn.
 * 
 * To test this, take a look at Feather's The Case of The Undetectable Side Effect.
 * 
 * You'll want to:
 * 1.  Use the Extract Method refactoring on all the parts that do stuff you can't handle in the test case
 * 2.  In your test case, Use Subclass and Override Method to override these methods to do something you can detect
 * 3.  Write some good test cases
 * 
 * I have additional hints at the bottom of the file if you get stuck
 */
public class Triangle {
	private int x;
	private int y;
	private double base;
	private double height;
	protected double angleRadians;
	private double splayAngleRadians;

	/**
	 * Constructs a new isosceles triangle from the given parameters. See Figure
	 * 1 on the printed test for additional explanation of the parameters.
	 * 
	 * @param x
	 *            the x-coordinate of the triangle's tip
	 * @param y
	 *            the y-coordinate of the triangle's tip
	 * @param base
	 *            the width of the triangle's base, in pixels
	 * @param height
	 *            the height of the triangle, in pixels
	 * @param angle
	 *            the angle formed between the x-axis and a line through this
	 *            triangle's tip that bisects its base, in degrees clockwise
	 */
	public Triangle(int x, int y, int base, int height) {
		this.x = x;
		this.y = y;
		this.base = base;
		this.height = height;
		double angle = openDialog("What angle do you want?");
		this.angleRadians = Math.toRadians(angle);
		this.splayAngleRadians = tipAngle(base, height);
	}

	/**
	 * Calculates the angle opposite the base for an isoceles triangle with the
	 * given base and height lengths.
	 * 
	 * @param base
	 * @param height
	 * @return half of the tip angle, in radians
	 */
	private static double tipAngle(int base, int height) {
		return Math.atan2(base / 2, height);
	}

	/**
	 * Scales this triangle by the given factor in each dimension, so a scaling
	 * factor of 2.0 quadruples the triangle's area.
	 * 
	 * @param factor
	 */
	public void scale(double factor) {
		this.height *= factor;
		this.base *= factor;
	}

	/**
	 * Rotates this triangle through the specified number of degrees.
	 * 
	 * @param degrees
	 *            positive rotates clockwise
	 */
	public void rotate(double degrees) {
		this.angleRadians += Math.toRadians(degrees);
	}

	/**
	 * @return the area of this triangle, in pixels^2
	 */
	public double area() {
		return this.base * this.height / 2.0;
	}

	/**
	 * Draws this triangle on the given graphics object.
	 * 
	 * @param graphics2
	 */
	public void drawOn(Graphics2D graphics2) {

		Point2D.Double center = new Point2D.Double(this.x, this.y);
		double halfBase = this.base / 2.0;
		double radius = Math.sqrt(this.height * this.height + halfBase
				* halfBase);
		double base1AngleRadians = this.angleRadians - this.splayAngleRadians;
		double base2AngleRadians = this.angleRadians + this.splayAngleRadians;

		Point2D.Double base1 = new Point2D.Double(this.x
				+ Math.cos(base1AngleRadians) * radius, this.y
				+ Math.sin(base1AngleRadians) * radius);
		Point2D.Double base2 = new Point2D.Double(this.x
				+ Math.cos(base2AngleRadians) * radius, this.y
				+ Math.sin(base2AngleRadians) * radius);

		drawLine(graphics2, center, base1);
		drawLine(graphics2, center, base2);
		drawLine(graphics2, base1, base2);
	}

	protected void drawLine(Graphics2D graphics2, Point2D.Double center,
			Point2D.Double base1) {
		Line2D.Double line;
		line = new Line2D.Double(center, base1);
		graphics2.draw(line);
	}
	
	protected double openDialog(String text) {
		
		String s = (String)JOptionPane.showInputDialog(
                null,
                text,
                "A question",
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                null);
		return Double.parseDouble(s);
		
	}
}


/*
ADDITIONAL HINTS

So for example, I would first extract all the calls that look like this

		Line2D.Double line;
		line = new Line2D.Double(center, base1);
		graphics2.draw(line);

Into a drawLine(double startx, double starty, double endx, double endy, Graphics2d pane)

Then in my test-specific subclass, I would re-define that method to store the xs and ys in a list or something, then do asserts based on that

*/