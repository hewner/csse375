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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.	See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with CleanSheets; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA	02111-1307	USA
 */
package csheets.ext.style.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * A component which allows the user to select a font.
 * @author Einar Pehrson
 */ 
@SuppressWarnings("serial")
public class FontChooser extends JComponent {

	/** The font sizes that can be selected */
	private static final Integer[] SIZES = new Integer[]
		{4, 6, 8, 9, 10, 12, 14, 16, 18, 20, 22, 24, 28, 32, 36, 40, 48, 64};	

	/** The default font size, if none is selected */
	private static final Integer DEFAULT_SIZE = 10;

	/** The font family list */
	private JList familyList;

	/** The font size list */
	private JList sizeList;

	/** The checkbox that indicates whether the font is bold */
	private JCheckBox boldBox = new JCheckBox("Bold");

	/** The checkbox that indicates whether or not the font is italic */
	private JCheckBox italicBox = new JCheckBox("Italic");

	/** The preview label */
	private JLabel previewLabel = new JLabel();

	/**
	 * Creates a new font selection panel.
	 */
	public FontChooser () {
		this(null);
	}

	/**
	 * Creates a new font selection panel with the given initial font selected.
	 * @param initialFont the font to select initially
	 */
	public FontChooser(Font initialFont) {
		// Fetches the available font family names
		String[] fontFamilyNames = GraphicsEnvironment
			.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

		// Creates and configures lists
		familyList = new JList(fontFamilyNames);
		familyList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		familyList.setVisibleRowCount(10);

		sizeList = new JList(SIZES);
		sizeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		sizeList.setVisibleRowCount(6);
		((DefaultListCellRenderer)sizeList.getCellRenderer())
			.setHorizontalAlignment(JLabel.RIGHT);

		// Configures preview label
		previewLabel.setBorder(BorderFactory.createTitledBorder("Preview"));
		previewLabel.setHorizontalAlignment(JLabel.CENTER);
		previewLabel.setPreferredSize(
			new Dimension(getPreferredSize().width, 80));

		// Creates and configures containers
		JPanel familyPanel = new JPanel(new BorderLayout());
		familyPanel.add(new JScrollPane(familyList));
		familyPanel.setBorder(BorderFactory.createTitledBorder("Family"));

		JPanel sizePanel = new JPanel(new BorderLayout());
		sizePanel.add(new JScrollPane(sizeList));
		sizePanel.setBorder(BorderFactory.createTitledBorder("Size"));

		JPanel stylePanel = new JPanel(new GridLayout(2, 1));
		stylePanel.add(boldBox);
		stylePanel.add(italicBox);
		stylePanel.setBorder(BorderFactory.createTitledBorder("Style"));

		JPanel propPanel = new JPanel(new BorderLayout());
		propPanel.add(sizePanel, BorderLayout.CENTER);
		propPanel.add(stylePanel, BorderLayout.SOUTH);

		// Registers listener
		PreviewLabelUpdater updater = new PreviewLabelUpdater();
		familyList.addListSelectionListener(updater);
		sizeList.addListSelectionListener(updater);
		boldBox.addActionListener(updater);
		italicBox.addActionListener(updater);

		// Configures layout and adds components
		setLayout(new BorderLayout(5, 5));
		add(familyPanel, BorderLayout.CENTER);
		add(propPanel, BorderLayout.EAST);
		add(previewLabel, BorderLayout.SOUTH);

		// Sets the initial font
		if (initialFont != null)
			setSelectedFont(initialFont);
		else
			setSelectedFont(new Font(fontFamilyNames[0], Font.PLAIN, 12));
	}

	/**
	 * Lets the user select a font from a chooser in a standard dialog.
	 * @param parent the parent component of the dialog
	 * @param title the title of the dialog
	 * @param initialFont the font to select initially
	 * @return the selected font or, if the user did not press OK, null
	 */
	public static Font showDialog(Component parent, String title,
			Font initialFont) {
		FontChooser chooser = new FontChooser(initialFont);
		int returnValue = JOptionPane.showConfirmDialog(
			parent,
			chooser,
			title,
			JOptionPane.OK_CANCEL_OPTION,
			JOptionPane.PLAIN_MESSAGE,
			(Icon)null);
		if (returnValue == JOptionPane.OK_OPTION)
			return chooser.getSelectedFont();
		else
			return null;
	}

	/**
	 * Returns the currently selected font in the dialog.
	 * @return the currently selected font
	 */
	public Font getSelectedFont() {
		return new Font(
			(String)familyList.getSelectedValue(),
			(boldBox.isSelected() ? Font.BOLD : Font.PLAIN)
				| (italicBox.isSelected() ? Font.ITALIC : Font.PLAIN),
			sizeList.isSelectionEmpty() ? DEFAULT_SIZE
				: (Integer)sizeList.getSelectedValue()
		);
	}

	/**
	 * Sets the currently selected font in the dialog.
	 * @param font the font to select
	 */
	public void setSelectedFont(Font font) {
		familyList.setSelectedValue(font.getFamily(), true);
		sizeList.setSelectedValue(font.getSize(), true);
		boldBox.setSelected(font.isBold());
		italicBox.setSelected(font.isItalic());
	}

	/**
	 * A controller that updates the preview label.
	 */
	private class PreviewLabelUpdater implements ListSelectionListener, ActionListener {

		/**
		 * Creates a new preview label updater.
		 */
		public PreviewLabelUpdater() {}

		/**
		 * Updates the preview label.
		 * @param e the event that was fired
		 */
		public void actionPerformed(ActionEvent e) {
			previewLabel.setFont(getSelectedFont());
		}

		/**
		 * Updates the preview label.
		 * @param e the event that was fired
		 */
		public void valueChanged(ListSelectionEvent e) {
			if (!e.getValueIsAdjusting()) {
				Font font = getSelectedFont();
				previewLabel.setFont(font);
				previewLabel.setText(font.getName());
			}
		}
	}
}