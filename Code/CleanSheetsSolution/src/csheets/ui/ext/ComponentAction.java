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

import java.awt.event.ActionEvent;

import javax.swing.JComponent;

import csheets.ui.ctrl.FocusOwnerAction;

/**
 * An action for enabling and disabling UI extension components.
 * @author Einar Pehrson
 */
@SuppressWarnings("serial")
public class ComponentAction extends FocusOwnerAction {

	/** The extension to control*/
	private UIExtension extension;

	/** The component to control */
	private JComponent component;

	/** The property key of the component */
	private String componentKey;

	/**
	 * Creates a new component action.
	 * @param extension the extension to control
	 * @param component the component to control
	 * @param componentKey the property key of the component
	 */
	public ComponentAction(UIExtension extension, JComponent component,
			String componentKey) {
		// Stores members
		this.extension = extension;
		this.component = component;
		this.componentKey = componentKey;

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
		component.setVisible(!component.isVisible());
		extension.setEnabledProperty(componentKey, component.isVisible());
	}
}