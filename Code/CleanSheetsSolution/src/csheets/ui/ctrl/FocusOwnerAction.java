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

import java.awt.KeyboardFocusManager;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import csheets.ui.sheet.SpreadsheetTable;

/**
 * An action to be invoked on the spreadsheet table that is the focus owner.
 * @author Einar Pehrson
 */
public abstract class FocusOwnerAction extends BaseAction implements PropertyChangeListener {

	/** The current focus owner */
	protected SpreadsheetTable focusOwner;

	/**
	 * Creates a new focus owner action.
	 */
	public FocusOwnerAction() {
		KeyboardFocusManager.getCurrentKeyboardFocusManager()
			.addPropertyChangeListener("permanentFocusOwner", this);
	}

	/**
	 * Stores the focus owner
	 * @param event the event that was fired
	 */
	public void propertyChange(PropertyChangeEvent event) {
		Object o = event.getNewValue();
		if (o instanceof SpreadsheetTable)
			focusOwner = (SpreadsheetTable)o;
	}
}