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

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;

import csheets.CleanSheets;
import csheets.core.Workbook;
import csheets.ui.ctrl.AboutAction;
import csheets.ui.ctrl.ActionManager;
import csheets.ui.ctrl.AddSpreadsheetAction;
import csheets.ui.ctrl.ClearAction;
import csheets.ui.ctrl.CloseAction;
import csheets.ui.ctrl.CloseAllAction;
import csheets.ui.ctrl.CopyAction;
import csheets.ui.ctrl.CutAction;
import csheets.ui.ctrl.ExitAction;
import csheets.ui.ctrl.HelpAction;
import csheets.ui.ctrl.InsertColumnAction;
import csheets.ui.ctrl.InsertRowAction;
import csheets.ui.ctrl.LicenseAction;
import csheets.ui.ctrl.NewAction;
import csheets.ui.ctrl.OpenAction;
import csheets.ui.ctrl.PasteAction;
import csheets.ui.ctrl.PreferencesAction;
import csheets.ui.ctrl.PrintAction;
import csheets.ui.ctrl.RedoAction;
import csheets.ui.ctrl.RemoveColumnAction;
import csheets.ui.ctrl.RemoveRowAction;
import csheets.ui.ctrl.RemoveSpreadsheetAction;
import csheets.ui.ctrl.RenameSpreadsheetAction;
import csheets.ui.ctrl.SaveAction;
import csheets.ui.ctrl.SaveAsAction;
import csheets.ui.ctrl.SearchAction;
import csheets.ui.ctrl.SelectAllAction;
import csheets.ui.ctrl.SelectionEvent;
import csheets.ui.ctrl.SelectionListener;
import csheets.ui.ctrl.SortAction;
import csheets.ui.ctrl.UIController;
import csheets.ui.ctrl.UndoAction;
import csheets.ui.ext.UIExtension;
import csheets.ui.sheet.AddressBox;
import csheets.ui.sheet.CellEditor;
import csheets.ui.sheet.WorkbookPane;

/**
 * The main frame of the GUI.
 * @author Einar Pehrson
 */
@SuppressWarnings("serial")
public class Frame extends JFrame implements SelectionListener {

	/** The base of the window title */
	public static final String TITLE = "CleanSheets";

	/** The CleanSheets application */
	private CleanSheets app;

	/**
	 * Creates a new frame.
	 * @param app the CleanSheets application
	 */
	public Frame(CleanSheets app) {
		// Stores members and creates controllers
		this.app = app;
		UIController uiController = new UIController(app);

		// Creates action manager
		FileChooser chooser = null;
		try {
			chooser = new FileChooser(this, app.getUserProperties());
		} catch (java.security.AccessControlException ace) {}
		ActionManager actionManager = new ActionManager(app, uiController, chooser);

		registerAllActions(app, uiController, chooser, actionManager);

		// Registers help actions
		actionManager.registerAction("help", new HelpAction());
		actionManager.registerAction("license", new LicenseAction());
		actionManager.registerAction("about", new AboutAction());

		// Creates spreadsheet components
		WorkbookPane workbookPane = new WorkbookPane(uiController, actionManager);
		CellEditor cellEditor = new CellEditor(uiController);
		AddressBox addressBox = new AddressBox(uiController);

		// Creates tool bars
		JPanel toolBarPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
		toolBarPanel.add(new StandardToolBar(actionManager));
		for (UIExtension extension : uiController.getExtensions()) {
			JToolBar extToolBar = extension.getToolBar();
			if (extToolBar != null)
				toolBarPanel.add(extToolBar);
		}

		// Creates and lays out top panel
		JPanel cellPanel = new JPanel(new BorderLayout());
		cellPanel.add(addressBox, BorderLayout.WEST);
		cellPanel.add(cellEditor, BorderLayout.CENTER);
		JPanel topPanel = new JPanel(new BorderLayout());
		topPanel.add(toolBarPanel, BorderLayout.NORTH);
		topPanel.add(cellPanel, BorderLayout.SOUTH);

		// Creates and lays out side bar components
		JTabbedPane sideBar = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.WRAP_TAB_LAYOUT);
		sideBar.setPreferredSize(new Dimension(170, 480));
		Font font = sideBar.getFont();
		sideBar.setFont(new Font(font.getFamily(), Font.PLAIN, font.getSize() - 1));
		for (UIExtension extension : uiController.getExtensions()) {
			JComponent extBar = extension.getSideBar();
			if (extBar != null)
				sideBar.insertTab(extension.getExtension().getName(), extension.getIcon(),
					extBar, null, sideBar.getTabCount());
		}

