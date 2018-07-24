/*
 * Copyright (c) 2005 Einar Pehrson
 *
 * This file is part of
 * CleanSheets Extension for Style
 *
 * CleanSheets Extension for Style is free software; you can
 * redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either version 2 of
 * the License, or (at your option) any later version.
 *
 * CleanSheets Extension for Style is distributed in the hope that
 * it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with CleanSheets Extension for Style; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA  02111-1307  USA
 */
package csheets.ext.style;

import java.awt.Color;
import java.awt.Font;
import java.text.DateFormat;
import java.text.Format;
import java.text.NumberFormat;

import javax.swing.BorderFactory;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;

import csheets.core.Cell;
import csheets.core.Value;
import csheets.ext.CellExtension;

/**
 * An extension of a cell in a spreadsheet, with support for style, i.e. font,
 * format, alignment, color and border.
 * @author Einar Pehrson
 */
public class StylableCell extends CellExtension {

	/** The unique version identifier used for serialization */
	private static final long serialVersionUID = -4860391005521272291L;

	/** The default number format */
	public static final NumberFormat NUMBER_FORMAT	= NumberFormat.getInstance();

	/** The default date format */
	public static final DateFormat DATE_FORMAT
		= DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);

	/** The default font */
	public static final Font FONT = UIManager.getFont("Table.font");

	/** The default vertical alignment */
	public static final int VERTICAL_ALIGNMENT = SwingConstants.CENTER;

	/** The default foreground color */
	public static final Color FOREGROUND = UIManager.getColor("Table.foreground");

	/** The default background color */
	public static final Color BACKGROUND = UIManager.getColor("Table.background");

	/** The default empty border */
	public static final Border BORDER
		= BorderFactory.createEmptyBorder(1, 1, 1, 1);

	/** The format applied to the cell's value before rendering */
	private Format format;

	/** The font used when rendering the cell's content */
	private Font font;

	/** The horizontal alignment of the cell's content */
	private int hAlignment;

	/** The vertical alignment of the cell's content */
	private int vAlignment;

	/** The color used when rendering the cell's content */
	private Color fgColor;

	/** The background color of the cell */
	private Color bgColor;

	/** The border of the cell */
	private Border border;

	/**
	 * Creates a stylable cell extension for the given cell.
	 * @param cell the cell to extend
	 */
	StylableCell(Cell cell) {
		super(cell, StyleExtension.NAME);
		resetStyle();
	}

	/**
	 * Returns whether a format can be applied to the cell's value.
	 * @return whether a format can be applied to the cell's value
	 */
	public boolean isFormattable() {
		Value.Type valueType = getValue().getType();
		return valueType == Value.Type.NUMERIC || valueType == Value.Type.DATE;
	}

	/**
	 * Returns the format applied to the cell's value before rendering.
	 * @return the format applied to the cell's value before rendering
	 */
	public Format getFormat() {
		switch (getValue().getType()) {
			case NUMERIC:
				return format instanceof NumberFormat ? format : NUMBER_FORMAT;
			case DATE:
				return format instanceof DateFormat ? format : DATE_FORMAT;
			default:
				return null;
		}
	}

	/**
	 * Returns the font used when rendering the cell's content.
	 * @return the font used when rendering the cell's content
	 */
	public Font getFont() {
		return font;
	}

	/**
	 * Returns the horizontal alignment of the cell's content.
	 * @return the horizontal alignment of the cell's content
	 */
	public int getHorizontalAlignment() {
		if (hAlignment == -1)
			// Returns default alignment
			switch (getValue().getType()) {
				case NUMERIC:
				case DATE: 		return SwingConstants.RIGHT;
				case BOOLEAN:
				case ERROR: 	return SwingConstants.CENTER;
				default:		return SwingConstants.LEFT;
			}
		else
			return hAlignment;
	}

	/**
	 * Returns the vertical alignment of the cell's content.
	 * @return the vertical alignment of the cell's content
	 */
	public int getVerticalAlignment() {
		return vAlignment;
	}

	/**
	 * Returns the color used when rendering the cell's content.
	 * @return the color used when rendering the cell's content
	 */
	public Color getForegroundColor() {
		return fgColor;
	}

	/**
	 * Returns the background color of the cell.
	 * @return the background color of the cell
	 */
	public Color getBackgroundColor() {
		return bgColor;
	}

	/**
	 * Returns the border of the cell.
	 * @return the border of the cell
	 */
	public Border getBorder() {
		return border;
	}

	/**
	 * Sets the format applied to the cell's value before rendering.
	 * @param format the format applied to the cell's value before rendering
	 */
	public void setFormat(Format format) {
		this.format = format;
	}

	/**
	 * Sets the font used when rendering the cell's content.
	 * @param font the font used when rendering the cell's content
	 */
	public void setFont(Font font) {
		this.font = font;
	}

	/**
	 * Sets the horizontal alignment of the cell's content.
	 * @param hAlignment the horizontal alignment of the cell's content
	 */
	public void setHorizontalAlignment(int hAlignment) {
		if (hAlignment == SwingConstants.LEFT
		 ||	hAlignment == SwingConstants.CENTER
		 ||	hAlignment == SwingConstants.RIGHT)
		 	this.hAlignment = hAlignment;
		 else throw new IllegalArgumentException("Illegal alignment");
	}

	/**
	 * Sets the vertical alignment of the cell's content.
	 * @param vAlignment the vertical alignment of the cell's content
	 */
	public void setVerticalAlignment(int vAlignment) {
		if (vAlignment == SwingConstants.TOP
		 ||	vAlignment == SwingConstants.CENTER
		 ||	vAlignment == SwingConstants.BOTTOM)
			this.vAlignment = vAlignment;
		 else throw new IllegalArgumentException("Illegal alignment");
	}

	/**
	 * Sets the color used when rendering the cell's content.
	 * @param fgColor the color used when rendering the cell's content
	 */
	public void setForegroundColor(Color fgColor) {
		this.fgColor = fgColor;
	}

	/**
	 * Sets the background color of the cell.
	 * @param bgColor the background color of the cell
	 */
	public void setBackgroundColor(Color bgColor) {
		this.bgColor = bgColor;
	}

	/**
	 * Sets the border of the cell.
	 * @param border the border of the cell
	 */
	public void setBorder(Border border) {
		this.border = border;
	}

	/**
	 * Restes the style of the cell.
	 */
	public void resetStyle() {
		this.format		= null;
		this.font		= FONT;
		this.hAlignment	= -1;
		this.vAlignment	= VERTICAL_ALIGNMENT;
		this.fgColor	= FOREGROUND;
		this.bgColor	= BACKGROUND;
		this.border		= BORDER;
	}

	/**
	 * Removes the style from the cell.
	 * @param cell the cell that was modified
	 */
	public void cellCleared(Cell cell) {
		if (this.getDelegate().equals(cell)) {
			resetStyle();
		}
	}

	/**
	 * Copies the style from the source cell to this one.
	 * @param cell the cell that was modified
	 * @param source the cell from which data was copied
	 */
	public void cellCopied(Cell cell, Cell source) {
		if (this.getDelegate().equals(cell)) {
			StylableCell stylableSource = (StylableCell)source.getExtension(
				StyleExtension.NAME);
			this.format		= stylableSource.format;
			this.font		= stylableSource.font;
			this.hAlignment	= stylableSource.hAlignment;
			this.vAlignment	= stylableSource.vAlignment;
			this.fgColor	= stylableSource.fgColor;
			this.bgColor	= stylableSource.bgColor;
			this.border		= stylableSource.border;
		}
	}
}