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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
public class InterruptionComposition {
	private static InterruptionComposition instance = null;

	private InterruptionComposition() {
	}

	/**
	 * Method used to obtain an instance of this class.
	 * @return An instance of {@link InterruptionComposition}.
	 */
	public static InterruptionComposition getInstance() {
		if (instance == null) {
			instance = new InterruptionComposition();
		}
		return instance;
	}

	/**
	 * Returns the sequential composition between two TIOSTSs.
	 * @param t1 The first TIOSTS.
	 * @param t2 The second TIOSTS.
	 * @return The interruption composition between t1 and t2.
	 * @throws IncompatibleCompositionalOperationException If t1 and t2 do not fit conditions demanded to it.  
	 */
	public TIOSTS interruptionComposition(TIOSTS model1, TIOSTS model2) throws IncompatibleCompositionalOperationException {
		TIOSTS result = null;
		//if (isCompatible(model1, model2)){
			TIOSTS tiosts1 = model1.clone();
			TIOSTS tiosts2 = model2.clone();
			
			Collection<String> eSync = intersectionActions(tiosts1.getActions(), tiosts2.getActions());			
			result = initialize(tiosts1, tiosts2, eSync);			
			result = sync(tiosts1.getTransitions(), tiosts2.getTransitions(), eSync, result);			
		//}
		
		return result;
	}

	private TIOSTS initialize (TIOSTS tiosts1, TIOSTS tiosts2, Collection<String> eSync){
		TIOSTS result = new TIOSTS(tiosts1.getName() + "*" + tiosts2.getName());
		
		List<Action> outputActions = new ArrayList<Action>();
		List<Action> inputActions = new ArrayList<Action>();
		List<TypedData> variables;
		List<TypedData> parameters;
		List<String> clocks;
		Collection<Transition> transitions1 = tiosts1.getTransitions();
		Collection<Transition> transitions2 = tiosts2.getTransitions();		
		
		//Adding the internal action corresponding the the initiating location
		result.setInternalActions(tiosts1.getInternalActions());
		
		//Adding output actions
		for(Action a:tiosts1.getOutputActions()){
			if (!outputActions.contains(a)){
				outputActions.add(a);
			}			
		}
		
		for(Action a:tiosts2.getOutputActions()){
			if (!outputActions.contains(a)){
				outputActions.add(a);
			}			
		}
		result.setOutputActions(outputActions);
	
		//Inserting every action and parameter from S1 and S2, excepting input actions and parameters from the synchronization set
		parameters = tiosts1.getActionParameters();
		parameters.addAll(tiosts2.getActionParameters());
		
		for(Action a:tiosts1.getInputActions()){
				String aName = a.getName();
				if(!eSync.contains(aName)){
					inputActions.add(a);			
				}
		}
		
		for(Action a:tiosts2.getInputActions()){
			String aName = a.getName();
			if(!eSync.contains(aName)){
				inputActions.add(a);			
			}
		}
		
		result.setInputActions(inputActions);
		result.setActionParameters(parameters);	

		//Performing union between variables, parameters and clocks of t1 and t2
		variables = tiosts1.getVariables();
		variables.addAll(tiosts2.getVariables());
		result.setVariables(variables);		

		clocks = tiosts1.getClocks();
		// TODO Adjust the set of clocks according to the parser.
		//clocks.addAll(tiosts2.getClocks());
		result.setClocks(clocks);
		
		result.setInitialCondition(tiosts1.getInitialCondition());
		

		//Performing union between locations, excepting the excluded ones	
		Collection<Location> excludedLocations = buildSynchronizationLocations(tiosts1, tiosts2, eSync);
		
		for(Location l:tiosts1.getLocations()){
			if(!excludedLocations.contains(l)){
				Location loc = new Location(l.getLabel());
				result.addLocation(loc);
				if (l.isInitialLocation()){
					result.setInitialLocation(loc);
					loc.setIsInitialLocation(true);
				}
			}
		}		
		for(Location l:tiosts2.getLocations()){
			if(!excludedLocations.contains(l)){
				result.addLocation(new Location(l.getLabel()));
			}
		}
		
		//Adding transitions from tiosts1 that do not synchronize and are not changed by the sync process	
		for(Transition t:transitions1){
				if(!excludedLocations.contains(t.getSource()) && !excludedLocations.contains(t.getTarget())){
					result.createTransition(t.getSource().getLabel(), t.getDataGuard(), t.getClockGuard(), t.getAction(), t.getDataAssignments(), t.getClockAssignments(), t.getDeadline(), t.getTarget().getLabel());
				}							
		}
		
		//Adding transitions from tiosts2 that do not synchronize, are not the initial transition of tiosts2 and are not changed by the sync process	
		for(Transition t:transitions2){
			if(!excludedLocations.contains(t.getSource()) && !excludedLocations.contains(t.getTarget())){
				result.createTransition(t.getSource().getLabel(), t.getDataGuard(), t.getClockGuard(), t.getAction(), t.getDataAssignments(), t.getClockAssignments(), t.getDeadline(), t.getTarget().getLabel());
			}							
		}
		
		return result;
	}
	//TODO: inserir restricao de clock em t02

