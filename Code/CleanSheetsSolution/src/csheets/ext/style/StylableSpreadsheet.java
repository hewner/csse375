/*
 * Copyright (c) 2005 Einar Pehrson
 *
 * This file is part of
 * CleanSheets Extension for Style
 *
 * CleanSheets Extension for Style is free software; you can
 * redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either version 2 of
 * the License, or (at your option) any later version.
 *
 * CleanSheets Extension for Style is distributed in the hope that
 * it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with CleanSheets Extension for Style; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA  02111-1307  USA
 */
package csheets.ext.style;

import java.util.HashMap;
import java.util.Map;

import csheets.core.Address;
import csheets.core.Spreadsheet;
import csheets.ext.SpreadsheetExtension;

/**
 * An extension of a spreadsheet, with support for style, i.e. row and column
 * dimensions and cell spanning.
 * @author Einar Pehrson
 */
public class StylableSpreadsheet extends SpreadsheetExtension {

	/** The unique version identifier used for serialization */
	private static final long serialVersionUID = -302349897603798290L;

	/** The width of the columns in the spreadsheet */
	private Map<Integer, Integer> columnWidths = new HashMap<Integer, Integer>();

	/** The heights of the rows in the spreadsheet */
	private Map<Integer, Integer> rowHeights = new HashMap<Integer, Integer>();

	/** The spans of cells in the spreadsheet */
	@SuppressWarnings("unused")
	private Map<Address, Address> cellSpans = new HashMap<Address, Address>();

	/*
	 * Possible additions:
	 * - Cell spanning
	 * - Grid visiblity (horizontal and vertical)
	 * - Grid color
	 * - Cell margin
	 */

	/**
	 * Creates a stylable cell spreadsheet for the given spreadsheet.
	 * @param spreadsheet the spreadsheet to extend
	 */
	protected StylableSpreadsheet(Spreadsheet spreadsheet) {
		super(spreadsheet, StyleExtension.NAME);
	}

	/**
	 * Returns the height of the given row.
	 * @param row the index of the row
	 * @return the height of the given row, or -1 if the default height applies
	 */
	public int getRowHeight(int row) {
		Integer height = rowHeights.get(row);
		if (height != null)
			return height;
		else
			return -1;
	}

	/**
	 * Sets the height of the given row.
	 * @param row the index of the row
	 * @param height the height of the row
	 */
	public void setRowHeight(int row, int height) {
		rowHeights.put(row, height);
	}

	/**
	 * Returns the width of the given column.
	 * @param column the index of the column
	 * @return the width of the given column, or -1 if the default width applies
	 */
	public int getColumnWidth(int column) {
		Integer width = columnWidths.get(column);
		if (width != null)
			return width;
		else
			return -1;
	}

	/**
	 * Sets the width of the given column.
	 * @param column the index of the column
	 * @param width the width of the column
	 */
	public void setColumnWidth(int column, int width) {
		columnWidths.put(column, width);
	}
}