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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;

import csheets.core.Cell;
import csheets.core.IllegalValueTypeException;
import csheets.core.formula.BinaryOperation;
import csheets.core.formula.Expression;
import csheets.core.formula.FunctionCall;
import csheets.core.formula.Literal;
import csheets.core.formula.Operator;
import csheets.core.formula.Reference;
import csheets.core.formula.UnaryOperation;
import csheets.core.formula.lang.Adder;
import csheets.core.formula.lang.Divider;
import csheets.core.formula.lang.Exponentiator;
import csheets.core.formula.lang.Multiplier;
import csheets.core.formula.lang.Negator;
import csheets.core.formula.lang.Subtracter;
import csheets.core.formula.util.ExpressionVisitor;

/** A Visitor for calculating System Generated assertions for a formula
    in the form of an Expression tree. */
public class AssertionArithmeticVisitor implements ExpressionVisitor {

	/** A stack used to calculate the final interval */
	private Stack<List<MultiInterval>> intervalStack = new Stack<List<MultiInterval>>();

	private Set<Cell> referencedCells = new TreeSet<Cell>();

	/**
	 * Constructs a new AssertionArithmeticVisitor.
	 */
	public AssertionArithmeticVisitor() {}

	/** Retrieve the result of the arithmetic calculations performed by this visitor.
		<p><b>NOTE!</b> This function should only be called after the visitor
		has been used to traverse some Expression tree (by calling Expression.accept() passing
		this visitor as an argument. Otherwise an exception will be thrown.
		@param expression the expression from which the
		@return The resulting interval from the performed calculations.
		@throws AssertionArithmeticException if no result has been calculated yet, or if
				the calculations resulted in more than one result. (Indicates an error in the formula). */
	public MultiInterval getResult(Expression expression)
			throws AssertionArithmeticException, MathException {
		// Clears collections
		intervalStack.clear();
		referencedCells.clear();

		// Builds intervals
		expression.accept(this);

		// intervalStack == null set temporary? by Fredrik
		if (intervalStack == null || intervalStack.size() != 1) {
			throw new AssertionArithmeticException("Result from assertion arithmetics was errenous. Multiple results found. Error in formula?");
		}

		List<MultiInterval> list = intervalStack.peek();

		if (list.size() != 1) {
			throw new AssertionArithmeticException("Result from assertion arithmetics was errenous. Single result with multiple intervals. Error in formula?");
		}

		return list.get(0);
	}

	public Object visitBinaryOperation(BinaryOperation operation)
			throws AssertionArithmeticException, MathException {

		Operator operator = operation.getOperator();

		operation.getLeftOperand().accept(this);
		List<MultiInterval> leftList = intervalStack.pop();

		operation.getRightOperand().accept(this);
		List<MultiInterval> rightList = intervalStack.pop();

		if (leftList.size() != 1 || rightList.size() != 1)
			throw new AssertionArithmeticException("No supported binary operator exist for ranges.");

		MultiInterval left = leftList.get(0);
		MultiInterval right = rightList.get(0);

		List<MultiInterval> list = new ArrayList<MultiInterval>(1);
		if (operator instanceof Multiplier) {
			list.add(MultiInterval.mul(left, right));
			intervalStack.push(list);
		} else if (operator instanceof Adder) {
			list.add(MultiInterval.add(left, right));
			intervalStack.push(list);
		} else if (operator instanceof Subtracter) {
			list.add(MultiInterval.sub(left, right));
			intervalStack.push(list);
		} else if (operator instanceof Divider) {
			list.add(MultiInterval.div(left, right));
			intervalStack.push(list);
		} else if (operator instanceof Exponentiator) {
			list.add(MultiInterval.pow(left, right));
			intervalStack.push(list);
		} else {
			throw new AssertionArithmeticException("Unsupported binary operator " + operator + " found.");
		}
		return operation;
	}

