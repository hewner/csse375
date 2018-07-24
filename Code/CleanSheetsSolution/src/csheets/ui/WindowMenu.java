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
package csheets.ui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.KeyStroke;

import csheets.CleanSheets;
import csheets.SpreadsheetAppEvent;
import csheets.SpreadsheetAppListener;
import csheets.core.Workbook;
import csheets.ui.ctrl.SelectionEvent;
import csheets.ui.ctrl.SelectionListener;
import csheets.ui.ctrl.UIController;

/**
 * A menu for listing the open workbooks, and allowing the user to navigate
 * between them.
 * @author Einar Pehrson
 */
@SuppressWarnings("serial")
public class WindowMenu extends JMenu implements SelectionListener, SpreadsheetAppListener {

	/** The user interface controller */
	private UIController uiController;

	/** The workbook selection items */
	private Map<Workbook, JCheckBoxMenuItem> items
		= new HashMap<Workbook, JCheckBoxMenuItem>();

	/** The button group to which workbook selection items are added */
	private ButtonGroup group = new ButtonGroup();

	/** The number of menu items on the menu not denoting workbooks */
	private int nonWorkbookItems;

	/**
	 * Creates a window menu.
	 * @param app the CleanSheets application
	 * @param uiController the user interface controller
	 */
	public WindowMenu(CleanSheets app, UIController uiController) {
		super("Window");
		this.uiController = uiController;
		app.addSpreadsheetAppListener(this);
		uiController.addSelectionListener(this);
		setMnemonic(KeyEvent.VK_W);
	}

	/**
	 * Adds an item for the workbook that was created.
	 * @param event the spreadsheet application event that occured
	 */
	public void workbookCreated(SpreadsheetAppEvent event) {
		// Creates item and names it
		JCheckBoxMenuItem item = new JCheckBoxMenuItem(
			new SelectWorkbookAction(event.getWorkbook(), uiController));
		int index = getItemCount() - nonWorkbookItems + 1;
		File file = event.getFile();
		item.setText(file == null ? " Untitled" : file.getName());

		// Accelerates item
		KeyStroke stroke = KeyStroke.getKeyStroke("ctrl " + index);
		item.setMnemonic(stroke.getKeyChar());
		item.setAccelerator(stroke);
		item.setSelected(true);

		// Adds itme
		items.put(event.getWorkbook(), item);
		group.add(item);
		add(item);
	}

	/**
	 * Adds an item for the workbook that was loaded.
	 * @param event the spreadsheet application event that occured
	 */
	public void workbookLoaded(SpreadsheetAppEvent event) {
		workbookCreated(event);
	}

	/**
	 * Removes the item of the workbook that was unloaded, and renames the other.
	 * @param event the spreadsheet application event that occured
	 */
	public void workbookUnloaded(SpreadsheetAppEvent event) {
		// Removes the item
		JCheckBoxMenuItem item = items.remove(event.getWorkbook());
		if (item != null)
			remove(item);

		// Updates remaining items
		int index = 1;
		for (Component c : getMenuComponents()) {
			item = (JCheckBoxMenuItem)c;
			KeyStroke stroke = KeyStroke.getKeyStroke("ctrl " + index++);
			item.setMnemonic(stroke.getKeyChar());
			item.setAccelerator(stroke);
		}
	}

	/**
	 * Renames the item of the workbook that was saved.
	 * @param event the spreadsheet application event that occured
	 */
	public void workbookSaved(SpreadsheetAppEvent event) {
		items.get(event.getWorkbook()).setText(event.getFile().getName());;
	}

	/**
	 * Selects the item of the workbook that was selected.
	 * @param event the selection event that was fired
	 */
	public void selectionChanged(SelectionEvent event) {
		JCheckBoxMenuItem item = items.get(event.getWorkbook());
		if (item != null)
			item.setSelected(true);
	}

	/**
	 * An action for selecting a workbook.
	 * @author Einar Pehrson
	 */
	@SuppressWarnings("serial")
	protected static class SelectWorkbookAction extends AbstractAction {

		/** The workbook to select */
		private Workbook workbook;

		/** The user interface controller */
		private UIController uiController;

		/**
		 * Creates a new workbook selection action.
		 * @param workbook the workbook to select
		 * @param uiController the user interface controller
		 */
		public SelectWorkbookAction(Workbook workbook, UIController uiController) {
			this.workbook = workbook;
			this.uiController = uiController;
		}

		public void actionPerformed(ActionEvent e) {
			uiController.setActiveWorkbook(workbook);
		}
	}
}