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
import br.edu.ufcg.symbolrt.compiler.facade.Compiler;
import br.edu.ufcg.symbolrt.facade.SYMBOLRT;


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
public class Main {
	
	public static void main(String[] args) {
		
		String usageMessage = "Usage: symbolrt [OPTION] fileName specificationName testPurposeName\n\n" +
							  "By default, SYMBOLRT compiles the specified file and show test cases for the " +
							  "indicated specification and test purpose.\n\n" +
							  "Options:\n-a	show all intermediate models\n" + 
							  "specificationName = spec1 ; spec2 OR spec1 || spec2";
		
		if ((args.length < 4) && (args.length > 7)) {
			System.out.println(usageMessage);
			System.exit(0);
		}
		
		String fileName = "";
		String specName1 = "";
		String specName2 = "";
		String compositionalOption = "";
		String tpName = "";
		boolean showModels = false;
		
		if (args.length == 4) {			
			//Example: symbolrt ./examples/Mouse.srt Mouse MouseTP1
			fileName = args[1];
			specName1 = args[2];
			tpName = args[3];
		} else if ((args.length == 5) && (args[1].equals("-a"))) {
			//Example: symbolrt -a ./examples/Mouse.srt Mouse MouseTP1
			showModels = true;
			fileName = args[2];
			specName1 = args[3];
			tpName = args[4];
		} else if (args.length == 6){
			//Example: symbolrt ./examples/ChoosePay.srt Choose ; Pay ChoosePayTP1
			//Example: symbolrt ./examples/MouseScreen.srt Mouse || Screen MouseScreenTP1
			fileName = args[1];
			specName1 = args[2];
			compositionalOption = args[3];
			specName2 = args[4];
			tpName = args[5];
			
			if (!compositionalOption.equals(";") && !compositionalOption.equals("||")){
				System.out.println(usageMessage);
				System.exit(0);
			}					
		} else if ((args.length == 7) && (args[1].equals("-a"))){
			//Example: symbolrt -a ./examples/ChoosePay.srt Choose ; Pay ChoosePayTP1
			//Example: symbolrt -a ./examples/MouseScreen.srt Mouse || Screen MouseScreenTP1
			showModels = true;
			fileName = args[2];
			specName1 = args[3];
			compositionalOption = args[4];
			specName2 = args[5];
			tpName = args[6];
			if (!compositionalOption.equals(";") && !compositionalOption.equals("||")){
				System.out.println(usageMessage);
				System.exit(0);
			}					
		} else{
			System.out.println(usageMessage);
			System.exit(0);
		}

		long start = System.currentTimeMillis();
		
		Compiler.compile(fileName, specName1);

		TIOSTS tiostsSpec = Compiler.getSpecification(specName1);		
		
		if (compositionalOption.equals(";")){
			Compiler.compile(fileName, specName2);
			SequentialComposition seqComposition = SequentialComposition.getInstance();
			try {
				tiostsSpec = seqComposition.sequentialComposition(tiostsSpec, Compiler.getSpecification(specName2));
			} catch (IncompatibleCompositionalOperationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}else if (compositionalOption.equals("||")){
			Compiler.compile(fileName, specName2);
			ParallelComposition parComposition = ParallelComposition.getInstance();
			try {
				tiostsSpec = parComposition.parallelComposition(tiostsSpec, Compiler.getSpecification(specName2));
			} catch (IncompatibleCompositionalOperationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
		
		SYMBOLRT symbolrt = SYMBOLRT.getInstance();
		List<TIOSTS> testCases = symbolrt.generateTestCases(tiostsSpec, Compiler.getSpecification(tpName), showModels);

		long finish = System.currentTimeMillis();
		long result = finish - start;
		System.out.println(testCases.size() + " test case(s) generated in " + result + " milliseconds.");
		
		symbolrt.show(testCases);
		//symbolrt.store(testCases);
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
