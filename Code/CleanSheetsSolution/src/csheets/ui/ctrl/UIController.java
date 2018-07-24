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
package csheets.ui.ctrl;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Stack;

import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;

import csheets.CleanSheets;
import csheets.SpreadsheetAppEvent;
import csheets.SpreadsheetAppListener;
import csheets.core.Cell;
import csheets.core.Spreadsheet;
import csheets.core.Workbook;
import csheets.ext.Extension;
import csheets.ext.ExtensionManager;
import csheets.ui.ext.UIExtension;
import csheets.ui.sheet.CellTransferHandler;

/**
 * A controller for managing the current selection, i.e. the active workbook,
 * spreadsheet and cell, as well as for keeping track of modifications to
 * workbooks and of user interface extensions.
 * @author Einar Pehrson
 */
public class UIController implements SpreadsheetAppListener {

	/** The active workbook */
	private Workbook activeWorkbook;

	/** The active spreadsheet */
	private Spreadsheet activeSpreadsheet;

	/** The active cell */
	private Cell activeCell;

	/** The workbooks that have been selected, in order */
	private Stack<Workbook> workbooks = new Stack<Workbook>();

	/** The map that registers whether workbooks have changes */
	private Map<Workbook, Boolean> changeLog = new HashMap<Workbook, Boolean>();

	/** The CleanSheets application */
	private CleanSheets app;

	/** The transfer haandler used to transfer ranges of cells */
	private TransferHandler transferHandler = new CellTransferHandler();

	/** The user interface extensions that have been loaded */
	private UIExtension[] extensions;

	/** The selection listeners registered to receive events */
	private List<SelectionListener> selListeners = new ArrayList<SelectionListener>();

	/** The edit listeners registered to receive events */
	private List<EditListener> editListeners = new ArrayList<EditListener>();

	// private Map<Workbook, Spreadsheet> activeSpreadsheets;
	// private Map<Spreadsheet, Cell> activeCells;

	/**
	 * Creates a new user interface controller.
	 * @param app the CleanSheets application
	 */
	public UIController(CleanSheets app) {
		// Stores members
		this.app = app;
		app.addSpreadsheetAppListener(this);

		// Fetches extensions
		List<UIExtension> uiExtensions = new LinkedList<UIExtension>();
		for (Extension extension : ExtensionManager.getInstance().getExtensions()) {
			UIExtension uiExtension = extension.getUIExtension(this);
			if (uiExtension != null)
				uiExtensions.add(uiExtension);
		}
		this.extensions =
			uiExtensions.toArray(new UIExtension[uiExtensions.size()]);
	}

/*
 * SELECTION
 */

	/**
	 * Returns the active workbook.
	 * @return the active workbook
	 */
	public Workbook getActiveWorkbook() {
		return activeWorkbook;
	}

	/**
	 * Sets the given workbook of the application.
	 * @param workbook the workbook to use
	 */
	public void setActiveWorkbook(Workbook workbook) {
		if (activeWorkbook == null || activeWorkbook != workbook) {
			Workbook prevWorkbook = activeWorkbook;
			Spreadsheet prevSpreadsheet = activeSpreadsheet;
			Cell prevCell = activeCell;
			activeWorkbook = workbook;
			activeSpreadsheet = null;
			activeCell = null;
			if (activeWorkbook != null) {
				workbooks.remove(activeWorkbook);
				workbooks.push(activeWorkbook);
			}
			fireSelectionChanged(new SelectionEvent(this,
				activeWorkbook, activeSpreadsheet, activeCell,
				prevWorkbook, prevSpreadsheet, prevCell));
		}
	}

	/**
	 * Returns the active spreadsheet.
	 * @return the active spreadsheet
	 */
	public Spreadsheet getActiveSpreadsheet() {
		return activeSpreadsheet;
	}

	/**
	 * Sets the active spreadsheet of the application, and thereby also the
	 * active workbook.
	 * @param spreadsheet the spreadsheet to use
	 */
	public void setActiveSpreadsheet(Spreadsheet spreadsheet) {
		if (activeSpreadsheet == null || activeSpreadsheet != spreadsheet) {
			Workbook prevWorkbook = activeWorkbook;
			Spreadsheet prevSpreadsheet = activeSpreadsheet;
			Cell prevCell = activeCell;
			activeSpreadsheet = spreadsheet;
			activeWorkbook = activeSpreadsheet.getWorkbook();
			if (activeWorkbook != null) {
				workbooks.remove(activeWorkbook);
				workbooks.push(activeWorkbook);
			}
			fireSelectionChanged(new SelectionEvent(this,
				activeWorkbook, activeSpreadsheet, activeCell,
				prevWorkbook, prevSpreadsheet, prevCell));
		}
	}

	/**
	 * Returns the active cell of the active workbook's active spreadsheet.
	 * @return the active cell
	 */
	public Cell getActiveCell() {
		return activeCell;
	}

