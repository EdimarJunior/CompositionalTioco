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
 * Wilkerson de Lucena Andrade      01/08/2010     Initial version
 * Wilkerson de Lucena Andrade      03/01/2011     Guards divided into data and clock guards
 * Wilkerson de Lucena Andrade      04/01/2011     Assignments divided into data and clock assignments
 * Wilkerson de Lucena Andrade      25/06/2011     ActionParameter class introduced
 * 
 */
package br.edu.ufcg.symbolrt.base;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import br.edu.ufcg.symbolrt.util.Constants;


/**
 * <code>TIOSTS</code> Class. <br>
 * 
 * This class represents a Timed Input-Output Symbolic Transition System (TIOSTS).
 * 
 * Formally, a TIOSTS is a tuple &lang; V, P, &Theta;, L, l<sup>0</sup>, &Sigma;, C, T &rang;, 
 * where: 1) V is a countable set of typed variables; 2) P is a countable set of actionParameters; 
 * 3) &Theta; is the initial condition, a predicate with variables in V; 4) L is a countable,
 * non-empty set of locations; 5) l<sup>0</sup> &isin; L is the initial location; 6) &Sigma; = &Sigma;<sup>?</sup>
 * &cup; &Sigma;<sup>!</sup> &cup; &Sigma;<sup>&tau;</sup> is a countable, non-empty alphabet, where 
 * &Sigma;<sup>?</sup> is a countable set of input actions, &Sigma;<sup>!</sup> is a countable set of 
 * output actions, and &Sigma;<sup>&tau;</sup> is a countable set of internal actions; 7) C is a countable 
 * set of clocks; 8) T is a countable set of transitions.
 *  
 * @author Wilkerson de Lucena Andrade  ( <a href="mailto:wilkerson@computacao.ufcg.edu.br">wilkerson@computacao.ufcg.edu.br</a> )
 * @author Adriana Carla Damasceno  ( <a href="mailto:adriana@copin.ufcg.edu.br">adriana@copin.ufcg.edu.br</a> )
 * 
 * @version 1.1
 * <br>
 * SYmbolic Model-Based test case generation toOL for Real-Time systems (SYMBOLRT)
 * <br>
 * (C) Copyright 2010-2013 Federal University of Campina Grande (UFCG)
 * <br>
 * <a href="https://sites.google.com/a/computacao.ufcg.edu.br/symbolrt">https://sites.google.com/a/computacao.ufcg.edu.br/symbolrt</a>
 */
public class TIOSTS {
	
	
	// **************************** Attributes ****************************
	
	private String name;						// The name of the IOSTS model.
	
	private String initialCondition;			// &Theta; - The initial condition.
	
	private Location initialLocation;			// l<sup>0</sup> - The initial location.
	
	private List<TypedData> variables;			// V - The countable set of typed variables.
	
	private List<TypedData> actionParameters;	// P - The countable set of action actionParameters.
	
	private List<String> clocks;				// C - The countable set of clocks.
	
	private List<Action> inputActions;			// &Sigma;<sup>?</sup> - The countable set of input actions.
	
	private List<Action> outputActions;			// &Sigma;<sup>!</sup> - The countable set of output actions.
	
	private List<Action> internalActions;		// &Sigma;<sup>&tau;</sup> - The countable set of internal actions.
	
	private Map<String,Location> locations;		// L - The countable, non-empty set of locations.
	
	private Map<String,Transition> transitions;	// T - The countable set of transitions.

	
	
	
	// **************************** Constructors ****************************	
	
	/**
	 * Creates a new TIOSTS with the specified information.
	 * @param name The name of the TIOSTS.
	 */
	public TIOSTS(String name){
		this.name = name;
		this.initialCondition = null;
		this.initialLocation = null;
		this.variables = new ArrayList<TypedData>();
		this.actionParameters = new ArrayList<TypedData>();
		this.clocks = new ArrayList<String>();
		this.inputActions = new ArrayList<Action>();
		this.outputActions = new ArrayList<Action>();
		this.internalActions = new ArrayList<Action>();
		this.locations = new HashMap<String, Location>();
		this.transitions = new HashMap<String, Transition>();
	}
	
