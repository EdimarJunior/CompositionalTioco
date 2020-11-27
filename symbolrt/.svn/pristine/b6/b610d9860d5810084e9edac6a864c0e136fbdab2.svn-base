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


public class MainRemoveMessage {

	public static TIOSTS createTIOSTS(String name){
		TIOSTS tiosts = new TIOSTS(name);
		
		// Variables
		TypedData state = new TypedData("state", Constants.TYPE_INTEGER);
		TypedData choice = new TypedData("choice", Constants.TYPE_INTEGER);
		// Action Parameters
		TypedData messageState = new TypedData("messageState", Constants.TYPE_INTEGER);
		TypedData intCode = new TypedData("intCode", Constants.TYPE_INTEGER);
		// Clocks
		String clock = "clock";
		
		tiosts.addVariable(state);
		tiosts.addVariable(choice);
		
		tiosts.addActionParameter(messageState);
		tiosts.addActionParameter(intCode);
		
		tiosts.addClock(clock);

		
		// Internal Action
		Action init = new Action("Init", Constants.ACTION_INTERNAL);
		
		// Input Actions
		Action goToMessageCenter = new Action("GoToMessageCenter", Constants.ACTION_INPUT);
		Action goToInbox = new Action("GoToInbox", Constants.ACTION_INPUT);
		Action scrollToAMessage = new Action("ScrollToAMessage", Constants.ACTION_INPUT);
		scrollToAMessage.addParameter("messageState");
		Action selectRemoveOption = new Action("SelectRemoveOption", Constants.ACTION_INPUT);
		Action confirmDialog = new Action("ConfirmDialog", Constants.ACTION_INPUT);
		
		Action interrupt = new Action("Interrupt", Constants.ACTION_INPUT);
		interrupt.addParameter(intCode.getName());
		Action sendAlertFromPhone2ToPhone1 = new Action("SendAlertFromPhone2ToPhone1", Constants.ACTION_INPUT);
		Action selectOkOption = new Action("SelectOkOption", Constants.ACTION_INPUT);
		
	
		// Output Actions
		Action allFolderAreDisplayed = new Action("AllFolderAreDisplayed", Constants.ACTION_OUTPUT);
		Action allInboxMessagesAreDisplayed = new Action("AllInboxMessagesAreDisplayed", Constants.ACTION_OUTPUT);
		Action messageIsHighlighted = new Action("MessageIsHighlighted", Constants.ACTION_OUTPUT);
		Action messageRemovedIsDisplayed = new Action("MessageRemovedIsDisplayed", Constants.ACTION_OUTPUT);
		Action blockMessageCannotBeRemovedDialogIsDisplayed = new Action("BlockMessageCannotBeRemovedDialogIsDisplayed", Constants.ACTION_OUTPUT);
		Action messageContentIsDisplayed = new Action("MessageContentIsDisplayed", Constants.ACTION_OUTPUT);
		Action dialogWithAlertIsDisplayed = new Action("DialogWithAlertIsDisplayed", Constants.ACTION_OUTPUT);
		Action backToPreviousApp = new Action("BackToPreviousApp", Constants.ACTION_OUTPUT);
		
		tiosts.addAction(init);

		tiosts.addAction(goToMessageCenter);
		tiosts.addAction(goToInbox);
		tiosts.addAction(scrollToAMessage);
		tiosts.addAction(selectRemoveOption);
		tiosts.addAction(confirmDialog);
		tiosts.addAction(interrupt);
		tiosts.addAction(sendAlertFromPhone2ToPhone1);
		tiosts.addAction(selectOkOption);
		
		tiosts.addAction(allFolderAreDisplayed);
		tiosts.addAction(allInboxMessagesAreDisplayed);
		tiosts.addAction(messageIsHighlighted);
		tiosts.addAction(messageRemovedIsDisplayed);
		tiosts.addAction(blockMessageCannotBeRemovedDialogIsDisplayed);
		tiosts.addAction(messageContentIsDisplayed);
		tiosts.addAction(dialogWithAlertIsDisplayed);
		tiosts.addAction(backToPreviousApp);
		
		// Locations
		Location start = new Location("START");
		Location s1 = new Location("S1");
		Location s2 = new Location("S2");
		Location s3 = new Location("S3");
		Location s4 = new Location("S4");
		Location s5 = new Location("S5");
		Location s6 = new Location("S6");
		Location s7 = new Location("S7");
		Location s8 = new Location("S8");
		Location s9 = new Location("S9");
		Location s10 = new Location("S10");
		Location s11 = new Location("S11");
		Location s12 = new Location("S12");
		Location s13 = new Location("S13");
		Location s14 = new Location("S14");
		Location s15 = new Location("S15");
		Location s16 = new Location("S16");
		
		
		tiosts.addLocation(start);
		tiosts.addLocation(s1);
		tiosts.addLocation(s2);
		tiosts.addLocation(s3);
		tiosts.addLocation(s4);
		tiosts.addLocation(s5);
		tiosts.addLocation(s6);
		tiosts.addLocation(s7);
		tiosts.addLocation(s8);
		tiosts.addLocation(s9);
		tiosts.addLocation(s10);
		tiosts.addLocation(s11);
		tiosts.addLocation(s12);
		tiosts.addLocation(s13);
		tiosts.addLocation(s14);
		tiosts.addLocation(s15);
		tiosts.addLocation(s16);

		
		// Initial Condition
		tiosts.setInitialCondition(Constants.GUARD_TRUE);
		
		// Initial Location
		tiosts.setInitialLocation(start);
		
		// Transitions
		tiosts.createTransition("START", Constants.GUARD_TRUE, Constants.GUARD_TRUE, init, null, null, "S1");
		tiosts.createTransition("S1", Constants.GUARD_TRUE, Constants.GUARD_TRUE, goToMessageCenter, null, null, "S2");
		tiosts.createTransition("S2", Constants.GUARD_TRUE, Constants.GUARD_TRUE, allFolderAreDisplayed, null, null, "S3");
		tiosts.createTransition("S3", Constants.GUARD_TRUE, Constants.GUARD_TRUE, goToInbox, null, null, "S4");
		tiosts.createTransition("S4", Constants.GUARD_TRUE, Constants.GUARD_TRUE, allInboxMessagesAreDisplayed, null, null, "S5");
		tiosts.createTransition("S5", Constants.GUARD_TRUE, Constants.GUARD_TRUE, scrollToAMessage, null, null, "S6");
		tiosts.createTransition("S6", Constants.GUARD_TRUE, Constants.GUARD_TRUE, messageIsHighlighted, "state := messageState", null, "S7");
		tiosts.createTransition("S7", Constants.GUARD_TRUE, Constants.GUARD_TRUE, selectRemoveOption, null, null, "S8");
		tiosts.createTransition("S8", "state = 1", "clock <= 2000", messageRemovedIsDisplayed, null, null, "S9");
		tiosts.createTransition("S8", "state = 2", "clock <= 2000", blockMessageCannotBeRemovedDialogIsDisplayed, null, null, "S10");
		tiosts.createTransition("S10", Constants.GUARD_TRUE, Constants.GUARD_TRUE, confirmDialog, null, null, "S11");
		tiosts.createTransition("S11", Constants.GUARD_TRUE, Constants.GUARD_TRUE, messageContentIsDisplayed, null, null, "S12");
		
		
		tiosts.createTransition("S13", Constants.GUARD_TRUE, Constants.GUARD_TRUE, sendAlertFromPhone2ToPhone1, null, null, "S14");
		tiosts.createTransition("S14", Constants.GUARD_TRUE, Constants.GUARD_TRUE, dialogWithAlertIsDisplayed, null, null, "S15");
		tiosts.createTransition("S15", Constants.GUARD_TRUE, Constants.GUARD_TRUE, selectOkOption, null, null, "S16");
				
		// intCode = 1
		tiosts.createTransition("S3", "intCode = 1 AND choice = 0", Constants.GUARD_TRUE, interrupt, "choice := intCode", null, "S13");
		tiosts.createTransition("S16", "choice = 1", Constants.GUARD_TRUE, backToPreviousApp, null, null, "S3");
		
		// intCode = 2
		tiosts.createTransition("S5", "intCode = 2 AND choice = 0", Constants.GUARD_TRUE, interrupt, "choice := intCode", null, "S13");
		tiosts.createTransition("S16", "choice = 2", Constants.GUARD_TRUE, backToPreviousApp, null, null, "S5");
		
		// intCode = 3
		tiosts.createTransition("S7", "intCode = 3 AND choice = 0", Constants.GUARD_TRUE, interrupt, "choice := intCode", null, "S13");
		tiosts.createTransition("S16", "choice = 3", Constants.GUARD_TRUE, backToPreviousApp, null, null, "S7");
		
		// intCode = 4
		tiosts.createTransition("S9", "intCode = 4 AND choice = 0", Constants.GUARD_TRUE, interrupt, "choice := intCode", null, "S13");
		tiosts.createTransition("S16", "choice = 4", Constants.GUARD_TRUE, backToPreviousApp, null, null, "S9");
		
		// intCode = 5
		tiosts.createTransition("S10", "intCode = 5 AND choice = 0", Constants.GUARD_TRUE, interrupt, "choice := intCode", null, "S13");
		tiosts.createTransition("S16", "choice = 5", Constants.GUARD_TRUE, backToPreviousApp, null, null, "S10");
		
		return tiosts;
	}
	
	
	public static TIOSTS createTP1(){
		TIOSTS tp = new TIOSTS("TP1");
		
		// Variables, parameters, and clocks
		TypedData intCode = new TypedData("intCode", Constants.TYPE_INTEGER);
		tp.addActionParameter(intCode);
		
		// Input, output, and internal actions
		Action init = new Action("Init", Constants.ACTION_INTERNAL);

		Action interrupt = new Action("Interrupt", Constants.ACTION_INPUT);
		interrupt.addParameter(intCode.getName());

		Action messageRemovedIsDisplayed = new Action("MessageRemovedIsDisplayed", Constants.ACTION_OUTPUT);
		
	
		tp.addAction(init);
		tp.addAction(interrupt);
		tp.addAction(messageRemovedIsDisplayed);
		
		
		// Locations
		Location start = new Location("START");
		Location begin = new Location("Begin");
		Location inte = new Location("Int");
		Location accept = new Location("Accept");
		
		tp.addLocation(start);
		tp.addLocation(begin);
		tp.addLocation(inte);
		tp.addLocation(accept);
		
		// Initial Condition
		tp.setInitialCondition(Constants.GUARD_TRUE);
		
		// Initial Location
		tp.setInitialLocation(start);
		
		// Transitions
		tp.createTransition("START", Constants.GUARD_TRUE, Constants.GUARD_TRUE, init, null, null, "Begin");
		tp.createTransition("Begin", "intCode = 2", Constants.GUARD_TRUE, interrupt, null, null, "Int");
		tp.createTransition("Int", Constants.GUARD_TRUE, Constants.GUARD_TRUE, messageRemovedIsDisplayed, null, null, "Accept");
		
		return tp;
	}
	
	
	public static TIOSTS createTP2(){
		TIOSTS tp = new TIOSTS("TP1");
		
		// Variables, parameters, and clocks
		TypedData intCode = new TypedData("intCode", Constants.TYPE_INTEGER);
		tp.addActionParameter(intCode);
		
		// Input, output, and internal actions
		Action init = new Action("Init", Constants.ACTION_INTERNAL);

		Action interrupt = new Action("Interrupt", Constants.ACTION_INPUT);
		interrupt.addParameter(intCode.getName());

		Action messageRemovedIsDisplayed = new Action("MessageRemovedIsDisplayed", Constants.ACTION_OUTPUT);
		
	
		tp.addAction(init);
		tp.addAction(interrupt);
		tp.addAction(messageRemovedIsDisplayed);
		
		
		// Locations
		Location start = new Location("START");
		Location begin = new Location("Begin");
		Location reject = new Location("Reject");
		Location accept = new Location("Accept");
		
		tp.addLocation(start);
		tp.addLocation(begin);
		tp.addLocation(reject);
		tp.addLocation(accept);
		
		// Initial Condition
		tp.setInitialCondition(Constants.GUARD_TRUE);
		
		// Initial Location
		tp.setInitialLocation(start);
		
		// Transitions
		tp.createTransition("START", Constants.GUARD_TRUE, Constants.GUARD_TRUE, init, null, null, "Begin");
		tp.createTransition("Begin", Constants.GUARD_TRUE, Constants.GUARD_TRUE, interrupt, null, null, "Reject");
		tp.createTransition("Begin", Constants.GUARD_TRUE, Constants.GUARD_TRUE, messageRemovedIsDisplayed, null, null, "Accept");
		
		return tp;
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		long initialTime = System.currentTimeMillis();
		
		GraphVisualization tiostsVis = new GraphVisualization();
		
		TIOSTS spec = createTIOSTS("RemoveMessage_IncomingAlert");
		tiostsVis.show(spec);
		
		TIOSTS tp = createTP1();
		//TIOSTS tp = createTP2();
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
