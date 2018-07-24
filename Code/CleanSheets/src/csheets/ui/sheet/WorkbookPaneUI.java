/*
 * Copyright (c) 2005 Einar Pehrson <einar@pehrson.nu>.
 * Copyright (c) Nobuo Tamemasa
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

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import javax.swing.plaf.metal.MetalTabbedPaneUI;

/**
 * An extension of the <code>MetalTabbedPaneUI</code> that adds a number of
 * navigation buttons to the pane.
 * @author Nobuo Tamemasa
 * @author Einar Pehrson
 */
public class WorkbookPaneUI extends MetalTabbedPaneUI {

	protected ActionListener[] buttonListeners;

	/**
	 * Creates a new WorkbookPane UI.
	 */
	public WorkbookPaneUI() {}

	public void installUI(JComponent c) {
		this.tabPane = (JTabbedPane)c;
		c.setLayout(createLayoutManager());
		installDefaults(); 
		installComponents();
		installListeners();
		installKeyboardActions();
		runCount = 1;
		selectedRun = 0;	 
	}

	public void uninstallUI(JComponent c) {
		uninstallComponents();
		super.uninstallUI(c);
	}

	protected LayoutManager createLayoutManager() {
		return new SingleRowTabbedLayout(tabPane);
	}

	protected void installComponents() {
		JButton[] buttons = ((WorkbookPane)tabPane).getButtons();
		for (int i=0;i<buttons.length;i++) {
			tabPane.add(buttons[i]);
		}
	}

	protected void uninstallComponents() {
		JButton[] buttons = ((WorkbookPane)tabPane).getButtons();
		for (int i=0;i<buttons.length;i++) {
			tabPane.remove(buttons[i]);
		}
	}

	protected void installListeners() {
		super.installListeners();
		WorkbookPane stabPane = (WorkbookPane)tabPane;
		JButton[] buttons = stabPane.getButtons();
		int n = buttons.length;
		buttonListeners = new ActionListener[n];

		for (int i=0;i<n;i++) {
			buttonListeners[i] = null;
			String str = buttons[i].getActionCommand();

			if (str.equals(WorkbookPane.FIRST_COMMAND)) {
				buttonListeners[i] = new TabShifter();
			} else if (str.equals(WorkbookPane.PREV_COMMAND)) {
				buttonListeners[i] = new TabShifter() {
					protected int getStartIndex() {
						return sPane.getVisibleStartIndex() - 1;
					}
				};
			} else if (str.equals(WorkbookPane.NEXT_COMMAND)) {
				buttonListeners[i] = new TabShifter() {
					protected int getStartIndex() {
						return sPane.getVisibleStartIndex() + 1;
					}
				};
			} else if (str.equals(WorkbookPane.LAST_COMMAND)) {
				buttonListeners[i] = new TabShifter() {
					protected int getStartIndex() {
						return getStartIndex(sPane.getTabCount() - 1);
					}
				};
			}			 
			buttons[i].addActionListener(buttonListeners[i]);
		}
	}

	protected void uninstallListeners() {
		super.uninstallListeners();
		JButton[] buttons = ((WorkbookPane)tabPane).getButtons();
		for (int i=0;i<buttons.length;i++) {
			buttons[i].removeActionListener(buttonListeners[i]);
		}
	}

	public int tabForCoordinate(JTabbedPane pane, int x, int y) {
		WorkbookPane stabPane = (WorkbookPane)tabPane;
		int visibleCount = stabPane.getVisibleCount();
		int visibleStartIndex = stabPane.getVisibleStartIndex();

		for (int i=0,index = visibleStartIndex; i < visibleCount
				&& i < rects.length; i++,index++) {
			if (rects[index].contains(x, y)) {
				return index;
			}
		}
		return -1;
	}