	// **************************** Methods ****************************	

	/**
	 * Returns the name of this TIOSTS.
	 * @return The name of this TIOSTS.
	 */
	public String getName() {
		return name;
	}


	/**
	 * Modifies the name of this TIOSTS.
	 * @param name The new name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}


	/**
	 * Returns the initial location of the TIOSTS.
	 * @return The initial location of the TIOSTS.
	 */
	public Location getInitialLocation() {
		return initialLocation;
	}


	/**
	 * Modifies the initial location of the TIOSTS.
	 * @param initialLocation The new initial location to set.
	 */
	public void setInitialLocation(Location initialLocation) {
		this.initialLocation = initialLocation;
		this.initialLocation.setIsInitialLocation(true);
	}


	/**
	 * Returns the initial condition of the TIOSTS.
	 * @return The initial condition of the TIOSTS.
	 */
	public String getInitialCondition() {
		return initialCondition;
	}

	
	/**
	 * Modifies the initial condition of the TIOSTS.
	 * @param initialCondition The new initial condition to set.
	 */
	public void setInitialCondition(String initialCondition) {
		this.initialCondition = initialCondition;
	}
	
	
	/**
	 * Returns the variables of the TIOSTS.
	 * @return The variables of the TIOSTS.
	 */
	public List<TypedData> getVariables() {
		return variables;
	}
	
	/**
	 * Returns the variable names of the TIOSTS.
	 * @return The variable names of the TIOSTS.
	 * @author Adriana Carla Damasceno  ( <a href="mailto:adriana@copin.ufcg.edu.br">adriana@copin.ufcg.edu.br</a> )
	 */
	public List<String> getVariableNames() {
		List<String> variableNames = new LinkedList<String>();
		for(TypedData variableName:this.variables){
			variableNames.add(variableName.getName());
		}
		return variableNames;
	}

	
	/**
	 * Adds a new variable to the TIOSTS.
	 * @param variable The new variable to be added.
	 */
	public void addVariable(TypedData variable) {
		this.variables.add(variable);
	}
	
	
	/**
	 * Returns the variable with the specified name. 
	 * @param name The name of the variable to be searched.
	 * @return The variable with the specified name. If there is no variable with the specified name null is returned.
	 */
	public TypedData getVariable(String name) {
		for (TypedData variable : this.variables) {
			if (variable.getName().equals(name)) {
				return variable;
			}
		}
		return null;
	}


	/**
	 * Returns the action parameters of the TIOSTS.
	 * @return The action parameters of the TIOSTS.
	 */
	public List<TypedData> getActionParameters() {
		return actionParameters;
	}

	/**
	 * Returns the action names of the TIOSTS.
	 * @return The action names of the TIOSTS.
	 * @author Adriana Carla Damasceno  ( <a href="mailto:adriana@copin.ufcg.edu.br">adriana@copin.ufcg.edu.br</a> )
	 */
	public List<String> getActionNames() {
		List<String> actionNames = new LinkedList<String>();
		actionNames.addAll(this.getInputActionNames());
		for(Action a:this.getInternalActions()){
			if(!actionNames.contains(a.getName())){
				actionNames.add(a.getName());
			}
		}
		for(String a:this.getOutputActionNames()){
			if(!actionNames.contains(a)){
				actionNames.add(a);
			}
		}
		return actionNames;
	}
	
	/**
	 * Returns the actions of the TIOSTS.
	 * @return The actions of the TIOSTS.
	 * @author Adriana Carla Damasceno  ( <a href="mailto:adriana@copin.ufcg.edu.br">adriana@copin.ufcg.edu.br</a> )
	 */
	public List<Action> getActions() {
		List<Action> actions = new LinkedList<Action>();
		actions.addAll(this.getInputActions());
		actions.addAll(this.getOutputActions());
		actions.addAll(this.getInternalActions());
		
		return actions;
	}

