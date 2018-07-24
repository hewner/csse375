/*
 * Copyright (c) 2005 Peter Palotas, Fredrik Johansson, Einar Pehrson,
 * Sebastian Kekkonen, Lars Magnus Lång, Malin Johansson and Sofia Nilsson
 *
 * This file is part of
 * CleanSheets Extension for Assertions
 *
 * CleanSheets Extension for Assertions is free software; you can
 * redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either version 2 of
 * the License, or (at your option) any later version.
 *
 * CleanSheets Extension for Assertions is distributed in the hope that
 * it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with CleanSheets Extension for Assertions; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA  02111-1307  USA
 */
package csheets.ext.assertion;

import csheets.core.formula.Formula;

/** Class representing a System Generated Assertion.
	@author Peter Palotas */
public class SGAssertion extends Assertion {

	/** The unique version identifier used for serialization */
	private static final long serialVersionUID = -5713386003093260622L;

	/** Constructs a new System Generated Assertion (SGA) for the specified
	    Cell if possible.
	    @param cell the <code>Cell</code> on which to create an SGA.
	    @throws AssertionArithmeticException if the SGA could not be generated. */
	public SGAssertion(AssertableCell cell) throws AssertionArithmeticException, MathException {
		Formula formula = cell.getFormula();
		if (formula == null) {
			throw new AssertionArithmeticException("Cell " + cell.getAddress() + " does not contain a formula");
		}

		intervals = new AssertionArithmeticVisitor().getResult(formula);
	}
}