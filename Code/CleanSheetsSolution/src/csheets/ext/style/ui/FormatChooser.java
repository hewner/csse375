/*
 * Copyright (c) 2002,2003 Martin Desruisseaux
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * A component which allows the user to select a border.
 * @author Martin Desruisseaux
 * @author Einar Pehrson
 */ 
@SuppressWarnings("serial")
public class FormatChooser extends JPanel {

	/** The maximum number of items to keep in the history list. */
	private static final int HISTORY_SIZE = 50;

	/** The color for error message. */
	private static final Color ERROR_COLOR = Color.RED;

	/** The format to configure by this <code>FormatChooser</code>. */
	private Format format;

	/** A sample value for the "preview" text. */
	private Object value;

	/** The panel in which to edit the pattern */
	private final JComboBox choices = new JComboBox();

	/** The preview label with the <code>value</code> formated using <code>format</code> */
	private final JLabel previewLabel = new JLabel();

	/**
	 * Creates a pattern chooser for the given date format.
	 * @param format the format to configure
	 * @param value the value to format
	 */
	public FormatChooser(DateFormat format, Date value) {
		this(getPatterns(format));

		// Initializes format
		this.value = value;
		setFormat(format);
	}

	/**
	 * Creates a pattern chooser for the given number format.
	 * @param format the format to configure
	 * @param value the value to format
	 */
	public FormatChooser(NumberFormat format, Number value) {
		this(getPatterns(format));

		// Initializes format
		this.value = value;
		setFormat(format);
	}

