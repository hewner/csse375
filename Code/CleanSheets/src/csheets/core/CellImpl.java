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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import csheets.core.formula.Formula;
import csheets.core.formula.Reference;
import csheets.core.formula.compiler.FormulaCompilationException;
import csheets.core.formula.compiler.FormulaCompiler;
import csheets.core.formula.util.ReferenceTransposer;
import csheets.ext.CellExtension;
import csheets.ext.Extension;
import csheets.ext.ExtensionManager;

/**
 * The implementation of the <code>Cell</code> interface.
 * @author Einar Pehrson
 */
public class CellImpl implements Cell {

	/** The unique version identifier used for serialization */
	private static final long serialVersionUID = 926673794084390673L;

	/** The spreadsheet to which the cell belongs */
	private Spreadsheet spreadsheet;

	/** The address of the cell */
	private Address address;

	/** The value of the cell */
	private Value value = new Value();

	/** The content of the cell */
	private String content = "";

	/** The cell's formula */
	private Formula formula;

	/** The cell's precedents */
	private SortedSet<Cell> precedents = new TreeSet<Cell>();

	/** The cell's dependents */
	private SortedSet<Cell> dependents = new TreeSet<Cell>();

	/** The cell listeners that have been registered on the cell */
	private transient List<CellListener> listeners
		= new ArrayList<CellListener>();

	/** The cell extensions that have been instantiated */
	private transient Map<String, CellExtension> extensions = 
		new HashMap<String, CellExtension>();

	/**
	 * Creates a new cell at the given address in the given spreadsheet.
	 * (not intended to be used directly).
	 * @see Spreadsheet#getCell(Address)
	 * @param spreadsheet the spreadsheet
	 * @param address the address of the cell
	 */
	CellImpl(Spreadsheet spreadsheet, Address address) {
		this.spreadsheet = spreadsheet;
		this.address = address;
	}

	/**
	 * Creates a new cell at the given address in the given spreadsheet,
	 * initialized with the given content (not intended to be used directly).
	 * @see Spreadsheet#getCell(Address)
	 * @param spreadsheet the spreadsheet
	 * @param address the address of the cell
	 * @param content the content of the cell
	 * @throws ExpressionSyntaxException if an incorrectly formatted formula was entered
	 */
	CellImpl(Spreadsheet spreadsheet, Address address, String content) throws FormulaCompilationException {
		this(spreadsheet, address);
		storeContent(content);
		reevaluate();
	}

/*
 * LOCATION
 */

	public Spreadsheet getSpreadsheet() {
		return spreadsheet;
	}

	public Address getAddress() {
		return address;
	}

/*
 * VALUE
 */

	public Value getValue() {
		return value;
	}

	/**
	 * Updates the cell's value, and fires an event if it changed.
	 */
	private void reevaluate() {
		Value oldValue = value;

		// Fetches the new value
		Value newValue;
		if (formula != null)
			try {
				newValue = formula.evaluate();
			} catch (IllegalValueTypeException e) {
				newValue = new Value(e);
			}
		else
			newValue = Value.parseValue(content);

		// Stores value
		value = newValue;

		// Checks for change
		if (!newValue.equals(oldValue))
			fireValueChanged();
	}

	/**
	 * Notifies all registered listeners that the value of the cell changed.
	 */
	private void fireValueChanged() {
		for (CellListener listener : listeners)
			listener.valueChanged(this);
		for (CellExtension extension : extensions.values())
			extension.valueChanged(this);

		// Notifies dependents of the changed value
		for (Cell dependent : dependents) {
			if (dependent instanceof CellImpl)
				((CellImpl)dependent).reevaluate();
		}
	}

/*
 * CONTENT
 */

	public String getContent() {
		return content;
	}

	public Formula getFormula() {
		return formula;
	}

	public void clear() {
		try {
			setContent("");
		} catch (FormulaCompilationException e) {}
		fireCellCleared();
	}

	public void setContent(String content) throws FormulaCompilationException {
		if (!this.content.equals(content)) {
			storeContent(content);
			fireContentChanged();
			reevaluate();
		}
	}

	/**
	 * Updates the cell's content, and registers dependencies.
	 * @param content the content to store
	 * @throws FormulaCompilationException if an incorrectly formatted formula was entered
	 */
	private void storeContent(String content) throws FormulaCompilationException {
		// Parses formula
		Formula formula = null;
		if (content.length() > 1)
			formula = FormulaCompiler.getInstance().compile(this, content);

		// Stores content and formula
		this.content = content;
		this.formula = formula;
		updateDependencies();
	}

	/**
	 * Updates the cell's dependencies.
	 */
	private void updateDependencies() {
		// Deregisters as dependent with each old precedent
		for (Cell precedent : precedents)
			((CellImpl)precedent).removeDependent(this);
		precedents.clear();

		if (formula != null)
			// Registers as dependent with each new precedent
			for (Reference reference : formula.getReferences())
				for (Cell precedent : reference.getCells()) {
					if (!this.equals(precedent)) {
						precedents.add(precedent);
						((CellImpl)precedent).addDependent(this);
					}
				}
	}

