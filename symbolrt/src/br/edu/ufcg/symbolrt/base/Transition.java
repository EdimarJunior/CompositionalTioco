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
 * Wilkerson de Lucena Andrade      20/07/2010     Initial version
 * Wilkerson de Lucena Andrade      03/01/2010     Guards divided into data and clock guards
 * 
 */
package br.edu.ufcg.symbolrt.base;

import br.edu.ufcg.symbolrt.util.Constants;

/**
 * <code>Transition</code> Class. <br>
 * This class is used to represent a transition (edge) in a TIOSTS model of type {@link TIOSTS}. In other 
 * words, this class represents a directed connection between two locations (i.e., two objects of
 * class {@link Location}.
 * 
 * @author Wilkerson de Lucena Andrade  ( <a href="mailto:wilkerson@computacao.ufcg.edu.br">wilkerson@computacao.ufcg.edu.br</a> )
 * 
 * @version 1.1
 * <br>
 * SYmbolic Model-Based test case generation toOL for Real-Time systems (SYMBOLRT)
 * <br>
 * (C) Copyright 2010-2013 Federal University of Campina Grande (UFCG)
 * <br>
 * <a href="https://sites.google.com/a/computacao.ufcg.edu.br/symbolrt">https://sites.google.com/a/computacao.ufcg.edu.br/symbolrt</a>
 */
public class Transition {
	
	
	// **************************** Attributes ****************************
	
	/**
	 * The source location of the transition.
	 */
	private Location source;
	
	/**
	 * The data guard of the transition. If there is no data guard use {@link Constants#GUARD_TRUE}.
	 */
	private String dataGuard;
	
	/**
	 * The clock guard of the transition. If there is no clock guard use {@link Constants#GUARD_TRUE};
	 */
	private ClockGuard clockGuard;
	
	/**
	 * The action of the transition.
	 */
	private Action action;
	
	/**
	 * The assignments of the transition.
	 */
	private String dataAssignments;
	
	/**
	 * The clocks to be reset to zero.
	 */
	private String clockAssignments;
	
	/**
	 * The deadline of the transition. The possible values are defined by the following constants: (1) for lazy deadline use 
	 * {@link Constants#DEADLINE_LAZY}; (2) for delayable deadline use {@link Constants#DEADLINE_DELAYABLE}; (3) for eager 
	 * deadline use {@link Constants#DEADLINE_EAGER}.
	 */
	private String deadline;
	
	/**
	 * The target location of the transition.
	 */
	private Location target;
	
	/**
	 * The id that uniquely identifies a transition.
	 */
	private String key;

	
	
	
	// **************************** Constructors ****************************
	
	/**
	 * Creates a new transition with the following specified parameters.
	 * @param source The source location of the transition.
	 * @param dataGuard The data guard of the transition. If there is no data guard use {@link Constants#GUARD_TRUE}.
	 * @param clockGuard The clock guard of the transition.
	 * @param action The action of the transition.
	 * @param dataAssignments The data assignments of the transition.
	 * @param clockAssignments The clock assignments of the transition.
	 * @param deadline The deadline of the transition. The possible values are defined by the following constants: (1) for lazy deadline use 
	 * {@link Constants#DEADLINE_LAZY}; (2) for delayable deadline use {@link Constants#DEADLINE_DELAYABLE}; (3) for eager 
	 * deadline use {@link Constants#DEADLINE_EAGER}.
	 * @param target The target location of the transition.
	 */
	public Transition(Location source, String dataGuard, ClockGuard clockGuard, Action action, String dataAssignments, String clockAssignments, String deadline, Location target){
		this.source = source;
		this.dataGuard = dataGuard;
		this.clockGuard = clockGuard;
		this.action = action;
		this.dataAssignments = dataAssignments;
		this.clockAssignments = clockAssignments;
		this.deadline = deadline;
		this.target = target;
		setKey();
	}
	
	
	/**
	 * Creates a new transition with the following specified parameters and default value for deadline: delayable for output actions and 
	 * lazy for input and internal actions.
	 * @param source The source location of the transition.
	 * @param dataGuard The data guard of the transition. If there is no data guard use {@link Constants#GUARD_TRUE}.
	 * @param clockGuard The clock guard of the transition. 
	 * @param action The action of the transition.
	 * @param dataAssignments The data assignments of the transition.
	 * @param clockAssignments The clock assignments of the transition.
	 * @param target The target location of the transition.
	 */
	public Transition(Location source, String dataGuard, ClockGuard clockGuard, Action action, String dataAssignments, String clockAssignments, Location target){
		this.source = source;
		this.dataGuard = dataGuard;
		this.clockGuard = clockGuard;
		this.action = action;
		this.dataAssignments = dataAssignments;
		this.clockAssignments = clockAssignments;
		this.target = target;
		setKey();
		setDefaultDeadline();
	}
	

	
	