	private boolean isCompatible(TIOSTS tiosts1, TIOSTS tiosts2) throws IncompatibleCompositionalOperationException{
		boolean resp = true;
		
		Transition tc1 = null;
		Transition tc1Linha = null;
		Transition t02 = null;
		Transition tc2 = null;
		
		//The model 2 must have a single initial transition		
		Collection<Transition> initialTransitions2 = tiosts2.getInitialLocation().getOutTransitions().get(0).getTarget().getOutTransitions();
		if  (initialTransitions2.size() != 1){
			throw new IncompatibleCompositionalOperationException("The initial location of the second TIOSTS has more than one or no transitions.");		
		}
		
		//The initial transition of TIOSTS 2 has not an input action
		Action actiont02 = tiosts2.getInitialLocation().getOutTransitions().get(0).getTarget().getOutTransitions().get(0).getAction();
		if  (actiont02.getType() != Constants.ACTION_INPUT){
			throw new IncompatibleCompositionalOperationException("The initial transition of the second TIOSTS does not have an input action.");		
		}
		
		//Action t02 cannot be present in any other transition from model 2	
		t02 = tiosts2.getInitialLocation().getOutTransitions().get(0).getTarget().getOutTransitions().get(0);
		for(Transition transition2: tiosts2.getTransitions()){
			String action2 = transition2.getAction().getName();
			if (action2.equals(actiont02.getName()) && !transition2.equals(t02)){
				throw new IncompatibleCompositionalOperationException("The action from the initial transition of the second TIOSTS should not belong to any other transition.");
			}
		}
		
		//Action tc1 must be present containing an action with the same label from actiont02 and must be the only one to contain actiont02 label
		boolean found = false;
		for(Transition transition1: tiosts1.getTransitions()){
			String action2 = transition1.getAction().getName();
			if (action2.equals(actiont02.getName())){
				if (found == false){
					tc1 = transition1;
				}else{
					throw new IncompatibleCompositionalOperationException("There is more than one transition with " + action2 + " action in the first TIOSTS.");
				}
			}
		}	
		
		//tc1 must be present with a single transition between two locations
		if (tc1 == null){
			throw new IncompatibleCompositionalOperationException("There is no transition with " + actiont02.getName() + " in the second TIOSTS.");
		}else{
			if(tc1.getSource().getInTransitions().size() != 1 && tc1.getTarget().getOutTransitions().size() != 1){
				throw new IncompatibleCompositionalOperationException("The transition with " + actiont02.getName() + " in the first TIOSTS is not a single transition between two locations.");
			}
		}
		
		//Action tc1 must be present containing an action with the same label from actiont02
		tc1Linha = tc1.getTarget().getOutTransitions().get(0);
		Action actiontc1Linha = tc1Linha.getAction();
		found = false;
		for(Transition transition1: tiosts1.getTransitions()){
			String action2 = transition1.getAction().getName();
			if (action2.equals(actiontc1Linha.getName()) && !transition1.equals(tc1Linha)){				
					throw new IncompatibleCompositionalOperationException("There is more than one transition with " + action2 + " action in the first TIOSTS.");
			}
		}
		//tc1Linha must be present with a single transition between two locations
		if(tc1Linha.getSource().getInTransitions().size() != 1 && tc1Linha.getTarget().getOutTransitions().size() != 1){
			throw new IncompatibleCompositionalOperationException("The transition with " + tc1Linha.getAction().getName() + " in the first TIOSTS is not a single transition between two locations.");
		}
		//tc1Linha must be present with a single transition between two locations
		if(tc1Linha.getSource().getInTransitions().size() != 1 && tc1Linha.getTarget().getOutTransitions().size() != 1){
			throw new IncompatibleCompositionalOperationException("The transition with " + tc1Linha.getAction().getName() + " in the first TIOSTS is not a single transition between two locations.");
		}
		
		//Finding tc2		
		for(Transition transition2:tiosts2.getTransitions()){
			Action action2 = transition2.getAction();
			if(action2.getName().equals(actiontc1Linha.getName())){
				if (tc2 == null){
					tc2 = transition2;
				}else{
					throw new IncompatibleCompositionalOperationException("The transition with " + action2.getName()+" is duplicated in the second TIOSTS.");
				}
			}
		}
		
		//tc2 must be present with a single transition between two locations
		if(tc2.getSource().getInTransitions().size() != 1 && tc2.getTarget().getOutTransitions().size() != 1){
			throw new IncompatibleCompositionalOperationException("The transition with " + tc1Linha.getAction().getName() + " in the second TIOSTS is not a single transition between two locations.");
		}
		
		//Every synchronization transition must have lazy deadline
		
		if (tc1.getDeadline() != Constants.DEADLINE_LAZY){
			throw new IncompatibleCompositionalOperationException("The transition with"+tc1.getAction().getName() + " in the first TIOSTS does not have Lazy deadline");
		}
		if (tc1Linha.getDeadline() != Constants.DEADLINE_LAZY){
			throw new IncompatibleCompositionalOperationException("The transition with"+tc1Linha.getAction().getName() + " in the first TIOSTS does not have Lazy deadline");
		}
		if (tc2.getDeadline() != Constants.DEADLINE_LAZY){
			throw new IncompatibleCompositionalOperationException("The transition with"+tc2.getAction().getName() + " in the second TIOSTS does not have Lazy deadline");
		}
		if (t02.getDeadline() != Constants.DEADLINE_LAZY){
			throw new IncompatibleCompositionalOperationException("The transition with"+t02.getAction().getName() + " in the second TIOSTS does not have Lazy deadline");
		}
		
		//Verifying that assignments of tc1 and t02 are empty
		String assignmentstc1 = tc1.getDataAssignments(); 

		if (assignmentstc1 != null){
			throw new IncompatibleCompositionalOperationException("The assignments of the transition "+tc1.getSource().getLabel()+"-"+tc1.getTarget().getLabel()+" must be empty.");								
		}
		String assignmentstc2 = tc2.getDataAssignments(); 

		if (assignmentstc2 != null){
			throw new IncompatibleCompositionalOperationException("The assignments of the transition "+tc2.getSource().getLabel()+"-"+tc2.getTarget().getLabel()+" must be empty.");								
		}
		
		//Verifying parameter matching between synchronizing actions of tiosts1 and tiosts2
		List<String> parameterstc1 = tc1.getAction().getParameters();
		List<String> parameterst02 = t02.getAction().getParameters();
		if (parameterstc1.size() != parameterst02.size()){
			throw new IncompatibleCompositionalOperationException("Parameters from the first synchronization pair of actions do not have the same lenght.");
		}
		
		List<String> parameterstc1Linha = tc1Linha.getAction().getParameters();
		List<String> parameterstc2 = tc2.getAction().getParameters();
		if (parameterstc1Linha.size() != parameterstc2.size()){
			throw new IncompatibleCompositionalOperationException("Parameters from the first synchronization pair of actions do not have the same lenght.");
		}
		
		//Verifying that there is no intersection between locations of T1 and T2
		Set<Location> intersectionLocations = new HashSet<Location>(tiosts1.getLocations());
		intersectionLocations.retainAll(tiosts2.getLocations());
		if (!intersectionLocations.isEmpty()){			
			throw new IncompatibleCompositionalOperationException("The set of locations from both TIOSTSs must be disjoint.");
		}

		//Verifying that there is no intersection between variables of T1 and T2
		
		//Verifying that there is no intersection between locations of T1 and T2
		Set<TypedData> intersectionVariables = new HashSet<TypedData>(tiosts1.getVariables());
		intersectionVariables.retainAll(tiosts2.getVariables());
		if (!intersectionVariables.isEmpty()){			
			throw new IncompatibleCompositionalOperationException("The set of variables from both TIOSTSs must be disjoint.");
		}		
		return resp;
	}
	
