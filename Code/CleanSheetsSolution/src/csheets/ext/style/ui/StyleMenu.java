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

import javax.swing.JMenu;

import csheets.ui.ctrl.UIController;

/**
 * A menu that displays style-related actions.
 * @author Einar Pehrson
 */
@SuppressWarnings("serial")
public class StyleMenu extends JMenu {

	/**
	 * Creates a new style menu.
	 * @param uiController the user interface controller
	 */
	public StyleMenu(UIController uiController) {
		super("Style");
		setMnemonic(KeyEvent.VK_S);

		// Adds font actions
		add(new FontAction(uiController));

		// Adds font style actions
		add(new BoldAction(uiController));
		add(new ItalicAction(uiController));
		add(new UnderlineAction(uiController));
		addSeparator();

		// Adds color and border actions
		add(new FormatAction(uiController));
		add(new BorderAction(uiController));
		add(new ForegroundAction(uiController));
		add(new BackgroundAction(uiController));
		addSeparator();

		// Adds alignment actions
		add(new AlignLeftAction(uiController));
		add(new AlignCenterAction(uiController));
		add(new AlignRightAction(uiController));
		addSeparator();
		add(new AlignTopAction(uiController));
		add(new AlignMiddleAction(uiController));
		add(new AlignBottomAction(uiController));
	}
}