	// **************************** Methods ****************************
	
	/**
	 * Returns the source location connected to this transition.
	 * @return The source location connected to this transition.
	 */
	public Location getSource() {
		return source;
	}

	
	/**
	 * Modifies the source location connected to this transition.
	 * @param source The new source location to set.
	 */
	public void setSource(Location source) {
		this.source = source;
		setKey();
	}

		
	/**
	 * Returns the data guard of this transition.
	 * @return The data guard of this transition.
	 */
	public String getDataGuard() {
		return dataGuard;
	}

	
	/**
	 * Modifies the data guard of this transition.
	 * @param dataGuard The new data guard to set.
	 */
	public void setDataGuard(String dataGuard) {
		this.dataGuard = dataGuard;
	}
	
	
	/**
	 * Returns the clock guard of this transition.
	 * @return The clock guard of this transition.
	 */
	public ClockGuard getClockGuard() {
		return clockGuard;
	}

	
	/**
	 * Modifies the clock guard of this transition.
	 * @param clockGuard The new clock guard to set.
	 */
	public void setClockGuard(ClockGuard clockGuard) {
		this.clockGuard = clockGuard;
	}

	
	/**
	 * Returns the all guard of this transition (i.e., the data and the clock guard).
	 * @return The all guard of this transition.
	 */
	public String getAllGuard(){
		String clockGuardString = this.clockGuard.toString();
		if (clockGuardString.equals(Constants.GUARD_TRUE)){
			return this.dataGuard;
		} else if (this.dataGuard.equals(Constants.GUARD_TRUE)){
			return clockGuardString;
		} else {
			return this.dataGuard + " " + Constants.GUARD_CONJUNCTION + " " + clockGuardString;
		}
	}

	
	/**
	 * Returns the action of this transition.
	 * @return The action of this transition.
	 */
	public Action getAction() {
		return action;
	}

	
	/**
	 * Modifies the action of this transition.
	 * @param action The new action to set.
	 */
	public void setAction(Action action) {
		this.action = action;
		setKey();
	}

	
	/**
	 * Returns the data assignments of this transition.
	 * @return The data assignments of this transition.
	 */
	public String getDataAssignments() {
		return dataAssignments;
	}

	
	/**
	 * Modifies the data assignments of this transition.
	 * @param dataAssignments The new data assignments to set.
	 */
	public void setAssignments(String dataAssignments) {
		this.dataAssignments = dataAssignments;
	}

	
	/**
	 * Returns the clocks to be reset in this transition.
	 * @return The clock assignments of this transition.
	 */
	public String getClockAssignments() {
		return clockAssignments;
	}

	
	/**
	 * Modifies the clock assignments of this transition.
	 * @param clockAssignments The new clock assignments to set.
	 */
	public void setClockAssignments(String clockAssignments) {
		this.clockAssignments = clockAssignments;
	}
	
	
	/**
	 * Returns all assignments of this transition (i.e., the data and the clock assignments).
	 * @return All assignments of this transition.
	 */
	public String getAllAssignments(){
		if ((clockAssignments == null) && (dataAssignments == null)) {
			return "";
		} else if ((clockAssignments != null) && (dataAssignments != null)) {
			return dataAssignments + Constants.ASSIGNMENT_SEPARATOR + clockAssignments;  
		} else if (clockAssignments == null) {
			return dataAssignments;
		} else {
			return clockAssignments;
		}
	}
	
		
	/**
	 * Returns the deadline of this transition.
	 * @return The deadline of this transition.
	 */
	public String getDeadline() {
		return deadline;
	}


