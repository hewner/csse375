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

import javax.swing.JOptionPane;

import csheets.ext.assertion.AssertableCell;
import csheets.ext.assertion.Assertion;
import csheets.ext.assertion.AssertionException;
import csheets.ext.assertion.AssertionWarning;
import csheets.ext.assertion.USAssertion;
import csheets.ui.ctrl.UIController;

/**
 * A controller for updating the user-specified assertion of a cell.
 * @author Einar Pehrson
 */
public class AssertionController {

	/** The user interface controller */
	private UIController uiController;

	/**
	 * Creates a new assertion controller.
	 * @param uiController the user interface controller
	 */
	public AssertionController(UIController uiController) {
		this.uiController = uiController;
	}

	/**
	 * Attempts to create a new assertion from the given string.
	 * If successful, adds the assertion to the given cell.
	 * Otherwise, displays an error message.
	 * If the input string is empty or null, the assertion is set to null.
	 * @param cell the cell for which the assertion should be set
	 * @param assertionString the assertion, as entered by the user
	 * @return true if the cell's assertion was changed
	 */
	public boolean setAssertion(AssertableCell cell, String assertionString) {
		// Clears assertion, if insufficient input
		if (assertionString == null || assertionString.equals("")) {
			cell.setUSAssertion(null);
			return true;
		}

		// Attempts to create the assertion
		USAssertion assertion;
		try {
			assertion = new USAssertion(assertionString);
		} catch (AssertionException e) {
			// Informs user that the assertion syntax was erroneous
			showError(e);
			return false;
		}

		// Stores the assertion
		cell.setUSAssertion(assertion);
		uiController.setWorkbookModified(cell.getSpreadsheet().getWorkbook());

		if (!assertion.isConsistent()) {
			// Informs user that the assertion has warnings
			String message = "The assertion may be incorrect or inconsistent:\n";
			for (AssertionWarning warning : assertion.getWarnings())
				message += "\n" + warning;
			showError(message);
		}

		return true;
	}

	/**
	 * Assigns a color to the given assertion result.
	 * @param result the assertion result for which a color is wanted
	 * @return the appropriate color to use in the interface
	 */	
	public static Color getAssertionResultColor(Assertion.Result result){
		switch (result) {
			case OK:
				return new Color(0f, 0.7f, 0f);
			case NAN:
				return new Color(0.7f, 0.0f, 0f);
			case FAILED:
				return new Color(0.7f, 0.0f, 0f);
			case NO_DATA:
				return new Color (0.7f, 0.7f, 0f);
			default:
				return Color.BLACK;
		}
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