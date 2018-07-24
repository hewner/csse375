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
package csheets.core;

import java.util.EventListener;

/**
 * A listener interface for receiving notification on events occurring in a
 * workbook.
 * @author Einar Pehrson
 */
public interface WorkbookListener extends EventListener {

	/**
	 * Invoked when a spreadsheet has been inserted into a workbook.
	 * @param spreadsheet the spreadsheet that was inserted
	 * @param index the index at which the spreadsheet was inserted
	 */
	public void spreadsheetInserted(Spreadsheet spreadsheet, int index);

	/**
	 * Invoked when a spreadsheet has been removed from a workbook.
	 * @param spreadsheet the spreadsheet that was removed
	 */
	public void spreadsheetRemoved(Spreadsheet spreadsheet);

	/**
	 * Invoked when a spreadsheet in a workbook has been renamed.
	 * @param spreadsheet the spreadsheet that was renamed
	 */
	public void spreadsheetRenamed(Spreadsheet spreadsheet);
}