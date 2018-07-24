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
import java.util.List;
import java.util.TreeSet;

/**
 * This class represents a non-empty interval.
 *
 * A single number can be represented by setting <code>lowerLimit == upperLimit</code>
 *
 * A one sided interval is represented by either setting <code>upperLimit</code>
 * to <code>Double.POSITIVE_INFINITY</code> or <code>lowerLimit</code> to <code>Double.NEGATIVE_INFINITY</code>.
 *
 * @author Fredrik Johansson
 * @author Peter Palotas
 */
public class Interval implements Comparable<Interval>, Cloneable, Serializable {

	/** The unique version identifier used for serialization */
	private static final long serialVersionUID = -1176661760834511074L;

	/** The lower limit of the interval */
	private double lowerLimit;

	/** The upper limit of the interval */
	private double upperLimit;

	/** Denotes wether the interval is closed at its lower limit or not */
	private boolean lowerLimitClosed;

	/** Denotes wether the interval is closed at its upper limit or not */
	private boolean upperLimitClosed;

	/** Constructs a new instance of Interval.
		@param lowerLimit The value of the lower limit of this interval. This must be less than or equal to <code>upperLimit</code>.
		@param upperLimit The value of the upper limit of this interval. This must be greater than or equal to <code>lowerLimit</code>.
		@param lowerLimitClosed if <code>true</code> the value specified by <code>lowerLimit</code> will
			   be included in the interval, otherwise it will not be. Note that this <i>must</i> be <code>false</code> if
			   <code>lowerLimit</code> is infinite.
		@param upperLimitClosed if <code>true</code> the value specified by <code>upperLimit</code> will
			   be included in the interval, otherwise it will not be. Note that this <i>must</i> be <code>false</code> if
			   <code>upperLimit</code> is infinite.
		@throws IllegalArgumentException if illegal limits were passed. Limits must not be <code>Double.NaN</code>and
			   <code>lowerLimit</code> must be less than or equal to <code>upperLimit</code>.
	*/
	public Interval(double lowerLimit, double upperLimit,
			boolean lowerLimitClosed, boolean upperLimitClosed)
			throws IllegalArgumentException {

		if (lowerLimit > upperLimit)
			throw new IllegalArgumentException("Lower limit of interval must not be larger than upper limit");

		if (Double.isNaN(lowerLimit) ||
			Double.isNaN(upperLimit))
				throw new IllegalArgumentException("An interval limit must not be NaN");

		if (Double.isInfinite(lowerLimit) && lowerLimitClosed || Double.isInfinite(upperLimit) && upperLimitClosed) {
			throw new IllegalArgumentException("A limit that is infinite cannot be closed");
		}

		this.lowerLimit = lowerLimit;
		this.upperLimit = upperLimit;
		this.lowerLimitClosed = lowerLimitClosed;
		this.upperLimitClosed = upperLimitClosed;
	}

	/** Constructs a new instance of Interval containing a single value only.
		@param constant The single value this Interval should contain.
		@throws IllegalArgumentException if <code>constant</code> is <code>Double.NaN</code> or infinite.
	*/
	public Interval(double constant) throws IllegalArgumentException {
		if (Double.isNaN(constant))
			throw new IllegalArgumentException("An interval limit must not be NaN");

		if (Double.isInfinite(constant))
			throw new IllegalArgumentException("A single value interval must not be infinity");

		this.lowerLimit = this.upperLimit = constant;
		this.lowerLimitClosed = this.upperLimitClosed = true;
	}

	/** Return the lower limit of this Interval.
		<B>NOTE!</B> If <code>isLowerLimitClosed()</code> returns <code>false</code>,
		the value returned by this function is <I>NOT</I> part of the interval.
		@return the lower limit of this Interval.
	*/
	public double getLowerLimit() {
		return lowerLimit;
	}

	/** Return the upper limit of this Interval.
		<B>NOTE!</B> If <code>isUpperLimitClosed()</code> returns <code>false</code>,
		the value returned by this function is <I>NOT</I> part of the interval.
		@return the lower limit of this Interval.
	*/
	public double getUpperLimit() {
		return upperLimit;
	}

	/** Indicates if this interval is closed at its lower limit or not (i.e. wether
		the value returned by <code>getLowerLimit()</code> is included in the interval
		or not).
		@return <code>true</code> if the value returned by <code>getLowerLimit()</code>
				is included in the Interval, <code>false</code> otherwise.
	*/
	public boolean isLowerLimitClosed() {
		return lowerLimitClosed;
	}

