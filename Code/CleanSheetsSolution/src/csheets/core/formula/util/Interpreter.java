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
	
import csheets.core.Value;
import csheets.core.formula.BinaryOperation;
import csheets.core.formula.FunctionCall;
import csheets.core.formula.Literal;
import csheets.core.formula.Reference;
import csheets.core.formula.UnaryOperation;

/**
 * A base-class for customized formula interpreters. The interpretation relies
 * on the Visitor pattern, so a typical invocation would look like
 * <code>formula.accept(interpreter)</code>.
 * @author Einar Pehrson
 */
public abstract class Interpreter implements ExpressionVisitor {

	/**
	 * Creates a new interpreter.
	 */
	public Interpreter() {}

	public Value visitLiteral(Literal literal) {
		return literal.evaluate();
	}

	public abstract Value visitUnaryOperation(UnaryOperation operation);

	public abstract Value visitBinaryOperation(BinaryOperation operation);

	public Value visitReference(Reference reference) {
		return reference.evaluate();
	}

	public abstract Value visitFunctionCall(FunctionCall call);
}