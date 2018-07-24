/*
 * Copyright (c) 2005 Jens Schou, Staffan Gustafsson, Björn Lanneskog, 
 * Einar Pehrson and Sebastian Kekkonen
 *
 * This file is part of
 * CleanSheets Extension for Test Cases
 *
 * CleanSheets Extension for Test Cases is free software; you can
 * redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either version 2 of
 * the License, or (at your option) any later version.
 *
 * CleanSheets Extension for Test Cases is distributed in the hope that
 * it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with CleanSheets Extension for Test Cases; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA  02111-1307  USA
 */
package csheets.ext.test;
	
import java.util.Set;

import csheets.core.Cell;
import csheets.core.Value;
import csheets.core.formula.Expression;
import csheets.core.formula.Literal;
import csheets.core.formula.Reference;
import csheets.core.formula.lang.CellReference;
import csheets.core.formula.lang.RangeReference;
import csheets.core.formula.lang.ReferenceOperation;
import csheets.core.formula.util.ExpressionBuilder;
import csheets.core.formula.util.ExpressionVisitorException;

/**
 * An expression visitor that creates the expression for a test case, by
 * replacing references with test case parameters.
 * @author Einar Pehrson
 */
public class TestCaseBuilder extends ExpressionBuilder {

	/** The parameters of which the test case consists */
	private Set<TestCaseParam> params;

	/**
	 * Creates a new reference replacer, that converts the given expression
	 * to a test case using the given set of test case parameters
	 * @param params the parameters of which the test case consists
	 */
	public TestCaseBuilder(Set<TestCaseParam> params) {
		this.params = params;
	}

	/**
	 * Replaces the reference with the appropriate test case parameter(s).
	 * @param reference the reference to visit
	 */
	public Expression visitReference(Reference reference) {
		if (reference instanceof CellReference) {
			// Converts cell reference to literal
			CellReference cellRef = ((CellReference)reference);
			for (TestCaseParam param : params)
				if (cellRef.getCell().equals(param.getCell().getDelegate()))
					return new Literal(param.getValue());
			throw new ExpressionVisitorException(); // MissingTestCaseParamException
		} else {
			ReferenceOperation refOp = (ReferenceOperation)reference;
			if (refOp.getOperator() instanceof RangeReference) {
				// Converts range reference to matrix literal
				RangeReference op = (RangeReference)refOp.getOperator();
				Cell[][] cells = op.getCells(refOp.getLeftOperand(), refOp.getRightOperand());
				Value[][] values = new Value[cells.length][cells[0].length];
				for (int row = 0; row < cells.length; row++)
					for (int column = 0; column < cells[row].length; column++) {
						for (TestCaseParam param : params)
							if (cells[row][column].equals(param.getCell().getDelegate()))
								values[row][column] = param.getValue();
						if (values[row][column] == null)
							throw new ExpressionVisitorException(); 
					}
				return new Literal(new Value(values));
			} else
				return super.visitReference(reference);
		}
	}
}