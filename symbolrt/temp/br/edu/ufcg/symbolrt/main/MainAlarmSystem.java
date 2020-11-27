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


public class MainAlarmSystem {

	public static TIOSTS createTIOSTS(String name){
		TIOSTS tiosts = new TIOSTS(name);
		
		// Variables
		TypedData room = new TypedData("room", Constants.TYPE_INTEGER);
		TypedData invasionType = new TypedData("invasionType", Constants.TYPE_INTEGER);
		TypedData choice = new TypedData("choice", Constants.TYPE_INTEGER);
		// Action Parameters
		TypedData roomNumber = new TypedData("roomNumber", Constants.TYPE_INTEGER);
		TypedData windowNumber = new TypedData("windowNumber", Constants.TYPE_INTEGER);
		TypedData doorNumber = new TypedData("doorNumber", Constants.TYPE_INTEGER);
		TypedData movSensorNumber = new TypedData("movSensorNumber", Constants.TYPE_INTEGER);
		TypedData invType = new TypedData("invType", Constants.TYPE_INTEGER);
		TypedData intCode = new TypedData("intCode", Constants.TYPE_INTEGER);
		// Clocks
		String clock = "clock";
		String interruptionClock = "interruptionClock";
		
		tiosts.addVariable(room);
		tiosts.addVariable(invasionType);
		tiosts.addVariable(choice);
		
		tiosts.addActionParameter(roomNumber);
		tiosts.addActionParameter(windowNumber);
		tiosts.addActionParameter(doorNumber);
		tiosts.addActionParameter(movSensorNumber);
		tiosts.addActionParameter(invType);
		tiosts.addActionParameter(intCode);
		
		tiosts.addClock(clock);
		tiosts.addClock(interruptionClock);

		
		// Internal Action
		Action init = new Action("Init", Constants.ACTION_INTERNAL);
		
		// Input Actions
		Action verifyInvasion = new Action("VerifyInvasion", Constants.ACTION_INPUT);
		verifyInvasion.addParameter(roomNumber.getName());
		verifyInvasion.addParameter(invType.getName());
		
		Action interrupt = new Action("Interrupt", Constants.ACTION_INPUT);
		interrupt.addParameter(intCode.getName());
		
		Action powerFailInterrupt = new Action("PowerFailInterrupt", Constants.ACTION_INPUT);
		
		// Output Actions
		Action noInvasionDetected = new Action("NoInvasionDetected", Constants.ACTION_OUTPUT);
		
		Action error = new Action("Error", Constants.ACTION_OUTPUT);
		
		Action triggerWindowBreakingAlarm = new Action("TriggerWindowBreakingAlarm", Constants.ACTION_OUTPUT);
		triggerWindowBreakingAlarm.addParameter(windowNumber.getName());
		
		Action triggerDoorOpeningAlarm = new Action("TriggerDoorOpeningAlarm", Constants.ACTION_OUTPUT);
		triggerDoorOpeningAlarm.addParameter(doorNumber.getName());
		
		Action triggerRoomMovementAlarm = new Action("TriggerRoomMovementAlarm", Constants.ACTION_OUTPUT);
		triggerRoomMovementAlarm.addParameter(movSensorNumber.getName());
		
		Action callPolice = new Action("CallPolice", Constants.ACTION_OUTPUT);
		callPolice.addParameter(roomNumber.getName());
		
		Action turnOnLights = new Action("TurnOnLights", Constants.ACTION_OUTPUT);
		turnOnLights.addParameter(roomNumber.getName());
		
		Action transferToBackupPower = new Action("TransferToBackupPower", Constants.ACTION_OUTPUT);
		
		Action reactivateAlarmSystem = new Action("ReactivateAlarmSystem", Constants.ACTION_OUTPUT);
		
		
		tiosts.addAction(init);
		tiosts.addAction(verifyInvasion);
		tiosts.addAction(interrupt);
		tiosts.addAction(powerFailInterrupt);
		
		tiosts.addAction(noInvasionDetected);
		tiosts.addAction(error);
		tiosts.addAction(triggerWindowBreakingAlarm);
		tiosts.addAction(triggerDoorOpeningAlarm);
		tiosts.addAction(triggerRoomMovementAlarm);
		tiosts.addAction(callPolice);
		tiosts.addAction(turnOnLights);
		tiosts.addAction(transferToBackupPower);
		tiosts.addAction(reactivateAlarmSystem);
		
		// Locations
		Location start = new Location("START");
		Location s1 = new Location("S1");
		Location s2 = new Location("S2");
		Location s3 = new Location("S3");
		Location s4 = new Location("S4");
		Location s5 = new Location("S5");
		Location s6 = new Location("S6");
		Location i1 = new Location("I1");
		Location i2 = new Location("I2");
		Location i3 = new Location("I3");
		
		
		tiosts.addLocation(start);
		tiosts.addLocation(s1);
		tiosts.addLocation(s2);
		tiosts.addLocation(s3);
		tiosts.addLocation(s4);
		tiosts.addLocation(s5);
		tiosts.addLocation(s6);
		tiosts.addLocation(i1);
		tiosts.addLocation(i2);
		tiosts.addLocation(i3);

		
		// Initial Condition
		tiosts.setInitialCondition(Constants.GUARD_TRUE);
		
		// Initial Location
		tiosts.setInitialLocation(start);
		
		// Transitions
		tiosts.createTransition("START", Constants.GUARD_TRUE, Constants.GUARD_TRUE, init, null, null, "S1");
		tiosts.createTransition("S1", Constants.GUARD_TRUE, Constants.GUARD_TRUE, verifyInvasion, "room := roomNumber | invasionType := invType", "clock := 0", "S2");
		tiosts.createTransition("S2", "invasionType = 0", Constants.GUARD_TRUE, noInvasionDetected, null, null, "S1");
		tiosts.createTransition("S2", "invasionType > 280", Constants.GUARD_TRUE, error, null, null, "S6");
		tiosts.createTransition("S2", "invasionType >= 1 AND invasionType <= 50 AND invasionType = windowNumber", "clock <= 500", triggerWindowBreakingAlarm, null, "clock := 0", "S3");
		tiosts.createTransition("S2", "invasionType >= 51 AND invasionType <= 80 AND invasionType = doorNumber", "clock <= 500", triggerDoorOpeningAlarm, null, "clock := 0", "S3");
		tiosts.createTransition("S2", "invasionType >= 81 AND invasionType <= 280 AND invasionType = movSensorNumber", "clock <= 500", triggerRoomMovementAlarm, null, "clock := 0", "S3");
		tiosts.createTransition("S3", "room = roomNumber", "clock <= 4000", callPolice, null, "clock := 0", "S4");
		tiosts.createTransition("S4", "room = roomNumber", "clock <= 500", turnOnLights, null, null, "S5");
		
		tiosts.createTransition("I1", Constants.GUARD_TRUE, Constants.GUARD_TRUE, powerFailInterrupt, null, "interruptionClock := 0", "I2");
		tiosts.createTransition("I2", Constants.GUARD_TRUE, Constants.GUARD_TRUE, transferToBackupPower, null, null, "I3");
		
		// intCode = 1
		tiosts.createTransition("S1", "intCode = 1 AND choice = 0", Constants.GUARD_TRUE, interrupt, "choice := intCode", null, "I1");
		tiosts.createTransition("I3", "choice = 1", "interruptionClock <= 50", reactivateAlarmSystem, null, null, "S1");
		
		// intCode = 2
		tiosts.createTransition("S6", "intCode = 2 AND choice = 0", Constants.GUARD_TRUE, interrupt, "choice := intCode", null, "I1");
		tiosts.createTransition("I3", "choice = 2", "interruptionClock <= 50", reactivateAlarmSystem, null, null, "S6");
		
		// intCode = 3
		tiosts.createTransition("S3", "intCode = 3 AND choice = 0", Constants.GUARD_TRUE, interrupt, "choice := intCode", null, "I1");
		tiosts.createTransition("I3", "choice = 3", "interruptionClock <= 50", reactivateAlarmSystem, null, null, "S3");
		
		// intCode = 4
		tiosts.createTransition("S4", "intCode = 4 AND choice = 0", Constants.GUARD_TRUE, interrupt, "choice := intCode", null, "I1");
		tiosts.createTransition("I3", "choice = 4", "interruptionClock <= 50", reactivateAlarmSystem, null, null, "S4");
		
		// intCode = 5
		tiosts.createTransition("S5", "intCode = 5 AND choice = 0", Constants.GUARD_TRUE, interrupt, "choice := intCode", null, "I1");
		tiosts.createTransition("I3", "choice = 5", "interruptionClock <= 50", reactivateAlarmSystem, null, null, "S5");
		
		return tiosts;
	}
	
	
	
