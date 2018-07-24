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

import csheets.core.formula.BinaryOperation;
import csheets.core.formula.FunctionCall;
import csheets.core.formula.Literal;
import csheets.core.formula.Reference;
import csheets.core.formula.UnaryOperation;

/**
 * A class for printing expressions on multiple lines with indentation.
 * @author Einar Pehrson
 */
public class ExpressionTreePrinter extends AbstractExpressionVisitor {

	/** The number of spaces to use for each indentation unit */
	public static final int INDENT_DISTANCE = 3;

	/** The current indentation count */
	private int indentCount = 0;

	/**
	 * Creates a new expression printer.
	 */
	public ExpressionTreePrinter() {}

	public Object visitLiteral(Literal literal) {
		print(literal);
		return literal;
	}

	public Object visitUnaryOperation(UnaryOperation operation) {
		print(operation.getOperator());
		indentCount++;
		super.visitUnaryOperation(operation);
		indentCount--;
		return operation;
	}

	public Object visitBinaryOperation(BinaryOperation operation) {
		print(operation.getOperator());
		indentCount++;
		super.visitBinaryOperation(operation);
		indentCount--;
		return operation;
	}

	public Object visitReference(Reference reference) {
		print(reference);
		return reference;
	}

	public Object visitFunctionCall(FunctionCall call) {
		print(call.getFunction());
		indentCount++;
		super.visitFunctionCall(call);
		indentCount--;
		return call;
	}

	private void print(Object o) {
		String indentation = "";
		for (int i = 0; i < indentCount * INDENT_DISTANCE; i++)
			indentation += " ";
		System.out.println(indentation + o);
	}
}