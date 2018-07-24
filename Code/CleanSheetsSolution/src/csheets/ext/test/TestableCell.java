/*
 * Copyright (c) 2005 Jens Schou, Staffan Gustafsson, Björn Lanneskog, 
 * Einar Pehrson and Sebastian Kekkonen
 *
 * This file is part of
 * CleanSheets Extension for Test Cases
 *
 * CleanSheets Extension for Test Cases is free software; you can
 * redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either version 2 of
 * the License, or (at your option) any later version.
 *
 * CleanSheets Extension for Test Cases is distributed in the hope that
 * it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with CleanSheets Extension for Test Cases; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA  02111-1307  USA
 */
package csheets.ext.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import csheets.core.Cell;
import csheets.core.Value;
import csheets.ext.CellExtension;

/**
 * An extension of a cell in a spreadsheet, with support for test cases.
 * @author Staffan Gustafsson
 * @author Jens Schou
 * @author Einar Pehrson
 */
public class TestableCell extends CellExtension {

	/** The unique version identifier used for serialization */
	private static final long serialVersionUID = -2626239432851585308L;

	/** The cell's test case parameters */
	private Set<TestCaseParam> tcParams = new HashSet<TestCaseParam>();

	/** The cell's test cases */
	private Set<TestCase> testCases = new HashSet<TestCase>();

	/** The listeners registered to receive events from the testable cell */
	private transient List<TestableCellListener> listeners
		= new ArrayList<TestableCellListener>();
	
	/**
	 * Creates a testable cell extension for the given cell.
	 * @param cell the cell to extend
	 */
	TestableCell(Cell cell) {
		super(cell, TestExtension.NAME);
	}


/*
 * DATA UPDATES
 */


	/**
	 * Invoked to indicate that the content of the cell in the spreadsheet was
	 * modified and that test cases and test case paremeters that depend on that
	 * data must be updated, and new ones generated.
	 */
	public void contentChanged(Cell cell) {
		if (getFormula() != null) {
			resetTestCases();
		} else {
			removeAllTcpsOfType(TestCaseParam.Type.DERIVED);
			testCases.clear();
		}
	}
	
	
/*
 * TEST CASE ACCESSORS
 */
	
	
	/**
	 * Returns the test cases for the cell, which consist of a predetermined
	 * value for each of the cell's precedents.
	 * @return the cell's test cases
	 */
	public Set<TestCase> getTestCases(){
		return testCases;
	}
	
	/**
	 * Returns whether the cell has any test cases.
	 * @return true if the cell has any test cases
	 */
	public boolean hasTestCases(){
		return !testCases.isEmpty();
	}
	
	/**
	 * Returns whether any of the cell's test cases have been rejected.
	 * @return true if any of the cell's test cases have been rejected
	 */
	public boolean hasTestError() {
		for (TestCase testCase : testCases)
			if (testCase.getValidationState() == TestCase.ValidationState.REJECTED)
				return true;
		return false;
	}
	
	/**
	 * Returns the testedness of the cell, i.e. the ratio of valid
	 * test cases to available test cases in the cell.
	 * @return a number between 0.0 and 1.0 denoting the level of testedness
	 */
	public double getTestedness() {
		if (hasTestCases()) {
			// Calculates and returns the testedness
			double nValid = 0;
			for (TestCase testCase : testCases)
				if (testCase.getValidationState() == TestCase.ValidationState.VALID)
					nValid++;
			return nValid / testCases.size();
		} else
			return 0d;
	}
	
	
/*
 * TEST CASE MODIFIERS
 */


	/**
	 * Generates new test cases for the cell, provided that all its precedents
	 * have test case parameters.		 
	 */
	public void resetTestCases(){
		boolean changed = false;

		if(!testCases.isEmpty()) {
			testCases.clear();
			removeAllTcpsOfType(TestCaseParam.Type.DERIVED);
			changed = true;
		}

		if(allPrecedentsHaveParams() && getPrecedents().size() > 0) {
			// We pick one precedent at random to initiate the set
			TestableCell firstPrec = (TestableCell)getPrecedents().first();
			Iterator<TestCaseParam> paramIt = firstPrec.getTestCaseParams().iterator();
			// make one extention in the set per parameter
			while(paramIt.hasNext()) {
				//extendTestCases takes care of the rest of the precedents
				extendTestCases(firstPrec, paramIt.next());
			}
			changed = true;
		}

		if(changed) {
			fireTestCasesChanged();
		}
	}
	
