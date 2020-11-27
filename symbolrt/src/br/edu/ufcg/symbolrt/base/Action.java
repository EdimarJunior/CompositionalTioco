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
import java.util.List;

import br.edu.ufcg.symbolrt.util.Constants;


/**
 * <code>Action</code> Class.<br>
 * This class is used to represent an action of a transition in a TIOSTS model. There are three kinds of actions:
 * input actions, output actions, and internal actions. Each action has a signature, i.e., a tuple of distinct parameters.
 * The signature of internal actions is the empty tuple.  
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
public class Action {

	
	// **************************** Attributes ****************************
	
	/**
	 * The type of the action. The possible values are defined by the following constants: (1) for input action use 
	 * {@link Constants#ACTION_INPUT}; (2) for output action use {@link Constants#ACTION_OUTPUT}; (3) for internal action use
	 * {@link Constants#ACTION_INTERNAL}.
	 */
	private int type;
	
	/**
	 * The name of the action.
	 */
	private String name;
	
	/**
	 * The list of parameters of the action.
	 */
	private  List<String> parameters;


	
	
	// **************************** Constructors ****************************
	
	/**
	 * Creates a new action with the following specified parameters.
	 * @param name The name of the action.
	 * @param type The type of the action. The possible values are defined by the following constants: (1) for input action use 
	 * {@link Constants#ACTION_INPUT}; (2) for output action use {@link Constants#ACTION_OUTPUT}; (3) for internal action use
	 * {@link Constants#ACTION_INTERNAL}.
	 * @param parameters The parameters of the action.
	 */
	public Action(String name, int type, List<String> parameters){
		this.name = name;
		this.type = type;
		this.parameters = parameters;
	}

	/**
	 * Creates a new action with the following specified parameters.
	 * @param name The name of the action.
	 * @param type The type of the action. The possible values are defined by the following constants: (1) for input action use 
	 * {@link Constants#ACTION_INPUT}; (2) for output action use {@link Constants#ACTION_OUTPUT}; (3) for internal action use
	 * {@link Constants#ACTION_INTERNAL}.
	 */
	public Action(String name, int type){
		this.name = name;
		this.type = type;
		this.parameters = new ArrayList<String>();
	}
	
	

	
	// **************************** Methods ****************************

	/**
	 * Returns the type of this action.
	 * @return The type of this action.
	 */
	public int getType() {
		return type;
	}


	/**
	 * Modifies the type of this action. The possible values are defined by the following constants: (1) for input action use 
	 * {@link Constants#ACTION_INPUT}; (2) for output action use {@link Constants#ACTION_OUTPUT}; (3) for internal action use
	 * {@link Constants#ACTION_INTERNAL}.
	 * @param type The new type to set.
	 */
	public void setType(int type) {
		this.type = type;
	}
	

	/**
	 * Returns the name of this action.
	 * @return The name of this action.
	 */
	public String getName() {
		return name;
	}


	/**
	 * Modifies the name of this action.
	 * @param name The new name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}


	/**
	 * Returns the list of parameters of this action.
	 * @return The list of parameters of this action.
	 */
	public List<String> getParameters() {
		return parameters;
	}


	/**
	 * Modifies the list of parameters of this action.
	 * @param parameters The new list of parameters to set.
	 */
	public void setParameters(List<String> parameters) {
		this.parameters = parameters;
	}
	
	
	/**
	 * Adds a new parameter in this action.
	 * @param parameter The new parameter to be added.
	 */
	public void addParameter(String parameter) {
		this.parameters.add(parameter);
	}
	
	
	/**
	 * Verifies if this action is equals to the other object. An action is considered equals to another if it has the same name.
	 * @return true if this action is equals to the other object or false otherwise.
	 */
	@Override
	public boolean equals(Object other) {
		if (other == null) return false;
		try {
			Action otherAction = (Action) other;
			return this.name.equals(otherAction.getName());
		} catch (ClassCastException e) {
			return false;
		}
	}
	
	
	/**
	 * Returns a string representing this action.
	 * @return A string representing this action.
	 */
	@Override
	public String toString() {
		StringBuffer result = new StringBuffer(this.name);
		
		switch (this.type) {
		case Constants.ACTION_INPUT:
			result.append("?(" + parametersToString() + ")");
			break;
		case Constants.ACTION_OUTPUT:
			result.append("!(" + parametersToString() + ")");
			break;
		default:
			break;
		}
		
		return result.toString();
	}
	
	
	/*
	 * Returns a string representing the parameters of this action in sequence. 
	 * @return The string representing the parameters.
	 */
	private String parametersToString(){
		if (this.parameters.size() > 0) {
			StringBuffer result = new StringBuffer();
			for (String parameter : this.parameters) {
				result.append(parameter + ",");
			}
			return result.substring(0, result.length() - 1);
		}
		return "";
	}
	
	/**
	 * Creates and returns a copy of this object.
	 * @return A clone of this instance.
	 */
	@Override
	public Action clone(){
		return new Action(this.name, this.type, new ArrayList<String>(this.parameters));
	}
	
	
}
