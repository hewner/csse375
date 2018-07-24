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
package csheets.ext.style.ui;

import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.SwingConstants;

import csheets.ext.style.StylableCell;
import csheets.ext.style.StyleExtension;
import csheets.ui.ctrl.UIController;

/**
 * A middle-alignment operation.
 * @author Einar Pehrson
 */
@SuppressWarnings("serial")
public class AlignMiddleAction extends StyleAction {

	/**
	 * Creates a new align middle action.
	 * @param uiController the user interface controller
	 */
	public AlignMiddleAction(UIController uiController) {
		super(uiController);
	}

	protected String getName() {
		return "Middle Align";
	}

	protected void defineProperties() {
		putValue(MNEMONIC_KEY, KeyEvent.VK_M);
		putValue(SMALL_ICON, new ImageIcon(StyleExtension.class.getResource("res/img/align_middle.gif")));
	}

	/**
	 * Aligns the content of the given cell to the middle.
	 * @param cell the cell to which style should be applied
	 */
	protected void applyStyle(StylableCell cell) {
		cell.setVerticalAlignment(SwingConstants.CENTER);
	}
}