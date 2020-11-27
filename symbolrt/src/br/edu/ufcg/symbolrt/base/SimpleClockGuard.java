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
 * Wilkerson de Lucena Andrade      11/08/2011     Initial version
 * 
 */
package br.edu.ufcg.symbolrt.base;

import br.edu.ufcg.symbolrt.util.Constants;

/**
 * <code>SimpleClockGuard</code> Class.<br>
 * This class is used to represent a simple clock guard of the form c#i, where c is a clock, # &isin; {<, <=, =, >=, >}, and i is an   
 * integer constant.
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
public class SimpleClockGuard {

	// **************************** Attributes ****************************
	
	/**
	 * The unique number used to identify the clock included in this clock guard. This number is used for identifying clocks in the
	 * DBM representation.
	 */
	private int clockId;
	
	/**
	 * The name of the clock used in this guard. 
	 */
	private String clock;
	
	/**
	 * The relation of the clock guard. The possible values are defined by the following constants: (1) for < use {@link Constants#GUARD_LT}; 
	 * (2) for <= use {@link Constants#GUARD_LEQ}; (3) for = use {@link Constants#GUARD_EQUAL}; (4) for >= use {@link Constants#GUARD_GEQ};
	 * (5) for > use {@link Constants#GUARD_GT}.
	 */
	private String relation;
	
	/**
	 * The integer constant used in this guard.
	 */
	private String integerConstant;
	
	
	
	
	/**
	 * Creates a new simple clock guard with the following specified parameters.
	 * @param clockId The identification code of this clock.
	 * @param clock The clock used in this guard.
	 * @param relation The relation of the clock guard. The possible values are defined by the following constants: (1) for < use 
	 * {@link Constants#GUARD_LT}; (2) for <= use {@link Constants#GUARD_LEQ}; (3) for = use {@link Constants#GUARD_EQUAL}; 
	 * (4) for >= use {@link Constants#GUARD_GEQ}; (5) for > use {@link Constants#GUARD_GT}.
	 * @param integerConstant The integer constant used in this guard.
	 */
	public SimpleClockGuard(int clockId, String clock, String relation, String integerConstant){
		this.clock = clock;
		this.clockId = clockId;
		this.relation = relation;
		this.integerConstant = integerConstant;
	}

	
	/**
	 * Returns the name of the clock used in this simple clock guard.
	 * @return The name of the clock used in this simple clock guard.
	 */
	public String getClock() {
		return clock;
	}

	
	/**
	 * Modifies the clock of this clock guard.
	 * @param clock The new clock to set.
	 */
	public void setClock(String clock) {
		this.clock = clock;
	}
	
	
	/**
	 * Returns the id of the clock used in this simple clock guard.
	 * @return The id of the clock used in this simple clock guard.
	 */
	public int getClockId() {
		return clockId;
	}

	
	/**
	 * Modifies the clock id of the clock included in this clock guard.
	 * @param clockId The clock id to set.
	 */
	public void setClockId(int clockId) {
		this.clockId = clockId;
	}


	/**
	 * Returns the relation of this simple clock guard.
	 * @return The relation of this simple clock guard.
	 */
	public String getRelation() {
		return relation;
	}

	
	/**
	 * Modifies the relation of this clock guard. The possible values are defined by the following constants: (1) for < use 
	 * {@link Constants#GUARD_LT}; (2) for <= use {@link Constants#GUARD_LEQ}; (3) for = use {@link Constants#GUARD_EQUAL}; 
	 * (4) for >= use {@link Constants#GUARD_GEQ}; (5) for > use {@link Constants#GUARD_GT}.
	 * @param relation The new relation to set.
	 */
	public void setRelation(String relation) {
		this.relation = relation;
	}

	
	/**
	 * Returns the integer constant of this simple clock guard.
	 * @return The integer constant of this simple clock guard.
	 */
	public String getIntegerConstant() {
		return integerConstant;
	}

	
	/**
	 * Modifies the integer constant of this clock guard.
	 * @param integerConstant The new integer constant to set.
	 */	
	public void setIntegerConstant(String integerConstant) {
		this.integerConstant = integerConstant;
	}
	
	/**
	 * Verifies if this simple clock guard is equals to the other object. A simple clock guard is considered equals to another if 
	 * it has the same attributes.
	 * @return true if this simple clock guard is equals to the other object or false otherwise.
	 */
	@Override
	public boolean equals(Object other) {
		if (other == null) return false;
		try {
			SimpleClockGuard otherSimpleClockGuard = (SimpleClockGuard) other;
			return this.clockId == otherSimpleClockGuard.getClockId() && this.clock.equals(otherSimpleClockGuard.getClock()) &&
					this.relation.equals(otherSimpleClockGuard.getRelation()) && this.integerConstant.equals(otherSimpleClockGuard.getIntegerConstant());
		} catch (ClassCastException e) {
			return false;
		}
	}
	
	/**
	 * Returns a string representing this simple clock guard.
	 * @return A string representing this simple clock guard.
	 */
	@Override
	public String toString(){
		return this.clock + " " + this.relation + " " + this.integerConstant;
	}

	public String toDBMFormat(){
		String result = "";
		
		if (this.relation.equals(Constants.GUARD_LEQ)) {
			result = this.clockId + " " + Constants.DBM_CLOCK_REFERENCE + " " + this.integerConstant + " " + Constants.DBM_WEAK;
		} else if (this.relation.equals(Constants.GUARD_LT)) {
			result = this.clockId + " " + Constants.DBM_CLOCK_REFERENCE + " " + this.integerConstant + " " + Constants.DBM_STRICT;
		} else if (this.relation.equals(Constants.GUARD_EQUAL)) {
			result = this.clockId + " " + Constants.DBM_CLOCK_REFERENCE + " " + this.integerConstant + " " + Constants.DBM_WEAK + "\n" +
					Constants.DBM_CLOCK_REFERENCE + " " + this.clockId + " -" + this.integerConstant + " " + Constants.DBM_WEAK;
		} else if (this.relation.equals(Constants.GUARD_GEQ)) {
			result = Constants.DBM_CLOCK_REFERENCE + " " + this.clockId + " -" + this.integerConstant + " " + Constants.DBM_WEAK;
		} else if (this.relation.equals(Constants.GUARD_GT)) {
			result = Constants.DBM_CLOCK_REFERENCE + " " + this.clockId + " -" + this.integerConstant + " " + Constants.DBM_STRICT;
		}
		
		return result.toString();
	}
	
	/**
	 * Creates and returns a copy of this object.
	 * @return A clone of this instance.
	 */
	@Override
	public SimpleClockGuard clone(){
		return new SimpleClockGuard(this.clockId, this.clock, this.relation, this.integerConstant);
	}

	
}
