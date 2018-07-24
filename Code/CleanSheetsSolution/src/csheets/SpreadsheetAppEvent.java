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
package csheets;

import java.io.File;
import java.util.EventObject;

import csheets.core.Workbook;

/**
 * A spreadsheet application event is used to notify interested parties that
 * a workbook has been created, loaded or stored. The event provides
 * information about the workbook in which the event occurred, and about
 * the file in which it is stored, if any.
 * @author Einar Pehrson
 */
public class SpreadsheetAppEvent extends EventObject {

	/** The serialVersionUID of the SpreadsheetAppEvent.java */
	private static final long serialVersionUID = -8589956625032669055L;

	/** The types of events that are fired from a spreadsheet application */
	public enum Type {

		/** Denotes that a workbook was created */
		CREATED,

		/** Denotes that a workbook was loaded */
		LOADED,

		/** Denotes that a workbook was unloaded */
		UNLOADED,

		/** Denotes that a workbook was saved */
		SAVED,
	}

	/** The workbook that was affected. */
	private Workbook workbook;

	/** The file in which the project is stored. */
	private File file;

	/** The type of the event */
	private Type type;

	/**
	 * Creates a new spreadsheet application event,
	 * belonging to a workbook stored in a file.
	 * @param source the source of the event
	 * @param workbook the workbook that was affected
	 * @param file the file in which the workbook is stored
	 * @param type the type of the event
	 */
	public SpreadsheetAppEvent(Object source, Workbook workbook,
			File file, Type type) {
		super(source);

		// Stores members
		this.workbook = workbook;
		this.file = file;
		this.type = type;
	}

	/**
	 * Returns the workbook that was affected by the event.
	 * @return the affected workbook
	 */
	public Workbook getWorkbook() {
		return workbook;
	}

	/**
	 * Returns the file in which the workbook is stored.
	 * @return the file in which the workbook is stored
	 */
	public File getFile() {
		return file;
	}

	/**
	 * Returns the type of the event.
	 * @return the type of the event
	 */
	public Type getType() {
		return type;
	}
}