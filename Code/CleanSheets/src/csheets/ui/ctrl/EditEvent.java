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
package csheets.ui.ctrl;

import java.util.EventObject;

import csheets.core.Workbook;

/**
 * An edit event is used to notify interested parties that a workbook has been
 * modified.
 * @author Einar Pehrson
 */
@SuppressWarnings("serial")
public class EditEvent extends EventObject {

	/** The workbook that was modified */
	private Workbook workbook;

	/**
	 * Creates a new edit event.
	 * @param source the source of the event
	 * @param workbook the workbook that was modified
	 */
	public EditEvent(Object source, Workbook workbook) {
		super(source);

		// Stores members
		this.workbook = workbook;
	}

	/**
	 * Returns the active workbook.
	 * @return the active workbook
	 */
	public Workbook getWorkbook() {
		return workbook;
	}
}