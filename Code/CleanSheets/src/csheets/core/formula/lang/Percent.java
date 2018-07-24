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

import csheets.core.IllegalValueTypeException;
import csheets.core.Value;
import csheets.core.formula.Expression;
import csheets.core.formula.UnaryOperator;

/**
 * A percent operator (division by 100).
 * @author Einar Pehrson
 */
public class Percent implements UnaryOperator {

	/** The unique version identifier used for serialization */
	private static final long serialVersionUID = 4470721782585882011L;

	/**
	 * Creates a new percent operator.
	 */
	public Percent() {}

	/**
	 * Returns the operand's value divided by 100.
	 * @return the operand's value divided by 100
	 * @throws IllegalValueTypeException if the value of the operand is not numeric
	 */
	public Value applyTo(Expression operand) throws IllegalValueTypeException {
		return new Value(operand.evaluate().toDouble() / 100d);
	}

	public String getIdentifier() {
		return "%";
	}

	public boolean isPrefix() {
		return false;
	}

	public Value.Type getOperandValueType() {
		return Value.Type.NUMERIC;
	}

	public String toString() {
		return getIdentifier();
	}
}