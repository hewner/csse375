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

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Enumeration;
import java.util.LinkedList;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.KeyStroke;
import javax.swing.TransferHandler;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import csheets.core.Address;
import csheets.core.Cell;
import csheets.core.Spreadsheet;
import csheets.ext.style.StylableSpreadsheet;
import csheets.ext.style.StyleExtension;
import csheets.ui.ctrl.SelectionEvent;
import csheets.ui.ctrl.SelectionListener;
import csheets.ui.ctrl.UIController;
import csheets.ui.ext.TableDecorator;
import csheets.ui.ext.UIExtension;
import csheets.ui.grid.Grid;

/**
 * A customized JTable component, used to visualize a spreadsheet.
 * @author Einar Pehrson
 */
@SuppressWarnings("serial")
public class SpreadsheetTable extends Grid implements SelectionListener {

	/** The action command used for the action */
	public static final String CLEAR_SELECTION_COMMAND = "Clear the content of the selected cells";

	/** The spreadsheet that is displayed by the table */
	private Spreadsheet spreadsheet;

	/** The user interface controller */
	private UIController uiController;

	/** The table decorators invoked when painting the table */
	private java.util.List<TableDecorator> decorators
		= new LinkedList<TableDecorator>();

	/** The column width tracker */
	private PropertyChangeListener columnWidthTracker = new ColumnWidthTracker();

	/**
	 * Creates a spreadsheet table for the given spreadsheet.
	 * @param spreadsheet the spreadsheet to display in the table
	 * @param uiController the user interface controller
	 */
	public SpreadsheetTable(Spreadsheet spreadsheet, UIController uiController) {
		this(new SpreadsheetTableModel(spreadsheet, uiController), uiController);
	}

	/**
	 * Creates a spreadsheet table for the given spreadsheet table model.
	 * @param tableModel the spreadsheet table model to display in the table
	 * @param uiController the user interface controller
	 */
	public SpreadsheetTable(SpreadsheetTableModel tableModel, UIController uiController) {
		super(null);

		// Stores members
		this.uiController = uiController;
		uiController.addSelectionListener(this);

		// Configures cell rendering and editing
		setDefaultRenderer(Cell.class, new CellRenderer(uiController));
		setDefaultEditor(Cell.class, new CellEditor(uiController));
		setDragEnabled(true);
		setTransferHandler(uiController.getCellTransferHandler());

		// Configures cell editing actions
		ActionMap actionMap = getActionMap();
		actionMap.put(TransferHandler.getCutAction().getValue(Action.NAME),
			TransferHandler.getCutAction());
		actionMap.put(TransferHandler.getCopyAction().getValue(Action.NAME),
			TransferHandler.getCopyAction());
		actionMap.put(TransferHandler.getPasteAction().getValue(Action.NAME),
			TransferHandler.getPasteAction());
		actionMap.put(CLEAR_SELECTION_COMMAND, new ClearSelectionAction());
		getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0),
			CLEAR_SELECTION_COMMAND);

		// Fetches decorators
		for (UIExtension extension : uiController.getExtensions()) {
			TableDecorator decorator = extension.getTableDecorator();
			if (decorator != null)
				decorators.add(decorator);
		}

		// Updates model
		setModel(tableModel);
	}

/*
 * DATA
 */

	/**
	 * Returns the spreadsheet that the table displays.
	 * @return the spreadsheet that the table displays.
	 */
	public Spreadsheet getSpreadsheet() {
		return spreadsheet;
	}

	/**
	 * Sets the spreadsheet that is displayed by the table.
	 * @param spreadsheet the spreadsheet that is displayed by the table
	 */
	public void setSpreadsheet(Spreadsheet spreadsheet) {
		setModel(new SpreadsheetTableModel(spreadsheet, uiController));
	}

	/**
	 * Sets the data model of the table. Overridden to only accept instances
	 * of the <code>SpreadsheetTableModel</code> class.
	 * @param dataModel the new data source for this table, must be a <code>SpreadsheetTableModel</code>
	 */
	public void setModel(TableModel dataModel) {
		if (!(dataModel instanceof SpreadsheetTableModel))
			return;

		// Updates model
		this.spreadsheet = ((SpreadsheetTableModel)dataModel).getSpreadsheet();
		super.setModel(dataModel);

		// Restores column widths and row heights
		StylableSpreadsheet styleableSpreadsheet = (StylableSpreadsheet)
			spreadsheet.getExtension(StyleExtension.NAME);
		for (int column = 0; column < spreadsheet.getColumnCount(); column++) {
			int columnWidth = styleableSpreadsheet.getColumnWidth(column);
			if (columnWidth != -1)
				columnModel.getColumn(column).setPreferredWidth(columnWidth);
		}
		for (int row = 0; row < spreadsheet.getRowCount(); row++) {
			int rowHeight = styleableSpreadsheet.getRowHeight(row);
			if (rowHeight != -1)
				super.setRowHeight(row, rowHeight);
		}

		// Adds column width listener
		Enumeration<TableColumn> columns = columnModel.getColumns();
		while (columns.hasMoreElements())
			columns.nextElement().addPropertyChangeListener(columnWidthTracker);
	}

