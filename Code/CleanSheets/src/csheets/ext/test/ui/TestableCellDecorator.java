/*
 * Copyright (c) 2005 Jens Schou, Staffan Gustafsson, Björn Lanneskog, 
 * Einar Pehrson and Sebastian Kekkonen
 *
 * This file is part of
 * CleanSheets Extension for Test Cases
 *
 * CleanSheets Extension for Test Cases is free software; you can
 * redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either version 2 of
 * the License, or (at your option) any later version.
 *
 * CleanSheets Extension for Test Cases is distributed in the hope that
 * it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with CleanSheets Extension for Test Cases; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA  02111-1307  USA
 */
package csheets.ext.test.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;

import csheets.core.Cell;
import csheets.ext.test.TestExtension;
import csheets.ext.test.TestableCell;
import csheets.ui.ext.CellDecorator;

/**
 * A decorator for testable cells.
 * @author Einar Pehrson
 */
public class TestableCellDecorator extends CellDecorator {

	/** The font used to render the 'A' */
	private static final Font font = new Font("Dialog", Font.PLAIN, 10);

	/** The color used to indicate a warning */
	private static final Color warningColor = new Color(0.7f, 0.7f, 0f);

	/**
	 * Creates a new cell decorator.
	 */
	public TestableCellDecorator() {}

	/**
	 * Decorates the given graphics context if the cell being rendered
	 * has a test error.
	 * @param component the cell renderer component
	 * @param g the graphics context on which drawing should be done
	 * @param cell the cell being rendered
	 * @param selected whether the cell is selected
	 * @param hasFocus whether the cell has focus
	 */
	public void decorate(JComponent component, Graphics g, Cell cell,
			boolean selected, boolean hasFocus) {
		if (enabled) {
			// Checks for error
			TestableCell testableCell = (TestableCell)cell.getExtension(TestExtension.NAME);
			boolean hasError = testableCell.hasTestError();
			boolean hasWarning = testableCell.hasTestCases()
				&& testableCell.getTestedness() < 1.0;
			if (hasError || hasWarning) {
				// Stores current graphics context properties
				Graphics2D g2 = (Graphics2D)g;
				Color oldPaint = g2.getColor();
				Font oldFont = g2.getFont();

				// Selects color
				if (hasError)
					g2.setColor(Color.red);
				else if (hasWarning)
					g2.setColor(warningColor);
	
				// Prints 'T' using own font, then restores the old font
				g2.setFont(font);
				g2.drawString("T", 12, 12);
	
				// Restores graphics context properties
				g2.setColor(oldPaint);
				g2.setFont(oldFont);
			}
		}
	}
}