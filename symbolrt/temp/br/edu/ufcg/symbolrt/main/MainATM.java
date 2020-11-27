package br.edu.ufcg.symbolrt.main;

import br.edu.ufcg.symbolrt.base.Action;
import br.edu.ufcg.symbolrt.base.Location;
import br.edu.ufcg.symbolrt.base.TIOSTS;
import br.edu.ufcg.symbolrt.base.TypedData;
import br.edu.ufcg.symbolrt.facade.SYMBOLRT;
import br.edu.ufcg.symbolrt.symbolicexecution.base.SymbolicExecutionTree;
import br.edu.ufcg.symbolrt.symbolicexecution.base.SymbolicState;
import br.edu.ufcg.symbolrt.symbolicexecution.base.SymbolicTransition;
import br.edu.ufcg.symbolrt.util.Constants;

public class MainATM {

	public static TIOSTS createTIOSTS(){
		TIOSTS tiosts = new TIOSTS("WithdrawalTransation1");
		
		// Variables, parameters, and clocks
		TypedData balance = new TypedData("balance", Constants.TYPE_INTEGER);
		TypedData withdrawalValue = new TypedData("withdrawalValue", Constants.TYPE_INTEGER);
		TypedData amount = new TypedData("amount", Constants.TYPE_INTEGER);
		String clock = "clock";
		
		tiosts.addVariable(balance);
		tiosts.addVariable(withdrawalValue);
		tiosts.addActionParameter(amount);
		tiosts.addClock(clock);
		
		// Input, output, and internal actions
		Action init = new Action("Init", Constants.ACTION_INTERNAL);
		Action withdrawal = new Action("Withdrawal", Constants.ACTION_INPUT);
		withdrawal.addParameter(amount.getName());
		Action insufficientFunds = new Action("InsufficientFunds", Constants.ACTION_OUTPUT);
		insufficientFunds.addParameter(amount.getName());
		Action printBalance = new Action("PrintBalance", Constants.ACTION_OUTPUT);
		printBalance.addParameter(amount.getName());
		Action dispenseCash = new Action("DispenseCash", Constants.ACTION_OUTPUT);
		dispenseCash.addParameter(amount.getName());
		
		tiosts.addAction(init);
		tiosts.addAction(withdrawal);
		tiosts.addAction(insufficientFunds);
		tiosts.addAction(printBalance);
		tiosts.addAction(dispenseCash);
		
		// Locations
		Location start = new Location("START");
		Location idle = new Location("Idle");
		Location verify = new Location("Verify");
		Location print = new Location("Print");
		
		tiosts.addLocation(start);
		tiosts.addLocation(idle);
		tiosts.addLocation(verify);
		tiosts.addLocation(print);
		
		// Initial Condition
		tiosts.setInitialCondition("balance > 0");
		
		// Initial Location
		tiosts.setInitialLocation(start);
		
		// Transitions
		tiosts.createTransition("START", "balance > 0", Constants.GUARD_TRUE, init, null, null, "Idle");
		tiosts.createTransition("Idle", "amount > 0", Constants.GUARD_TRUE, withdrawal, "withdrawalValue := amount", "clock := 0", "Verify");
		tiosts.createTransition("Verify", "amount = withdrawalValue AND withdrawalValue <= balance", "clock <= 10", dispenseCash, "balance := balance - withdrawalValue", null, "Idle");
		tiosts.createTransition("Verify", "amount = withdrawalValue AND withdrawalValue > balance", "clock <= 2", insufficientFunds, null, "clock := 0", "Print");
		tiosts.createTransition("Print", "amount = balance", "clock <= 5", printBalance, null, null, "Idle");
		return tiosts;
	}
	
	
	
	
	
	public static TIOSTS createTP(){
		TIOSTS tp = new TIOSTS("TP1");
		
		// Variables, parameters, and clocks
		TypedData amount = new TypedData("amount", Constants.TYPE_INTEGER);
		tp.addActionParameter(amount);
		
		// Input, output, and internal actions
		Action init = new Action("Init", Constants.ACTION_INTERNAL);
		Action withdrawal = new Action("Withdrawal", Constants.ACTION_INPUT);
		withdrawal.addParameter(amount.getName());
		Action insufficientFunds = new Action("InsufficientFunds", Constants.ACTION_OUTPUT);
		insufficientFunds.addParameter(amount.getName());
		Action dispenseCash = new Action("DispenseCash", Constants.ACTION_OUTPUT);
		dispenseCash.addParameter(amount.getName());
		
		tp.addAction(init);
		tp.addAction(withdrawal);
		tp.addAction(insufficientFunds);
		tp.addAction(dispenseCash);
		
		
		// Locations
		Location start = new Location("START");
		Location enterAmount = new Location("EnterAmount");
		Location verify = new Location("Verify");
		Location accept = new Location("Accept");
		Location reject = new Location("Reject");
		
		tp.addLocation(start);
		tp.addLocation(enterAmount);
		tp.addLocation(verify);
		tp.addLocation(accept);
		tp.addLocation(reject);
		
		// Initial Condition
		tp.setInitialCondition(Constants.GUARD_TRUE);
		
		// Initial Location
		tp.setInitialLocation(start);
		
		// Transitions
		tp.createTransition("START", Constants.GUARD_TRUE, Constants.GUARD_TRUE, init, null, null, "EnterAmount");
		tp.createTransition("EnterAmount", Constants.GUARD_TRUE, Constants.GUARD_TRUE, withdrawal, null, null, "Verify");
		tp.createTransition("Verify", Constants.GUARD_TRUE, Constants.GUARD_TRUE, dispenseCash, null, null, "Accept");
		tp.createTransition("Verify", Constants.GUARD_TRUE, Constants.GUARD_TRUE, insufficientFunds, null, null, "Reject");
		
		return tp;
	}
	
	
	
	public static void main(String[] args) {
		
		SYMBOLRT.getInstance().generateTestCases(createTIOSTS(), createTP(), true);
		
		/*GraphVisualization tiostsVis = new GraphVisualization();
		
		TIOSTS spec = createTIOSTS("WithdrawalTransation1");
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
		//tiostsVis.save(createTIOSTS("WithdrawalTransation"));
		
		
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
		*/

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
