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
package csheets.ui.sheet;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Icon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicArrowButton;

import csheets.core.Address;
import csheets.core.Spreadsheet;
import csheets.core.Workbook;
import csheets.core.WorkbookListener;
import csheets.ui.ctrl.ActionManager;
import csheets.ui.ctrl.SelectionEvent;
import csheets.ui.ctrl.SelectionListener;
import csheets.ui.ctrl.UIController;

/**
 * A tabbed pane, used to display the spreadsheets in a workbook.
 * @author Einar Pehrson
 * @author Nobuo Tamemasa
 */
@SuppressWarnings("serial")
public class WorkbookPane extends JTabbedPane implements SelectionListener {

	/** The command for navigating to the first tab in the pane */
	public static final String FIRST_COMMAND = "First tab";

	/** The command for navigating to the previous tab in the pane */
	public static final String PREV_COMMAND = "Previous tab";

	/** The command for navigating to the next tab in the pane */
	public static final String NEXT_COMMAND = "Next tab";

	/** The command for navigating to the last tab in the pane */
	public static final String LAST_COMMAND = "Last tab";

	/** The user interface controller */
	private UIController uiController;

	/** The navigation buttons */
	private JButton[] buttons = new JButton[] {
		new StopArrowButton(WEST, FIRST_COMMAND),
		new BasicArrowButton(WEST),
		new BasicArrowButton(EAST),
		new StopArrowButton(EAST, LAST_COMMAND)
	};

	/** The preferred size of the navigation buttons */
	private Dimension buttonSize = new Dimension(16,17);

	/** The number of visible tabs in the pane */
	private int visibleCount = 0;

	/** The index of the fist visible tab in the pane */
	private int visibleStartIndex = 0;

	/** The popup-menu */
	private JPopupMenu popupMenu = new JPopupMenu();

	/** The workbook listener that manages spreadsheets in the pane */
	private WorkbookListener synchronizer = new SpreadsheetSynchronizer();

	/**
	 * Creates a workbook pane.
	 * @param actionManager a manager for actions
	 * @param uiController the user interface controller
	 */
	public WorkbookPane(UIController uiController, ActionManager actionManager) {
		super(BOTTOM, SCROLL_TAB_LAYOUT);

		// Stores members
		this.uiController = uiController;
		uiController.addSelectionListener(this);

		// Configures navigation
		WorkbookPaneUI ui = new WorkbookPaneUI();
		buttons[1].setActionCommand(PREV_COMMAND);
		buttons[2].setActionCommand(NEXT_COMMAND);
		setUI(ui);

		// Configures actions
		InputMap inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_UP, KeyEvent.CTRL_MASK),
			"Select previous spreadsheet");
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_DOWN, KeyEvent.CTRL_MASK),
			"Select next spreadsheet");
		getActionMap().put("Select previous spreadsheet", ui.new NavigateAction(SwingConstants.PREVIOUS));
		getActionMap().put("Select next spreadsheet", ui.new NavigateAction(SwingConstants.NEXT));

		// Adds popup-menu
		popupMenu.add(actionManager.getAction("addsheet"));
		popupMenu.add(actionManager.getAction("removesheet"));
		popupMenu.add(actionManager.getAction("renamesheet"));
		addMouseListener(new PopupShower());		
	}

/*
 * SELECTION
 */

	/**
	 * Updates the tabs in the pane when a new active workbook is selected.
	 * @param event the selection event that was fired
	 */
	public void selectionChanged(SelectionEvent event) {
		Workbook workbook = event.getWorkbook();
		if (event.isWorkbookChanged()) {
			// Adds spreadsheet tables
			removeAll();
			if (workbook != null && workbook.getSpreadsheetCount() > 0) {
				int i = 0;
				for (Spreadsheet spreadsheet : workbook) {
					SpreadsheetTable table = new SpreadsheetTable(spreadsheet, uiController);
					add(spreadsheet.getTitle(), new JScrollPane(table));
					setMnemonicAt(i++, KeyStroke.getKeyStroke(Integer.toString(i)).getKeyCode());
					table.selectionChanged(event);
				}
			} else
				add(new JPanel());

			// Adds listener
			if (event.getPreviousWorkbook() != null)
				event.getPreviousWorkbook().removeWorkbookListener(synchronizer);
			if (event.getWorkbook() != null)
				event.getWorkbook().addWorkbookListener(synchronizer);
		}
	}

	protected ChangeListener createChangeListener() {
		return new SelectionListener();
	}

	/**
	 * An extension of the change listener added to the tabbed pane's list
	 * selection model, which also updates the <code>SelectionController</code.
	 */
	@SuppressWarnings("serial")
	protected class SelectionListener extends ModelListener {

		public void stateChanged(ChangeEvent e) {
			super.stateChanged(e);

			// Updates selection
			Component selected = getSelectedComponent();
			if (selected != null && selected instanceof JScrollPane) {
				Component c = ((JScrollPane)selected).getViewport().getView();
				if (c instanceof SpreadsheetTable) {
					SpreadsheetTable table = (SpreadsheetTable)c;
					int activeColumn = table.getColumnModel().getSelectionModel().getAnchorSelectionIndex();
					int activeRow = table.getSelectionModel().getAnchorSelectionIndex();
					uiController.setActiveCell(table.getSpreadsheet()
						.getCell(new Address(activeColumn, activeRow)));
				}
			}
		}
	}

