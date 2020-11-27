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
 * Wilkerson de Lucena Andrade      27/06/2011     Variable class renamed to TypedData
 * 
 */
package br.edu.ufcg.symbolrt.base;


/**
 * <code>TypedData</code> Class. <br>
 * This class is used to represent variables, constants, parameters, and action parameters in a TIOSTS model. 
 * Each typed data has a name, a type, and a value.
 * 
 * @author Wilkerson de Lucena Andrade  ( <a href="mailto:wilkerson@computacao.ufcg.edu.br">wilkerson@computacao.ufcg.edu.br</a> )
 * 
 * @version 2.0
 * <br>
 * SYmbolic Model-Based test case generation toOL for Real-Time systems (SYMBOLRT)
 * <br>
 * (C) Copyright 2010-2013 Federal University of Campina Grande (UFCG)
 * <br>
 * <a href="https://sites.google.com/a/computacao.ufcg.edu.br/symbolrt">https://sites.google.com/a/computacao.ufcg.edu.br/symbolrt</a>
 */
public class TypedData {

	
	// **************************** Attributes ****************************
	
	/*
	 * The name of the typed data.
	 */
	private String name;
	
	/*
	 * The type of the typed data. The possible values are defined by the constants
	 * defined in Constants#TYPE_*.
	 */
	private int type;
	
	/*
	 * The value of the typed data.
	 */
	private Object value;
	

	
	
	// **************************** Constructor ****************************
	
	/**
	 * Creates a new typed data with the following specified parameters.
	 * @param name The name of the typed data.
	 * @param type The type of the typed data. The possible values are defined by Constants#TYPE_*.
	 */
	public TypedData(String name, int type) {
		this.name = name;
		this.type = type;
		this.value = null;
	}




	// **************************** Methods ****************************

	
	/**
	 * Returns the name of this typed data.
	 * @return The name of this typed data.
	 */
	public String getName() {
		return name;
	}


	/**
	 * Returns the type of this typed data.
	 * @return The type of this typed data.
	 */
	public int getType() {
		return type;
	}


	/**
	 * Returns the value of this typed data.
	 * @return The value of this typed data.
	 */
	public Object getValue() {
		return value;
	}


	/**
	 * Modifies the value of this typed data.
	 * @param value The new value to set.
	 */
	public void setValue(Object value) {
		this.value = value;
	}


	/**
	 * Verifies if this typed data is equals to the other object.
	 * @return true if this typed data is equals to the other object or false otherwise.
	 */
	@Override
	public boolean equals(Object other) {
		if (other == null) return false;
		try {
			TypedData otherTypedData = (TypedData) other;
			return this.name.equals(otherTypedData.getName());
		} catch (ClassCastException e) {
			return false;
		}
	}
	
	
	/**
	 * Creates and returns a copy of this object.
	 * @return A clone of this instance.
	 */
	@Override
	public TypedData clone(){
		TypedData clone = new TypedData(this.name, this.type);
		clone.setValue(this.value);
		return clone;
	}
		
}
