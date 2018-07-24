/*
 * Copyright (c) 2005 Jens Schou, Staffan Gustafsson, Björn Lanneskog, 
 * Einar Pehrson and Sebastian Kekkonen
 *
 * This file is part of
 * CleanSheets Extension for Test Cases
 *
 * CleanSheets Extension for Test Cases is free software; you can
 * redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either version 2 of
 * the License, or (at your option) any later version.
 *
 * CleanSheets Extension for Test Cases is distributed in the hope that
 * it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with CleanSheets Extension for Test Cases; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA  02111-1307  USA
 */
package csheets.ext.test;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import csheets.core.IllegalValueTypeException;
import csheets.core.Value;
import csheets.core.formula.Formula;

/**
 * Contains the information for a single test case, i.e. a single test case
 * parameter for each of the precedents.
 * @author Staffan Gustafsson
 * @author Jens Schou
 * @author Einar Pehrson
 */
public class TestCase extends Formula implements Serializable {

	/** The unique version identifier used for serialization */
	private static final long serialVersionUID = -6135481035045505755L;

	/** The validation states of a test case. */
	public enum ValidationState {

		/** The test case was validated successfully. */
		VALID,

		/** The test case failed. */
		REJECTED,

		/** The test case has not yet been validated. */
		PENDING
	};

	/** The validation state of the test case. */
	private ValidationState validationState = ValidationState.PENDING;

	/** The parameters of the test case */
	private Set<TestCaseParam> params = new HashSet<TestCaseParam>();

	/** The value received when the test case was evaluated */
	private Value value;

	/**
	 * Creates a new test case for the given cell in the given spreadsheet.
	 * The value of the test case is calculated by replacing the references in
	 * the cell's formula with the given set of test case parameters.
	 * @param cell the cell for which the test case is to be created
	 * @param params the set of test case parameters to use
	 */
	public TestCase(TestableCell cell, Set<TestCaseParam> params) {
		super(cell, new TestCaseBuilder(params).getExpression(cell.getFormula()));

		// Stores members
		this.params = params;

		// Evaluates the test case
		try {
			this.value = super.evaluate();
		} catch (IllegalValueTypeException e) {
			this.value = new Value(e);
		}
	}

	public TestableCell getCell() {
		return (TestableCell)super.getCell();
	}

	public Value evaluate() {
		return value;
	}

	/**
	 *
	 * @return the set of test case parameters that make up the test case
	 */
	public Set<TestCaseParam> getParams(){
		return params;
	}

	/**
	 *
	 * @return Returns the ValidationState of the TestCase.
	 */
	public ValidationState getValidationState() {
		return validationState;
	}

	/**
	 *
	 * @param state Sets the ValidationState of the TestCase to the param's state.
	 */
	public void setValidationState(ValidationState state) {
		this.validationState = state;
	}

	/**
	 * Test if the TestCase is based on a specified TestCaseParam.
	 * @param param The TestCaseParam we want to know it the TestCase uses
	 * @return true if the TestCase uses param, otherwise false
	 */
	public boolean hasParam(TestCaseParam param) {
		Iterator<TestCaseParam> it = params.iterator();
		while(it.hasNext()) {
			if(it.next().equals(param)) return true;
		}
		return false;
	}

	/**
	 * Returns whether the other object has the same parameters
	 * and cell as this has.
	 * @param other the object to check for equality
	 * @return true if the objects are equal
	 */
	public boolean equals(Object other){
		if (!(other instanceof TestCase) || other == null)
			return false;
		TestCase otherTC = (TestCase)other;
		return params.equals(otherTC.getParams()) && getCell().equals(otherTC.getCell());
	}

	/**
	 * Returns the hash code of the test case.
	 * @return the hash code of the test case.
	 */
	public int hashCode() {
		return getCell().hashCode() + params.hashCode();
	}
}