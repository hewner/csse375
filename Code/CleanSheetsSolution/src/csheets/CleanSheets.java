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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import javax.swing.SwingUtilities;

import csheets.core.Workbook;
import csheets.core.formula.compiler.FormulaCompiler;
import csheets.core.formula.lang.Language;
import csheets.ext.ExtensionManager;
import csheets.io.Codec;
import csheets.io.CodecFactory;
import csheets.io.NamedProperties;

/**
 * CleanSheets - the main class of the application.
 * The class manages workbooks, performs I/O operations and provides support
 * for notifying listeners when workbooks are created, loaded or saved.
 * @author Einar Pehrson
 */
public class CleanSheets {

	/** The filename of the default properties, loaded from the directory of the class */
	private static final String DEFAULT_PROPERTIES_FILENAME = "res/defaults.xml";

	/** The filename of the user properties, loaded from the user's current working directory */
	private static final String USER_PROPERTIES_FILENAME = "csheets.xml";

	/** The open workbooks */
	private Map<Workbook, File> workbooks = new HashMap<Workbook, File>();

	/** The application's properties */
	private NamedProperties props;

	/** The listeners registered to receive events */
	private List<SpreadsheetAppListener> listeners
		= new ArrayList<SpreadsheetAppListener>();

	/**
	 * Creates the CleanSheets application.
	 */
	public CleanSheets() {
		// Loads compilers
		FormulaCompiler.getInstance();

		// Loads language
		Language.getInstance();

		// Loads extensions
		ExtensionManager.getInstance();

		// Loads default properties
		Properties defaultProps = new Properties();
		InputStream defaultStream = CleanSheets.class.getResourceAsStream(DEFAULT_PROPERTIES_FILENAME);
		if (defaultStream != null)
			try {
				defaultProps.loadFromXML(defaultStream);
			} catch (IOException e) {
				System.err.println("Could not load default application properties.");
			} finally {
				try {
					if (defaultStream != null)
						defaultStream.close();
				} catch (IOException e) {}
			}

		// Loads user properties
		File propsFile = new File(USER_PROPERTIES_FILENAME);
		props = new NamedProperties(propsFile, defaultProps);
	}

	/**
	 * Starts CleanSheets from the command-line.
	 * @param args the command-line arguments (not used)
	 */
	public static void main(String[] args) {
		
        Locale france = new Locale("fr", "FR");
        Locale.setDefault(france);
		
		CleanSheets app = new CleanSheets();

		// Configures look and feel
		javax.swing.JFrame.setDefaultLookAndFeelDecorated(true);
		javax.swing.JDialog.setDefaultLookAndFeelDecorated(true);
		/* try {
			javax.swing.UIManager.setLookAndFeel("className");
		} catch (Exception e) {} */

		// Creates user interface
		new csheets.ui.Frame.Creator(app).createAndWait();
		app.create();
	}

	/**
	 * Returns the current user properties.
	 * @return the current user properties
	 */
	public Properties getUserProperties() {
		return props;
	}

	/**
	 * Exits the application.
	 */
	public void exit() {
		// Stores properties
		if (props.size() > 0)
			try {
				props.storeToXML("CleanSheets User Properties (" + 
					DateFormat.getDateTimeInstance().format(new Date()) + ")");
			} catch (IOException e) {
				System.err.println("An error occurred while saving properties.");
			}

		// Terminates the virtual machine
		System.exit(0);
	}

	/**
	 * Creates a new workbook.
	 */
	public void create() {
		Workbook workbook = new Workbook(3);
		workbooks.put(workbook, null);
		fireSpreadsheetAppEvent(workbook, null, SpreadsheetAppEvent.Type.CREATED);
	}

	/**
	 * Loads a workbook from the given file.
	 * @param file the file in which the workbook is stored
	 * @throws IOException if the file could not be loaded correctly
	 */
	public void load(File file) throws IOException, ClassNotFoundException {
		Codec codec = new CodecFactory().getCodec(file);
		if (codec != null) {
			FileInputStream stream = null;
			Workbook workbook;
			try {
				// Reads workbook data
				stream = new FileInputStream(file);
				workbook = codec.read(stream);
			} finally {
				try {
					if (stream != null)
						stream.close();
				} catch (IOException e) {}
			}

			// Loads the workbook
			workbooks.put(workbook, file);
			fireSpreadsheetAppEvent(workbook, file, SpreadsheetAppEvent.Type.LOADED);
		} else
			throw new IOException("Codec could not be found");
	}

	/**
	 * Unloads the given workbook.
	 * @param workbook the workbook to unload
	 */
	public void unload(Workbook workbook) {
		File file = workbooks.remove(workbook);
		fireSpreadsheetAppEvent(workbook, file, SpreadsheetAppEvent.Type.UNLOADED);
	}

