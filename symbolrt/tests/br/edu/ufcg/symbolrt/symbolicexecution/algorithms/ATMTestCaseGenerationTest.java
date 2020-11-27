package br.edu.ufcg.symbolrt.symbolicexecution.algorithms;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

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



public class ATMTestCaseGenerationTest {
	
	private TIOSTS spec;
	private TIOSTS tp;
	private TIOSTS tiostsTestCase;
	Completion completionOperation;
	SynchronousProduct synchronousProductOperation;
	SymbolicExecution symb;
	TestCaseSelection tcSelection;
	TestTreeTransformation testTree;
	
	
	public ATMTestCaseGenerationTest(){
		this.completionOperation = Completion.getInstance();
		this.synchronousProductOperation = SynchronousProduct.getInstance();
		this.symb = SymbolicExecution.getInstance();
		this.tcSelection = TestCaseSelection.getInstance();
		this.testTree = TestTreeTransformation.getInstance();
	}
	
	
	@Before
	public void setUp() throws Exception {
		this.spec = createSpec();
		this.tp = createTestPurpose();
		this.tiostsTestCase = createTestCase();
	}
	
	
	private TIOSTS createSpec(){
		TIOSTS tiosts = new TIOSTS("WithdrawalTransation");
		
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
		withdrawal.addParameter("amount");
		Action insufficientFunds = new Action("InsufficientFunds", Constants.ACTION_OUTPUT);
		insufficientFunds.addParameter("amount");
		Action printBalance = new Action("PrintBalance", Constants.ACTION_OUTPUT);
		printBalance.addParameter("amount");
		Action dispenseCash = new Action("DispenseCash", Constants.ACTION_OUTPUT);
		dispenseCash.addParameter("amount");
		
		tiosts.addAction(init);
		tiosts.addAction(withdrawal);
		tiosts.addAction(insufficientFunds);
		tiosts.addAction(printBalance);
		tiosts.addAction(dispenseCash);
		
		// Locations
		Location start = new Location("START");
		tiosts.addLocation(start);
		tiosts.addLocation(new Location("Idle"));
		tiosts.addLocation(new Location("Verify"));
		tiosts.addLocation(new Location("Print"));
		
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
	
	
	private TIOSTS createTestPurpose(){
		TIOSTS tp = new TIOSTS("TP");
		
		// Variables, parameters, and clocks
		TypedData amount = new TypedData("amount", Constants.TYPE_INTEGER);
		tp.addActionParameter(amount);
		
		// Input, output, and internal actions
		Action init = new Action("Init", Constants.ACTION_INTERNAL);
		Action withdrawal = new Action("Withdrawal", Constants.ACTION_INPUT);
		withdrawal.addParameter("amount");
		Action insufficientFunds = new Action("InsufficientFunds", Constants.ACTION_OUTPUT);
		insufficientFunds.addParameter("amount");
		Action dispenseCash = new Action("DispenseCash", Constants.ACTION_OUTPUT);
		dispenseCash.addParameter("amount");
		
		tp.addAction(init);
		tp.addAction(withdrawal);
		tp.addAction(insufficientFunds);
		tp.addAction(dispenseCash);
		
		// Locations
		Location start = new Location("START");
		tp.addLocation(start);
		tp.addLocation(new Location("EnterAmount"));
		tp.addLocation(new Location("Verify"));
		tp.addLocation(new Location("Accept"));
		tp.addLocation(new Location("Reject"));
		
		// Initial Location
		tp.setInitialLocation(start);
		
		// Transitions
		tp.createTransition("START", Constants.GUARD_TRUE, Constants.GUARD_TRUE, init, null, null, "EnterAmount");
		tp.createTransition("EnterAmount", Constants.GUARD_TRUE, Constants.GUARD_TRUE, withdrawal, null, null, "Verify");
		tp.createTransition("Verify", Constants.GUARD_TRUE, Constants.GUARD_TRUE, dispenseCash, null, null, "Accept");
		tp.createTransition("Verify", Constants.GUARD_TRUE, Constants.GUARD_TRUE, insufficientFunds, null, null, "Reject");
		
		return tp;
	}
	
	private TIOSTS createTestCase(){
		TIOSTS tiosts = new TIOSTS("TestTree_TC1");

		
		// Variables, parameters, and clocks
		TypedData balance_0 = new TypedData("balance_0", Constants.TYPE_INTEGER);
		TypedData withdrawalValue_0 = new TypedData("withdrawalValue_0", Constants.TYPE_INTEGER);
		TypedData amoutn_1 = new TypedData("amount_1", Constants.TYPE_INTEGER);
		TypedData amoutn_2 = new TypedData("amount_2", Constants.TYPE_INTEGER);
		String clock = "clock";
		
		tiosts.addVariable(balance_0);
		tiosts.addVariable(withdrawalValue_0);
		tiosts.addActionParameter(amoutn_1);
		tiosts.addActionParameter(amoutn_2);
		tiosts.addClock(clock);
		
		
		// Input, output, and internal actions
		Action init = new Action("Init", Constants.ACTION_INTERNAL);
		Action withdrawal = new Action("Withdrawal", Constants.ACTION_OUTPUT);
		withdrawal.addParameter("amount_1");
		Action dispenseCash = new Action("DispenseCash", Constants.ACTION_INPUT);
		dispenseCash.addParameter("amount_2");
		Action insufficientFunds = new Action("InsufficientFunds", Constants.ACTION_INPUT);
		insufficientFunds.addParameter("amount_2");

		
		tiosts.addAction(init);
		tiosts.addAction(withdrawal);
		tiosts.addAction(dispenseCash);
		tiosts.addAction(insufficientFunds);

		
		
		// Locations
		Location start_start = new Location("START_START");
		tiosts.addLocation(start_start);
		tiosts.addLocation(new Location("Idle_EnterAmount"));
		tiosts.addLocation(new Location("Verify_Verify"));
		tiosts.addLocation(new Location("Print_Inconclusive"));
		tiosts.addLocation(new Location("Idle_Accept"));
		
		// Initial Location
		tiosts.setInitialLocation(start_start);
		
		
		// Transitions
		tiosts.createTransition("START_START", "balance_0 > 0", Constants.GUARD_TRUE, init, null, null, Constants.DEADLINE_LAZY, "Idle_EnterAmount");
		tiosts.createTransition("Idle_EnterAmount", "balance_0 > 0 AND amount_1 > 0", Constants.GUARD_TRUE, withdrawal, null, "clock := 0", Constants.DEADLINE_LAZY, "Verify_Verify");
		tiosts.createTransition("Verify_Verify", "balance_0 > 0 AND amount_1 > 0 AND amount_2 = amount_1 AND amount_1 <= balance_0", "clock <= 10", dispenseCash, null, null, Constants.DEADLINE_DELAYABLE, "Idle_Accept");
		tiosts.createTransition("Verify_Verify", "balance_0 > 0 AND amount_1 > 0 AND amount_2 = amount_1 AND amount_1 > balance_0", "clock <= 2", insufficientFunds, null, "clock := 0", Constants.DEADLINE_DELAYABLE, "Print_Inconclusive");
		
		return tiosts;
	}
	
	@Test
	public void testATMTestCaseGeneration() {
		// Synchronous product between specification and completed test purpose
		TIOSTS sp = null;
		try {
			sp = synchronousProductOperation.synchronousProduct(this.spec, completionOperation.complete(this.tp));
		} catch (ClockGuardException e) {
			fail("The test purpose is valid, then no exception must occurs."); 
		} catch (IncompatibleSynchronousProductException e) {
			fail("The specification is compatible with the defined test purpose, then no exception must occurs.");
		}
		
		// Symbolic execution of the synchronous product
		SymbolicExecutionTree zset = null;
		try {
			zset = symb.execute(sp);
		} catch (SymbolicValueNameException e) {
			System.out.println(e.getMessage());
		}
		
		// Test Case Selection
		List<SymbolicExecutionTree> testCases = tcSelection.selectAllTestCases(zset);
	
		assertEquals(1, testCases.size()); // It is expected only one test case
		
		
		// Test Tree Transformation
		TIOSTS testCase = testTree.translateTestTree(testCases.get(0), sp);
		
		assertEquals(this.tiostsTestCase, testCase);
	}


}
