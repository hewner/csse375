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

import csheets.core.formula.Formula;
import csheets.core.formula.compiler.FormulaCompilationException;
import csheets.ext.Extensible;

/**
 * A cell in a spreadsheet.
 * <p>The cell has content, which can be interpreted in
 * different ways: <ul>
 * <li>As a formula - The content should consitute a syntactically correct
 * expression, and begin with an assignment.
 * <li>As a value - If the cell contains a formula, the formula is evaluated to
 * produce a value, provided that the cell does not contain calculation errors
 * (e.g. division by 0). Otherwise, the content is interpreted as the value.
 * </ul>
 * @author Einar Pehrson
 */
public interface Cell extends Comparable<Cell>, Extensible<Cell>, Serializable {

/*
 * LOCATION
 */

	/**
	 * Returns the spreadsheet to which the cell belongs.
	 * @return the spreadsheet to which the cell belongs
	 */
	public Spreadsheet getSpreadsheet();

	/**
	 * Returns the address of the cell.
	 * @return the address of the cell
	 */
	public Address getAddress();

/*
 * VALUE
 */

	/**
	 * Returns the value of the cell.
	 * @return the value of the cell
	 */
	public Value getValue();

/*
 * CONTENT
 */

	/**
	 * Returns the content of the cell, as entered by the user.
	 * @return the content of the cell
	 */
	public String getContent();

	/**
	 * Returns an expression representing the cell's formula.
	 * @return the cell's formula, or null if the cell does not contain one
	 */
	public Formula getFormula();

	/**
	 * Sets the content of the cell and if it starts with the assignment operator
	 * attempts to parse a formula from it.
	 * @throws FormulaCompilationException if an incorrectly formatted formula was entered
	 */
	public void setContent(String content) throws FormulaCompilationException;

	/**
	 * Clears the content of the cell.
	 */
	public void clear();

/*
 * DEPENDENCIES
 */

	/**
	 * Returns the precedents of the cell, i.e. the cells that the
	 * formula in the cell references.
	 * @return a set of the cell's precedents
	 */
	public SortedSet<Cell> getPrecedents();

	/**
	 * Returns the dependents of the cell, i.e. the cells that contain a reference
	 * to the cell in their formula.
	 * @return a set of the cells which depend on the cell
	 */
	public SortedSet<Cell> getDependents() ;

/*
 * CLIPBOARD
 */

	/**
	 * Copies all data from the source cell to this one.
	 * @param source the cell from which data should be copied
	 */
	public void copyFrom(Cell source);

	/**
	 * Moves all data from the source cell to this one.
	 * @param source the cell from which data should be moved
	 */
	public void moveFrom(Cell source);

/*
 * EVENT HANDLING
 */

	/**
	 * Registers the given listener on the cell.
	 * @param listener the listener to be added
	 */
	public void addCellListener(CellListener listener);

	/**
	 * Removes the given listener from the cell.
	 * @param listener the listener to be removed
	 */
	public void removeCellListener(CellListener listener);
	
	/**
	 * Returns the listeners that have been registered on the cell.
	 * @return the listeners that have been registered on the cell
	 */
	public CellListener[] getCellListeners();
}