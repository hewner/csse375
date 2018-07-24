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
package csheets.core;

import java.io.Serializable;
import java.util.SortedSet;

import csheets.ext.Extensible;

/**
 * A spreadsheet which provides cell data and dependencies.
 * @author Einar Pehrson
 */
public interface Spreadsheet extends Iterable<Cell>, Extensible<Spreadsheet>,
		Serializable {

/*
 * LOCATION
 */

	/**
	 * Returns the workbook to which the spreadsheet belongs.
	 * @return the workbook to which the spreadsheet belongs
	 */
	public Workbook getWorkbook();

	/**
	 * Returns the title of the spreadsheet.
	 * @return the title of the spreadsheet.
	 */
	public String getTitle();

	/**
	 * Sets the title of the spreadsheet.
	 * @param title the title of the spreadsheet.
	 */
	public void setTitle(String title);

/*
 * DIMENSIONS
 */

	/**
	 * Returns the number of columns in the spreadsheet.
	 * @return the number of columns in the spreadsheet.
	 */
	public int getColumnCount();

	/**
	 * Returns the number of rows in the spreadsheet.
	 * @return the number of rows in the spreadsheet.
	 */
	public int getRowCount();

/*
 * CELLS
 */

	/**
	 * Returns the cell at the given address.
	 * @param address the address of the cell
	 * @return the cell at the given address
	 */
	public Cell getCell(Address address);

	/**
	 * Returns the cell at the given column and row in the spreadsheet.
	 * @param column the column index of the cell's location
	 * @param row the row index of the cell's location
	 * @return the cell at the given column and row in the spreadsheet
	 */
	public Cell getCell(int column, int row);

	/**
	 * Returns the cells in the range between the given addresses.
	 * @param address1 the address of the cell in one end of the range
	 * @param address2 the address of the cell in the other end of the range
	 * @return a sorted set of the cells in the range
	 */
	public SortedSet<Cell> getCells(Address address1, Address address2);

	/**
	 * Returns the cells in the given column.
	 * @param index the index of the column
	 * @return an array of the cells in the column
	 */
	public Cell[] getColumn(int index);

	/**
	 * Returns the cells in the given row.
	 * @param index the index of the row
	 * @return an array of the cells in the row
	 */
	public Cell[] getRow(int index);

/*
 * EVENT HANDLING
 */

	/**
	 * Registers the given listener to receive events from all cells in the
	 * spreadsheet.
	 * @param listener the listener to be added
	 */
	public void addCellListener(CellListener listener);

	/**
	 * Deregisters the given listener from receiving events from all cells in
	 * the spreadsheet.
	 * @param listener the listener to be removed
	 */
	public void removeCellListener(CellListener listener);
	
	/**
	 * Returns the cell listeners that have been registered on the spreadsheet.
	 * @return the cell listeners that have been registered on the spreadsheet
	 */
	public CellListener[] getCellListeners();
}