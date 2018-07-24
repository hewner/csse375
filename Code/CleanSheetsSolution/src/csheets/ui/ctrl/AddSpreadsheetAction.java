/*
 * Copyright (c) 2005 Einar Pehrson <einar@pehrson.nu>.
 *
 * This file is part of
 * CleanSheets - a spreadsheet application for the Java platform.
 *
 * CleanSheets is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * CleanSheets is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with CleanSheets; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package csheets.ui.ctrl;

import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;

import csheets.CleanSheets;

/**
 * A spreadsheet insertion operation.
 * @author Einar Pehrson
 */
@SuppressWarnings("serial")
public class AddSpreadsheetAction extends FocusOwnerAction {

	/** The user interface controller */
	private UIController uiController;

	/**
	 * Creates a new spreadsheet insertion action.
	 * @param uiController the user interface controller
	 */
	public AddSpreadsheetAction(UIController uiController) {
		this.uiController = uiController;
	}

	protected String getName() {
		return "Add";
	}

	protected void defineProperties() {
		putValue(SMALL_ICON, new ImageIcon(CleanSheets.class.getResource("res/img/add_sheet.gif")));
	}

	/**
	 * Adds a new spreadsheet at the end of the active workbook.
	 * @param event the event that was fired
	 */
	public void actionPerformed(ActionEvent event) {
		if (focusOwner == null)
			return;
		focusOwner.getSpreadsheet().getWorkbook().addSpreadsheet();
		uiController.setWorkbookModified(focusOwner.getSpreadsheet().getWorkbook());
	}
}