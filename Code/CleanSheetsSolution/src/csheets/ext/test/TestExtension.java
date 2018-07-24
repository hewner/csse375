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
package csheets.ext.test;

import csheets.core.Cell;
import csheets.core.Spreadsheet;
import csheets.ext.Extension;
import csheets.ext.test.ui.TestUIExtension;
import csheets.ui.ctrl.UIController;
import csheets.ui.ext.UIExtension;

/**
 * The extension for tests.
 * @author Einar Pehrson
 */
public class TestExtension extends Extension {

	/** The name of the extension */
	public static final String NAME = "Test Cases";

	/**
	 * Creates a new test extension.
	 */
	public TestExtension() {
		super(NAME);
	}

	/**
	 * Makes the given spreadsheet testable.
	 * @param spreadsheet the spreadsheet to extend
	 * @return a testable spreadsheet
	 */
	public TestableSpreadsheet extend(Spreadsheet spreadsheet) {
		return new TestableSpreadsheet(spreadsheet);
	}

	/**
	 * Makes the given cell testable.
	 * @param cell the cell to extend
	 * @return a testable cell
	 */
	public TestableCell extend(Cell cell) {
		return new TestableCell(cell);
	}

	/**
	 * Returns a user interface extension for testing.
	 * @param uiController the user interface controller
	 * @return a user interface extension for testing
	 */
	public UIExtension getUIExtension(UIController uiController) {
		return new TestUIExtension(this, uiController);
	}
}