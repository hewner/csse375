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

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

/**
 * A base class for the test case and test case parameter tables.
 * @author Einar Pehrson
 */
@SuppressWarnings("serial")
public class TestTable extends JTable {

	/** The margin around the packed columns for this model. */
    public static final int COLUMN_MARGIN = 10;

	/** The minimum width of a column. */
    public static final int MINIMUM_COLUMN_WIDTH = 30;

	/** The table's column model */
	private DefaultTableColumnModel columnModel;

	/**
	 * Creates a new test table.
	 * @param tableModel the table model
	 */
	public TestTable(TableModel tableModel) {
		super(tableModel);

		// Stores members
		this.columnModel = (DefaultTableColumnModel)getColumnModel();

		// Prevents user from moving and resizing columns
		getTableHeader().setReorderingAllowed(false);
		getTableHeader().setResizingAllowed(false);
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		// Configures selection mode
		setColumnSelectionAllowed(true);
		setRowSelectionAllowed(true);
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		// Packs columns into right size
		packColumns();
	}

	/**
	 * Adjusts the width of all the columns in the table.
	 */
	public void packColumns() {
        for (int i = 0; i < getColumnCount(); i++)
			packColumn(i);
	}

	/**
	 * Adjusts the width of the column at the given index.
	 * @param columnIndex the index of the column to pack
	 */
	public void packColumn(int columnIndex) {
		TableColumn column = columnModel.getColumn(columnIndex);

		// Get width of column header
		TableCellRenderer renderer = column.getHeaderRenderer();
		if (renderer == null)
			renderer = this.getTableHeader().getDefaultRenderer();
		Component comp = renderer.getTableCellRendererComponent(
			this, column.getHeaderValue(), false, false, 0, 0);
		int width = comp.getPreferredSize().width;
		
		// Get maximum width of column data
		for (int rowIndex = 0; rowIndex < this.getRowCount(); rowIndex++) {
			renderer = this.getCellRenderer(rowIndex, columnIndex);
			comp = renderer.getTableCellRendererComponent(this, getValueAt(
				rowIndex, columnIndex), false, false, rowIndex, columnIndex);
			width = Math.max(width, comp.getPreferredSize().width);
		}

		// Add margin
		width += 2 * COLUMN_MARGIN;
		if (width > MINIMUM_COLUMN_WIDTH) {
			column.setPreferredWidth(width);
		} else
			column.setPreferredWidth(MINIMUM_COLUMN_WIDTH);
	}
}