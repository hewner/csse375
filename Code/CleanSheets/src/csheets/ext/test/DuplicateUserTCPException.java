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

/**
 * An exception that is thrown if a user enters a duplicate
 * test case param of type USER_ENTERED.
 * @author Jens Schou
 */
public class DuplicateUserTCPException extends Exception {

	/** The serialVersionUID of the DuplicateUserTCPException.java */
	private static final long serialVersionUID = 8640730494411675302L;

	/** The value that caused the exception */
	private Object value;

	/**
	 * Creates a new duplicate user entered TCP exception.
	 * @param value the value that caused the exception
	 */
	public DuplicateUserTCPException(Object value){
		this(value, null);
	}

	/**
	 * Creates a new duplicate user entered TCP exception.
	 * @param value the value that caused the exception
	 * @param message a message that describes what happened
	 */
	public DuplicateUserTCPException(Object value, String message){
		super(message);
		this.value = value;
	}

	/**
	 * Returns the value that caused the exception.
	 * @return the value that caused the exception
	 */
	public Object getValue() {
		return value;
	}
}