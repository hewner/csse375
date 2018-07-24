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
package csheets.ui.sheet;

import javax.swing.table.AbstractTableModel;

import csheets.core.Address;
import csheets.core.Cell;
import csheets.core.CellListener;
import csheets.core.Spreadsheet;
import csheets.ui.ctrl.UIController;

/**
 * An table model that provides a table with data from a spreadsheet.
 * @author Einar Pehrson
 */
@SuppressWarnings("serial")
public class SpreadsheetTableModel extends AbstractTableModel {

	/** The spreadsheet that provides the data */
	private Spreadsheet spreadsheet;

	/** The user interface controller */
	private UIController uiController;

	/** The cell listener that monitors all cells in the spreadsheet */
	private CellListener cellListener = new CellChangeListener();

	/**
	 * Creates a spreadsheet table model.
	 * @param spreadsheet the spreadsheet that provides the data
	 * @param uiController the user interface controller
	 */
	public SpreadsheetTableModel(Spreadsheet spreadsheet, UIController uiController) {
		this.spreadsheet = spreadsheet;
		this.uiController = uiController;
		spreadsheet.addCellListener(cellListener);
	}

	/**
	 * Returns <code>Cell.class</code> for all columns.
	 * @param column the column whose data type is requested
	 * @return <code>Cell.class</code>
	 */
	public Class<?> getColumnClass(int column) {
		return Cell.class;
	}

	public int getRowCount() {
		return Math.max(128, spreadsheet.getRowCount() + 1);
	}

	public int getColumnCount() {
		return Math.max(52, spreadsheet.getRowCount() + 1);
	}

	/**
	 * Returns the spreadsheet that provides the data.
	 * @return the spreadsheet that provides the data
	 */
	public Spreadsheet getSpreadsheet() {
		return spreadsheet;
	}

	/**
	 * Returns the cell at the given row and column in the spreadsheet.
	 * @param row the row index of the cell being requested
	 * @param column the column index of the cell being requested
	 * @return the cell at the given address
	 * @see Spreadsheet#getCell(Address)
	 */
	public Object getValueAt(int row, int column) {
		if (column <= spreadsheet.getColumnCount() && row <= spreadsheet.getRowCount())
			return spreadsheet.getCell(new Address(column, row));
		else
			return null;
	}

	/**
	 * Implemented as a no-op, since the table's <code>CellEditor</code>
	 * instance updates the content of cells.
	 * @param value the value to set
	 * @param row the row index of the cell to change
	 * @param column the column index of the cell to change
	 * @see Cell#setContent(String)
	 */
	public void setValueAt(Object value, int row, int column) {}

	/**
	 * Overridden to allow editing of the table.
	 * @param row the row whose value is to be queried
	 * @param column the column whose value is to be queried		 
	 * @return true if the cell at the given row and column is editable
	 */
	public boolean isCellEditable(int row, int column) {
		return true;
	}

	/**
	 * A cell listener that monitors all cells in the spreadsheet, fires table
	 * model events when values change, and notifies the UI controller when
	 * cell content changes.
	 */
	private class CellChangeListener implements CellListener {

		/**
		 * Creates a new cell change listener.
		 */
		public CellChangeListener() {}

		/**
		 * Fires a table model event to reflect that the cell was updated.
		 * @param cell the cell that was modified
		 */
		public void valueChanged(Cell cell) {
			fireTableCellUpdated(cell.getAddress().getRow(),
				cell.getAddress().getColumn());
		}

		/**
		 * Notifies the UI controller that the workbook has been modified.
		 * @param cell the cell that was modified
		 */
		public void contentChanged(Cell cell) {
			uiController.setWorkbookModified(cell.getSpreadsheet().getWorkbook());
		}

		public void dependentsChanged(Cell cell) {}
		public void cellCleared(Cell cell) {}
		public void cellCopied(Cell cell, Cell source) {}
	}
}