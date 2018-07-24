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
import csheets.core.formula.Function;
import csheets.core.formula.FunctionParameter;

/**
 * A function that counts those of its arguments that yield numeric values.
 * @author Einar Pehrson
 */
public class Count implements Function {

	/** The function's parameters */
	public static final FunctionParameter[] parameters = new FunctionParameter[] {
		new FunctionParameter(Value.Type.UNDEFINED, "Expression", false,
			"An expression whose value is checked for type compliance")
		/* new FunctionParameter(Value.Type.TEXT, "Type", true,
			'The type to look for. Accepted values are "NUMERIC" (default),'
			+ '"TEXT", "BOOLEAN", "DATE", "MATRIX" and "ERROR".'); */
	};

	/**
	 * Creates a new instance of the COUNT function.
	 */
	public Count() {}

	public String getIdentifier() {
		return "COUNT";
	}

	public Value applyTo(Expression[] arguments) throws IllegalValueTypeException {
		double count = 0;
		for (Expression expression : arguments) {
			Value value = expression.evaluate();
			if (value.getType() == Value.Type.NUMERIC)
			 	count++;
		}
		return new Value(count);
	}

	public FunctionParameter[] getParameters() {
		return parameters;
	}

	public boolean isVarArg() {
		return true;
	}
}