	public void paint(Graphics g, JComponent c) {
		int selectedIndex = tabPane.getSelectedIndex();
		int tabPlacement = tabPane.getTabPlacement();
		ensureCurrentLayout();

		WorkbookPane stabPane = (WorkbookPane)tabPane;
		int visibleCount = stabPane.getVisibleCount();
		int visibleStartIndex = stabPane.getVisibleStartIndex();

		Rectangle iconRect = new Rectangle(),
							textRect = new Rectangle();
		Rectangle clipRect = g.getClipBounds();
		tabRuns[0] = visibleStartIndex;

		for (int i=0,index=visibleStartIndex; i<visibleCount && i<rects.length; i++,index++) {
			if (rects[index].intersects(clipRect)) {
				paintTab(g, tabPlacement, rects, index, iconRect, textRect);
			}
		}
		if (stabPane.isVisibleTab(selectedIndex)) {
			if (rects[selectedIndex].intersects(clipRect)) {
				paintTab(g, tabPlacement, rects, selectedIndex, iconRect, textRect);
			}
		}

		paintContentBorder(g, tabPlacement, selectedIndex);
	}


	protected void paintContentBorderTopEdge( Graphics g,
				int tabPlacement, int selectedIndex, int x, int y, int w, int h ) {
		g.setColor(selectHighlight);
		if (tabPlacement != TOP || selectedIndex < 0 || 
				(rects[selectedIndex].y + rects[selectedIndex].height + 1 < y) ||
				!((WorkbookPane)tabPane).isVisibleTab(selectedIndex) ) {
			g.drawLine(x, y, x+w-2, y);
		} else {
			Rectangle selRect = rects[selectedIndex];
			g.drawLine(x, y, selRect.x + 1, y);
			if (selRect.x + selRect.width < x + w - 2) {
				g.drawLine(selRect.x + selRect.width, y, x+w-2, y);
			} else {
	g.setColor(shadow); 
				g.drawLine(x+w-2, y, x+w-2, y);
			}
		}
	}

	protected void paintContentBorderBottomEdge(Graphics g,
				int tabPlacement, int selectedIndex, int x, int y, int w, int h) { 
		g.setColor(darkShadow);
		if (tabPlacement != BOTTOM || selectedIndex < 0 ||
				(rects[selectedIndex].y - 1 > h) ||
				!((WorkbookPane)tabPane).isVisibleTab(selectedIndex) ) {
			g.drawLine(x, y+h-1, x+w-1, y+h-1);
		} else {
			Rectangle selRect = rects[selectedIndex];
			g.drawLine(x, y+h-1, selRect.x, y+h-1);
			if (selRect.x + selRect.width < x + w - 2) {
				g.drawLine(selRect.x + selRect.width, y+h-1, x+w-1, y+h-1);
			} 
		}
	}



	protected Insets getTabAreaInsets(int tabPlacement) {
		WorkbookPane stabPane = (WorkbookPane)tabPane;
		Dimension d = stabPane.getPreferredButtonSize();
		int n = 4;
		Insets currentInsets = new Insets(0,0,0,0);
		currentInsets.top = tabAreaInsets.bottom;
		currentInsets.bottom = tabAreaInsets.top;
		currentInsets.left = tabAreaInsets.left	+ n * d.width;
		currentInsets.right = tabAreaInsets.right;
		return currentInsets;
	}

	protected int lastTabInRun(int tabCount, int run) {
		WorkbookPane stabPane = (WorkbookPane)tabPane;
		return stabPane.getVisibleStartIndex() + stabPane.getVisibleCount() -1;
	}

	protected void ensureCurrentLayout() {				 
		SingleRowTabbedLayout layout = (SingleRowTabbedLayout)tabPane.getLayout();
		layout.calculateLayoutInfo(); 
		setButtonsEnabled();
	}

	protected void setButtonsEnabled() {
		WorkbookPane stabPane = (WorkbookPane)tabPane;
		int visibleCount = stabPane.getVisibleCount();
		int visibleStartIndex = stabPane.getVisibleStartIndex();
		JButton[] buttons = stabPane.getButtons();
		boolean lEnable = 0 < visibleStartIndex;
		boolean rEnable = visibleStartIndex + visibleCount < tabPane.getTabCount();
		for (int i=0;i<buttons.length;i++) {
			boolean enable = false;
			String str = buttons[i].getActionCommand();
			if (str.equals(WorkbookPane.FIRST_COMMAND))
				enable = lEnable;
			else if (str.equals(WorkbookPane.PREV_COMMAND))
				enable = lEnable;
			else if (str.equals(WorkbookPane.NEXT_COMMAND))
				enable = rEnable;
			else if (str.equals(WorkbookPane.LAST_COMMAND))
				enable = rEnable;
			buttons[i].setEnabled(enable);
		}	 
	}		 

