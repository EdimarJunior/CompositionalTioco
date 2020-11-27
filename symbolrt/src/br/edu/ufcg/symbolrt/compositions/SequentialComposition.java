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
public class SequentialComposition {

	private static SequentialComposition instance = null;

	private SequentialComposition() {
	}

	/**
	 * Method used to obtain an instance of this class.
	 * @return An instance of {@link SequentialComposition}.
	 */
	public static SequentialComposition getInstance() {
		if (instance == null) {
			instance = new SequentialComposition();
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
	public TIOSTS sequentialComposition(TIOSTS model1, TIOSTS model2) throws IncompatibleCompositionalOperationException {

		TIOSTS result = null;

		TIOSTS tiosts1 = model1.clone();
		TIOSTS tiosts2 = model2.clone();
		
		//Despising the init location	
		Transition t02 = tiosts2.getInitialLocation().getOutTransitions().get(0).getTarget().getOutTransitions().get(0);
		
		String compositionAction = t02.getAction().getName();
		Location l02 = t02.getSource();
		Location l02Linha = t02.getTarget();		
		
		Transition tc1 = getCompositionTransition(tiosts1, compositionAction);
		
		Location lc = tc1.getTarget();
		Location l0c1 = tc1.getSource();

		//if (isCompatible(tiosts1, tiosts2, lc)){
			result = initialize(tiosts1, tiosts2, tc1, t02);

			//Synchronizing transitions from both models
			Action actionlc = tc1.getAction();
			Action actionl02 = t02.getAction();

			Location source = new Location(tc1.getSource().getLabel()+ "," + t02.getSource().getLabel());
			Location target = new Location(tc1.getTarget().getLabel()+ "," + t02.getTarget().getLabel());
			result.addLocation(source);
			result.addLocation(target);
			String dataAssignments = createAssignments(tc1.getDataAssignments(), t02.getDataAssignments());
			String clockAssignments = createAssignments(tc1.getClockAssignments(), t02.getClockAssignments());
			
			//Performing the conjunction among the initial condition of tiosts2 and data conditions of transitions t1 and t2
			String dataGuard = createDataGuard(tc1.getDataGuard(), t02.getDataGuard());
			dataGuard = createDataGuard(dataGuard, tiosts2.getInitialCondition());
			ClockGuard clockGuard = createClockGuard(tc1.getClockGuard(), t02.getClockGuard());
			List<String> parameters = actionl02.getParameters();
			Action action = result.recoverAction(actionlc); 
			action.setParameters(parameters);
			result.createTransition(source.getLabel(), dataGuard, clockGuard, action, dataAssignments, clockAssignments, Constants.DEADLINE_LAZY, target.getLabel());

			//Building the new set of independent transitions from model 1
			for(Transition t:tiosts1.getTransitions()){
				//Despising the tc1 transition
				if (!t.equals(tc1)){
					if(t.getTarget().equals(l0c1)){
						//Replacing l0c1 by (l0c1,l02)
						result.createTransition(t.getSource().getLabel(), t.getDataGuard(), t.getClockGuard(), t.getAction(), t.getDataAssignments(), t.getClockAssignments(), t.getDeadline(), source.getLabel());
					}else{
						//if the source is l02, this transition is despised because it is replaced by (l0c1, lc1)-(l02, l02Linha)
						if(t.getSource().equals(l0c1)){
							result.createTransition(source.getLabel(), t.getDataGuard(), t.getClockGuard(), t.getAction(), t.getDataAssignments(), t.getClockAssignments(), t.getDeadline(), t.getTarget().getLabel());
						}
						else{
							result.createTransition(t.getSource().getLabel(), t.getDataGuard(), t.getClockGuard(), t.getAction(), t.getDataAssignments(), t.getClockAssignments(), t.getDeadline(), t.getTarget().getLabel());
						}
					}
				}
			}

			Transition init2 = tiosts2.getInitialLocation().getOutTransitions().get(0);
			//Building the new set of independent transitions from model 2
			for(Transition t:tiosts2.getTransitions()){
				//Despising the init2 and t02 transitions
				if ((!t.equals(init2) && !t.equals(t02))){
					if(t.getSource().equals(l02Linha)){
						//Replacing l02Linha by (lc1,l02Linha)
						result.createTransition(target.getLabel(), t.getDataGuard(), t.getClockGuard(), t.getAction(), t.getDataAssignments(), t.getClockAssignments(), t.getDeadline(), t.getTarget().getLabel());
					}else{
						//Replacing l02Linha by (lc1,l02Linha)
						if(t.getTarget().equals(l02Linha)){
							result.createTransition(t.getSource().getLabel(), t.getDataGuard(), t.getClockGuard(), t.getAction(), t.getDataAssignments(), t.getClockAssignments(), t.getDeadline(), target.getLabel());
						}else{
							result.createTransition(t.getSource().getLabel(), t.getDataGuard(), t.getClockGuard(), t.getAction(), t.getDataAssignments(), t.getClockAssignments(), t.getDeadline(), t.getTarget().getLabel());
						}
					}
				}
			}
			
		//}
		
		return result;
	}

	private TIOSTS initialize (TIOSTS tiosts1, TIOSTS tiosts2, Transition tc1, Transition t02){
		TIOSTS result = new TIOSTS(tiosts1.getName() +";"+tiosts2.getName());
		
		List<Action> outputActions;
		List<Action> inputActions;
		List<TypedData> variables;
		List<TypedData> parameters;
		List<String> clocks;
		
		//Adding the internal action corresponding the the initiating location
		result.setInternalActions(tiosts1.getInternalActions());

		outputActions = tiosts1.getOutputActions();
		outputActions.addAll(tiosts2.getOutputActions());
		result.setOutputActions(outputActions);

		//Inserting every action from S1 and S2, excepting a02 from S2
		inputActions = tiosts1.getInputActions();
		Action a02 = t02.getAction();
		for(Action a:tiosts2.getInputActions()){
			if(!a.equals(a02)){
				inputActions.add(a);
			}
		}		
		result.setInputActions(inputActions);

		//Performing union between variables, parameters and clocks of t1 and t2

		variables = tiosts1.getVariables();
		variables.addAll(tiosts2.getVariables());
		result.setVariables(variables);

		parameters = tiosts1.getActionParameters();
		//Excluding a02 parameters from the set of parameters
		List<String> parametersa02 = a02.getParameters();
		//TODO Por que nao detecta o parametro de a02 como igual?
		for(TypedData tp:tiosts2.getActionParameters()){
			if (!tp.equals(parametersa02)){
				parameters.add(tp);
			}
		}
		result.setActionParameters(parameters);	

		clocks = tiosts1.getClocks();
		// TODO Adjust the set of clocks according to the parser.
		clocks.addAll(tiosts2.getClocks());
		result.setClocks(clocks);
		
		//Performing union between locations, excepting l0c1, lc1, l02, l'02 and START2
		
		Location l02 = t02.getSource();
		Location l02Linha = t02.getTarget();
		Location l0c1 = tc1.getSource();
		Location lc1 = tc1.getTarget();
		Location start2 = t02.getSource().getInTransitions().get(0).getSource();
		
		for(Location l:tiosts1.getLocations()){
			if(!l.equals(lc1) && !l.equals(l0c1)){
				Location loc = new Location(l.getLabel());
				result.addLocation(loc);
				if (l.isInitialLocation()){
					result.setInitialLocation(loc);
					loc.setIsInitialLocation(true);
				}
			}
		}
		
		for(Location l:tiosts2.getLocations()){
			if(!l.equals(l02) && !l.equals(l02Linha) && !l.equals(start2)){
				result.addLocation(new Location(l.getLabel()));
			}
		}
		
		result.setInitialCondition(tiosts1.getInitialCondition());
		
		return result;
	}
	//TODO: inserir restricao de clock em t02

	private boolean isCompatible(TIOSTS tiosts1, TIOSTS tiosts2, Location lc) throws IncompatibleCompositionalOperationException{
		boolean resp = true;		

		List<Transition> outTransitionslc1 =  lc.getOutTransitions();
		List<Transition> inTransitionslc1 = lc.getInTransitions();

		//Verifying that the composition node has one input and no output transitions
		if (!(outTransitionslc1.isEmpty() && inTransitionslc1.size() == 1)){
			throw new IncompatibleCompositionalOperationException("The final location of the first model should not contain output transitions or it has input transitions diferent from 1.");
		}

		//Verifying that assignments from tc1 is empty		
		Transition tc1 = inTransitionslc1.get(0);
		Action actiontc1 = tc1.getAction();
		String assignmentstc1 = tc1.getDataAssignments(); 

		if (assignmentstc1 != null){
			throw new IncompatibleCompositionalOperationException("The assignments of the composition location must be empty.");								
		}

		// Verifying that action is a single output action in tiosts1
		resp = resp && isSingleAction(tiosts1, actiontc1, Constants.ACTION_OUTPUT);

		//Despising the init location of the second TIOSTS
		List<Transition> outputTransitionsInit2 = tiosts2.getInitialLocation().getOutTransitions();

		//Verifying that the initial location of tiosts2 has only one outgoing transition 		
		List<Transition> outputTransitionslo2 = outputTransitionsInit2.get(0).getTarget().getOutTransitions();
		Action actionl02 = outputTransitionslo2.get(0).getAction();

		if (!(outputTransitionslo2.size() == 1 && (actionl02.getType() == Constants.ACTION_INPUT))){
			throw new IncompatibleCompositionalOperationException("There is more than one output transition after the initial location of the second TIOSTS or the transition has no input action.");
		}	

		// Verifying that actionl02 is a single input action in tiosts2
		resp = resp && isSingleAction(tiosts2, actionl02, Constants.ACTION_INPUT);

		//Verifying that actionl02 and actionlf1 have the same label
		if(!(actiontc1.getName()).equals(actionl02.getName())){
			throw new IncompatibleCompositionalOperationException("Actions from both TIOSTS do not synchronize.");
		}

		//parameter matching between synchronizing actions of tiosts1 and tiosts2
		List<String> parameterslf1 = actiontc1.getParameters();
		List<String> parametersl02 = actionl02.getParameters();
		if (parameterslf1.size() != parametersl02.size()){
			throw new IncompatibleCompositionalOperationException("The number of parameters between the synchronizing actions of the TIOSTS must be equal.");
		}	

		//Verifying that there is no intersection between locations of T1 and T2
		if (intersection(tiosts1.getLocationNames(), tiosts2.getLocationNames()) == true){			
			throw new IncompatibleCompositionalOperationException("The set of locations from both TIOSTSs must be disjoint.");
		}

		//Verifying that there is no intersection between variables of T1 and T2
		if (intersection(tiosts1.getVariableNames(), tiosts2.getVariableNames()) == true){
			throw new IncompatibleCompositionalOperationException("The set of variables from both TIOSTSs must be disjoint.");
		}	
		return resp;
	}

	private Transition getCompositionTransition(TIOSTS tiosts, String compositeAction) throws IncompatibleCompositionalOperationException{
		Collection<Transition> transitions = tiosts.getTransitions();

		Transition compositeTransition = null;
		for(Transition t: transitions){					

			//Verifies if the current transition t is the only transition which contains action
			boolean foundTransition = false;
			String actionTransition = t.getAction().getName();
			
			if (actionTransition.equals(compositeAction)){				
				if(foundTransition == false){
					compositeTransition = t;
					foundTransition = true;				
				}else{
					throw new IncompatibleCompositionalOperationException("Only one transition containg the "+ compositeAction +"is allowed in TIOSTS 1.");
				}
				
			}
		}

		return compositeTransition;
	}
	
	private boolean isSingleAction(TIOSTS tiosts, Action action, int type) throws IncompatibleCompositionalOperationException{
		boolean resp = true;
		
		if(action.getType() == type){
			boolean foundAction = false;
			for(Transition t:tiosts.getTransitions()){
				if (action.getName().equals(t.getAction().getName()) && action.getType() != t.getAction().getType()){
					if (foundAction == false){
						foundAction = true;								
					}else{
						throw new IncompatibleCompositionalOperationException("More than one action " + action.getName() + " was found in the TIOSTS.");
					}
				}
			}	

		}else{
			throw new IncompatibleCompositionalOperationException("Some action type of a TIOSTS is wrong.");
		}		
		return resp;		
	}
	
	/**
	 * Creates the assignments of a transition considering the assignments of specification and test purpose.
	 * @param specAssignments The assignments of a transition of specification.
	 * @param tpAssignments The assignments of a transition of test purpose.
	 * @return The assignment resulting from merging assignments of specification and test purpose. 
	 */
	private boolean intersection(List<String> colecao1, List<String> colecao2){
		boolean isIntersection = false;

		for (String c:colecao1){
			if (colecao2.contains(c)){
				isIntersection = true;
			}
		}		

		return isIntersection;
	}
	
	/**
	 * Creates the assignments of a transition considering the assignments of specification and test purpose.
	 * @param specAssignments The assignments of a transition of specification.
	 * @param tpAssignments The assignments of a transition of test purpose.
	 * @return The assignment resulting from merging assignments of specification and test purpose. 
	 */
	private String createAssignments(String specAssignments, String tpAssignments){
		if (tpAssignments == null) {
			return specAssignments;
		} else if (specAssignments == null) {
			return tpAssignments;
		} else {
			return specAssignments + Constants.ASSIGNMENT_SEPARATOR + tpAssignments;
		}
	}

	/**
	 * Creates the data guard of a transition considering the guard of specification and test purpose.
	 * @param specGuard The guard of a transition of specification.
	 * @param tpGuard The guard of a transition of test purpose.
	 * @return The data guard resulting from conjunction of guards of specification and test purpose. 
	 */
	private String createDataGuard(String specGuard, String tpGuard){
		if (tpGuard == Constants.GUARD_TRUE) {
			return specGuard;
		} else if (specGuard == Constants.GUARD_TRUE){
			return tpGuard;
		} else {
			return specGuard + " " + Constants.GUARD_CONJUNCTION + " " + tpGuard;
		}
	}
	
	/**
	 * Creates the clock guard of a transition considering the guard of specification and test purpose.
	 * @param specGuard The guard of a transition of specification.
	 * @param tpGuard The guard of a transition of test purpose.
	 * @return The clock guard resulting from conjunction of guards of specification and test purpose. 
	 */
	private ClockGuard createClockGuard(ClockGuard specGuard, ClockGuard tpGuard){
		ClockGuard newClockGuard = new ClockGuard();
		
		for (SimpleClockGuard simpleClockGuard : specGuard.getClockGuard()) {
			newClockGuard.addSimpleClockGuard(simpleClockGuard.clone());
		}
		
		for (SimpleClockGuard simpleClockGuard : tpGuard.getClockGuard()) {
			newClockGuard.addSimpleClockGuard(simpleClockGuard.clone());
		}
		return newClockGuard;
	}

}