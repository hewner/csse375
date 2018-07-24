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

import java.awt.Dimension;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JToolBar;

import csheets.core.Cell;
import csheets.ext.test.TestExtension;
import csheets.ext.test.TestableCell;
import csheets.ext.test.TestableCellListener;
import csheets.ext.test.TestableSpreadsheet;
import csheets.ui.ctrl.SelectionEvent;
import csheets.ui.ctrl.SelectionListener;

/**
 * A toolbar that shows information about test cases.
 * @author Lars Magnus "Burken" Lång
 * @author Sebastian Kekkonen
 */
@SuppressWarnings("serial")
public class TestToolBar extends JToolBar implements SelectionListener,
		TestableCellListener {

	private JProgressBar cellTestednessBar;
	private JProgressBar spreadsheetTestednessBar;
	private JLabel lightLabelTestCases;
	private Icon redIcon;
	private Icon yellowIcon;
	private Icon greenIcon;

	public TestToolBar() {
		setName(TestExtension.NAME);

		// Loads icons
		redIcon = new ImageIcon(TestExtension.class.getResource(
			"res/img/light_red.gif"), "Red light");
		yellowIcon = new ImageIcon(TestExtension.class.getResource(
			"res/img/light_yellow.gif"), "Yellow light");
		greenIcon = new ImageIcon(TestExtension.class.getResource(
			"res/img/light_green.gif"), "Green light");

		// Creates icon labels
		lightLabelTestCases = new JLabel(yellowIcon);

		// Creates cell testedness progress bar
		cellTestednessBar = new JProgressBar(JProgressBar.VERTICAL);
		cellTestednessBar.setPreferredSize(new Dimension(20, 20));
		cellTestednessBar.setToolTipText("Cell Testedness");
		cellTestednessBar.setString("0 %");

		// Creates spreadsheet testedness progress bar
		spreadsheetTestednessBar = new JProgressBar(JProgressBar.VERTICAL);
		spreadsheetTestednessBar.setPreferredSize(new Dimension(20, 20));
		spreadsheetTestednessBar.setToolTipText("Spreadsheet Testedness");
		spreadsheetTestednessBar.setString("0 %");

		// Lays out components
		add(new JLabel(new ImageIcon(
				TestExtension.class.getResource("res/img/logo.gif"))));
		addSeparator();
		add(lightLabelTestCases);
		addSeparator();
		add(cellTestednessBar);
		addSeparator();
		add(spreadsheetTestednessBar);
	}

	public void selectionChanged(SelectionEvent event) {
		Cell cell = event.getCell();
		if (cell != null) {
			TestableCell activeCell = (TestableCell)cell.getExtension(TestExtension.NAME);
			activeCell.addTestableCellListener(this);
			testCasesChanged(activeCell);
		} else {
			setTestCaseLamp(2);
			setTestCaseProgressBar(0);
		}

		// Stops listening to previous active cell
		if (event.getPreviousCell() != null)
			((TestableCell)event.getPreviousCell().getExtension(TestExtension.NAME))
				.removeTestableCellListener(this);

		// Updates spreadsheet testedness
		if (event.getSpreadsheet() != null) {
			TestableSpreadsheet spreadsheet = (TestableSpreadsheet)
				event.getSpreadsheet().getExtension(TestExtension.NAME);
			setSpreadsheetTestednessBar(spreadsheet.getTestedness());
		} else
			setSpreadsheetTestednessBar(0);
	}

	public void testCasesChanged(TestableCell cell) {
		// Updates cell testedness
		double testedness = cell.getTestedness();
		setTestCaseProgressBar(testedness);
		if (cell.hasTestError())
			setTestCaseLamp(3);
		else if (testedness < 1.0 )
			setTestCaseLamp(2);
		else
			setTestCaseLamp(1);
	}

	public void testCaseParametersChanged(TestableCell cell) {}

	/*
	 *
	 * @param lamp 1-green, 2-yellow, 3-red
	 */
	private void setTestCaseLamp(int lamp){
		switch(lamp){
		case 1: 
			lightLabelTestCases.setIcon(greenIcon);
			break;
		case 2:
			lightLabelTestCases.setIcon(yellowIcon);
			break;
		case 3:
			lightLabelTestCases.setIcon(redIcon);
			break;
		default:
			lightLabelTestCases.setIcon(yellowIcon);
			break;
		}
	}

	private void setTestCaseProgressBar(double testedness){
		cellTestednessBar.setValue((int)(testedness*100));
		cellTestednessBar.setString((int)(testedness*100) + "%");				
	}

	private void setSpreadsheetTestednessBar(double testedness){
		spreadsheetTestednessBar.setValue((int)(testedness*100));
		spreadsheetTestednessBar.setString((int)(testedness*100) + "%");
	}
}