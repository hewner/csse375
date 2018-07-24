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
import csheets.core.Spreadsheet;
import csheets.core.formula.Reference;
import csheets.ui.ctrl.UIController;

/**
 * A mutable tree node containing a reference. The addresses that the reference
 * points to are added as child nodes.
 * @author Einar Pehrson
 */
@SuppressWarnings("serial")
public class ReferenceNode extends DefaultMutableTreeNode {

	/**
	 * Creates a new reference node.
	 * @param reference the reference of the node
	 * @param treeModel the data model to which the node belongs
	 * @param uiController the user interface controller
	 */
	public ReferenceNode(Reference reference, Spreadsheet spreadsheet,
		DefaultTreeModel treeModel, UIController uiController) {
		super(reference);

		// Adds children
		for (Cell cell : reference.getCells())
			add(new PrecedentsNode(cell, treeModel, uiController));
	}
}