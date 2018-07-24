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
import java.util.Properties;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;

import csheets.CleanSheets;
import csheets.SpreadsheetAppEvent;
import csheets.SpreadsheetAppListener;
import csheets.ui.ctrl.OpenAction;
import csheets.ui.ctrl.UIController;

/**
 * A menu for displaying recently opened files, and allowing the user to
 * reopen them.
 * @author Einar Pehrson
 */
@SuppressWarnings("serial")
public class ReopenMenu extends JMenu implements SpreadsheetAppListener {

	/** The CleanSheets application */
	private CleanSheets app;

	/** The maximum number of items in the menu */
	private int maximumItems = 10;

	/** The application properties */
	private Properties props;

	/** The number of menu items on the menu not denoting files */
	private int nonReopenItems;

	/** The user interface controller */
	private UIController uiController;

	/**
	 * Creates a reopen menu, and creates items using the given properties (if available).
	 * @param app the CleanSheets application
	 * @param uiController the user interface controller
	 */
	public ReopenMenu(CleanSheets app, UIController uiController) {
		super("Reopen");

		// Stores members
		this.app = app;
		this.uiController = uiController;
		this.props = app.getUserProperties();

		// Configures menu
		app.addSpreadsheetAppListener(this);
		setMnemonic(KeyEvent.VK_R);
		setIcon(new ImageIcon(CleanSheets.class.getResource("res/img/reopen.gif")));

		if (props != null) {
			// Loads recent files from properties and adds menu items
			String filename;
			for (int i = 0; (filename = props.getProperty("recentfile" + i)) != null; i++) {
				File file = new File(filename);
				if (file.exists()) addReopenItem(file, false);
			}
		}

		// Adds removal items
		addSeparator();
		add(new RemoveObsoleteAction());
		add(new RemoveAllAction());
		nonReopenItems = 3;
	}

	/**
	 * Adds an item for this file to the top of the reopen menu.
	 * @param file the filename of the file
	 * @return the item that was added
	 */
	public JMenuItem addReopenItem(File file) {
		return addReopenItem(file, true);
	}

	/**
	 * Adds an item for this file to the top of the reopen menu.
	 * @param file the filename of the file
	 * @param updateProperties whether to update properties after adding the iten
	 * @return the item that was added
	 */
	private JMenuItem addReopenItem(File file, boolean updateProperties) {
		// Removes any existing identical items
		Component[] items = getMenuComponents();
		for (int i = 0; i < items.length; i++) {
			// Breaks at separator
			if (items[i] instanceof JSeparator) break;

			// Removes item, if identical
			JMenuItem item = (JMenuItem)items[i];
			if (file.getAbsolutePath().equals(item.getText()))
				remove(item);
		}

		// Adds the item to the menu and trims the menu to the appropriate size
		JMenuItem item = insert(new ReopenAction(app, uiController, file), 0);
		while (getMenuComponentCount() - nonReopenItems > maximumItems) remove(maximumItems);

		// Updates properties
		if (updateProperties) 
			updateProperties();

		return item;
	}

	/**
	 * Removes all obsolete file items from the reopen menu, 
	 * i.e. the items referring to files that don't exist.
	 */
	public void removeObsolete() {
		Component[] items = getMenuComponents();
		for (int i = 0; i < items.length; i++) {
			// Breaks at separator
			if (items[i] instanceof JSeparator) break;

			// Removes item, if obsolete
			JMenuItem item = (JMenuItem)items[i];
			if (!new File(item.getText()).exists())
				remove(item);
		}

		// Updates properties
		updateProperties();
	}

	/**
	 * Removes all file items from the reopen menu, 
	 */
	public void removeAll() {
		Component[] items = getMenuComponents();
		for (int i = 0; i < items.length; i++) {
			// Breaks at separator
			if (items[i] instanceof JSeparator) break;

			// Removes item
			remove(items[i]);
		}

		// Updates properties
		updateProperties();
	}

	/**
	 * Sets the maximum number of reopen items in the menu.
	 * @param items the number of reopen items in the menu
	 */
	public void setMaximumItems(int items) {
		this.maximumItems = items;
	}

	/**
	 * Updates the recent files in the application properties.
	 */
	private void updateProperties() {
		if (props != null) {
			// Stores the current recent files
			int i = 0;
			for (int n = getMenuComponentCount() - nonReopenItems; i < n; i++)
				props.setProperty("recentfile" + (n - i - 1),
					((JMenuItem)getMenuComponent(i)).getText());

			for (; (props.getProperty("recentfile" + i)) != null; i++)
				props.remove("recentfile" + i);
		}
	}

	public void workbookCreated(SpreadsheetAppEvent event) {}

	public void workbookLoaded(SpreadsheetAppEvent event) {
		addReopenItem(event.getFile(), true);
	}

	public void workbookUnloaded(SpreadsheetAppEvent event) {}

	public void workbookSaved(SpreadsheetAppEvent event) {
		addReopenItem(event.getFile(), true);
	}

	/**
	 * An action for reopening a spreadsheet.
	 * @author Einar Pehrson
	 */
	@SuppressWarnings("serial")
	protected static class ReopenAction extends OpenAction {

		/** The file to open */
		private File file;

		/**
		 * Creates a new reopen action.
		 * @param app the CleanSheets application
		 * @param uiController the user interface controller
		 * @param file the file to open
		 */
		public ReopenAction(CleanSheets app, UIController uiController,
				File file) {
			super(app, uiController, null);
			this.file = file;
			setEnabled(true);
			putValue(NAME, file.getAbsolutePath());
			putValue(ACTION_COMMAND_KEY, file.getAbsolutePath());
		}

		protected void defineProperties() {
			putValue(SHORT_DESCRIPTION, null);
			putValue(SMALL_ICON, null);
		}

		public File getFile() {
			return file;
		}
	}

	/**
	 * An action for removing the obsolete items from the menu.
	 */
	@SuppressWarnings("serial")
	protected class RemoveObsoleteAction extends AbstractAction {

		/**
		 * Creates a new remove obsolete action.
		 */
		public RemoveObsoleteAction() {
			// Configures action
			String name = "Remove obsolete";
			putValue(NAME, name);
			putValue(SHORT_DESCRIPTION, name);
			putValue(ACTION_COMMAND_KEY, name);
			putValue(MNEMONIC_KEY, KeyEvent.VK_O);
		}

		public void actionPerformed(ActionEvent e) {
			removeObsolete();
		}
	}

	/**
	 * An action for removing all items from the menu.
	 */
	@SuppressWarnings("serial")
	protected class RemoveAllAction extends AbstractAction {

		/**
		 * Creates a new remove all action.
		 */
		public RemoveAllAction() {
			// Configures action
			String name = "Remove all";
			putValue(NAME, name);
			putValue(SHORT_DESCRIPTION, name);
			putValue(ACTION_COMMAND_KEY, name);
			putValue(MNEMONIC_KEY, KeyEvent.VK_R);
		}

		public void actionPerformed(ActionEvent e) {
			removeAll();
		}
	}
}