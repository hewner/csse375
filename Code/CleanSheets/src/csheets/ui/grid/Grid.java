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

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.table.TableModel;


/**
 * A customized JTable component, with a row header and some other improved
 * features.
 * @author Einar Pehrson
 */
@SuppressWarnings("serial")
public class Grid extends JTable {

	/** The action command used for the action */
	public static final String RESUME_EDIT_COMMAND = "Edit active cell";

	/** The table's row header, if it has been placed in a scroll bar */
	private RowHeader rowHeader;

	/**
	 * Creates a blank grid.
	 */
	public Grid() {
		this(null);
	}

	/**
	 * Creates a grid for the given table model.
	 * @param tableModel the table model to display in the table
	 */
	public Grid(TableModel tableModel) {
		super(tableModel);

		// Configures reordering and resizing
		getTableHeader().setReorderingAllowed(false);
		getTableHeader().setResizingAllowed(true);
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		// Configures selection
		setCellSelectionEnabled(true);
		setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

		// Configures cell borders
		setGridColor(gridColor.brighter());
		// setShowGrid(false);
		// setIntercellSpacing(new Dimension(0, 0));

		// Configures table headers
		UIManager.getDefaults().putDefaults(new Object[] {
			"TableHeader.selectionForeground", Color.black,
			"TableHeader.selectionBackground", Color.orange});
		getTableHeader().setDefaultRenderer(
			new HeaderRenderer(SwingConstants.HORIZONTAL));

		// Configures cell editing
		setSurrendersFocusOnKeystroke(true);
		getActionMap().put(RESUME_EDIT_COMMAND, new ResumeEditAction());
		getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0), RESUME_EDIT_COMMAND);
	}

	/**
	 * Processes key bindings in the table. Overridden to prevent modified
	 * key events from invoking the editor.
	 */
	protected boolean processKeyBinding(KeyStroke ks, KeyEvent e, int condition,
			boolean pressed) {
		if (e.getKeyCode() == KeyEvent.VK_C
		 || e.getKeyCode() == KeyEvent.VK_X
		 || e.getKeyCode() == KeyEvent.VK_V
		 || !(e.isAltDown() || e.isControlDown()))
			return super.processKeyBinding(ks, e, condition, pressed);
		else
			return false;
	}

	/**
	 * Changes the current selection in the table. Overridden to repaint row
	 * and column headers as well.
	 * @param row the row that was selected
	 * @param column the column that was selected
	 * @param toggle whether the selection should be toggled
	 * @param extend whether the selection should be extended
	 */
	public void changeSelection(int row, int column, boolean toggle, boolean extend) {
		super.changeSelection(row, column, toggle, extend);
		if (tableHeader != null)
			tableHeader.repaint();
		if (rowHeader != null)
			rowHeader.repaint();
	}

/*
 * ROW HEADER
 */

	/**
	 * Adds the row header.
	 */
	protected void configureEnclosingScrollPane() {
		super.configureEnclosingScrollPane();
		if (rowHeader == null)
			rowHeader = new RowHeader(this);
		setEnclosingScrollPaneRowHeaderView(rowHeader);
	}

	/**
	 * Removes the row header.
	 */
	protected void unconfigureEnclosingScrollPane() {
		super.unconfigureEnclosingScrollPane();
		setEnclosingScrollPaneRowHeaderView(null);
	}

	/**
	 * Sets the row header view of an enclosing scroll pane to the given
	 * component.
	 * @param header the component to use as the row header, or null if no header is wanted
	 */
	private void setEnclosingScrollPaneRowHeaderView(JComponent header) {
		// If the table is the main viewport of a scroll pane
		if (getParent() instanceof JViewport)
			if (getParent().getParent() instanceof JScrollPane) {
				JScrollPane scrollPane = (JScrollPane)(getParent().getParent());
				JViewport viewport = scrollPane.getViewport();
				if (viewport != null && viewport.getView() == this)
					// Updates row header
					scrollPane.setRowHeaderView(header);
			}
	}

/*
 * ACTIONS
 */

	/**
	 * An action for editing a cell, without clearing its contents.
	 * @author Einar Pehrson
	 */
	protected class ResumeEditAction extends AbstractAction {

		/**
		 * Creates an edit resuming action.
		 */
		public ResumeEditAction() {
			// Configures action
			putValue(NAME, RESUME_EDIT_COMMAND);
			putValue(SHORT_DESCRIPTION, RESUME_EDIT_COMMAND);
			putValue(ACTION_COMMAND_KEY, RESUME_EDIT_COMMAND);
		}

		public void actionPerformed(ActionEvent e) {
			int row = getSelectionModel().getAnchorSelectionIndex();
			int column = getColumnModel().getSelectionModel().getAnchorSelectionIndex();
			if (row >= 0 && column >= 0)
				editCellAt(row, column, e);
		}
	}
}