	/** Indicates if this interval is closed at its upper limit or not (i.e. wether
		the value returned by <code>getUpperLimit()</code> is included in the interval
		or not).
		@return <code>true</code> if the value returned by <code>getUpperLimit()</code>
				is included in the Interval, <code>false</code> otherwise.
	*/
	public boolean isUpperLimitClosed() {
		return upperLimitClosed;
	}

	/** Indicates wether the specified value is contained in this Interval or not.
		@param value the value to check against this Interval.
		@return <code>true</code> if <code>value</code> is contained in this Interval,
				<code>false</code> otherwise.
	*/
	public boolean contains(double value) {
		return (value > lowerLimit && value < upperLimit) ||
			(lowerLimitClosed && value == lowerLimit) ||
			(upperLimitClosed && value == upperLimit);
	}


	/** Indicates wether this interval intersects with another interval.
		@param interval The interval to check this one against.
		@return <code>true</code> if the intervals do intersect, <code>false</code> otherwise.
	*/
	public boolean intersects(Interval interval) {
		if (interval == null)
			return false;

		if (interval == this)
			return true;

		if (equals(interval))
			return true;

		// We need a special check if the intervals are adjacent and atleast
		// one of them are open.
		if (upperLimit == interval.lowerLimit &&
			(!upperLimitClosed || !interval.lowerLimitClosed)) {
			return false;
		}
		else if (interval.upperLimit == lowerLimit &&
			(!interval.upperLimitClosed || !lowerLimitClosed)) {
			return false;
		}

		return contains(interval.getLowerLimit()) ||
			   contains(interval.getUpperLimit()) ||
			   interval.contains(getLowerLimit()) ||
			   interval.contains(getUpperLimit());
	}

	/** Indicates wether this Interval fully encloses another interval.
		@param interval The interval to check wether it is enclosed in this interval or not.
		@return <code>true</code> if <code>interval</code> is fully enclosed within this Interval,
				<code>false</code> otherwise.
	*/
	public boolean encloses(Interval interval) {
		return contains(interval.lowerLimit) &&
			   contains(interval.upperLimit);
	}

	public boolean equals(Object obj) {
		if (obj == null)
			return false;

		if (!(obj instanceof Interval))
			return false;

		Interval i = (Interval)obj;
		return (lowerLimit == i.lowerLimit) && (upperLimit == i.upperLimit) && (lowerLimitClosed == i.lowerLimitClosed) && (upperLimitClosed == i.upperLimitClosed);
	}


	/** Compares two intervals, by essentially comparing their lower limit.
		If lower limits are equal (as well as isLowerLimitClosed()),
		the upper limits are compared.
		@param i the interval to compare this interval to.
		@return A negative value if the lower limit of this interval is less than
				that of the compared one, zero (0) if the two intervals are equal,
				and a positive value if the lower limit of this interval is greater
				than that of the compared one. */
	public int compareTo(Interval i) {
		if (lowerLimit < i.lowerLimit)
			return -1;

		if (lowerLimit > i.lowerLimit)
			return 1;

		// Lower limit are equal
		if (lowerLimitClosed && !i.lowerLimitClosed)
			return -1;

		if (!lowerLimitClosed && i.lowerLimitClosed)
			return 1;

		// Lower limits and closure are equal, comparing upper limits.
		if (upperLimit < i.upperLimit)
			return -1;

		if (upperLimit > i.upperLimit)
			return 1;

		// Upper limits are equal
		if (upperLimitClosed && !i.upperLimitClosed)
			return 1;

		if (!upperLimitClosed && i.upperLimitClosed)
			return -1;

		// Intervals are indeed equal
		return 0;
	}

