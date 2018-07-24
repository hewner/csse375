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

import csheets.ext.Extension;
import csheets.ui.ctrl.UIController;
import csheets.ui.ext.UIExtension;

/**
 * The extension for dependency trees.
 * @author Einar Pehrson
 */
public class DependencyTreeExtension extends Extension {

	/** The name of the extension */
	public static final String NAME = "Dependencies";

	/**
	 * Creates a new assertion extension.
	 */
	public DependencyTreeExtension() {
		super(NAME);
	}

	/**
	 * Returns a user interface extension for dependency trees.
	 * @param uiController the user interface controller
	 * @return a user interface extension for dependency trees
	 */
	public UIExtension getUIExtension(UIController uiController) {
		return new DependencyTreeUIExtension(this, uiController);
	}
}