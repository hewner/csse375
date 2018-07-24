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

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import csheets.ui.BlankIcon;

/**
 * An base-class for actions.
 * @author Einar Pehrson
 */
public abstract class BaseAction extends AbstractAction {

	/**
	 * Creates a new base action.
	 */
	public BaseAction() {
		// Configures action
		String name = getName();
		putValue(NAME, name);
		putValue(SHORT_DESCRIPTION, name);
		putValue(ACTION_COMMAND_KEY, name);
		putValue(SMALL_ICON, new BlankIcon(16));
		defineProperties();
	}

	/**
	 * Returns the action's name.
	 * @return the action's name, which is used as short description and action command
	 */
	protected abstract String getName();

	/**
	 * Defines the action's properties.
	 */
	protected void defineProperties() {}

	/**
	 * Returns whether the action requires the active workbook to be
	 * modified in order to be enabled. By default, the method returns false.
	 * @return whether the action should be enabled
	 */
	protected boolean requiresModification() {
		return false;
	}

	/**
	 * Returns whether the action requires the active workbook to be
	 * stored in a file in order to be enabled. By default, the method
	 * returns false.
	 * @return whether the action should be enabled
	 */
	protected boolean requiresFile() {
		return false;
	}

	/**
	 * Shows the user an error message.
	 */
	protected void showErrorDialog(Object message) {
		JOptionPane.showMessageDialog(
			null,
			message,
			"Error",
			JOptionPane.ERROR_MESSAGE
		);
	}
}