	/**
	 * Adds a new action parameter to the TIOSTS.
	 * @param actionParameter The new action parameter to be added.
	 */
	public void addActionParameter(TypedData actionParameter) {
		this.actionParameters.add(actionParameter);
	}
	
	
	/**
	 * Returns the action parameter with the specified name. 
	 * @param name The name of the action parameter to be searched.
	 * @return The action parameter with the specified name. If there is no action parameter with the specified name null is returned.
	 */
	public TypedData getActionParameter(String name) {
		for (TypedData actionParameter : this.actionParameters) {
			if (actionParameter.getName().equals(name)) {
				return actionParameter;
			}
		}
		return null;
	}
	
	/**
	 * Returns the clocks of the TIOSTS.
	 * @return The clocks of the TIOSTS.
	 */
	public List<String> getClocks() {
		return clocks;
	}
	

	/**
	 * Adds a new clock to the TIOSTS.
	 * @param clock The new clock to be added.
	 */
	public void addClock(String clock) {
		this.clocks.add(clock);
	}
	
	/**
	 * Returns an integer representing the position of the clock in the list of clocks. This number is used for identifying clocks in the
	 * DBM representation. 
	 * @param clockName The clock to be searched.
	 * @return An integer representing the position of the clock in the list of clocks. For example: 1 is returned for the first clock, 2 is 
	 * returned for the second clock, and so on. If there is no clock with the specified name 0 is returned. 
	 */
	public int getClockId(String clockName) {
		/*int result = 0;
		
		int indice = 0;
		for (String clock : allClocks) {
			if (clock.equals(clockName)){
				result += ++indice;
				break;
			} else {
				indice += 1;
			}
		}*/
		
		return this.clocks.indexOf(clockName) + 1;
	}
	
	/**
	 * Returns the input actions of the TIOSTS.
	 * @return The input actions of the TIOSTS.
	 */
	public List<Action> getInputActions() {
		return inputActions;
	}
	
	/**
	 * Returns the input action names of the TIOSTS.
	 * @return The input action names of the TIOSTS.
	 * @author Adriana Carla Damasceno  ( <a href="mailto:adriana@copin.ufcg.edu.br">adriana@copin.ufcg.edu.br</a> )
	 */
	public List<String> getInputActionNames() {
		List<String> inputActionNames = new LinkedList<String>();
		for(Action action:this.inputActions){
			inputActionNames.add(action.getName());
		}
		return inputActionNames;
	}

	
	/**
	 * Returns the input action with the specified name.
	 * @param name The name of the action to be returned.
	 * @return The action with the specified name or null if there is no one. 
	 */
	public Action getInputAction(String name) {
		for (Action action : this.inputActions) {
			if (action.getName().equals(name)) return action;
		}
		return null;
	}
	
	/**
	 * Recovers the reference object of an action.
	 * @param name The name of the action to be returned.
	 * @return The action with the specified name or null if there is no one. 
	 * @author Adriana Carla Damasceno  ( <a href="mailto:adriana@copin.ufcg.edu.br">adriana@copin.ufcg.edu.br</a> )
	 */
	
	public Action recoverAction(Action action){
		Action newAction = null;
		List<Action> actions = null;
		if (action.getType() == Constants.ACTION_INPUT) {
			actions = this.getInputActions();
			newAction = actions.get(actions.indexOf(action));
		} else if (action.getType() == Constants.ACTION_OUTPUT) {
			actions = this.getOutputActions();
			newAction = actions.get(actions.indexOf(action));				
		} else {
			actions = this.getInternalActions();
			newAction = actions.get(actions.indexOf(action));		
		}
		return newAction;
	}
	
