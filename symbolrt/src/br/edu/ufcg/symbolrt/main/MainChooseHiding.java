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

import java.util.LinkedList;
import java.util.List;

import br.edu.ufcg.symbolrt.compositions.*;
import br.edu.ufcg.symbolrt.compositions.exceptions.IncompatibleCompositionalOperationException;
import br.edu.ufcg.symbolrt.base.TIOSTS;
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
public class MainChooseHiding {

	public static void main(String[] args) {
		//Estudo de caso do Avião
		
		long start = System.currentTimeMillis();
		
		Compiler.compile("./examples/ChooseHiding.srt", "ChooseHiding");
		TIOSTS tiostsChoose = Compiler.getSpecification("ChooseHiding");			
		TIOSTS testPurpose = Compiler.getSpecification("ChooseHidingTP1");
		
		TIOSTS tiostsHid = null;		
		
		
		HidingOperator hidOperator = HidingOperator.getInstance();
		try {
			tiostsHid = hidOperator.hidingOperator(tiostsChoose, "Cash");
		} catch (IncompatibleCompositionalOperationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
		SYMBOLRT symbolrt = SYMBOLRT.getInstance();
		//List<TIOSTS> testCases = symbolrt.generateTestCases(tiostsHid, testPurpose, true);
		//testCases.add(tiostsChoose);
		List<TIOSTS> testCases = new LinkedList<TIOSTS>();
		testCases.add(tiostsChoose);
		
		symbolrt.show(testCases);

		long finish = System.currentTimeMillis();
		long result = finish - start;
		System.out.println(testCases.size() + " test case(s) generated in " + result + " milliseconds.");
	}	
}