	/** Returns the union of two intersecting or bordering intervals.
		@param i1 An interval
		@param i2 An interval intersecting or bordering <code>i1</code>.
		@return A new interval containing the union of the the two intervals specified, or <code>null</code> if
				the two intervals cannot be unioned into a single interval.
	*/
	public static Interval union(Interval i1, Interval i2) {
		if (i1.intersects(i2)) {
			// Order the intervals
			if (i1.compareTo(i2) > 0) {
				Interval temp = i1;
				i1 = i2;
				i2 = temp;
			}

			double llimit = i1.lowerLimit;
			boolean llimitclosed = i1.lowerLimitClosed;

			double ulimit;
			boolean ulimitclosed;

			if (i1.upperLimit > i2.upperLimit) {
				ulimit = i1.upperLimit;
				ulimitclosed = i1.upperLimitClosed;
			} else if (i1.upperLimit < i2.upperLimit) {
				ulimit = i2.upperLimit;
				ulimitclosed = i2.upperLimitClosed;
			} else {
				ulimit = i1.upperLimit;
				ulimitclosed = (i1.upperLimitClosed || i2.upperLimitClosed);
			}

			return new Interval(llimit, ulimit, llimitclosed, ulimitclosed);
		} else if (i1.lowerLimit == i2.upperLimit && (i1.lowerLimitClosed || i2.upperLimitClosed)
				   || i1.upperLimit == i2.lowerLimit && (i1.upperLimitClosed || i2.lowerLimitClosed)) {
			if (i2.upperLimit == i1.lowerLimit) {
				Interval temp = i2;
				i2 = i1;
				i1 = temp;
			}

			return new Interval(i1.lowerLimit, i2.upperLimit, i1.lowerLimitClosed, i2.upperLimitClosed);
		} else {
			return null;
		}
	}

	/**
	 * Calculates the negation of an interval
	 * @param interval is the interval
	 * @return the negated interval
	 */
	public static Interval negate(Interval interval) {
		return new Interval(-interval.upperLimit, -interval.lowerLimit,
							interval.upperLimitClosed, interval.lowerLimitClosed);
	}

	/**
	 * Calculates the sum of two intervals
	 * @param interval1 is the first interval
	 * @param interval2 is the second interval
	 * @return an interval describing the sum of the two intervals
	 */
	public static Interval add(Interval interval1, Interval interval2) {
		boolean lowerLimitClosed =
			interval1.lowerLimitClosed && interval2.lowerLimitClosed;
		boolean upperLimitClosed =
			interval1.upperLimitClosed && interval2.upperLimitClosed;
		return new Interval(interval1.lowerLimit+interval2.lowerLimit,
							interval1.upperLimit+interval2.upperLimit,
							lowerLimitClosed, upperLimitClosed);
	}

	/**
	 * Calculates the difference between two intervals
	 * @param interval1 is the first interval
	 * @param interval2 is the second interval
	 * @return an interval describing the difference between the two intervals
	 */
	public static Interval sub(Interval interval1, Interval interval2) {
		boolean lowerLimitClosed =
			interval1.lowerLimitClosed && interval2.upperLimitClosed;
		boolean upperLimitClosed =
			interval1.upperLimitClosed && interval2.lowerLimitClosed;

		return new Interval(interval1.lowerLimit-interval2.upperLimit,
							interval1.upperLimit-interval2.lowerLimit,
							lowerLimitClosed, upperLimitClosed);
	}


	/**
	 * Calculates the product of two intervals
	 * @param interval1 is the first interval
	 * @param interval2 is the second interval
	 * @return an interval describing the product of the two intervals
	 */
	public static Interval mul(Interval interval1, Interval interval2) {
		TreeSet<Double> valuesClosed = new TreeSet<Double>();
		TreeSet<Double> valuesOpen = new TreeSet<Double>();

		double value = interval1.lowerLimit * interval2.lowerLimit;
		if (interval1.lowerLimitClosed && interval2.lowerLimitClosed)
			valuesClosed.add(value);
		else
			valuesOpen.add(value);

		value = interval1.lowerLimit * interval2.upperLimit;
		if (interval1.lowerLimitClosed && interval2.upperLimitClosed)
			valuesClosed.add(value);
		else
			valuesOpen.add(value);

		value = interval1.upperLimit * interval2.lowerLimit;
		if (interval1.upperLimitClosed && interval2.lowerLimitClosed)
			valuesClosed.add(value);
		else
			valuesOpen.add(value);

		value = interval1.upperLimit * interval2.upperLimit;
		if (interval1.upperLimitClosed && interval2.upperLimitClosed)
			valuesClosed.add(value);
		else
			valuesOpen.add(value);

		double lowerLimit, upperLimit;
		boolean lowerLimitClosed, upperLimitClosed;

		if (valuesClosed.size() > 0 && valuesOpen.size() > 0) {
			if (valuesClosed.first()<=valuesOpen.first()) {
				lowerLimit = valuesClosed.first();
				lowerLimitClosed = true;
			} else {
				lowerLimit = valuesOpen.first();
				lowerLimitClosed = false;
			}
			if (valuesClosed.last()>=valuesOpen.last()) {
				upperLimit = valuesClosed.last();
				upperLimitClosed = true;
			} else {
				upperLimit = valuesOpen.last();
				upperLimitClosed = false;
			}
		} else {
			if (valuesClosed.size() > 0) {
				lowerLimitClosed = true;
				upperLimitClosed = true;
				lowerLimit = valuesClosed.first();
				upperLimit = valuesClosed.last();
			} else {
				lowerLimitClosed = false;
				upperLimitClosed = false;
				lowerLimit = valuesOpen.first();
				upperLimit = valuesOpen.last();
			}
		}

		return new Interval(lowerLimit, upperLimit, lowerLimitClosed, upperLimitClosed);
	}