	// 
	// Tab Navigation by Key 
	// (Not yet done)
	//
	protected void ensureVisibleTabAt(int index) { 
		WorkbookPane stabPane = (WorkbookPane)tabPane;
		int visibleCount = stabPane.getVisibleCount();
		int visibleStartIndex = stabPane.getVisibleStartIndex();
		int visibleEndIndex = visibleStartIndex + visibleCount -1;

		if (visibleStartIndex < index && index < visibleEndIndex) {
			return;
		}
		// int selectedIndex = tabPane.getSelectedIndex();
		// boolean directionIsRight = (0 < index - selectedIndex)? true: false;
		//if (directionIsRight) {
			if (index <= visibleStartIndex) {
				//System.out.println("dec");
				if (visibleStartIndex == 0) return;
				stabPane.setVisibleStartIndex( --visibleStartIndex );
				((SingleRowTabbedLayout)tabPane.getLayout()).calculateLayoutInfo();
				int count = stabPane.getVisibleCount();
				int startIndex = stabPane.getVisibleStartIndex();
				if (startIndex <= index								&&
													index <= startIndex + count-1) {
				} else {
					stabPane.setVisibleStartIndex( ++visibleStartIndex );
				}
			}
		//} else {
			if (visibleEndIndex <= index) {
				if (visibleStartIndex == visibleCount+1) return;
				stabPane.setVisibleStartIndex( ++visibleStartIndex );
				((SingleRowTabbedLayout)tabPane.getLayout()).calculateLayoutInfo();
				int count = stabPane.getVisibleCount();
				int startIndex = stabPane.getVisibleStartIndex();
				if (startIndex <= index								&&
													index <= startIndex + count-1) {
				} else {
					stabPane.setVisibleStartIndex( --visibleStartIndex );
				}
			}
		//}
	}

	protected void selectNextTab(int current) {
		for (int i=current+1;i<tabPane.getTabCount();i++) {
			if (tabPane.isEnabledAt(i)) {
				ensureVisibleTabAt(i);
				tabPane.setSelectedIndex(i);
				break;
			}
		}
	}

	protected void selectPreviousTab(int current) {
		for (int i=current-1;0<=i;i--) {
			if (tabPane.isEnabledAt(i)) {
				ensureVisibleTabAt(i);
				tabPane.setSelectedIndex(i);
				break;
			}
		}
	}

	// these methods exist for innerclass
	void setMaxTabHeight(int maxTabHeight) {
		this.maxTabHeight = maxTabHeight;
	}

	int getMaxTabHeight() {
		return maxTabHeight;
	}

	Rectangle[] getRects() {
		return rects;
	}

	WorkbookPane getTabbedPane() {
		return (WorkbookPane)tabPane;
	}

	protected FontMetrics getFontMetrics() {
		Font font = tabPane.getFont();
		return tabPane.getFontMetrics(font);
	}

	protected int calculateMaxTabHeight(int tabPlacement) {
		return super.calculateMaxTabHeight(tabPlacement);
	}

	protected int calculateTabWidth(int tabPlacement, int tabIndex, FontMetrics metrics) {
		return super.calculateTabWidth(tabPlacement, tabIndex, metrics);
	}

	protected void assureRectsCreated(int tabCount) {
		super.assureRectsCreated(tabCount);
	}

	/**
	 * The layout used for the tabbed pane.
	 */
	protected class SingleRowTabbedLayout extends BasicTabbedPaneUI.TabbedPaneLayout {
		JTabbedPane tabPane;

		public SingleRowTabbedLayout(JTabbedPane tabPane) {
			this.tabPane = tabPane;
		}

		public void layoutContainer(Container parent) {
			super.layoutContainer(parent);
			if (tabPane.getComponentCount() < 1) {
				return;
			}

			int tabPlacement = tabPane.getTabPlacement();
			int maxTabHeight = calculateMaxTabHeight(tabPlacement);
			Insets tabAreaInsets = getTabAreaInsets(tabPlacement);
			Insets				insets = tabPane.getInsets();
			Rectangle		 bounds = tabPane.getBounds();

			WorkbookPane stabPane = (WorkbookPane)tabPane;
			Dimension				 d = stabPane.getPreferredButtonSize();
			JButton[]	 buttons = stabPane.getButtons();
			int x,y;
			y = bounds.y + bounds.height - insets.bottom
				- tabAreaInsets.bottom - maxTabHeight;
			x = bounds.x + insets.left;
			for (int i=0;i<buttons.length;i++) {
				buttons[i].setBounds(x, y, d.width, d.height);
				x += d.width;
			}
		}

