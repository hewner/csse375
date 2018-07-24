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

import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/**
 * A tree expansion listener that asks the node being expanded to
 * to add child nodes for all its references.
 * @author Einar Pehrson
 */
public class ExpansionPopulator implements TreeExpansionListener {

	/**
	 * Creates a new expansion populator.
	 */
	public ExpansionPopulator() {}

	/**
	 * Populates the node that was expanded.
	 * @param event the event that was fired
	 */
	public void treeExpanded(TreeExpansionEvent event) {
		TreePath path = event.getPath();
		TreeNode lastNode = (TreeNode)path.getLastPathComponent();
		if (lastNode instanceof CellNode) {
			// Populates the cell node
			CellNode node = (CellNode)lastNode;
			node.populate();
		}
	}

	/**
	 * Does nothing.
	 * @param event the event that was fired
	 */
	public void treeCollapsed(TreeExpansionEvent event) {}
}