	/** Calculates the cosine interval of an interval.
		@return the interval corresponding to the possible values of running cosine on the values of this interval.
	*/
	public static Interval cos(Interval interval) {
		if (interval.size() > 2 * Math.PI) {
			return new Interval(-1, 1, true, true);
		}

		double llimit = interval.lowerLimit % (Math.PI * 2);
		double ulimit = interval.upperLimit % (Math.PI * 2);
		boolean llimc = interval.lowerLimitClosed;
		boolean ulimc = interval.upperLimitClosed;
		if (llimit > ulimit) {
			llimit-=Math.PI*2;
		}

		Interval tmp = new Interval(llimit, ulimit, llimc, ulimc);

		if (tmp.lowerLimit >= Math.PI)
			return Interval.negate(Interval.cos(Interval.sub(interval, new Interval(Math.PI))));

		if (tmp.lowerLimit < 0 && tmp.upperLimit < 0) {
			Interval vv = new Interval(Math.PI * 2 * Math.ceil(-tmp.lowerLimit));
			Interval qq = Interval.add(vv, tmp);
			return Interval.cos(qq);
		}

		if (tmp.lowerLimit < 0 && tmp.upperLimit >= 0) {
			Interval neg = new Interval(0, -tmp.lowerLimit, false, tmp.lowerLimitClosed);
			Interval pos = new Interval(0, tmp.upperLimit, true, tmp.upperLimitClosed);
			return Interval.union(Interval.cos(neg), Interval.cos(pos));
		}
		
		double l = tmp.lowerLimit;
		double u = tmp.upperLimit;

		if (u <= Math.PI) {
			return new Interval(Math.cos(u), Math.cos(l), tmp.upperLimitClosed, tmp.lowerLimitClosed); }
		else if (u <= (Math.PI * 2))
			return new Interval(-1, Math.cos(Math.min((Math.PI * 2) - u, l)), true, ((Math.PI * 2) - u < l) ? interval.upperLimitClosed : interval.lowerLimitClosed);
		else
			return new Interval(-1, 1, true, true);
	}
	
	/** Calculates the sine interval of an interval.
		@return the interval corresponding to the possible values of running sine on the values of this interval.
	*/
	public static Interval sin(Interval interval) {
		Interval tmp = Interval.sub(interval, new Interval(Math.PI / 2));
		return Interval.cos(tmp);
	}

	public static Interval tan(Interval interval) throws MathException {
		Interval s = Interval.sin(interval);
		Interval c = Interval.cos(interval);
		try {
			Interval d = Interval.div(s,c);
			return d;
		} catch (MathException e) {
			throw new MathException("Illegal tan value condition");
		}
		//System.out.println("tan(" + interval + ") = " + s + " / " + c + " = " + d);

	}

