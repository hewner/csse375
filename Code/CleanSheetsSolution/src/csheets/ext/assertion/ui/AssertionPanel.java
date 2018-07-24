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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import csheets.core.Cell;
import csheets.ext.assertion.AssertableCell;
import csheets.ext.assertion.AssertableCellListener;
import csheets.ext.assertion.Assertion;
import csheets.ext.assertion.AssertionExtension;
import csheets.ui.ctrl.FocusOwnerAction;
import csheets.ui.ctrl.SelectionEvent;
import csheets.ui.ctrl.SelectionListener;
import csheets.ui.ctrl.UIController;

/**
 * A panel for adding or editing an assertion for a cell
 * @author Björn Lanneskog
 * @author Einar Pehrson
 */
@SuppressWarnings("serial")
public class AssertionPanel extends JPanel implements SelectionListener,
		AssertableCellListener {

	/** The assertion controller */
	private AssertionController controller;

	/** The assertable cell currently being displayed in the panel */
	private AssertableCell cell;

	/** The label on which the status of the assertion is displayed */
	private JLabel statusLabel = new JLabel();

	/** The text field in which the assertion of the cell is displayed.*/
	private JTextField usField = new JTextField();

	/**A label showing the system-generated assertion*/
	private JLabel sgLabel = new JLabel();

	/**
	 * Creates a new assertion panel.
	 * @param uiController the user interface controller
	 */
	public AssertionPanel(UIController uiController) {
		// Configures panel
		super(new BorderLayout());
		setName(AssertionExtension.NAME);

		// Creates controller
		controller = new AssertionController(uiController);
		uiController.addSelectionListener(this);

		// Creates system-generated assertion components
		JPanel sgPanel = new JPanel();
		sgPanel.setPreferredSize(new Dimension(130, 60));
		sgPanel.add(sgLabel);

		// Creates user-specified assertion components
		ApplyAction applyAction = new ApplyAction();
		JButton applyButton = new JButton(applyAction);
		usField.setPreferredSize(new Dimension(120, 24));
		usField.setMaximumSize(new Dimension(1000, 24));
		usField.addActionListener(applyAction);
		usField.setAlignmentX(Component.CENTER_ALIGNMENT);
		applyButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

		// Lays out user-specified assertion components
		JPanel usPanel = new JPanel();
		usPanel.setLayout(new BoxLayout(usPanel, BoxLayout.PAGE_AXIS));
		usPanel.setPreferredSize(new Dimension(130, 120));
		usPanel.add(usField);
		usPanel.add(Box.createRigidArea(new Dimension(120, 4)));
		usPanel.add(applyButton);
		usPanel.add(Box.createRigidArea(new Dimension(120, 12)));
		usPanel.add(statusLabel);
		usPanel.add(Box.createRigidArea(new Dimension(120, 12)));

		// Creates assertion syntax components
		JPanel syntaxPanel = new JPanel();
		JTextArea syntaxArea = new JTextArea("");
			// "Operators: > < >= <=\nOR\nEXCEPT\nINTEGER"
		syntaxArea.setPreferredSize(new Dimension(120, 100));
		syntaxArea.setLineWrap(true);
		syntaxArea.setEditable(false);
		syntaxArea.setBackground(getBackground());
		syntaxPanel.add(syntaxArea);

		// Adds borders
		TitledBorder border = BorderFactory.createTitledBorder("System-generated");
		border.setTitleJustification(TitledBorder.CENTER);
		sgPanel.setBorder(border);
		border = BorderFactory.createTitledBorder("User-specified");
		border.setTitleJustification(TitledBorder.CENTER);
		usPanel.setBorder(border);
		border = BorderFactory.createTitledBorder("Syntax");
		border.setTitleJustification(TitledBorder.CENTER);
		// syntaxPanel.setBorder(border);

		// Adds panels
		JPanel northPanel = new JPanel(new BorderLayout());
		northPanel.add(sgPanel, BorderLayout.NORTH);
		northPanel.add(usPanel, BorderLayout.SOUTH);
		add(northPanel, BorderLayout.NORTH);
		add(syntaxPanel, BorderLayout.CENTER);
	}

	/**
	 * Updates the assertion field and status label when the active cell of
	 * the application is changed.
	 * @param event the selection event that was fired
	 */
	public void selectionChanged(SelectionEvent event) {
		Cell cell = event.getCell();
		if (cell != null) {
			AssertableCell activeCell
				= (AssertableCell)cell.getExtension(AssertionExtension.NAME);
			activeCell.addAssertableCellListener(this);
			assertionsChanged(activeCell);
		} else {
			usField.setText("");
			statusLabel.setText("");
			sgLabel.setText("");
		}

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
		// Stores the cell for use when applying assertion
		this.cell = cell;

		// Initializes colors and text
		Color usColor = Color.BLACK;
		Color sgColor = Color.BLACK;
		String usStatus = "";

		// Updates system-generated assertion label
		if (cell.isSGAsserted()) {
			sgLabel.setText(cell.getSGAssertion().toString());
			sgColor = AssertionController.getAssertionResultColor(cell.assertSG());
		} else
			sgLabel.setText("No assertion");

		// Updates the text field and validates the assertion, if any
		if (cell.isUSAsserted()) {
			usField.setText(cell.getUSAssertion().toString());
			Assertion.Result result = cell.assertUS();
	
			// Determines the status message
			usColor = AssertionController.getAssertionResultColor(result);
			switch (result) {
				case OK:
					usStatus += "Valid";
					break;
				case NAN:
					usStatus += "Non-numeric value";
					break;
				case FAILED:
					usStatus += "Illegal value";
					break;
				case NO_DATA:
					usStatus += "No value";
					break;
			}

			// Compares assertions
			Assertion.ComparisonResult compResult = cell.assertAssertions();
			switch (compResult) {
				case NON_EQUAL:
					usColor =  new Color(0.7f, 0.0f, 0f);
					usStatus = "Conflicting assertions";
					 break;
				case ILLEGAL_INTERVAL:
					sgLabel.setText(compResult.getErrorMsg());
					break;
			}
		} else {
			usField.setText("");
			usStatus = "No assertion";
		}

		// Updates the label
		sgLabel.setForeground(sgColor);
		statusLabel.setForeground(usColor);
		statusLabel.setText(usStatus);
	}

	/**
	 * An action used to apply changes made in the assertion field.
	 */
	protected class ApplyAction extends FocusOwnerAction {

		/**
		 * Creates a new apply action.
		 */
		public ApplyAction() {}

		protected String getName() {
			return "Apply";
		}

		public void actionPerformed(ActionEvent e) {
			if (cell != null)
				if (controller.setAssertion(cell, usField.getText().trim()))
					focusOwner.repaint();
		}
	}
}