	/**
	 * Adds a new input action to the TIOSTS.
	 * @param action The new action to be added.
	 */
	public void addInputAction(Action action) {
		this.inputActions.add(action);
	}
	
	
	/**
	 * Modifies the list of input actions of the TIOSTS.
	 * @param newActions The new list to set.
	 */
	public void setInputActions(List<Action> newActions) {
		this.inputActions = newActions;
	}
	
	
	/**
	 * Returns the output actions of the TIOSTS.
	 * @return The output actions of the TIOSTS.
	 */
	public List<Action> getOutputActions() {
		return outputActions;
	}
	
	
	/**
	 * Returns the output action with the specified name.
	 * @param name The name of the action to be returned.
	 * @return The action with the specified name or null if there is no one. 
	 */
	public Action getOutputAction(String name) {
		for (Action action : this.outputActions) {
			if (action.getName().equals(name)) return action;
		}
		return null;
	}


	/**
	 * Adds a new output action to the TIOSTS.
	 * @param action The new action to be added.
	 */
	public void addOutputAction(Action action) {
		this.outputActions.add(action);
	}
	
	
	/**
	 * Modifies the list of output actions of the TIOSTS.
	 * @param newActions The new list to set.
	 */
	public void setOutputActions(List<Action> newActions) {
		this.outputActions = newActions;
	}
	
	/**
	 * Modifies the variables of the TIOSTS.
	 * @param variables  The variables of the TIOSTS.
	 */
	public void setVariables(List<TypedData> variables) {
		this.variables = variables;
	}
	
	/**
	 * Modifies the clocks of the TIOSTS.
	 * @param The clocks of the TIOSTS.
	 * @author Adriana Carla Damasceno  ( <a href="mailto:adriana@copin.ufcg.edu.br">adriana@copin.ufcg.edu.br</a> )
	 */
	public void setClocks(List<String> clocks) {
		this.clocks = clocks;
	}
	
	/**
	 * Modifies the action parameters of the TIOSTS.
	 * @param The action parameters of the TIOSTS.
	 * @author Adriana Carla Damasceno  ( <a href="mailto:adriana@copin.ufcg.edu.br">adriana@copin.ufcg.edu.br</a> )
	 */
	public void setActionParameters(List<TypedData> actionParameters) {
		this.actionParameters = actionParameters;
	}
	
	/**
	 * Returns the internal actions of the TIOSTS.
	 * @return The internal actions of the TIOSTS.
	 */
	public List<Action> getInternalActions() {
		return internalActions;
	}

	
	/**
	 * Returns the internal action with the specified name.
	 * @param name The name of the action to be returned.
	 * @return The action with the specified name or null if there is no one. 
	 */
	public Action getInternalAction(String name) {
		for (Action action : this.internalActions) {
			if (action.getName().equals(name)) return action;
		}
		return null;
	}
	
	
	/**
	 * Adds a new internal action to the TIOSTS.
	 * @param action The new action to be added.
	 */
	public void addInternalAction(Action action) {
		this.internalActions.add(action);
	}
	
	
	/**
	 * Modifies the list of internal actions of the TIOSTS.
	 * @param newActions The new list to set.
	 */
	public void setInternalActions(List<Action> newActions) {
		this.internalActions = newActions;
	}


	/**
	 * Returns a collection of locations of the TIOSTS.
	 * @return The locations of the TIOSTS.
	 */
	public Collection<Location> getLocations() {
		return locations.values();
	}
	
	/**
	 * Returns the location names of the TIOSTS.
	 * @return The location names of the TIOSTS.
	 * @author Adriana Carla Damasceno  ( <a href="mailto:adriana@copin.ufcg.edu.br">adriana@copin.ufcg.edu.br</a> )
	 */
	public List<String> getLocationNames() {
		List<String> locationNames = new LinkedList<String>();
		for(Location locationName:this.getLocations()){
			locationNames.add(locationName.getLabel());
		}
		return locationNames;
	}
	
	
	/**
	 * Returns the location associated to the indicated label, or null if this model contains no location with this label.
	 * @param label The label of the location to be returned.
	 * @return The location with the indicated label, or null if this model contains no location with this label.
	 */
	public Location getLocation(String label) {
		return this.locations.get(label);
	}