	public Object visitFunctionCall(FunctionCall call)
			throws AssertionArithmeticException {

		List<MultiInterval> paramList = new ArrayList<MultiInterval>();
		for (Expression argument : call.getArguments()) {
			argument.accept(this);
			List<MultiInterval> list = intervalStack.pop();
			paramList.addAll(list);
		}

		List<MultiInterval> list = new ArrayList<MultiInterval>(1);

		String funcName = call.getFunction().getIdentifier().toUpperCase();

		if (funcName.equals("RAND")) {
			list.add(MultiInterval.rand());
		} else if (funcName.equals("COS")) {
			list.add(MultiInterval.cos(paramList.get(0)));
		} else if (funcName.equals("SIN")) {
			list.add(MultiInterval.sin(paramList.get(0)));
		} else if (funcName.equals("TAN")) {
			list.add(MultiInterval.tan(paramList.get(0)));
		} else if (funcName.equals("ABS")) {
			list.add(MultiInterval.abs(paramList.get(0)));
		} else if (funcName.equals("INTEGER")) {
			list.add(MultiInterval.toInt(paramList.get(0)));
		} else if (funcName.equals("SQRT")) {
			list.add(MultiInterval.sqrt(paramList.get(0)));
		} else if (funcName.equals("EXP")) {
			list.add(MultiInterval.exp(paramList.get(0)));
		} else if (funcName.equals("LOG")) {
			list.add(MultiInterval.log10(paramList.get(0)));
		} else if (funcName.equals("LN")) {
			list.add(MultiInterval.ln(paramList.get(0)));
		} else if (funcName.equals("FACT")) {
			list.add(MultiInterval.fact(paramList.get(0)));
		} else if (funcName.equals("SUM")) {
			list.add(MultiInterval.sum(paramList));
		} else if (funcName.equals("AVG")) {
			list.add(MultiInterval.avg(paramList));
		} else {
			throw new AssertionArithmeticException(
				"Call to unsupported function " + call.getFunction() + " found.");
		}

		intervalStack.push(list);
		return call;
	}

	public Object visitLiteral(Literal literal) throws AssertionArithmeticException {
		try {
			double value = literal.getValue().toDouble();
			MultiInterval literalInterval = new MultiInterval();
			literalInterval.include(new Interval(value));
			List<MultiInterval> list = new ArrayList<MultiInterval>(1);
			list.add(literalInterval);
			intervalStack.push(list);
		} catch (IllegalValueTypeException e) {
			throw new AssertionArithmeticException("Non-numeric value found in formula.");
		}
		return literal;
	}

	public Object visitReference(Reference reference) throws AssertionArithmeticException {
		List<MultiInterval> list = new ArrayList<MultiInterval>(1);

		for (Cell cell : reference.getCells()) {

			AssertableCell c = (AssertableCell)cell.getExtension(AssertionExtension.NAME);
			checkReference(c);

			if (!c.isAsserted())
				throw new AssertionArithmeticException("Referenced cell "
					+ c + " does not have an assertion associated with it.");

			Assertion ass = c.getSGAssertion();
			if (ass == null)
				ass = c.getUSAssertion();

			list.add(ass.getMultiInterval());
		}

		intervalStack.push(list);
		return reference;
	}

	public Object visitUnaryOperation(UnaryOperation operation)
			throws AssertionArithmeticException  {

		operation.getOperand().accept(this);

		List<MultiInterval> operandList = intervalStack.pop();

		if (operandList.size() != 1)
			throw new AssertionArithmeticException("No supported unary operator exist for ranges.");

		MultiInterval operandInterval = operandList.get(0);
		Operator operator = operation.getOperator();

		if (operator instanceof Negator) {
			List<MultiInterval> negList = new ArrayList<MultiInterval>(1);
			negList.add(MultiInterval.negate(operandInterval));
			intervalStack.push(negList);
		} else {
			throw new AssertionArithmeticException("Unsupported unary operator " + operator + " found.");
		}
		return operation;
	}

	/** Checks that multiple references to the same cell does not exist within formula.
		Even checks indirect references.
		@param cell the cell to check
		@throws AssertionArithmeticException if a multiple reference to the same cell was found. */
	private void checkReference(Cell cell) {
		if (referencedCells.contains(cell))
			throw new AssertionArithmeticException("Multiple references to the same cell found in formula. Cannot generate assertion.");
		referencedCells.add(cell);
		for (Cell c : cell.getPrecedents())
			checkReference(c);
	}
}