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
import csheets.core.formula.Expression;
import csheets.core.formula.FunctionCall;
import csheets.core.formula.Literal;
import csheets.core.formula.Reference;
import csheets.core.formula.UnaryOperation;

/**
 * A default implementation of an expression visitor, that simply visits all
 * the nodes in the tree. All methods return the expression that was visited.
 * @author Einar Pehrson
 */
public abstract class AbstractExpressionVisitor implements ExpressionVisitor {

	/**
	 * Creates a new expression visitor.
	 */
	public AbstractExpressionVisitor() {}

	public Object visitLiteral(Literal literal) {
		return literal;
	}

	public Object visitUnaryOperation(UnaryOperation operation) {
		operation.getOperand().accept(this);
		return operation;
	}

	public Object visitBinaryOperation(BinaryOperation operation) {
		operation.getLeftOperand().accept(this);
		operation.getRightOperand().accept(this);
		return operation;
	}

	public Object visitReference(Reference reference) {
		return reference;
	}

	public Object visitFunctionCall(FunctionCall call) {
		for (Expression argument : call.getArguments())
			argument.accept(this);
		return call;
	}
}