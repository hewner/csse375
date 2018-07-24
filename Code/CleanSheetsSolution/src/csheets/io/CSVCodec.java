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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.LinkedList;
import java.util.List;

import csheets.core.Spreadsheet;
import csheets.core.Workbook;

/**
 * A codec for comma-separated files.
 * @author Einar Pehrson
 */
public class CSVCodec implements Codec {

	/** The string used to separate the content of different cells */
	public static final String SEPARATOR = ";";

	/**
	 * Creates a new CSV codec.
	 */
	public CSVCodec() {}

	public Workbook read(InputStream stream) throws IOException {
		// Wraps stream
		Reader streamReader = new InputStreamReader(stream);
		BufferedReader reader = new BufferedReader(streamReader);

		// Reads content of rows
		String line;
		int columns = 0;
		List<String[]> rows = new LinkedList<String[]>();
		while ((line = reader.readLine()) != null) {
			String[] row = line.split(SEPARATOR);
			rows.add(row);
			if (row.length > columns)
				columns = row.length;
		}

		// Builds content matrix
		String[][] content = new String[rows.size()][columns];
		int i = 0;
		for (String[] row : rows)
			content[i++] = row;

		// Frees resources
		reader.close();
		streamReader.close();
		stream.close();

		return new Workbook(content);
	}

	public void write(Workbook workbook, OutputStream stream) throws IOException {
		System.out.println("Writing!");
		// Wraps stream
		PrintWriter writer = new PrintWriter(new BufferedWriter(
			new OutputStreamWriter(stream)));

		// Writes content of rows
		Spreadsheet sheet = workbook.getSpreadsheet(0);
		for (int row = 0; row < sheet.getRowCount(); row++) {
			for (int column = 0; column < sheet.getColumnCount(); column++)
				if (column + 1 < sheet.getColumnCount())
					writer.print(sheet.getCell(column, row).getContent()
						+ SEPARATOR);
			if (row + 1 < sheet.getRowCount())
				writer.println();
		}


		// Frees resources
		writer.close();
		stream.close();
		System.out.println("Done!");
	}
}