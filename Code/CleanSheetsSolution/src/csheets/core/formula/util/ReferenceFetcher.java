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

import java.util.SortedSet;
import java.util.TreeSet;

import csheets.core.formula.Expression;
import csheets.core.formula.Reference;

/**
 * An expression visitor that collects the references from an expression.
 * @author Einar Pehrson
 */
public class ReferenceFetcher extends AbstractExpressionVisitor {

	/** The references that have been fetched */
	private SortedSet<Reference> references;

	/**
	 * Creates a new reference fetcher.
	 */
	public ReferenceFetcher() {}

	/**
	 * Traverses the given expression and returns the references that were found.
	 * @param expression the expression from which to fetch references
	 * @return the references that have been fetched
	 */
	public SortedSet<Reference> getReferences(Expression expression) {
		references = new TreeSet<Reference>();
		expression.accept(this);
		return references;
	}

	/**
	 * Adds the reference to the set.
	 * @param reference the reference to visit
	 */
	public Object visitReference(Reference reference) {
		references.add(reference);
		return reference;
	}
}