package br.edu.ufcg.symbolrt.main;

import java.util.List;

import br.edu.ufcg.symbolrt.algorithms.Completion;
import br.edu.ufcg.symbolrt.algorithms.SynchronousProduct;
import br.edu.ufcg.symbolrt.base.Action;
import br.edu.ufcg.symbolrt.base.Location;
import br.edu.ufcg.symbolrt.base.TIOSTS;
import br.edu.ufcg.symbolrt.base.TypedData;
import br.edu.ufcg.symbolrt.exception.ClockGuardException;
import br.edu.ufcg.symbolrt.exception.IncompatibleSynchronousProductException;
import br.edu.ufcg.symbolrt.symbolicexecution.algorithms.SymbolicExecution;
import br.edu.ufcg.symbolrt.symbolicexecution.algorithms.TestCaseSelection;
import br.edu.ufcg.symbolrt.symbolicexecution.algorithms.TestTreeTransformation;
import br.edu.ufcg.symbolrt.symbolicexecution.base.SymbolicExecutionTree;
import br.edu.ufcg.symbolrt.symbolicexecution.base.SymbolicState;
import br.edu.ufcg.symbolrt.symbolicexecution.base.SymbolicTransition;
import br.edu.ufcg.symbolrt.symbolicexecution.exception.SymbolicValueNameException;
import br.edu.ufcg.symbolrt.util.Constants;
import br.edu.ufcg.symbolrt.util.GraphVisualization;


public class MainInterruption {
	
