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
package csheets.ext.style.ui;

import java.awt.event.ActionEvent;

import csheets.core.Cell;
import csheets.ext.style.StylableCell;
import csheets.ext.style.StyleExtension;
import csheets.ui.ctrl.FocusOwnerAction;
import csheets.ui.ctrl.UIController;

/**
 * A style operation.
 * @author Einar Pehrson
 */
public abstract class StyleAction extends FocusOwnerAction {

	/** The user interface controller */
	protected UIController uiController;

	/**
	 * Creates a new style action.
	 * @param uiController the user interface controller
	 */
	public StyleAction(UIController uiController) {
		this.uiController = uiController;
	}

	/**
	 * Applies the style to the selected cells in the focus owner table.
	 * @param event the event that was fired
	 */
	public void actionPerformed(ActionEvent event) {
		if (focusOwner == null)
			return;

		// Aligns each selected cell
		for (Cell[] row : focusOwner.getSelectedCells())
			for (Cell cell : row) {
				StylableCell stylableCell = (StylableCell)cell.getExtension(
					StyleExtension.NAME);
				applyStyle(stylableCell);
			}

		uiController.setWorkbookModified(focusOwner.getSpreadsheet().getWorkbook());
		focusOwner.repaint();
	}

	/**
	 * Applies the style to the given stylable cell
	 * @param cell the cell to which style should be applied
	 */
	protected abstract void applyStyle(StylableCell cell);
}