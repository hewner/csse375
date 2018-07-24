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
package csheets.ext;

import java.util.Iterator;
import java.util.SortedSet;

import csheets.core.Address;
import csheets.core.Cell;
import csheets.core.CellListener;
import csheets.core.Spreadsheet;
import csheets.core.Workbook;

/**
 * A base class for extensions of spreadsheets that uses delegation
 * to provide cell data.
 * @author Einar Pehrson
 */
public abstract class SpreadsheetExtension implements Spreadsheet {

	/** The delegate of the extension */
	private Spreadsheet delegate;

	/** The name of the extension to which the spreadsheet extension belongs */
	private String name;

	/**
	 * Creates a new spreadsheet extension.
	 * @param delegate the delegate of the extension
	 * @param name the name of the extension to which the spreadsheet extension belongs
	 */
	public SpreadsheetExtension(Spreadsheet delegate, String name) {
		this.delegate = delegate;
		this.name = name;
	}

	/**
	 * Returns the name of the extension to which the spreadsheet extension belongs.
	 * @return the name of the extension to which the spreadsheet extension belongs
	 */
	public final String getName() {
		return name;
	}

	public final String getTitle() {
		return delegate.getTitle();
	}

	public final void setTitle(String title) {
		delegate.setTitle(title);
	}

	public final Workbook getWorkbook() {
		return delegate.getWorkbook();
	}

	public final int getColumnCount() {
		return delegate.getColumnCount();
	}

	public final int getRowCount() {
		return delegate.getRowCount();
	}

	public final Cell getCell(Address address) {
		Cell cell = delegate.getCell(address);
		Cell extension = cell.getExtension(name);
		if (extension != null)
			return extension;
		else
			return cell;
	}

	public final Cell getCell(int column, int row) {
		return getCell(new Address(column, row));
	}

	public final SortedSet<Cell> getCells(Address address1, Address address2) {
		return delegate.getCells(address1, address2);
	}

	public final Cell[] getColumn(int index) {
		return delegate.getColumn(index);
	}

	public final Cell[] getRow(int index) {
		return delegate.getRow(index);
	}

	public final Iterator<Cell> iterator() {
		return delegate.iterator();
	}

	public void addCellListener(CellListener listener) {
		delegate.addCellListener(listener);
	}

	public void removeCellListener(CellListener listener) {
		delegate.removeCellListener(listener);
	}

	public CellListener[] getCellListeners() {
		return delegate.getCellListeners();
	}

	public final Spreadsheet getExtension(String name) {
		return delegate.getExtension(name);
	}
}