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

import csheets.core.Cell;
import csheets.core.formula.Formula;
import csheets.core.formula.Reference;

/**
 * An expression visitor that looks for circular references in a formula, i.e.
 * a reference back to the cell in the formula of a cell that precedes it.
 * @author Einar Pehrson
 */
public class CircularReferenceFinder extends AbstractExpressionVisitor {

	/** The cell to search for circular references */
	private Formula formula;

	/**
	 * Creates a new circular reference finder.
	 */
	public CircularReferenceFinder() {}

	/**
	 * Checks if the given formula has any circular references.
	 * @throws CircularReferenceException if the formula contains any circular references
	 */
	public void check(Formula formula) throws CircularReferenceException {
		this.formula = formula;
		formula.accept(this);
	}

	/*
	 * Returns whether the given formula has any circular references.
	 * @param formula the formula to check for circularities
	 * @return true if the given formula has any circular references
	 */
/*	public boolean hasCircularReference(Formula formula) {} */

	/**
	 * Checks if the given reference causes a circular reference.
	 * @param reference the reference to visit
	 * @throws CircularReferenceException if the given reference causes a circular reference
	 */
	public Object visitReference(Reference reference) throws CircularReferenceException, ExpressionVisitorException {
		for (Cell precedent : reference.getCells()) {
			// Checks for circularity
			if (precedent.equals(formula.getCell()))
				throw new CircularReferenceException(formula);

			// Looks further
			Formula precedentFormula = precedent.getFormula();
			if (precedentFormula != null)
				precedentFormula.accept(this);
		}
		return reference;
	}
}