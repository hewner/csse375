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

import javax.swing.ImageIcon;

import csheets.CleanSheets;

/**
 * A row removal operation.
 * @author Einar Pehrson
 */
@SuppressWarnings("serial")
public class RemoveRowAction extends FocusOwnerAction {

	/**
	 * Creates a new row removal action.
	 */
	public RemoveRowAction() {}

	protected String getName() {
		return "Remove Row";
	}

	protected void defineProperties() {
		setEnabled(false);
		putValue(SMALL_ICON, new ImageIcon(CleanSheets.class.getResource("res/img/remove_row.gif")));
	}

	/**
	 * Removes the rows of the selected cells in the focus owner table.
	 * @param event the event that was fired
	 */
	public void actionPerformed(ActionEvent event) {
		if (focusOwner == null)
			return;
	}
}