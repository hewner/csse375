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
import csheets.core.formula.compiler.IllegalFunctionCallException;
import csheets.core.formula.lang.CellReference;
import csheets.core.formula.lang.ReferenceOperation;

/**
 * A base-class for classes that rebuild expressions. In this form, it simply
 * copies expressions.
 * @author Einar Pehrson
 */
public class ExpressionBuilder implements ExpressionVisitor {

	/**
	 * Creates a new expression builder.
	 */
	public ExpressionBuilder() {}

	/**
	 * Returns a copy of the given expression.
	 * @param expression the expression to rebuild
	 * @return the rebuilt expression
	 */
	public Expression getExpression(Expression expression) {
		return (Expression)expression.accept(this);
	}

	public Expression visitLiteral(Literal literal) {
		return new Literal(literal.getValue());
	}

	public Expression visitUnaryOperation(UnaryOperation operation) {
		Expression operand = (Expression)operation.getOperand().accept(this);
		return new UnaryOperation(operation.getOperator(), operand);
	}

	public Expression visitBinaryOperation(BinaryOperation operation) {
		Expression leftOperand = (Expression)operation.getLeftOperand().accept(this);
		Expression rightOperand = (Expression)operation.getRightOperand().accept(this);
		return new BinaryOperation(leftOperand, operation.getOperator(), rightOperand);
	}

	public Expression visitReference(Reference reference) {
		if (reference instanceof CellReference) {
			CellReference cellRef = (CellReference)reference;
			return new CellReference(cellRef.getCell(),
				cellRef.isColumnAbsolute(), cellRef.isRowAbsolute());
		} else {
			ReferenceOperation refOp = (ReferenceOperation)reference;
			return new ReferenceOperation(
				(Reference)refOp.getLeftOperand().accept(this),
				refOp.getOperator(),
				(Reference)refOp.getRightOperand().accept(this));
		}
	}

	public Expression visitFunctionCall(FunctionCall call) {
		Expression[] arguments = call.getArguments();
		Expression[] newArguments = new Expression[arguments.length];
		int i = 0;
		for (Expression argument : arguments)
			newArguments[i++] = (Expression)argument.accept(this);
		try {
			return new FunctionCall(call.getFunction(), newArguments);
		} catch (IllegalFunctionCallException e) {
			// Doesn't happen
			return null;
		}
	}
}