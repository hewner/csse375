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

/** Representing a parse error of an assertion. Thrown by Assertion().
	@author Fredrik Johansson
	@author Peter Palotas
*/
public class AssertionException extends Exception {
	static final long serialVersionUID = 2950555036431465919L;
	private int line;
	private int column;
	private String errorMsg;

	public AssertionException(int line, int column, String errorMsg) {
		this.line = line;
		this.column = column;
		this.errorMsg = errorMsg;
	}

	public AssertionException(String errorMsg) {
		this.line = this.column = -1;
		this.errorMsg = errorMsg;
	}

	public AssertionException(antlr.RecognitionException re) {
		this.line = re.getLine();
		this.column = re.getColumn();
		this.errorMsg = re.toString();
	}

	public AssertionException(antlr.TokenStreamException tse) {
		this.line = -1;
		this.column = -1;
		this.errorMsg = tse.toString();
	}

	public AssertionException(antlr.CharStreamException cse) {
		this.line = -1;
		this.column = -1;
		this.errorMsg = cse.toString();
	}

	public AssertionException(antlr.MismatchedCharException ex) {
		this.line = ex.getLine();
		this.column = ex.getColumn();
		this.errorMsg = ex.getMessage();
	}

	public AssertionException(antlr.MismatchedTokenException ex) {
		this.line = ex.getLine();
		this.column = ex.getColumn();
		this.errorMsg = ex.getMessage();
	}

	public AssertionException(antlr.NoViableAltException ex) {
		this.line = ex.getLine();
		this.column = ex.getColumn();
		this.errorMsg = ex.getMessage();
	}

	public AssertionException(antlr.NoViableAltForCharException ex) {
		this.line = ex.getLine();
		this.column = ex.getColumn();
		this.errorMsg = ex.getMessage();
	}

	/** Returns <code>true</code> if this exception contains information
		about where in the assertion string the error occured,
		meaning getLine() and getColumn() will return
		correct values. Returns <code>false</code> otherwise.
	*/
	public boolean hasLocationInfo() {
		return (this.column != -1);
	}

	/** Returns the line of the assertion on which this
		error occured if hasLocationInfo() returns
		<code>true</code>. Otherwise <code>-1</code> will
		be returned.
	*/
	public int getLine() {
		return line;
	}

	/** Returns the column of the assertion on which this
		error occured if hasLocationInfo() returns
		<code>true</code>. Otherwise <code>-1</code> will
		be returned.
	*/
	public int getColumn() {
		return column;
	}

	/** Returns the message of this error. */
	public String getMessage() {
		return errorMsg;
	}

	/** Returns a message describing this error.
	*/
	public String toString() {
		String s = "Error parsing assertion:\n";
		if (hasLocationInfo())
			s += "col " + getColumn() + ": ";
		s += getMessage();
		return s;
	}
}
