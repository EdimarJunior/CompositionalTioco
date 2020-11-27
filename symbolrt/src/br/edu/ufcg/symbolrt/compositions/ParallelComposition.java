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
 * This class contains an algorithm needed to perform the parallel composition between two TIOSTSs.
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
public class ParallelComposition {
	
	private static ParallelComposition instance = null;
	private List<String> visitedLocations;

	private ParallelComposition() {
		visitedLocations = new LinkedList<String>();
	}

	/**
	 * Method used to obtain an instance of this class.
	 * @return An instance of {@link ParallelComposition}.
	 */
	public static ParallelComposition getInstance() {
		if (instance == null) {
			instance = new ParallelComposition();
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
	public TIOSTS parallelComposition(TIOSTS model1, TIOSTS model2) throws IncompatibleCompositionalOperationException {

		TIOSTS result;
		TIOSTS tiosts1 = model1.clone();
		TIOSTS tiosts2 = model2.clone();

		//Verifying if the set of clocks, variables and locations are disjoint

		if (!isCompatible(tiosts1, tiosts2)){
			throw new IncompatibleCompositionalOperationException("The set of variables, locations and clocks from both TIOSTSs must be disjoint");
		}//Conditions are satisfied, so we can build the resulting TIOSTS from the composition of t1 and t2
		else{
			result = initialize(tiosts1, tiosts2);
			
			List<String> actionst1Minust2 = minusSet(model1.getActionNames(), model2.getActionNames());			
			List<String> actionst2Minust1 = minusSet(model2.getActionNames(), model1.getActionNames());
			
			//Removing Start nodes of both TIOSTSs			
			Location initialNodeTiosts1 = tiosts1.getInitialLocation().getOutTransitions().get(0).getTarget();
			Location initialNodeTiosts2 = tiosts2.getInitialLocation().getOutTransitions().get(0).getTarget();
			
			//Creating the resulting transitions			
			parComposition(initialNodeTiosts1, initialNodeTiosts2, result, actionst1Minust2, actionst2Minust1);
			
			//Updating the initial node reference of the parallel result 
			Location initialResultLocation = new Location(initialNodeTiosts1.getLabel() + "," + initialNodeTiosts2.getLabel());

		}

		return result;
	}	

	private void parComposition(Location loc1, Location loc2, TIOSTS pc, List<String> actionsT1, List<String> actionsT2){
		
		if (isVisited(loc1,loc2)) return;

		//Building the locations and transitions of t1 and t2	

		for (Transition t1 : loc1.getOutTransitions()) {
			for (Transition t2 : loc2.getOutTransitions()){
				Action action1 = t1.getAction();
				Action action2 = t2.getAction();
				Location source;
				Location target;
				Action newAction = null;
			
				//Case 1 (t1 do not synchronize)
				if (actionsT1.contains(action1.getName())){
					source = new Location(loc1.getLabel() + "," + loc2.getLabel());
					pc.addLocation(source);
					target = new Location(t1.getTarget().getLabel() + "," + loc2.getLabel());
					pc.addLocation(target);
					newAction = pc.recoverAction(t1.getAction());
					pc.createTransition(source.getLabel(), t1.getDataGuard(), t1.getClockGuard(), newAction, t1.getDataAssignments(), t1.getClockAssignments(), t1.getDeadline(), target.getLabel());
					parComposition(t1.getTarget(), loc2, pc, actionsT1, actionsT2);
				}
				//Case 2 (t2 do not synchronize)
				if (actionsT2.contains(action2.getName())){
					source = new Location(loc1.getLabel() + "," + loc2.getLabel());
					pc.addLocation(source);
					target = new Location(loc1.getLabel() + "," + t2.getTarget().getLabel());
					pc.addLocation(target);
					newAction = pc.recoverAction(t2.getAction());
					pc.createTransition(source.getLabel(), t2.getDataGuard(), t2.getClockGuard(), newAction, t2.getDataAssignments(), t2.getClockAssignments(), t2.getDeadline(), target.getLabel());
					parComposition(loc1, t2.getTarget(), pc, actionsT1, actionsT2);
				}
				if (canSynchronize(action1, action2)){
					//Case 3 (both synchronize)	
					source = new Location(loc1.getLabel() + "," + loc2.getLabel());
					pc.addLocation(source);
					target = new Location(t1.getTarget().getLabel()+ "," + t2.getTarget().getLabel());
					pc.addLocation(target);
					String dataAssignments = createAssignments(t1.getDataAssignments(), t2.getDataAssignments());
					String clockAssignments = createAssignments(t1.getClockAssignments(), t2.getClockAssignments());
					String dataGuard = createDataGuard(t1.getDataGuard(), t2.getDataGuard());
					ClockGuard clockGuard = createClockGuard(t1.getClockGuard(), t2.getClockGuard());
					List<String> parameters = action2.getParameters();
					//Recovering the output action from t1 or t2
					if (t1.getAction().getType() == Constants.ACTION_OUTPUT){
						newAction = pc.recoverAction(t1.getAction());				
					}
					if (t2.getAction().getType() == Constants.ACTION_OUTPUT){
						newAction = pc.recoverAction(t2.getAction());				
					}
					newAction.setParameters(parameters);
					pc.createTransition(source.getLabel(), dataGuard, clockGuard, newAction, dataAssignments, clockAssignments, deadlineOperator(t1.getDeadline(), t2.getDeadline()), target.getLabel());
					parComposition(t1.getTarget(), t2.getTarget(), pc, actionsT1, actionsT2);
				}
			}
		}		
	}	
	
	private boolean canSynchronize(Action a1, Action a2){
		boolean resp = false;
		String name1 = a1.getName();
		if (a1.getType() == Constants.ACTION_INPUT && a2.getType() == Constants.ACTION_OUTPUT && name1.equals(a2.getName())){
			resp = true;
		}
		if (a2.getType() == Constants.ACTION_INPUT && a1.getType() == Constants.ACTION_OUTPUT && name1.equals(a2.getName())){
			resp = true;
		}
		return resp;		
	}
	//updateLocation(result, initialResultLocation);
	private List<String> minusSet(List<String> set1, List<String> set2){
		List<String> resultSet = new LinkedList<String>();
		
		for(String s:set1){
			if(!set2.contains(s)){
				resultSet.add(s);								
			}
		}
		return resultSet;		
	}
	
	private boolean isCompatible(TIOSTS tiosts1, TIOSTS tiosts2){
		//boolean resp = isIntersection(tiosts1.getLocationNames(), tiosts2.getLocationNames()) == false && isIntersection(tiosts1.getClocks(), tiosts2.getClocks()) == false && isIntersection(tiosts1.getVariableNames(), tiosts2.getVariableNames()) == false;
		boolean resp = isIntersection(tiosts1.getLocationNames(), tiosts2.getLocationNames()) == false && isIntersection(tiosts1.getVariableNames(), tiosts2.getVariableNames()) == false;
		return resp;
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
		// TODO Refatorar depois. Deve ir para a classe ClockGuard
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
	
	private TIOSTS initialize(TIOSTS tiosts1, TIOSTS tiosts2){
		TIOSTS tiostsResult = new TIOSTS(tiosts1.getName() + "x" +tiosts2.getName());

		//Performing union between internal actions from t1 and t2, excluding their init nodes				
		Transition initTransitionT1 = tiosts1.getInitialLocation().getOutTransitions().get(0);
		Transition initTransitionT2 = tiosts2.getInitialLocation().getOutTransitions().get(0);
			
		for(Action internal:tiosts1.getInternalActions()){
			String nameAction = internal.getName();
			if(!nameAction.equals(initTransitionT1.getAction().getName())){
				tiostsResult.addInternalAction(internal);				
			}
		}
		
		for(Action internal:tiosts2.getInternalActions()){
			String nameAction = internal.getName();
			if(!nameAction.equals(initTransitionT2.getAction().getName())){
				tiostsResult.addInternalAction(internal);				
			}
		}
		
		//Performing union between output actions from t1 and t2
		List<Action> outputActions = tiosts1.getOutputActions();
		for(Action outputActionT2:tiosts2.getOutputActions()){ 
			if (!outputActions.contains(outputActionT2)){
				outputActions.add(outputActionT2);
			}
		}
		tiostsResult.setOutputActions(outputActions);
		
		//Performing union between variables and clocks from t1 and t2
		List<TypedData> variables = tiosts1.getVariables();
		variables.addAll(tiosts2.getVariables());
		tiostsResult.setVariables(variables);

		List<String> clocks = tiosts1.getClocks();
		// TODO Adjust the set of clocks according to the parser.
		//clocks.addAll(tiosts2.getClocks());
		tiostsResult.setClocks(clocks);
		
		//Performing union between parameters
		List<TypedData> actionParameters = tiosts1.getActionParameters();
		for(TypedData parameterT2:tiosts2.getActionParameters()){ 
			if (!actionParameters.contains(parameterT2)){
				actionParameters.add(parameterT2);
			}
		}
		tiostsResult.setActionParameters(actionParameters);
		
		//Performing union of input actions from t1 and t2 (excluding output actions of TIOSTS t1 and t2)
		
		List<String> outputActionLabelsT1 = tiosts1.getOutputActionNames();
		List<String> outputActionLabelsT2 = tiosts2.getOutputActionNames();
		
		for(Action input:tiosts1.getInputActions()){
			String nameAction = input.getName();
			if(!outputActionLabelsT2.contains(nameAction)){
				tiostsResult.addInputAction(input);						
			}
		}	
		for(Action input:tiosts2.getInputActions()){
			String nameAction = input.getName();
			if(!outputActionLabelsT1.contains(nameAction)){
				tiostsResult.addInputAction(input);						
			}
		}
		
		//Generating the initial transition		
		Location sourceLocation = new Location(initTransitionT1.getSource() +","+ initTransitionT2.getSource());
		tiostsResult.addLocation(sourceLocation);
		Location targetLocation = new Location(initTransitionT1.getTarget() +","+ initTransitionT2.getTarget());
		tiostsResult.addLocation(targetLocation);
		String dataGuard = createDataGuard(initTransitionT1.getDataGuard(), initTransitionT2.getDataGuard());
		ClockGuard clockGuard = createClockGuard(initTransitionT1.getClockGuard(), initTransitionT2.getClockGuard());
		Action action = new Action(initTransitionT1.getAction().getName()+"_"+initTransitionT2.getAction().getName(), Constants.ACTION_INTERNAL);
		tiostsResult.addInternalAction(action);
		String dataAssignments = createAssignments(initTransitionT1.getDataAssignments(), initTransitionT1.getDataAssignments());
		String clockAssignments = createAssignments(initTransitionT1.getClockAssignments(), initTransitionT1.getClockAssignments());		
		tiostsResult.createTransition(sourceLocation.getLabel(), dataGuard, clockGuard, action, dataAssignments, clockAssignments, targetLocation.getLabel());
		
		//Setting the initial condition		
		tiostsResult.setInitialCondition(tiosts1.getInitialCondition() + Constants.GUARD_CONJUNCTION + tiosts2.getInitialCondition());
		tiostsResult.setInitialLocation(sourceLocation);
		sourceLocation.setIsInitialLocation(true);
		
		return tiostsResult;		
	}
	
	private boolean isVisited(Location l1, Location l2){
		boolean visited = false;
		if(visitedLocations.contains(l1.getLabel() +", "+l2.getLabel())){
			visited = true;			
		}else{
			visitedLocations.add(l1.getLabel() +", "+l2.getLabel());
		}
		return visited;
	}
	
	private boolean isIntersection(List<String> colecao1, List<String> colecao2){
		boolean isIntersection = false;
		
		for (String c:colecao1){
			if (colecao2.contains(c)){
				isIntersection = true;
			}			
		}		
		
		return isIntersection;
	}
	
	private String deadlineOperator(String deadline1, String deadLine2){
		String result; 
		if ((deadline1 == Constants.DEADLINE_EAGER) || (deadLine2 == Constants.DEADLINE_EAGER)){
			result = Constants.DEADLINE_EAGER;
			
		}else{
			if ((deadline1 == Constants.DEADLINE_DELAYABLE || deadLine2 ==  Constants.DEADLINE_DELAYABLE)){
				result = Constants.DEADLINE_DELAYABLE;
			}else{
				result = Constants.DEADLINE_LAZY;
			}
			
		}
		
		return result;		
	}
	
}