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

import java.util.HashSet;
import java.util.Set;

import javax.swing.JOptionPane;

import csheets.core.Value;
import csheets.ext.assertion.AssertableCell;
import csheets.ext.assertion.Assertion;
import csheets.ext.assertion.AssertionExtension;
import csheets.ext.test.DuplicateUserTCPException;
import csheets.ext.test.TestCaseParam;
import csheets.ext.test.TestableCell;
import csheets.ui.ctrl.UIController;

/**
 * A controller for updating the test case parameters of a cell.
 * @author Einar Pehrson
 */
public class TestCaseParamController {

	/** The user interface controller */
	private UIController uiController;

	/**
	 * Creates a new test case parameter controller.
	 * @param uiController the user interface controller
	 */
	public TestCaseParamController(UIController uiController) {
		this.uiController = uiController;
	}

	/**
	 * Sets the active cell's user-specified testcase parameters.
	 * @param cell the cell to add the parameter to
	 * @param content the content of the parameter to be added
	 * @param unChangedParams a set containing all parameters that were not changed
	 * @return true the parameter that was added, or null if the parameter was not added
	 */
	public TestCaseParam setTestCaseParams(TestableCell cell, String content,
				Set<TestCaseParam> unChangedParams) {
		// Checks if any parameters were removed
		Set<TestCaseParam> oldParams = new HashSet<TestCaseParam>(cell.getTestCaseParams());
		oldParams.removeAll(unChangedParams);

		if (!oldParams.isEmpty())
			// Parameters were removed
			for (TestCaseParam removedParam : oldParams)
				cell.removeTestCaseParam(removedParam);

		// Checks if a parameter was added
		if(content != null) {
			Value value = Value.parseValue(content);

			// Asserts if possible
			if (value.getType() == Value.Type.NUMERIC) {
				AssertableCell assertableCell
					= (AssertableCell)cell.getExtension(AssertionExtension.NAME);
				if (assertableCell != null) {
					if (assertableCell.isAsserted())
						if (assertableCell.assertAny(value) == Assertion.Result.FAILED)
							// Shows error, should perhaps ask for corfirmation?
							showError("The test case parameter that was entered for cell " + 
								cell + " violated the cell's assertion.");
				}
			}

			// Attempts to add the parameter
			try {
				TestCaseParam param = cell.addTestCaseParam(value);
				uiController.setWorkbookModified(cell.getSpreadsheet().getWorkbook());
				return param;
			} catch (DuplicateUserTCPException e) {
				// Informs user that the parameter could not be added
				showError(e.getMessage());
				return null;
			}
		} else
			return null;
	}

	/**
	 * Shows an error dialog displaying the given message.
	 */
	private void showError(Object message) {
		JOptionPane.showMessageDialog(
			null,
			message,
			"Error",
			JOptionPane.ERROR_MESSAGE
		);
	}
}