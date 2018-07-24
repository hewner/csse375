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

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

/** A class representing (possibly) multiple intervals. This can be used to specify
	any combination of values to be included in an interval. The MultiInterval is
	built up by specifying normal intervals to be included or excluded from the
	MultiInterval.
	@author Peter Palotas
	@author Fredrik Johansson
*/
public class MultiInterval implements Iterable<Interval>, Serializable {

	/** The unique version identifier used for serialization */
	private static final long serialVersionUID = 3311313307922046224L;

	/**
		Contains a list of the intervals included in this multi interval.
		<p><b>Invariant:</b> intervalSet does not contain any intersecting or
	 	bordering intervals. (Two bordering intervals are eg. [0 to 1[ [1 to 2]
	 	which could easily be written as one interval [0 to 2].
	*/
	private TreeSet<Interval> intervalSet;

	/** Default constructor. Constructs an empty MultiInterval containing no values. Note that if the
		first operation called on this interval after its construction is <code>exclude()</code>,
		<b>all</b> values <b>except</b> the ones excluded will be included in this interval.
	*/
	public MultiInterval() {
		intervalSet = new TreeSet<Interval>();
	}

	/** Add an interval to be included in this MultiInterval.
		@param interval An interval specifying values to be included in this MultiInterval.
	*/
	public void include(Interval interval) {
		if (intervalSet.isEmpty()) {
			intervalSet.add(interval);
			return;
		}

		// Check if the new interval intersects with any one already included
		for (Iterator<Interval> iter = intervalSet.iterator(); iter.hasNext(); ) {
			Interval i = iter.next();

			// Try to create a union, if successful we should
			Interval union = Interval.union(i, interval);
			if (union != null) {
				// remove the old interval intersecting with the new one and
				// include the union between them instead.
				iter.remove();
				include(union);
				return;
			}
		}

		// The new interval did not intersect with any already included, just add it.
		intervalSet.add(interval);
	}

	/** Exclude a specific interval from this MultiInterval. If this MultiInterval is
		empty (i.e. does not include anything) when this function is called, the MultiInterval
		will accept <i>any value except the ones included in the interval specified</i>.
		@param interval an interval specifying which values to exclude from this MultiInterval.
	*/
	public void exclude(Interval interval) {
		// If list is empty, we assume we want to include all possible values
		// except the ones specified. So we add an interval covering all
		// values to the list.
		if (intervalSet.isEmpty()) {
			intervalSet.add(new Interval(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, false, false));
		}

		TreeSet<Interval> nlist = new TreeSet<Interval>();

		for (Iterator<Interval> iter = intervalSet.iterator(); iter.hasNext(); ) {
			Interval i = iter.next();


			if (i.intersects(interval)) {
				// Check if the lower part of the interval should be included
				if (i.getLowerLimit() < interval.getLowerLimit() || i.getLowerLimit() == interval.getLowerLimit() &&
					i.isLowerLimitClosed() && !interval.isLowerLimitClosed()) {
					nlist.add(new Interval(i.getLowerLimit(), interval.getLowerLimit(), i.isLowerLimitClosed(), !interval.isLowerLimitClosed()));
				}

				// Check if the upper part of the interval should be included
				if (i.getUpperLimit() > interval.getUpperLimit() || i.getUpperLimit() == interval.getUpperLimit() &&
					i.isUpperLimitClosed() && !interval.isUpperLimitClosed()) {
					nlist.add(new Interval(interval.getUpperLimit(), i.getUpperLimit(), !interval.isUpperLimitClosed(), i.isUpperLimitClosed()));
				}

			} else {
				// If the interval to exclude does not intersect the current interval
				// we keep the current interval as is.
				nlist.add(i);
			}
		}
		intervalSet = nlist;
	}

