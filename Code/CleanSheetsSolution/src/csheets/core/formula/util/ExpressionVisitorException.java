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

/**
 * A base-class for exceptions thrown when traversing an expression tree.
 * @author Einar Pehrson
 */
public class ExpressionVisitorException extends RuntimeException {

	/** The serialVersionUID of the ExpressionVisitorException.java */
	private static final long serialVersionUID = -4215222805123779367L;

	/**
	 * Creates a new expression visitor exception.
	 */
	public ExpressionVisitorException() {}

	/**
	 * Creates a new expression visitor exception.
	 * @param message a message that describes what happened
	 */
	public ExpressionVisitorException(String message) {
		super(message);
	}

	/**
	 * Creates a new expression visitor exception.
	 * @param message a message that describes what happened
	 * @param cause the cause of the exception
	 */
	public ExpressionVisitorException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Creates a new expression visitor exception.
	 * @param cause the cause of the exception
	 */
	public ExpressionVisitorException(Throwable cause) {
		super(cause);
	}
}