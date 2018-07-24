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
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.List;

/**
 * A filename filter for filtering on filename extensions.
 * @author Einar Pehrson
 */
public class FilenameExtensionFilter implements FilenameFilter {

	/** The extensions of allowed files. */
	private List<String> extensions;

	/**
	 * Creates a new filter for files with the given extensions.
	 * @param extensions the extension of allowed files
	 */
	public FilenameExtensionFilter(String... extensions) {
		this(Arrays.asList(extensions));
	}

	/**
	 * Creates a new filter for files with the given extensions.
	 * @param extensions the extension of allowed files
	 */
	public FilenameExtensionFilter(List<String> extensions) {
		if (extensions.size() == 0)
			throw new IllegalArgumentException();
		this.extensions = extensions;
	}

	public boolean accept(File file, String filename) {
		String extension = filename.substring(filename.lastIndexOf('.') + 1).toLowerCase().trim();
		return extensions.contains(extension);
	}

	/**
	 * Returns the file extensions that the filter allows.
	 * @return the file extensions that the filter allows
	 */
	public String[] getExtensions() {
		return extensions.toArray(new String[extensions.size()]);
	}
}