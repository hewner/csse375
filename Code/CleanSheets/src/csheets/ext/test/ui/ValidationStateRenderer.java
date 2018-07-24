/*
 * Copyright (c) 2005 Jens Schou, Staffan Gustafsson, Björn Lanneskog, 
 * Einar Pehrson and Sebastian Kekkonen
 *
 * This file is part of
 * CleanSheets Extension for Test Cases
 *
 * CleanSheets Extension for Test Cases is free software; you can
 * redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either version 2 of
 * the License, or (at your option) any later version.
 *
 * CleanSheets Extension for Test Cases is distributed in the hope that
 * it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with CleanSheets Extension for Test Cases; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA  02111-1307  USA
 */
package csheets.ext.test.ui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import csheets.ext.test.TestCase;

/**
 * A combo box in which validation states are selected, which acts as a table
 * cell renderer.
 * @author Einar Pehrson
 */
@SuppressWarnings("serial")
public class ValidationStateRenderer extends JComboBox implements TableCellRenderer {

	/**
	 * Creates a new validation state renderer.
	 */
	public ValidationStateRenderer() {
		super(TestCase.ValidationState.values());
	}

	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean selected, boolean hasFocus, int row, int column) {
		TestCase.ValidationState state = (TestCase.ValidationState)value;

		// Sets colors
		switch (state) {
			case VALID:
				setForeground(Color.green);
				break;
			case REJECTED:
				setForeground(Color.red);
				break;
			default:
				if (selected) {
					setForeground(table.getSelectionForeground());
					super.setBackground(table.getSelectionBackground());
				} else {
					setForeground(table.getForeground());
					setBackground(table.getBackground());
				}
		}

		// Selects the current value
		setSelectedItem(value);
		return this;
	}
}