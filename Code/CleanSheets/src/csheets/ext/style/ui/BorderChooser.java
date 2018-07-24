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
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;

import csheets.ext.style.StyleExtension;

/**
 * A component which allows the user to select a border.
 * @author Einar Pehrson
 */ 
@SuppressWarnings("serial")
public class BorderChooser extends JComponent {

	/** The toggle button for the top border segment */
	private JToggleButton topButton = new JToggleButton(new ImageIcon(
		StyleExtension.class.getResource("res/img/border_top.gif")));

	/** The toggle button for the left border segment */
	private JToggleButton leftButton = new JToggleButton(new ImageIcon(
		StyleExtension.class.getResource("res/img/border_left.gif")));

	/** The toggle button for the right border segment */
	private JToggleButton rightButton = new JToggleButton(new ImageIcon(
		StyleExtension.class.getResource("res/img/border_right.gif")));

	/** The toggle button for the bottom border segment */
	private JToggleButton bottomButton = new JToggleButton(new ImageIcon(
		StyleExtension.class.getResource("res/img/border_bottom.gif")));

	/** The preview label */
	private JLabel previewLabel = new JLabel("");

	/** The currently selected border color */
	private Color color = Color.black;

	/**
	 * Creates a new border selection panel.
	 */
	public BorderChooser () {
		this(null);
	}

	/**
	 * Creates a new border selection panel with the given initial border
	 * selected.
	 * @param initialBorder the border to select initially
	 */
	public BorderChooser(Border initialBorder) {
		// Configures preview label
		previewLabel.setPreferredSize(new Dimension(100, 80));
		previewLabel.setBorder(
			BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder("Preview"),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)
		));

		// Registers listener
		PreviewLabelUpdater updater = new PreviewLabelUpdater();
		topButton.addItemListener(updater);
		leftButton.addItemListener(updater);
		rightButton.addItemListener(updater);
		bottomButton.addItemListener(updater);

		// Lays out button panel
		JPanel buttonPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
		gc.gridwidth = gc.gridheight = 2;
		gc.gridy = 1;
		buttonPanel.add(leftButton, gc);
		gc.gridx = 2;
		gc.gridy = 0;
		buttonPanel.add(topButton, gc);
		gc.gridy = 2;
		buttonPanel.add(bottomButton, gc);
		gc.gridx = 4;
		gc.gridy = 1;
		buttonPanel.add(rightButton, gc);
		buttonPanel.setBorder(BorderFactory.createTitledBorder("Border"));

		// Creates color panel
		JPanel colorPanel = new JPanel();
		colorPanel.setLayout(new BoxLayout(colorPanel, BoxLayout.X_AXIS));
		JButton colorButton = new JButton(new ColorAction());
		colorButton.setAlignmentY(0.5f);
		colorPanel.add(colorButton);
		colorPanel.setBorder(BorderFactory.createTitledBorder("Color"));

		// Configures layout and adds components
		setLayout(new BorderLayout(5, 5));
		add(buttonPanel, BorderLayout.CENTER);
		add(colorPanel, BorderLayout.EAST);
		add(previewLabel, BorderLayout.SOUTH);

		// Sets the initial border
		setSelectedBorder(initialBorder);
	}

	/**
	 * Lets the user select a border from a chooser in a standard dialog.
	 * @param parent the parent component of the dialog
	 * @param title the title of the dialog
	 * @param initialBorder the border to select initially
	 * @return the selected border or, if the user did not press OK, null
	 */
	public static Border showDialog(Component parent, String title,
			Border initialBorder) {
		BorderChooser chooser = new BorderChooser(initialBorder);
		int returnValue = JOptionPane.showConfirmDialog(
			parent,
			chooser,
			title,
			JOptionPane.OK_CANCEL_OPTION,
			JOptionPane.PLAIN_MESSAGE,
			(Icon)null);
		if (returnValue == JOptionPane.OK_OPTION)
			return chooser.getSelectedBorder();
		else
			return null;
	}

	/**
	 * Returns the currently selected border in the dialog.
	 * @return the currently selected border
	 */
	public Border getSelectedBorder() {
		return BorderFactory.createMatteBorder(
			topButton.isSelected() ? 1 : 0,
			leftButton.isSelected() ? 1 : 0,
			bottomButton.isSelected() ? 1 : 0,
			rightButton.isSelected() ? 1 : 0,
			color);
	}

	/**
	 * Sets the currently selected border in the dialog.
	 * @param border the border to select
	 */
	public void setSelectedBorder(Border border) {
		if (border instanceof MatteBorder) {
			MatteBorder matteBorder = (MatteBorder)border;
			color = matteBorder.getMatteColor();
			Insets insets = matteBorder.getBorderInsets();
			topButton.setSelected(insets.top > 0);
			leftButton.setSelected(insets.left > 0);
			bottomButton.setSelected(insets.bottom > 0);
			rightButton.setSelected(insets.right > 0);
		}
	}

	/**
	 * Updates the border of the preview label.
	 */
	private void updatePreviewBorder() {
		previewLabel.setBorder(
			BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder("Preview"),
				BorderFactory.createCompoundBorder(
					BorderFactory.createEmptyBorder(5, 5, 5, 5),
					getSelectedBorder())));
	}

	/**
	 * A controller that listens to selection events on the buttons.
	 */
	private class PreviewLabelUpdater implements ItemListener {

		/**
		 * Creates a new preview label updater.
		 */
		public PreviewLabelUpdater() {}

		/**
		 * Updates the preview label.
		 * @param e the event that was fired
		 */
		public void itemStateChanged(ItemEvent e) {
			updatePreviewBorder();
		}
	}

	/**
	 * An action for letting the user select a new border color from a chooser.
	 */
	private class ColorAction extends AbstractAction {

		public ColorAction() {
			putValue(SMALL_ICON, new ImageIcon(
				StyleExtension.class.getResource("res/img/color_bg.gif")));
		}

		public void actionPerformed(ActionEvent e) {
			Color c = JColorChooser.showDialog(null, "Choose Border Color",
				color);
			if (c != null) {
				color = c;
				updatePreviewBorder();
			}
		}
	}
}