	/**
	 * Calculates the quotient of two intervals
	 * @param interval1 is the first interval
	 * @param interval2 is the second interval
	 * @return an interval describing the quotient of the two intervals
	 * @throws ArithmeticException if the second interval contains the value 0. Can't divide by zero.
	 */
	public static Interval div(Interval interval1, Interval interval2) throws MathException {
		if (interval2.contains(0.0))
			throw new MathException("Division by zero condition");

		TreeSet<Double> valuesOpen = new TreeSet<Double>();
		TreeSet<Double> valuesClosed = new TreeSet<Double>();
		double value;

		if (interval2.lowerLimit != Double.NEGATIVE_INFINITY) {
			if (interval2.lowerLimit!=0) {
				value = interval1.lowerLimit / interval2.lowerLimit;
				if (interval1.lowerLimitClosed && interval2.lowerLimitClosed)
					valuesClosed.add(value);
				else
					valuesOpen.add(value);
				value = interval1.upperLimit / interval2.lowerLimit;
				if (interval1.upperLimitClosed && interval2.lowerLimitClosed)
					valuesClosed.add(value);
				else
					valuesOpen.add(value);
			}
			else { // we now know that the lower limit of interval2 is positive
				if (interval1.lowerLimit < 0)
					valuesOpen.add(Double.NEGATIVE_INFINITY);
				if (interval1.upperLimit > 0)
					valuesOpen.add(Double.POSITIVE_INFINITY);
			}
		} else {
			valuesOpen.add(0.0);
		}

		if (interval2.upperLimit != Double.POSITIVE_INFINITY) {
			if (interval2.upperLimit!=0) {
				value = interval1.lowerLimit / interval2.upperLimit;
				if (interval1.lowerLimitClosed && interval2.upperLimitClosed)
					valuesClosed.add(value);
				else
					valuesOpen.add(value);
				value = interval1.upperLimit / interval2.upperLimit;
				if (interval1.upperLimitClosed && interval2.upperLimitClosed)
					valuesClosed.add(value);
				else
					valuesOpen.add(value);
			}
			else { // we now know that the upper limit of interval2 is negative
				if (interval1.lowerLimit < 0)
					valuesOpen.add(Double.POSITIVE_INFINITY);
				if (interval1.upperLimit > 0)
					valuesOpen.add(Double.NEGATIVE_INFINITY);
			}
		} else {
			valuesOpen.add(0.0);
		}

		double lowerLimit, upperLimit;
		boolean lowerLimitClosed, upperLimitClosed;

		if (valuesClosed.size() > 0 && valuesOpen.size() > 0) {
			if (valuesClosed.first()<=valuesOpen.first()) {
				lowerLimit = valuesClosed.first();
				lowerLimitClosed = true;
			} else {
				lowerLimit = valuesOpen.first();
				lowerLimitClosed = false;
			}
			if (valuesClosed.last()>=valuesOpen.last()) {
				upperLimit = valuesClosed.last();
				upperLimitClosed = true;
			} else {
				upperLimit = valuesOpen.last();
				upperLimitClosed = false;
			}
		} else {
			if (valuesClosed.size() > 0) {
				lowerLimitClosed = true;
				upperLimitClosed = true;
				lowerLimit = valuesClosed.first();
				upperLimit = valuesClosed.last();
			} else {
				lowerLimitClosed = false;
				upperLimitClosed = false;
				lowerLimit = valuesOpen.first();
				upperLimit = valuesOpen.last();
			}
		}

		return new Interval(lowerLimit, upperLimit, lowerLimitClosed, upperLimitClosed);
	}


