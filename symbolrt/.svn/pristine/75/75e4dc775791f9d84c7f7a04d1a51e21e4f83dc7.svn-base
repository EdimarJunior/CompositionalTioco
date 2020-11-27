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
 * Wilkerson de Lucena Andrade      08/01/2011     Initial version
 */
package br.edu.ufcg.symbolrt.symbolicexecution.base;

import br.edu.ufcg.symbolrt.base.ClockGuard;
import br.edu.ufcg.symbolrt.util.Constants;



/**
 * <code>SymbolicTransition</code> Class.<br>
 * This class is used to represent a transition connecting two Zone-Based Symbolic Extended States. 
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
public class SymbolicTransition {

	
	// **************************** Attributes ****************************
	
	/**
	 * The origin state of the transition.
	 */
	private SymbolicState sourceZSES;
	
	/**
	 * The symbolic action of the transition.
	 */
	private SymbolicAction symbolicAction;
	
	/**
	 * The destination state of the transition. 
	 */
	private SymbolicState targetZSES;
	
	
	// Attributes important in test tree transformation 
	
	/**
	 * The clock guard of the transition.
	 */
	private ClockGuard clockGuard;
	
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
	
	
	
	
	// **************************** Constructors ****************************
	
	/**
	 * Creates a new symbolic transition with the following specified parameters.
	 * @param sourceZSES The origin state of the transition.
	 * @param symbolicAction The symbolic action of the transition.
	 * @param targetZSES The destination state of the transition. 
	 */
	public SymbolicTransition(SymbolicState sourceZSES, SymbolicAction symbolicAction, SymbolicState targetZSES) {
		this.sourceZSES = sourceZSES;
		this.symbolicAction = symbolicAction;
		this.targetZSES = targetZSES;
		this.clockGuard = new ClockGuard();
		this.clockAssignments = null;
		this.deadline = Constants.DEADLINE_LAZY;
	}
	
	
	/**
	 * Creates a new symbolic transition with the following specified parameters.
	 * @param sourceZSES The origin state of the transition.
	 * @param symbolicAction The symbolic action of the transition.
	 * @param targetZSES The destination state of the transition. 
	 */
	public SymbolicTransition(SymbolicState sourceZSES, SymbolicAction symbolicAction, ClockGuard clockGuard, String clockAssignments, String deadline, SymbolicState targetZSES) {
		this.sourceZSES = sourceZSES;
		this.symbolicAction = symbolicAction;
		this.clockGuard = clockGuard;
		this.clockAssignments = clockAssignments;
		this.deadline = deadline;
		this.targetZSES = targetZSES;
	}



	
	// **************************** Methods ****************************

	/**
	 * Returns the origin state of this transition.
	 * @return The origin ZSES of this transition.
	 */
	public SymbolicState getSourceZSES() {
		return sourceZSES;
	}


	/**
	 * Modifies the origin state of this transition.
	 * @param sourceZSES The new ZSES to set.
	 */
	public void setSourceZSES(SymbolicState sourceZSES) {
		this.sourceZSES = sourceZSES;
	}


	/**
	 * Returns the symbolic action of this transition.
	 * @return The symbolic action of this transition.
	 */
	public SymbolicAction getSymbolicAction() {
		return symbolicAction;
	}


	/**
	 * Modifies the symbolic action of this transition.
	 * @param symbolicAction The new symbolic action to set.
	 */
	public void setSymbolicAction(SymbolicAction symbolicAction) {
		this.symbolicAction = symbolicAction;
	}


	/**
	 * Returns the destination state of this transition.
	 * @return The destination ZSES of this transition.
	 */
	public SymbolicState getTargetZSES() {
		return targetZSES;
	}


	/**
	 * Modifies the destination ZSES of this transition.
	 * @param targetZSES The new ZSES to set.
	 */
	public void setTargetZSES(SymbolicState targetZSES) {
		this.targetZSES = targetZSES;
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
	 * Verifies if this symbolic transition is equals to the other object.
	 * @return true if this symbolic transition is equals to the other object or false otherwise.
	 */
	@Override
	public boolean equals(Object other) {
		if (other == null) return false;
		try {
			SymbolicTransition otherSymbolicTransition = (SymbolicTransition) other;
			return this.sourceZSES.equals(otherSymbolicTransition.getSourceZSES()) && this.symbolicAction.equals(otherSymbolicTransition.getSymbolicAction())
					&& this.targetZSES.equals(otherSymbolicTransition.getTargetZSES());
		} catch (ClassCastException e) {
			return false;
		}
	}
	

	/**
	 * Returns a string representing this symbolic transition.
	 * @return A string representing this symbolic transition.
	 */
	@Override
	public String toString() {
		return this.sourceZSES.getLocation().getLabel() + "#" + this.symbolicAction.toString() + "#" + this.targetZSES.getLocation().getLabel();
	}
	
	
}
