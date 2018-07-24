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

import java.util.SortedSet;
import java.util.TreeSet;

import csheets.core.Address;
import csheets.core.Cell;
import csheets.core.CellListener;
import csheets.core.Spreadsheet;
import csheets.core.Value;
import csheets.core.formula.Formula;
import csheets.core.formula.compiler.FormulaCompilationException;

/**
 * A base class for extensions of cells in a spreadsheet that uses delegation
 * to provide cell data.
 * @author Einar Pehrson
 */
public abstract class CellExtension implements Cell, CellListener {

	/** The delegate of the extension */
	private Cell delegate;

	/** The name of the extension to which the cell extension belongs */
	private String name;

	/**
	 * Creates a new cell extension.
	 * @param delegate the delegate of the extension
	 * @param name the name of the extension to which the cell extension belongs
	 */
	public CellExtension(Cell delegate, String name) {
		this.delegate = delegate;
		this.name = name;
		delegate.addCellListener(this);
	}

	/**
	 * Returns the extension's delegate.
	 * @return the extension's delegate, i.e. the cell to which it belongs
	 */
	public final Cell getDelegate() {
		return delegate;
	}

	/**
	 * Returns the name of the extension to which the cell extension belongs.
	 * @return the name of the extension to which the cell extension belongs
	 */
	public final String getName() {
		return name;
	}

	public final Spreadsheet getSpreadsheet() {
		Spreadsheet spreadsheet = delegate.getSpreadsheet();
		Spreadsheet extension = spreadsheet.getExtension(name);
		if (extension != null)
			return extension;
		else
			return spreadsheet;
	}

	public final Address getAddress() {
		return delegate.getAddress();
	}

	public final Value getValue() {
		return delegate.getValue();
	}

	public final String getContent() {
		return delegate.getContent();
	}

	public final Formula getFormula() {
		return delegate.getFormula();
	}

	public final void setContent(String content) throws FormulaCompilationException {
		// Disallow?
		delegate.setContent(content);
	}

	public void clear() {
		delegate.clear();
	}

	public final SortedSet<Cell> getPrecedents() {
		SortedSet<Cell> cellExts = new TreeSet<Cell>();
		for (Cell cell : delegate.getPrecedents())
			cellExts.add(cell.getExtension(getName()));
		return cellExts;
	}

	public final SortedSet<Cell> getDependents()  {
		SortedSet<Cell> cellExts = new TreeSet<Cell>();
		for (Cell cell : delegate.getDependents())
			cellExts.add(cell.getExtension(getName()));
		return cellExts;
	}

	public final void copyFrom(Cell source) {
		delegate.copyFrom(source);
	}

	public final void moveFrom(Cell source) {
		delegate.moveFrom(source);
	}

	public final void addCellListener(CellListener listener) {
		delegate.addCellListener(listener);
	}

	public final void removeCellListener(CellListener listener) {
		delegate.removeCellListener(listener);
	}

	public final CellListener[] getCellListeners() {
		return delegate.getCellListeners();
	}

	public final Cell getExtension(String name) {
		return delegate.getExtension(name);
	}

	public final int compareTo(Cell cell) {
		return delegate.compareTo(cell);
	}

	public final String toString() {
		return delegate.toString();
	}

	public void valueChanged(Cell cell) {}

	public void contentChanged(Cell cell) {}

	public void dependentsChanged(Cell cell) {}

	public void cellCleared(Cell cell) {}

	public void cellCopied(Cell cell, Cell source) {}
}