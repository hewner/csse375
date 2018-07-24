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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import csheets.core.Cell;
import csheets.core.Value;
import csheets.ext.CellExtension;

/**
 * An extension of a cell in a spreadsheet, with support for assertions.
 * @author Peter Palotas
 * @author Fredrik Johansson
 * @author Einar Pehrson
 */
public class AssertableCell extends CellExtension {

	/** The unique version identifier used for serialization */
	private static final long serialVersionUID = 4956240183977127091L;

	/** The cell's user-specified assertion */
    private USAssertion usAssertion;

    /** The cell's system-generated assertion */
    private SGAssertion sgAssertion;

	/** The listeners registered to receive events from the assertable cell */
	private transient List<AssertableCellListener> listeners
		= new ArrayList<AssertableCellListener>();

	/**
	 * A flag that, when set, indicates that the system-generated assertion is
	 * <code>null</code> because of a mathematical error.
	 */
	private boolean mathError;

	/**
	 * A short description of a possible existing math-error occuring when
	 * generating the system-generated assertion.
	 */
	private String mathErrorMsg;

	/**
	 * Creates a assertable cell extension for the given cell.
	 * @param cell the cell to extend
	 */
	AssertableCell(Cell cell) {
		super(cell, AssertionExtension.NAME);
	}


/*
 * DATA UPDATES
 */


	public void contentChanged(Cell cell) {
		if (getFormula() != null && !getFormula().getReferences().isEmpty())
			generateAssertion();
		else {
			sgAssertion = null;
			for (Cell depCell : getDependents())
				((AssertableCell)depCell).generateAssertion();
		}
	}


/*
 * ASSERTIONS ACCESSORS
 */


	/**
	 * Get the cell's user supplied assertion.
	 * @return The user supplied assertion for the cell or <code>null</code> if no user
	 supplied assertion exists.
	*/
	public USAssertion getUSAssertion() {
		return usAssertion;
	}

	/** Get the cell's system generated assertion.
		@return The system generated assertion for the cell or <code>null</code> if no
		system generated assertion exist. */
	public SGAssertion getSGAssertion() {
		return sgAssertion;
	}

	/** Get the most significant assertion of the cell. This is currently the
		System Generated Assertion if one exists, and the user supplied assertion otherwise.
		@return the most significant assertion if one exists, or <code>null</code> otherwise. */
	public Assertion getPriorityAssertion() {
		if (isSGAsserted())
			return getSGAssertion();

		if (isUSAsserted())
			return getUSAssertion();

		return null;
	}

	/**
	 * Returns whether the cell has an assertion.
	 * @return true if the cell has an assertion
	 */
	public boolean isAsserted() {
		return isUSAsserted() || isSGAsserted();
	}

	/** Checks if the cell has a user supplied assertion associated with it.
		@return <code>true</code> if the cell has a user supplied assertion associated with it, <code>false</code> otherwise. */
	public boolean isUSAsserted() {
		return usAssertion != null;
	}

	/** Checks if the cell has a system generated assertion associated with it.
		@return <code>true</code> if the cell has a system generated assertion associated with it, <code>false</code> otherwise. */
	public boolean isSGAsserted() {
		return sgAssertion != null;
	}


/*
 * ASSERTION MODIFIERS
 */


	/**
	 * Sets the user-specified assertion for the cell.
	 * @param assertion the user-specified assertion
	 */
	public void setUSAssertion(USAssertion assertion) {
		this.usAssertion = assertion;
		// Notifies listeners
		fireAssertionsChanged();

		// Notifies all depending SGAssertions if this cell has no SGAssertion
		if (sgAssertion == null)
			for (Cell cell : getDependents())
				((AssertableCell)cell).generateAssertion();
	}

	/**
	 * Invoked to indicate that the content of the cell in the spreadsheet was
	 * modified and that assertions that depend on that data must be updated.
	 */
	public void generateAssertion() {
 		if (getFormula() != null && !getFormula().hasCircularReference()) {
	 		// For debugging purposes only
			// System.out.println("Update SGA for cell: " + this);

			try {
				sgAssertion = new SGAssertion(this);
				mathError = false;
				mathErrorMsg = null;
			} catch (AssertionArithmeticException e) {
				// For debugging purposes only
				//e.printStackTrace();

				sgAssertion = null;
				mathError = false;
				mathErrorMsg = null;
			} catch (MathException e) {
				// Division by zero error
				mathErrorMsg = e.getMessage();
				mathError = true;
				sgAssertion = null;
			}

			for (Cell cell : getDependents())
				((AssertableCell)cell).generateAssertion();

			// Notifies listeners
			fireAssertionsChanged();
		}
	}


/*
 * ASSERTING
 */


	/** Asserts the current value of the cell using the user supplied assertion associated with the cell.
		@return the result of the assertion. If no assertion exists, <code>Assertion.Result.OK</code> is returned.
		@see USAssertion#validate(Value) */
	public Assertion.Result assertUS() {
		return assertUS(getValue());
	}

