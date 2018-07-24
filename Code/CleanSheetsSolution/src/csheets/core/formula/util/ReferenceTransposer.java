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
package csheets.core.formula.util;

import csheets.core.Address;
import csheets.core.formula.Expression;
import csheets.core.formula.Reference;
import csheets.core.formula.lang.CellReference;

/**
 * An expression visitor that transposes the references in an expression.
 * The visitor is used for copying and moving formulas between cells.
 * @author Einar Pehrson
 */
public class ReferenceTransposer extends ExpressionBuilder {

	/** The number of columns to transpose the formula */
	private int columns;

	/** The number of rows to transpose the formula */
	private int rows;

	/**
	 * Creates a new reference fetcher.
	 * @param columns the number of columns to transpose formulae
	 * @param rows the number of rows to transpose formulae
	 */
	public ReferenceTransposer(int columns, int rows) {
		this.columns = columns;
		this.rows = rows;
	}

	/**
	 * Returns a transposed copy of the given expression.
	 * @param expression the expression to rebuild
	 * @return the transposed expression
	 */
	public Expression getExpression(Expression expression) {
		return (Expression)expression.accept(this);
	}

	/**
	 * Transposes the reference.
	 * @param reference the reference to visit
	 */
	public Expression visitReference(Reference reference) {
		if (reference instanceof CellReference) {
			// Fetches reference and address
			CellReference cellRef = (CellReference)reference;
			Address address = cellRef.getCell().getAddress();

			// Transposes column index
			int newColumn = address.getColumn();
			if (!cellRef.isColumnAbsolute())
				newColumn += columns;

			// Transposes row index
			int newRow = address.getRow();
			if (!cellRef.isRowAbsolute())
				newRow += rows;

			// Returns new reference
			return new CellReference(
				cellRef.getCell().getSpreadsheet().getCell(
					new Address(newColumn, newRow)),
				cellRef.isColumnAbsolute(),
				cellRef.isRowAbsolute()
			);
		} else
			return super.visitReference(reference);
	}
}