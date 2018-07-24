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

import javax.swing.Action;

/**
 * A clipboard action, i.e. a cut, copy or paste operation.
 * @author Einar Pehrson
 */
public abstract class ClipboardAction extends FocusOwnerAction {

	/**
	 * Creates a new clipboard action.
	 */
	public ClipboardAction() {}

	/**
	 * Forwards the event to the focus owner.
	 * @param event the event that was fired
	 */
	public void actionPerformed(ActionEvent event) {
		if (focusOwner == null)
			return;
		String command = (String)event.getActionCommand();
		Action action = focusOwner.getActionMap().get(command);
		if (action != null)
			action.actionPerformed(
				new ActionEvent(focusOwner, ActionEvent.ACTION_PERFORMED, null));
	}
}