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
package csheets.core.formula;

/**
 * An operation in a formula.
 * @author Einar Pehrson
 */
public abstract class Operation<O extends Operator> implements Expression {

	/** The operator */
	protected O operator;

	/**
	 * Creates a new operation.
	 * @param operator the (n-ary) operator
	 */
	public Operation(O operator) {
		this.operator = operator;
	}

	/** 
	 * Returns the operator.
	 * @return the operator.
	 */
	public O getOperator() {
		return operator;
	}
}