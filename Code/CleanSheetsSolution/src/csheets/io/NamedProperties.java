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
package csheets.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

/**
 * An extension of properties, predetermined to be saved in a specified file.
 * If the file exists, any properties therein are loaded.
 * @author Einar Pehrson
 */
public class NamedProperties extends Properties {

	/** The serialVersionUID of the NamedProperties.java */
	private static final long serialVersionUID = 5734781797863986818L;

	/** The file in which the properties are and should be stored */
	private File file;

	/**
	 * Creates the named properties.
	 * @param file the file in which the properties are and should be stored
	 * @throws IOException if the file could not be read from
	 */
	public NamedProperties(File file) throws IOException {
		this(file, null);
	}

	/**
	 * Creates the named properties.
	 * @param defaults the properties to use as defaults
	 * @param file the file in which the properties are and should be stored
	 * @throws IOException if the file could not be read from
	 */
	public NamedProperties(File file, Properties defaults) {
		super(defaults);

		if (file.exists()) {
			// Loads properties from file
			InputStream stream = null;
			try {
				stream = new FileInputStream(file);
				loadFromXML(stream);
			} catch (IOException e) {
			} finally {
				try {
					if (stream != null)
						stream.close();
				} catch (IOException e) {}
			}
		}
		this.file = file;
	}

	/**
	 * Invokes store() using the file that belongs to the properties.
	 * @param comment a description of the property list, or null if no comment is desired. 
	 * @throws IOException if the file could not be written to
	 */
	public void store(String comment) throws IOException {
		OutputStream stream = null;
		try {
			stream = new FileOutputStream(file);
			super.store(stream, comment);
		} finally {
			try {
				if (stream != null)
					stream.close();
			} catch (IOException e) {}
		}
	}

	/**
	 * Invokes storeToXML() using the file that belongs to the properties.
	 * @param comment a description of the property list, or null if no comment is desired. 
	 * @throws IOException if the file could not be written to
	 */
	public void storeToXML(String comment) throws IOException {
		OutputStream stream = null;
		try {
			stream = new FileOutputStream(file);
			super.storeToXML(stream, comment);
		} finally {
			try {
				if (stream != null)
					stream.close();
			} catch (IOException e) {}
		}
	}
}