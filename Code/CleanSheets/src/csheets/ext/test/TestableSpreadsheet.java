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

import java.util.Set;

import csheets.core.Spreadsheet;
import csheets.ext.SpreadsheetExtension;

/**
 * An extension of a spreadsheet, with support for testable cells.
 * @author Einar Pehrson
 */
public class TestableSpreadsheet extends SpreadsheetExtension {

	/** The unique version identifier used for serialization */
	private static final long serialVersionUID = -8144504629252776866L;

	/**
	 * Creates a testable spreadsheet for the given spreadsheet.
	 * @param spreadsheet the spreadsheet to extend
	 */
	protected TestableSpreadsheet(Spreadsheet spreadsheet) {
		super(spreadsheet, TestExtension.NAME);
	}

	/**
	 * Returns the testedness of the spreadsheet, i.e. the ratio of valid
	 * test cases to available test cases in all cells.
	 * @return a number between 0.0 and 1.0 denoting the level of testedness
	 */
	public double getTestedness() {
		// Sums test cases
		double nAvailable = 0;
		double nValid = 0;

		for (int row = 0; row < getRowCount(); row++)
			for (int column = 0; column < getColumnCount(); column++) {
				TestableCell cell = (TestableCell)getCell(column, row);
				if (cell.hasTestCases()) {
					Set<TestCase> testCases = cell.getTestCases();
					nAvailable += testCases.size();
					for (TestCase testCase : testCases)
						if (testCase.getValidationState() == TestCase.ValidationState.VALID)
							nValid++;
				}
			}

		// Calculates and returns the testedness
		if (nAvailable > 0)
			return nValid / nAvailable;
		else
			return 0;
	}
}