	/**
	 * Adds a new location to the TIOSTS.
	 * @param location The new location to be added.
	 * @return True if the location was successfully added, and 
	 * returns False if there is already a location with the same label. 
	 */
	public boolean addLocation(Location location) {
		if (locations.containsKey(location.getLabel())) {
			return false;
		}
		locations.put(location.getLabel(), location);
		return true;
	}
	
	
	/**
	 * Returns a collection of transitions of the TIOSTS.
	 * @return The transitions of the TIOSTS.
	 */
	public Collection<Transition> getTransitions() {
		return transitions.values();
	}
	
	/**
	 * Returns all input and output actions of the TIOSTS.
	 * @return All input and output actions of the TIOSTS.
	 */
	public List<Action> getInputOutputActions(){
		List<Action> result = new ArrayList<Action>();
		result.addAll(this.inputActions);
		result.addAll(this.outputActions);
		return result;
	}
	
	/**
	 * Returns the output action names of the TIOSTS.
	 * @return The output action names of the TIOSTS.
	 * @author Adriana Carla Damasceno  ( <a href="mailto:adriana@copin.ufcg.edu.br">adriana@copin.ufcg.edu.br</a> )
	 */
	public List<String> getOutputActionNames() {
		List<String> outputActionNames = new LinkedList<String>();
		for(Action action:this.outputActions){
			outputActionNames.add(action.getName());
		}
		return outputActionNames;
	}
	
