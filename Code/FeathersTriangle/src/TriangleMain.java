import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;
import javax.swing.JFrame;


/**
 * You should not need to modify this class
 * 
 * @author hewner
 *
 */
public class TriangleMain extends JComponent{

	Triangle triangle;
	
	public TriangleMain() {
		triangle = new Triangle(0, 0, 50, 100);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		triangle.drawOn((Graphics2D) g);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JFrame frame = new JFrame("My cool traingle");
		TriangleMain component = new TriangleMain();
		frame.setSize(300, 300);
		frame.add(component);
		frame.setVisible(true);
	}

}
