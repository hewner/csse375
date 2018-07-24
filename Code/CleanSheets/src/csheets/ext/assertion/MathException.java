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

import csheets.core.formula.util.ExpressionVisitorException;

/**
 * This Exception is thrown if you try to do calculations on intervals
 * which contains forbidden values according to the choosen matematical
 * operation. For example division by zero.
 * @author Fredrik
 */
public class MathException extends ExpressionVisitorException {

	/** The serialVersionUID of the MathException.java */
	private static final long serialVersionUID = -8774781783987699960L;

	public MathException() {
		super();
	}
	
	public MathException(String message) {
		super(message);
	}
	
	public MathException(String message, Throwable cause) {
		super(message, cause);
	}

	public MathException(Throwable cause) {
		super(cause);
	}
}