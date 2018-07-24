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

import java.util.Iterator;
import java.util.NoSuchElementException;

/** An iterator wrapper used by MultiInterval to be able to expose
    the intervals contained in the MultiInterval without
    allowing the user to modify the underlying collection
    directly.
    @author Peter Palotas */
public class ConstMultiIntervalIterator implements Iterator<Interval> {

	private Iterator<Interval> iter;

	/** Creates a new "const"-iterator using the iterator specified
	    for all operations except remove.
	    @param iter the iterator to use for iteration. */
	protected ConstMultiIntervalIterator(Iterator<Interval> iter) {
		this.iter = iter;
	}

	/** Returns <code>true</code> if the iteration has more elements.
		(In other words, returns <code>true</code> if <code>next</code> would return
		an element rather than throwing an exception.)
		@return true if the iterator has more elements, <code>false</code> otherwise. */
	public boolean hasNext() {
		return iter.hasNext();
	}

	/** Returns the next element in the iteration. Calling this method repeatedly until the
		<code>hasNext()</code> method returns <code>false</code> will return each element
		in the underlying collection exactly once.
		@return the next element in the iteration.
		@exception NoSuchElementException iteration has no more elements. */
	public Interval next() {
		return iter.next();
	}

	/** Throws UnsupportedOperationException since this iterator does not allow modification
		of the unerlying collection.
		@exception UnsupportedOperationException whenever called. */
	public void remove() {
		throw new UnsupportedOperationException("Cannot modify a MultiInterval through its iterator");
	}
};