	/**
	 * Add the specified action to this TIOSTS. 
	 * @param action The action to be added. 
	 * @return true if the action was added and false otherwise.
	 */
	public boolean addAction(Action action) {
		switch (action.getType()) {
		case Constants.ACTION_INPUT:
			this.inputActions.add(action);
			return true;
		case Constants.ACTION_OUTPUT:
			this.outputActions.add(action);
			return true;
		case Constants.ACTION_INTERNAL:
			this.internalActions.add(action);
			return true;
		default:
			return false;
		}
	}
	
	
	/**
	 * Returns the action with the specified name and type. 
	 * @param type The type of the action. The possible values are defined by the following constants: (1) for input action use 
	 * {@link Constants#ACTION_INPUT}; (2) for output action use {@link Constants#ACTION_OUTPUT}; (3) for internal action use
	 * {@link Constants#ACTION_INTERNAL}.
	 * @param name The name of the action.
	 * @return The action with the specified name and type or null if there is no one.
	 */
	public Action getAction(int type, String name) {
		switch (type) {
		case Constants.ACTION_INPUT:
			return getInputAction(name);
		case Constants.ACTION_OUTPUT:
			return getOutputAction(name);
		case Constants.ACTION_INTERNAL:
			return getInternalAction(name);
		default:
			return null;
		}
		
	}
	
	
	/**
	 * Returns the transition with the following actionParameters if one exists, and returns null otherwise.
	 * @param source The source location of the transition to be returned.
	 * @param dataGuard The data guard of the transition to be returned.
	 * @param clockGuard The clock guard of the transition to be returned.
	 * @param action The action of the transition to be returned.
	 * @param target The target location of the transition to be returned.
	 * @return The transition with the actionParameters if one exists, and returns null otherwise.
	 */
	public Transition getTransition(String source, String dataGuard, ClockGuard clockGuard, Action action, String target){
		for (Transition t : this.transitions.values()) {
			if (t.getSource().getLabel().equals(source) && t.getDataGuard().equals(dataGuard) && t.getClockGuard().equals(clockGuard) && 
					t.getAction().equals(action) && t.getTarget().getLabel().equals(target)){
				return t;
			}
		}
		return null;
	}
	
	
	/**
	 * Creates a transition of the TIOSTS. It is assumed the source and target location already exists.
	 * @param sourceLocation The label of the source location of the transition.
	 * @param dataGuard The data guard of the transition.
	 * @param clockGuardString The clock guard of the transition.
	 * @param action The action of the transition.
	 * @param dataAssignments The data assignments of the transition.
	 * @param clockAssignments The clock assignments of the transition. 
	 * @param targetLocation The label of the target location of the transition.
	 */
	public void createTransition(String sourceLocation, String dataGuard, String clockGuardString, Action action, String dataAssignments, String clockAssignments, String targetLocation) {
		ClockGuard clockGuard = new ClockGuard(clockGuardString, this.clocks);
		createTransition(sourceLocation, dataGuard, clockGuard, action, dataAssignments, clockAssignments, targetLocation);
	}

	
	/**
	 * Creates a transition of the TIOSTS. It is assumed the source and target location already exists.
	 * @param sourceLocation The label of the source location of the transition.
	 * @param dataGuard The data guard of the transition.
	 * @param clockGuard The clock guard of the transition.
	 * @param action The action of the transition.
	 * @param dataAssignments The data assignments of the transition.
	 * @param clockAssignments The clock assignments of the transition. 
	 * @param targetLocation The label of the target location of the transition.
	 */
	public void createTransition(String sourceLocation, String dataGuard, ClockGuard clockGuard, Action action, String dataAssignments, String clockAssignments, String targetLocation) {
		Location source = this.locations.get(sourceLocation);
		Location target = this.locations.get(targetLocation);
		
		// Updates the initial condition considering that there is only one outgoing transition from the initial location
		if (source.isInitialLocation()){
			this.initialCondition = dataGuard;
		}
		
		Transition transition = new Transition(source, dataGuard, clockGuard, action, dataAssignments, clockAssignments, target);
		
		source.addOutTransition(transition);
		target.addInTransition(transition);
		
		this.transitions.put(transition.getKey(), transition);
	}

	
	/**
	 * Creates a transition of the TIOSTS. It is assumed the source and target location already exists.
	 * @param sourceLocation The label of the source location of the transition.
	 * @param dataGuard The data guard of the transition.
	 * @param clockGuardString The clock guard of the transition.
	 * @param action The action of the transition.
	 * @param dataAssignments The data assignments of the transition.
	 * @param clockAssignments The clock assignments of the transition.
	 * @param deadline The deadline of the transition. The possible values are defined by the following constants: (1) for lazy deadline use 
	 * {@link Constants#DEADLINE_LAZY}; (2) for delayable deadline use {@link Constants#DEADLINE_DELAYABLE}; (3) for eager 
	 * deadline use {@link Constants#DEADLINE_EAGER}.
	 * @param targetLocation The label of the target location of the transition.
	 */
	public void createTransition(String sourceLocation, String dataGuard, String clockGuardString, Action action, String dataAssignments, String clockAssignments, String deadline, String targetLocation) {
		ClockGuard clockGuard = new ClockGuard(clockGuardString, this.clocks);
		createTransition(sourceLocation, dataGuard, clockGuard, action, dataAssignments, clockAssignments, deadline, targetLocation);
	}

	
	/**
	 * Creates a transition of the TIOSTS. It is assumed the source and target location already exists.
	 * @param sourceLocation The label of the source location of the transition.
	 * @param dataGuard The data guard of the transition.
	 * @param clockGuard The clock guard of the transition.
	 * @param action The action of the transition.
	 * @param dataAssignments The data assignments of the transition.
	 * @param clockAssignments The clock assignments of the transition.
	 * @param deadline The deadline of the transition. The possible values are defined by the following constants: (1) for lazy deadline use 
	 * {@link Constants#DEADLINE_LAZY}; (2) for delayable deadline use {@link Constants#DEADLINE_DELAYABLE}; (3) for eager 
	 * deadline use {@link Constants#DEADLINE_EAGER}.
	 * @param targetLocation The label of the target location of the transition.
	 */
	public void createTransition(String sourceLocation, String dataGuard, ClockGuard clockGuard, Action action, String dataAssignments, String clockAssignments, String deadline, String targetLocation) {
		Location source = this.locations.get(sourceLocation);
		Location target = this.locations.get(targetLocation);
		
		// Updates the initial condition considering that there is only one outgoing transition from the initial location
		if (source.isInitialLocation()){
			this.initialCondition = dataGuard;
		}
		
		Transition transition = new Transition(source, dataGuard, clockGuard, action, dataAssignments, clockAssignments, deadline, target);
		
		source.addOutTransition(transition);
		target.addInTransition(transition);
		
		this.transitions.put(transition.getKey(), transition);
	}

	
	/**
	 * Verifies if this TIOSTS is equals to the other object.
	 * @return true if this TIOSTS is equals to the other object or false otherwise.
	 */
	@Override
	public boolean equals(Object other) {
		if (other == null) return false;
		try {
			TIOSTS otherTIOSTS = (TIOSTS) other;
			return this.name.equals(otherTIOSTS.getName()) && this.initialCondition.equals(otherTIOSTS.getInitialCondition()) && 
					this.initialLocation.equals(otherTIOSTS.getInitialLocation()) && this.variables.equals(otherTIOSTS.getVariables()) &&
					this.actionParameters.equals(otherTIOSTS.getActionParameters()) && this.clocks.equals(otherTIOSTS.getClocks()) &&
					this.inputActions.equals(otherTIOSTS.getInputActions()) && this.outputActions.equals(otherTIOSTS.getOutputActions()) &&
					this.internalActions.equals(otherTIOSTS.getInternalActions()) && this.getLocations().toString().equals(otherTIOSTS.getLocations().toString()) &&
					this.getTransitions().toString().equals(otherTIOSTS.getTransitions().toString());
		} catch (ClassCastException e) {
			return false;
		}
	}
	