/*
 * SELECTION
 */

	/**
	 * Returns the active cell of the spreadsheet table.
	 * @return the active cell of the spreadsheet table
	 */
	public Cell getSelectedCell() {
		int activeColumn = getColumnModel().getSelectionModel().getAnchorSelectionIndex();
		int activeRow = getSelectionModel().getAnchorSelectionIndex();
		return spreadsheet.getCell(new Address(activeColumn, activeRow));
	}

	/**
	 * Returns the currently selected cells in the spreadsheet table.
	 * @return a two-dimensional array of the the currently selected cells in the spreadsheet table
	 */
	public Cell[][] getSelectedCells() {
		int[] rows = getSelectedRows();
		int[] columns = getSelectedColumns();
		Cell[][] range = new Cell[rows.length][columns.length];
		for (int row = 0; row < range.length; row++)
			for (int column = 0; column < range[row].length; column++)
				range[row][column] = spreadsheet.getCell(columns[column], rows[row]);
		return range;
	}

	/**
	 * Clears the currently selected cells in the table.
	 */
	public void clearSelectedCells() {
		for (Cell[] row : getSelectedCells())
			for (Cell cell : row)
				cell.clear();
	}

	/**
	 * Changes the current selection in the table. Overridden to update the
	 * user interface controller as well.
	 * @param row the row that was selected
	 * @param column the column that was selected
	 * @param toggle whether the selection should be toggled
	 * @param extend whether the selection should be extended
	 */
	public void changeSelection(int row, int column, boolean toggle, boolean extend) {
		super.changeSelection(row, column, toggle, extend);
		if (!extend)
			uiController.setActiveCell(getSelectedCell());
	}

	/**
	 * Selects all cells in the spreadsheet table.
	 */
	public void selectAll() {
		super.changeSelection(0, 0, false, false);
		changeSelection(
			spreadsheet.getRowCount(),
			spreadsheet.getColumnCount(), false, true);
		uiController.setActiveCell(getSelectedCell());
	}

	/**
	 * Updates the selection in the table when the active cell is changed.
	 * @param event the selection event that was fired
	 */
	public void selectionChanged(SelectionEvent event) {
		if (spreadsheet == event.getSpreadsheet() && event.isCellChanged()) {
			int activeColumn = getColumnModel().getSelectionModel().getAnchorSelectionIndex();
			int activeRow = getSelectionModel().getAnchorSelectionIndex();
			Address address = event.getCell().getAddress();
			if (event.getPreviousCell() == null || (address.getColumn()
				!= activeColumn || address.getRow() != activeRow)) {
			 	changeSelection(address.getRow(), address.getColumn(), false, false);
			 	requestFocus();
			}
		}
	}

/*
 * DECORATION
 */

	/**
	 * Overridden to delegate painting to decorators.
	 * @param g the Graphics object to protect
	 */
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		// Invokes decorators
		for (TableDecorator decorator : decorators)
			if (decorator.isEnabled())
				decorator.decorate(g, this);
	}

/*
 * HEADERS
 */

	/**
	 * Sets the height for row to rowHeight, revalidates, and repaints. The height of the cells in this row will be equal to the row height minus the row margin. 
	 * @param row - the row whose height is being changed
	 * @param rowHeight - new row height, in pixels 
	 * @throws IllegalArgumentException if rowHeight is less than 1
	 */
	public void setRowHeight(int row, int rowHeight) {
		super.setRowHeight(row, rowHeight);
		uiController.setWorkbookModified(spreadsheet.getWorkbook());
		StylableSpreadsheet styleableSpreadsheet = (StylableSpreadsheet)
			spreadsheet.getExtension(StyleExtension.NAME);
		styleableSpreadsheet.setRowHeight(row, rowHeight);
	}

	/**
	 * A listener that forwards column width changes to the style extension.
	 */
	private class ColumnWidthTracker implements PropertyChangeListener {

		/**
		 * Creates a new column width tracker.
		 */
		public ColumnWidthTracker() {}

		/**
		 * Stores the width of the column that was resized.
		 * @param event the event that was fired
		 */
		public void propertyChange(PropertyChangeEvent event) {
			if (event.getPropertyName().equals("width")) {
				TableColumn source = (TableColumn)event.getSource();
				StylableSpreadsheet styleableSpreadsheet = (StylableSpreadsheet)
					spreadsheet.getExtension(StyleExtension.NAME);
				if (styleableSpreadsheet.getColumnWidth(source.getModelIndex())
						!= source.getWidth()) {
					styleableSpreadsheet.setColumnWidth(
						source.getModelIndex(), source.getWidth());
					uiController.setWorkbookModified(spreadsheet.getWorkbook());
				}
			}
		}
	}

/*
 * ACTIONS
 */

	/**
	 * An action for clearing the content of the selected cells, without
	 * invoking the editor.
	 * @author Einar Pehrson
	 */
	@SuppressWarnings("serial")
	protected class ClearSelectionAction extends AbstractAction {

		/**
		 * Creates a selection clearing action.
		 */
		public ClearSelectionAction() {
			// Configures action
			putValue(NAME, CLEAR_SELECTION_COMMAND);
			putValue(SHORT_DESCRIPTION, CLEAR_SELECTION_COMMAND);
			putValue(ACTION_COMMAND_KEY, CLEAR_SELECTION_COMMAND);
		}

		public void actionPerformed(ActionEvent event) {
			clearSelectedCells();
		}
	}
}