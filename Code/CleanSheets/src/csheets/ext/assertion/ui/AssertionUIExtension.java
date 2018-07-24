/*
 * Copyright (c) 2005 Peter Palotas, Fredrik Johansson, Einar Pehrson,
 * Sebastian Kekkonen, Lars Magnus Lång, Malin Johansson and Sofia Nilsson
 *
 * This file is part of
 * CleanSheets Extension for Assertions
 *
 * CleanSheets Extension for Assertions is free software; you can
 * redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either version 2 of
 * the License, or (at your option) any later version.
 *
 * CleanSheets Extension for Assertions is distributed in the hope that
 * it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with CleanSheets Extension for Assertions; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA  02111-1307  USA
 */
package csheets.ext.assertion.ui;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JToolBar;

import csheets.ext.assertion.AssertionExtension;
import csheets.ui.ctrl.UIController;
import csheets.ui.ext.CellDecorator;
import csheets.ui.ext.UIExtension;

/**
 * The user interface extension for assertions.
 * @author Einar Pehrson
 */
public class AssertionUIExtension extends UIExtension {

	/** The icon to display with the extension's name */
	private Icon icon;

	/** A cell decorator that visualizes assertions on cells */
	private CellDecorator cellDecorator;

	/** A toolbar that visualizes assertions on cells */
	private AssertionToolBar toolBar;

	/** A side bar that provides editing of assertions */
	private JComponent sideBar;

	/**
	 * Creates a new user interface extension for assertions.
	 * @param extension the extension for which components are provided
	 * @param uiController the user interface controller
	 */
	public AssertionUIExtension(AssertionExtension extension, UIController uiController) {
		super(extension, uiController);
	}

	/**
	 * Returns an icon to display with the extension's name.
	 * @return an icon with an interval
	 */
	public Icon getIcon() {
		if (icon == null)
			icon = new ImageIcon(
				AssertionExtension.class.getResource("res/img/logo.gif"));
		return icon;
	}

	/**
	 * Returns a cell decorator that visualizes assertions on cells.
	 * @return decorator for assertable cells
	 */
	public CellDecorator getCellDecorator() {
		if (cellDecorator == null)
			cellDecorator = new AssertableCellDecorator();
		return cellDecorator;
	}

	/**
	 * Returns a toolbar that visualizes assertions on cells.
	 * @return a JToolBar component
	 */
	public JToolBar getToolBar() {
		if (toolBar == null) {
			toolBar = new AssertionToolBar();
			uiController.addSelectionListener(toolBar);
		}
		return toolBar;
	}

	/**
	 * Returns a side bar that provides editing of assertions.
	 * @return a side bar
	 */
	public JComponent getSideBar() {
		if (sideBar == null)
			sideBar = new AssertionPanel(uiController);
		return sideBar;
	}
}