	/**
	 * Creates a pattern chooser for the given format.
	 * @param patterns the patterns to choose from
	 */
	private FormatChooser(String[] patterns) {
		// Creates format box
		if (patterns != null)
			choices.setModel(new DefaultComboBoxModel(patterns));
		choices.setEditable(true);
		choices.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				applyPattern(false);
			}
		});

		// Creates format container
		JPanel boxPanel = new JPanel();
		boxPanel.add(choices);
		boxPanel.setBorder(BorderFactory.createTitledBorder("Format"));

		// Configures preview label
		previewLabel.setHorizontalAlignment(JLabel.CENTER);
		previewLabel.setPreferredSize(new Dimension(70, 50));
		previewLabel.setBorder(
			BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder("Preview"),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)
		));

		// Configures layout and adds components
		setLayout(new BorderLayout(5, 5));
		add(boxPanel, BorderLayout.CENTER);
		add(previewLabel, BorderLayout.SOUTH);
		choices.getEditor().getEditorComponent().requestFocus();
	}

	/**
	 * Returns a set of patterns for formatting in the given locale,
	 * @param format for which to get a set of default patterns.
	 * @return the patterns that were found
	 */
	private static synchronized String[] getPatterns(Format format) {
		Locale locale = Locale.getDefault();
		if (format instanceof NumberFormat)
			return getNumberPatterns(locale);
		else if (format instanceof DateFormat)
			return getDatePatterns(locale);
		else
			return null;
	}

	/**
	 * Returns a set of patterns for formatting numbers in the given locale.
	 * @param locale the locale for which to fetch patterns
	 * @return the patterns that were found
	 */
	private static String[] getNumberPatterns(Locale locale) {
		// Collects formats
		NumberFormat[] formats = new NumberFormat[] {
			NumberFormat.getInstance(locale),
			NumberFormat.getNumberInstance(locale),
			NumberFormat.getPercentInstance(locale),
			NumberFormat.getCurrencyInstance(locale)};

		// Collects patterns
		Set<String> patterns = new LinkedHashSet<String>();
		for (int i = 0; i < formats.length; i++) {
			if (formats[i] instanceof DecimalFormat) {
				int digits = -1;
					if (i == 1)
						digits = 4;
					else if (i == 2)
						digits = 2;
				DecimalFormat decimal = (DecimalFormat)formats[i];
				patterns.add(decimal.toLocalizedPattern());
				for (int decimals = 0; decimals <= digits; decimals++) {
					decimal.setMinimumFractionDigits(decimals);
					decimal.setMaximumFractionDigits(decimals);
					patterns.add(decimal.toLocalizedPattern());
				}
			}
		}
		return patterns.toArray(new String[patterns.size()]);
	}

	/**
	 * Returns a set of patterns for formatting dates in the given locale.
	 * @param locale the locale for which to fetch patterns
	 * @return the patterns that were found
	 */
	private static String[] getDatePatterns(Locale locale) {
		// Collects formats
		Set<DateFormat> formats = new LinkedHashSet<DateFormat>();
		int[] codes = {DateFormat.SHORT, DateFormat.MEDIUM, DateFormat.LONG,
			DateFormat.FULL};
		for (int code : codes) {
			formats.add(DateFormat.getDateInstance(code, locale));
			formats.add(DateFormat.getTimeInstance(code, locale));
			for (int timeCode : codes)
				formats.add(DateFormat.getDateTimeInstance(code, timeCode, locale));
		}

		// Collects patterns
		SortedSet<String> patterns = new TreeSet<String>();
		for (DateFormat format : formats)
			if (format instanceof SimpleDateFormat)
				patterns.add(((SimpleDateFormat) format).toLocalizedPattern());
		return patterns.toArray(new String[patterns.size()]);
	}

	/**
	 * Returns the current format.
	 * @return the current format.
	 */
	public Format getFormat() {
		return format;
	}

	/**
	 * Set the format to configure. The default implementation accept instance
	 * of {@link DecimalFormat} or {@link SimpleDateFormat}.
	 * @param format the format to congifure.
	 * @throws IllegalArgumentException if the format is invalid.
	 */
	public void setFormat(Format format) throws IllegalArgumentException {
		Format old = this.format;
		this.format = format;
		try {
			update();
		} catch (IllegalStateException exception) {
			this.format = old;
			// The format is not one of recognized type.  Since this format was given in argument
			// (rather then the internal format field), Change the exception type for consistency
			// with the usual specification.
			IllegalArgumentException e = new IllegalArgumentException(
				exception.getLocalizedMessage());
			e.initCause(exception);
			throw e;
		}
		firePropertyChange("format", old, format);
	}

	/**
	 * Returns the localized pattern for the {@linkplain #getFormat current format}.
	 * The default implementation recognize {@link DecimalFormat} and
	 * {@link SimpleDateFormat} instances.
	 * @return The pattern for the current format.
	 * @throws IllegalStateException is the current format is not one of recognized type.
	 */
	public String getPattern() throws IllegalStateException {
		if (format instanceof DecimalFormat)
			return ((DecimalFormat) format).toLocalizedPattern();
		if (format instanceof SimpleDateFormat)
			return ((SimpleDateFormat) format).toLocalizedPattern();
		throw new IllegalStateException();
	}

	/**
	 * Sets the localized pattern for the {@linkplain #getFormat current format}.
	 * The default implementation recognize {@link DecimalFormat} and
	 * {@link SimpleDateFormat} instances.
	 * @param  pattern The pattern for the current format.
	 * @throws IllegalStateException is the current format is not one of recognized type.
	 * @throws IllegalArgumentException if the specified pattern is invalid.
	 */
	public void setPattern(String pattern)
			throws IllegalStateException, IllegalArgumentException {
		if (format instanceof DecimalFormat)
			((DecimalFormat) format).applyLocalizedPattern(pattern);
		else if (format instanceof SimpleDateFormat)
			((SimpleDateFormat) format).applyLocalizedPattern(pattern);
		else
			throw new IllegalStateException();
		update();
	}

	/**
	 * Update the preview text according the current format pattern.
	 */
	private void update() {
		choices.setSelectedItem(getPattern());
		try {
			previewLabel.setText(value!=null ? format.format(value) : null);
			previewLabel.setForeground(getForeground());
		} catch (IllegalArgumentException exception) {
			previewLabel.setText(exception.getLocalizedMessage());
			previewLabel.setForeground(ERROR_COLOR);
		}
	}

	/**
	 * Apply the currently selected pattern. If <code>add</code> is <code>true</code>,
	 * then the pattern is added to the combo box list.
	 * @param  add <code>true</code> for adding the pattern to the combo box list.
	 * @return <code>true</code> if the pattern is valid.
	 */
	private boolean applyPattern(boolean add) {
		String pattern = choices.getSelectedItem().toString();
		if (pattern.trim().length() == 0) {
			update();
			return false;
		}
		try {
			setPattern(pattern);
		} catch (RuntimeException exception) {
			/* The pattern is not valid. Replace the value by an error message */
			previewLabel.setText(exception.getLocalizedMessage());
			previewLabel.setForeground(ERROR_COLOR);
			return false;
		}
		if (add) {
			DefaultComboBoxModel model = (DefaultComboBoxModel)choices.getModel();
			pattern = choices.getSelectedItem().toString();
			int index = model.getIndexOf(pattern);
			if (index > 0)
				model.removeElementAt(index);
			if (index != 0)
				model.insertElementAt(pattern, 0);
			int size = model.getSize();
			while (size > HISTORY_SIZE)
				model.removeElementAt(size-1);
			if (size != 0)
				choices.setSelectedIndex(0);
		}
		return true;
	}

	/**
	 * Shows a dialog box requesting input from the user.
	 * @param owner the parent component for the dialog box
	 * @param  title the dialog box title
	 * @return the selected format or, if the user did not press OK, null
	 */
	public Format showDialog(Component owner, String title) {
		int returnValue = JOptionPane.showConfirmDialog(
			owner,
			this,
			title,
			JOptionPane.OK_CANCEL_OPTION,
			JOptionPane.PLAIN_MESSAGE,
			(Icon)null);
		if (returnValue == JOptionPane.OK_OPTION)
			if (applyPattern(true))
				return getFormat();
		return null;
	}
}