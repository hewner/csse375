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

import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

import csheets.ui.ctrl.UIController;
import csheets.ui.ext.UIExtension;

/**
 * The user interface extension for dependency trees.
 * @author Einar Pehrson
 */
public class DependencyTreeUIExtension extends UIExtension {

	/** The icon to display with the extension's name */
	private Icon icon;

	/** A panel in which a precedents tree and a dependents tree is displayed */
	private JComponent sideBar;

	/**
	 * Creates a new user interface extension..
	 * @param extension the extension for which components are provided
	 * @param uiController the user interface controller
	 */
	public DependencyTreeUIExtension(DependencyTreeExtension extension,
			UIController uiController) {
		super(extension, uiController);
	}

	/**
	 * Returns an icon to display with the extension's name.
	 * @return an icon with a tree
	 */
	public Icon getIcon() {
		if (icon == null)
			icon = new ImageIcon(
				DependencyTreeExtension.class.getResource("res/img/logo.gif"));
		return icon;
	}

	/**
	 * Returns a panel in which a precedents tree and a dependents tree is
	 * displayed.
	 * @return a panel with two dependency trees
	 */
	public JComponent getSideBar() {
		if (sideBar == null) {
			sideBar = new JPanel(new GridLayout(2, 1));
			sideBar.setName(DependencyTreeExtension.NAME);
	
			// Creates components
			PrecedentsTree precedentsTree = new PrecedentsTree(uiController);
			JScrollPane precedentsPane = new JScrollPane(precedentsTree);
			DependentsTree dependentsTree = new DependentsTree(uiController);
			JScrollPane dependentsPane = new JScrollPane(dependentsTree);
	
			// Adds borders
			TitledBorder border = BorderFactory.createTitledBorder("Precedents");
			border.setTitleJustification(TitledBorder.CENTER);
			precedentsPane.setBorder(border);
			border = BorderFactory.createTitledBorder("Dependents");
			border.setTitleJustification(TitledBorder.CENTER);
			dependentsPane.setBorder(border);
	
			// Creates side bar
			sideBar.add(precedentsPane);
			sideBar.add(dependentsPane);
		}
		return sideBar;
	}
}