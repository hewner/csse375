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
package csheets.ui;

import java.awt.Insets;

import javax.swing.JToolBar;

import csheets.ui.ctrl.ActionManager;

/**
 * The tool bar that displays standard file- and edit-related actions.
 * @author Einar Pehrson
 */
@SuppressWarnings("serial")
public class StandardToolBar extends JToolBar {

	/**
	 * Creates a new standard tool bar.
	 * @param actionManager a factory for actions
	 */
	public StandardToolBar(ActionManager actionManager) {
		super("Standard");

		// Creates common insets
		Insets insets = new Insets(2, 2, 2, 2);

		// Adds file actions
		add(actionManager.getAction("new")).setMargin(insets);
		add(actionManager.getAction("open")).setMargin(insets);
		add(actionManager.getAction("save")).setMargin(insets);
		addSeparator();

		// Adds edit actions
		add(actionManager.getAction("undo")).setMargin(insets);
		add(actionManager.getAction("redo")).setMargin(insets);
		addSeparator();
		add(actionManager.getAction("copy")).setMargin(insets);
		add(actionManager.getAction("cut")).setMargin(insets);
		add(actionManager.getAction("paste")).setMargin(insets);
	}
}