	protected void extendTestCases(TestableCell firstPrec, TestCaseParam param) {
		SortedSet<Cell> precedents = getPrecedents();
		precedents.remove(firstPrec);

		// The first precedent initiates the set
		// make one entry in the set for the parameter
		Map<Cell, Value> caseMap = new HashMap<Cell, Value>();
		caseMap.put(firstPrec, param.getValue());
		
		Set<Map<Cell, Value>> casesSet = createCasesSet(precedents, caseMap);

		if(toTestCases(casesSet))
			fireTestCasesChanged();
	}
	
	private Set<Map<Cell, Value>> createCasesSet(Set<Cell> precedents,
													 Map<Cell, Value> caseMap){
		// Set to store all maps used to make test cases
		Set<Map<Cell, Value>> casesSet = new HashSet<Map<Cell, Value>>();
		casesSet.add(caseMap);

		// Now, update casesSet for each precedent
		for(Cell prec : precedents){
			// a temporary set to store new caseMaps during the iteration
			Set<Map<Cell, Value>> tempCasesSet
				= new HashSet<Map<Cell, Value>>();

			for(TestCaseParam precParam : ((TestableCell)prec).getTestCaseParams()){
				// for each test case param in the precedent
				for(Map<Cell, Value> item : casesSet){

					// for every caseMap
					// make a copy, add current precedent address and param
					Map<Cell, Value> itemCopy = new HashMap<Cell, Value>();
					itemCopy.putAll(item);
					itemCopy.put(precParam.getCell(), precParam.getValue());
					// add the copy to tempCasesSet
					tempCasesSet.add(itemCopy);
				}
			}
			casesSet = tempCasesSet;
		}
		return casesSet;
	}

	private boolean toTestCases(Set<Map<Cell, Value>> casesSet){
		boolean tcChanged = false;
		// for every item in casesSet, make TestCase and add to testCases

		for(Map<Cell, Value> aoMap : casesSet){
			Set<TestCaseParam> tcParams = new HashSet<TestCaseParam>();
			Set<Map.Entry<Cell, Value>> aoSet = aoMap.entrySet();

			for(Map.Entry<Cell, Value> entry : aoSet){
				tcParams.add(new TestCaseParam(
					(TestableCell)entry.getKey().getExtension(TestExtension.NAME),
					entry.getValue(), TestCaseParam.Type.DERIVED));
			}
			
			// Creates the test case
			TestCase testCase = new TestCase(this, tcParams);
			testCases.add(testCase);
			tcChanged = true;
		}
		return tcChanged;
	}

	
	/*
	 * TEST CASE PARAMETER ACCESSORS
	 */
	

	/**
	 * Returns the cell's test case parameters.
	 * @return the cell's the test case parameters.
	 */
	public Set<TestCaseParam> getTestCaseParams(){
		return tcParams;
	}
	
	/**
	 * Returns whether the cell has any test case parameters.
	 * @return true if the cell has any test case parameters
	 */
	public boolean hasTestCaseParams(){
		return !tcParams.isEmpty();
	}
	
	/**
	 * Tests if all of the cells precedents have test case parameters.
	 * @return true if all of the cells precedents have test case parameters
	 */
	protected boolean allPrecedentsHaveParams(){
		for (Cell precedent : getPrecedents())
			if (!((TestableCell)precedent).hasTestCaseParams())
				return false;
		return true;
	}
	
