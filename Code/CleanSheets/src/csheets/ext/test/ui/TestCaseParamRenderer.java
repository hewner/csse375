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
import csheets.ext.test.TestCaseParam;
import csheets.ext.test.TestableCell;

/**
 * A table cell renderer for test case parameters.
 * @author Malin Johansson
 * @author Sofia Nilsson
 * @author Einar Pehrson
 * @author Björn Lanneskog
 */
@SuppressWarnings("serial")
public class TestCaseParamRenderer extends DefaultTableCellRenderer {

	/**
	 * Creates a new test case parameter renderer.
	 */
	public TestCaseParamRenderer() {}

	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		
		if (value instanceof TestCaseParam) {
			// Get param, and cell at column 0 in this row
			TestCaseParam param = (TestCaseParam)value;
			TestableCell cell = (TestableCell)table.getValueAt(row, 0);
	
			if (param != null && param.getValue().getType() == Value.Type.NUMERIC) {
				AssertableCell assertableCell
					= (AssertableCell)cell.getExtension(AssertionExtension.NAME);
				if (assertableCell != null) {
					// Assert value against cells assertion
					Assertion.Result result = assertableCell.assertAny(param.getValue());
					// check if user entered
					if(param.isUserEntered()) {
						// set red if testcaseparam breaks against assertion
						if (result == Assertion.Result.NAN || result == Assertion.Result.FAILED) {
							setForeground(Color.RED);
							setToolTipText("Entered test case param violates assertion");
						}
						// set black if testcaseparam is within assertion
						else {
							setForeground(Color.BLACK);
							setToolTipText(null);
						}
					}
					// check if derived or system generated
					else if(param.isDerived() || param.isSystemGenerated()) {
						// set light red if testcaseparam breaks against assertion
						if (result == Assertion.Result.NAN || result == Assertion.Result.FAILED) {
							setForeground(new java.awt.Color(246, 126, 126));
							setToolTipText("Derived test case param violates assertion");
						}
						// set light gray if testcaseparam is within assertion
						else {
							setForeground(java.awt.Color.LIGHT_GRAY);
							setToolTipText(null);
						}
					}
				}
			}
		}
			
		// Render component
		return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	}
}