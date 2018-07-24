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

import csheets.core.formula.Formula;

/**
 * An exception that is thrown if a formula contains a reference (directly or
 * indirectly) to the cell in which it is contained.
 * @author Einar Pehrson
 */
public class CircularReferenceException extends ExpressionVisitorException {

	/** The serialVersionUID of the CircularReferenceException.java */
	private static final long serialVersionUID = 4204972508404909370L;

	/** The formula in which the circularity exists */
	private Formula formula;

	/**
	 * Creates a new circular reference exception.
	 * @param formula the formula that caused the exception
	 */
	public CircularReferenceException(Formula formula) {
		super("A circular reference was encountered in the formula " + formula + ".");
		this.formula = formula;
	}

	/**
	 * Returns the formula in which the circularity exists.
	 * @return the formula in which the circularity exists
	 */
	public Formula getFormula() {
		return formula;
	}

	/**
	 * Returns a string representation of the exception.
	 * @return a string representation of the exception
	 */
	public String toString() {
		return "#CIRCLE!";
	}

	/**
	 * Returns whether the other object is an identical value .
	 * @param other the object to check for equality
	 * @return true if the objects are equal
	 */
	public boolean equals(Object other) {
		if (!(other instanceof CircularReferenceException) || other == null)
			return false;
		CircularReferenceException e = (CircularReferenceException)other;
		return formula.getCell().equals(e.formula.getCell());
	}
}