	/*
	 * TEST CASE PARAMETER MODIFIERS
	 */
	
	
	/**
	 * Add a test case parameter to the cell's set of test case parameters.
	 * On addition, the cell's dependents are notified.
	 * @param value the value of the test case parameter to be added
	 * @return the parameter that was added, or null if the cell already had an identical parameter
	 */
	public TestCaseParam addTestCaseParam(Value value) throws DuplicateUserTCPException {

		TestCaseParam param = null;
		Iterator<TestCaseParam> it = tcParams.iterator();
		while(it.hasNext()) {
			param = it.next();
			if(value.equals(param.getValue())) {
				if(param.isUserEntered()) {
					throw new DuplicateUserTCPException(value,
				   "Cells cannot have duplicate user-entered test case parameters");
				}
				else {
					param.setType(TestCaseParam.Type.USER_ENTERED, true);
					return param;
				}
			}
		}
		return addTestCaseParam(value, TestCaseParam.Type.USER_ENTERED);
	}
	
	/**
	 * Add a test case parameter to the cell's set of test case parameters.
	 * On addition, the cell's dependents are notified.
	 * @param value the value of the test case parameter to be added
	 * @param type the type of test case parameter
	 */
	public TestCaseParam addTestCaseParam(Value value, TestCaseParam.Type type) {

		TestCaseParam param = null;
		Iterator<TestCaseParam> it = tcParams.iterator();
		while(it.hasNext()) {
			param = it.next();
			if(value.equals(param.getValue())) {
				param.setType(type, true);
				return param;
			}
		}
		param = new TestCaseParam(this, value, type);
		tcParams.add(param);
		for (Cell dependent : getDependents()) {
			((TestableCell)dependent).precedentAddedParam(this, param);
		}
		// Notifies listeners
		fireTestCaseParametersChanged();

			return param;
	}
	
	/**
	 * Removes a test case parameter from the cell's set of test case parameters.
	 * On removal, the cell's dependents are notified.
	 * @param param the test case parameter to be removed
	 */
	public void removeTestCaseParam(TestCaseParam param) {
		removeTestCaseParam(param, TestCaseParam.Type.USER_ENTERED);
	}

	/**
	 * Removes a test case parameter from the cell's set of test case parameters.
	 * On removal, the cell's dependents are notified.
	 * @param param the test case parameter to be removed
	 * @param type the type of the parameter to remove
	 */
	public void removeTestCaseParam(TestCaseParam param, TestCaseParam.Type type) {

		// hitta param, toggla av type		
		param.setType(type, false);
		// om param har inga type -> ta bort och meddela dependents
		if(param.hasNoType()) {
			tcParams.remove(param);

			// Notifies the cell's dependents
			for (Cell dependent : getDependents()){
				((TestableCell)dependent).precedentRemovedParam(this, param);
			}  
			// Notifies listeners		
			fireTestCaseParametersChanged();
		}
	}
	
	protected void removeAllTcpsOfType(TestCaseParam.Type type) {
		Iterator<TestCaseParam> tcpIt = tcParams.iterator();
		// for all params...
		while(tcpIt.hasNext()){
			TestCaseParam tcp = tcpIt.next();
			// ...check those of the specified type
			tcp.setType(type, false);
			if(tcp.hasNoType()){
				tcpIt.remove();

				// ...for all dependents...
				for (Cell dependent : getDependents())
					// ...I no longer have this param
					((TestableCell)dependent).precedentRemovedParam(this, tcp);

				// Notifies listeners		
				fireTestCaseParametersChanged();
			}
		}
	}
	
	
	/*
	 * TEST CASE PARAMETER UPDATES
	 */

	
	/**
	 * Invoked when a test case parameter is added to one of the cell's
	 * precedents. This causes the cell's test cases to be updated.
	 * @param cell the precedent to which the test case parameter was added
	 * @param param the test case parameter that was added
	 */
	public void precedentAddedParam(TestableCell cell, TestCaseParam param) {
		/*
	 * We only need to do anything if all our precedents have params
	 */
		if (allPrecedentsHaveParams()) {
			/*
	 * if we don't have any test cases, we just make a whole new set
	 */
			if(testCases.isEmpty())
				resetTestCases();
			/*
	 * if test cases exist, we want to keep the old, and just update
	 * with the new test cases generated by the new param
	 */
			else {
				extendTestCases(cell, param);
			}
		}
	}
	
