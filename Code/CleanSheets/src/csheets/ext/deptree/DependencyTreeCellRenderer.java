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

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import csheets.core.Cell;

/**
 * The renderer used for nodes in dependency trees.
 * @author Malin Johansson
 * @author Sofia Nilsson
 * @author Einar Pehrson
 */
@SuppressWarnings("serial")
public class DependencyTreeCellRenderer extends DefaultTreeCellRenderer{
	
	/** An icon representing a node containing a formula */
	private Icon formulaIcon = new ImageIcon(
		DependencyTreeExtension.class.getResource("res/img/formula.gif"));
	
	/** An icon representing a cell containing a constant value */
	private Icon literalIcon = new ImageIcon(
		DependencyTreeExtension.class.getResource("res/img/literal.gif"));
	
	/** An icon representing a cell containing a range */
	private Icon rangeIcon = new ImageIcon(
		DependencyTreeExtension.class.getResource("res/img/range.gif"));
	
	/**
	 * Creates a new dependency tree cell renderer
	 */
	public DependencyTreeCellRenderer() {}
	
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, 
							boolean leaf, int row, boolean hasFocus) {

		if (value instanceof CellNode) {
			// Selects the appropriate icon and tool tip to display
			Cell cell = ((CellNode)value).getCell();
			Icon icon;
			if (cell.getFormula() == null) {
				icon = literalIcon;
				setToolTipText(cell.getValue().toString());
			} else {
				icon = formulaIcon;
				setToolTipText(cell.getValue() + " = " + cell.getFormula());
			}

			// Updates the appropriate icon(s)
			if (leaf)
				setLeafIcon(icon);
			else {
				setOpenIcon(icon);
				setClosedIcon(icon);
			}
		} else if (value instanceof ReferenceNode) {
			// Node is a reference to multiple cells, and therefore not a leaf
			setOpenIcon(rangeIcon);
			setClosedIcon(rangeIcon);
		}

		return super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
	}
}