	/**
	 * Notifies all registered listeners that the content of the cell changed.
	 */
	private void fireContentChanged() {
		for (CellListener listener : listeners)
			listener.contentChanged(this);
		for (CellExtension extension : extensions.values())
			extension.contentChanged(this);
	}

	/**
	 * Notifies all registered listeners that the cell was cleared.
	 */
	private void fireCellCleared() {
		for (CellListener listener : listeners)
			listener.cellCleared(this);
		for (CellExtension extension : extensions.values())
			extension.cellCleared(this);
	}

/*
 * DEPENDENCIES
 */

	public SortedSet<Cell> getPrecedents() {
		return new TreeSet<Cell>(precedents);
	}

	public SortedSet<Cell> getDependents() {
		return new TreeSet<Cell>(dependents);
	}

	/**
	 * Adds the given cell as a dependent of this cell, to be notified when its
	 * value changes.
	 * @param cell the dependent to add
	 */
	private void addDependent(Cell cell) {
		dependents.add(cell);
		fireDependentsChanged();
	}

	/**
	 * Removes the given cell as a dependent of this cell.
	 * @param cell the dependent to remove
	 */
	private void removeDependent(Cell cell) {
		dependents.remove(cell);
		fireDependentsChanged();
	}

	/**
	 * Notifies all registered listeners that the content of the cell changed.
	 */
	private void fireDependentsChanged() {
		for (CellListener listener : listeners)
			listener.dependentsChanged(this);
		for (CellExtension extension : extensions.values())
			extension.dependentsChanged(this);
	}

/*
 * CLIPBOARD
 */

	public void copyFrom(Cell source) {
		// Copies content
		if (source.getFormula() == null)
			try {
				setContent(source.getContent());
			} catch (FormulaCompilationException e) {}
		else {
			// Copies and transposes formula
			this.formula = new Formula(this,
				new ReferenceTransposer(
					getAddress().getColumn() - source.getAddress().getColumn(),
					getAddress().getRow() - source.getAddress().getRow()
				).getExpression(source.getFormula().getExpression())
			);
			this.content = source.getContent().charAt(0) + formula.toString();
			updateDependencies();
			fireContentChanged();
			reevaluate();
		}
		fireCellCopied(source);
	}

	public void moveFrom(Cell source) {
		// Change the address of the source cell
		// Remove the target cell from the spreadsheet
		// Flag the target cell as overwritten!
		
		// fireCellCopied(source);
	}

	/**
	 * Notifies all registered listeners that the cell was copied (or moved).
	 * @param source the cell from which data was copied
	 */
	private void fireCellCopied(Cell source) {
		for (CellListener listener : listeners)
			listener.cellCopied(this, source);
		for (CellExtension extension : extensions.values())
			extension.cellCopied(this, source);
	}

/*
 * EVENT HANDLING
 */

	public void addCellListener(CellListener listener) {
		listeners.add(listener);
	}

	public void removeCellListener(CellListener listener) {
		listeners.remove(listener);
	}

	public CellListener[] getCellListeners() {
		return listeners.toArray(new CellListener[listeners.size()]);
	}

/*
 * EXTENSIONS
 */

	public Cell getExtension(String name) {
		// Looks for an existing cell extension
		CellExtension extension = extensions.get(name);
		if (extension == null) {
			// Creates a new cell extension
			Extension x = ExtensionManager.getInstance().getExtension(name);
			if (x != null) {
				extension = x.extend(this);
				if (extension != null)
					extensions.put(name, extension);
			}
		}
		return extension;
	}

/*
 * GENERAL
 */

	/**
	 * Compares this cell with the specified cell for order,
	 * by comparing their addresses.
	 * @param cell the cell to be compared
	 * @return a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object.
	 */
	public int compareTo(Cell cell) {
		if (spreadsheet != cell.getSpreadsheet())
			return -1;
		else
			return address.compareTo(cell.getAddress());
	}

	/**
	 * Returns a string representation of the cell.
	 * @return the cell's content
	 */
	public String toString() {
		return address.toString();
	}

	/**
	 * Customizes deserialization by recreating the listener list and by catching
	 * exceptions when extensions are not found.
	 * @param stream the object input stream from which the object is to be read
	 * @throws IOException If any of the usual Input/Output related exceptions occur
	 * @throws ClassNotFoundException If the class of a serialized object cannot be found.
	 */
	private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
		stream.defaultReadObject();
		listeners = new ArrayList<CellListener>();

		// Reads extensions
		extensions = new HashMap<String, CellExtension>();
		int extCount = stream.readInt();
		for (int i = 0; i < extCount; i++) {
			try {
				CellExtension extension = (CellExtension)stream.readObject();
				extensions.put(extension.getName(), extension);
			} catch (ClassNotFoundException e) {}
		}
	}

	/**
	 * Customizes serialization by writing extensions separately.
	 * @param stream the object output stream to which the object is to be written
	 * @throws IOException If any of the usual Input/Output related exceptions occur
	 */
	private void writeObject(ObjectOutputStream stream) throws IOException {
		stream.defaultWriteObject();

		// Writes extensions
		stream.writeInt(extensions.size());
		for (CellExtension extension : extensions.values())
			stream.writeObject(extension);
	}
}