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


public class MainCoffee {

	public static TIOSTS createCoffeeSPEC(){
		TIOSTS tiosts = new TIOSTS("CoffeeMachine");
		
		// Variables
		TypedData price = new TypedData("price", Constants.TYPE_INTEGER);
		tiosts.addVariable(price);
		TypedData total = new TypedData("total", Constants.TYPE_INTEGER);
		tiosts.addVariable(total);
		TypedData drink = new TypedData("drink", Constants.TYPE_BOOLEAN);
		tiosts.addVariable(drink);
		
		// Action parameters
		TypedData coinParameter = new TypedData("coin", Constants.TYPE_INTEGER);
		tiosts.addActionParameter(coinParameter);
		TypedData moneyBackParameter = new TypedData("moneyBack", Constants.TYPE_INTEGER);
		tiosts.addActionParameter(moneyBackParameter);
		TypedData drinkRequestParameter = new TypedData("drinkRequest", Constants.TYPE_BOOLEAN);
		tiosts.addActionParameter(drinkRequestParameter);
		
		
		
		// Input, output, and internal actions
		Action init = new Action("Init", Constants.ACTION_INTERNAL);
		tiosts.addAction(init);
		Action coin = new Action("Coin", Constants.ACTION_INPUT);
		coin.addParameter("coin");
		tiosts.addAction(coin);
		Action cancel = new Action("Cancel", Constants.ACTION_INPUT);
		tiosts.addAction(cancel);
		Action chooseBeverage = new Action("ChooseBeverage", Constants.ACTION_INPUT);
		chooseBeverage.addParameter("drinkRequest");
		tiosts.addAction(chooseBeverage);
		Action returnAction = new Action("Return", Constants.ACTION_OUTPUT);
		returnAction.addParameter("moneyBack");
		tiosts.addAction(returnAction);
		Action deliver = new Action("Deliver", Constants.ACTION_OUTPUT);
		deliver.addParameter("drinkRequest");
		tiosts.addAction(deliver);
		
		
		// Locations
		Location start = new Location("START");
		tiosts.addLocation(start);
		Location idle = new Location("Idle");
		tiosts.addLocation(idle);
		Location pay = new Location("Pay");
		tiosts.addLocation(pay);
		Location choose = new Location("Choose");
		tiosts.addLocation(choose);
		Location returnLocation = new Location("Return");
		tiosts.addLocation(returnLocation);
		Location delivery = new Location("Delivery");
		tiosts.addLocation(delivery);
		
		
		// Initial Condition
		tiosts.setInitialCondition("price > 0");
		
		// Initial Location
		tiosts.setInitialLocation(start);
		
		// Transitions
		tiosts.createTransition("START", "price > 0", Constants.GUARD_TRUE, init, "total := 0", null, "Idle");
		tiosts.createTransition("Idle", "coin > 0", Constants.GUARD_TRUE, coin, "total := total + coin", null, "Pay");
		tiosts.createTransition("Idle", Constants.GUARD_TRUE, Constants.GUARD_TRUE, cancel, null, null, "Return");
		tiosts.createTransition("Return", "moneyBack = total", Constants.GUARD_TRUE, returnAction, "total := 0", null, "Idle");
		tiosts.createTransition("Pay", "(total < price) AND (moneyBack = total)", Constants.GUARD_TRUE, returnAction, "total := 0", null, "Idle");
		tiosts.createTransition("Pay", "(total >= price) AND (moneyBack = total - price)", Constants.GUARD_TRUE, returnAction, "total := price", null, "Choose");
		tiosts.createTransition("Choose", "total = price", Constants.GUARD_TRUE, chooseBeverage, "drink := drinkRequest", null, "Delivery");
		tiosts.createTransition("Choose", Constants.GUARD_TRUE, Constants.GUARD_TRUE, cancel, null, null, "Return");
		tiosts.createTransition("Delivery", "drink <=> drinkRequest", Constants.GUARD_TRUE, deliver, "total := 0", null, "Idle");
	
		
		return tiosts;
	}
	
	
	
	public static TIOSTS createTestPurpose(){
		TIOSTS tiosts = new TIOSTS("TP");
		
		// Variables
		TypedData price = new TypedData("price", Constants.TYPE_INTEGER);
		tiosts.addActionParameter(price);
		TypedData total = new TypedData("total", Constants.TYPE_INTEGER);
		tiosts.addActionParameter(total);
		

		// Input, output, and internal actions
		Action init = new Action("Init", Constants.ACTION_INTERNAL);
		tiosts.addAction(init);
		Action cancel = new Action("Cancel", Constants.ACTION_INPUT);
		tiosts.addAction(cancel);
		Action returnAction = new Action("Return", Constants.ACTION_OUTPUT);
		returnAction.addParameter("moneyBack");
		tiosts.addAction(returnAction);
		Action deliver = new Action("Deliver", Constants.ACTION_OUTPUT);
		deliver.addParameter("drinkRequest");
		tiosts.addAction(deliver);
		
		
		// Locations
		Location start = new Location("START");
		tiosts.addLocation(start);
		Location begin = new Location("Begin");
		tiosts.addLocation(begin);
		Location accept = new Location("Accept");
		tiosts.addLocation(accept);
		Location reject = new Location("Reject");
		tiosts.addLocation(reject);
		
		
		// Initial Condition
		tiosts.setInitialCondition(Constants.GUARD_TRUE);
		
		// Initial Location
		tiosts.setInitialLocation(start);

		// Transitions
		tiosts.createTransition("START", Constants.GUARD_TRUE, Constants.GUARD_TRUE, init, null, null, "Begin");
		tiosts.createTransition("Begin", "drinkRequest <=> TRUE", Constants.GUARD_TRUE, deliver, null, null, "Accept");
		tiosts.createTransition("Begin", Constants.GUARD_TRUE, Constants.GUARD_TRUE, cancel, null, null, "Reject");
		tiosts.createTransition("Begin", "total < price", Constants.GUARD_TRUE, returnAction, null, null, "Reject");
		
		return tiosts;
	}
	
	
	public static void main(String[] args) {
		GraphVisualization tiostsVis = new GraphVisualization();
		
		TIOSTS spec = createCoffeeSPEC();
		tiostsVis.show(spec);
		
		TIOSTS tp = createTestPurpose();
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