	public static TIOSTS createTP(String name){
		TIOSTS tp = new TIOSTS(name);
		
		// Variables, parameters, and clocks
		TypedData intCode = new TypedData("intCode", Constants.TYPE_INTEGER);
		tp.addActionParameter(intCode);
		TypedData roomNumber = new TypedData("roomNumber", Constants.TYPE_INTEGER);
		tp.addActionParameter(roomNumber);
		
		// Input, output, and internal actions
		Action init = new Action("Init", Constants.ACTION_INTERNAL);

		Action interrupt = new Action("Interrupt", Constants.ACTION_INPUT);
		interrupt.addParameter(intCode.getName());

		Action turnOnLights = new Action("TurnOnLights", Constants.ACTION_OUTPUT);
		turnOnLights.addParameter(roomNumber.getName());		

		Action error = new Action("Error", Constants.ACTION_OUTPUT);
		
		Action noInvasionDetected = new Action("NoInvasionDetected", Constants.ACTION_OUTPUT);
		
		tp.addAction(init);
		tp.addAction(interrupt);
		tp.addAction(turnOnLights);
		tp.addAction(error);
		tp.addAction(noInvasionDetected);
		
		
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
		tp.createTransition("TP1", "intCode = 3", Constants.GUARD_TRUE, interrupt, null, null, "TP2");
		tp.createTransition("TP2", Constants.GUARD_TRUE, Constants.GUARD_TRUE, turnOnLights, null, null, "Accept");
		tp.createTransition("TP1", Constants.GUARD_TRUE, Constants.GUARD_TRUE, error, null, null, "Reject");
		tp.createTransition("TP1", Constants.GUARD_TRUE, Constants.GUARD_TRUE, noInvasionDetected, null, null, "Reject");
		
		return tp;
	}

	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		long initialTime = System.currentTimeMillis();
		
		GraphVisualization tiostsVis = new GraphVisualization();
		
		TIOSTS spec = createTIOSTS("BurglarAlarmSystem");
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
