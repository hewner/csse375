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
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import csheets.core.Workbook;
import csheets.ext.ExtensionManager;

/**
 * A codec for the native CleanSheets format that uses Java Serialization.
 * @author Einar Pehrson
 */
public class CLSCodec implements Codec {

	/**
	 * Creates a new CleanSheets codec.
	 */
	public CLSCodec() {}

	public Workbook read(InputStream stream) throws IOException, ClassNotFoundException {
		ObjectInputStream ois = new DynamicObjectInputStream(stream,
			ExtensionManager.getInstance().getLoader());
		return (Workbook)ois.readObject();
	}

	public void write(Workbook workbook, OutputStream stream) throws IOException {
		ObjectOutputStream oos = new ObjectOutputStream(stream);
		oos.writeObject(workbook);
		oos.flush();
	}
}