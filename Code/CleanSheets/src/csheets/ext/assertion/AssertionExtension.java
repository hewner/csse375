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
package csheets.ext.assertion;

import csheets.core.Cell;
import csheets.ext.Extension;
import csheets.ext.assertion.ui.AssertionUIExtension;
import csheets.ui.ctrl.UIController;
import csheets.ui.ext.UIExtension;

/**
 * The extension for assertions.
 * @author Einar Pehrson
 */
public class AssertionExtension extends Extension {

	/** The name of the extension */
	public static final String NAME = "Assertions";

	/**
	 * Creates a new assertion extension.
	 */
	public AssertionExtension() {
		super(NAME);
	}

	/**
	 * Makes the given cell assertable.
	 * @param cell the cell to extend
	 * @return an assertable cell
	 */
	public AssertableCell extend(Cell cell) {
		return new AssertableCell(cell);
	}

	/**
	 * Returns a user interface extension for assertions.
	 * @param uiController the user interface controller
	 * @return a user interface extension for assertions
	 */
	public UIExtension getUIExtension(UIController uiController) {
		return new AssertionUIExtension(this, uiController);
	}
}