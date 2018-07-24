/*
 * Copyright (c) 2005 Einar Pehrson, Malin Johansson and Sofia Nilsson
 *
 * This file is part of
 * CleanSheets Extension for Dependency Trees
 *
 * CleanSheets Extension for Dependency Trees is free software; you can
 * redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either version 2 of
 * the License, or (at your option) any later version.
 *
 * CleanSheets Extension for Dependency Trees is distributed in the hope that
 * it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with CleanSheets Extension for Dependency Trees; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA  02111-1307  USA
 */
package csheets.ext.deptree;

import java.util.SortedSet;
import java.util.TreeSet;

import javax.swing.tree.DefaultTreeModel;

import csheets.core.Cell;

/**
 * A tree node for a cell, to which the cell's dependents are added dynamically
 * when the node is expanded.
 * @author Einar Pehrson
 */
@SuppressWarnings("serial")
public class DependentsNode extends CellNode {

	/** The dependents of the node's cell. */
	private SortedSet<Cell> dependents = new TreeSet<Cell>();

	/**
	 * Creates a new dependents node.
	 * @param cell the cell of the node
	 * @param treeModel the data model to which the node belongs
	 */
	public DependentsNode(Cell cell, DefaultTreeModel treeModel) {
		super(cell, treeModel);
		dependents = cell.getDependents();
	}

	protected void addChildren() {
		for (Cell dependent : dependents)
			add(new DependentsNode(dependent, treeModel));
	}

	/**
	 * Returns whether the cell has references.
	 * @return true if the cell has references
	 */
	public boolean isLeaf() {
		return dependents.isEmpty();
	}
}