	/**
	 * Invoked when a test case parameter is removed from one of the cell's
	 * precedents. This causes the cell's test cases to be updated.
	 * @param cell the precedent from which the test case parameter was removed
	 * @param param the test case parameter that was removed
	 */
	public void precedentRemovedParam(TestableCell cell, TestCaseParam param){
		/*
		 * if all precedents still have params, just remove the test cases
		 * pertaining to the removed parameter.
		 */
		if(allPrecedentsHaveParams()){
			// iterate the test cases.
			Iterator<TestCase> tcIt =  testCases.iterator();
			
			// remove all test cases that used param as a parameter:
			while(tcIt.hasNext()){
				TestCase tCase = tcIt.next();
				Set<TestCaseParam> paramMap = tCase.getParams();
				if(paramMap.contains(param)){ // testcase uses removed param
					tcIt.remove(); // remove the test case
					TestCaseParam derivedParam = null;
					Iterator<TestCaseParam> it = tcParams.iterator();
					while(it.hasNext()) {
						derivedParam = it.next();
						if(tCase.evaluate().equals(derivedParam.getValue()))
							break;
					}
					if(derivedParam != null)
						removeTestCaseParam(derivedParam, TestCaseParam.Type.DERIVED);
				}
			}
			fireTestCasesChanged();
		}
			/*  If some precedents lack params, our dependents need to be notified
			 * that all our derived params no longer apply (except for the derived
			 * params that happen to be the same as a local param).
			 * (no need to notify if we don't have any test cases, since then we
			 * dn't have any derived params either) */
		else if(!testCases.isEmpty()){
			testCases.clear();
			// inga test cases -> inga derived test case params
			removeAllTcpsOfType(TestCaseParam.Type.DERIVED);
			fireTestCasesChanged();
		}
	}


	/*
	 * CLIPBOARD
	 */
	
	
	/**
	 * Removes the test case parameters from the cell.
	 * @param cell the cell that was modified
	 */
	public void cellCleared(Cell cell) {
		if (this.getDelegate().equals(cell)) {
			tcParams.clear();
		}
	}

	/**
	 * Copies the user-specified test case parameters from the source cell to
	 * this one.
	 * @param cell the cell that was modified
	 * @param source the cell from which data was copied
	 */
	public void cellCopied(Cell cell, Cell source) {
		if (this.getDelegate().equals(cell)) {
			TestableCell testableSource = (TestableCell)source.getExtension(
				TestExtension.NAME);
			tcParams.clear();
			for (TestCaseParam param : testableSource.getTestCaseParams())
				if (param.hasType(TestCaseParam.Type.USER_ENTERED))
					try {
						addTestCaseParam(param.getValue());
					} catch (DuplicateUserTCPException e) {}
		}
	}


	/*
	 * EVENT LISTENING SUPPORT
	 */
	
	
	/**
	 * Registers the given listener on the cell.
	 * @param listener the listener to be added
	 */
	public void addTestableCellListener(TestableCellListener listener) {
		listeners.add(listener);
	}

	/**
	 * Removes the given listener from the cell.
	 * @param listener the listener to be removed
	 */
	public void removeTestableCellListener(TestableCellListener listener) {
		listeners.remove(listener);
	}

	/**
	* Notifies all registered listeners that the cell's test cases changed.
	*/
	protected void fireTestCasesChanged() {
		for (TestableCellListener listener : listeners)
			listener.testCasesChanged(this);
	}

	/**
	* Notifies all registered listeners that the cell's test case parameters changed.
	*/
	protected void fireTestCaseParametersChanged() {
		for (TestableCellListener listener : listeners)
			listener.testCaseParametersChanged(this);
	}

	/**
	 * Customizes serialization, by recreating the listener list.
	 * @param stream the object input stream from which the object is to be read
	 * @throws IOException If any of the usual Input/Output related exceptions occur
	 * @throws ClassNotFoundException If the class of a serialized object cannot be found.
	 */
	private void readObject(java.io.ObjectInputStream stream)
			throws java.io.IOException, ClassNotFoundException {
	    stream.defaultReadObject();
		listeners = new ArrayList<TestableCellListener>();
	}
}