		public void calculateLayoutInfo() {
			int tabCount = tabPane.getTabCount(); 
			assureRectsCreated(tabCount);
			calculateTabWidths(tabPane.getTabPlacement(), tabCount);
			calculateTabRects(tabPane.getTabPlacement(), tabCount);
		}

		protected void calculateTabWidths(int tabPlacement, int tabCount) {
			if (tabCount == 0) {
				return;
			}
			FontMetrics metrics = getFontMetrics();
			int maxTabHeight = calculateMaxTabHeight(tabPlacement);
			setMaxTabHeight(maxTabHeight);
			Rectangle[] rects = getRects();	 
			for (int i = 0; i < tabCount; i++) {
				rects[i].width = calculateTabWidth(tabPlacement, i, metrics);
				rects[i].height = maxTabHeight;
			}
		}

		protected void calculateTabRects(int tabPlacement, int tabCount) {
			if (tabCount == 0) {
				return;
			}
			WorkbookPane stabPane = (WorkbookPane)tabPane;
			Dimension size = tabPane.getSize();
			Insets	insets = tabPane.getInsets(); 
			Insets tabAreaInsets = getTabAreaInsets(tabPlacement);
			int maxTabHeight = getMaxTabHeight();
			int x = insets.left + tabAreaInsets.left;
			int y;
			if (tabPlacement == TOP) {
				y = insets.top + tabAreaInsets.top;
			} else {				 // BOTTOM
				y = size.height - insets.bottom - tabAreaInsets.bottom - maxTabHeight;
			}

			int returnAt = size.width - (insets.right + tabAreaInsets.right);
			Rectangle[] rects = getRects();
			int visibleStartIndex = stabPane.getVisibleStartIndex();
			int visibleCount = 0;

			for (int i = visibleStartIndex; i < tabCount; i++) {
				Rectangle rect = rects[i];
				if (visibleStartIndex < i) {
					rect.x = rects[i-1].x + rects[i-1].width;
				} else {
					rect.x = x;
				}		 

				if (rect.x + rect.width > returnAt) {
					break;
				} else {
					visibleCount++;
					rect.y = y;
				}
			}
			stabPane.setVisibleCount(visibleCount);
			stabPane.setVisibleStartIndex(visibleStartIndex);
		}
	}

	// Listener
	protected class TabShifter implements ActionListener {
		WorkbookPane sPane;

		public void actionPerformed(ActionEvent e) {
			sPane = getTabbedPane();
			int index = getStartIndex();
			sPane.setVisibleStartIndex(index);
			sPane.repaint();
		}

		//public abstract int getStartIndex();
		protected int getStartIndex() {
			return 0; // first tab
		}

		protected int getStartIndex(int lastIndex) {
			Insets	insets = sPane.getInsets();
			Insets tabAreaInsets = getTabAreaInsets(sPane.getTabPlacement());
			int width = sPane.getSize().width
				 - (insets.left				+ insets.right)
				 - (tabAreaInsets.left + tabAreaInsets.right);		 
			int index;
			Rectangle[] rects = getRects();
			for (index=lastIndex;0<=index;index--) {
				width -= rects[index].width;
				if (width < 0)
					break;
			}
			return ++index;
		}
	}

	/**
	 * An action for navigating between the tabs in the pane.
	 * @author Einar Pehrson
	 */
	@SuppressWarnings("serial")
	protected class NavigateAction extends AbstractAction {

		/** The direction in which to navigate (a SwingConstants value) */
		private int direction;

		public NavigateAction(int direction) {
			if (direction == SwingConstants.PREVIOUS
			 || direction == SwingConstants.NEXT)
				this.direction = direction;
			else
				throw new IllegalArgumentException("A SwingConstants value expected");
		}

		public void actionPerformed(ActionEvent event) {
			navigateSelectedTab(direction);
		}
	}
}