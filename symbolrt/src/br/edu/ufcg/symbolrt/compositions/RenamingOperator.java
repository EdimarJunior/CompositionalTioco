/*
 * SYmbolic Model-Based test case generation toOL for Real-Time systems (SYMBOLRT)
 * (C) Copyright 2010-2012 Federal University of Campina Grande (UFCG)
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
 * Wilkerson de Lucena Andrade      16/06/2011     An old class named TIOSTSPreprocessing (Version 1.2) was divided into two classes. This class only contains the synchronous algorithm algorithm (Version 1.0).
 * 
 */

package br.edu.ufcg.symbolrt.compositions;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import br.edu.ufcg.symbolrt.base.Action;
import br.edu.ufcg.symbolrt.base.ClockGuard;
import br.edu.ufcg.symbolrt.base.Location;
import br.edu.ufcg.symbolrt.base.SimpleClockGuard;
import br.edu.ufcg.symbolrt.base.TIOSTS;
import br.edu.ufcg.symbolrt.base.Transition;
import br.edu.ufcg.symbolrt.base.TypedData;
import br.edu.ufcg.symbolrt.compositions.exceptions.IncompatibleCompositionalOperationException;
import br.edu.ufcg.symbolrt.util.Constants;


/**
 * <code>SynchronousProduct</code> Class. <br>
 * This class contains an algorithm needed to perform the sequential composition between two TIOSTSs.
 * 
 * @author Adriana Carla Damasceno  ( <a href="mailto:adriana@copin.ufcg.edu.br">adriana@copin.ufcg.edu.br</a> )
 * 
 * @version 1.0
 * <br>
 * SYmbolic Model-Based test case generation toOL for Real-Time systems (SYMBOLRT)
 * <br>
 * (C) Copyright 2010-2012 Federal University of Campina Grande (UFCG)
 * <br>
 * <a href="https://sites.google.com/a/computacao.ufcg.edu.br/symbolrt">https://sites.google.com/a/computacao.ufcg.edu.br/symbolrt</a>
 */
public class RenamingOperator {

	private static RenamingOperator instance = null;

	private RenamingOperator() {
	}

	/**
	 * Method used to obtain an instance of this class.
	 * @return An instance of {@link RenamingOperator}.
	 */
	public static RenamingOperator getInstance() {
		if (instance == null) {
			instance = new RenamingOperator();
		}
		return instance;
	}

	/**
	 * Returns the sequential composition between two TIOSTSs.
	 * @param t1 The first TIOSTS.
	 * @param t1 The second TIOSTS.
	 * @return The sequential composition between t1 and t2.
	 * @throws IncompatibleSequentialComposition If t1 do not fit conditions demanded to it.  
	 */
	public TIOSTS renamingOperator(TIOSTS model1, String aName, String bName) throws IncompatibleCompositionalOperationException {

		TIOSTS result = null;
		
		TIOSTS tiosts1 = model1.clone();
		
		//Defining actions a		
		Action a = null;			
		for(Action action:tiosts1.getActions()){
			if (aName.equals(action.getName())){
				a = action.clone();
			}			
		}
		
		//Defining actions b		
		Action b = new Action(bName, a.getType());			
		for(Action action:tiosts1.getActions()){
			if (bName.equals(action.getName())){
				b = action.clone();
			}			
		}
		
		result = initialize(tiosts1, a, b);

		//Building the new set of transitions
		
		for(Transition t:tiosts1.getTransitions()){		
			Action action = t.getAction();
			if (!action.equals(a)){
				result.createTransition(t.getSource().getLabel(), t.getDataGuard(), t.getClockGuard(), action, t.getDataAssignments(), t.getClockAssignments(), t.getDeadline(), t.getTarget().getLabel());
			}else{
				result.createTransition(t.getSource().getLabel(), t.getDataGuard(), t.getClockGuard(), b, t.getDataAssignments(), t.getClockAssignments(), t.getDeadline(), t.getTarget().getLabel());
			}				
		}
		
		return result;
	}

	private TIOSTS initialize (TIOSTS tiosts1, Action a, Action b){
		TIOSTS result = new TIOSTS("REPLACE " + a.getName() + " BY " + b.getName() + " IN " + tiosts1.getName());
		
		List<Action> outputActions = new LinkedList<Action>();
		List<Action> inputActions = new LinkedList<Action>();
		List<TypedData> variables;
		List<TypedData> parameters;
		List<String> clocks;
		
		//Adding the internal action corresponding the the initiating location
		result.setInternalActions(tiosts1.getInternalActions());
		
		//Adding each action from the set of input actions, excluding a	
		for(Action action:tiosts1.getInputActions()){
			if(!action.equals(a)){
				inputActions.add(action);
			}else{
				inputActions.add(b);				
			}
		}
		result.setInputActions(inputActions);
		
		//Adding each action from the set of output actions, excluding a	
		for(Action action:tiosts1.getOutputActions()){
			if(!action.equals(a)){
				outputActions.add(action);
			}else{
				outputActions.add(b);				
			}
		}
		result.setOutputActions(outputActions);
		
		//Initializing variables, parameters, clocks and locations

		variables = tiosts1.getVariables();
		result.setVariables(variables);

		parameters = tiosts1.getActionParameters();
		result.setActionParameters(parameters);	

		clocks = tiosts1.getClocks();
		result.setClocks(clocks);
		
		for(Location l:tiosts1.getLocations()){
				Location loc = new Location(l.getLabel());
				result.addLocation(loc);
				if (l.isInitialLocation()){
					result.setInitialLocation(loc);
					loc.setIsInitialLocation(true);
				}
		}
		
		result.setInitialCondition(tiosts1.getInitialCondition());		
		return result;
	}

}