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

import java.awt.Container;
import java.awt.event.ActionEvent;

import javax.swing.JComponent;
import javax.swing.JTabbedPane;

import csheets.ui.ctrl.FocusOwnerAction;

/**
 * An action for showing and hiding UI extension side bars.
 * @author Einar Pehrson
 */
@SuppressWarnings("serial")
public class SideBarAction extends FocusOwnerAction {

	/** The extension to control*/
	private UIExtension extension;

	/** The component to control */
	private JComponent component;

	/** The side bar pane */
	private JTabbedPane sideBar;

	/**
	 * Creates a new side bar action.
	 * @param extension the extension to control
	 * @param component the component to control
	 */
	public SideBarAction(UIExtension extension, JComponent component) {
		// Stores members
		this.extension = extension;
		this.component = component;

		// Fetches parent, and removes component if appropriate
		Container parent = component.getParent();
		if (parent instanceof JTabbedPane)
			sideBar = (JTabbedPane)parent;
		if (!component.isEnabled())
			sideBar.remove(component);

		// Configures action
		String name = extension.getExtension().getName();
		putValue(NAME, name);
		putValue(SHORT_DESCRIPTION, name);
		putValue(ACTION_COMMAND_KEY, name);
		putValue(SMALL_ICON, extension.getIcon());
	}

	protected String getName() {
		return null;
	}

	/**
	 * Toggles the visiblity of the component.
	 * @param event the event that was fired
	 */
	public void actionPerformed(ActionEvent event) {
		if (sideBar != null) {
			// Toggles component
			if (component.isEnabled())
				sideBar.remove(component);
			else {
				int i = 0;
				for (; i < sideBar.getTabCount()
					&& component.getName().compareTo(sideBar.getTitleAt(i)) < 0; i++);
				sideBar.insertTab(extension.getExtension().getName(),
					extension.getIcon(), component, null, i);
			}

			// Toggles properties
			component.setEnabled(!component.isEnabled());
			extension.setEnabledProperty("sidebar", component.isEnabled());
		}
	}
}