/*
 * Copyright (c) 2005 Jens Schou, Staffan Gustafsson, Björn Lanneskog, 
 * Einar Pehrson and Sebastian Kekkonen
 *
 * This file is part of
 * CleanSheets Extension for Test Cases
 *
 * CleanSheets Extension for Test Cases is free software; you can
 * redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either version 2 of
 * the License, or (at your option) any later version.
 *
 * CleanSheets Extension for Test Cases is distributed in the hope that
 * it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with CleanSheets Extension for Test Cases; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA  02111-1307  USA
 */
package csheets.ext.test.ui;

import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.border.TitledBorder;

import csheets.ext.test.TestExtension;
import csheets.ui.ctrl.UIController;
import csheets.ui.ext.CellDecorator;
import csheets.ui.ext.UIExtension;

/**
 * The user interface extension for tests.
 * @author Einar Pehrson
 */
public class TestUIExtension extends UIExtension {

	/** The icon to display with the extension's name */
	private Icon icon;

	/** A cell decorator that visualizes test cases for cells */
	private CellDecorator cellDecorator;

	/** A toolbar that visualizes cell and spreadsheet testedness */
	private TestToolBar toolBar;

	/** A side bar that provides editing of test cases and test case parameters */
	private JComponent sideBar;

	/**
	 * Creates a new user interface extension for tests.
	 * @param extension the extension for which components are provided
	 * @param uiController the user interface controller
	 */
	public TestUIExtension(TestExtension extension, UIController uiController) {
		super(extension, uiController);
	}

	/**
	 * Returns an icon to display with the extension's name.
	 * @return an icon with a testing pad
	 */
	public Icon getIcon() {
		if (icon == null)
			icon = new ImageIcon(
				TestExtension.class.getResource("res/img/logo.gif"));
		return icon;
	}

	/**
	 * Returns a cell decorator that visualizes test cases for cells.
	 * @return decorator for testable cells
	 */
	public CellDecorator getCellDecorator() {
		if (cellDecorator == null)
			cellDecorator = new TestableCellDecorator();
		return cellDecorator;
	}

	/**
	 * Returns a toolbar that visualizes cell and spreadsheet testedness.
	 * @return a JToolBar component
	 */
	public JToolBar getToolBar() {
		if (toolBar == null) {
			toolBar = new TestToolBar();
			uiController.addSelectionListener(toolBar);
		}
		return toolBar;
	}

	/**
	 * Returns a side bar that provides editing of test cases and test case
	 * parameters.
	 * @return a side bar
	 */
	public JComponent getSideBar() {
		if (sideBar == null) {
			// Creates and configures components
			TestCasePanel testCasePanel = new TestCasePanel(uiController);
			TestCaseParamPanel testCaseParamPanel = new TestCaseParamPanel(uiController);
			uiController.addSelectionListener(testCasePanel);
			uiController.addSelectionListener(testCaseParamPanel);
	
			// Adds borders
			TitledBorder border = BorderFactory.createTitledBorder("Test Cases");
			border.setTitleJustification(TitledBorder.CENTER);
			testCasePanel.setBorder(border);
			border = BorderFactory.createTitledBorder("Test Case Parameters");
			border.setTitleJustification(TitledBorder.CENTER);
			testCaseParamPanel.setBorder(border);
	
			// Creates side bar
			sideBar = new JPanel(new GridLayout(2, 1));
			sideBar.add(testCaseParamPanel);
			sideBar.add(testCasePanel);
			sideBar.setName(TestExtension.NAME);
		}
		return sideBar;
	}
}