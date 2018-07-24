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
package csheets.core.formula.lang;

import java.text.ParseException;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import csheets.core.Address;
import csheets.core.Cell;
import csheets.core.Spreadsheet;
import csheets.core.Value;
import csheets.core.formula.Reference;
import csheets.core.formula.util.ExpressionVisitor;
import csheets.core.formula.util.ExpressionVisitorException;

/**
 * A reference to a cell in a spreadsheet.
 * @author Einar Pehrson
 */
public class CellReference implements Reference {

	/** The unique version identifier used for serialization */
	private static final long serialVersionUID = -6600693551615086696L;

	/**
	 * The regular expression pattern used to match cell references:
	 * (\\$??)([a-zA-Z]+)(\\$??)(\\d+)$")
	 */
	private static final Pattern PATTERN = Pattern.compile(
		"(\\$??)([a-zA-Z]+)(\\$??)(\\d+)$");

	/** The string used to match the use of absolute references */
	private static final String ABSOLUTE_OPERATOR = "$";

	/** The cell to which the reference points */
	private Cell cell;

	/** If the column is denoted with an absolute reference */
	private boolean columnAbsolute;

	/** If the row is denoted with an absolute reference */
	private boolean rowAbsolute;

	/**
	 * Creates a new cell reference to the given address.
	 * By default, relative addressing is used.
	 * @param cell the cell to which the reference points
	 */
	public CellReference(Cell cell) {
		this(cell, false, false);
	}

	/**
	 * Creates a new cell reference to the given address, using the given
	 * reference mode.
	 * @param cell the cell to which the reference points
	 * @param columnAbsolute if the column is denoted with an absolute reference
	 * @param rowAbsolute if the column is denoted with an absolute reference
	 */
	public CellReference(Cell cell, boolean columnAbsolute, boolean rowAbsolute) {
		this.cell = cell;
		this.columnAbsolute = columnAbsolute;
		this.rowAbsolute = rowAbsolute;
	}

	/**
	 * Creates a new cell reference from a string matching the (@link #PATTERN).
	 * @param spreadsheet the spreadsheet of the cell
	 * @param reference a string representation of the reference
	 * @throws ParseException if the string did not match the pattern
	 */
	public CellReference(Spreadsheet spreadsheet, String reference) throws ParseException {
		// Matches the expression
		Matcher matcher = PATTERN.matcher(reference);
		if (matcher.matches()) {

			// Parses row and column indices
			int row = Integer.parseInt(matcher.group(4)) - 1;
			int column = -1;
			String columnStr = matcher.group(2).toUpperCase();
			for (int i = columnStr.length() - 1; i >= 0; i--)
				column += (columnStr.charAt(i) - Address.LOWEST_CHAR + 1)
					* Math.pow(Address.HIGHEST_CHAR - Address.LOWEST_CHAR + 1,
					columnStr.length() - (i + 1));

			// Stores members
			this.cell = spreadsheet.getCell(new Address(column, row));
			this.columnAbsolute = matcher.group(1).equals("$");
			this.rowAbsolute = matcher.group(3).equals("$");
		} else
			throw new ParseException(reference, 0);
	}

	public Value evaluate() {
		return cell.getValue();
	}

	public Object accept(ExpressionVisitor visitor) throws ExpressionVisitorException {
		return visitor.visitReference(this);
	}

	/**
	 * Returns the cell to which the reference points.
	 * @return the cell to which the reference points
	 */
	public Cell getCell() {
		return cell;
	}

	public SortedSet<Cell> getCells() {
		SortedSet<Cell> cells = new TreeSet<Cell>();
		cells.add(cell);
		return cells;
	}

	/**
	 * Returns whether the column is denoted with an absolute reference.
	 * @return true if the column is denoted with an absolute reference.
	 */
	public boolean isColumnAbsolute() {
		return columnAbsolute;
	}

	/**
	 * Returns whether the row is denoted with an absolute reference.
	 * @return true if the row is denoted with an absolute reference.
	 */
	public boolean isRowAbsolute() {
		return rowAbsolute;
	}

	/**
	 * Compares the cell reference with the given cell reference for order.
	 * @param reference the reference to be compared
	 * @return a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object.
	 */
	public int compareTo(Reference reference) {
		Cell otherCell = reference.getCells().first();
		int firstDiff = cell.compareTo(otherCell);
		if (firstDiff != 0)
			return firstDiff;
		else {
			if (reference instanceof CellReference) {
				// Handle reference modes?
				return -1;
			} else
				return -1;
		}
	}

	/**
	 * Returns a string representation of the address of the cell reference 
	 * on the form "B22", composed of the letter of the column and number of
	 * the row that intersect to form the address.	 
	 * @return a string representation of the address of the cell reference
	 */
	public String toString() {
		// Converts column
		String columnStr = "";
		for (int tempColumn = cell.getAddress().getColumn();
				tempColumn >= 0; tempColumn = tempColumn
					/ (Address.HIGHEST_CHAR - Address.LOWEST_CHAR + 1) - 1)
			columnStr = ((char)((char)(tempColumn % (Address.HIGHEST_CHAR
				- Address.LOWEST_CHAR + 1)) + Address.LOWEST_CHAR)) + columnStr;
		if (columnAbsolute)
			columnStr = ABSOLUTE_OPERATOR + columnStr;

		// Converts row
		String rowStr = (rowAbsolute ? ABSOLUTE_OPERATOR : "")
			+ (cell.getAddress().getRow() + 1);
		return columnStr + rowStr;
	}
}