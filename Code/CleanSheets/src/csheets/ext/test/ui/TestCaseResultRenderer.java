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
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import csheets.core.Value;
import csheets.ext.assertion.AssertableCell;
import csheets.ext.assertion.Assertion;
import csheets.ext.assertion.AssertionExtension;
import csheets.ext.test.TestCase;
import csheets.ext.test.TestableCell;

/**
 * A table cell renderer for test case results.
 * @author Einar Pehrson
 */
@SuppressWarnings("serial")
public class TestCaseResultRenderer extends DefaultTableCellRenderer {

	/**
	 * Creates a new test case result renderer.
	 */
	public TestCaseResultRenderer() {}

	public Component getTableCellRendererComponent(JTable table, Object o,
		boolean isSelected, boolean hasFocus, int row, int column) {

		// Defines variables
		Color color = Color.black;
		String tooltip = null;

		// Get result, and cell of test case at column 0 in this row
		Value value = (Value)o;
		TestableCell cell = ((TestCase)table.getValueAt(row, 0)).getCell();

		// Assert value against cells assertion, if possible
		AssertableCell assertableCell
			= (AssertableCell)cell.getExtension(AssertionExtension.NAME);
		if (assertableCell != null) {
			Assertion.Result result = assertableCell.assertAny(value);
			if (result == Assertion.Result.NAN || result == Assertion.Result.FAILED) {
				color = Color.RED;
				tooltip = "Entered test case param violates assertion";
			}
		}

		// Render component
		setForeground(color);
		setToolTipText(tooltip);
		return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	}
}