	/**
	 * Saves the given workbook to the file from which it was loaded,
	 * or to which it was most recently saved.
	 * @param workbook the workbook to save
	 * @throws IOException if the file could not be saved correctly
	 */
	public void save(Workbook workbook) throws IOException {
		File file = workbooks.get(workbook);
		if (file != null)
			saveAs(workbook, file);
		else
			throw new FileNotFoundException("No file assigned to the workbook.");
	}

	/**
	 * Saves the given workbook to the given file.
	 * @param workbook the workbook to save
	 * @param file the file to which the workbook should be saved
	 * @throws IOException if the file could not be saved correctly
	 */
	public void saveAs(Workbook workbook, File file) throws IOException {
		Codec codec = new CodecFactory().getCodec(file);
		if (codec != null) {
			FileOutputStream stream = null;
			try {
				// Reads workbook data
				stream = new FileOutputStream(file);
				codec.write(workbook, stream);
			} finally {
				try {
					if (stream != null)
						stream.close();
				} catch (IOException e) {}
			}

			workbooks.put(workbook, file);
			fireSpreadsheetAppEvent(workbook, file, SpreadsheetAppEvent.Type.SAVED);
		}
	}

	/**
	 * Returns the workbooks that are open.
	 * @return the workbooks that are open
	 */
	public Workbook[] getWorkbooks() {
		Collection<Workbook> workbookSet = workbooks.keySet();
		return workbookSet.toArray(new Workbook[workbookSet.size()]);
	}

	/**
	 * Returns the file in which the given workbook is stored.
	 * @return the file in which the given workbook is stored, or null if it isn't
	 */
	public File getFile(Workbook workbook) {
		return workbooks.get(workbook);
	}

	/**
	 * Returns whether a file has been specified for the given workbook,
	 * either when it was loaded or when it was last saved.
	 * @return whether the given workbook belongs to a file
	 */
	public boolean isWorkbookStored(Workbook workbook) {
		return workbooks.get(workbook) != null;
	}

	/**
	 * Returns the workbook that is stored in the given file, if it is already
	 * open.
	 * @param file the file to look for
	 * @return the workbook that is stored in the given file, or null if the file isn't open
	 */
	public Workbook getWorkbook(File file) {
		for (Map.Entry<Workbook, File> entry : workbooks.entrySet())
			if (entry.getValue() != null && entry.getValue().equals(file))
				return entry.getKey();
		return null;
	}

	/**
	 * Returns whether the given file is open, and a workbook thereby loaded
	 * from it or saved to it.
	 * @param file the file to look for
	 * @return whether the given file is open
	 */
	public boolean isFileOpen(File file) {
		return workbooks.containsValue(file);
	}

	/**
	 * Registers the given listener on the spreadsheet application.
	 * @param listener the listener to be added
	 */
	public void addSpreadsheetAppListener(SpreadsheetAppListener listener) {
		listeners.add(listener);
	}

	/**
	 * Removes the given listener from the spreadsheet application.
	 * @param listener the listener to be removed
	 */
	public void removeSpreadsheetAppListener(SpreadsheetAppListener listener) {
		listeners.remove(listener);
	}

	/**
	 * Notifies all registered listeners that a spreadsheet application event
	 * occurred.
	 * @param workbook the workbook that was affected
	 * @param file the file that was affected
	 */
	private void fireSpreadsheetAppEvent(Workbook workbook, File file,
			SpreadsheetAppEvent.Type type) {
		SpreadsheetAppEvent event
			= new SpreadsheetAppEvent(this, workbook, file, type);
		if (SwingUtilities.isEventDispatchThread())
			for (SpreadsheetAppListener listener : listeners)
				switch (event.getType()) {
					case CREATED:
						listener.workbookCreated(event); break;
					case LOADED:
						listener.workbookLoaded(event); break;
					case UNLOADED:
						listener.workbookUnloaded(event); break;
					case SAVED:
						listener.workbookSaved(event); break;
				}
		else
			SwingUtilities.invokeLater(
				new EventDispatcher(event, 
					listeners.toArray(new SpreadsheetAppListener[listeners.size()])
				)
			);
	}

	/**
	 * A utility for dispatching events on the AWT event dispatching thread.
	 * @author Einar Pehrson
	 */
	public static class EventDispatcher implements Runnable {

		/** The event to fire */
		private SpreadsheetAppEvent event;

		/** The listeners to which the event should be dispatched */
		private SpreadsheetAppListener[] listeners;

		/**
		 * Creates a new event dispatcher.
		 * @param event the event to fire
		 * @param listeners the listeners to which the event should be dispatched
		 */
		public EventDispatcher(SpreadsheetAppEvent event,
				SpreadsheetAppListener[] listeners) {
			this.event = event;
			this.listeners = listeners;
		}

		/**
		 * Dispatches the event.
		 */
		public void run() {
			for (SpreadsheetAppListener listener : listeners)
				switch (event.getType()) {
					case CREATED:
						listener.workbookCreated(event); break;
					case LOADED:
						listener.workbookLoaded(event); break;
					case UNLOADED:
						listener.workbookUnloaded(event); break;
					case SAVED:
						listener.workbookSaved(event); break;
				}
		}
	}
}