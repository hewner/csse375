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

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;

import csheets.core.Cell;

/**
 * A transferable for transferring a range of cells. Cells are transferred
 * either as objects locally within the Java VM, or exported as strings.
 * @author Einar Pehrson
 */
public class CellTransferable implements Transferable, ClipboardOwner {

	/** The data flavor for local transfer of cells */
	public static final DataFlavor LOCAL_CELL_FLAVOR
		= new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType + ";class=" +
			Cell.class.getName(), "CleanSheets Cell Range (Local)");

	/** The data flavor for serialized transfer of cells */
	public static final DataFlavor SERIAL_CELL_FLAVOR
		= new DataFlavor(Cell.class, "CleanSheets Cell Range (Serialized)");

	/** The cells to transfer */
	private Cell[][] cells;

	/**
	 * Creates a new cell transferable.
	 * @param cells the cells to transfer
	 */
	public CellTransferable(Cell[][] cells) {
		this.cells = cells;
	}

	public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
		if (LOCAL_CELL_FLAVOR.equals(flavor))
			// Exports as cell array
			return cells;
		else if (DataFlavor.stringFlavor.equals(flavor)) {
			// Exports as string
			String data = "";
			for (Cell[] row : cells) {
				for (int column = 0; column < row.length; column++) {
					data += row[column].getValue().toString();
					if (column != row.length - 1)
						data += "\t";
				}
				data += "\n";
			}
			return data;
		} else
			throw new UnsupportedFlavorException(flavor);
	}
	
	public DataFlavor[] getTransferDataFlavors() {
		return new DataFlavor[] {LOCAL_CELL_FLAVOR, DataFlavor.stringFlavor};
	}
	
	public boolean isDataFlavorSupported(DataFlavor flavor) {
		return LOCAL_CELL_FLAVOR.equals(flavor)
			|| DataFlavor.stringFlavor.equals(flavor);
	}

	public void lostOwnership(Clipboard clipboard, Transferable contents) {}
}