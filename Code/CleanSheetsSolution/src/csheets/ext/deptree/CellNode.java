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

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import csheets.core.Cell;

/**
 * A tree node for a cell, to which child nodes are added dynamically
 * when the node is expanded.
 * @author Einar Pehrson
 */
public abstract class CellNode extends DefaultMutableTreeNode {

	/** The cell of the node. */
	private Cell cell;

	/** If child nodes for the node's references have been added. */
	private boolean populated = false;

	/** The data model to which the node belongs */
	protected DefaultTreeModel treeModel;

	/**
	 * Creates a new cell node.
	 * @param cell the cell of the node
	 * @param treeModel the data model to which the node belongs
	 */
	public CellNode(Cell cell, DefaultTreeModel treeModel) {
		super(cell);

		// Stores members
		this.cell = cell;
		this.treeModel = treeModel;
	}

	/**
	 * Returns the cell of the node.
	 * @return the cell of the node.
	 */
	public Cell getCell() {
		return cell;
	}

	/**
	 * Populates the cell node by adding child nodes for all its references.
	 */
	public void populate() {
		if (!populated) {
			populated = true;
			addChildren();
			treeModel.nodeStructureChanged(this);
		}
	}

	/**
	 * Adds children to the node. Invoked once, when the node is populated.
	 */
	protected abstract void addChildren();
}