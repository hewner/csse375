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
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import csheets.CleanSheets;
import csheets.core.Workbook;
import csheets.ui.FileChooser;

/**
 * An action for closing and unloading the active workbook.
 * @author Einar Pehrson
 */
@SuppressWarnings("serial")
public class CloseAction extends SaveAction {

	/**
	 * Creates a new close action.
	 * @param app the CleanSheets application
	 * @param uiController the user interface controller
	 * @param chooser the file chooser to use when prompting the user for the file to save
	 */
	public CloseAction(CleanSheets app, UIController uiController, FileChooser chooser) {
		super(app, uiController, chooser);
	}

	protected String getName() {
		return "Close";
	}

	protected void defineProperties() {
		putValue(MNEMONIC_KEY, KeyEvent.VK_C);
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_W, ActionEvent.CTRL_MASK));
		putValue(SMALL_ICON, new ImageIcon(CleanSheets.class.getResource("res/img/close.gif")));
	}

	public void actionPerformed(ActionEvent e) {
		Workbook workbook = uiController.getActiveWorkbook();
		if (workbook != null) {
			if (uiController.isWorkbookModified(workbook)) {
				// Prompt to discard changes
				int option = JOptionPane.showConfirmDialog(
					null,
					"The current workbook has been modified.\n" +
					"Do you want to save the file before closing it?",
					"Save changes before closing?",
					JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.WARNING_MESSAGE
				);
		
				if (option == JOptionPane.YES_OPTION)
					// Saves file
					super.actionPerformed(e);
		
				if (!(option == JOptionPane.YES_OPTION
				   || option == JOptionPane.NO_OPTION))
					return;
			}
			app.unload(workbook);
		}
	}

	protected boolean requiresModification() {
		return false;
	}
}