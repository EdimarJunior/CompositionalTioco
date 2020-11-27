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

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import br.edu.ufcg.symbolrt.util.Constants;


/**
 * <code>ClockGuard</code> Class.<br>
 * This class is used to represent a clock guard. A clock guard is defined as a conjunction of simple guards (see {@link SimpleClockGuard}).  
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
public class ClockGuard {
	
	private List<SimpleClockGuard> clockGuard;
	
	// Usar este construtor quando n�o houver guarda de clock
	public ClockGuard(){
		this.clockGuard = new ArrayList<SimpleClockGuard>();
	}
	
	public ClockGuard(String allClockGuard, List<String> allClocks){
		this.clockGuard = new ArrayList<SimpleClockGuard>();
		
		if (!allClockGuard.equals(Constants.GUARD_TRUE)) {
			String[] tokens = allClockGuard.split(" AND ");
			for (String token : tokens) {
				clockGuard.add(createSimpleClockGuard(token.trim(), allClocks));
			}
		}
		
	}
	
	public List<SimpleClockGuard> getClockGuard() {
		return clockGuard;
	}

	public void setClockGuard(List<SimpleClockGuard> clockGuard) {
		this.clockGuard = clockGuard;
	}
	
	public void addSimpleClockGuard(SimpleClockGuard simpleClockGuard){
		this.clockGuard.add(simpleClockGuard);
	}
	

	/**
	 * Verifies if this clock guard is equals to the other object.
	 * @return true if this clock guard is equals to the other object or false otherwise.
	 */
	@Override
	public boolean equals(Object other) {
		if (other == null) return false;
		try {
			ClockGuard otherClockGuard = (ClockGuard) other;
			return this.clockGuard.equals(otherClockGuard.getClockGuard());
		} catch (ClassCastException e) {
			return false;
		}
	}

	
	@Override
	public String toString(){
		StringBuffer result = new StringBuffer();
		if (this.clockGuard.size() == 0) {
			result.append(Constants.GUARD_TRUE);
		} else {
			for (SimpleClockGuard simpleClockGuard : this.clockGuard) {
				result.append(simpleClockGuard.toString() + " " + Constants.GUARD_CONJUNCTION + " ");
			}
			result.delete(result.length()-5, result.length());
		}
		return result.toString();
	}

	private SimpleClockGuard createSimpleClockGuard(String token, List<String> allClocks) {
		String clockName = null;
		String relation = null;
		String integer = null;

		StringTokenizer st = new StringTokenizer(token, "<=>", true);
		int numTokens = st.countTokens();
		switch (numTokens) {
		case 4:
			clockName = st.nextToken().trim();
			relation = st.nextToken().trim();
			relation += st.nextToken().trim();
			integer = st.nextToken().trim();
			break;

		case 3:
			clockName = st.nextToken().trim();
			relation = st.nextToken().trim();
			integer = st.nextToken().trim();
			break;

		default:
			break;
		}
	
		return new SimpleClockGuard(allClocks.indexOf(clockName) + 1, clockName, relation, integer);
	}
	
	public String toDBMFormat(){
		StringBuffer result = new StringBuffer();
		for (SimpleClockGuard simpleClockGuard : this.clockGuard) {
			result.append(simpleClockGuard.toDBMFormat() + "\n");
		}
		return result.toString();
	}
	
	/**
	 * Creates and returns a copy of this object.
	 * @return A clone of this instance.
	 */
	@Override
	public ClockGuard clone(){
		ClockGuard clone = new ClockGuard();
		for (SimpleClockGuard simpleClockGuard : this.clockGuard) {
			clone.addSimpleClockGuard(simpleClockGuard.clone());
		}
		return clone;
	}

	

	
	public static void main(String[] args){
		/*StringTokenizer st = new StringTokenizer("A<=B", " +-/*<=>()", true);
	     while (st.hasMoreTokens()) {
	         String token = st.nextToken();
	         System.out.println(token);
	     }*/
	     ClockGuard c = new ClockGuard();
	     System.out.println(c.toString());
		
		String[] array = "ANDFNX >=    3 AND X < 4".split(" AND ");
		for (int i = 0; i < array.length; i++) {
			System.out.println(array[i].trim());
		}
	     
	     //int i = 1073741823;
	     //System.out.println(i);
	     //System.out.println(Integer.MAX_VALUE);
		
		
		// Verificando o funcionamento do metodo toDBMFormat
		SimpleClockGuard guard1 = new SimpleClockGuard(1, "clock1", "<=", "1");
		SimpleClockGuard guard2 = new SimpleClockGuard(2, "clock2", "<=", "10");
		ClockGuard cg = new ClockGuard();
		cg.addSimpleClockGuard(guard1);
		cg.addSimpleClockGuard(guard2);
		System.out.println(cg.toDBMFormat());
	     
	}

}
