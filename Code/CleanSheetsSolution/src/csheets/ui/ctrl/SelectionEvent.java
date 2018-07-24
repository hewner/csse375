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

import java.util.EventObject;

import csheets.core.Cell;
import csheets.core.Spreadsheet;
import csheets.core.Workbook;

/**
 * A selection application event is used to notify interested parties that
 * a new active workbook, spreadsheet and/or cell is selected.
 * @author Einar Pehrson
 */
@SuppressWarnings("serial")
public class SelectionEvent extends EventObject {

	/** The new active workbook */
	private Workbook workbook;

	/** The new active spreadsheet. */
	private Spreadsheet spreadsheet;

	/** The new active cell. */
	private Cell cell;

	/** The previous active workbook (or the same as the new one if it didn't change) */
	private Workbook prevWorkbook;

	/** The previous active spreadsheet (or the same as the new one if it didn't change) */
	private Spreadsheet prevSpreadsheet;

	/** The previous active cell */
	private Cell prevCell;

	/**
	 * Creates a new selection event.
	 * @param source the source of the event
	 * @param workbook the active workbook
	 * @param spreadsheet the active spreadsheet
	 * @param cell the active cell
	 * @param prevWorkbook the previous active workbook (or the same as the new one if it didn't change)
	 * @param prevSpreadsheet the previous active spreadsheet (or the same as the new one if it didn't change)
	 * @param prevCell the previous active cell
	 */
	public SelectionEvent(Object source,
			Workbook workbook, Spreadsheet spreadsheet, Cell cell,
			Workbook prevWorkbook, Spreadsheet prevSpreadsheet, Cell prevCell) {
		super(source);

		// Stores members
		this.workbook = workbook;
		this.spreadsheet = spreadsheet;
		this.cell = cell;
		this.prevWorkbook = prevWorkbook;
		this.prevSpreadsheet = prevSpreadsheet;
		this.prevCell = prevCell;
	}

	/**
	 * Returns the active workbook.
	 * @return the active workbook
	 */
	public Workbook getWorkbook() {
		return workbook;
	}

	/**
	 * Returns the active spreadsheet.
	 * @return the active spreadsheet
	 */
	public Spreadsheet getSpreadsheet() {
		return spreadsheet;
	}

	/**
	 * Returns the active cell.
	 * @return the active cell
	 */
	public Cell getCell() {
		return cell;
	}

	/**
	 * Returns the previous active workbook.
	 * @return the previous active workbook (or the same as the new one if it didn't change)
	 */
	public Workbook getPreviousWorkbook() {
		return prevWorkbook;
	}

	/**
	 * Returns the previous active spreadsheet.
	 * @return the previous active spreadsheet (or the same as the new one if it didn't change)
	 */
	public Spreadsheet getPreviousSpreadsheet() {
		return prevSpreadsheet;
	}

	/**
	 * Returns the previous active cell.
	 * @return the previous active cell
	 */
	public Cell getPreviousCell() {
		return prevCell;
	}

	/**
	 * Returns whether the event was caused by the active workbook being changed
	 * @return true if the active workbook and the previous active workbook are not the same
	 */
	public boolean isWorkbookChanged() {
		return workbook != prevWorkbook;
	}

	/**
	 * Returns whether the event was caused by the active spreadsheet being changed
	 * @return true if the active spreadsheet and the previous active spreadsheet are not the same
	 */
	public boolean isSpreadsheetChanged() {
		return spreadsheet != prevSpreadsheet;
	}

	/**
	 * Returns whether the event was caused by the active cell being changed
	 * @return true if the active cell and the previous active cell are not the same
	 */
	public boolean isCellChanged() {
		return cell != prevCell;
	}
}