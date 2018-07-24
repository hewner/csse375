/*
 * Copyright (c) 2005 Peter Palotas, Fredrik Johansson, Einar Pehrson,
 * Sebastian Kekkonen, Lars Magnus Lång, Malin Johansson and Sofia Nilsson
 *
 * This file is part of
 * CleanSheets Extension for Assertions
 *
 * CleanSheets Extension for Assertions is free software; you can
 * redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either version 2 of
 * the License, or (at your option) any later version.
 *
 * CleanSheets Extension for Assertions is distributed in the hope that
 * it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with CleanSheets Extension for Assertions; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA  02111-1307  USA
 */
package csheets.ext.assertion.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;

import csheets.core.Cell;
import csheets.ext.assertion.AssertableCell;
import csheets.ext.assertion.AssertionExtension;
import csheets.ui.ext.CellDecorator;

/**
 * A decorator for assertable cells.
 * @author Einar Pehrson
 */
public class AssertableCellDecorator extends CellDecorator {

	/** The font used to render the 'A' */
	private static final Font font = new Font("Dialog", Font.PLAIN, 10);

	/**
	 * Creates a new cell decorator.
	 */
	public AssertableCellDecorator() {}

	/**
	 * Decorates the given graphics context if the cell being rendered
	 * has an assertion error.
	 * @param component the cell renderer component
	 * @param g the graphics context on which drawing should be done
	 * @param cell the cell being rendered
	 * @param selected whether the cell is selected
	 * @param hasFocus whether the cell has focus
	 */
	public void decorate(JComponent component, Graphics g, Cell cell,
			boolean selected, boolean hasFocus) {
		if (enabled) {
			AssertableCell assertableCell = (AssertableCell)cell.getExtension(AssertionExtension.NAME);
			if (assertableCell.hasAssertionError()) {
				// Stores current graphics context properties
				Graphics2D g2 = (Graphics2D)g;
				Color oldPaint = g2.getColor();
				Font oldFont = g2.getFont();

				// Prints 'A' using own font, then restores the old font
				g2.setColor(Color.red);
				g2.setFont(font);
				g2.drawString("A", 4, 12);

				// Restores graphics context properties
				g2.setColor(oldPaint);
				g2.setFont(oldFont);
			}
		}
	}
}