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
package csheets.ui.ctrl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import csheets.CleanSheets;
import csheets.SpreadsheetAppEvent;
import csheets.SpreadsheetAppListener;
import csheets.ui.FileChooser;

/**
 * A manager for UI actions.
 * @author Einar Pehrson
 */
public class ActionManager {

	/** The map of file actions */
	private Map<String, BaseAction> actions = new HashMap<String, BaseAction>();

	/** The action that should only be enabled when the active workbook is modified */
	private List<BaseAction> modificationActions = new ArrayList<BaseAction>();

	/** The action that should only be enabled when the active workbook is stored in a file */
	private List<BaseAction> fileActions = new ArrayList<BaseAction>();

	/**
	 * Creates a new action manager.
	 * @param app the CleanSheets application
	 * @param uiController the user interface controller
	 * @param chooser a file chooser
	 */
	public ActionManager(CleanSheets app, UIController uiController, FileChooser chooser) {
		ActionEnabler enabler = new ActionEnabler();
		app.addSpreadsheetAppListener(enabler);
		uiController.addEditListener(enabler);
	}

	/**
	 * Returns the action with the given identifier
	 * @param identifier the unique identifier of the action
	 * @return the action of the given type
	 */
	public BaseAction getAction(String identifier) {
		return actions.get(identifier);
	}

	/**
	 * Registers the given action with the manager
	 * @param identifier the unique identifier of the action
	 * @param action the action to register
	 */
	public void registerAction(String identifier, BaseAction action) {
		actions.put(identifier, action);
		if (action.requiresModification())
			modificationActions.add(action);
		if (action.requiresFile())
			fileActions.add(action);
	}

	/**
	 * A workbook listener that sets the enabled state of actoins depending
	 * on whether the current workbook has been modified and/or is stored in
	 * a file.
	 */
	public class ActionEnabler implements SpreadsheetAppListener, EditListener {

		/**
		 * Creates a new action enabler. 
		 */
		public ActionEnabler() {}

		public void workbookCreated(SpreadsheetAppEvent event) {
			for (BaseAction action : modificationActions)
				action.setEnabled(false);
			for (BaseAction action : fileActions)
				action.setEnabled(false);
		}
	
		public void workbookLoaded(SpreadsheetAppEvent event) {
			for (BaseAction action : modificationActions)
				action.setEnabled(false);
			for (BaseAction action : fileActions)
				action.setEnabled(true);
		}
	
		public void workbookUnloaded(SpreadsheetAppEvent event) {}
	
		public void workbookSaved(SpreadsheetAppEvent event) {
			for (BaseAction action : modificationActions)
				action.setEnabled(false);
			for (BaseAction action : fileActions)
				action.setEnabled(true);
		}
	
		public void workbookModified(EditEvent event) {
			for (BaseAction action : modificationActions)
				action.setEnabled(true);
		}
	}
}