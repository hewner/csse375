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

import java.util.SortedSet;

import csheets.core.Cell;
import csheets.core.Value;

/**
 * A reference to one or more cells in a spreadsheet.
 * @author Einar Pehrson
 */
public interface Reference extends Expression, Comparable<Reference> {

	public Value evaluate();

	/**
	 * Returns the cell addresses (or address) that the reference points to.
	 * @return a naturally ordered set of the references' addresses
	 */
	public SortedSet<Cell> getCells();

	/**
	 * Compares the reference with the given reference for order.
	 * @param reference the reference to be compared
	 * @return a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object.
	 */
	public int compareTo(Reference reference);
}