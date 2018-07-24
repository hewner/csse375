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
import java.io.File;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import csheets.CleanSheets;
import csheets.core.Workbook;
import csheets.ui.FileChooser;

/**
 * An action for saving a spreadsheet to a user-supplied file.
 * @author Einar Pehrson
 */
@SuppressWarnings("serial")
public class SaveAsAction extends BaseAction {

	/** The CleanSheets application */
	protected CleanSheets app;

	/** The user interface controller */
	protected UIController uiController;

	/** The file chooser to use when prompting the user for the file to save */
	protected FileChooser chooser;

	/**
	 * Creates a new save as action.
	 * @param app the CleanSheets application
	 * @param uiController the user interface controller
	 * @param chooser the file chooser to use when prompting the user for the file to save
	 */
	public SaveAsAction(CleanSheets app, UIController uiController, FileChooser chooser) {
		// Stores members
		this.app = app;
		this.uiController = uiController;
		this.chooser = chooser;
	}

	protected String getName() {
		return "Save As...";
	}

	protected void defineProperties() {
		putValue(MNEMONIC_KEY, KeyEvent.VK_A);
		putValue(SMALL_ICON, new ImageIcon(CleanSheets.class.getResource("res/img/save_as.gif")));
	}

	public void actionPerformed(ActionEvent e) {
		// Fetches data
		Workbook workbook = uiController.getActiveWorkbook();
		File oldFile = app.getFile(workbook);
		File file = null;

		// Prompts the user for a file
		boolean promptForFile = true;
		while (promptForFile) {
			file = chooser.getFileToSave();
			if (file != null) {
				if (file.exists() && (oldFile == null || !file.equals(oldFile))) {
					// Prompt to overwrite the file
					int option = JOptionPane.showConfirmDialog(
						null,
						"The chosen file " + file + " already exists\n" +
						"Do you want to overwrite it?",
						"Replace existing file?",
						JOptionPane.YES_NO_CANCEL_OPTION,
						JOptionPane.WARNING_MESSAGE
					);

					if (option == JOptionPane.YES_OPTION)
						promptForFile = false;
					else if (option == JOptionPane.CANCEL_OPTION
						  || option == JOptionPane.CLOSED_OPTION)
						return;
				} else
					promptForFile = false;
			} else
				return;
		}

		// Saves the file
		try {
			app.saveAs(workbook, file);
		} catch (IOException ex) {
			showErrorDialog("An I/O error occurred when saving the file.");
			return;
		}
	}

	public void setEnabled(boolean enabled) {
		super.setEnabled(chooser == null ? false : enabled);
	}

	protected boolean requiresFile() {
		return true;
	}
}