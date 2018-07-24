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

import java.io.Serializable;

/** This class contains information about a warning in an assertion.
	A warning denotes a possible inconsistency or incorrectens in
	an assertion. Objects of this type are returned by <code>Assertion.getWarnings()</code>
	@see Assertion
	@author Peter Palotas
*/
public class AssertionWarning implements Serializable {

	/** The unique version identifier used for serialization */
	private static final long serialVersionUID = -390161028866493880L;

	private Interval i1;
	private Interval i2;
	private Type type;

	/** Denotes the type of a warning. See documentation for the constructor for more info. */
	public enum Type {
		INTERSECTING,
		ENCLOSING,
		EXCLUDING
	};

	/** Constructor.
		@param type Either INTERSECTING meaning that i1 and i2 intersects,
			ENCLOSING meaning that i2 is completely enclosed by i1 or
			EXCLUDING meaning that i2 is completely excluded by i1.
		@param i1 One of the intervals of the conflict.
		@param i2 The other interval of the conflict.
	*/
	public AssertionWarning(Type type, Interval i1, Interval i2) {
		this.type = type;
		this.i1 = i1;
		this.i2 = i2;
	}

	/** Returns a message in english describing the conflict represented
	  *  by this warning.
	  */
	public String toString() {

		switch (type) {
			case INTERSECTING:
				return "Interval " + i1 + " intersects with " + i2;

			case ENCLOSING:
				return "Interval " + i2 + " completely encloses " + i1;

			case EXCLUDING:
				return "Interval " + i2 + " completely encloses " + i1;
		}
		return "Unknown conflict between the intervals " + i1 + " and " + i2;
	}

	/** Returns the type of this warning.
	   @return the type of this warning.
	*/
	public Type getType() {
		return type;
	}

	/** Returns the first interval involved in this warning.
		@return the first interval involved in this warning. */
	public Interval getI1() {
		return i1;
	}

	/** Returns the second interval involved in this warning.
		@return the second interval involved in this warning. */
	public Interval getI2() {
		return i2;
	}
}
