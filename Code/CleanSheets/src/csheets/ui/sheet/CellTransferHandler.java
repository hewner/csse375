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

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.TransferHandler;

import csheets.core.Cell;
import csheets.core.formula.compiler.FormulaCompilationException;

/**
 * The transfer handler used to transfer ranges of cells.
 * @author Einar Pehrson
 */
@SuppressWarnings("serial")
public class CellTransferHandler extends TransferHandler {

	/** The table from which cells were copied or moved */
	private SpreadsheetTable sourceTable;

	/** The action that was taken on export, i.e. either COPY or MOVE */
	private int exportAction = -1;

	/**
	 * Creates a new cell transfer handler.
	 */
	public CellTransferHandler() {}

	protected Transferable createTransferable(JComponent c) {
		if (c instanceof SpreadsheetTable) {
			this.sourceTable = (SpreadsheetTable)c;
			return new CellTransferable(sourceTable.getSelectedCells());
		}
		return null;
	}

	public boolean importData(JComponent c, Transferable t) {
		if (canImport(c, t.getTransferDataFlavors())) {
			// Fetches destination
			SpreadsheetTable table = (SpreadsheetTable)c;
			int activeColumn = table.getColumnModel().getSelectionModel().getAnchorSelectionIndex();
			int activeRow = table.getSelectionModel().getAnchorSelectionIndex();

			if (t.isDataFlavorSupported(CellTransferable.LOCAL_CELL_FLAVOR)) {
				// Fetches transfer data from cell transferable
				Cell[][] range = null;
				try {
					range = (Cell[][])t.getTransferData(CellTransferable.LOCAL_CELL_FLAVOR);
				} catch (UnsupportedFlavorException e) {
					return false;
				} catch (IOException e) {
					return false;
				}

				// Pastes data
				for (int row = 0; row < range.length; row++)
					for (int column = 0; column < range[row].length; column++) {
						// Fetches source and destination cell
						Cell sourceCell = range[row][column];
						Cell destCell = table.getSpreadsheet().getCell(activeColumn + column, activeRow + row);
	
						// Performs action and updates table
						if (exportAction == COPY)
							destCell.copyFrom(sourceCell);
						else if (exportAction == MOVE || exportAction == -1)
							destCell.moveFrom(sourceCell);
					}
				return true;
			} else if (t.isDataFlavorSupported(DataFlavor.stringFlavor)) {
				// Fetches transfer data from string transferable
				String data = null;
				try {
					data = (String)t.getTransferData(DataFlavor.stringFlavor);
				} catch (UnsupportedFlavorException e) {
					return false;
				} catch (IOException e) {
					return false;
				}

				if (data != null) {
					// Pastes data
					String[] rows = data.split("\n");
					for (int row = 0; row < rows.length; row++) {
						String[] rowData = rows[row].split("\t");
						for (int column = 0; column < rowData.length; column++) {
							Cell destCell = table.getSpreadsheet().getCell(
								activeColumn + column, activeRow + row);
							try {
								destCell.setContent(rowData[column]);
							} catch (FormulaCompilationException e) {}
						}
					}
				}
			}
		}
		return false;
	}

	public boolean canImport(JComponent c, DataFlavor[] flavors) {
		boolean hasValidFlavor = false;
		for (DataFlavor flavor : flavors)
			if (flavor.equals(CellTransferable.LOCAL_CELL_FLAVOR)
				|| flavor.equals(DataFlavor.stringFlavor)) {
				hasValidFlavor = true;
				break;
			}
		return c instanceof SpreadsheetTable && hasValidFlavor;
	}

	public int getSourceActions(JComponent c) {
		return COPY; // COPY_OR_MOVE;
	}

	protected void exportDone(JComponent c, Transferable data, int action) {
		this.exportAction = action;
	}
}