/*
 * UPDATES
 */

	/**
	 * A workbook listener that adds and removes spreadsheets in the pane.
	 */
	private class SpreadsheetSynchronizer implements WorkbookListener {

		public void spreadsheetInserted(Spreadsheet spreadsheet, int index) {
			insertTab(spreadsheet.getTitle(), null, new JScrollPane(
				new SpreadsheetTable(spreadsheet, uiController)), null, index);
			for (int i = 0; i < getTabCount(); i++)
				setMnemonicAt(i, KeyStroke.getKeyStroke(Integer.toString(i)).getKeyCode());
		}
	
		public void spreadsheetRemoved(Spreadsheet spreadsheet) {
			for (Component c : getComponents())
				if (c instanceof JScrollPane) {
					Component view = ((JScrollPane)c).getViewport().getView();
					if (view instanceof SpreadsheetTable)
						if (((SpreadsheetTable)view).getSpreadsheet() == spreadsheet)
							remove(c);
				}
		}
	
		public void spreadsheetRenamed(Spreadsheet spreadsheet) {}
	}

/*
 * POPUP MENU
 */

	/**
	 * A mouse listener that shows a pop-up menu whenever appropriate.
	 */
	private class PopupShower extends MouseAdapter {

		public void mousePressed(MouseEvent e) {
			maybeShowPopup(e);
		}

		public void mouseReleased(MouseEvent e) {
			maybeShowPopup(e);
		}

		public void maybeShowPopup(MouseEvent e) {
			if (e.isPopupTrigger())
				popupMenu.show(e.getComponent(), e.getX(),
					e.getY() - popupMenu.getPreferredSize().height);
		}
	}

/*
 * NAVIGATION
 */

	public Dimension getPreferredButtonSize() {
		return buttonSize;
	}

	public JButton[] getButtons() {
		return buttons;
	}

	public void insertTab(String title, Icon icon, Component component,
			String tip, int index) {
		if (component instanceof BasicArrowButton) {
			if (component != null) {
				component.setVisible(true);
				addImpl(component, null, -1);
			}
		} else
			super.insertTab(title, icon, component, tip, index);
	}


	public boolean isVisibleTab(int index) {
		if ((visibleStartIndex <= index) &&
				(index < visibleStartIndex + visibleCount)) {
			return true;
		} else
			return false;
	}

	public int getVisibleCount() {
		return visibleCount;
	}

	public void setVisibleCount(int visibleCount) {
		if (visibleCount < 0)
			return;
		this.visibleCount = visibleCount;
	}

	public int getVisibleStartIndex() {
		return visibleStartIndex;
	}

	public void setVisibleStartIndex(int visibleStartIndex) {
		if (visibleStartIndex < 0 || getTabCount() <= visibleStartIndex)
			return;
		this.visibleStartIndex = visibleStartIndex;
	}

	/**
	 * An extension of a <code>BasicArrowButton</code> that adds a stop dash
	 * to the button.
	 * @author Nobuo Tamemasa
	 * @author Einar Pehrson
	 */
	@SuppressWarnings("serial")
	protected static class StopArrowButton extends BasicArrowButton {
	
		/**
		 * Creates a new stop arrow button.
		 * @param direction the direction in which the button's arrow faces
		 */
		public StopArrowButton(int direction, String command) {
			super(direction);
			setActionCommand(command);
		}
	
		public void paintTriangle(Graphics g, int x, int y, int size, 
						int direction, boolean isEnabled) {
			super.paintTriangle(g, x, y, size, direction, isEnabled);
			Color c = g.getColor();
			if (isEnabled)
				g.setColor(UIManager.getColor("controlDkShadow"));
			else
				g.setColor(UIManager.getColor("controlShadow"));
			g.translate(x, y);
			size = Math.max(size, 2);
			int mid = size / 2;
			int h = size-1;
			if (direction == WEST) {
				g.drawLine(-1, mid-h, -1, mid+h);
				if (!isEnabled) {
					g.setColor(UIManager.getColor("controlLtHighlight"));
					g.drawLine(0, mid-h+1, 0, mid-1);
					g.drawLine(0, mid+2, 0, mid+h+1);
				}
			} else {
				g.drawLine(size, mid-h, size, mid+h);
				if (!isEnabled) {
					g.setColor(UIManager.getColor("controlLtHighlight"));
					g.drawLine(size+1, mid-h+1, size+1, mid+h+1);
				}
			}	
			g.setColor(c);			
		}
	}
}