	/**
	 * Calculates the first <code>Interval raised to the power of the second <code>Interval</code>
	 * @param base is the first <code>Interval</code>
	 * @param exponent is the second <code>Interval</code>
	 * @return the first <code>Interval</code> raised to the power of the second <code>Interval</code>
	 * @throws MathException if the first <code>Interval</code> contains values below zero and the second <code>Interval</code> is not a singel integer value
	 */
	public static Interval pow(Interval base, Interval exponent) throws MathException {
		if (base.lowerLimit < 0 && Math.ceil(exponent.lowerLimit) != Math.floor(exponent.upperLimit))
			throw new MathException("Negative number raised to a non integer condition");

		TreeSet<Double> valuesOpen = new TreeSet<Double>();
		TreeSet<Double> valuesClosed = new TreeSet<Double>();
		if (base.lowerLimitClosed && exponent.lowerLimitClosed)
			valuesClosed.add(Math.pow(base.lowerLimit, exponent.lowerLimit));
		else
			valuesOpen.add(Math.pow(base.lowerLimit, exponent.lowerLimit));

		if (base.lowerLimitClosed && exponent.upperLimitClosed)
			valuesClosed.add(Math.pow(base.lowerLimit, exponent.upperLimit));
		else
			valuesOpen.add(Math.pow(base.lowerLimit, exponent.upperLimit));

		if (base.upperLimitClosed && exponent.lowerLimitClosed)
			valuesClosed.add(Math.pow(base.upperLimit, exponent.lowerLimit));
		else
			valuesOpen.add(Math.pow(base.upperLimit, exponent.lowerLimit));

		if (base.upperLimitClosed && exponent.upperLimitClosed)
			valuesClosed.add(Math.pow(base.upperLimit, exponent.upperLimit));
		else
			valuesOpen.add(Math.pow(base.upperLimit, exponent.upperLimit));

		if (base.contains(0))
			valuesClosed.add(0.0);


		double lowerLimit, upperLimit;
		boolean lowerLimitClosed, upperLimitClosed;

		if (valuesClosed.size() > 0 && valuesOpen.size() > 0) {
			if (valuesClosed.first()<=valuesOpen.first()) {
				lowerLimit = valuesClosed.first();
				lowerLimitClosed = true;
			} else {
				lowerLimit = valuesOpen.first();
				lowerLimitClosed = false;
			}
			if (valuesClosed.last()>=valuesOpen.last()) {
				upperLimit = valuesClosed.last();
				upperLimitClosed = true;
			} else {
				upperLimit = valuesOpen.last();
				upperLimitClosed = false;
			}
		} else {
			if (valuesClosed.size() > 0) {
				lowerLimitClosed = true;
				upperLimitClosed = true;
				lowerLimit = valuesClosed.first();
				upperLimit = valuesClosed.last();
			} else {
				lowerLimitClosed = false;
				upperLimitClosed = false;
				lowerLimit = valuesOpen.first();
				upperLimit = valuesOpen.last();
			}
		}

		return new Interval(lowerLimit, upperLimit, lowerLimitClosed, upperLimitClosed);
	}


	/**
	 * Calculates the natural logarithm for an <code>Interval</code>
	 * @param interval is the <code>Interval</code>
	 * @return the natural logarithm for the <code>Interval</code>
	 * @throws MathException if the <code>Interval</code> contains values below zero
	 */
	public static Interval ln(Interval interval) throws MathException {
		if (interval.lowerLimit<0 || interval.contains(0.0))
			throw new MathException("Natural logarithm on a value below zero condition");

		double lowerLimit = Math.log(interval.lowerLimit);
		double upperLimit = Math.log(interval.upperLimit);
		boolean lowerLimitClosed, upperLimitClosed;

		if (lowerLimit==Double.NEGATIVE_INFINITY)
			lowerLimitClosed = false;
		else
			lowerLimitClosed = interval.lowerLimitClosed;

		// if upperLimit == Double.NEGATIVE_INFINITY then interval.upperLimitClosed must be false
		// so we don't have to check for that condition.
		upperLimitClosed = interval.upperLimitClosed;

		return new Interval(lowerLimit, upperLimit, lowerLimitClosed, upperLimitClosed);
	}

	/**
	 * Calculates the base 10 logarithm of an <code>Interval</code>
	 * @param interval is the <code>Interval</code>
	 * @return the base 10 logarithm of the <code>Interval</code>
	 * @throws MathException if the <code>Interval</code> contains values below zero
	 */
	public static Interval log10(Interval interval) throws MathException {
		if (interval.lowerLimit<0 || interval.contains(0.0))
			throw new MathException("Logarithm on a value below zero condition");

		double lowerLimit = Math.log10(interval.lowerLimit);
		double upperLimit = Math.log10(interval.upperLimit);
		boolean lowerLimitClosed, upperLimitClosed;

		if (lowerLimit==Double.NEGATIVE_INFINITY)
			lowerLimitClosed = false;
		else
			lowerLimitClosed = interval.lowerLimitClosed;

		// if upperLimit == Double.NEGATIVE_INFINITY then interval.upperLimitClosed must be false
		// so we don't have to check for that condition.
		upperLimitClosed = interval.upperLimitClosed;

		return new Interval(lowerLimit, upperLimit, lowerLimitClosed, upperLimitClosed);
	}