	private Collection<Location> buildSynchronizationLocations(TIOSTS tiosts1, TIOSTS tiosts2, Collection<String> eSync){
		
		Collection<Location> excludedLocations = new HashSet<Location>();
		
		for(String actionSync:eSync){
			for(Transition transition1:tiosts1.getTransitions()){
				Action action = transition1.getAction();
				String actionName = action.getName();
				if(actionName.equals(actionSync)){					
					excludedLocations.add(transition1.getSource());
					excludedLocations.add(transition1.getTarget());				
				}
			}						
		}	
		
		for(String actionSync:eSync){
			for(Transition transition1:tiosts2.getTransitions()){
				Action action = transition1.getAction();
				String actionName = action.getName();
				if(actionName.equals(actionSync)){					
					excludedLocations.add(transition1.getSource());
					excludedLocations.add(transition1.getTarget());				
				}
			}						
		}
		return excludedLocations;
	}
	
	/**
	 * Creates the assignments of a transition considering the assignments of specification and test purpose.
	 * @param specAssignments The assignments of a transition of specification.
	 * @param tpAssignments The assignments of a transition of test purpose.
	 * @return The assignment resulting from merging assignments of specification and test purpose. 
	 */
	private Collection<String> intersectionActions(List<Action> colecao1, List<Action> colecao2){
		Collection<String> intersectionSet = new ArrayList<String>();
		for (Action c1:colecao1){
			for(Action c2:colecao2){
				//They have the same label, different types, can synchronize and the element was not inserted in the set
				if (c1.getName().equals(c2.getName()) && c1.getType() != c2.getType() && !intersectionSet.contains(c1.getName())){
					intersectionSet.add(c1.getName());
				}
			}
		}		
		return intersectionSet;
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
	
	private TIOSTS sync(Collection<Transition> transitions1, Collection<Transition> transitions2, Collection<String> eSync, TIOSTS resultTIOSTS){

		Collection<Location> excludedLocations = new HashSet<Location>();

		//Updating the synchronization transitions from both models
		for (String actionSync:eSync){

			//Recovering synchronization transitions from both models
			Transition t1Sync = recoverTransition(transitions1, actionSync);
			Transition t2Sync = recoverTransition(transitions2, actionSync);

			//Updating the set of excluded locations
			excludedLocations.add(t1Sync.getSource());
			excludedLocations.add(t2Sync.getSource());
			excludedLocations.add(t1Sync.getTarget());
			excludedLocations.add(t2Sync.getTarget());

			//Creating the new transitions from the composed model			
			Location source = new Location(t1Sync.getSource().getLabel() +","+ t2Sync.getSource().getLabel());
			Location target = new Location(t1Sync.getTarget().getLabel() +","+ t2Sync.getTarget().getLabel());
			resultTIOSTS.addLocation(source);
			resultTIOSTS.addLocation(target);
			//Building the dataGuard of synchronization transition			
			String dataGuard = createDataGuard(t1Sync.getDataGuard(), t2Sync.getDataGuard());
			Transition transitionl1 = t2Sync.getSource().getInTransitions().get(0);
			Location l1 = transitionl1.getSource();
			if (l1.isInitialLocation()){
				dataGuard = createDataGuard(dataGuard, transitionl1.getDataGuard());
			}	

			//Adding the new transition	
			String dataAssignments = createAssignments(t1Sync.getDataAssignments(), t2Sync.getDataAssignments());
			String clockAssignments = createAssignments(t1Sync.getClockAssignments(), t2Sync.getClockAssignments());
			ClockGuard clockGuard = createClockGuard(t1Sync.getClockGuard(), t2Sync.getClockGuard());
			Action action = null;
			if(t1Sync.getAction().getType() == Constants.ACTION_OUTPUT){
				action = resultTIOSTS.recoverAction(t1Sync.getAction());
				action.setParameters(t1Sync.getAction().getParameters());				
			}else{
				action = resultTIOSTS.recoverAction(t2Sync.getAction());
				action.setParameters(t2Sync.getAction().getParameters());
			}	

			resultTIOSTS.createTransition(source.getLabel(), dataGuard, clockGuard, action, dataAssignments, clockAssignments, Constants.DEADLINE_LAZY, target.getLabel());

		}

		//Updating the new set of independent transitions from model 1
		for (String actionSync:eSync){			
			//Recovering synchronization transitions from the resulting model
			Transition tSync = recoverTransition(resultTIOSTS.getTransitions(), actionSync);

			//Recovering synchronization transitions from both models
			Transition t1Sync = recoverTransition(transitions1, actionSync);
			Transition t2Sync = recoverTransition(transitions2, actionSync);

			//Updating the new set of independent transitions from model 1 whose source or target locations compose the synchronization transitions.
			for(Transition t:transitions1){
				//Despising the synchronizations transitions
				if (!eSync.contains(t.getAction().getName())){
					if(t.getTarget().equals(t1Sync.getSource())){
						resultTIOSTS.createTransition(t.getSource().getLabel(), t.getDataGuard(), t.getClockGuard(), t.getAction(), t.getDataAssignments(), t.getClockAssignments(), t.getDeadline(), tSync.getSource().getLabel());
					}else{
						if(t.getTarget().equals(t1Sync.getTarget())){
							resultTIOSTS.createTransition(t.getSource().getLabel(), t.getDataGuard(), t.getClockGuard(), t.getAction(), t.getDataAssignments(), t.getClockAssignments(), t.getDeadline(), tSync.getTarget().getLabel());
						}else{
							if(t.getSource().equals(t1Sync.getTarget())){
								resultTIOSTS.createTransition(tSync.getTarget().getLabel(), t.getDataGuard(), t.getClockGuard(), t.getAction(), t.getDataAssignments(), t.getClockAssignments(), t.getDeadline(), t.getTarget().getLabel());
							}else{
								if(t.getSource().equals(t1Sync.getSource())){
									resultTIOSTS.createTransition(tSync.getSource().getLabel(), t.getDataGuard(), t.getClockGuard(), t.getAction(), t.getDataAssignments(), t.getClockAssignments(), t.getDeadline(), t.getTarget().getLabel());
								}
							}							
						}
					}
				}
			}

			//Updating the new set of independent transitions from model 2
			for(Transition t:transitions2){
				//Despising the init2 and synchronization transitions				
				if ((!t.getSource().isInitialLocation() && !eSync.contains(t.getAction().getName()))){					
					if(t.getSource().equals(t2Sync.getTarget())){
						if (!excludedLocations.contains(t.getTarget())){
							resultTIOSTS.createTransition(tSync.getTarget().getLabel(), t.getDataGuard(), t.getClockGuard(), t.getAction(), t.getDataAssignments(), t.getClockAssignments(), t.getDeadline(), t.getTarget().getLabel());
						}else{
							//Recovering the next synchronization transition from the resulting system to insert this new transition
							Action actionTempSync = t.getTarget().getOutTransitions().get(0).getAction();
							Transition tTempSync = recoverTransition(resultTIOSTS.getTransitions(), actionTempSync.getName());
							resultTIOSTS.createTransition(tSync.getTarget().getLabel(), t.getDataGuard(), t.getClockGuard(), t.getAction(), t.getDataAssignments(), t.getClockAssignments(), t.getDeadline(), tTempSync.getSource().getLabel()); 
						}						
					}else{
						if(t.getTarget().equals(t2Sync.getSource())){
							if (!excludedLocations.contains(t.getSource())){
								resultTIOSTS.createTransition(t.getSource().getLabel(), t.getDataGuard(), t.getClockGuard(), t.getAction(), t.getDataAssignments(), t.getClockAssignments(), t.getDeadline(), tSync.getSource().getLabel());
							}else{
								//Recovering the previous synchronization transition from the resulting system to insert this new transition
								Action actionTempSync = t.getSource().getInTransitions().get(0).getAction();
								Transition tTempSync = recoverTransition(resultTIOSTS.getTransitions(), actionTempSync.getName());
								resultTIOSTS.createTransition(tTempSync.getTarget().getLabel(), t.getDataGuard(), t.getClockGuard(), t.getAction(), t.getDataAssignments(), t.getClockAssignments(), t.getDeadline(), tSync.getSource().getLabel()); 
							}							
						}else{
							if(t.getTarget().equals(t2Sync.getTarget())){
								if (!excludedLocations.contains(t.getSource())){
									resultTIOSTS.createTransition(t.getSource().getLabel(), t.getDataGuard(), t.getClockGuard(), t.getAction(), t.getDataAssignments(), t.getClockAssignments(), t.getDeadline(), tSync.getTarget().getLabel());
								}else{
									//Recovering the previous synchronization transition from the resulting system to insert this new transition
									Action actionTempSync = t.getSource().getOutTransitions().get(0).getAction();
									Transition tTempSync = recoverTransition(resultTIOSTS.getTransitions(), actionTempSync.getName());
									resultTIOSTS.createTransition(tTempSync.getSource().getLabel(), t.getDataGuard(), t.getClockGuard(), t.getAction(), t.getDataAssignments(), t.getClockAssignments(), t.getDeadline(), tSync.getSource().getLabel()); 
								}							
							}
							
						}
					}
				}				
			}		
		}

		return resultTIOSTS;
	}
	
	Transition recoverTransition(Collection<Transition> transitions1, String aSync){
		
		Transition transitionSync = null;				
		
		for(Transition t:transitions1){
			if(t.getAction().getName().equals(aSync)){
				transitionSync = t;
			}
		}
		
		return transitionSync;
	}
	

}