package br.edu.ufcg.symbolrt.algorithms;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

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
import br.edu.ufcg.symbolrt.util.Constants;


public class TIOSTSPreprocessingATMTest {

	private TIOSTS spec;
	private TIOSTS tp;
	private TIOSTS completeTP;
	private TIOSTS syncProduct;
	Completion completionOperation;
	SynchronousProduct synchronousProductOperation;
	
	
	public TIOSTSPreprocessingATMTest(){
		this.completionOperation = Completion.getInstance();
		this.synchronousProductOperation = SynchronousProduct.getInstance();
	}
	
	
	@Before
	public void setUp() throws Exception {
		this.spec = createSpec();
		this.tp = createTestPurpose();
		this.completeTP = createCompleteTestPurpose();
		this.syncProduct = createSynchronousProduct();
	}
	
	
	private TIOSTS createSpec(){
		TIOSTS tiosts = new TIOSTS("WithdrawalTransation");
		
		// Variables, parameters, and clocks
		TypedData balance = new TypedData("balance", Constants.TYPE_INTEGER);
		TypedData withdrawalValue = new TypedData("withdrawalValue", Constants.TYPE_INTEGER);
		String clock = "clock";
		
		tiosts.addVariable(balance);
		tiosts.addVariable(withdrawalValue);
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
	
	private TIOSTS createCompleteTestPurpose(){
		TIOSTS tp = new TIOSTS("TP_complete");
		
		// Variables, parameters, and clocks
		
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
		
		tp.createTransition("EnterAmount", Constants.GUARD_TRUE, Constants.GUARD_TRUE, insufficientFunds, null, null, "EnterAmount");
		tp.createTransition("EnterAmount", Constants.GUARD_TRUE, Constants.GUARD_TRUE, dispenseCash, null, null, "EnterAmount");
		
		tp.createTransition("Verify", Constants.GUARD_TRUE, Constants.GUARD_TRUE, dispenseCash, null, null, "Accept");
		tp.createTransition("Verify", Constants.GUARD_TRUE, Constants.GUARD_TRUE, withdrawal, null, null, "Verify");
		tp.createTransition("Verify", Constants.GUARD_TRUE, Constants.GUARD_TRUE, insufficientFunds, null, null, "Reject");
		
		return tp;
	}
	
	
	private TIOSTS createSynchronousProduct(){
		TIOSTS tiosts = new TIOSTS("WithdrawalTransation_X_TP_complete");
		
		// Variables, parameters, and clocks
		TypedData balance = new TypedData("balance", Constants.TYPE_INTEGER);
		TypedData withdrawalValue = new TypedData("withdrawalValue", Constants.TYPE_INTEGER);
		String clock = "clock";
		
		tiosts.addVariable(balance);
		tiosts.addVariable(withdrawalValue);
		tiosts.addClock(clock);
		
		// Input, output, and internal actions
		Action init = new Action("Init", Constants.ACTION_INTERNAL);
		Action withdrawal = new Action("Withdrawal", Constants.ACTION_OUTPUT);
		withdrawal.addParameter("amount");
		Action insufficientFunds = new Action("InsufficientFunds", Constants.ACTION_INPUT);
		insufficientFunds.addParameter("amount");
		Action printBalance = new Action("PrintBalance", Constants.ACTION_INPUT);
		printBalance.addParameter("amount");
		Action dispenseCash = new Action("DispenseCash", Constants.ACTION_INPUT);
		dispenseCash.addParameter("amount");
		
		tiosts.addAction(init);
		tiosts.addAction(withdrawal);
		tiosts.addAction(insufficientFunds);
		tiosts.addAction(printBalance);
		tiosts.addAction(dispenseCash);

		// Locations
		Location start_start = new Location("START_START");
		tiosts.addLocation(start_start);
		tiosts.addLocation(new Location("Idle_EnterAmount"));
		tiosts.addLocation(new Location("Verify_Verify"));
		tiosts.addLocation(new Location("Print_Reject"));
		tiosts.addLocation(new Location("Idle_Accept"));
		
		// Initial Location
		tiosts.setInitialLocation(start_start);
		
		// Transitions
		tiosts.createTransition("START_START", "balance > 0", Constants.GUARD_TRUE, init, null, null, "Idle_EnterAmount");
		tiosts.createTransition("Idle_EnterAmount", "amount > 0", Constants.GUARD_TRUE, withdrawal, "withdrawalValue := amount", "clock := 0", Constants.DEADLINE_LAZY, "Verify_Verify");
		tiosts.createTransition("Verify_Verify", "amount = withdrawalValue AND withdrawalValue <= balance", "clock <= 10", dispenseCash, "balance := balance - withdrawalValue", null, Constants.DEADLINE_DELAYABLE, "Idle_Accept");
		tiosts.createTransition("Verify_Verify", "amount = withdrawalValue AND withdrawalValue > balance", "clock <= 2", insufficientFunds, null, "clock := 0", Constants.DEADLINE_DELAYABLE, "Print_Reject");
		return tiosts;
	}
	

	@Test
	public void testComplete() {
		try {
			assertEquals(this.completeTP, completionOperation.complete(this.tp));
		} catch (ClockGuardException e) {
			fail("The test purpose is valid, then no exception must occurs.");
		}
	}
	
	@Test
	public void testSynchronousProduct01() {
		TIOSTS synchronousProduct = null;
		try {
			synchronousProduct = synchronousProductOperation.synchronousProduct(this.spec, this.completeTP);
		} catch (IncompatibleSynchronousProductException e) {
			fail("The specification is compatible with the defined test purpose, then no exception must occurs.");
		}
		assertEquals(this.syncProduct, synchronousProduct);
	}
	
	@Test
	public void testSynchronousProduct02() {
		TIOSTS synchronousProduct = null;
		try {
			synchronousProduct = synchronousProductOperation.synchronousProduct(this.spec, completionOperation.complete(this.tp));
		} catch (ClockGuardException e) {
			fail("The test purpose is valid, then no exception must occurs."); 
		} catch (IncompatibleSynchronousProductException e) {
			fail("The specification is compatible with the defined test purpose, then no exception must occurs.");
		}
		assertEquals(this.syncProduct, synchronousProduct);
	}


}
