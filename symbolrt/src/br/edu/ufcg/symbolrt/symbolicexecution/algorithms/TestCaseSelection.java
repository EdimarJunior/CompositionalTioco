/*
 * SYmbolic Model-Based test case generation toOL for Real-Time systems (SYMBOLRT)
 * (C) Copyright 2010-2013 Federal University of Campina Grande (UFCG)
 * 
 * This file is part of SYMBOLRT.
 *
 * SYMBOLRT is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * SYMBOLRT is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with SYMBOLRT.  If not, see <http://www.gnu.org/licenses/>.
 *
 *
 * REVISION HISTORY:
 * Author                           Date           Brief Description
 * -------------------------------- -------------- ------------------------------
 * Wilkerson de Lucena Andrade      13/01/2011     Initial version
 * 
 */
package br.edu.ufcg.symbolrt.symbolicexecution.algorithms;

import java.util.ArrayList;
import java.util.List;

import br.edu.ufcg.symbolrt.base.Location;
import br.edu.ufcg.symbolrt.symbolicexecution.base.SymbolicExecutionTree;
import br.edu.ufcg.symbolrt.symbolicexecution.base.SymbolicState;
import br.edu.ufcg.symbolrt.symbolicexecution.base.SymbolicTransition;
import br.edu.ufcg.symbolrt.util.Constants;


/**
 * <code>TestCaseSelection</code> Class. <br>
 * This class contains the main algorithms to select test cases in a ZSET. 
 * 
 * @author Wilkerson de Lucena Andrade  ( <a href="mailto:wilkerson@computacao.ufcg.edu.br">wilkerson@computacao.ufcg.edu.br</a> )
 * 
 * @version 1.0
 * <br>
 * SYmbolic Model-Based test case generation toOL for Real-Time systems (SYMBOLRT)
 * <br>
 * (C) Copyright 2010-2013 Federal University of Campina Grande (UFCG)
 * <br>
 * <a href="https://sites.google.com/a/computacao.ufcg.edu.br/symbolrt">https://sites.google.com/a/computacao.ufcg.edu.br/symbolrt</a>
 */
public class TestCaseSelection {
	
	private static TestCaseSelection instance = null;