	/** Check if a value is contained in this MultiInterval. <p><b>NOTE:</b> Infinity and NaN is never
		reported to be included in a MultiInterval.
		@param value The value to check for.
		@return <code>true</code> if the value has been specified to be included in this
			MultiInterval, <code>false</code> otherwise.
	*/
	public boolean contains(double value) {

		if (Double.isNaN(value) || Double.isInfinite(value))
			return false;

		for (Iterator<Interval> iter = intervalSet.iterator(); iter.hasNext(); ) {
			Interval i = iter.next();
			if (i.contains(value))
				return true;

			if (i.getUpperLimit() > value)
				return false;
		}
		return false;
	}


	/**
	 * Calculates a negation av a <code>MultiInterval</code>
	 * @param term is the <code>MultiInterval</code> to be negated
	 * @return the negated <code>MultiInterval</code>
	 */
	public static MultiInterval negate(MultiInterval term) {
		MultiInterval negation = new MultiInterval();
		for (Interval i:term.intervalSet)
			negation.include(Interval.negate(i));
		return negation;
	}

	/**
	 * Calculates the sum of two <code>MultiInterval</code>s
	 * @param term1 is the first term
	 * @param term2 is the second term
	 * @return the summarized <code>MultiInterval</code>s
	 */
	public static MultiInterval add(MultiInterval term1, MultiInterval term2) {
		MultiInterval sum = new MultiInterval();
		for (Interval i1:term1.intervalSet)
			for (Interval i2:term2.intervalSet)
				sum.include(Interval.add(i1,i2));
		return sum;
	}

	/**
	 * Calculates the difference of two <code>MultiInterval</code>s
	 * @param term1 is the first term
	 * @param term2 is the second term
	 * @return the difference of the two <code>MultiInterval</code>s
	 */
	public static MultiInterval sub(MultiInterval term1, MultiInterval term2) {
		MultiInterval difference = new MultiInterval();
		for (Interval i1:term1.intervalSet)
			for (Interval i2:term2.intervalSet)
				difference.include(Interval.sub(i1,i2));
		return difference;
	}

	/**
	 * Calculates the product of two <code>MultiInterval</code>s
	 * @param factor1 is the first factor
	 * @param factor2 is the second factor
	 * @return the product of thw two <code>MultiInterval</code>s
	 */
	public static MultiInterval mul(MultiInterval factor1, MultiInterval factor2) {
		MultiInterval product = new MultiInterval();
		for (Interval i1:factor1.intervalSet)
			for (Interval i2:factor2.intervalSet)
				product.include(Interval.mul(i1,i2));
		return product;
	}

	/**
	 * Calculates the quotient of two <code>MultiInterval</code>s
	 * @param numerator is the numerator
	 * @param denominator is the denominator
	 * @return the quotient of the two <code>MultiInterval</code>s
	 */
	public static MultiInterval div(MultiInterval numerator, MultiInterval denominator) throws MathException {
		MultiInterval quotient = new MultiInterval();
		for (Interval i1:numerator.intervalSet)
			for (Interval i2:denominator.intervalSet)
				quotient.include(Interval.div(i1,i2));
		return quotient;
	}


	/**
	 * Calculates the first <code>MultiInterval</code> raised to the power of the second <code>MultiInterval</code>
	 * @param base is the first <code>MultiInterval</code>
	 * @param exponent is the second <code>MultiInterval</code>
	 * @return the first <code>MultiInterval</code> raised to the power of the second <code>MultiInterval</code>
	 * @throws MathException if the first <code>MultiInterval</code> contains values below zero and the second <code>MultiInterval</code> is not build of singel integer value <code>Interval</code>s
	 */
	public static MultiInterval pow(MultiInterval base, MultiInterval exponent) throws MathException {
		MultiInterval product = new MultiInterval();
		for (Interval baseI : base.intervalSet)
			for (Interval expI : exponent.intervalSet)
				product.include(Interval.pow(baseI, expI));
		return product;
	}


