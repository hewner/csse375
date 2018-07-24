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
package csheets.ui;

import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;

/**
 * A blank icon used to indent menu items.
 * @author Einar Pehrson
 */
public final class BlankIcon implements Icon {

	/** The height of the icon */
	private int width;

	/** The width of the icon */
	private int height;

	/**
	 * Creates a new blank icon with the given size.
	 * @param size the size of the icon (used as width and height)
	 */
	public BlankIcon(int size) {
		this(size, size);
	}

	/**
	 * Creates a new blank icon with the given width and height.
	 * @param width the width of the icon
	 * @param height the height of the icon
	 */
	public BlankIcon(int width, int height) {
		this.width = width;
		this.height = height;
	}

	/**
	 * Returns the width of the icon.
	 * @return the width of the icon.
	 */
	public int getIconWidth() {
		return width;
	}

	/**
	 * Returns the height of the icon.
	 * @return the height of the icon.
	 */
	public int getIconHeight() {
		return height;
	}

	/**
	 * Does nothing.
	 */
	public void paintIcon(Component c, Graphics g, int x, int y) {}
}