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
package csheets.ui.ext;

import java.util.Properties;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JToolBar;

import csheets.ext.Extension;
import csheets.ui.ctrl.UIController;

/**
 * <p>A base class for user interface extensions to the CleanSheets application.
 * Subclasses should override the accessor methods for the components that they
 * provide, and add components as selection listeners whenever required.
 * <p>A user interface extension should only create one instance of each type of
 * component, and therefore always return the same component when a given
 * method is called.
 * @see csheets.ui.ctrl.SelectionListener
 * @author Einar Pehrson
 */
public abstract class UIExtension {

	/** The name of the extension */
	protected final Extension extension;

	/** The user interface controller */
	protected final UIController uiController;

	/** The application's user properties */
	protected final Properties props;

	/**
	 * Creates a new user interface extension..
	 * @param extension the extension for which components are provided
	 * @param uiController the user interface controller
	 */
	public UIExtension(Extension extension, UIController uiController) {
		// Stores member
		this.extension = extension;
		this.uiController = uiController;
		this.props = uiController.getUserProperties();

		// Enables components according to properties
		if (getCellDecorator() != null)
			getCellDecorator().setEnabled(Boolean.TRUE.equals(
				getEnabledProperty("celldecorator")));
		if (getTableDecorator() != null)
			getTableDecorator().setEnabled(Boolean.TRUE.equals(
				getEnabledProperty("tabledecorator")));
		if (getToolBar() != null)
			getToolBar().setVisible(Boolean.TRUE.equals(
				getEnabledProperty("toolbar")));
		if (getSideBar() != null)
			getSideBar().setEnabled(Boolean.TRUE.equals(
				getEnabledProperty("sidebar")));
	}

	/**
	 * Returns the extension for which this UI extension provides components.
	 * @return the extension for which this UI extension provides components
	 */
	public final Extension getExtension() {
		return extension;
	}

	/**
	 * Returns the enabled property corresponding to the given component key.
	 * @param propKey the property key of the relevant component
	 * @return a Boolean value if a property is found, null otherwise
	 */
	public final Boolean getEnabledProperty(String propKey) {
		String prop = props.getProperty(extension.getPropertyKey() + propKey);
		if (prop == null)
			return null;
		else if (prop.equalsIgnoreCase("true"))
			return true;
		else if (prop.equalsIgnoreCase("false"))
			return false;
		else
			return null;
	}

	/**
	 * Sets the enabled property corresponding to the given component key.
	 * @param propKey the property key of the relevant component
	 * @param enabled whether the component should be enabled or not
	 */
	public final void setEnabledProperty(String propKey, boolean enabled) {
		props.setProperty(extension.getPropertyKey() + propKey,
			Boolean.toString(enabled));
	}

	/**
	 * Returns an icon to display with the extension's name.
	 * @return an icon, or null if the extension does not provide one
	 */
	public Icon getIcon() {
		return null;
	}

	/**
	 * Returns a cell decorator that visualizes the data added by the extension.
	 * @return a cell decorator, or null if the extension does not provide one
	 */
	public CellDecorator getCellDecorator() {
		return null;
	}

	/**
	 * Returns a table decorator that visualizes the data added by the extension.
	 * @return a table decorator, or null if the extension does not provide one
	 */
	public TableDecorator getTableDecorator() {
		return null;
	}

	/**
	 * Returns a menu component that gives access to extension-specific
	 * functionality.
	 * @return a JMenu component, or null if the extension does not provide one
	 */
	public JMenu getMenu() {
		return null;
	}

	/**
	 * Returns a toolbar that gives access to extension-specific
	 * functionality.
	 * @return a JToolBar component, or null if the extension does not provide one
	 */
	public JToolBar getToolBar() {
		return null;
	}

	/**
	 * Returns a side bar that gives access to extension-specific
	 * functionality.
	 * @return a component, or null if the extension does not provide one
	 */
	public JComponent getSideBar() {
		return null;
	}
}