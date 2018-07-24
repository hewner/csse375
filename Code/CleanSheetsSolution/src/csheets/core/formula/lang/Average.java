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
 * A function that returns the numeric average of its arguments.
 * @author Einar Pehrson
 */
public class Average implements Function {

	/** The only (but repeatable) parameter: a numeric term */
	public static final FunctionParameter[] parameters = new FunctionParameter[] {
		new FunctionParameter(Value.Type.NUMERIC, "Term", false,
			"A number to be included in the average")
	};

	/**
	 * Creates a new instance of the AVERAGE function.
	 */
	public Average() {}

	public String getIdentifier() {
		return "AVERAGE";
	}

	public Value applyTo(Expression[] arguments) throws IllegalValueTypeException {
		double count = 0;
		double sum = 0;
		for (Expression expression : arguments) {
			Value value = expression.evaluate();
			if (value.getType() == Value.Type.NUMERIC) {
				count++;
				sum += value.toDouble();
			} else if (value.getType() == Value.Type.MATRIX)
				for (Value[] vector : value.toMatrix()) {
					for (Value item : vector)
						if (item.getType() == Value.Type.NUMERIC) {
							count++;
						 	sum += item.toDouble();
						} else
						 	throw new IllegalValueTypeException(item, Value.Type.NUMERIC);
			} else
				throw new IllegalValueTypeException(value, Value.Type.NUMERIC);
		}
		return new Value(sum / count);
	}

	public FunctionParameter[] getParameters() {
		return parameters;
	}

	public boolean isVarArg() {
		return true;
	}
}