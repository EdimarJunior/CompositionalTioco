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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.edu.ufcg.symbolrt.base.Action;


/**
 * <code>SymbolicAction</code> Class.<br>
 * This class is used to represent a Symbolic Action of a transition in a Zone-Based Symbolic Execution Tree. 
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
public class SymbolicAction {
	
	
	// **************************** Attributes ****************************
	
	/**
	 * The corresponding action in the TIOSTS model.
	 */
	private Action action;
	
	/**
	 * The list of unique identifiers denoting the action parameters of the action.
	 */
	private  List<String> paramIds;
	
	/**
	 * The mapping from the original action parameter names to the unique identifiers in paramIds.
	 */
	private Map<String,String> paramMapping;
	
	
	
	
	// **************************** Constructors ****************************
	
	/**
	 * Creates a new action with the following specified parameters.
	 * @param action The corresponding action in the TIOSTS model.
	 * @param paramIds The list of unique identifiers associated with the action parameters of the concrete action.
	 * @param paramMapping The mapping from the original action parameter names to the unique identifiers.
	 */
	public SymbolicAction(Action action, List<String> paramIds, Map<String,String> paramMapping){
		this.action = action;
		this.paramIds = paramIds;
		this.paramMapping = paramMapping;
	}




	// **************************** Methods ****************************
	
	/**
	 * Returns the concrete action associated with this symbolic action.
	 * @return The concrete action associated with this symbolic action.
	 */
	public Action getAction() {
		return action;
	}


	/**
	 * Modifies the concrete action associated with this symbolic action.
	 * @param action The new concrete action to set.
	 */
	public void setAction(Action action) {
		this.action = action;
	}


	/**
	 * Returns the unique identifiers associated with the action parameters of the concrete action.
	 * @return The list of unique identifiers associated with the action parameters of the concrete action.
	 */
	public List<String> getParamIds() {
		return paramIds;
	}


	/**
	 * Modifies the unique identifiers associated with the action parameters of the concrete action.
	 * @param paramIds The new list of identifies to set.
	 */
	public void setParamIds(List<String> paramIds) {
		this.paramIds = paramIds;
	}


	/**
	 * Returns the mapping from the original action parameter names to the unique identifiers.
	 * @return The mapping from the original action parameter names to the unique identifiers.
	 */
	public Map<String, String> getParamMapping() {
		return paramMapping;
	}


	/**
	 * Modifies the mapping from the original action parameter names to the unique identifiers.
	 * @param paramMapping The new mapping to set.
	 */
	public void setParamMapping(Map<String, String> paramMapping) {
		this.paramMapping = paramMapping;
	}
	

	/**
	 * Verifies if this symbolic action is equals to the other object.
	 * @return true if this symbolic action is equals to the other object or false otherwise.
	 */
	@Override
	public boolean equals(Object other) {
		if (other == null) return false;
		try {
			SymbolicAction otherSymbolicAction = (SymbolicAction) other;
			return this.action.equals(otherSymbolicAction.getAction()) && this.paramIds.equals(otherSymbolicAction.getParamIds())  
																		&&	this.paramMapping.equals(otherSymbolicAction.getParamMapping());
		} catch (ClassCastException e) {
			return false;
		}
	}
	

	/**
	 * Returns a string representing this symbolic action.
	 * @return A string representing this symbolic action.
	 */
	@Override
	public String toString() {
		StringBuffer result = new StringBuffer(this.action.toString());
		
		String uniqueIdentifiers = uniqueIdentifiersToString();
		if (uniqueIdentifiers.length() != 0) {
			//result.append("{" + uniqueIdentifiersToString() + "}");
		}
		result.append(this.paramMapping.toString());
		return result.toString();
	}

	
	/*
	 * Returns a string representing the unique identifiers of the action parameters. 
	 * @return The string representing the unique identifiers of the action parameters.
	 */
	private String uniqueIdentifiersToString(){
		if (this.paramIds.size() > 0) {
			StringBuffer result = new StringBuffer();
			for (String uniqueId : this.paramIds) {
				result.append(uniqueId + ",");
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
	public SymbolicAction clone() {
		return new SymbolicAction(this.action.clone(), new ArrayList<String>(this.paramIds), new HashMap<String, String>(this.paramMapping));
	}
	

}
