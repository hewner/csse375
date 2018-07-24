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

import java.util.EventListener;

/**
 * A listener interface for receiving notification on events occurring in a
 * testable cell.
 * @author Einar Pehrson
 */
public interface TestableCellListener extends EventListener {

	/**
	 * Invoked when a test case is added to or removed from a cell, or when
	 * the validation state of a test case is changed.
	 * @param cell the cell that was modified
	 */
	public void testCasesChanged(TestableCell cell);

	/**
	 * Invoked when a test case parameter is added to or removed from a cell.
	 * @param cell the cell that was modified
	 */
	public void testCaseParametersChanged(TestableCell cell);
}