		// Lays out split pane
		workbookPane.setMinimumSize(new Dimension(300, 100));
		sideBar.setMinimumSize(new Dimension(140, 100));
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
			workbookPane, sideBar);
		splitPane.setOneTouchExpandable(true);
		splitPane.setResizeWeight(1.0);

		// Configures layout and adds panels
		Container pane = getContentPane();
		pane.setPreferredSize(new Dimension(640, 480));
		pane.setLayout(new BorderLayout());
		pane.add(topPanel, BorderLayout.NORTH);
		pane.add(splitPane, BorderLayout.CENTER);
		setJMenuBar(new MenuBar(app, actionManager, uiController));

		// Registers listeners
		uiController.addSelectionListener(this);
		addWindowListener(new WindowClosingHandler(this,
			actionManager.getAction("exit")));

		// Configures appearance
		setTitle(TITLE);
		setIconImage(Toolkit.getDefaultToolkit().getImage(
			CleanSheets.class.getResource("res/img/sheet.gif")));
		pack();
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setLocationRelativeTo(null);
	}

	public static void registerAllActions(CleanSheets app, UIController uiController,
			FileChooser chooser, ActionManager actionManager) {
		// Registers file actions
		actionManager.registerAction("new", new NewAction(app));
		actionManager.registerAction("open", new OpenAction(app, uiController, chooser));
		actionManager.registerAction("close", new CloseAction(app, uiController, chooser));
		actionManager.registerAction("closeall", new CloseAllAction(app, uiController, chooser));
		actionManager.registerAction("save", new SaveAction(app, uiController, chooser));
		actionManager.registerAction("saveas", new SaveAsAction(app, uiController, chooser));
		actionManager.registerAction("exit", new ExitAction(app, uiController, chooser));
		actionManager.registerAction("print", new PrintAction());

		// Registers edit actions
		actionManager.registerAction("undo", new UndoAction());
		actionManager.registerAction("redo", new RedoAction());
		actionManager.registerAction("cut", new CutAction());
		actionManager.registerAction("copy", new CopyAction());
		actionManager.registerAction("paste", new PasteAction());
		actionManager.registerAction("clear", new ClearAction());
		actionManager.registerAction("selectall", new SelectAllAction());
		actionManager.registerAction("sort", new SortAction());
		actionManager.registerAction("search", new SearchAction());
		actionManager.registerAction("prefs", new PreferencesAction());

		// Registers spreadsheet actions
		actionManager.registerAction("addsheet", new AddSpreadsheetAction(uiController));
		actionManager.registerAction("removesheet", new RemoveSpreadsheetAction(uiController));
		actionManager.registerAction("renamesheet", new RenameSpreadsheetAction(uiController));
		actionManager.registerAction("insertcolumn", new InsertColumnAction());
		actionManager.registerAction("removecolumn", new RemoveColumnAction());
		actionManager.registerAction("insertrow", new InsertRowAction());
		actionManager.registerAction("removerow", new RemoveRowAction());
	}

	/**
	 * Updates the title of the window when a new active workbook is selected.
	 * @param event the selection event that was fired
	 */
	public void selectionChanged(SelectionEvent event) {
		Workbook workbook = event.getWorkbook();
		if (workbook != null) {
			setVisible(true);
			if (app.isWorkbookStored(workbook))
				setTitle(TITLE + " - " + app.getFile(workbook).getName());
			else
				setTitle(TITLE + " - Untitled");
		} else
			setTitle(TITLE);
	}

	/**
	 * A utility for creating a Frame on the AWT event dispatching thread.
	 * @author Einar Pehrson
	 */
	public static class Creator implements Runnable {

		/** The component that was created */
		private Frame frame;

		/** The CleanSheets application */
		private CleanSheets app;

		/**
		 * Creates a new frame creator.
		 * @param app the CleanSheets application
		 */
		public Creator(CleanSheets app) {
			this.app = app;
		}

		/**
		 * Creates a component on the AWT event dispatching thread.
		 * @return the component that was created
		 */
		public Frame createAndWait() {
			try {
				EventQueue.invokeAndWait(this);
			} catch (InterruptedException e) {
				return null;
			} catch (java.lang.reflect.InvocationTargetException e) {
				e.printStackTrace();
				return null;
			}
			return frame;
		}

		/**
		 * Asks the event queue to create a component at a later time.
		 * (Included for conformity.)
		 */
		public void createLater() {
			EventQueue.invokeLater(this);
		}

		public void run() {
			frame = new Frame(app);
		}
	}
}