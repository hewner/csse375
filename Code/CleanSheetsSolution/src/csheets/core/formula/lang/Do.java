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

import java.util.LinkedList;
import java.util.List;

import csheets.core.IllegalValueTypeException;
import csheets.core.Value;
import csheets.core.formula.BinaryOperator;
import csheets.core.formula.Expression;
import csheets.core.formula.Function;
import csheets.core.formula.FunctionParameter;
import csheets.core.formula.Literal;

/**
 * A function that emulates a looping statement, where each cell in a given
 * range that satisfy a given condition, or each corresponding cell in
 * another range, are passed to a function.
 * @author Einar Pehrson
 */
public class Do implements Function {

	/** Parameters: function, function range, condition and condition range */
	public static final FunctionParameter[] parameters = new FunctionParameter[] {
		new FunctionParameter(Value.Type.TEXT, "Function", false,
			"The name of the function to which arguments are to be passed."),
		new FunctionParameter(Value.Type.MATRIX, "Function Range", false,
			"The range from which to select arguments"),
		new FunctionParameter(Value.Type.TEXT, "Conditional Operator", false,
			"The binary operator to use in the condition when selecting arguments."),
		new FunctionParameter(Value.Type.UNDEFINED, "Conditional Argument", false,
			"The right operand to use in the condition when selecting arguments."),
		new FunctionParameter(Value.Type.MATRIX, "Condition Range", true,
			"The range to use when checking conditions.")
	};

	/**
	 * Creates a new instance of the DO function.
	 */
	public Do() {}

	public String getIdentifier() {
		return "DO";
	}

	public Value applyTo(Expression[] arguments) throws IllegalValueTypeException {
		// Check that the function and conditional operator exist
		Function function = null;
		BinaryOperator condOp = null;
		try {
			function = Language.getInstance().getFunction(arguments[0].evaluate().toText());
			condOp = Language.getInstance().getBinaryOperator(arguments[2].evaluate().toText());
		} catch (UnknownElementException e) {
			return new Value(e);
		}
		
		// Check that the range dimensions agree
		Value[][] opRange = arguments[1].evaluate().toMatrix();
		Value[][] condRange = opRange;
		if (arguments.length == 5) {
			condRange = arguments[4].evaluate().toMatrix();
			if (opRange.length != condRange.length || opRange[0].length != condRange[0].length)
				return new Value(new IllegalArgumentException("Range dimensions must be equal"));
		}

		// Collects arguments
		Literal condArg = new Literal(arguments[3].evaluate());
		List<Literal> accepted = new LinkedList<Literal>();
		for (int row = 0; row < condRange.length; row++)
			for (int column = 0; column < condRange[row].length; column++)
				if (condOp.applyTo(new Literal(condRange[row][column]), condArg)
						.toBoolean())
					accepted.add(new Literal(opRange[row][column]));

		// Evaluates function call and returns
		if (accepted.size() > 0)
			return function.applyTo(accepted.toArray(new Expression[accepted.size()]));
		else
			return new Value();
	}

	public FunctionParameter[] getParameters() {
		return parameters;
	}

	public boolean isVarArg() {
		return false;
	}
}