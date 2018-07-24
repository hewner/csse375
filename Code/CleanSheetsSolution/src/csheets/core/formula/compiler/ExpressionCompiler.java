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
package csheets.core.formula.compiler;

import csheets.core.Cell;
import csheets.core.formula.Expression;

/**
 * A compiler that generates expressions from strings.
 * @author Einar Pehrson
 */
public interface ExpressionCompiler {

	/**
	 * Compiles an expression from the given string.
	 * @param cell the cell for which the expression is to be compiled
	 * @param source a string representing the expression to be compiled
	 * @return the compiled expression
	 * @throws FormulaCompilationException if the expression could not be compiled
	 */
	public Expression compile(Cell cell, String source) throws FormulaCompilationException;

	/**
	 * @return the 
	 */
	public char getStarter();
}