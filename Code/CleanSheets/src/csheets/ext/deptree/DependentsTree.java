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

import csheets.core.Cell;
import csheets.ui.ctrl.SelectionEvent;
import csheets.ui.ctrl.UIController;

/**
 * A tree displaying the dependents of a cell.
 * @author Einar Pehrson
 */
@SuppressWarnings("serial")
public class DependentsTree extends DependencyTree {

	/**
	 * Creates a mew dependents tree.
	 * @param uiController the user interface controller
	 */
	public DependentsTree(UIController uiController) {
		super(uiController);
	}

	public void selectionChanged(SelectionEvent event) {
		Cell cell = event.getCell();
		if (event.isCellChanged())
			if (cell != null) {
				CellNode node = new DependentsNode(cell, treeModel);
				node.populate();
				treeModel.setRoot(node);
			} else
				treeModel.setRoot(null);
	}
}