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

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;

import csheets.core.Cell;
import csheets.ext.style.StylableCell;
import csheets.ext.style.StyleExtension;
import csheets.ui.ctrl.FocusOwnerAction;
import csheets.ui.ctrl.UIController;

/**
 * A font changing operation.
 * @author Einar Pehrson
 */
@SuppressWarnings("serial")
public class FontAction extends FocusOwnerAction {

	/** The user interface controller */
	protected UIController uiController;

	/**
	 * Creates a new font action.
	 * @param uiController the user interface controller
	 */
	public FontAction(UIController uiController) {
		this.uiController = uiController;
	}

	protected String getName() {
		return "Font...";
	}

	protected void defineProperties() {
		putValue(MNEMONIC_KEY, KeyEvent.VK_F);
		putValue(SMALL_ICON, new ImageIcon(StyleExtension.class.getResource("res/img/font.gif")));
	}

	/**
	 * Lets the user select a font from a chooser.
	 * Then applies the font to the selected cells in the focus owner table.
	 * @param event the event that was fired
	 */
	public void actionPerformed(ActionEvent event) {
		if (focusOwner == null)
			return;

		// Lets user select a font
		Font font = FontChooser.showDialog(
			null,
			"Choose Font",
			((StylableCell)focusOwner.getSelectedCell().
				getExtension(StyleExtension.NAME)).getFont());

		if (font != null) {
			// Changes the font of each selected cell
			for (Cell[] row : focusOwner.getSelectedCells())
				for (Cell cell : row) {
					StylableCell stylableCell = (StylableCell)cell.getExtension(
						StyleExtension.NAME);
					stylableCell.setFont(font);
				}
	
			uiController.setWorkbookModified(focusOwner.getSpreadsheet().getWorkbook());
			focusOwner.repaint();
		}
	}
}