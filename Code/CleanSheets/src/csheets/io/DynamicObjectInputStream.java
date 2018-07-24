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

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;

/**
 * An object input stream that resolves class descriptors through a given
 * class loader, and thereby allowing dynamic deserialization.
 * @author Einar Pehrson
 */
public class DynamicObjectInputStream extends ObjectInputStream {

	/** The class loader queried for class descriptor resolution */
	private ClassLoader loader;

	/**
	 * Creates a new dynamic object input stream.
	 * @param stream the input stream to read from
	 * @param loader the class loader queried for class descriptor resolution
	 * @throws IOException if an I/O error occurs while reading stream header
	 */
	public DynamicObjectInputStream(InputStream stream, ClassLoader loader)
			throws IOException {
		super(stream);
		this.loader = loader;
	}

	/**
	 * Load the local class equivalent of the specified stream class descriptor,
	 * by first querying the given class loader.
	 * @param desc the class descriptor
	 * @throws IOException if an I/O error occurs while resolving
	 * @throws ClassNotFoundException if the class of a serialized object cannot be found
	 */
	protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException,
		ClassNotFoundException {
		try {
			return Class.forName(desc.getName(), false, loader);
		} catch (ClassNotFoundException ex) {
			return super.resolveClass(desc);
		}
	}
}