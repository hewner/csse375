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

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JColorChooser;

import csheets.core.Cell;
import csheets.ext.style.StylableCell;
import csheets.ext.style.StyleExtension;
import csheets.ui.ctrl.FocusOwnerAction;
import csheets.ui.ctrl.UIController;

/**
 * A background color changing operation.
 * @author Einar Pehrson
 */
@SuppressWarnings("serial")
public class BackgroundAction extends FocusOwnerAction {

	/** The user interface controller */
	protected UIController uiController;

	/**
	 * Creates a new background color action.
	 * @param uiController the user interface controller
	 */
	public BackgroundAction(UIController uiController) {
		this.uiController = uiController;
	}

	protected String getName() {
		return "Background Color...";
	}

	protected void defineProperties() {
		putValue(MNEMONIC_KEY, KeyEvent.VK_G);
		putValue(SMALL_ICON, new ImageIcon(StyleExtension.class.getResource("res/img/color_bg.gif")));
	}

	/**
	 * Lets the user select a color from a chooser.
	 * Then applies the color to the selected cells in the focus owner table.
	 * @param event the event that was fired
	 */
	public void actionPerformed(ActionEvent event) {
		if (focusOwner == null)
			return;

		// Lets user select color
		Color color = JColorChooser.showDialog(
			null,
			"Choose Background Color",
			((StylableCell)focusOwner.getSelectedCell().
				getExtension(StyleExtension.NAME)).getBackgroundColor());

		if (color != null) {
			// Colors each selected cell
			for (Cell[] row : focusOwner.getSelectedCells())
				for (Cell cell : row) {
					StylableCell stylableCell = (StylableCell)cell.getExtension(
						StyleExtension.NAME);
					stylableCell.setBackgroundColor(color);
				}
	
			uiController.setWorkbookModified(focusOwner.getSpreadsheet().getWorkbook());
			focusOwner.repaint();
		}
	}
}