	/**
	 * Creates a new TIOSTS with the same information of this instance.
	 * @return A new TIOSTS with thtiosts2e same information of this instance.
	 */
	@Override
	public TIOSTS clone(){
		TIOSTS clone = new TIOSTS(this.name);
		clone.setInitialCondition(this.initialCondition);
		
		for (TypedData variable : this.variables) {
			clone.addVariable(variable.clone());
		}
		
		for (TypedData parameter : this.actionParameters) {
			clone.addActionParameter(parameter.clone());
		}
		
		for (String clock : this.clocks) {
			clone.addClock(clock);
		}
		
		for (Action inputAction : this.inputActions) {
			clone.addInputAction(inputAction.clone());
		}
		
		for (Action outputAction : this.outputActions) {
			clone.addOutputAction(outputAction.clone());
		}
		
		for (Action internalAction : this.internalActions) {
			clone.addInternalAction(internalAction.clone());
		}
		
		for (Location location : this.locations.values()) {
			Location newLocation = new Location(location.getLabel(), location.isInitialLocation());
			clone.addLocation(newLocation);
			if (newLocation.isInitialLocation()) clone.setInitialLocation(newLocation);
		}
		
		for (Transition t : this.transitions.values()) {
			Action action = t.getAction();
			Action newAction = null;
			List<Action> actions = null;
			if (action.getType() == Constants.ACTION_INPUT) {
				actions = clone.getInputActions();
				newAction = actions.get(actions.indexOf(action));
			} else if (action.getType() == Constants.ACTION_OUTPUT) {
				actions = clone.getOutputActions();
				newAction = actions.get(actions.indexOf(action));
			} else {
				actions = clone.getInternalActions();
				newAction = actions.get(actions.indexOf(action));
			}
			if (newAction == null) System.out.println("newAction is null in class TIOSTS - method clone!");
			clone.createTransition(t.getSource().getLabel(), t.getDataGuard(), t.getClockGuard().toString(), newAction, t.getDataAssignments(), t.getClockAssignments(), t.getDeadline(), t.getTarget().getLabel());
		}
		
		return clone;
	}
	
	
}
