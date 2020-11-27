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
 * Wilkerson de Lucena Andrade      15/07/2010     Initial version
 * 
 */
package br.edu.ufcg.symbolrt.base;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import br.edu.ufcg.symbolrt.util.Constants;




/**
 * <code>Location</code> Class. <br>
 * This class is used to represent a location (node) in a TIOSTS model of type {@link TIOSTS}.
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
public class Location {

	
	// **************************** Attributes ****************************	
	
	/**
	 * The label of the location.
	 */
	private String label;
	
	/**
	 * A list of outgoing transitions of the location.
	 */
	private List<Transition> outTransitions;
	
	/**
	 * A list of incoming transitions of the location.
	 */
	private List<Transition> inTransitions;
	
	/**
	 * The flag defined for indicating whether the location is the initial location of the model.
	 */
	private boolean isInitialLocation;

	

	
	// **************************** Constructors ****************************	
	
	/**
	 * Creates a new location with the specified label.
	 * @param label The label of the location.
	 */
	public Location(String label){
		this.label = label;
		inTransitions = new LinkedList<Transition>();
		outTransitions = new LinkedList<Transition>();
		this.isInitialLocation = false;
	}
	
	
	/**
	 * Creates a new location with the specified label.
	 * @param label The label of the location.
	 * @param isInitialLocation The condition of the location indicating whether it is initial location or not.
	 */
	public Location(String label, boolean isInitialLocation){
		this.label = label;
		inTransitions = new LinkedList<Transition>();
		outTransitions = new LinkedList<Transition>();
		this.isInitialLocation = isInitialLocation;
	}
	


	
	// **************************** Methods ****************************	
	
	/**
	 * Returns the label of this location.
	 * @return The label of this location.
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Modifies the label of this location.
	 * @param label The new label to set.
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * Returns a list of all incoming transitions at this location.
	 * @return The list of all incoming transitions at this location.
	 */
	public List<Transition> getInTransitions() {
		return inTransitions;
	}

	/**
	 * Modifies the list of all incoming transitions at this location.
	 * @param inTransitions The new list of incoming transitions to set.
	 */
	public void setInTransitions(List<Transition> inTransitions) {
		this.inTransitions = inTransitions;
	}
	
	/**
	 * Add a new incoming transition.
	 * @param transition The new incoming transition to be added.
	 */
	public void addInTransition(Transition transition) {
		this.inTransitions.add(transition);
	}
	
	/**
	 * Returns a list of all outgoing transitions at this location.
	 * @return The list of all outgoing transitions at this location.
	 */
	public List<Transition> getOutTransitions() {
		return outTransitions;
	}

	/**
	 * Modifies the list of all outgoing transitions at this location.
	 * @param outTransitions The new list of outgoing transitions to set.
	 */
	public void setOutTransitions(List<Transition> outTransitions) {
		this.outTransitions = outTransitions;
	}

	/**
	 * Add a new outgoing transition.
	 * @param transition The new outgoing transition to be added.
	 */
	public void addOutTransition(Transition transition) {
		this.outTransitions.add(transition);
	}
	
	/**
	 * Returns True if this location is the initial location of the model, and returns False otherwise.
	 * @return True if this location is the initial location of the model, and returns False otherwise.
	 */
	public boolean isInitialLocation() {
		return this.isInitialLocation;
	}
	
	/**
	 * Modifies the flag defined for indicating whether the location is the initial location of the model.
	 * @param isInitialLocation The new condition of the location.
	 */
	public void setIsInitialLocation(boolean isInitialLocation) {
		this.isInitialLocation = isInitialLocation;
	}

	
	
	
	/**
	 * Returns true if this is an accept or a reject location, and false otherwise. 
	 * @return true if this is an accept or a reject location, and false otherwise.
	 */
	public boolean isSpecialLocation(){
		return this.label.equals(Constants.LOCATION_LABEL_ACCEPT) || this.label.equals(Constants.LOCATION_LABEL_REJECT);
	}

	
	/**
	 * Returns true if this is a reject location, and false otherwise. 
	 * @return true if this is a reject location, and false otherwise.
	 */
	public boolean isRejectLocation(){
		return this.label.equals(Constants.LOCATION_LABEL_REJECT);
	}
	
	
	/**
	 * Returns a collection with all outgoing actions.
	 * @return A collection with all outgoing actions.
	 */
	public Collection<Action> getOutActions(){
		Collection<Action> outActions = new ArrayList<Action>();
		
		for (Transition transition : this.outTransitions) {
			outActions.add(transition.getAction());
		}
		
		return outActions;
	}
	
	
	/**
	 * Verifies if this location is equals to the other object.
	 * @return true if this location is equals to the other object or false otherwise.
	 */
	@Override
	public boolean equals(Object other) {
		if (other == null) return false;
		try {
			Location otherLocation = (Location) other;
			return this.isInitialLocation == otherLocation.isInitialLocation() && this.label.equals(otherLocation.getLabel()) && 
				this.inTransitions.equals(otherLocation.getInTransitions()) && this.outTransitions.equals(otherLocation.getOutTransitions());
		} catch (ClassCastException e) {
			return false;
		}
	}
	
	
	/**
	 * Returns a string representing this location.
	 * @return A string representing this location.
	 */
	@Override
	public String toString() {
		return this.label;
	}

	
}