	/**
	 * Modifies the deadline of this transition.
	 * @param deadline The new deadline to set. The possible values are defined by the following constants: (1) for lazy deadline use 
	 * {@link Constants#DEADLINE_LAZY}; (2) for delayable deadline use {@link Constants#DEADLINE_DELAYABLE}; (3) for eager 
	 * deadline use {@link Constants#DEADLINE_EAGER}.
	 */
	public void setDeadline(String deadline) {
		this.deadline = deadline;
	}


	/**
	 * Returns the target location connected to this transition.
	 * @return The target location connected to this transition.
	 */
	public Location getTarget() {
		return target;
	}

	
	/**
	 * Modifies the target location connected to this transition.
	 * @param target The new target location to set.
	 */
	public void setTarget(Location target) {
		this.target = target;
		setKey();
	}

	
	/**
	 * Returns the string which uniquely identifies this transition.
	 * @return The string which uniquely identifies this transition.
	 */
	public String getKey() {
		return key;
	}
	

	/**
	 * Updates the key which uniquely identifies this transition.
	 */
	private void setKey(){
		if (action != null) {
			this.key = source.getLabel() + "#" + action.getName() + "#" + target.getLabel();
		} else {
			this.key = source.getLabel() + "#_#" + target.getLabel();
		}
	}

	
	/**
	 * Updates the deadline of this transition. The default value for output actions is delayable 
	 * and for input and internal actions is lazy. 
	 */
	private void setDefaultDeadline(){
		if (this.action.getType() == Constants.ACTION_OUTPUT){
			this.deadline = Constants.DEADLINE_DELAYABLE;
		} else {
			this.deadline = Constants.DEADLINE_LAZY;
		}
	}
	
	
	/**
	 * Verifies if this transition is equals to the other object.
	 * @return true if this transition is equals to the other object or false otherwise.
	 */
	@Override
	public boolean equals(Object other) {
		if (other == null) return false;
		try {
			Transition otherTansition = (Transition) other;
			return this.source.getLabel().equals(otherTansition.getSource().getLabel()) && this.dataGuard.equals(otherTansition.getDataGuard()) &&
					this.clockGuard.equals(otherTansition.getClockGuard()) && this.action.equals(otherTansition.getAction()) &&
					this.deadline.equals(otherTansition.getDeadline()) && 
					(this.dataAssignments==null? otherTansition.getDataAssignments()==null : this.dataAssignments.equals(otherTansition.getDataAssignments())) &&
					(this.clockAssignments==null? otherTansition.getClockAssignments()==null : this.clockAssignments.equals(otherTansition.getClockAssignments())) &&
					 this.target.getLabel().equals(otherTansition.getTarget().getLabel());
		} catch (ClassCastException e) {
			return false;
		}
	}

	
	/**
	 * Returns a string representing the data of this transition (guard, action, and assignments).
	 * @return A string representing the data of this transition (guard, action, and assignments).
	 */
	@Override
	public String toString(){
		StringBuffer result = new StringBuffer();
		
		result.append("["+ getAllGuard() + "]\n");
		
		result.append("sync " + this.action.toString() + "\n");	
		
		result.append("do {" + getAllAssignments() + "}\n");
		
		result.append(this.deadline);
		
		return result.toString();
	}
	
	
}
