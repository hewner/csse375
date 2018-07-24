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
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.Arrays;

import javax.swing.CellRendererPane;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;
import javax.swing.table.TableCellRenderer;


/**
 * The row header for spreadsheet tables. This component emulates the
 * behaviour of <code>javax.swing.plaf.basic.BasicTableHeaderUI</code>, but
 * paints the header vertically in stead of horizontally.
 * @author Einar Pehrson
 */
@SuppressWarnings("serial")
public class RowHeader extends JComponent {

	/** The table to which the row header belongs */
	private JTable table;

	/** The header's renderer pane */
	private CellRendererPane rendererPane = new CellRendererPane();

	/** The row header renderer*/
	private TableCellRenderer renderer
		= new HeaderRenderer(SwingConstants.VERTICAL);

	/** The width of the row header */
	private int width = 30;

	/** The minimum height of rows */
	private int minRowHeight = 5;

	/** The margin around the packed rows of the header. */
    private int rowMargin = 1;

	/** The index of the row being resized, or -1 */
	private int resizingRow = -1;

	/**
	 * Creates a new row header for the given table.
	 * @param table the table to which the row header belongs
	 */
	public RowHeader(JTable table) {
		this.table = table;
		add(rendererPane);
		setPreferredSize(new Dimension(width, table.getRowCount() * table.getRowHeight()));
		MouseInputListener rowResizer = new RowResizer();
		addMouseListener(rowResizer);
		addMouseMotionListener(rowResizer);
	}

	public void paint(Graphics g) {
		// Calculates visible area
		Rectangle bounds = g.getClipBounds(); 
		Point top = bounds.getLocation();
		Point bottom = new Point(bounds.x, bounds.y + bounds.height - 1);

		// Finds rows to paint
		int minRow = table.rowAtPoint(top);
		int maxRow = table.rowAtPoint(bottom);
		if (minRow == -1)
			minRow =  0;
		if (maxRow == -1)
			maxRow = table.getRowCount()-1;  

		// Paints rows
		int y = table.getCellRect(minRow, 0, true).y;
		int[] selectedRows = table.getSelectedRows();
		for (int row = minRow; row <= maxRow; row++) {
			// Fetches component from renderer
			boolean selected = Arrays.binarySearch(selectedRows, row) >= 0;
			Component c = renderer.getTableCellRendererComponent(
				table, null, selected, false, row, -1);

			// Calculates coordinates and paints component
			int rowHeight = table.getRowHeight(row);
			rendererPane.paintComponent(g, c, this,
				0, y, width, rowHeight, true);
			y += rowHeight;
		}
	}

	/**
	 * Adjusts the height of the row at the given index to precisely fit all
	 * data being rendered.
	 * @param row the index of the row to auto-resize
	 */
	public void autoResize(int row) {
		// Gets width of row header
		int height = renderer.getTableCellRendererComponent(table, null,
				false, false, row, 0).getPreferredSize().height;
		
		// Gets maximum width of column data
		for (int column = 0; column < table.getColumnCount(); column++) {
			Component c = table.getCellRenderer(row, column)
				.getTableCellRendererComponent(table, table.getValueAt
					(row, column), false, false, row, column);
			height = Math.max(height, c.getPreferredSize().height);
		}

		// Adds margin
		height += 2 * rowMargin;
		if (height > minRowHeight) {
			table.setRowHeight(resizingRow, height);
		} else
			table.setRowHeight(minRowHeight);
		repaint();
	}

	/**
	 * A mouse input listener that enables resizing of rows
	 */
	protected class RowResizer extends MouseInputAdapter {

		/** The normal cursor */
		private final Cursor NORMAL_CURSOR = getCursor();

		/** The cursor to display when resizing a row */
		private final Cursor RESIZE_CURSOR
			= Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR);

		/**
		 * Selects the clicked row, unless resizing is intended.
		 * @param e the event that was fired
		 */
		public void mousePressed(MouseEvent e) {
			// Checks what was done
			resizingRow = getResizingRow(e.getPoint());
			if (resizingRow == -1) {
				int pressedRow = table.rowAtPoint(e.getPoint());
				int columns = table.getColumnCount();
	
				// Configures new selection
				if (e.isShiftDown())
					table.changeSelection(pressedRow, 0, false, true);
				else if (e.isControlDown())
					table.changeSelection(pressedRow, 0, true, false);
				else {
					table.changeSelection(pressedRow, columns, false, false);
					table.changeSelection(pressedRow, 0, false, true);
				}
				repaint();
			}
		}

		/**
		 * Auto-resizes a column whose border was double-clicked.
		 * @param e the event that was fired
		 */
		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() == 2 && resizingRow != -1)
				autoResize(resizingRow);
		}

		/**
		 * Sets the appropriate cursor depending on whether the mouse is on
		 * a row that can be resized.
		 * @param e the event that was fired
		 */
		public void mouseMoved(MouseEvent e) { 
			setCursor(getResizingRow(e.getPoint()) == -1
				? NORMAL_CURSOR : RESIZE_CURSOR);
		}

		/**
		 * Resizes the row that is dragged
		 * @param e the event that was fired
		 */
		public void mouseDragged(MouseEvent e) {
			if (resizingRow != -1) {
				int rowHeight = e.getPoint().y
					- table.getCellRect(resizingRow, 0, true).y;
				if (rowHeight >= minRowHeight)
					table.setRowHeight(resizingRow, rowHeight);
				repaint();
			}
		}

		/**
		 * Retrieves the index of the row at the given point, if it can be
		 * resized.
		 * @param p the point to look at
		 * @return the index of the row, or -1 if it can not be resized
		 */
		private int getResizingRow(Point p) {
			// Fetches the row index, and stops if it is invalid
			int row = table.rowAtPoint(p);
			if (row == -1)
				return row;

			// Fetches the bounding rectangle of the header row
			Rectangle r = table.getCellRect(row, 0, true);
			r = new Rectangle(0, r.y, width, table.getRowHeight(row));
			r.grow(0, -2);

			// Stops if the point is inside the header row
			if (r.contains(p))
				return -1;

			// If above the middle of the row, resize previous row
			if (p.y < (r.y + (r.height / 2)))
				row--;
			return row;
		}
	}
}