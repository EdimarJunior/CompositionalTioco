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
 * Wilkerson de Lucena Andrade      28/06/2011     Initial version
 * 
 */
package br.edu.ufcg.symbolrt.facade;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.edu.ufcg.symbolrt.algorithms.Completion;
import br.edu.ufcg.symbolrt.algorithms.SynchronousProduct;
import br.edu.ufcg.symbolrt.base.TIOSTS;
import br.edu.ufcg.symbolrt.exception.ClockGuardException;
import br.edu.ufcg.symbolrt.exception.IncompatibleSynchronousProductException;
import br.edu.ufcg.symbolrt.symbolicexecution.algorithms.SymbolicExecution;
import br.edu.ufcg.symbolrt.symbolicexecution.algorithms.TestCaseSelection;
import br.edu.ufcg.symbolrt.symbolicexecution.algorithms.TestTreeTransformation;
import br.edu.ufcg.symbolrt.symbolicexecution.base.SymbolicExecutionTree;
import br.edu.ufcg.symbolrt.symbolicexecution.exception.SymbolicValueNameException;
import br.edu.ufcg.symbolrt.util.Config;
import br.edu.ufcg.symbolrt.util.GraphVisualization;
import br.edu.ufcg.symbolrt.util.PersistenceManager;
import br.edu.ufcg.symbolrt.util.XMLPersistence;

/**
 * <code>SYMBOLRT</code> Class. <br>
 * This class is the facade of the system. It provides a simplified way of generating test cases abstracting all steps of the generation process.
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
public class SYMBOLRT {

	private static SYMBOLRT instance = null;
	
	private SYMBOLRT() {
	}

	
	/**
	 * Method used to obtain an instance of this class.
	 * @return An instance of {@link SYMBOLRT}.
	 */
	public static SYMBOLRT getInstance() {
		if (instance == null) {
			instance = new SYMBOLRT();
		}
		return instance;
	}

	
	/**
	 * Generates test cases from a specification and a test purpose.
	 * @param specification The specification from test cases must be generated.
	 * @param testPurpose The test purpose used to select test cases.
	 * @param showModels A flag used to decide whether figures representing the generated models must be 
	 * shown or not. Only intermediate models are considered by this flag.
	 * @return A list with all generated test cases.
	 */
	public List<TIOSTS> generateTestCases(TIOSTS specification, TIOSTS testPurpose, boolean showModels){
		

		
		// First step: Test Purpose Completion
		Completion completionOperation = Completion.getInstance();
		TIOSTS tpComplete = null;
		try {
			tpComplete = completionOperation.complete(testPurpose);
		} catch (ClockGuardException e) {
			System.out.println(e.getMessage());
		}
		
		completionOperation = null;
		
		//Storing graphic models to free memory space
		if (showModels) {
			List<TIOSTS> intermediateModels = new ArrayList<TIOSTS>();
			intermediateModels.add(specification);
			intermediateModels.add(testPurpose);
			intermediateModels.add(tpComplete);
			show(intermediateModels);
		}
		
		// Second step: Synchronous Product Operation
		SynchronousProduct synchronousOperation = SynchronousProduct.getInstance();
		TIOSTS sp = null;
		try {
			sp = synchronousOperation.synchronousProduct(specification, tpComplete);
		} catch (IncompatibleSynchronousProductException e) {
			System.out.println(e.getMessage());
		}
		
		//Storing graphic models to free memory space
		if (showModels) {
			List<TIOSTS> intermediateModels = new ArrayList<TIOSTS>();
			intermediateModels.add(sp);
			show(intermediateModels);
		}
		
		//Cleaning objects to free memory space	
		synchronousOperation = null;
		specification = null;
		testPurpose = null;
		tpComplete = null;
		System.gc();

		// Third step: Symbolic Execution 
		SymbolicExecution symb = SymbolicExecution.getInstance();
		SymbolicExecutionTree zset = null;
		try {
			zset = symb.execute(sp);
		} catch (SymbolicValueNameException e) {
			System.out.println(e.getMessage());
		}		
		// Forth step: Test Case Selection
		TestCaseSelection tcSelection = TestCaseSelection.getInstance();
		List<SymbolicExecutionTree> testTrees = tcSelection.selectAllTestCases(zset);
		
		//Cleaning objects to free memory space
		symb = null;
		zset = null;
		tcSelection = null;	

		// Fifth step: Test Tree Transformation
		
		List<TIOSTS> testCases = new ArrayList<TIOSTS>();
		
		TestTreeTransformation tt = TestTreeTransformation.getInstance();
		for (SymbolicExecutionTree testTree: testTrees) {
			testCases.add(tt.translateTestTree(testTree, sp));
		}
		
		/*TestTreeTransformation tt = TestTreeTransformation.getInstance();
		int i = 0;
		TIOSTS testCase;
		for (SymbolicExecutionTree testTree: testTrees) {
			testCase = tt.translateTestTree(testTree, sp);
			testCase.setName("testCase"+i);
			testCases.add(testCase);
			show(testCases);
			testCases = new ArrayList<TIOSTS>();
			i++;
		}*/
		
		//Cleaning objects to free memory space
		tt = null;
		sp  = null;
		
		return testCases;
	}
	
	/**
	 * Shows a graphical representation of each TIOSTS model contained in the list. 
	 * @param models The list of TIOSTS models to graphically show.
	 */
	public void show(List<TIOSTS> models) {
		GraphVisualization tiostsVisualization = new GraphVisualization();
		for (TIOSTS tiosts: models) {
			//tiostsVisualization.show(tiosts);		
			tiostsVisualization.save(tiosts);
		}
	}
	
	/**
	 * Stores each TIOSTS model in a file. The folder where all files are stored is
	 * defined in the symbolrt.properties file. 
	 * @param models The list of TIOSTS models to store.
	 */
	public void store(List<TIOSTS> models) {
		// Creates a new folder or empty the existent one.
		String folderPath = Config.getInstance().getTestCasesPath();
		File folder = new File(folderPath);
		if (folder.exists()) {
			for (File file: folder.listFiles()) {
				if (!file.delete()) {
					System.out.println("File \"" + file.getName() + "\" not removed!");
				}
			}
		} else {
			folder.mkdirs();
		}
		
		// Stores all files of the models list.
		PersistenceManager pm = XMLPersistence.getInstance();
		for (TIOSTS tiosts: models) {
			try {
				pm.store(tiosts, folderPath + tiosts.getName());
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}
	}

	
}
