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

import java.io.StringReader;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Vector;

/**
 * This class represents an Assertion.
 * @author Fredrik Johansson
 * @author Peter Palotas
 */
public class USAssertion extends Assertion {

	/** The unique version identifier used for serialization */
	private static final long serialVersionUID = -7911803174007268562L;

	/** The original assertion string as entered by the user. */
	protected String assertion;

	/** A list of warnings or inconsitencies in the current assertion */
	private List<AssertionWarning> warnings = new Vector<AssertionWarning>();


	/**
	 * Constructs an Assertion object
	 * @param assertion is a string representation of an assertion
     * @throws AssertionException is thrown if a syntactic or semantic error occurs
     */
	public USAssertion(String assertion) throws AssertionException {

		List<Interval> orIntervals = new Vector<Interval>();
		List<Interval> exceptIntervals = new Vector<Interval>();

		AssertionLexer lexer = new AssertionLexer(new StringReader(assertion));
		AssertionParser parser = new AssertionParser(lexer);

		try {
			parser.assertion(this, orIntervals, exceptIntervals);
		} catch (antlr.MismatchedCharException mce) {
			throw new AssertionException(mce);
		} catch (antlr.MismatchedTokenException mte) {
			throw new AssertionException(mte);
		} catch (antlr.NoViableAltException nvae) {
			throw new AssertionException(nvae);
		} catch (antlr.NoViableAltForCharException nvafce) {
			throw new AssertionException(nvafce);
		} catch (antlr.SemanticException se) {
			throw new AssertionException(se);
		} catch (antlr.RecognitionException re) {
			throw new AssertionException(re);
		} catch (antlr.TokenStreamException tse) {
			throw new AssertionException(tse);
		}

		this.assertion = assertion;

		// To do: Make the inconsistency checks more efficient and add more conditions to check for.

		// Check the consistency of the assertion, and create warnings for
		// suspected inconsistencies.

		// Check for intersections between or-intervals
		for (ListIterator<Interval> it1 = orIntervals.listIterator(); it1.hasNext(); ) {
			Interval i1 = it1.next();
			for (ListIterator<Interval> it2 = orIntervals.listIterator(it1.nextIndex()); it2.hasNext(); ) {
				Interval i2 = it2.next();
				if (i1.intersects(i2)) {
					// Check if either interval completely encloses the other.
					if (i1.encloses(i2))
						warnings.add(new AssertionWarning(AssertionWarning.Type.ENCLOSING, i2, i1));
					else if (i2.encloses(i1))
						warnings.add(new AssertionWarning(AssertionWarning.Type.ENCLOSING, i1, i2));
					else
						warnings.add(new AssertionWarning(AssertionWarning.Type.INTERSECTING, i1, i2));
				}
			}
		}

		// Check for intersections between exclusion intervals
		for (ListIterator<Interval> it1 = exceptIntervals.listIterator(); it1.hasNext(); ) {
			Interval i1 = it1.next();
			for (ListIterator<Interval> it2 = exceptIntervals.listIterator(it1.nextIndex()); it2.hasNext(); ) {
				Interval i2 = it2.next();
				if (i1.intersects(i2)) {
					// Check if either interval completely encloses the other.
					if (i1.encloses(i2))
						warnings.add(new AssertionWarning(AssertionWarning.Type.ENCLOSING, i2, i1));
					else if (i2.encloses(i1))
						warnings.add(new AssertionWarning(AssertionWarning.Type.ENCLOSING, i1, i2));
					else
						warnings.add(new AssertionWarning(AssertionWarning.Type.INTERSECTING, i1, i2));
				}
			}
		}

		// Check for complete exclusions of intervals and exclusions of nothing.
		for (ListIterator<Interval> it1 = exceptIntervals.listIterator(); it1.hasNext(); ) {
			Interval i1 = it1.next();
			for (ListIterator<Interval> it2 = orIntervals.listIterator(); it2.hasNext(); ) {
				Interval i2 = it2.next();
				if (i1.encloses(i2))
					warnings.add(new AssertionWarning(AssertionWarning.Type.EXCLUDING, i1, i2));
			}
		}

		// Build the MultiInterval
		for (Interval io : orIntervals) {
			intervals.include(io);
		}

		for (Interval ie : exceptIntervals) {
			intervals.exclude(ie);
		}

		// Special case if only "integer" was specified as an assertion
		if (isInteger && orIntervals.isEmpty() && exceptIntervals.isEmpty()) {
			intervals.include(new Interval(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, false, false));
		}
	}

	/**
	 * Indicates wether the assertion is consistent or wether it may contain
	 * some inconsistencies.  If there are inconsistecies the details can be
	 * retrieved by calling <code>getWarnings()</code>.
	 * @return <code>true</code> if there are no inconsitencies, <code>false</code> otherwise.
	 */
	public boolean isConsistent() {
		return warnings.isEmpty();
	}

	/**
	 * Retrieves the warnings associated with this assertion describing possible
	 * inconsitencies in the assertion.
	 * @return a <code>List</code> of <code>AssertionWarning</code> objects describing any
	 * warnings/inconsistencies in the assertion represented by this object. This <code>List</code>
	 * will be non-empty if <code>isConsistent()</code> returns <code>false</code>, and empty otherwise.
	 */
	public List<AssertionWarning> getWarnings() {
		return warnings;
	}

	/**
	 * @return the String representation of the assertion as specified in the
	 *         constructor.
	 */
	public String toString() {
		return assertion;
	}



	/** Used for "pretty printing" an assertion. The assertion string
		is re-constructed from the parsed data.
		@return a pretty printed version of the assertion.
	*/
	public String prettyString()
	{
		return super.toString();
	}

	/** Used to print all warnings that were generated while parsing the assertion.
		Used only for debugging purposes.
		@deprecated printWarnings
	*/
	public void printWarnings()
	{
		List w = getWarnings();
		Iterator i = w.iterator();
		System.out.println("\nWarnings for: " + toString());
		for ( ; i.hasNext() ;)
		{
			AssertionWarning aw = (AssertionWarning)i.next();
			System.out.println(aw.toString());
		}
	}
}