	/**
	 * Calculates the cosine <code>MultiInterval</code> of a <code>MultiInterval</code>
	 * @param mInterval is the <code>MultiInterval</code>
	 * @return the cosine <code>MultiInterval</code> of the <code>MultiInterval</code>
	 */
	public static MultiInterval cos(MultiInterval mInterval) {
		MultiInterval result = new MultiInterval();
		for (Interval i : mInterval.intervalSet)
			result.include(Interval.cos(i));
		return result;
	}

	/**
	 * Calculates the sine <code>MultiInterval</code> of a <code>MultiInterval</code>
	 * @param mInterval is the <code>MultiInterval</code>
	 * @return the sine <code>MultiInterval</code> of the <code>MultiInterval</code>
	 */
	public static MultiInterval sin(MultiInterval mInterval) {
		MultiInterval result = new MultiInterval();
		for (Interval i : mInterval.intervalSet)
			result.include(Interval.sin(i));
		return result;
	}

	/**
	 * Calculates the tangent <code>MultiInterval</code> of a <code>MultiInterval</code>
	 * @param mInterval is the <code>MultiInterval</code>
	 * @return the tangent <code>MultiInterval</code> of the <code>MultiInterval</code>
	 */
	public static MultiInterval tan(MultiInterval mInterval) {
		MultiInterval result = new MultiInterval();
		for (Interval i : mInterval.intervalSet)
			result.include(Interval.tan(i));
		return result;
	}

	/**
	 * Calculates the natural logarithm of a <code>MultiInterval</code>
	 * @param mInterval is the <code>MultiInterval</code>
	 * @return the natural logarithm of the <code>MultiInterval</code>
	 * @throws MathException if the <code>MultiInterval</code> contains values below zero
	 */

	public static MultiInterval ln(MultiInterval mInterval) throws MathException {
		MultiInterval log = new MultiInterval();
		for (Interval i:mInterval.intervalSet)
			log.include(Interval.ln(i));
		return log;
	}

	/**
	 * Calculates the base 10 logarithm of a <code>MultiInterval</code>
	 * @param mInterval is the <code>MultiInterval</code>
	 * @return the base 10 logarithm of the <code>MultiInterval</code>
	 * @throws MathException if the <code>MultiInterval</code> contains values below zero
	 */
	public static MultiInterval log10(MultiInterval mInterval) throws MathException {
		MultiInterval log = new MultiInterval();
		for (Interval i : mInterval.intervalSet)
			log.include(Interval.log10(i));
		return log;
	}

	/**
	 * Calculates Eulers number e raised to a <code>MultiInterval</code>
	 * @param exponent is the <code>MultiInterval</code>
	 * @return Eulers number e raised to the <code>MultiInterval</code>
	 */
	public static MultiInterval exp(MultiInterval exponent) {
		MultiInterval exp = new MultiInterval();
		for (Interval i : exponent.intervalSet)
			exp.include(Interval.exp(i));
		return exp;
	}

	/**
	 * Calculates the square root of a <code>MultiInterval</code>
	 * @param mInterval is the <code>MultiInterval</code>
	 * @return the square root of the <code>MultiInterval</code>
	 * @throws MathException if the <code>MultiInterval</code> contains values below zero
	 */
	public static MultiInterval sqrt(MultiInterval mInterval) throws MathException {
		MultiInterval root = new MultiInterval();
		for (Interval i : mInterval.intervalSet)
			root.include(Interval.sqrt(i));
		return root;
	}

	/**
	 * Calculates the <code>MultiInterval</code> you get if you convert all values from <code>double</code> to <code>int</code>
	 * @param mInterval is the <code>MultiInterval</code>
	 * @return the <code>MultiInterval</code> you get if you convert the values from <code>double</code> to <code>int</code>
	 */
	public static MultiInterval toInt(MultiInterval mInterval) {
		MultiInterval result = new MultiInterval();
		for (Interval i : mInterval.intervalSet)
			result.include(Interval.toInt(i));
		return result;
	}

