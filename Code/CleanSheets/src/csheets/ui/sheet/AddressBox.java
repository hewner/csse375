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
package csheets.ui.sheet;

import java.awt.Dimension;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import csheets.core.Cell;
import csheets.ui.ctrl.SelectionEvent;
import csheets.ui.ctrl.SelectionListener;
import csheets.ui.ctrl.UIController;

/**
 * A text field in which cell addresses are displayed, and can be named.
 * @author Einar Pehrson
 */
@SuppressWarnings("serial")
public class AddressBox extends JComboBox implements SelectionListener {

	/** The user interface controller */
	// private UIController uiController;

	/**
	 * Creates a cell content field.
	 * @param uiController the user interface controller
	 */
	public AddressBox(UIController uiController) {
		// Stores user interface controller
		// this.uiController = uiController;
		uiController.addSelectionListener(this);

		// Configures box
		setPreferredSize(new Dimension(80, 20));
		setEditable(false); // Change!
		if (getRenderer() instanceof JLabel)
			((JLabel)getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
		if (getEditor().getEditorComponent() instanceof JTextField)
			((JTextField)getEditor().getEditorComponent()).setHorizontalAlignment(SwingConstants.CENTER);
		// setAction(new RegisterNameAction());
	}

	/**
	 * Updates the text field with the content of the new active cell.
	 * @param event the selection event that was fired
	 */
	public void selectionChanged(SelectionEvent event) {
		Cell cell = event.getCell();
		if (cell != null) {
			if (getItemCount() > 0)
				removeItemAt(0);
			insertItemAt(cell, 0);
			setSelectedIndex(0);
		}
	}
}