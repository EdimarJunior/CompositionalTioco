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
import br.edu.ufcg.symbolrt.symbolicexecution.exception.SymbolicValueNameException;
import br.edu.ufcg.symbolrt.util.Constants;
import br.edu.ufcg.symbolrt.util.GraphVisualization;


public class MainAGV {

	public static TIOSTS createTIOSTS(String name){
		TIOSTS tiosts = new TIOSTS(name);
		
		// Variables
		TypedData references = new TypedData("references", Constants.TYPE_INTEGER);
		TypedData path = new TypedData("path", Constants.TYPE_INTEGER);
		TypedData signal = new TypedData("signal", Constants.TYPE_INTEGER);
		TypedData choice = new TypedData("choice", Constants.TYPE_INTEGER);
		// Action Parameters
		TypedData initialRef = new TypedData("initialRef", Constants.TYPE_INTEGER);
		TypedData initialPath = new TypedData("initialPath", Constants.TYPE_INTEGER);
		TypedData ref = new TypedData("ref", Constants.TYPE_INTEGER);
		TypedData newPath = new TypedData("newPath", Constants.TYPE_INTEGER);
		TypedData inputSignal = new TypedData("inputSignal", Constants.TYPE_INTEGER);
		TypedData intCode = new TypedData("intCode", Constants.TYPE_INTEGER);
		// Clocks
		String clock = "clock";
		String periodicClock = "periodicClock";
		String interruptionClock = "interruptionClock";
		
		tiosts.addVariable(references);
		tiosts.addVariable(path);
		tiosts.addVariable(signal);
		tiosts.addVariable(choice);
		
		tiosts.addActionParameter(initialRef);
		tiosts.addActionParameter(initialPath);
		tiosts.addActionParameter(ref);
		tiosts.addActionParameter(newPath);
		tiosts.addActionParameter(inputSignal);
		tiosts.addActionParameter(intCode);
		
		tiosts.addClock(clock);
		tiosts.addClock(periodicClock);
		tiosts.addClock(interruptionClock);

		
		// Internal Action
		Action init = new Action("Init", Constants.ACTION_INTERNAL);
		
		// Input Actions
		Action readFile = new Action("ReadFile", Constants.ACTION_INPUT);
		readFile.addParameter(initialRef.getName());
		readFile.addParameter(initialPath.getName());
		
		Action updateReferences = new Action("UpdateReferences", Constants.ACTION_INPUT);
		updateReferences.addParameter(ref.getName());
		
		Action interrupt = new Action("Interrupt", Constants.ACTION_INPUT);
		interrupt.addParameter(intCode.getName());
		
		Action obstacleDetectedInterrupt = new Action("ObstacleDetectedInterrupt", Constants.ACTION_INPUT);
		
		Action updatePath = new Action("updatePath", Constants.ACTION_INPUT);
		updatePath.addParameter(newPath.getName());
		
		Action selfDiagnosisInterrupt = new Action("SelfDiagnosisInterrupt", Constants.ACTION_INPUT);
		
		// Output Actions
		Action fileSuccessfullyReadMessage = new Action("FileSuccessfullyReadMessage", Constants.ACTION_OUTPUT);
		
		Action referencesSuccessfullyDecodedMessage = new Action("ReferencesSuccessfullyDecodedMessage", Constants.ACTION_OUTPUT);
		
		Action pathSuccessfullyDecodedMessage = new Action("PathSuccessfullyDecodedMessage", Constants.ACTION_OUTPUT);
		
		Action startMovingMessage = new Action("StartMovingMessage", Constants.ACTION_OUTPUT);
		
		Action workDoneMessage = new Action("WorkDoneMessage", Constants.ACTION_OUTPUT);
		
		Action referencesUpdatedMessage = new Action("ReferencesUpdatedMessage", Constants.ACTION_OUTPUT);
		
		Action captureImagesFromReferences = new Action("CaptureImagesFromReferences", Constants.ACTION_OUTPUT);
		
		Action mapUpdatedMessage = new Action("MapUpdatedMessage", Constants.ACTION_OUTPUT);
		
		Action obstacleIdentifiedMessage = new Action("ObstacleIdentifiedMessage", Constants.ACTION_OUTPUT);
		
		Action obstacleOvercomeMessage = new Action("ObstacleOvercomeMessage", Constants.ACTION_OUTPUT);
		
		Action selfDiagnosisPerformedMessage = new Action("SelfDiagnosisPerformedMessage", Constants.ACTION_OUTPUT);
		
		
		tiosts.addAction(init);
		tiosts.addAction(readFile);
		tiosts.addAction(updateReferences);
		tiosts.addAction(interrupt);
		tiosts.addAction(obstacleDetectedInterrupt);
		tiosts.addAction(updatePath);
		tiosts.addAction(selfDiagnosisInterrupt);
		
		tiosts.addAction(fileSuccessfullyReadMessage);
		tiosts.addAction(referencesSuccessfullyDecodedMessage);
		tiosts.addAction(pathSuccessfullyDecodedMessage);
		tiosts.addAction(startMovingMessage);
		tiosts.addAction(workDoneMessage);
		tiosts.addAction(referencesUpdatedMessage);
		tiosts.addAction(captureImagesFromReferences);
		tiosts.addAction(mapUpdatedMessage);
		tiosts.addAction(obstacleIdentifiedMessage);
		tiosts.addAction(obstacleOvercomeMessage);
		tiosts.addAction(selfDiagnosisPerformedMessage);
		
		// Locations
		Location start = new Location("START");
		Location s1 = new Location("S1");
		Location s2 = new Location("S2");
		Location s3 = new Location("S3");
		Location s4 = new Location("S4");
		Location s5 = new Location("S5");
		Location s6 = new Location("S6");
		Location s7 = new Location("S7");
		Location p1 = new Location("P1");
		Location p2 = new Location("P2");
		Location p3 = new Location("P3");
		Location i11 = new Location("I1.1");
		Location i12 = new Location("I1.2");
		Location i13 = new Location("I1.3");
		Location i14 = new Location("I1.4");
		Location i21 = new Location("I2.1");
		Location i22 = new Location("I2.2");
		
		
		tiosts.addLocation(start);
		tiosts.addLocation(s1);
		tiosts.addLocation(s2);
		tiosts.addLocation(s3);
		tiosts.addLocation(s4);
		tiosts.addLocation(s5);
		tiosts.addLocation(s6);
		tiosts.addLocation(s7);
		tiosts.addLocation(p1);
		tiosts.addLocation(p2);
		tiosts.addLocation(p3);
		tiosts.addLocation(i11);
		tiosts.addLocation(i12);
		tiosts.addLocation(i13);
		tiosts.addLocation(i14);
		tiosts.addLocation(i21);
		tiosts.addLocation(i22);

		
		// Initial Condition
		tiosts.setInitialCondition(Constants.GUARD_TRUE);
		
		// Initial Location
		tiosts.setInitialLocation(start);
		
		// Transitions
		tiosts.createTransition("START", Constants.GUARD_TRUE, Constants.GUARD_TRUE, init, null, null, "S1");
		tiosts.createTransition("S1", Constants.GUARD_TRUE, Constants.GUARD_TRUE, readFile, "references := initialRef | path := initialPath", null, "S2");
		tiosts.createTransition("S2", Constants.GUARD_TRUE, Constants.GUARD_TRUE, fileSuccessfullyReadMessage, null, null, "S3");
		tiosts.createTransition("S3", Constants.GUARD_TRUE, Constants.GUARD_TRUE, referencesSuccessfullyDecodedMessage, null, null, "S4");
		tiosts.createTransition("S4", Constants.GUARD_TRUE, Constants.GUARD_TRUE, pathSuccessfullyDecodedMessage, null, null, "S5");
		tiosts.createTransition("S5", Constants.GUARD_TRUE, Constants.GUARD_TRUE, startMovingMessage, null, "periodicClock := 0", "S6");
		tiosts.createTransition("S6", Constants.GUARD_TRUE, Constants.GUARD_TRUE, workDoneMessage, null, null, "S7");
		
		//Loop
		tiosts.createTransition("S6", Constants.GUARD_TRUE, "periodicClock = 2000", updateReferences, "references := ref", null, "P1");
		tiosts.createTransition("P1", Constants.GUARD_TRUE, Constants.GUARD_TRUE, referencesUpdatedMessage, null, "clock := 0", "P2");
		tiosts.createTransition("P2", Constants.GUARD_TRUE, "clock <= 300", captureImagesFromReferences, null, "clock := 0", "P3");
		tiosts.createTransition("P3", Constants.GUARD_TRUE, "clock <= 200", mapUpdatedMessage, null, "periodicClock := 0", "S6");
		
		
		// Obstacle detected interrupt
		tiosts.createTransition("S6", "intCode = 1 AND choice = 0", Constants.GUARD_TRUE, interrupt, "choice := intCode", null, "I1.1");
		tiosts.createTransition("I1.1", Constants.GUARD_TRUE, Constants.GUARD_TRUE, obstacleDetectedInterrupt, null, "interruptionClock := 0", "I1.2");
		tiosts.createTransition("I1.2", Constants.GUARD_TRUE, Constants.GUARD_TRUE, obstacleIdentifiedMessage, null, null, "I1.3");
		tiosts.createTransition("I1.3", Constants.GUARD_TRUE, Constants.GUARD_TRUE, updatePath, "path := newPath", null, "I1.4");
		tiosts.createTransition("I1.4", "choice = 1", "interruptionClock <= 500", obstacleOvercomeMessage, null, null, "S6");
		
		
		// Self diagnosis interrupt
		tiosts.createTransition("S6", "intCode = 2 AND choice = 0", Constants.GUARD_TRUE, interrupt, "choice := intCode", null, "I2.1");
		tiosts.createTransition("I2.1", Constants.GUARD_TRUE, Constants.GUARD_TRUE, selfDiagnosisInterrupt, "signal := inputSignal", "interruptionClock := 0", "I2.2");
		tiosts.createTransition("I2.2", "choice = 2", "interruptionClock <= 20", selfDiagnosisPerformedMessage, null, null, "S6");
		
		return tiosts;
	}

	
	
	
	// Scenario without loops and interruption
	public static TIOSTS createTP1(){
		TIOSTS tp = new TIOSTS("TP1");
		
		// Variables, parameters, and clocks
		TypedData intCode = new TypedData("intCode", Constants.TYPE_INTEGER);
		tp.addActionParameter(intCode);
		TypedData ref = new TypedData("ref", Constants.TYPE_INTEGER);
		tp.addActionParameter(ref);
		
		// Input, output, and internal actions
		Action init = new Action("Init", Constants.ACTION_INTERNAL);

		Action interrupt = new Action("Interrupt", Constants.ACTION_INPUT);
		interrupt.addParameter(intCode.getName());

		Action updateReferences = new Action("UpdateReferences", Constants.ACTION_INPUT);
		updateReferences.addParameter(ref.getName());

		Action workDoneMessage = new Action("WorkDoneMessage", Constants.ACTION_OUTPUT);
		
		tp.addAction(init);
		tp.addAction(interrupt);
		tp.addAction(updateReferences);
		tp.addAction(workDoneMessage);
		
		
		// Locations
		Location start = new Location("START");
		Location tp1 = new Location("TP1");
		Location accept = new Location("Accept");
		Location reject = new Location("Reject");
		
		tp.addLocation(start);
		tp.addLocation(tp1);
		tp.addLocation(accept);
		tp.addLocation(reject);
		
		// Initial Condition
		tp.setInitialCondition(Constants.GUARD_TRUE);
		
		// Initial Location
		tp.setInitialLocation(start);
		
		// Transitions
		tp.createTransition("START", Constants.GUARD_TRUE, Constants.GUARD_TRUE, init, null, null, "TP1");
		tp.createTransition("TP1", Constants.GUARD_TRUE, Constants.GUARD_TRUE, workDoneMessage, null, null, "Accept");
		tp.createTransition("TP1", Constants.GUARD_TRUE, Constants.GUARD_TRUE, interrupt, null, null, "Reject");
		tp.createTransition("TP1", Constants.GUARD_TRUE, Constants.GUARD_TRUE, updateReferences, null, null, "Reject");
		
		return tp;
	}


	
	// Scenario with one loop and no interruption
	public static TIOSTS createTP2(){
		TIOSTS tp = new TIOSTS("TP2");
		
		// Variables, parameters, and clocks
		TypedData intCode = new TypedData("intCode", Constants.TYPE_INTEGER);
		tp.addActionParameter(intCode);
		TypedData ref = new TypedData("ref", Constants.TYPE_INTEGER);
		tp.addActionParameter(ref);
		
		// Input, output, and internal actions
		Action init = new Action("Init", Constants.ACTION_INTERNAL);

		Action interrupt = new Action("Interrupt", Constants.ACTION_INPUT);
		interrupt.addParameter(intCode.getName());

		Action updateReferences = new Action("UpdateReferences", Constants.ACTION_INPUT);
		updateReferences.addParameter(ref.getName());

		Action workDoneMessage = new Action("WorkDoneMessage", Constants.ACTION_OUTPUT);
		
		tp.addAction(init);
		tp.addAction(interrupt);
		tp.addAction(updateReferences);
		tp.addAction(workDoneMessage);
		
		
		// Locations
		Location start = new Location("START");
		Location tp1 = new Location("TP1");
		Location tp2 = new Location("TP2");
		Location accept = new Location("Accept");
		Location reject = new Location("Reject");
		
		tp.addLocation(start);
		tp.addLocation(tp1);
		tp.addLocation(tp2);
		tp.addLocation(accept);
		tp.addLocation(reject);
		
		// Initial Condition
		tp.setInitialCondition(Constants.GUARD_TRUE);
		
		// Initial Location
		tp.setInitialLocation(start);
		
		// Transitions
		tp.createTransition("START", Constants.GUARD_TRUE, Constants.GUARD_TRUE, init, null, null, "TP1");
		tp.createTransition("TP1", Constants.GUARD_TRUE, Constants.GUARD_TRUE, updateReferences, null, null, "TP2");
		tp.createTransition("TP2", Constants.GUARD_TRUE, Constants.GUARD_TRUE, workDoneMessage, null, null, "Accept");
		tp.createTransition("TP1", Constants.GUARD_TRUE, Constants.GUARD_TRUE, interrupt, null, null, "Reject");
		tp.createTransition("TP2", Constants.GUARD_TRUE, Constants.GUARD_TRUE, interrupt, null, null, "Reject");
				
		return tp;
	}


	
	// Scenario with interruption 1 and no loops
	public static TIOSTS createTP3(){
		TIOSTS tp = new TIOSTS("TP3");
		
		// Variables, parameters, and clocks
		TypedData intCode = new TypedData("intCode", Constants.TYPE_INTEGER);
		tp.addActionParameter(intCode);
		TypedData ref = new TypedData("ref", Constants.TYPE_INTEGER);
		tp.addActionParameter(ref);
		
		// Input, output, and internal actions
		Action init = new Action("Init", Constants.ACTION_INTERNAL);

		Action interrupt = new Action("Interrupt", Constants.ACTION_INPUT);
		interrupt.addParameter(intCode.getName());

		Action updateReferences = new Action("UpdateReferences", Constants.ACTION_INPUT);
		updateReferences.addParameter(ref.getName());

		Action workDoneMessage = new Action("WorkDoneMessage", Constants.ACTION_OUTPUT);
		
		tp.addAction(init);
		tp.addAction(interrupt);
		tp.addAction(updateReferences);
		tp.addAction(workDoneMessage);
		
		
		// Locations
		Location start = new Location("START");
		Location tp1 = new Location("TP1");
		Location tp2 = new Location("TP2");
		Location accept = new Location("Accept");
		Location reject = new Location("Reject");
		
		tp.addLocation(start);
		tp.addLocation(tp1);
		tp.addLocation(tp2);
		tp.addLocation(accept);
		tp.addLocation(reject);
		
		// Initial Condition
		tp.setInitialCondition(Constants.GUARD_TRUE);
		
		// Initial Location
		tp.setInitialLocation(start);
		
		// Transitions
		tp.createTransition("START", Constants.GUARD_TRUE, Constants.GUARD_TRUE, init, null, null, "TP1");
		tp.createTransition("TP1", "intCode = 1", Constants.GUARD_TRUE, interrupt, null, null, "TP2");
		tp.createTransition("TP2", Constants.GUARD_TRUE, Constants.GUARD_TRUE, workDoneMessage, null, null, "Accept");
		tp.createTransition("TP2", Constants.GUARD_TRUE, Constants.GUARD_TRUE, updateReferences, null, null, "Reject");
		tp.createTransition("TP1", Constants.GUARD_TRUE, Constants.GUARD_TRUE, updateReferences, null, null, "Reject");
				
		return tp;
	}
	
	
	
