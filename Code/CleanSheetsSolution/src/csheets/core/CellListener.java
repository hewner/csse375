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
 * cell.
 * @author Einar Pehrson
 */
public interface CellListener extends EventListener {

	/**
	 * Invoked when the value of a cell has changed, either by a change in
	 * its content, or by a change of value in one of its precedents.
	 * @param cell the cell that was modified
	 */
	public void valueChanged(Cell cell);

	/**
	 * Invoked when the content of a cell has been changed.
	 * As a consequence, the cell's formula and its precedents may have changed.
	 * @param cell the cell that was modified
	 */
	public void contentChanged(Cell cell);

	/**
	 * Invoked when a new dependent has been registered on a cell,
	 * or when an old one has been removed.
	 * @param cell the cell that was modified
	 */
	public void dependentsChanged(Cell cell);

	/**
	 * Invoked when a cell has been cleared.
	 * @param cell the cell that was modified
	 */
	public void cellCleared(Cell cell);

	/**
	 * Invoked when a cell has been copied.
	 * @param cell the cell that was modified
	 * @param source the cell from which data was copied
	 */
	public void cellCopied(Cell cell, Cell source);
}