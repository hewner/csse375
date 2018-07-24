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
import java.io.FileNotFoundException;
import java.io.InvalidClassException;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;

import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

import csheets.CleanSheets;
import csheets.core.Workbook;
import csheets.ui.FileChooser;

/**
 * An action for opening a spreadsheet.
 * @author Einar Pehrson
 */
@SuppressWarnings("serial")
public class OpenAction extends BaseAction {

	/** The CleanSheets application */
	protected CleanSheets app;

	/** The user interface controller */
	protected UIController uiController;

	/** The file chooser to use when prompting the user for the file to open */
	private FileChooser chooser;

	/**
	 * Creates a new open action.
	 * @param app the CleanSheets application
	 * @param uiController the user interface controller
	 * @param chooser the file chooser to use when prompting the user for the file to open
	 */
	public OpenAction(CleanSheets app, UIController uiController, FileChooser chooser) {
		// Stores members
		this.app = app;
		this.uiController = uiController;
		this.chooser = chooser;
		setEnabled(chooser != null);
	}

	protected String getName() {
		return "Open...";
	}

	protected void defineProperties() {
		putValue(MNEMONIC_KEY, KeyEvent.VK_O);
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		putValue(SMALL_ICON, new ImageIcon(CleanSheets.class.getResource("res/img/open.gif")));
	}

	public void actionPerformed(ActionEvent event) {
		File file = getFile();
		if (file != null) {
			Workbook workbook = app.getWorkbook(file);
			if (workbook == null)
				try {
					// Opens the file
					app.load(file);
				} catch (FileNotFoundException fe) {
					showErrorDialog("The file you requested was not found.");
				} catch (ClassNotFoundException e) {
					showErrorDialog("The class of a serialized object cannot be found:\n" + e.getMessage() + ".");
				} catch (InvalidClassException e) {
					showErrorDialog("Something is wrong with a class used by serialization.");
				} catch (StreamCorruptedException e) {
					showErrorDialog("Control information in the input stream is inconsistent.");
				} catch (OptionalDataException e) {
					showErrorDialog("Primitive data was found in the input stream instead of objects.");
				} catch (Exception e) {
					showErrorDialog("An I/O error occurred when loading the file.");
				}
			else
				uiController.setActiveWorkbook(workbook);
		}
	}

	/**
	 * Returns the file to open.
	 * @return file the file to open
	 */
	public File getFile() {
		return chooser.getFileToOpen();
	}
}