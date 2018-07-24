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
import javax.swing.KeyStroke;

import csheets.CleanSheets;
import csheets.ui.FileChooser;

/**
 * An action for terminating the application.
 * @author Einar Pehrson
 */
@SuppressWarnings("serial")
public class ExitAction extends CloseAllAction {

	/**
	 * Creates a new exit action.
	 * @param app the CleanSheets application
	 * @param uiController the user interface controller
	 * @param chooser the file chooser to use when prompting the user for the file to save
	 */
	public ExitAction(CleanSheets app, UIController uiController, FileChooser chooser) {
		super(app, uiController, chooser);
	}

	protected String getName() {
		return "Exit";
	}

	protected void defineProperties() {
		putValue(MNEMONIC_KEY, KeyEvent.VK_X);
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
		putValue(SMALL_ICON, new ImageIcon(CleanSheets.class.getResource("res/img/exit.gif")));
	}

	public void actionPerformed(ActionEvent e) {
		// Closes all workbooks
		super.actionPerformed(e);

		if (uiController.getActiveWorkbook() == null)
			// All workbooks were closed, exits the application
			app.exit();
	}
}