	/** Asserts the specified value using the user supplied assertion associated with the cell.
		@return the result of the assertion. If no assertion exists, <code>Assertion.Result.OK</code> is returned.
		@see USAssertion#validate(Value) */
	public Assertion.Result assertUS(Value value) {
		if (usAssertion != null)
			return usAssertion.validate(value);

		// If no assertion exist, the value is okay.
		return Assertion.Result.OK;
	}

	/** Asserts the current value of the cell using the system generated assertion associated with the cell.
		@return the result of the assertion. If no assertion exists, <code>Assertion.Result.OK</code> is returned.
		@see SGAssertion#validate(Value) */
	public Assertion.Result assertSG() {
		return assertSG(getValue());
	}

	/** Asserts the specified value using the system generated assertion associated with the cell.
		@return the result of the assertion. If no assertion exists, <code>Assertion.Result.OK</code> is returned.
		@see SGAssertion#validate(Value) */
	public Assertion.Result assertSG(Value value) {
		if (sgAssertion != null)
			return sgAssertion.validate(value);

		// If no assertion exist, the value is okay.
		return Assertion.Result.OK;
	}

	/** Asserts the specified value using any (or both) assertion(s) associated with the cell.
		The system generated assertion (if available) will be run first and upon error its return
		code will be returned. If the value was successfully asserted using the system generated
		assertion then the value will be asserted using the user supplied assertion, and its
		return code will be returned.  If no assertion is available in the cell <code>Assertion.Result.OK</code>
		will be returned.
		@return the success status of the assertion.
		@see USAssertion#validate(Value) */
	public Assertion.Result assertAny(Value value) {
		Assertion.Result res = Assertion.Result.OK;

		if (isSGAsserted()) {
			res = assertSG(value);
		}

		if (res == Assertion.Result.OK && isUSAsserted()) {
			res = assertUS(value);
		}

		return res;
	}

	/** Asserts the current value of the cell using any (or both) assertion(s) associated with the cell.
		The system generated assertion (if available) will be run first and upon error its return
		code will be returned. If the value was successfully asserted using the system generated
		assertion then the value will be asserted using the user supplied assertion, and its
		return code will be returned.  If no assertion is available in the cell <code>Assertion.Result.OK</code>
		will be returned.
		@return the success status of the assertion.
		@see USAssertion#validate(Value) */
	public Assertion.Result assertAny() {
		return assertAny(getValue());
	}

	/** Checks wether the assertions associated with the cell agree with each other and
	    that no division by zero can occur using values within this assertion.
	    @return
	    		<ul>
	    		<li><code>Assertion.ComparisonResult.OK</code> if both the SGA and USA are specified for the cell and are equal <i>or</i>
	    		if none or only one of them is specified, AND the system generated assertion did not fail to
	    		generate because it would include a division by zero error.
	    		<li><code>Assertion.ComparisonResult.NON_EQUAL</code> if the SGA and USA are both specified and do not agree with each other.
	    		<li><code>Assertion.ComparisonResult.DIV_BY_ZERO</code> if the SGA failed to be generated because it would lead to
	    			a division by zero error, either because of errenous assertions in precedents or an errenous
	    			formula in the cell.
				</ul>
	    		 */
	public Assertion.ComparisonResult assertAssertions() {

		if (!isSGAsserted() && mathError) {
			Assertion.ComparisonResult errorResult = Assertion.ComparisonResult.ILLEGAL_INTERVAL;
			errorResult.setErrorMsg(mathErrorMsg);
			return errorResult;
		}

		if (isSGAsserted() && isUSAsserted())
			return sgAssertion.equals(usAssertion) ? Assertion.ComparisonResult.OK : Assertion.ComparisonResult.NON_EQUAL;

		return Assertion.ComparisonResult.OK;
	}

	/** Checks wether there are any errors or inconsitencies in the cell related
		to assertions.
		@return <code>false</code> if there are no errors that can be found in the cell using
				assertion related functions, <code>true</code> otherwise. */
	public boolean hasAssertionError() {
		return !((assertAssertions() == Assertion.ComparisonResult.OK) && (assertAny() == Assertion.Result.OK));
	}


/*
 * EVENT LISTENING SUPPORT
 */


	/**
	 * Registers the given listener on the cell.
	 * @param listener the listener to be added
	 */
	public void addAssertableCellListener(AssertableCellListener listener) {
		listeners.add(listener);
	}

	/**
	 * Removes the given listener from the cell.
	 * @param listener the listener to be removed
	 */
	public void removeAssertableCellListener(AssertableCellListener listener) {
		listeners.remove(listener);
	}

	/**
	 * Notifies all registered listeners that the cell's assertions changed.
	 */
	protected void fireAssertionsChanged() {
		for (AssertableCellListener listener : listeners)
			listener.assertionsChanged(this);
	}

	/**
	 * Customizes serialization, by recreating the listener list.
	 * @param stream the object input stream from which the object is to be read
	 * @throws IOException If any of the usual Input/Output related exceptions occur
	 * @throws ClassNotFoundException If the class of a serialized object cannot be found.
	 */
	private void readObject(java.io.ObjectInputStream stream)
			throws java.io.IOException, ClassNotFoundException {
	    stream.defaultReadObject();
		listeners = new ArrayList<AssertableCellListener>();
	}
}