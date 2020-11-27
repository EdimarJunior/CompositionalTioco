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
 * Wilkerson de Lucena Andrade      16/06/2011     An old class named TIOSTSPreprocessing (Version 1.2) was divided into two classes. This class only contains the synchronous algorithm algorithm (Version 1.0).
 * 
 */

package br.edu.ufcg.symbolrt.algorithms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import br.edu.ufcg.symbolrt.base.Action;
import br.edu.ufcg.symbolrt.base.ClockGuard;
import br.edu.ufcg.symbolrt.base.Location;
import br.edu.ufcg.symbolrt.base.SimpleClockGuard;
import br.edu.ufcg.symbolrt.base.TIOSTS;
import br.edu.ufcg.symbolrt.base.Transition;
import br.edu.ufcg.symbolrt.base.TypedData;
import br.edu.ufcg.symbolrt.exception.IncompatibleSynchronousProductException;
import br.edu.ufcg.symbolrt.util.Constants;


/**
 * <code>SynchronousProduct</code> Class. <br>
 * This class contains an algorithm needed to process TIOSTSs before test case generation. The implemented algorithm here is used for computing
 * the synchronous product between test purposes and specifications. 
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
public class SynchronousProduct {
	
	private static SynchronousProduct instance = null;


	private SynchronousProduct() {
	}

	
	/**
	 * Method used to obtain an instance of this class.
	 * @return An instance of {@link SynchronousProduct}.
	 */
	public static SynchronousProduct getInstance() {
		if (instance == null) {
			instance = new SynchronousProduct();
		}
		return instance;
	}
	
	
	/**
	 * Returns the synchronous product between a specification and a test purpose.
	 * @param spec The specification of the system under test.
	 * @param tp The test purpose indicating a specific scenario to be tested.
	 * @return The synchronous product between spec and tp.
	 * @throws IncompatibleSynchronousProductException If the parameters are not compatible for the synchronous product operation.  
	 */
	public TIOSTS synchronousProduct(TIOSTS spec, TIOSTS tp) throws IncompatibleSynchronousProductException {
		TIOSTS sp = initializeSyncProduct(spec, tp);
		syncProduct(spec.getInitialLocation(), tp.getInitialLocation(), sp);
		
		// Sets the initial location and initial condition
		Location initialLocation = sp.getLocation(spec.getInitialLocation().getLabel() + "_" + tp.getInitialLocation().getLabel());
		sp.setInitialLocation(initialLocation);
		// For the initial location there is only one outgoing transition and no clock guard.
		sp.setInitialCondition(initialLocation.getOutTransitions().get(0).getDataGuard());
				
		return mirror(sp);
	}
	
	
	/**
	 * Computes the synchronous product from the initial location of the specification and the initial location of the test purpose,
	 * storing the result in TIOSTS sp.
	 * @param specLoc The initial location of the specification.
	 * @param tpLoc The initial location of the test purpose.
	 * @param sp The resulting synchronous product.
	 */
	private void syncProduct(Location specLoc, Location tpLoc, TIOSTS sp){
		if (tpLoc.isSpecialLocation()) return;
		
		for (Transition specTransition : specLoc.getOutTransitions()) {
			Collection<Transition> tpTransitions = getTransitionsByAction(tpLoc, specTransition.getAction());
			
			Action spAction = sp.getAction(specTransition.getAction().getType(), specTransition.getAction().getName());
			
			Location source = new Location(specLoc.getLabel() + "_" + tpLoc.getLabel());
			sp.addLocation(source);
			
			if (tpTransitions.size() == 0) {
				Location target = new Location(specTransition.getTarget().getLabel() + "_" + tpLoc.getLabel());
				sp.addLocation(target);
				if (sp.getTransition(source.getLabel(), specTransition.getDataGuard(), specTransition.getClockGuard(), spAction, target.getLabel()) == null) {
					sp.createTransition(source.getLabel(), specTransition.getDataGuard(), specTransition.getClockGuard().toString(), spAction, specTransition.getDataAssignments(), specTransition.getClockAssignments(), specTransition.getDeadline(), target.getLabel());
					syncProduct(specTransition.getTarget(), tpLoc, sp);
				}
			} else {
				for (Transition tpTransition : tpTransitions) {
					Location target = new Location(specTransition.getTarget().getLabel() + "_" + tpTransition.getTarget().getLabel());
					sp.addLocation(target);
					String dataGuard = createDataGuard(specTransition.getDataGuard(), tpTransition.getDataGuard());
					ClockGuard clockGuard = createClockGuard(specTransition.getClockGuard(), tpTransition.getClockGuard());
					if (sp.getTransition(source.getLabel(), dataGuard, clockGuard, spAction, target.getLabel()) == null){
						String dataAssignments = createAssignments(specTransition.getDataAssignments(), tpTransition.getDataAssignments());
						String clockAssignments = createAssignments(specTransition.getClockAssignments(), tpTransition.getClockAssignments());
						sp.createTransition(source.getLabel(), dataGuard, clockGuard, spAction, dataAssignments, clockAssignments, specTransition.getDeadline(), target.getLabel());
						syncProduct(specTransition.getTarget(), tpTransition.getTarget(), sp);
					}
				}
			}
			
		}
	}
	
	
	/**
	 * Initializes the TIOSTS model used to represent the synchronous product. This method defines the name of the resulting model, adds
	 * variables, parameters, clocks, and actions to the model. Moreover, this method is responsible for verifying if the specification 
	 * and test purpose are compatible for synchronous product.  
	 * @param spec The specification of the systems under test.
	 * @param tp The test purpose.
	 * @return The TIOSTS containing variables, parameters, clocks, and actions. The transitions are computed in {@link SynchronousProduct#syncProduct(Location, Location, TIOSTS)}. 
	 * @throws IncompatibleSynchronousProductException If specification and test purpose are not compatible for the synchronous product operation.
	 */
	private TIOSTS initializeSyncProduct(TIOSTS spec, TIOSTS tp) throws IncompatibleSynchronousProductException{
		TIOSTS sp = new TIOSTS(spec.getName() + "_X_" + tp.getName());
		
		// Adds the variables of specification to the synchronous product
		for (TypedData variable: spec.getVariables()) {
			sp.addVariable(variable.clone());
		}
		
		// Verifies if the variables of specification are different from the variables of test purpose
		List<TypedData> spVariables = sp.getVariables();
		for (TypedData tpVariable: tp.getVariables()) {
			if (!spVariables.contains(tpVariable)) {
				sp.addVariable(tpVariable);
			} else {
				throw new IncompatibleSynchronousProductException("The variables of specification must be different from variables of test purpose.");
			}
		}
		
		// Adds parameters of specification to the synchronous product
		for (TypedData parameter: spec.getActionParameters()){
			sp.addActionParameter(parameter.clone());
		}
		
		// Verifies if the parameters of test purpose are the same as specification or if they are variables of specification
		List<TypedData> spParameters = sp.getActionParameters();
		for (TypedData tpParameter: tp.getActionParameters()) {
			if (!spParameters.contains(tpParameter) && !spVariables.contains(tpParameter)) {
				throw new IncompatibleSynchronousProductException("The parameters of test purpose must be the same as specification or variables of specification.");
			}
		}
		
		// Adds the clocks of specification to the synchronous product
		for (String clock: spec.getClocks()) {
			sp.addClock(clock);
		}
		
		// Verifies if the clocks of specification are different from the clocks of test purpose
		List<String> spClocks = sp.getClocks();
		for (String tpClock: tp.getClocks()) {
			if (!spClocks.contains(tpClock)) {
				sp.addClock(tpClock);
			} else {
				throw new IncompatibleSynchronousProductException("The clocks of specification must be different from clocks of test purpose.");
			}
		}
		
		// Adds the input actions of specification to the synchronous product
		for (Action inputAction: spec.getInputActions()) {
			sp.addInputAction(inputAction.clone());
		}
		
		// Adds the output actions of specification to the synchronous product
		for (Action outputAction: spec.getOutputActions()) {
			sp.addOutputAction(outputAction.clone());
		}
		
		// Adds the internal actions of specification to the synchronous product
		for (Action internalAction: spec.getInternalActions()) {
			sp.addInternalAction(internalAction.clone());
		}
		
		return sp;
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
	
	
	/**
	 * Returns a collection of transitions given the location and an action if there is a transition with the specified action.
	 * @param loc The location to be evaluated.
	 * @param action The action to be searched.
	 * @return The collection of transitions with the specified action. If there are no transitions an empty collection is returned.
	 */
	private Collection<Transition> getTransitionsByAction(Location loc, Action action){
		Collection<Transition> result = new ArrayList<Transition>();
		for (Transition t : loc.getOutTransitions()) {
			if (t.getAction().equals(action)) result.add(t);
		}
		return result;
	}

	
	/**
	 * Returns the TIOSTS model with input actions changed to output actions and vice-versa.
	 * @param tiosts The TIOSTS model to be processed. 
	 * @return The mirror of the tiosts.
	 */
	private TIOSTS mirror(TIOSTS tiosts){
		List<Action> inputActions = tiosts.getInputActions();
		List<Action> outputActions = tiosts.getOutputActions();
		
		for (Action action : inputActions) {
			action.setType(Constants.ACTION_OUTPUT);
		}
		tiosts.setOutputActions(inputActions);
		
		for (Action action : outputActions) {
			action.setType(Constants.ACTION_INPUT);
		}
		tiosts.setInputActions(outputActions);
		
		return tiosts;
	}


}
