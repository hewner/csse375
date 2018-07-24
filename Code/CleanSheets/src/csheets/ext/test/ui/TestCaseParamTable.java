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

import javax.swing.DefaultCellEditor;
import javax.swing.JTextField;
import javax.swing.table.TableModel;

import csheets.ext.test.TestCaseParam;

/**
 * The table used to display and provide editing of test case parameters.
 * @author Björn Lanneskog
 * @author Einar Pehrson
 */
@SuppressWarnings("serial")
public class TestCaseParamTable extends TestTable {

	/**
	 * Creates a new test case parameter table.
	 * @param tableModel the table model
	 */
	public TestCaseParamTable(TableModel tableModel) {
		super(tableModel);

		// Configures test case parameter editor and renderer
		setDefaultRenderer(TestCaseParam.class, new TestCaseParamRenderer());
		setDefaultEditor(TestCaseParam.class, new DefaultCellEditor(
			new JTextField()));
	}

	/**
	 * Overridden to disable editing of precedent addresses.
	 * @param row the row whose value is to be queried
	 * @param column the column whose value is to be queried		 
	 * @return true if the cell at the given row and column is editable
	 */
	public boolean isCellEditable(int row, int column) {
		return column != 0;
	}

	/**
	 * Returns the class of the column in the table.
	 * @param column the column whose class is to be queried
	 * @return class the class to be returned
	 */
	public Class<?> getColumnClass(int column) { 
		if (column == 0)
			return Object.class; // Cell.class
		else
			return TestCaseParam.class;
	}
}