	public static TIOSTS createTIOSTS(String name){
		TIOSTS tiosts = new TIOSTS(name);
		
		// Variables, parameters, and clocks
		TypedData choice = new TypedData("choice", Constants.TYPE_INTEGER);
		TypedData intCode = new TypedData("intCode", Constants.TYPE_INTEGER);
		tiosts.addVariable(choice);
		tiosts.addActionParameter(intCode);

		
		// Input, output, and internal actions
		Action init = new Action("Init", Constants.ACTION_INTERNAL);
		Action input01 = new Action("Input01", Constants.ACTION_INPUT);
		Action output01 = new Action("Output01", Constants.ACTION_OUTPUT);
		Action input02 = new Action("Input02", Constants.ACTION_INPUT);
		Action output02 = new Action("Output02", Constants.ACTION_OUTPUT);
		Action input03 = new Action("Input03", Constants.ACTION_INPUT);
		Action output03 = new Action("Output03", Constants.ACTION_OUTPUT);
		Action interrupt = new Action("Interrupt", Constants.ACTION_INPUT);
		interrupt.addParameter(intCode.getName());
		Action back = new Action("Back", Constants.ACTION_OUTPUT);
		
		tiosts.addAction(init);
		tiosts.addAction(input01);
		tiosts.addAction(output01);
		tiosts.addAction(input02);
		tiosts.addAction(output02);
		tiosts.addAction(input03);
		tiosts.addAction(output03);
		tiosts.addAction(interrupt);
		tiosts.addAction(back);

		
		// Locations
		Location start = new Location("START");
		Location s00 = new Location("S00");
		Location s01 = new Location("S01");
		Location s02 = new Location("S02");
		Location s03 = new Location("S03");
		Location s04 = new Location("S04");
		Location s05 = new Location("S05");
		Location s06 = new Location("S06");
		Location i01 = new Location("I01");
		
		tiosts.addLocation(start);
		tiosts.addLocation(s00);
		tiosts.addLocation(s01);
		tiosts.addLocation(s02);
		tiosts.addLocation(s03);
		tiosts.addLocation(s04);
		tiosts.addLocation(s05);
		tiosts.addLocation(s06);
		tiosts.addLocation(i01);
		
		// Initial Condition
		tiosts.setInitialCondition(Constants.GUARD_TRUE);
		
		// Initial Location
		tiosts.setInitialLocation(start);
		
		// Transitions
		tiosts.createTransition("START", Constants.GUARD_TRUE, Constants.GUARD_TRUE, init, null, null, "S00");
		tiosts.createTransition("S00", Constants.GUARD_TRUE, Constants.GUARD_TRUE, input01, null, null, "S01");
		tiosts.createTransition("S01", Constants.GUARD_TRUE, Constants.GUARD_TRUE, output01, null, null, "S02");
		tiosts.createTransition("S02", Constants.GUARD_TRUE, Constants.GUARD_TRUE, input02, null, null, "S03");
		tiosts.createTransition("S03", Constants.GUARD_TRUE, Constants.GUARD_TRUE, output02, null, null, "S04");
		tiosts.createTransition("S04", Constants.GUARD_TRUE, Constants.GUARD_TRUE, input03, null, null, "S05");
		tiosts.createTransition("S05", Constants.GUARD_TRUE, Constants.GUARD_TRUE, output03, null, null, "S06");
		
		// Interruption transitions
		tiosts.createTransition("S02", "intCode = 1 AND choice = 0", Constants.GUARD_TRUE, interrupt, "choice := intCode", null, "I01");
		tiosts.createTransition("I01", "choice = 1", Constants.GUARD_TRUE, back, null, null, "S02");
		
		tiosts.createTransition("S04", "intCode = 2 AND choice = 0", Constants.GUARD_TRUE, interrupt, "choice := intCode", null, "I01");
		tiosts.createTransition("I01", "choice = 2", Constants.GUARD_TRUE, back, null, null, "S04");
		
		return tiosts;
	}
	
	
	public static TIOSTS createTP(String name){
		TIOSTS tp = new TIOSTS(name);
		
		// Variables, parameters, and clocks
		TypedData intCode = new TypedData("intCode", Constants.TYPE_INTEGER);
		tp.addActionParameter(intCode);

		
		// Input, output, and internal actions
		Action init = new Action("Init", Constants.ACTION_INTERNAL);
		Action output03 = new Action("Output03", Constants.ACTION_OUTPUT);
		Action interrupt = new Action("Interrupt", Constants.ACTION_INPUT);
		interrupt.addParameter(intCode.getName());
		
		tp.addAction(init);
		tp.addAction(interrupt);
		tp.addAction(output03);
		
		
		// Locations
		Location start = new Location("START");
		Location tp00 = new Location("TP00");
		Location tp01 = new Location("TP01");
		Location accept = new Location("Accept");

		
		tp.addLocation(start);
		tp.addLocation(tp00);
		tp.addLocation(tp01);
		tp.addLocation(accept);
		
		// Initial Condition
		tp.setInitialCondition(Constants.GUARD_TRUE);
		
		// Initial Location
		tp.setInitialLocation(start);
		
		// Transitions
		tp.createTransition("START", Constants.GUARD_TRUE, Constants.GUARD_TRUE, init, null, null, "TP00");
		tp.createTransition("TP00", "intCode = 1", Constants.GUARD_TRUE, interrupt, null, null, "TP01");
		tp.createTransition("TP01", Constants.GUARD_TRUE, Constants.GUARD_TRUE, output03, null, null, "Accept");

		
		return tp;
	}
	
	
	public static void main(String[] args) {
		
		
		GraphVisualization tiostsVis = new GraphVisualization();
		
		TIOSTS spec = createTIOSTS("Interruption");
		tiostsVis.show(spec);
		
		TIOSTS tp = createTP("TP1");
		tiostsVis.show(tp);

		Completion completionOperation = Completion.getInstance();
		TIOSTS tpComplete = null;
		try {
			tpComplete = completionOperation.complete(tp);
		} catch (ClockGuardException e) {
			System.out.println(e.getMessage());
		}
		tiostsVis.show(tpComplete);
		
		SynchronousProduct synchronousOperation = SynchronousProduct.getInstance();
		TIOSTS sp = null;
		try {
			sp = synchronousOperation.synchronousProduct(spec, tpComplete);
		} catch (IncompatibleSynchronousProductException e) {
			System.out.println(e.getMessage());
		}
		tiostsVis.show(sp);
		
		// *********************** Symbolic Execution *********************** 
		SymbolicExecution symb = SymbolicExecution.getInstance();
		SymbolicExecutionTree zset = null;
		try {
			zset = symb.execute(sp);
		} catch (SymbolicValueNameException e) {
			System.out.println(e.getMessage());
		}

		
		
		
		// *********************** Test Case Selection ***********************
		TestCaseSelection tc = TestCaseSelection.getInstance();
		List<SymbolicExecutionTree> testCases = tc.selectAllTestCases(zset);
		
		System.out.println("Number of TCs: " + testCases.size());
		printZSET(testCases.get(0));
		
		
		
		
		// *********************** Test Tree Transformation ***********************
		TestTreeTransformation tt = TestTreeTransformation.getInstance();
		TIOSTS testCase = tt.translateTestTree(testCases.get(0), sp);
		tiostsVis.show(testCase);

	}
	
	
	public static void printZSET(SymbolicExecutionTree zset) {
		System.out.println("*********************** States ***********************");
		for (SymbolicState zses : zset.getSymbolicStates()) {
			System.out.println(zses.toString() + "\n\n");
		}
		
		System.out.println("*********************** Transitions ***********************");
		for (SymbolicState zses : zset.getSymbolicStates()) {
			for (SymbolicTransition t : zses.getChildrenTransitions()) {
				System.out.println(t.toString() + "\n");
			}
		}
	}




}
