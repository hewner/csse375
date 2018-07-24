/*
 * Copyright (c) 2005 Peter Palotas, Fredrik Johansson, Einar Pehrson,
 * Sebastian Kekkonen, Lars Magnus Lång, Malin Johansson and Sofia Nilsson
 *
 * This file is part of
 * CleanSheets Extension for Assertions
 *
 * CleanSheets Extension for Assertions is free software; you can
 * redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either version 2 of
 * the License, or (at your option) any later version.
 *
 * CleanSheets Extension for Assertions is distributed in the hope that
 * it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with CleanSheets Extension for Assertions; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA  02111-1307  USA
 */
package csheets.ext.assertion.ui;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JToolBar;

import csheets.core.Cell;
import csheets.ext.assertion.AssertableCell;
import csheets.ext.assertion.AssertableCellListener;
import csheets.ext.assertion.Assertion;
import csheets.ext.assertion.AssertionExtension;
import csheets.ui.ctrl.SelectionEvent;
import csheets.ui.ctrl.SelectionListener;

/**
 * A toolbar that shows information about assertions.
 * @author Lars Magnus "Burken" Lång
 * @author Sebastian Kekkonen
 */
@SuppressWarnings("serial")
public class AssertionToolBar extends JToolBar implements SelectionListener,
		AssertableCellListener {

	private JTextField usAssertion;
	private JTextField sgAssertion;

	private Icon redIcon;
	private Icon yellowIcon;
	private Icon greenIcon;

	private JLabel lightLabelAssertion;

	public AssertionToolBar(){
		setName(AssertionExtension.NAME);
		redIcon = new ImageIcon(AssertionExtension.class.getResource(
			"res/img/light_red.gif"), "Red light");
		yellowIcon = new ImageIcon(AssertionExtension.class.getResource(
			"res/img/light_yellow.gif"), "Yellow light");
		greenIcon = new ImageIcon(AssertionExtension.class.getResource(
			"res/img/light_green.gif"), "Green light");

		lightLabelAssertion = new JLabel(yellowIcon);
		//yellowIcon.paintIcon(lightPanelAssertion, lightPanelAssertion.getGraphics(), 0, 0);

		usAssertion = new JTextField(5);
		usAssertion.setEditable(false);
		sgAssertion = new JTextField(5);
		sgAssertion.setEditable(false);

		add(new JLabel(new ImageIcon(
				AssertionExtension.class.getResource("res/img/logo.gif"))));
		addSeparator();
		add(lightLabelAssertion);
		addSeparator();
		add(usAssertion);
		addSeparator();
		add(new JLabel("="));
		addSeparator();
		add(sgAssertion);
	}

	private void setUSAssertionText(String s){
		usAssertion.setText(s);
	}

	private void setSGAssertionText(String s){
		sgAssertion.setText(s);
	}


	/*
	 *
	 * @param lamp 1-green, 2-yellow, 3-red
	 */
	private void setAssertionLamp(int lamp){
		switch(lamp){
		case 1: 
			lightLabelAssertion.setIcon(greenIcon);
			break;
		case 2:
			lightLabelAssertion.setIcon(yellowIcon);
			break;
		case 3:
			lightLabelAssertion.setIcon(redIcon);
			break;
		default:
			lightLabelAssertion.setIcon(yellowIcon);
			break;
		}
	}

	public void selectionChanged(SelectionEvent event) {
		Cell cell = event.getCell();
		if (cell != null) {
			AssertableCell activeCell
				= (AssertableCell)cell.getExtension(AssertionExtension.NAME);
			activeCell.addAssertableCellListener(this);
			assertionsChanged(activeCell);
		} else
			setAssertionLamp(2);

		// Stops listening to previous active cell
		if (event.getPreviousCell() != null)
			((AssertableCell)event.getPreviousCell().getExtension(AssertionExtension.NAME))
				.removeAssertableCellListener(this);
	}

	/**
	 * Updates the assertion field and status label when the assertion of the
	 * active cell is changed.
	 * @param cell the cell whose assertion changed
	 */
	public void assertionsChanged(AssertableCell cell) {
		Assertion usAssertion = cell.getUSAssertion();
		Assertion sgAssertion = cell.getSGAssertion();

		//Set lights depending on the circumstances
		// Code for setting lamps simplified by Peter after the addition of some new methods in cell.
		if (cell.hasAssertionError()) {
			if (cell.assertAssertions() == Assertion.ComparisonResult.OK
					&& cell.assertAny() == Assertion.Result.NO_DATA)
				setAssertionLamp(2);
			else
				setAssertionLamp(3);
		} else if (cell.isAsserted())
			setAssertionLamp(1);
		else
			setAssertionLamp(2);

		if(usAssertion != null)
			setUSAssertionText(usAssertion.toString());
		else
			setUSAssertionText("");

		if(sgAssertion != null)
			setSGAssertionText(sgAssertion.toString());
		else
			setSGAssertionText("");
	}
}