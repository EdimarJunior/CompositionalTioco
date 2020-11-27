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
public class MainSettopbox {

	public static void main(String[] args) {
		//Estudo de caso do Avião
		
		long start = System.currentTimeMillis();
		
		Compiler.compile("./examples/SetTopBox.srt", "Separator");
		TIOSTS tiostsTuner = Compiler.getSpecification("Tuner");
		TIOSTS tiostsSeparator = Compiler.getSpecification("Separator");
		TIOSTS tiostsVideoDecoder = Compiler.getSpecification("VideoDecoder");
		TIOSTS tiostsAudioDecoder = Compiler.getSpecification("AudioDecoder");
		TIOSTS tiostsInterruption = Compiler.getSpecification("InterruptionSetTopBox");		
		
		TIOSTS testPurpose = Compiler.getSpecification("SetTopBoxTP1");
		
		// (((Tuner ; Separator) || VideoDecoder) || AudioDecoder ) /\ Interruption
						
		SequentialComposition seqComposition = SequentialComposition.getInstance();
		//Tuner ; Separator	
		TIOSTS tiostsSeqTunerSeparator = null;
		try {
			tiostsSeqTunerSeparator = seqComposition.sequentialComposition(tiostsTuner, tiostsSeparator);
		} catch (IncompatibleCompositionalOperationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*//(Tuner ; Separator) || VideoDecoder		
		TIOSTS tiostsParVideoDecoder = null;
		
		ParallelComposition parComposition = ParallelComposition.getInstance();
		try {
			tiostsParVideoDecoder = parComposition.parallelComposition(tiostsSeqTunerSeparator, tiostsVideoDecoder);
		} catch (IncompatibleCompositionalOperationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		// (((Tuner ; Separator) || VideoDecoder) || AudioDecoder )
		
		TIOSTS tiostsParAudioDecoder = null;
		try {
			tiostsParAudioDecoder = parComposition.parallelComposition(tiostsParVideoDecoder, tiostsAudioDecoder);
		} catch (IncompatibleCompositionalOperationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		TIOSTS tiostsSetTopBox = null;		
		InterruptionComposition intComposition = InterruptionComposition.getInstance();
		try {
			tiostsSetTopBox = intComposition.interruptionComposition(tiostsParAudioDecoder, tiostsInterruption);
		} catch (IncompatibleCompositionalOperationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		SYMBOLRT symbolrt = SYMBOLRT.getInstance();
		//	List<TIOSTS> testCases = symbolrt.generateTestCases(tiostsSetTopBox, testPurpose, true);
		List<TIOSTS> testCases = symbolrt.generateTestCases(tiostsSeqTunerSeparator, testPurpose, true);
		
		/*testCases.add(tiostsContactsApp);
		testCases.add(tiostsReceiveCallApp);
		testCases.add(tiostsMessageApp);
		testCases.add(tiostsSeqContactsMessage);
		testCases.add(tiostsSeqCallMessage);
		testCases.add(tiostsCellPhone);
		
		showTextual(tiostsCellPhone);
		
		System.out.println("Total de transições: " + tiostsCellPhone.getTransitions().size() );
		System.out.println("Total de locations: " + tiostsCellPhone.getLocations().size());*/
		
		int size = testCases.size();
		
		System.out.print(size + " test case(s) generated");		
		symbolrt.show(testCases);

		long finish = System.currentTimeMillis();
		long result = finish - start;
		System.out.println(" in " + result + " milliseconds.");
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
