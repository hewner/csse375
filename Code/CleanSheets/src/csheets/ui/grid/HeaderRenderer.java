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
package csheets.ui.grid;

import java.awt.Component;
import java.util.Arrays;

import javax.swing.JTable;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;

import csheets.core.Address;

/**
 * The table cell renderer used for spreadsheet table headers.
 * @author Einar Pehrson
 */
@SuppressWarnings("serial")
public class HeaderRenderer extends DefaultTableCellRenderer {

	/** The axis of the header */
	private int axis;

	/**
	 * Creates a new spreadsheet table header renderer.
	 * @param axis the axis of the header, either SwingConstants.HORIZONTAL
	 * or SwingConstants.VERTICAL
	 */
	public HeaderRenderer(int axis) {
		// Checks and stores axis
		if (axis != HORIZONTAL && axis != VERTICAL)
			throw new IllegalArgumentException("Axis must be either"
				+ " SwingConstants.HORIZONTAL or SwingConstants.VERTICAL");
		this.axis = axis;

		// Uses table header's appearance
		LookAndFeel.installColorsAndFont(this, "TableHeader.background",
				"TableHeader.foreground", "TableHeader.font");
		LookAndFeel.installProperty(this, "opaque", Boolean.TRUE);
		setBorder(UIManager.getBorder("TableHeader.cellBorder"));
		setHorizontalAlignment(CENTER);
	}

	public Component getTableCellRendererComponent(JTable table, Object value,
		boolean selected, boolean hasFocus, int row, int column) {
		// Checks column selection
		if (axis == HORIZONTAL)
			selected = Arrays.binarySearch(table.getSelectedColumns(), column)
				>= 0;

		// Selects and applies color
		setBackground(UIManager.getColor("TableHeader."
			+ (selected ? "selectionBackground" : "background")));
		setForeground(UIManager.getColor("TableHeader."
			+ (selected ? "selectionForeground" : "foreground")));

		// Updates text and returns
		if (axis == VERTICAL)
			setText(Integer.toString(row + 1));
		else {
			String columnStr;
			int tempColumn = column;
			for (columnStr = ""; tempColumn >= 0; tempColumn = tempColumn
					/ (Address.HIGHEST_CHAR - Address.LOWEST_CHAR + 1) - 1)
				columnStr = (char)((char)(tempColumn % (Address.HIGHEST_CHAR
					- Address.LOWEST_CHAR + 1)) + Address.LOWEST_CHAR) + columnStr;
			setText(columnStr);
		}
		return this;
	}
}