	/**
	 * Calculates the absolute value <code>MultiInterval</code> of a <code>MultiInterval</code>
	 * @param mInterval is the <code>MultiInterval</code>
	 * @return the absolute value <code>MultiInterval</code> of the <code>MultiInterval</code>
	 */
	public static MultiInterval abs(MultiInterval mInterval) {
		MultiInterval result = new MultiInterval();
		for (Interval i : mInterval.intervalSet)
			result.include(Interval.abs(i));
		return result;
	}

	/**
	 * Returns a <code>MultiInterval</code> holding all possible values you get from the <code>Math.random()</code> method
	 * @return a <code>MultiInterval</code> holding all possible values you get from the <code>Math.random()</code> mathod
	 */
	public static MultiInterval rand() {
		MultiInterval result = new MultiInterval();
		result.include(Interval.rand());
		return result;
	}

	/**
	 * Calculates the factorial of a <code>MultiInterval</code>
	 * @param mInterval is the <code>MultiInterval</code>
	 * @return the factorial of the <code>MultiInterval</code>
	 * @throws MathException if the <code>MultiInterval</code> contains values below zero
	 */
	public static MultiInterval fact(MultiInterval mInterval) throws MathException {
		MultiInterval result = new MultiInterval();
		for (Interval i : mInterval.intervalSet)
			result.include(Interval.fact(i));
		return result;
	}

	/**
	 * Calculates the sum of a <code>List</code> of <code>MultiInterval</code>s
	 * @param terms is the terms to be summarized
	 * @return the sum of the <code>MultiInterval</code>s
	 */
	public static MultiInterval sum(List<MultiInterval> terms) {
		MultiInterval sum = new MultiInterval();
		Iterator<MultiInterval> iter = terms.iterator();
		if (iter.hasNext())
			sum = iter.next().clone();
		while (iter.hasNext()) {
			sum = MultiInterval.add(sum,iter.next());
		}
		return sum;
	}

	/**
	 * Calculates the average of a <code>List</code> of <code>MultiInterval</code>s
	 * @param terms is the terms
	 * @return the average of the <code>MultiInterval</code>s
	 */
	public static MultiInterval avg(List<MultiInterval> terms) {
		MultiInterval size = new MultiInterval();
		size.include(new Interval(terms.size()));
		MultiInterval sum = MultiInterval.sum(terms);
		MultiInterval result = new MultiInterval(); // for the compiler to be happy
		try {
			result = MultiInterval.div(sum,size);
		} catch (MathException e) { // should never happen
			e.printStackTrace();
		}
		return result;
	}


	/** Returns an iterator over the intervals in this MultiInterval in proper sequence.
		Note that this iterator cannot be used to modify the MultiInterval.
		@return an iterator over the intervals in this MultiInterval in proper sequence.
	*/
	public Iterator<Interval> iterator() {
		return new ConstMultiIntervalIterator(intervalSet.iterator());
	}

	/** Compares the specified MultiInterval with this MultiInterval for equality.
		@return <code>true</code> if the two intervals represent the same ranges of values,
				<code>false</code> otherwise.
	*/
	public boolean equals(Object o) {
		if (o == null)
			return false;

		if (!(o instanceof MultiInterval))
			return false;

		MultiInterval mi = (MultiInterval)o;

		return intervalSet.equals(mi.intervalSet);
	}

	/**
	 * Makes a copy of this <code>MultiInterval</code> intance
	 */
	public MultiInterval clone() {
		MultiInterval newMI = new MultiInterval();
		for (Interval i:intervalSet)
			newMI.include(i);
		return newMI;
	}


	/** Returns a string representation of this MultiInterval. */
	public String toString() {
		String s = "";

		for (Iterator<Interval> iter = intervalSet.iterator(); iter.hasNext(); ) {
			Interval i = iter.next();
			s += i.toString();
			if (iter.hasNext())
				s += " or ";
		}
		return s;
	}
}