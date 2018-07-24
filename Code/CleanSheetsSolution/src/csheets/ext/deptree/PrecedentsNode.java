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
import csheets.core.formula.Reference;
import csheets.core.formula.lang.CellReference;
import csheets.ui.ctrl.UIController;

/**
 * A tree node for a cell, to which the cell's precedents are added dynamically
 * when the node is expanded.
 * @author Einar Pehrson
 */
@SuppressWarnings("serial")
public class PrecedentsNode extends CellNode {

	/** The references of the node's cell. */
	private SortedSet<Reference> references = new TreeSet<Reference>();

	/** The user interface controller */
	private UIController uiController;

	/**
	 * Creates a new precedents node.
	 * @param cell the cell of the node
	 * @param treeModel the data model to which the node belongs
	 * @param uiController the user interface controller
	 */
	public PrecedentsNode(Cell cell, DefaultTreeModel treeModel,
			UIController uiController) {
		super(cell, treeModel);
		this.uiController = uiController;
		if (cell.getFormula() != null)
			references = cell.getFormula().getReferences();
	}

	protected void addChildren() {
		for (Reference reference : references) {
			if (reference instanceof CellReference) {
				Cell cell = ((CellReference)reference).getCell();
				add(new PrecedentsNode(cell, treeModel, uiController));
			} else
				add(new ReferenceNode(reference, getCell().getSpreadsheet(),
					treeModel, uiController));
		}
	}

	/**
	 * Returns whether the cell has references.
	 * @return true if the cell has references
	 */
	public boolean isLeaf() {
		return references.isEmpty();
	}
}