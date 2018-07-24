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
package csheets.ui;

import java.awt.Component;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import csheets.SpreadsheetAppEvent;
import csheets.SpreadsheetAppListener;
import csheets.io.FilenameExtensionFilter;

/**
 * A file chooser that loads choosable file types from properties.
 * @author Einar Pehrson
 */
@SuppressWarnings("serial")
public class FileChooser extends JFileChooser implements SpreadsheetAppListener {

	/** The parent component of dialogs created by the chooser */
	private Component parent;

	/** The file filters used by the chooser */
	private List<Filter> filters = new ArrayList<Filter>();

	/**
	 * Creates a new file chooser. Choosable file types are loaded from the
	 * given properties. 
	 * Dialogs are shown with the given component as parent.
	 * @param parent the parent component of dialogs
	 * @param props the user properties
	 */
	public FileChooser(Component parent, Properties props) {
		// Stores members
		this.parent = parent;

		// Configures chooser
		setAcceptAllFileFilterUsed(false);

		if (props != null) {
			// Loads file types
			String extension, description;
			for (int i = 0;
				(extension = props.getProperty("filetype" + i + ".extension")) != null
			 && (description = props.getProperty("filetype" + i + ".description")) != null;
				 	i++) {
				addChoosableFileFilter(new Filter(
					new FilenameExtensionFilter(extension), description));
			}
			if (filters.size() > 0)
				setFileFilter(filters.get(0));

			// Loads current directory
			String currentDir = props.getProperty("recentfile0");
			if (currentDir != null)
				setCurrentDirectory(new File(currentDir));
		}
	}

	/**
	 * Shows a file chooser and returns the file the user selected, if any.
	 */
	public File getFileToOpen() {
		if (showOpenDialog(parent) == JFileChooser.APPROVE_OPTION)
			return getChosenFile();
		else
			return null;
	}

	/**
	 * Shows a file chooser and returns the file the user selected, if any.
	 */
	public File getFileToSave() {
		if (showSaveDialog(parent) == JFileChooser.APPROVE_OPTION)
			return getChosenFile();
		else
			return null;
	}

	/**
	 * Shows a file chooser and returns the file the user selected, if any.
	 */
	private File getChosenFile() {
		File file = getSelectedFile();
		String filename = file.getName();
		if (filename.lastIndexOf('.') != -1) {
			// Checks if the file is acceptable by any of the filters.
			boolean filterAcceptable = filters.isEmpty();
			for (Filter filter : filters)
				if (filter.getFilter().accept(file, filename)) {
					filterAcceptable = true;
					break;
				}
			return file.isDirectory() || filterAcceptable ? file : null;
		} else {
			// Appends default extension from the chosen filter
			FileFilter filter = getFileFilter();
			if (filter instanceof Filter) {
				FilenameFilter filenameFilter = ((Filter)filter).getFilter();
				if (filenameFilter instanceof FilenameExtensionFilter) {
					String[] extensions = ((FilenameExtensionFilter)
						filenameFilter).getExtensions();
					return new File(file.getAbsolutePath() + "." + extensions[0]);
				}
			}
			return null;
		}
	}

	public void addChoosableFileFilter(FileFilter filter) {
		super.addChoosableFileFilter(filter);
		if (filter instanceof Filter) filters.add((Filter)filter);
	}

	public void workbookCreated(SpreadsheetAppEvent event) {}

	public void workbookLoaded(SpreadsheetAppEvent event) {
		setCurrentDirectory(event.getFile());
	}

	public void workbookUnloaded(SpreadsheetAppEvent event) {}

	public void workbookSaved(SpreadsheetAppEvent event) {
		setCurrentDirectory(event.getFile());
	}

	/**
	 * A file filter for use in file choosers.
	 * @author Einar Pehrson
	 */
	public static class Filter extends FileFilter {

		/** The filename filter used to determine which files to accept. */
		private FilenameFilter filter;

		/** The description of the filter. */
		private String description;

		/**
		 * Creates a new filter, using the given filename filter to determine which
		 * files to accept.
		 * @param filter the filename filter used to determine which files to accept
		 * @param description the description of the filter
		 */
		public Filter(FilenameFilter filter, String description) {
			this.filter = filter;
			this.description = description;
		}

		public boolean accept(File f) {
			return f != null && (f.isDirectory() || filter.accept(f, f.getName()));
		}

		/**
		 * Returns the filename filter used to determine which files to accept.
		 * @return the filename filter
		 */
		public FilenameFilter getFilter() {
			return filter;
		}

		/**
		 * Returns the description of the filter
		 * @return the description of the filter
		 */
		public String getDescription() {
			return description;
		}
	}
}