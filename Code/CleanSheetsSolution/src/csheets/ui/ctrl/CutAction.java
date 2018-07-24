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

import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;
import javax.swing.TransferHandler;

import csheets.CleanSheets;

/**
 * A cut operation.
 * @author Einar Pehrson
 */
@SuppressWarnings("serial")
public class CutAction extends ClipboardAction {

	/**
	 * Creates a new cut action.
	 */
	public CutAction() {}

	protected String getName() {
		return "Cut";
	}

	protected void defineProperties() {
		setEnabled(false);
		putValue(ACTION_COMMAND_KEY, (String)TransferHandler.getCutAction().getValue(Action.NAME));
		putValue(MNEMONIC_KEY, KeyEvent.VK_X);
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
		putValue(SMALL_ICON, new ImageIcon(CleanSheets.class.getResource("res/img/cut.gif")));
	}
}