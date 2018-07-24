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

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Action;

/**
 * A window adapter that invokes an action on window closing.
 * @author Einar Pehrson
 */
public class WindowClosingHandler extends WindowAdapter {

	/** The window to listen to */
	private Window window;

	/** The action to invoke on window closing */
	private Action action;

	/**
	 * Creates a new window closing handler
	 * @param window the window to listen to
	 * @param action the action to invoke on window closing
	 */
	 public WindowClosingHandler(Window window, Action action) {
	 	this.window = window;
	 	this.action = action;
	 }

	/**
	 * Invokes exit() on the UI controller when a window closing event
	 * is received.
	 * @param e the event that was fied
	 */
	public void windowClosing(WindowEvent e) {
		action.actionPerformed(new ActionEvent(window, ActionEvent.ACTION_PERFORMED,
			action.getValue(Action.ACTION_COMMAND_KEY).toString()));
	}
}