	/**
	 * Sets the active cell of the application, and thereby also the active
	 * spreadsheet and workbook.
	 * @param cell the cell to use
	 */
	public void setActiveCell(Cell cell) {
		if (activeCell == null || activeCell != cell) {
			Workbook prevWorkbook = activeWorkbook;
			Spreadsheet prevSpreadsheet = activeSpreadsheet;
			Cell prevCell = activeCell;
			activeCell = cell;
			activeSpreadsheet = cell.getSpreadsheet();
			activeWorkbook = activeSpreadsheet.getWorkbook();
			if (activeWorkbook != null) {
				workbooks.remove(activeWorkbook);
				workbooks.push(activeWorkbook);
			}
			fireSelectionChanged(new SelectionEvent(this,
				activeWorkbook, activeSpreadsheet, activeCell,
				prevWorkbook, prevSpreadsheet, prevCell));
		}
	}

/*
 * EDITING
 */

	/**
	 * Returns whether the active workbook has been modified.
	 * @return whether the active workbook has been modified
	 */
	public boolean isActiveWorkbookModified() {
		if (activeWorkbook != null) {
			Boolean modified = changeLog.get(activeWorkbook);
			return modified != null && modified == true;
		} else
			return false;
	}

	/**
	 * Returns whether the given workbook has been modified.
	 * @return whether the given workbook has been modified
	 */
	public boolean isWorkbookModified(Workbook workbook) {
		Boolean modified = changeLog.get(workbook);
		return modified != null && modified == true;
	}

	/**
	 * Specifies whether the given workbook has been modified.
	 * @param workbook the relevant workbook
	 */
	public void setWorkbookModified(Workbook workbook) {
		changeLog.put(workbook, true);
		fireWorkbookModified(workbook);
	}

	/**
	 * Returns the transfer haandler used to transfer ranges of cells.
	 * @return the transfer haandler used to transfer ranges of cells
	 */
	public TransferHandler getCellTransferHandler() {
		return transferHandler;
	}

/*
 * PROPERTIES
 */

	/**
	 * Returns the current user properties.
	 * @return the current user properties
	 */
	public Properties getUserProperties() {
		return app.getUserProperties();
	}

/*
 * EXTENSIONS
 */

	/**
	 * Returns the user interface extensions that have been loaded.
	 * @return the user interface extensions that have been loaded
	 */
	public UIExtension[] getExtensions() {
		return extensions;
	}

/*
 * EVENT FIRING & LISTENING
 */

	public void workbookCreated(SpreadsheetAppEvent event) {
		Workbook workbook = event.getWorkbook();
		changeLog.put(workbook, false);
		if (workbook.getSpreadsheetCount() > 0)
			setActiveCell(workbook.getSpreadsheet(0).getCell(0, 0));
		else
			setActiveWorkbook(workbook);
	}

	public void workbookLoaded(SpreadsheetAppEvent event) {
		workbookCreated(event);
	}

	public void workbookUnloaded(SpreadsheetAppEvent event) {
		changeLog.remove(event.getWorkbook());
		workbooks.remove(event.getWorkbook());
		Workbook activeWorkbook = null;
		try {
			activeWorkbook = workbooks.peek();
		} catch (EmptyStackException e) {}
		setActiveWorkbook(activeWorkbook);
	}

	public void workbookSaved(SpreadsheetAppEvent event) {
		changeLog.put(event.getWorkbook(), false);
	}

	/**
	 * Registers the given listener on the user interface controller.
	 * @param listener the listener to be added
	 */
	public void addSelectionListener(SelectionListener listener) {
		selListeners.add(listener);
	}

	/**
	 * Removes the given listener from the user interface controller.
	 * @param listener the listener to be removed
	 */
	public void removeSelectionListener(SelectionListener listener) {
		selListeners.remove(listener);
	}

	/**
	 * Registers the given listener on the user interface controller.
	 * @param listener the listener to be added
	 */
	public void addEditListener(EditListener listener) {
		editListeners.add(listener);
	}

	/**
	 * Removes the given listener from the user interface controller.
	 * @param listener the listener to be removed
	 */
	public void removeEditListener(EditListener listener) {
		editListeners.remove(listener);
	}

	/**
	 * Notifies all registered listeners that the selection changed.
	 * @param event the event to fire
	 */
	private void fireSelectionChanged(SelectionEvent event) {
		SwingUtilities.invokeLater(new EventDispatcher(event,
			selListeners.toArray(new SelectionListener[selListeners.size()])));
	}

	/**
	 * Notifies all registered listeners that the workbook was modified.
	 * @param workbook the workbook that was modified
	 */
	private void fireWorkbookModified(Workbook workbook) {
		EditEvent event = new EditEvent(this, workbook);
		for (EditListener listener : editListeners.toArray(
				new EditListener[editListeners.size()]))
			listener.workbookModified(event);
	}

	/**
	 * A utility for dispatching events on the AWT event dispatching thread.
	 * @author Einar Pehrson
	 */
	public static class EventDispatcher implements Runnable {

		/** The event to fire */
		private SelectionEvent event;

		/** The listeners to which the event should be dispatched */
		private SelectionListener[] listeners;

		/**
		 * Creates a new event dispatcher.
		 * @param event the event to fire
		 * @param listeners the listeners to which the event should be dispatched
		 */
		public EventDispatcher(SelectionEvent event, SelectionListener[] listeners) {
			this.event = event;
			this.listeners = listeners;
		}

		/**
		 * Dispatches the event.
		 */
		public void run() {
			for (SelectionListener listener : listeners)
				listener.selectionChanged(event);
		}
	}
}