	private TestCaseSelection() {
	}

	
	/**
	 * Method used to obtain an instance of this class.
	 * @return An instance of {@link TestCaseSelection}.
	 */
	public static TestCaseSelection getInstance() {
		if (instance == null) {
			instance = new TestCaseSelection();
		}
		return instance;
	}
	
	
	/**
	 * Selects all possible test cases of the generated symbolic execution tree.
	 * @param zset The symbolic execution tree to be analyzed.
	 * @return A list containing all extracted test cases.
	 */
	public List<SymbolicExecutionTree> selectAllTestCases(SymbolicExecutionTree zset) {
		List<SymbolicExecutionTree> testCases = new ArrayList<SymbolicExecutionTree>();
		
		int counter = 0;
		for (SymbolicState zses : zset.getAcceptZSESs()) {
			testCases.add(selectTestCase("TestTree_TC" + ++counter, zses, zset));
		}
		
		return testCases;
	}
	
	
	// Select one test case based on the accept state parameter
	private SymbolicExecutionTree selectTestCase(String name, SymbolicState acceptZSES, SymbolicExecutionTree zset) {
		SymbolicExecutionTree testTree = new SymbolicExecutionTree(name);
		backwardTraversal(testTree, acceptZSES);
		forwardTraversal(testTree, zset);
		return testTree;
	}
	
	
	// Backward traversal from an accept ZSES to the root ZSES
	private SymbolicExecutionTree backwardTraversal(SymbolicExecutionTree testTree, SymbolicState acceptZSES){
		SymbolicState currentTTZSES = new SymbolicState(acceptZSES.getLocation(), acceptZSES.getPathCondition(), acceptZSES.getMapping(), acceptZSES.getZone());
		testTree.addSymbolicState(currentTTZSES);
		SymbolicState parentTTZSES = null;

		SymbolicTransition transition = acceptZSES.getParentTransition(); 
		while (transition != null) {
			SymbolicState parentZSES = transition.getSourceZSES();
			parentTTZSES = new SymbolicState(parentZSES.getLocation(), parentZSES.getPathCondition(), parentZSES.getMapping(), parentZSES.getZone());
			testTree.addSymbolicState(parentTTZSES);
			SymbolicTransition newTTTransition = new SymbolicTransition(parentTTZSES, transition.getSymbolicAction(), transition.getClockGuard().clone(), transition.getClockAssignments(), transition.getDeadline(), currentTTZSES);
			
			currentTTZSES.setParentTransition(newTTTransition);
			parentTTZSES.addChildTransition(newTTTransition);

			currentTTZSES = parentTTZSES;
			
			transition = parentZSES.getParentTransition();
		}
		
		testTree.setInitialZSES(parentTTZSES);

		return testTree;		
	}
	
	
	// Forward traversal performed in order to add missing inputs
	private SymbolicExecutionTree forwardTraversal(SymbolicExecutionTree testTree, SymbolicExecutionTree zset) {
		
		SymbolicState ttState = testTree.getInitialZSES();
		List<SymbolicTransition> ttInputTransitions = null;
		
		SymbolicState zsetState = zset.getInitialZSES();
		List<SymbolicTransition> zsetInputTransitions = null;
		
		while (ttState.getChildrenTransitions().size() != 0) {
			ttInputTransitions = ttState.getOutgoingInputTransitions();
			zsetInputTransitions = zsetState.getOutgoingInputTransitions();
			
			for (SymbolicTransition transition : getRemainingInputTransitions(ttInputTransitions, zsetInputTransitions)) {
				SymbolicState targetZSES = transition.getTargetZSES();
				//SymbolicState newTargetZSES = new SymbolicState(targetZSES.getLocation(), targetZSES.getPathCondition(), targetZSES.getMapping(), targetZSES.getZone());
				//SymbolicState newTargetZSES = new SymbolicState(new Location(targetZSES.getLocation().getLabel() + "_Inconc"), targetZSES.getPathCondition(), targetZSES.getMapping(), targetZSES.getZone());
				SymbolicState newTargetZSES = new SymbolicState(createInconclusiveLocation(targetZSES.getLocation()), targetZSES.getPathCondition(), targetZSES.getMapping(), targetZSES.getZone());
				testTree.addSymbolicState(newTargetZSES);
				SymbolicTransition newSymbolicTransition = new SymbolicTransition(ttState, transition.getSymbolicAction(), transition.getClockGuard().clone(), transition.getClockAssignments(), transition.getDeadline(), newTargetZSES);
				ttState.addChildTransition(newSymbolicTransition);
				newTargetZSES.setParentTransition(newSymbolicTransition);
			}
			
			ttState = ttState.getChildrenTransitions().get(0).getTargetZSES();
			zsetState = getNextZSES(ttState, zsetState);
		}
		
		
		return testTree;
	}
	
	// Identify missing inputs allowed by the specification
	private List<SymbolicTransition> getRemainingInputTransitions(List<SymbolicTransition> ttTransition, List<SymbolicTransition> zsetTransition) {
		List<SymbolicTransition> remainingInputTransitions = new ArrayList<SymbolicTransition>();

		for (SymbolicTransition transition : zsetTransition) {
			if (!ttTransition.contains(transition)) {
				remainingInputTransitions.add(transition);
			}
		}
		
		return remainingInputTransitions;
	}
	
	
	// Given a state of the test tree, this method returns the corresponding state in the complete execution tree. 
	private SymbolicState getNextZSES(SymbolicState nextTTState, SymbolicState previousZSETState){
		for (SymbolicTransition transition : previousZSETState.getChildrenTransitions()) {
			SymbolicState zses = transition.getTargetZSES();
			if ( nextTTState.getLocation().equals(zses.getLocation()) && nextTTState.getPathCondition().equals(zses.getPathCondition())
					&& nextTTState.getMapping().equals(zses.getMapping()) && nextTTState.getZone().equals(zses.getZone()) ) {
				return zses;
			}
		}
		return null;
	}
	
	// Creates a new inconclusive location for a test case.
	private Location createInconclusiveLocation(Location location) {
		String oldLabel = location.getLabel();
		String newLabel = "";
		
		if (oldLabel.endsWith(Constants.LOCATION_LABEL_REJECT)) {
			newLabel = oldLabel.substring(0, oldLabel.lastIndexOf(Constants.LOCATION_LABEL_REJECT)-1);
		} else if (oldLabel.endsWith(Constants.LOCATION_LABEL_ACCEPT)) {
			newLabel = oldLabel.substring(0, oldLabel.lastIndexOf(Constants.LOCATION_LABEL_ACCEPT)-1);
		} else {
			newLabel = oldLabel;
		}
		
		return new Location(newLabel + "_" + Constants.VERDICT_INCONCLUSIVE);
	}
	

}
