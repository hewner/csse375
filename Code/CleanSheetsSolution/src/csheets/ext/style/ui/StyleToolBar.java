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
package csheets.ext.style.ui;

import java.awt.Insets;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;

import csheets.ext.style.StylableCell;
import csheets.ext.style.StyleExtension;
import csheets.ui.ctrl.SelectionEvent;
import csheets.ui.ctrl.SelectionListener;
import csheets.ui.ctrl.UIController;

/**
 * A tool bar that displays style-related actions.
 * @author Einar Pehrson
 */
@SuppressWarnings("serial")
public class StyleToolBar extends JToolBar implements SelectionListener {

	/** The common button insets */
	private static final Insets INSETS = new Insets(2, 2, 2, 2);

	/** The button for making the current font bold */
	private JToggleButton boldButton;

	/** The button for making the current font italic */
	private JToggleButton italicButton;

	/** The button for applying left alignment */
	private Map<Integer, JToggleButton> hAlignButtons
		= new HashMap<Integer, JToggleButton>();

	/**
	 * Creates a new style tool bar.
	 * @param uiController the user interface controller
	 */
	public StyleToolBar(UIController uiController) {
		super("Style");
		uiController.addSelectionListener(this);

		// Adds font actions
		add(new FontAction(uiController));
		boldButton = addToggleButton(new BoldAction(uiController));
		italicButton = addToggleButton(new ItalicAction(uiController));
		addSeparator();

		// Adds color actions
		add(new FormatAction(uiController));
		add(new BorderAction(uiController));
		add(new ForegroundAction(uiController));
		add(new BackgroundAction(uiController));
		addSeparator();

		// Adds alignment actions
		ButtonGroup hAlignGroup = new ButtonGroup();
		hAlignButtons.put(SwingConstants.LEFT, addToggleButton(
			new AlignLeftAction(uiController), hAlignGroup));
		hAlignButtons.put(SwingConstants.CENTER, addToggleButton(
			new AlignCenterAction(uiController), hAlignGroup));
		hAlignButtons.put(SwingConstants.RIGHT, addToggleButton(
			new AlignRightAction(uiController), hAlignGroup));
	}

	/**
	 * Adds a button with the given action to the tool bar, and reduces the
	 * default insets.
	 * @param action the action to add
	 * @return the button that was added
	 */
	public JButton add(Action action) {
		JButton button = super.add(action);
		button.setMargin(INSETS);
		return button;
	}

	/**
	 * Adds a button with the given action to the tool bar, and reduces the
	 * default insets.
	 * @param action the action to add
	 * @param groups the button groups to which the button belongs
	 * @return the button that was added
	 */
	public JToggleButton addToggleButton(Action action, ButtonGroup... groups) {
		JToggleButton button = new JToggleButton(action);
		button.setText(null);
		button.setMargin(INSETS);
		add(button);
		for (ButtonGroup group : groups)
			group.add(button);
		return button;
	}

	/**
	 * Selects buttons depending on the style of the active cell.
	 * @param event the selection event that was fired
	 */
	public void selectionChanged(SelectionEvent event) {
		if (event.getCell() != null && event.isCellChanged()) {
			StylableCell cell = (StylableCell)event.getCell().getExtension(
				StyleExtension.NAME);
			boldButton.setSelected(cell.getFont().isBold());
			italicButton.setSelected(cell.getFont().isItalic());
			hAlignButtons.get(cell.getHorizontalAlignment()).setSelected(true);
		}
	}
}