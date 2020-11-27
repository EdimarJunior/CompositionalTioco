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
 * Wilkerson de Lucena Andrade      05/01/2011     Initial version
 */
package br.edu.ufcg.symbolrt.symbolicexecution.base;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.edu.ufcg.symbolrt.base.Location;
import br.edu.ufcg.symbolrt.util.Constants;


/**
 * <code>SymbolicState</code> Class.<br>
 * This class is used to represent a Zone-Based Symbolic Extended State (ZSES) in a Zone-Based Symbolic Execution Tree (ZSET). 
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
public class SymbolicState {
	
	
	// **************************** Attributes ****************************
	
	/**
	 * The corresponding location in the TIOSTS model.
	 */
	private Location location;
	
	/**
	 * The path condition representing a data guard.
	 */
	private  String pathCondition;
	
	/**
	 * The mapping from variables and action parameters to their symbolic values.
	 */
	private Map<String,String> mapping;
	
	/**
	 * The zone representing the solution set of a clock constraint.
	 */
	private String zone; 
	
	/**
	 * The transition connecting this ZSES to its parent.
	 */
	private SymbolicTransition parentTransition;
	
	/**
	 * The transitions connecting this ZSES to its children.
	 */
	private List<SymbolicTransition> childrenTransitions; 
	
	
	
	
	// **************************** Constructors ****************************

	/**
	 * Creates a new action with the following specified parameters.
	 * @param location The corresponding location in the TIOSTS model.
	 * @param pathCondition The path condition representing a data guard.
	 * @param mapping The mapping from variables and action parameters to their symbolic values.
	 * @param zone The zone representing the solution set of a clock constraint.
	 */
	public SymbolicState(Location location, String pathCondition, Map<String,String> mapping, String zone){
		this.location = location;
		this.pathCondition = pathCondition;
		this.mapping = mapping;
		this.zone = zone;
		this.parentTransition = null;
		this.childrenTransitions = new ArrayList<SymbolicTransition>();
	}

	


	// **************************** Methods ****************************

	/**
	 * Returns the corresponding location of this ZSES.
	 * @return The corresponding location of this ZSES.
	 */
	public Location getLocation() {
		return location;
	}


	/**
	 * Modifies the corresponding location of this ZSES.
	 * @param location The new corresponding location to set.
	 */
	public void setLocation(Location location) {
		this.location = location;
	}


	/**
	 * Returns the path condition of this ZSES.
	 * @return The path condition of this ZSES.
	 */
	public String getPathCondition() {
		return pathCondition;
	}


	/**
	 * Modifies the path condition of this ZSES.
	 * @param pathCondition The new path condition to set.
	 */
	public void setPathCondition(String pathCondition) {
		this.pathCondition = pathCondition;
	}


	/**
	 * Returns the mapping from variables and action parameters to their symbolic values.
	 * @return The mapping from variables and action parameters to their symbolic values.
	 */
	public Map<String, String> getMapping() {
		return mapping;
	}


	/**
	 * Modifies the mapping from variables and action parameters to their symbolic values.
	 * @param mapping The new mapping to set.
	 */
	public void setMapping(Map<String, String> mapping) {
		this.mapping = mapping;
	}


	/**
	 * Returns the zone of this ZSES.
	 * @return The zone of this ZSES.
	 */
	public String getZone() {
		return zone;
	}


	/**
	 * Modifies the zone of this ZSES.
	 * @param zone The new zone to set.
	 */
	public void setZone(String zone) {
		this.zone = zone;
	}


	/**
	 * Returns the transition connecting this state to its parent state.
	 * @return The transition connecting this state to its parent state.
	 */
	public SymbolicTransition getParentTransition() {
		return parentTransition;
	}


	/**
	 * Modifies the transition connecting this state to its parent state.
	 * @param parentTransition The new transition to set.
	 */
	public void setParentTransition(SymbolicTransition parentTransition) {
		this.parentTransition = parentTransition;
	}


	/**
	 * Returns a list with all transitions that connect this state to its children.
	 * @return The list with all transitions that connect this state to its children.
	 */
	public List<SymbolicTransition> getChildrenTransitions() {
		return childrenTransitions;
	}


	/**
	 * Modifies the list with all transitions that connect this state to its children.
	 * @param childrenTransitions The new list to set.
	 */
	public void setChildrenTransitions(List<SymbolicTransition> childrenTransitions) {
		this.childrenTransitions = childrenTransitions;
	}
	
	
	/**
	 * Adds a new child transition to this state.
	 * @param childTransition The new child transition to add.
	 */
	public void addChildTransition(SymbolicTransition childTransition) {
		this.childrenTransitions.add(childTransition);
	}

	
	/**
	 * Returns true if this is an accept ZSES, and false otherwise. 
	 * @return true if this is an accept ZSES, and false otherwise.
	 */
	public boolean isAcceptZSES(){
		return this.location.getLabel().contains(Constants.LOCATION_LABEL_ACCEPT);
	}
	
	
	/**
	 * Returns all outgoing input transitions from this ZSES.
	 * @return The outgoing input transitions.
	 */
	public List<SymbolicTransition> getOutgoingInputTransitions(){
		List<SymbolicTransition> inputTransitions = new ArrayList<SymbolicTransition>();
		
		for (SymbolicTransition transition : this.childrenTransitions) {
			if (transition.getSymbolicAction().getAction().getType() == Constants.ACTION_INPUT) {
				inputTransitions.add(transition);
			}
		}
		
		return inputTransitions;
	}
	
	
	/**
	 * Verifies if this ZSES is equals to the other object.
	 * @return true if this ZSES is equals to the other object or false otherwise.
	 */
	@Override
	public boolean equals(Object other) {
		if (other == null) return false;
		try {
			SymbolicState otherZSES = (SymbolicState) other;
			return this.location.equals(otherZSES.getLocation()) && this.pathCondition.equals(otherZSES.getPathCondition()) &&
					this.mapping.equals(otherZSES.getMapping()) && this.zone.equals(otherZSES.getZone());
		} catch (ClassCastException e) {
			return false;
		}
	}
	
	
	/**
	 * Returns a string representing this ZSES.
	 * @return A string representing this ZSES.
	 */
	@Override
	public String toString() {
		StringBuffer result = new StringBuffer(this.location.toString());
		result.append("\nPath condition: " + this.pathCondition);
		result.append("\n" + this.mapping.toString());
		result.append("\nZone: " + this.zone);
		
		return result.toString();
	}
	

}
