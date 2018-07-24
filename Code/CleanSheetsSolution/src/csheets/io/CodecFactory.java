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

/**
 * A factory for codecs.
 * @author Einar Pehrson
 */
public class CodecFactory {

	/**
	 * Creates a new codec factory.
	 */
	public CodecFactory() {}

	/**
	 * Returns the appropriate codec for the given file.
	 * @param file the file to use
	 * @return the appropriate codec, or null if none could be found
	 */
	public Codec getCodec(File file) {
		String filename = file.getName();
		String extension = filename.substring(filename.lastIndexOf('.') + 1).toLowerCase().trim();
		return getCodec(extension);
	}

	/**
	 * Returns the appropriate codec for the given filename extension.
	 * @param extension the filename extension of the file to use
	 * @return the appropriate codec, or null if none could be found
	 */
	public Codec getCodec(String extension) {
		// Builds the class name
		String className = this.getClass().getPackage().getName()
			+ "." + extension.toUpperCase() + "Codec";

		// Fetches the class and instantiates the codec
		Codec codec = null;
		try {
			Class codecClass = Class.forName(className);
			codec = (Codec)codecClass.newInstance();
		} catch (Exception e) {
			return null;
		}

		return codec;
	}
}