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

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import csheets.ui.ctrl.UIController;

/**
 * A mouse listener that updates the active cell of a spreadsheet when a
 * cell node is double-clicked.
 * @author Einar Pehrson
 */
public class DoubleClickNavigator extends MouseAdapter {

	/** The tree to use when determining the node at a given location */
	private JTree tree;

	/** The user interface controller */
	private UIController uiController;

	/**
	 * Creates a new double click navigator.
	 * @param tree the tree to use when determining the node at a given location
	 * @param uiController the user interface controller
	 */
	public DoubleClickNavigator(JTree tree, UIController uiController) {
		// Stores members
		this.tree = tree;
		this.uiController = uiController;
	}

	/**
	 * Sets the cell of a double-clicked node as the application's active cell.
	 * @param event the event that was fired
	 */
	public void mouseClicked(MouseEvent event) {
		if (SwingUtilities.isLeftMouseButton(event)
				&& event.getClickCount() > 1) {
			// Fetches the path that was double-clicked
			int row = tree.getRowForLocation(event.getX(), event.getY());
			TreePath path = tree.getPathForRow(row);
			if (path != null) {
				TreeNode lastNode = (TreeNode)path.getLastPathComponent();
				if (lastNode instanceof CellNode) {
					// Sets the last node of the path as the active cell
					CellNode node = (CellNode)lastNode;
					uiController.setActiveCell(node.getCell());
				}
			}
		}
	}
}