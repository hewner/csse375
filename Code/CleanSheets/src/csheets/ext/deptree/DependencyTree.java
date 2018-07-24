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

import javax.swing.JTree;
import javax.swing.ToolTipManager;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

import csheets.ui.ctrl.SelectionEvent;
import csheets.ui.ctrl.SelectionListener;
import csheets.ui.ctrl.UIController;

/**
 * An abstract base-class for trees displaying cell dependencies.
 * @author Einar Pehrson
 */
public abstract class DependencyTree extends JTree implements SelectionListener {

	/** The user interface controller */
	protected UIController uiController;

	/** The data model that defines the tree */
	protected DefaultTreeModel treeModel;

	/**
	 * Creates a mew dependency tree. 
	 * @param uiController the user interface controller
	 */
	public DependencyTree(UIController uiController) {
		super(new DefaultTreeModel(null));

		// Stores members
		this.treeModel = (DefaultTreeModel)super.treeModel;
		this.uiController = uiController;
		uiController.addSelectionListener(this);

		// Creates and configures tree
		getSelectionModel().setSelectionMode(
			TreeSelectionModel.SINGLE_TREE_SELECTION);
		setToggleClickCount(Integer.MAX_VALUE);
		addTreeExpansionListener(new ExpansionPopulator());
		addMouseListener(new DoubleClickNavigator(this, uiController));
		setCellRenderer(new DependencyTreeCellRenderer());
		ToolTipManager.sharedInstance().registerComponent(this);
	}

	/**
	 * Rebuilds the tree by updating its root node when the active cell is changed.
	 * @param event the selection event that was fired
	 */
	public abstract void selectionChanged(SelectionEvent event);
}