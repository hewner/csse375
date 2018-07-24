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
import javax.swing.JComboBox;
import javax.swing.table.TableModel;

import csheets.core.Value;
import csheets.ext.test.TestCase;

/**
 * The table used to display test cases, and to provide editing of validation
 * states.
 * @author Einar Pehrson
 */
@SuppressWarnings("serial")
public class TestCaseTable extends TestTable {

	/**
	 * Creates a new test case table.
	 * @param tableModel the table model
	 */
	public TestCaseTable(TableModel tableModel) {
		super(tableModel);

		// Configures value renderer
		setDefaultRenderer(Value.class, new TestCaseResultRenderer());

		// Configures validation state editor and renderer
		setDefaultRenderer(TestCase.ValidationState.class, new ValidationStateRenderer());
		setDefaultEditor(TestCase.ValidationState.class, new DefaultCellEditor(
			new JComboBox(TestCase.ValidationState.values())));
	}

	/**
	 * Overridden to only allow editing of validation states.
	 * @param row the row whose value is to be queried
	 * @param column the column whose value is to be queried		 
	 * @return true if the cell at the given row and column is editable
	 */
	public boolean isCellEditable(int row, int column) {
		return getColumnClass(column) == TestCase.ValidationState.class;
	}


	/**
	 * Returns the class of the column in the table.
	 * @param column the column whose class is to be queried
	 * @return class the class to be returned
	 */
	public Class<?> getColumnClass(int column) {
		switch (column) {
		//	case 0:	return TestCase.class;
			case 1: return Value.class;
			case 2: return TestCase.ValidationState.class;
			default: return Object.class;
		}
	}
}