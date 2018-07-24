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
package csheets.ui.ctrl;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import csheets.CleanSheets;

/**
 * An action for displaying the about window.
 * @author Einar Pehrson
 */
@SuppressWarnings("serial")
public class AboutAction extends BaseAction {

	/** The text to display in the about dialog */
	private String text;

	/**
	 * Creates a new about action.
	 */
	public AboutAction() {
		InputStream stream = CleanSheets.class.getResourceAsStream(
			"res/doc/about.txt");
		if (stream != null) {
			// Loads text from file
			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(stream));
				int availableBytes = stream.available();
				char[] chars = new char[availableBytes];
				br.read(chars, 0, availableBytes);
				text = new String(chars);
			} catch (Exception ex) {
				text = "An error occurred when loading the text.";
			} finally {
				try {
					stream.close();
				} catch (IOException e) {}
			}
		} else
			setEnabled(false);
	}

	protected String getName() {
		return "About...";
	}

	protected void defineProperties() {
		putValue(MNEMONIC_KEY, KeyEvent.VK_A);
		putValue(SMALL_ICON, new ImageIcon(CleanSheets.class.getResource("res/img/about.gif")));
	}

	public void actionPerformed(ActionEvent e) {
		JOptionPane.showMessageDialog(null,
			text,
			"About CleanSheets",
			JOptionPane.INFORMATION_MESSAGE,
			new ImageIcon(CleanSheets.class.getResource("res/img/sheet.gif")));
	}
}