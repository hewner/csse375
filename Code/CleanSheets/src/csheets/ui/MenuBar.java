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

import java.awt.event.KeyEvent;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

import csheets.CleanSheets;
import csheets.ui.ctrl.ActionManager;
import csheets.ui.ctrl.UIController;
import csheets.ui.ext.CellDecorator;
import csheets.ui.ext.CellDecoratorAction;
import csheets.ui.ext.ComponentAction;
import csheets.ui.ext.SideBarAction;
import csheets.ui.ext.TableDecorator;
import csheets.ui.ext.TableDecoratorAction;
import csheets.ui.ext.UIExtension;

/**
 * The menu bar.
 * @author Einar Pehrson
 */
@SuppressWarnings("serial")
public class MenuBar extends JMenuBar {

	/**
	 * Creates the menu bar.
	 * @param app the CleanSheets application
	 * @param actionManager a manager for actions
	 * @param uiController the user interface controller
	 */
	public MenuBar(CleanSheets app, ActionManager actionManager, UIController uiController) {
		// Creates the file menu
		JMenu fileMenu = addMenu("File", KeyEvent.VK_F);
		fileMenu.add(actionManager.getAction("new"));
		fileMenu.add(actionManager.getAction("open"));
		fileMenu.add(new ReopenMenu(app, uiController));
		fileMenu.add(actionManager.getAction("save"));
		fileMenu.add(actionManager.getAction("saveas"));
		fileMenu.addSeparator();
		fileMenu.add(actionManager.getAction("print"));
		fileMenu.addSeparator();
		fileMenu.add(actionManager.getAction("close"));
		fileMenu.add(actionManager.getAction("closeall"));
		fileMenu.add(actionManager.getAction("exit"));

		// Creates the edit menu
		JMenu editMenu = addMenu("Edit", KeyEvent.VK_E);
		editMenu.add(actionManager.getAction("undo"));
		editMenu.add(actionManager.getAction("redo"));
		editMenu.addSeparator();
		editMenu.add(actionManager.getAction("cut"));
		editMenu.add(actionManager.getAction("copy"));
		editMenu.add(actionManager.getAction("paste"));
		editMenu.addSeparator();
		editMenu.add(actionManager.getAction("selectall"));
		editMenu.add(actionManager.getAction("sort"));
		editMenu.add(actionManager.getAction("search"));
		editMenu.add(actionManager.getAction("clear"));
		editMenu.addSeparator();
		editMenu.add(actionManager.getAction("prefs"));

		// Creates the view menu
		JMenu viewMenu = addMenu("View", KeyEvent.VK_V);

		JMenu sideBarMenu = (JMenu)viewMenu.add(new JMenu("Side Bars"));
		sideBarMenu.setMnemonic(KeyEvent.VK_S);
		sideBarMenu.setIcon(new ImageIcon(CleanSheets.class.getResource("res/img/sidebar.gif")));

		JMenu toolBarMenu = (JMenu)viewMenu.add(new JMenu("Tool Bars"));
		toolBarMenu.setMnemonic(KeyEvent.VK_T);
		toolBarMenu.setIcon(new ImageIcon(CleanSheets.class.getResource("res/img/toolbar.gif")));

		viewMenu.addSeparator();
		JMenu cellDecMenu = (JMenu)viewMenu.add(new JMenu("Cell Decorators"));
		cellDecMenu.setMnemonic(KeyEvent.VK_C);
		cellDecMenu.setIcon(new BlankIcon(16));

		JMenu tableDecMenu = (JMenu)viewMenu.add(new JMenu("Table Decorators"));
		tableDecMenu.setMnemonic(KeyEvent.VK_C);
		tableDecMenu.setIcon(new BlankIcon(16));

		// Populates the view sub-menus
		for (UIExtension extension : uiController.getExtensions()) {

			CellDecorator cellDec = extension.getCellDecorator();
			if (cellDec != null)
				cellDecMenu.add(new JCheckBoxMenuItem(new CellDecoratorAction(
					extension))).setSelected(cellDec.isEnabled());

			TableDecorator tableDec = extension.getTableDecorator();
			if (tableDec != null)
				tableDecMenu.add(new JCheckBoxMenuItem(new TableDecoratorAction(
					extension))).setSelected(tableDec.isEnabled());

			JComponent sideBar = extension.getSideBar();
			if (sideBar != null)
				sideBarMenu.add(new JCheckBoxMenuItem(
					new SideBarAction(extension, sideBar))
				).setSelected(sideBar.isEnabled());

			JComponent toolBar = extension.getToolBar();
			if (toolBar != null)
				toolBarMenu.add(new JCheckBoxMenuItem(
					new ComponentAction(extension, toolBar, "toolbar"))
				).setSelected(toolBar.isVisible());
		}

		// Creates the spreadsheet menu
		JMenu sheetMenu = addMenu("Spreadsheet", KeyEvent.VK_S);
		sheetMenu.add(actionManager.getAction("addsheet"));
		sheetMenu.add(actionManager.getAction("removesheet"));
		sheetMenu.add(actionManager.getAction("renamesheet"));
		sheetMenu.addSeparator();
		sheetMenu.add(actionManager.getAction("insertcolumn"));
		sheetMenu.add(actionManager.getAction("removecolumn"));
		sheetMenu.add(actionManager.getAction("insertrow"));
		sheetMenu.add(actionManager.getAction("removerow"));

		// Creates the extension menus
		JMenu extensionsMenu = addMenu("Extensions", KeyEvent.VK_X);
		for (UIExtension extension : uiController.getExtensions()) {
			JMenu extensionMenu = extension.getMenu();
			if (extensionMenu != null) {
				// Updates icon
				if (extensionMenu.getIcon() == null) {
					Icon icon = extension.getIcon();
					extensionMenu.setIcon(icon != null ? icon : new BlankIcon(16));
				}
	
				// Adds menu
				extensionsMenu.add(extensionMenu);
			}
		}

		// Creates the window menu
		add(new WindowMenu(app, uiController));

		// Creates the help menu
		JMenu helpMenu = addMenu("Help", KeyEvent.VK_H);
		helpMenu.add(actionManager.getAction("help"));
		helpMenu.addSeparator();
		helpMenu.add(actionManager.getAction("license"));
		helpMenu.add(actionManager.getAction("about"));
}

	/**
	 * Creates a menu and adds it to the menu bar.
	 * @param name		The name of the menu.
	 * @param mnemonic	The shortcut-key to access the menu.
	 * @return		The menu created.
	 */
	private JMenu addMenu(String name, int mnemonic) {
		JMenu menu = new JMenu(name);
		menu.setMnemonic(mnemonic);
		return add(menu);
	}
}