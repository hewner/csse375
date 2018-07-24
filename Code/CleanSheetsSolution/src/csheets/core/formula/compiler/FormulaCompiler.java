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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import csheets.CleanSheets;
import csheets.core.Cell;
import csheets.core.formula.Expression;
import csheets.core.formula.Formula;

/**
 * A compiler that generates formulas from strings.
 * @author Einar Pehrson
 */
public class FormulaCompiler {

	/** The singleton instance */
	private static final FormulaCompiler instance = new FormulaCompiler();

	/** The name of the file in which compiler properties are stored */
	private static final String PROPERTIES_FILENAME = "res/compilers.props";

	/** The expression compilers used to compile formulas */
	private List<ExpressionCompiler> compilers = new ArrayList<ExpressionCompiler>();
	
	/**
	 * Creates the formula compiler.
	 */
	private FormulaCompiler() {
		// Loads properties
		Properties compilerProps = new Properties();
		InputStream stream = CleanSheets.class.getResourceAsStream(PROPERTIES_FILENAME);
		if (stream != null) {
			try {
				compilerProps.load(stream);
			} catch (IOException e) {
				System.err.println("An I/O error occurred when loading compiler"
					+ " properties file (" + PROPERTIES_FILENAME + ").");
				return;
			} finally {
				try {
					if (stream != null)
						stream.close();
				} catch (IOException e) {}
			}

			// Loads elements
			for (Object className : compilerProps.keySet()) {
				// Loads class and instantiates element
				Class elementClass;
				Object element;
				try {
					elementClass = Class.forName(getClass().getPackage()
						.getName() + "." + (String)className);
					element = elementClass.newInstance();
				} catch (Exception e) {
					// Skip this element, regardless of what went wrong
					e.printStackTrace();
					continue;
				}

				// Stores element
				if (ExpressionCompiler.class.isAssignableFrom(elementClass))
					compilers.add(ExpressionCompiler.class.cast(element));
			}
		} else
			System.err.println("Could not find compiler properties file ("
				+ PROPERTIES_FILENAME + ").");
	}

	/**
	 * Returns the singleton instance.
	 * @return the singleton instance
	 */
	public static FormulaCompiler getInstance() {
		return instance;
	}

	/**
	 * Compiles a formula for the given cell from the given string.
	 * @param cell the cell for which a formula is to be generated
	 * @param source a string representing the formula to be compiled
	 * @return a list of lexical tokens
	 * @throws FormulaCompilationException if the formula could not be compiled
	 */
	public Formula compile(Cell cell, String source) throws FormulaCompilationException {
		for (ExpressionCompiler compiler : compilers)
			if (source.charAt(0) == compiler.getStarter()) {
				Expression expression = compiler.compile(cell, source);
				return new Formula(cell, expression);
			}
		return null;
	}
}