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
import java.awt.event.KeyEvent;
import java.text.DateFormat;
import java.text.Format;
import java.text.NumberFormat;

import javax.swing.ImageIcon;

import csheets.core.Cell;
import csheets.core.IllegalValueTypeException;
import csheets.core.Value;
import csheets.ext.style.StylableCell;
import csheets.ext.style.StyleExtension;
import csheets.ui.ctrl.FocusOwnerAction;
import csheets.ui.ctrl.SelectionEvent;
import csheets.ui.ctrl.SelectionListener;
import csheets.ui.ctrl.UIController;

/**
 * A format changing operation.
 * @author Einar Pehrson
 */
@SuppressWarnings("serial")
public class FormatAction extends FocusOwnerAction implements SelectionListener {

	/** The user interface controller */
	private UIController uiController;

	/** The cell being styled */
	private StylableCell cell;

	/**
	 * Creates a new format action.
	 * @param uiController the user interface controller
	 */
	public FormatAction(UIController uiController) {
		this.uiController = uiController;
		uiController.addSelectionListener(this);
	}

	protected String getName() {
		return "Format...";
	}

	protected void defineProperties() {
		putValue(MNEMONIC_KEY, KeyEvent.VK_D);
		putValue(SMALL_ICON, new ImageIcon(StyleExtension.class.getResource("res/img/format.gif")));
	}

	/**
	 * Updates the state of the action when a new cell is selected.
	 * @param event the selection event that was fired
	 */
	public void selectionChanged(SelectionEvent event) {
		Cell c = event.getCell();
		cell = c == null ? null : (StylableCell)c.getExtension(StyleExtension.NAME);
		setEnabled(c == null ? false : cell.isFormattable());
	}

	/**
	 * Lets the user select a format from a chooser.
	 * Then applies the format to the selected cells in the focus owner table.
	 * @param event the event that was fired
	 */
	public void actionPerformed(ActionEvent event) {
		if (focusOwner == null)
			return;
		// Lets user select a format
		Format format = null;
		try {
			if (cell.getValue().getType() == Value.Type.NUMERIC)
				format = new FormatChooser(
					(NumberFormat)cell.getFormat().clone(), cell.getValue().toNumber()
				).showDialog(null, "Choose Format");
			else if (cell.getValue().getType() == Value.Type.DATE)
				format = new FormatChooser(
					(DateFormat)cell.getFormat().clone(), cell.getValue().toDate()
				).showDialog(null, "Choose Format");
		} catch (IllegalValueTypeException e) {}

		if (format != null) {
			// Changes the format of each selected cell
			for (Cell[] row : focusOwner.getSelectedCells())
				for (Cell cell : row) {
					StylableCell stylableCell = (StylableCell)cell.getExtension(
						StyleExtension.NAME);
					stylableCell.setFormat(
						stylableCell.isFormattable() ? format : null);
				}
	
			uiController.setWorkbookModified(focusOwner.getSpreadsheet().getWorkbook());
			focusOwner.repaint();
		}
	}
}