	/**
	 * Calculates Eulers number e raised to an <code>Interval</code>
	 * @param interval is the <code>Interval</code>
	 * @return Eulers number e raised to the <code>Interval</code>
	 */
	public static Interval exp(Interval interval) {
		double lowerLimit = Math.exp(interval.lowerLimit);
		double upperLimit = Math.exp(interval.upperLimit);
		boolean lowerLimitClosed, upperLimitClosed;
		if (upperLimit == Double.POSITIVE_INFINITY)
			upperLimitClosed = false;
		else
			upperLimitClosed = interval.upperLimitClosed;
		lowerLimitClosed = interval.lowerLimitClosed;
		return new Interval(lowerLimit, upperLimit, lowerLimitClosed, upperLimitClosed);
	}

	/**
	 * Calculates the square root of an <code>Interval</code>
	 * @param interval is the <code>Interval</code>
	 * @return the square root of the <code>Interval</code>
	 * @throws MathException if the <code>Interval</code> contains values below zero
	 */
	public static Interval sqrt(Interval interval) throws MathException {
		if (interval.lowerLimit < 0)
			throw new MathException("Square root of a value below zero condition");
		return new Interval(Math.sqrt(interval.lowerLimit), Math.sqrt(interval.upperLimit),
				interval.lowerLimitClosed, interval.upperLimitClosed);
	}

	/**
	 * Calculates the <code>Interval</code> you get if you convert the values from <code>double</code> to <code>int</code>
	 * @param interval is the <code>Interval</code>
	 * @return the <code>Interval</code> you get if you convert the values from <code>double</code> to <code>int</code>
	 */
	public static Interval toInt(Interval interval) {
		double lowerLimit, upperLimit;
		if (interval.lowerLimit < 0 &&
			Math.ceil(interval.lowerLimit) == Math.floor(interval.lowerLimit) &&
			!interval.lowerLimitClosed)
				lowerLimit = ((int)interval.lowerLimit) + 1;
		else
			lowerLimit = (int)interval.lowerLimit;

		if (interval.upperLimit > 0 &&
			Math.ceil(interval.upperLimit) == Math.floor(interval.upperLimit) &&
			!interval.upperLimitClosed)
				upperLimit = ((int)interval.upperLimit) - 1;
		else
			upperLimit = (int)interval.upperLimit;

		return new Interval(lowerLimit, upperLimit, true, true);
	}


	/**
	 * Calculates the absolute value <code>Interval</code> of an <code>Interval</code>
	 * @param interval is the <code>Interval</code>
	 * @return the absolute value <code>Interval</code> of the <code>Interval</code>
	 */
	public static Interval abs(Interval interval) {
		boolean lowerLimitClosed, upperLimitClosed;
		double lowerLimit, upperLimit;
		double lowerAbs = Math.abs(interval.lowerLimit);
		double upperAbs = Math.abs(interval.upperLimit);
		if (interval.contains(0)) {
			lowerLimit = 0;
			lowerLimitClosed = true;
			if (lowerAbs<upperAbs) {
				upperLimit = upperAbs;
				upperLimitClosed = interval.upperLimitClosed;
			} else if (lowerAbs>upperAbs) {
				upperLimit = lowerAbs;
				upperLimitClosed = interval.lowerLimitClosed;
			} else { // lowerAbs == upperAbs
				upperLimit = upperAbs;
				upperLimitClosed = interval.lowerLimitClosed || interval.upperLimitClosed;
			}
		} else {
			if (lowerAbs<upperAbs) {
				lowerLimit = lowerAbs;
				upperLimit = upperAbs;
				lowerLimitClosed = interval.lowerLimitClosed;
				upperLimitClosed = interval.upperLimitClosed;
			} else {
				lowerLimit = upperAbs;
				upperLimit = lowerAbs;
				lowerLimitClosed = interval.upperLimitClosed;
				upperLimitClosed = interval.lowerLimitClosed;
			}
		}

		return new Interval(lowerLimit, upperLimit, lowerLimitClosed, upperLimitClosed);
	}

	/**
	 * Returns an <code>Interval</code> holding all possible values you get from the <code>Math.random()</code> method
	 * @return an <code>Interval</code> holding all possible values you get from the <code>Math.random()</code> mathod
	 */
	public static Interval rand() {
		return new Interval(0, 1, true, false);
	}

