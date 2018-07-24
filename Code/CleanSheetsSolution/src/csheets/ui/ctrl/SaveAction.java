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
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

import csheets.CleanSheets;
import csheets.core.Workbook;
import csheets.ui.FileChooser;

/**
 * An action for saving a spreadsheet to the current working file.
 * @author Einar Pehrson
 */
@SuppressWarnings("serial")
public class SaveAction extends SaveAsAction {

	/**
	 * Creates a new save command
	 * @param app the CleanSheets application
	 * @param uiController the user interface controller
	 * @param chooser the file chooser to use when prompting the user for the file to save
	 */
	public SaveAction(CleanSheets app, UIController uiController, FileChooser chooser) {
		super(app, uiController, chooser);
	}

	protected String getName() {
		return "Save";
	}

	protected void defineProperties() {
		putValue(MNEMONIC_KEY, KeyEvent.VK_S);
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		putValue(SMALL_ICON, new ImageIcon(CleanSheets.class.getResource("res/img/save.gif")));
	}

	public void actionPerformed(ActionEvent e) {
		Workbook workbook = uiController.getActiveWorkbook();
		if (app.isWorkbookStored(workbook))
			try {
				app.save(workbook);
			} catch (IOException ex) {
				showErrorDialog("An I/O error occurred when saving the file.");
				return;
			}
		else
			super.actionPerformed(e);
	}

	protected boolean requiresModification() {
		return true;
	}

	protected boolean requiresFile() {
		return false;
	}
}