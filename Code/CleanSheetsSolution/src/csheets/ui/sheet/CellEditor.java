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

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.EventObject;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableCellEditor;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

import csheets.core.Address;
import csheets.core.Cell;
import csheets.core.formula.compiler.FormulaCompilationException;
import csheets.core.formula.lang.UnknownElementException;
import csheets.ui.ctrl.SelectionEvent;
import csheets.ui.ctrl.SelectionListener;
import csheets.ui.ctrl.UIController;

/**
 * The table editor used for editing cells in a spreadsheet.
 * @author Einar Pehrson
 */
@SuppressWarnings("serial")
public class CellEditor extends JTextField implements TableCellEditor, SelectionListener {

	/** The required number of mouse clicks before editing starts */
	public static final int CLICK_COUNT_TO_START = 2;

	/** The action command used for the cancel action */
	public static final String CANCEL_COMMAND = "Cancel editing";

	/** The shared document used to store cell contents */
	private static Document document = new PlainDocument();

	/** The cell that is being edited */
	private Cell cell;

	/** Whether the next edit should keep the content of the cell */
	private boolean resumeOnNextEdit = false;

	/** The change event that is fired */
	private ChangeEvent changeEvent = new ChangeEvent(this);

	/** The user interface controller */
	private UIController uiController;