	/**
	 * Calculates the factorial of an <code>Interval</code>
	 * @param interval is the <code>Interval</code>
	 * @return the factorial of the <code>Interval</code>
	 * @throws MathException if the <code>Interval</code> contains values below zero
	 */
	public static Interval fact(Interval interval) throws MathException {
		if (interval.lowerLimit<0)
			throw new MathException("Faculty on a value below zero condition");

		double lowerLimit = fac(interval.lowerLimit);
		double upperLimit;
		if (!interval.upperLimitClosed && Math.floor(interval.upperLimit) == Math.ceil(interval.upperLimit))
			upperLimit = fac(interval.upperLimit-1);
		else
			upperLimit = fac(interval.upperLimit);

		return new Interval(lowerLimit, upperLimit, true, true);
	}

	/**
	 * Calculates the sum of a <code>List</code> of intervals
	 * @param intervals is the intervals to be summarized
	 * @return an <code>Interval</code> describing the sum of the intervals
	 */
	public static Interval sum(List<Interval> intervals) {
		double lowerLimit = 0;
		double upperLimit = 0;
		boolean lowerLimitClosed = true;
		boolean upperLimitClosed = true;
		for (Interval interval:intervals) {
			lowerLimit+=interval.lowerLimit;
			upperLimit+=interval.upperLimit;
			lowerLimitClosed&=interval.lowerLimitClosed;
			upperLimitClosed&=interval.upperLimitClosed;
		}
		return new Interval(lowerLimit, upperLimit, lowerLimitClosed, upperLimitClosed);
	}

	/**
	 * Calculates the average interval for a <code>List</code> of intervals
	 * @param intervals is the intervals to calculate the average for
	 * @return an <code>Interval</code> describing the average of the intervals
	 */
	public static Interval avg(List<Interval> intervals) {
		Interval result = new Interval(0); // for the compiler to be happy
		try {
			result = div(sum(intervals), new Interval(intervals.size()));
		} catch (MathException e) {
			e.printStackTrace(); // should never happen
		}
		return result;
	}

	public Interval clone() {
		return new Interval(lowerLimit, upperLimit, lowerLimitClosed, upperLimitClosed);
	}


	/**
	  Retrieve the size of this interval.
	  @return the size (or width) of this interval.
	*/
	public double size() {
		double clow = lowerLimitClosed ? lowerLimit : (lowerLimit + Math.ulp(lowerLimit));
		double cup = upperLimitClosed ? upperLimit : (upperLimit - Math.ulp(upperLimit));
		return cup - clow;
	}


	/** Converts this Interval into a string representation.
		@return a String containing a textual representation of this interval with the
				same syntax as is used for specifying assertions.
	*/
	public String toString() {
		String s = "";

		// Check if interval denotes a single value
		if (lowerLimitClosed && upperLimitClosed && lowerLimit == upperLimit) {
			s += lowerLimit;
			return s;
		}

		// Check if interval denotes a one-sided interval
		if (lowerLimit == Double.NEGATIVE_INFINITY && upperLimit != Double.POSITIVE_INFINITY)	{
			s += (upperLimitClosed ? "<= " : "< ") + upperLimit;
			return s;
		}
		else if (upperLimit == Double.POSITIVE_INFINITY && lowerLimit != Double.NEGATIVE_INFINITY) {
			s += (lowerLimitClosed ? ">= " : "> ") + lowerLimit;
			return s;
		}

		// Interval denotes a two sided interval
		if (lowerLimitClosed)
			s+="[";
		else
			s+="]";
		s+=lowerLimit + " to " + upperLimit;
		if (upperLimitClosed)
			s+="]";
		else
			s+="[";
		return s;
	}

	/**
	 * Private method to calculate the faculty of a number>=0 after converting it to an integer
	 * @param x is the number
	 * @return the faculty of the number x
	 */
	private static int fac(double x) {
		int n = (int)x;
		int prod = 1;
		while (n>0) {
			prod*=n;
			n--;
		}
		return prod;
	}

	/**
	 * Private method to calculate the modulo of two double values
	 * @param numerator is the numerator
	 * @param denominator is the denominator
	 * @return the modulo as a double of two double values
	 */
	@SuppressWarnings("unused")
	private static double mod(double numerator, double denominator) {
		long numeratorL = (long)numerator;
		long denominatorL = (long)denominator;
		int exp = 0;
		while (numerator > numeratorL || denominator > denominatorL) {
			numerator*=10;
			denominator*=10;
			numeratorL = (long)numerator;
			denominatorL = (long)denominator;
			exp++;
		}
		return (numeratorL%denominatorL) / Math.pow(10,exp);
	}

}
