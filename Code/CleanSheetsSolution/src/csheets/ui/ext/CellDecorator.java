/*
 * Copyright (c) 2005 Einar Pehrson <einar@pehrson.nu>.
 *
 * This file is part of
 * CleanSheets - a spreadsheet application for the Java platform.
 *
 * CleanSheets is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * CleanSheets is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with CleanSheets; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package csheets.ui.ext;

import java.awt.Graphics;

import javax.swing.JComponent;

import csheets.core.Cell;

/**
 * A cell decorator visualizes extension-specific data by drawing on top of
 * a cell's graphics context. Decorators should respect the enabled state set
 * on them.
 * @author Einar Pehrson
 */
public abstract class CellDecorator {

	/** Whether the decorator should be used when rendering, default is false */
	protected boolean enabled = false;

	/**
	 * Creates a new cell decorator.
	 */
	public CellDecorator() {}

	/**
	 * Decorates the given graphics context, by drawing visualizations of
	 * extension-specific data on it.
	 * @param component the cell renderer component
	 * @param g the graphics context on which drawing should be done
	 * @param cell the cell being rendered
	 * @param selected whether the cell is selected
	 * @param hasFocus whether the cell has focus
	 */
	public abstract void decorate(JComponent component, Graphics g, Cell cell,
			boolean selected, boolean hasFocus);

	/**
	 * Returns whether the decorator should be used when rendering.
	 * @return true if the decorator should be used when rendering
	 */	
	public final boolean isEnabled() {
		return enabled;
	}

	/**
	 * Sets whether the decorator should be used when rendering.
	 * @param enabled whether the decorator should be used when rendering
	 */
	public final void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}