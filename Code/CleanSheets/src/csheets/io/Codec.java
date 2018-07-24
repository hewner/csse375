/*
 * Copyright (c) 2005 Einar Pehrson <einar@pehrson.nu>.
 *
 * This file is part of
 * CleanSheets - a workbook application for the Java platform.
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
import java.io.OutputStream;

import csheets.core.Workbook;

/**
 * An interface for classes capable of reading and writing workbooks.
 * @author Einar Pehrson
 */
public interface Codec {

	/**
	 * Reads a workbook from the given input stream.
	 * @param stream the input stream from which the workbook should be read
	 * @throws IOException if the workbook could not be read correctly
	 * @throws ClassNotFoundException If the class of a serialized object could not be found
	 */
	public Workbook read(InputStream stream) throws IOException, ClassNotFoundException;

	/**
	 * Writes a workbook to the given output stream.
	 * @param stream the output stream to which the workbook should be written
	 * @throws IOException if the workbook could not be written correctly
	 */
	public void write(Workbook workbook, OutputStream stream) throws IOException;
}