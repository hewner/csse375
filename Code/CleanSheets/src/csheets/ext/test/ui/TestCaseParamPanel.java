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

import java.awt.BorderLayout;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import csheets.core.Cell;
import csheets.ext.test.TestCaseParam;
import csheets.ext.test.TestExtension;
import csheets.ext.test.TestableCell;
import csheets.ui.ctrl.SelectionEvent;
import csheets.ui.ctrl.SelectionListener;
import csheets.ui.ctrl.UIController;

/**
 * A panel for displaying and providing editing of test case parameters
 * @author Björn Lanneskog
 * @author Einar Pehrson
 */
@SuppressWarnings("serial")
public class TestCaseParamPanel extends JPanel implements SelectionListener {

	/** Array which contains labels of columns in the table.*/
	private static final String[] columnNames = 
		{"Precedent", "", "", "", "", "", "", "", "", ""};
	
	/** The table model for this table.*/
	private DefaultTableModel tableModel;
	
	/** The Visible table for entering test case params.*/
	private TestCaseParamTable table;
	
	/** The test case parameter controller.*/
	private TestCaseParamController controller;

	/** Determines whether the table arranger should respond to events. */
	private boolean listening = true;

	/**
	 * Creates a new test case parameter panel.
	 * @param uiController the user interface controller
	 */
	public TestCaseParamPanel(UIController uiController) {
		// Creates and configures table
		controller = new TestCaseParamController(uiController);
		tableModel = new DefaultTableModel(columnNames,0);
		tableModel.addTableModelListener(new TestCaseParamSetter());
		table = new TestCaseParamTable(tableModel);
		
		// Configures layout and adds components
		setLayout(new BorderLayout());
		JScrollPane tableScrollPane = new JScrollPane(table);
		add(tableScrollPane, BorderLayout.CENTER);
	}

	/**
	 * Updates the table of precedents when the active cell is changed.
	 * @param event the selection event that was fired
	 */
	public void selectionChanged(SelectionEvent event) {
		listening = false;

		// Clears the table
		tableModel.setRowCount(0);

		Cell cell = event.getCell();
		if (cell != null) {
			// Resizes the table
			Set<Cell> precedents = cell.getPrecedents();
			tableModel.setRowCount(precedents.size());

			// Adds the test case parameters of each precedent to the table
			int row = 0;
			for (Cell precedent : precedents) {
				TestableCell testablePrecedent = (TestableCell)precedent.getExtension(TestExtension.NAME);
				tableModel.setValueAt(testablePrecedent, row, 0);
				int column = 1;
				for (TestCaseParam param : testablePrecedent.getTestCaseParams()) {
					if (tableModel.getColumnCount() - 1 == column)
						tableModel.addColumn("");
					tableModel.setValueAt(param, row, column++);
				}
				row++;
			}
			table.packColumns();
		}

		listening = true;
	}

	/**
	 * A table model listener for setting the test case parameters of a cell.
	 * @author Björn Lanneskog
	 * @author Einar Pehrson
	 */
	protected class TestCaseParamSetter implements TableModelListener {
		
		public void tableChanged(TableModelEvent e) {
			// If there has been an update on any column but the first
			if(listening && e.getType() == TableModelEvent.UPDATE && e.getColumn() != 0 && listening == true) {

				// Fetches the parameter and the cell
				String param = tableModel.getValueAt(e.getFirstRow(), e.getColumn()).toString().trim();
				TestableCell cell = (TestableCell)tableModel.getValueAt(e.getFirstRow(), 0);

				// Checks if the selected parameter was removed
				if(param.equals(""))
					param = null;

				// Collects the unchanged parameters
				Set<TestCaseParam> unChangedParams = new HashSet<TestCaseParam>();
				Vector params = (Vector)tableModel.getDataVector().elementAt(e.getFirstRow());
				for (Object o : params)
					if(o instanceof TestCaseParam)
						unChangedParams.add((TestCaseParam)o);

				// Stores the parameter
				TestCaseParam added = controller.setTestCaseParams(cell, param, unChangedParams);
				listening = false;
				if (added != null) {
					tableModel.setValueAt(added, e.getFirstRow(), e.getColumn());
					// If the last column was filled, adds another
				} else
					tableModel.setValueAt("", e.getFirstRow(), e.getColumn());
				listening = true;

				// Packs the column
				table.packColumn(e.getColumn());
			}
		}
	}
}