	/**
	 * Creates a new cell editor.
	 * @param uiController the user interface controller
	 */
	public CellEditor(UIController uiController) {
		// Stores members
		this.uiController = uiController;
		uiController.addSelectionListener(this);
		setDocument(document);

		// Applies actions
		setAction(new StopAction(0, 1));
		getActionMap().put(CANCEL_COMMAND, new CancelAction());
		getActionMap().put("Stop and move up", new StopAction(0, -1));
		getActionMap().put("Stop and move down", new StopAction(0, 1));
		getActionMap().put("Stop and move left", new StopAction(-1, 0));
		getActionMap().put("Stop and move right", new StopAction(1, 0));
		getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), CANCEL_COMMAND);
		getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "Stop and move up");
		getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "Stop and move down");
		getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, KeyEvent.SHIFT_MASK), "Stop and move left");
		getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0), "Stop and move right");
	}

	/**
	 * Stops editing and updates the cell's content.
	 * @return true if a change was made, and unless an erroneous formula was entered
	 */
	public boolean stopCellEditing() {
		String content = getText();
		if (cell != null && content != null) {
			// Halts if nothing was changed
			if (content.equals(cell.getContent())) {
				cancelCellEditing();
				return false;
			}

			// Updates cell content (and parses formula)
			try {
				cell.setContent(content);
			} catch (FormulaCompilationException e) {
				// Retrieves correct message
				String message;
				if (e.getCause() instanceof antlr.TokenStreamRecognitionException)
					message = "The parser responded: " +
						((antlr.TokenStreamRecognitionException)e.getCause()).recog.getMessage();
				else if (e instanceof UnknownElementException)
					message = "The parser recognized the formula, but a language"
						+ " element (" + ((UnknownElementException)e).getIdentifier()
						+ ") could not be created.";
				else
					message = e.getMessage();

				// Finds the window that contains the editor
				Component parent = SwingUtilities.getWindowAncestor(this);
				if (parent == null)
					parent = this;

				// Inform user of erroneous syntax
				JOptionPane.showMessageDialog(
					parent,
					"The entered formula could not be compiled\n"
					 + message,
					"Formula compilation error",
					JOptionPane.ERROR_MESSAGE
				);
				return false;
			}
		}

		fireEditingStopped();
		return true;
	}

	/**
	 * Returns the cell that is (or was) being edited.
	 * @return the cell that is (or was) being edited
	 */
	public Cell getCellEditorValue() {
		return cell;
	}

	/**
	 * Checks if the given event should cause editing to be resumed.
	 * @param event the event that was fired
	 * @return true unless the click-count of a mouse event is too low
	 */
	public boolean isCellEditable(EventObject event) {
		// Checks whether the event should cause editing to be resumed
		resumeOnNextEdit = event instanceof MouseEvent
			|| (event instanceof ActionEvent &&
				((ActionEvent)event).getActionCommand().equals(
					SpreadsheetTable.RESUME_EDIT_COMMAND));

		// Checks whether editing should start
		if (event instanceof MouseEvent)
			return ((MouseEvent)event).getClickCount() >= CLICK_COUNT_TO_START;
		else
			return true;
	}

	/**
	 * Returns true if the given event should cause the cell to be selected.
	 * @param event the event that was fired
	 * @return true
	 */
	public boolean shouldSelectCell(EventObject event) { 
		return true; 
	}

	/**
	 * Invoked when editing is cancelled. Simply fires an event.
	 */
	public void cancelCellEditing() { 
		fireEditingCanceled(); 
	}

	/**
	 * Stores the given cell in the editor. Depnding on if editing should
	 * be resumed, the text displayed in the editor is either the cell's
	 * content or an empty string.
	 * @param table the table in which the cell is located
	 * @param value the cell to edit
	 * @param selected whether the cell is selected
	 * @param row the row in which the cell is located
	 * @param column the column in which the cell is located
	 */
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean selected, int row, int column) {
		if (value != null && value instanceof Cell) {
			cell = (Cell)value;
			if (resumeOnNextEdit)
				setText(((Cell)value).getContent());
			else
				setText("");
		}
		return this;
	}

	/**
	 * Updates the text field with the content of the new active cell.
	 * @param event the selection event that was fired
	 */
	public void selectionChanged(SelectionEvent event) {
		cell = event.getCell();
		if (cell != null)
			setText(cell.getContent());
		else
			setText("");
	}

	/**
	 * Adds a <code>CellEditorListener</code> to the listener list.
	 * @param listener the new listener to be added
	 */
	public void addCellEditorListener(CellEditorListener listener) {
		listenerList.add(CellEditorListener.class, listener);
	}

	/**
	 * Removes a <code>CellEditorListener</code> from the listener list.
	 * @param listener the listener to be removed
	 */
	public void removeCellEditorListener(CellEditorListener listener) {
		listenerList.remove(CellEditorListener.class, listener);
	}

	/**
	 * Returns an array of all the <code>CellEditorListener</code>s added.
	 * @return all of the <code>CellEditorListener</code>s added
	 */
	public CellEditorListener[] getCellEditorListeners() {
		return (CellEditorListener[])listenerList.getListeners(CellEditorListener.class);
	}

	/**
	 * Notifies all listeners that editing was stopped.
	 */
	private void fireEditingStopped() {
		Object[] listeners = listenerList.getListenerList();
		for (int i = listeners.length-2; i>=0; i-=2) {
			if (listeners[i] == CellEditorListener.class)
				((CellEditorListener)listeners[i+1]).editingStopped(changeEvent);
		}
	}

	/**
	 * Notifies all listeners that editing was stopped.
	 */
	private void fireEditingCanceled() {
		Object[] listeners = listenerList.getListenerList();
		for (int i = listeners.length-2; i>=0; i-=2) {
			if (listeners[i] == CellEditorListener.class)
				((CellEditorListener)listeners[i+1]).editingCanceled(changeEvent);
		}
	}

	/**
	 * An action for stopping editing of a cell.
	 * @author Einar Pehrson
	 */
	protected class StopAction extends AbstractAction {

		/** The number of columns to move the selection down */
		private int columns = 0;

		/** The number of rows to move the selection to the right */
		private int rows = 0;

		/**
		 * Creates an edit stopping action. When the action is invoked
	 	 * the active cell selection is moved the given number of columns
	 	 * and rows.
		 * @param columns the number of columns to move the selection down
		 * @param rows the number of rows to move the selection to the right
		 */
		public StopAction(int columns, int rows) {
			// Stores members
			this.columns = columns;
			this.rows = rows;
		}

		public void actionPerformed(ActionEvent event) {
			if (stopCellEditing() && cell != null) {
				// Transfers focus away from the text field
				transferFocus();

				// Moves the active cell selection one row down
				int column = cell.getAddress().getColumn() + columns;
				int row = cell.getAddress().getRow() + rows;
				if (column >= 0 && row >= 0) {
					Address address = new Address(column, row);
					Cell cell = uiController.getActiveSpreadsheet().getCell(address);
					uiController.setActiveCell(cell);
				}
			}
		}
	}

	/**
	 * An action for cancelling editing of a cell.
	 * @author Einar Pehrson
	 */
	@SuppressWarnings("serial")
	protected class CancelAction extends AbstractAction {

		/**
		 * Creates an edit cancelling action.
		 */
		public CancelAction() {
			// Configures action
			putValue(NAME, CANCEL_COMMAND);
			putValue(SHORT_DESCRIPTION, CANCEL_COMMAND);
			putValue(ACTION_COMMAND_KEY, CANCEL_COMMAND);
		}

		public void actionPerformed(ActionEvent event) {
			cancelCellEditing();
		}
	}
}