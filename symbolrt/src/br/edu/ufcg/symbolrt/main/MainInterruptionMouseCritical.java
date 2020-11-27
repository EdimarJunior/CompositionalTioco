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
 * Wilkerson de Lucena Andrade      19/04/2012     Initial version
 * 
 */
package br.edu.ufcg.symbolrt.main;

import java.util.List;

import br.edu.ufcg.symbolrt.compositions.*;
import br.edu.ufcg.symbolrt.compositions.exceptions.IncompatibleCompositionalOperationException;
import br.edu.ufcg.symbolrt.base.Location;
import br.edu.ufcg.symbolrt.base.TIOSTS;
import br.edu.ufcg.symbolrt.base.Transition;
import br.edu.ufcg.symbolrt.base.TypedData;
import br.edu.ufcg.symbolrt.compiler.facade.Compiler;
import br.edu.ufcg.symbolrt.facade.SYMBOLRT;
import br.edu.ufcg.symbolrt.util.Constants;

/**
 * <code>Main</code> Class. <br>
 * This class contains the main for executing the SYMBOLRT tool.
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
public class MainInterruptionMouseCritical {

	public static void main(String[] args) {
		//Estudo de caso do Avião
		
		long start = System.currentTimeMillis();
		
		Compiler.compile("./examples/MouseInterruptedCritical.srt", "MouseInterrupted");
		TIOSTS tiostsMouse = Compiler.getSpecification("MouseInterrupted");		
		TIOSTS tiostsInterruption = Compiler.getSpecification("Interruption");		
		TIOSTS testPurpose = Compiler.getSpecification("MouseInterruptedTP3");
		
		TIOSTS tiostsInt = null;		
		
		SequentialComposition seqComposition = SequentialComposition.getInstance();
		try {
			tiostsInt = seqComposition.sequentialComposition(tiostsMouse, tiostsInterruption);
		} catch (IncompatibleCompositionalOperationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//tiostsInt.addVariable(new TypedData("coordinateI", Constants.TYPE_INTEGER));
		
		
		SYMBOLRT symbolrt = SYMBOLRT.getInstance();
		List<TIOSTS> testCases = symbolrt.generateTestCases(tiostsInt, testPurpose, true);
		
		symbolrt.show(testCases);
		
		//showTextual(tiostsInt);

		long finish = System.currentTimeMillis();
		long result = finish - start;
		System.out.println(testCases.size() + " test case(s) generated in " + result + " milliseconds.");
	}
	
	private static void showTextual(TIOSTS tiosts){
		for(Transition t:tiosts.getTransitions()){
			System.out.println(t.getSource() + " " + t.getAction() + " " + t.getTarget());			
		}
		for(Location l:tiosts.getLocations()){
			System.out.println(l.getLabel());
			
		}
		for(String a:tiosts.getActionNames()){
			System.out.println(a);			
		}
		
		System.out.println("Clocks:");		
		for(String a:tiosts.getClocks()){
			System.out.println(a);			
		}
		
		System.out.println("Variables:");		
		for(String a:tiosts.getVariableNames()){
			System.out.println(a);			
		}
		
	}
}
