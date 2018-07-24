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

import csheets.core.Cell;
import csheets.core.Spreadsheet;
import csheets.core.Value;
import csheets.core.formula.BinaryOperator;
import csheets.core.formula.Expression;
import csheets.core.formula.Reference;

/**
 * A reference to a range of cells in a spreadsheet.
 * @author Einar Pehrson
 */
public class RangeReference implements BinaryOperator {

	/** The unique version identifier used for serialization */
	private static final long serialVersionUID = 8527083457981256682L;

	/**
	 * Creates a new range reference operator.
	 */
	public RangeReference() {}

	public Value applyTo(Expression leftOperand, Expression rightOperand) {
		// Casts operands
		if (!(leftOperand instanceof CellReference && rightOperand instanceof CellReference))
			return new Value(new IllegalArgumentException("#OPERAND!"));
		CellReference ref1 = (CellReference)leftOperand;
		CellReference ref2 = (CellReference)rightOperand;

		// Fetches the cells
		Cell[][] cells;
		try {
			cells = getCells(ref1, ref2);
		} catch (IllegalArgumentException e) {
			return new Value(e);
		}

		// Fetches the values
		Value[][] values = new Value[cells.length][cells[0].length];
		for (int row = 0; row < cells.length; row++)
			for (int column = 0; column < cells[row].length; column++)
				values[row][column] = cells[row][column].getValue();
		return new Value(values);
	}

	/**
	 * Returns the range of cells formed by the two cell references.
	 * @param reference1 the first reference
	 * @param reference2 the other reference
	 * @return an array of the cells that constitute the range
	 */
	public Cell[][] getCells(Reference reference1, Reference reference2) {
		// Casts operands
		if (!(reference1 instanceof CellReference && reference2 instanceof CellReference))
			throw new IllegalArgumentException("#OPERAND!");
		CellReference ref1 = (CellReference)reference1;
		CellReference ref2 = (CellReference)reference2;

		// Checks that the references point to cells in the same spreadsheet
		Spreadsheet spreadsheet = ref1.getCell().getSpreadsheet();
		if (spreadsheet != ref2.getCell().getSpreadsheet())
			throw new IllegalArgumentException("#3DREF!");

		// Fetches coordinates
		int column1 = ref1.getCell().getAddress().getColumn();
		int column2 = ref2.getCell().getAddress().getColumn();
		int row1 = ref1.getCell().getAddress().getRow();
		int row2 = ref2.getCell().getAddress().getRow();
		int startColumn = column1 <= column2 ? column1 : column2;
		int endColumn = column1 <= column2 ? column2 : column1;
		int startRow = row1 <= row2 ? row1 : row2;
		int endRow = row1 <= row2 ? row2 : row1;

		// Builds the matrix
		Cell[][] matrix = new Cell
			[endRow - startRow + 1]
			[endColumn - startColumn + 1];
		for (int row = 0; row < matrix.length; row++)
			for (int column = 0; column < matrix[row].length; column++)
				matrix[row][column] = spreadsheet.getCell
					(column + startColumn, row + startRow);
		return matrix;
	}

	public String getIdentifier() {
		return ":";
	}

	public Value.Type getOperandValueType() {
		return Value.Type.NUMERIC;
	}

	public String toString() {
		return getIdentifier();
	}
}