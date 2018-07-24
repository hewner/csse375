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

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import csheets.CleanSheets;

/**
 * An action for displaying the license agreement.
 * @author Einar Pehrson
 */
@SuppressWarnings("serial")
public class LicenseAction extends BaseAction {

	/** The scroll pane in which the text is shown */
	private JScrollPane licensePane;

	/**
	 * Creates a new help action.
	 */
	public LicenseAction() {
		InputStream stream = CleanSheets.class.getResourceAsStream(
			"res/doc/license.txt");
		if (stream != null) {
			// Loads text from file
			String text;
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

			// Creates and configures text area
			JTextArea textArea = new JTextArea(text, 25, 80);
			textArea.setFont(new Font("Courier", Font.PLAIN, 12));
			textArea.setLineWrap(true);
			textArea.setWrapStyleWord(true);
			textArea.setTabSize(4);
			textArea.setDisabledTextColor(Color.black);
			textArea.setEnabled(false);
			licensePane = new JScrollPane(textArea);
		} else
			setEnabled(false);
	}

	protected String getName() {
		return "License Agreement...";
	}

	protected void defineProperties() {
		putValue(MNEMONIC_KEY, KeyEvent.VK_L);
		putValue(SMALL_ICON, new ImageIcon(CleanSheets.class.getResource("res/img/license.gif")));
	}

	public void actionPerformed(ActionEvent e) {
		JOptionPane.showMessageDialog(
			(Frame)null,
			licensePane,
			"License Agreement",
			JOptionPane.PLAIN_MESSAGE,
			(Icon)null);
	}
}