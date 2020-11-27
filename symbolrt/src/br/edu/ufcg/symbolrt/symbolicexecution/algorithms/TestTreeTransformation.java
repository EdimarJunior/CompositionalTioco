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
 * Wilkerson de Lucena Andrade      14/01/2011     Initial version
 * 
 */
package br.edu.ufcg.symbolrt.symbolicexecution.algorithms;

import java.util.ArrayList;
import java.util.List;

import br.edu.ufcg.symbolrt.base.Action;
import br.edu.ufcg.symbolrt.base.Location;
import br.edu.ufcg.symbolrt.base.TIOSTS;
import br.edu.ufcg.symbolrt.base.TypedData;
import br.edu.ufcg.symbolrt.symbolicexecution.base.SymbolicAction;
import br.edu.ufcg.symbolrt.symbolicexecution.base.SymbolicExecutionTree;
import br.edu.ufcg.symbolrt.symbolicexecution.base.SymbolicState;
import br.edu.ufcg.symbolrt.symbolicexecution.base.SymbolicTransition;
import br.edu.ufcg.symbolrt.util.Constants;


/**
 * <code>TestTreeTransformation</code> Class. <br>
 * This class contains the main algorithms to translate a test tree into a TIOSTS test case. 
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
public class TestTreeTransformation {

	private static TestTreeTransformation instance = null;


	private TestTreeTransformation() {
	}

	
	/**
	 * Method used to obtain an instance of this class.
	 * @return An instance of {@link TestCaseSelection}.
	 */
	public static TestTreeTransformation getInstance() {
		if (instance == null) {
			instance = new TestTreeTransformation();
		}
		return instance;
	}
	
	
	/**
	 * Translates a test tree into a TIOSTS test case.
	 * @param testTree The test tree to translate.
	 * @param sp The synchronous product needed to get some missing information.
	 * @return The TIOSTS test case.
	 */
	public TIOSTS translateTestTree(SymbolicExecutionTree testTree, TIOSTS sp){
		TIOSTS testCase = new TIOSTS(testTree.getName());
		
		// Add clocks to the test case
		for (String clock : sp.getClocks()) {
			testCase.addClock(clock);
		}
	
		SymbolicState initialZSES = testTree.getInitialZSES();
		
		// Add variables to the test case
		for (String varName : initialZSES.getMapping().keySet()) {
			TypedData variable = sp.getVariable(varName); 
			if (variable != null) {
				testCase.addVariable(new TypedData(initialZSES.getMapping().get(varName), variable.getType()));
			}
		}
		
		// Initial Location
		Location initialLocation = new Location(initialZSES.getLocation().getLabel());
		testCase.addLocation(initialLocation);
		testCase.setInitialLocation(initialLocation);
		

		List<SymbolicState> unvisited = new ArrayList<SymbolicState>();
		unvisited.add(initialZSES);
		String currentLocationLabel = "";
		
		while (unvisited.size() != 0) {
			SymbolicState currentZSES = unvisited.remove(0); // Pick and remove the first element of the unvisited list
			currentLocationLabel = currentZSES.getLocation().getLabel();
			
			for (SymbolicTransition symbolicTransition : currentZSES.getChildrenTransitions()) {
				SymbolicState targetZSES = symbolicTransition.getTargetZSES();
				Location targetLocation = new Location(targetZSES.getLocation().getLabel());
				testCase.addLocation(targetLocation);
				
				SymbolicAction symbolicAction = symbolicTransition.getSymbolicAction();
				Action action = new Action(symbolicAction.getAction().getName(), symbolicAction.getAction().getType(), symbolicAction.getParamIds());
				testCase.addAction(action);
				
				// Add action parameters to the test case
				for (String paramId : action.getParameters()) {
					TypedData actionParameter = sp.getActionParameter(paramId.split(Constants.SYMBOLIC_VALUE_SEPARATOR)[0]);
					if (actionParameter != null && testCase.getActionParameter(paramId) == null) {
						testCase.addActionParameter(new TypedData(paramId, actionParameter.getType()));
					}
				}
				
				testCase.createTransition(currentLocationLabel, targetZSES.getPathCondition(), symbolicTransition.getClockGuard().clone(), action, null, symbolicTransition.getClockAssignments(), symbolicTransition.getDeadline(), targetLocation.getLabel());
				
				unvisited.add(targetZSES);
			}
		
		}
		
		// Initial condition of the test case. TODO This line is not needed... Remove after developing a test case
		testCase.setInitialCondition(testCase.getInitialLocation().getOutTransitions().get(0).getDataGuard()); // All initial locations have only one outgoing transition and no clock guard.
		
		return testCase;
	}

	
}
