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
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import csheets.core.Cell;
import csheets.core.Value;
import csheets.ext.test.TestCase;
import csheets.ext.test.TestCaseParam;
import csheets.ext.test.TestExtension;
import csheets.ext.test.TestableCell;
import csheets.ext.test.TestableCellListener;
import csheets.ui.ctrl.SelectionEvent;
import csheets.ui.ctrl.SelectionListener;
import csheets.ui.ctrl.UIController;

/**
 * A panel for displaying test cases and providing editing of their validation
 * states.
 * @author Björn Lanneskog
 * @author Einar Pehrson
 */
@SuppressWarnings("serial")
public class TestCasePanel extends JPanel implements SelectionListener,
		TestableCellListener {
	
	/** Array which contains labels of columns in the table.*/
	public static final String[] columnNames = {"Formula", "Result", "Validation state"};
		
	/** The table model for this table.*/
	private DefaultTableModel tableModel;
	
	/** The viewable table. */
	private TestCaseTable table;

	/** Determines whether the table arranger should respond to events. */
	private boolean listening = true;

	/**
	 * Creates a new test case panel.
	 * @param uiController the user interface controller
	 */
	public TestCasePanel(UIController uiController) {
		// Creates and configures table
		tableModel = new DefaultTableModel(columnNames, 0);
		table = new TestCaseTable(tableModel);
		tableModel.addTableModelListener(new ValidationStateChanger(uiController));
		
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
		Cell cell = event.getCell();
		if (cell != null) {
			TestableCell activeCell = (TestableCell)cell.getExtension(TestExtension.NAME);
			activeCell.addTestableCellListener(this);
			testCasesChanged(activeCell);
		} else
			// Clears the table
			tableModel.setRowCount(0);

		// Stops listening to previous active cell
		if (event.getPreviousCell() != null)
			((TestableCell)event.getPreviousCell().getExtension(TestExtension.NAME))
				.removeTestableCellListener(this);
	}

	/**
	 * Updates the table of test cases when they have changed.
	 * @param cell the cell in which the event occurred
	 */
	public void testCasesChanged(TestableCell cell) {
		// Clears the table
		tableModel.setRowCount(0);

		if (cell.hasTestCases()) {
			// Fetches the cell's formula, and displays it in the column header
			// String[] colNames = {cell.getFormula().toString(), "Result", "Validation state"};
			// tableModel.setColumnIdentifiers(colNames);
	
			// Adds the test cases to the table
			Set<TestCase> testCases = cell.getTestCases();
			tableModel.setRowCount(testCases.size());
			int row = 0;
			listening = false;
			for (TestCase testCase : testCases) {
				tableModel.setValueAt(testCase, row, 0);
				tableModel.setValueAt(testCase.evaluate(), row, 1);
				tableModel.setValueAt(testCase.getValidationState(), row++, 2);
			}
			listening = true;
		} // else
			// Clears the table header
			// tableModel.setColumnIdentifiers(columnNames);
		table.packColumns();
	}

	public void testCaseParametersChanged(TestableCell cell) {}

	/**
	 * A table model listener for setting the validation state of test cases.
	 * @author Einar Pehrson
	 */
	protected class ValidationStateChanger implements TableModelListener {

		/** The user interface controller */
		private UIController uiController;

		/**
		 * Creates a new validation state changer
		 * @param uiController the user interface controller
		 */
		public ValidationStateChanger(UIController uiController) {
			this.uiController = uiController;
		}

		/**
		 * Sets the validation state of a test case.
		 * @param e the event that was fired
		 */
		public void tableChanged(TableModelEvent e) {
			if(listening && e.getType() == TableModelEvent.UPDATE
					&& e.getFirstRow() >= 0) {
				// Stops listening
				listening = false;

				// Updates test case
				TestCase testCase = (TestCase)tableModel.getValueAt(e.getFirstRow(), 0);
				TestCase.ValidationState state = (TestCase.ValidationState)
					tableModel.getValueAt(e.getFirstRow(), 2);
				if (state != testCase.getValidationState()) {
					testCase.setValidationState(state);
	
					// Updates resulting param
					TestableCell cell = testCase.getCell();
					Value result = testCase.evaluate();
					switch (state) {
						case VALID:
							cell.addTestCaseParam(testCase.evaluate(),
								TestCaseParam.Type.DERIVED);
							break;
						case PENDING:
						case REJECTED:
							TestCaseParam resultParam = null;
							for (TestCaseParam param : cell.getTestCaseParams())
								if (result.equals(param.getValue()))
									resultParam = param;
							if (resultParam != null)
								cell.removeTestCaseParam(resultParam,
									TestCaseParam.Type.DERIVED);
					}
					uiController.setWorkbookModified(cell.getSpreadsheet().getWorkbook());
				}

				// Resumes
				listening = true;
			}
		}
	}
}