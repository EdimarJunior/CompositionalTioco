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
 * Wilkerson de Lucena Andrade      11/01/2011     Initial version
 */
package br.edu.ufcg.symbolrt.symbolicexecution.base;

import java.util.ArrayList;
import java.util.List;


/**
 * <code>SymbolicExecutionTree</code> Class.<br>
 * This class is used to represent a Zone-Based Symbolic Execution Tree (ZSET). 
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
public class SymbolicExecutionTree {

	
	private String name;
	
	private SymbolicState initialZSES;
	
	private List<SymbolicState> symbolicStates;
	
	
	
	
	public SymbolicExecutionTree(String name, SymbolicState initialZSES) {
		this.name = name;
		this.initialZSES = initialZSES;
		this.symbolicStates = new ArrayList<SymbolicState>();
		this.symbolicStates.add(initialZSES);
	}
	

	public SymbolicExecutionTree(String name) {
		this.name = name;
		this.initialZSES = null;
		this.symbolicStates = new ArrayList<SymbolicState>();
	}

	
	

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}


	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}


	/**
	 * @return the initialZSES
	 */
	public SymbolicState getInitialZSES() {
		return initialZSES;
	}


	/**
	 * @param initialZSES the initialZSES to set
	 */
	public void setInitialZSES(SymbolicState initialZSES) {
		this.initialZSES = initialZSES;
	}


	/**
	 * @return the symbolicStates
	 */
	public List<SymbolicState> getSymbolicStates() {
		return symbolicStates;
	}


	/**
	 * @param symbolicStates the symbolicStates to set
	 */
	public void setSymbolicStates(List<SymbolicState> symbolicStates) {
		this.symbolicStates = symbolicStates;
	}
	
	/**
	 * 
	 * @param symbolicState
	 */
	public void addSymbolicState(SymbolicState symbolicState){
		this.symbolicStates.add(symbolicState);
	}
	
	
	/**
	 * Returns a list containing all accept ZSESs of this ZSET.
	 * @return All accept ZSESs of this ZSET.
	 */
	public List<SymbolicState> getAcceptZSESs(){
		List<SymbolicState> result = new ArrayList<SymbolicState>();
		
		for (SymbolicState zses : this.symbolicStates) {
			if (zses.isAcceptZSES()) {
				result.add(zses);
			}
		}
		
		return result;
	}
	
	
}