	// Scenario with interruption 2 and no loops
	public static TIOSTS createTP4(){
		TIOSTS tp = new TIOSTS("TP4");
		
		// Variables, parameters, and clocks
		TypedData intCode = new TypedData("intCode", Constants.TYPE_INTEGER);
		tp.addActionParameter(intCode);
		TypedData ref = new TypedData("ref", Constants.TYPE_INTEGER);
		tp.addActionParameter(ref);
		
		// Input, output, and internal actions
		Action init = new Action("Init", Constants.ACTION_INTERNAL);

		Action interrupt = new Action("Interrupt", Constants.ACTION_INPUT);
		interrupt.addParameter(intCode.getName());

		Action updateReferences = new Action("UpdateReferences", Constants.ACTION_INPUT);
		updateReferences.addParameter(ref.getName());

		Action workDoneMessage = new Action("WorkDoneMessage", Constants.ACTION_OUTPUT);
		
		tp.addAction(init);
		tp.addAction(interrupt);
		tp.addAction(updateReferences);
		tp.addAction(workDoneMessage);
		
		
		// Locations
		Location start = new Location("START");
		Location tp1 = new Location("TP1");
		Location tp2 = new Location("TP2");
		Location accept = new Location("Accept");
		Location reject = new Location("Reject");
		
		tp.addLocation(start);
		tp.addLocation(tp1);
		tp.addLocation(tp2);
		tp.addLocation(accept);
		tp.addLocation(reject);
		
		// Initial Condition
		tp.setInitialCondition(Constants.GUARD_TRUE);
		
		// Initial Location
		tp.setInitialLocation(start);
		
		// Transitions
		tp.createTransition("START", Constants.GUARD_TRUE, Constants.GUARD_TRUE, init, null, null, "TP1");
		tp.createTransition("TP1", "intCode = 2", Constants.GUARD_TRUE, interrupt, null, null, "TP2");
		tp.createTransition("TP2", Constants.GUARD_TRUE, Constants.GUARD_TRUE, workDoneMessage, null, null, "Accept");
		tp.createTransition("TP2", Constants.GUARD_TRUE, Constants.GUARD_TRUE, updateReferences, null, null, "Reject");
		tp.createTransition("TP1", Constants.GUARD_TRUE, Constants.GUARD_TRUE, updateReferences, null, null, "Reject");
				
		return tp;
	}

	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		long initialTime = System.currentTimeMillis();
		
		GraphVisualization tiostsVis = new GraphVisualization();
		
		TIOSTS spec = createTIOSTS("AGVSystem");
		tiostsVis.show(spec);
		
		TIOSTS tp = createTP1();
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
		
		//System.out.println("Number of TCs: " + testCases.size());
		//printZSET(testCases.get(0));
		
		
		// *********************** Test Tree Transformation ***********************
		TestTreeTransformation tt = TestTreeTransformation.getInstance();
		
		TIOSTS testCase = null;
		for (SymbolicExecutionTree tcTree : testCases){
			testCase = tt.translateTestTree(tcTree, sp);
			tiostsVis.show(testCase);
		}
		
		long finalTime = System.currentTimeMillis();
		System.out.println("Spent time: " + ((finalTime - initialTime) / 1000) + " seconds.");
	}

	
}
