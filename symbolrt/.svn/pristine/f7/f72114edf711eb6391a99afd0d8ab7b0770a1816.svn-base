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


public class TIOSTSPreprocessingCoffeeTest {
	
	private TIOSTS spec;
	private TIOSTS tp;
	private TIOSTS completeTP;
	private TIOSTS syncProduct;
	Completion completionOperation;
	SynchronousProduct synchronousProductOperation;
	
	
	public TIOSTSPreprocessingCoffeeTest() {
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
		TIOSTS tiosts = new TIOSTS("CoffeeMachine");
		
		// Variables
		TypedData price = new TypedData("price", Constants.TYPE_INTEGER);
		tiosts.addVariable(price); // In fact, this is a system parameter
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
		Action coin = new Action("Coin", Constants.ACTION_INPUT);
		coin.addParameter("coin");
		Action cancel = new Action("Cancel", Constants.ACTION_INPUT);
		Action chooseBeverage = new Action("ChooseBeverage", Constants.ACTION_INPUT);
		chooseBeverage.addParameter("drinkRequest");
		Action returnAction = new Action("Return", Constants.ACTION_OUTPUT);
		returnAction.addParameter("moneyBack");
		Action deliver = new Action("Deliver", Constants.ACTION_OUTPUT);
		deliver.addParameter("drinkRequest");

		tiosts.addAction(init);
		tiosts.addAction(coin);
		tiosts.addAction(cancel);
		tiosts.addAction(chooseBeverage);
		tiosts.addAction(returnAction);
		tiosts.addAction(deliver);
		
		// Locations
		Location start = new Location("START");
		tiosts.addLocation(start);
		tiosts.addLocation(new Location("Idle"));
		tiosts.addLocation(new Location("Pay"));
		tiosts.addLocation(new Location("Choose"));
		tiosts.addLocation(new Location("Return"));
		tiosts.addLocation(new Location("Delivery"));
		
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
		tiosts.createTransition("Delivery", "drink = drinkRequest", Constants.GUARD_TRUE, deliver, "total := 0", null, "Idle");
	
		return tiosts;
	}
	
	
	private TIOSTS createTestPurpose(){
		TIOSTS tiosts = new TIOSTS("TP");
		
		// Variables, parameters, and clocks
		TypedData price = new TypedData("price", Constants.TYPE_INTEGER);
		tiosts.addActionParameter(price);
		TypedData total = new TypedData("total", Constants.TYPE_INTEGER);
		tiosts.addActionParameter(total);
		
		// Input, output, and internal actions
		Action init = new Action("Init", Constants.ACTION_INTERNAL);
		Action cancel = new Action("Cancel", Constants.ACTION_INPUT);
		Action returnAction = new Action("Return", Constants.ACTION_OUTPUT);
		returnAction.addParameter("moneyBack");
		Action deliver = new Action("Deliver", Constants.ACTION_OUTPUT);
		deliver.addParameter("drinkRequest");
		
		tiosts.addAction(init);
		tiosts.addAction(cancel);
		tiosts.addAction(returnAction);
		tiosts.addAction(deliver);
		
		// Locations
		Location start = new Location("START");
		tiosts.addLocation(start);
		tiosts.addLocation(new Location("Begin"));
		tiosts.addLocation(new Location("Accept"));
		tiosts.addLocation(new Location("Reject"));
		
		// Initial Location
		tiosts.setInitialLocation(start);

		// Transitions
		tiosts.createTransition("START", Constants.GUARD_TRUE, Constants.GUARD_TRUE, init, null, null, "Begin");
		tiosts.createTransition("Begin", "drinkRequest = TRUE", Constants.GUARD_TRUE, deliver, null, null, "Accept");
		tiosts.createTransition("Begin", Constants.GUARD_TRUE, Constants.GUARD_TRUE, cancel, null, null, "Reject");
		tiosts.createTransition("Begin", "total < price", Constants.GUARD_TRUE, returnAction, null, null, "Reject");
		
		return tiosts;
	}
	
	
	private TIOSTS createCompleteTestPurpose(){
		TIOSTS tiosts = new TIOSTS("TP_complete");
		
		// Variables, parameters, and clocks
		TypedData price = new TypedData("price", Constants.TYPE_INTEGER);
		tiosts.addActionParameter(price);
		TypedData total = new TypedData("total", Constants.TYPE_INTEGER);
		tiosts.addActionParameter(total);
		
		// Input, output, and internal actions
		Action init = new Action("Init", Constants.ACTION_INTERNAL);
		Action cancel = new Action("Cancel", Constants.ACTION_INPUT);
		Action returnAction = new Action("Return", Constants.ACTION_OUTPUT);
		returnAction.addParameter("moneyBack");
		Action deliver = new Action("Deliver", Constants.ACTION_OUTPUT);
		deliver.addParameter("drinkRequest");
		
		tiosts.addAction(init);
		tiosts.addAction(cancel);
		tiosts.addAction(returnAction);
		tiosts.addAction(deliver);
		
		// Locations
		Location start = new Location("START");
		tiosts.addLocation(start);
		tiosts.addLocation(new Location("Accept"));
		tiosts.addLocation(new Location("Begin"));
		tiosts.addLocation(new Location("Reject"));
		
		// Initial Location
		tiosts.setInitialLocation(start);

		// Transitions
		tiosts.createTransition("START", Constants.GUARD_TRUE, Constants.GUARD_TRUE, init, null, null, "Begin");
		tiosts.createTransition("Begin", "drinkRequest = TRUE", Constants.GUARD_TRUE, deliver, null, null, "Accept");
		tiosts.createTransition("Begin", Constants.GUARD_TRUE, Constants.GUARD_TRUE, cancel, null, null, "Reject");
		tiosts.createTransition("Begin", "NOT (total < price)", Constants.GUARD_TRUE, returnAction, null, null, "Begin"); // New transition
		tiosts.createTransition("Begin", "total < price", Constants.GUARD_TRUE, returnAction, null, null, "Reject");
		tiosts.createTransition("Begin", "NOT (drinkRequest = TRUE)", Constants.GUARD_TRUE, deliver, null, null, "Reject"); // New transition
		
		return tiosts;
	}
	
	
	private TIOSTS createSynchronousProduct(){
		TIOSTS tiosts = new TIOSTS("CoffeeMachine_X_TP_complete");
		
		// Variables
		TypedData price = new TypedData("price", Constants.TYPE_INTEGER); 
		tiosts.addVariable(price); // In fact, this is a system parameter
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
		Action coin = new Action("Coin", Constants.ACTION_OUTPUT);
		coin.addParameter("coin");
		Action cancel = new Action("Cancel", Constants.ACTION_OUTPUT);
		Action chooseBeverage = new Action("ChooseBeverage", Constants.ACTION_OUTPUT);
		chooseBeverage.addParameter("drinkRequest");
		Action returnAction = new Action("Return", Constants.ACTION_INPUT);
		returnAction.addParameter("moneyBack");
		Action deliver = new Action("Deliver", Constants.ACTION_INPUT);
		deliver.addParameter("drinkRequest");
		
		tiosts.addInternalAction(init);
		tiosts.addAction(coin);
		tiosts.addAction(cancel);
		tiosts.addAction(chooseBeverage);
		tiosts.addAction(returnAction);
		tiosts.addAction(deliver);
		
		// Locations
		Location start_start = new Location("START_START");
		tiosts.addLocation(start_start);
		tiosts.addLocation(new Location("Idle_Begin"));
		tiosts.addLocation(new Location("Pay_Begin"));
		tiosts.addLocation(new Location("Choose_Begin"));
		tiosts.addLocation(new Location("Delivery_Begin"));
		tiosts.addLocation(new Location("Choose_Reject"));
		tiosts.addLocation(new Location("Idle_Reject"));
		tiosts.addLocation(new Location("Idle_Accept"));
		tiosts.addLocation(new Location("Return_Reject"));
		
		// Initial Location
		tiosts.setInitialLocation(start_start);
		
		// Transitions
		tiosts.createTransition("Idle_Begin", Constants.GUARD_TRUE, Constants.GUARD_TRUE, cancel, null, null, Constants.DEADLINE_LAZY, "Return_Reject");
		tiosts.createTransition("Delivery_Begin", "drink = drinkRequest AND NOT (drinkRequest = TRUE)", Constants.GUARD_TRUE, deliver, "total := 0", null, Constants.DEADLINE_DELAYABLE, "Idle_Reject");
		tiosts.createTransition("START_START", "price > 0", Constants.GUARD_TRUE, init, "total := 0", null, "Idle_Begin");
		tiosts.createTransition("Choose_Begin", "total = price", Constants.GUARD_TRUE, chooseBeverage, "drink := drinkRequest", null, Constants.DEADLINE_LAZY, "Delivery_Begin");
		tiosts.createTransition("Delivery_Begin", "drink = drinkRequest AND drinkRequest = TRUE", Constants.GUARD_TRUE, deliver, "total := 0", null, Constants.DEADLINE_DELAYABLE, "Idle_Accept");
		tiosts.createTransition("Choose_Begin", Constants.GUARD_TRUE, Constants.GUARD_TRUE, cancel, null, null, Constants.DEADLINE_LAZY, "Return_Reject");
		tiosts.createTransition("Pay_Begin", "(total < price) AND (moneyBack = total) AND total < price", Constants.GUARD_TRUE, returnAction, "total := 0", null, Constants.DEADLINE_DELAYABLE, "Idle_Reject");
		tiosts.createTransition("Pay_Begin", "(total >= price) AND (moneyBack = total - price) AND NOT (total < price)", Constants.GUARD_TRUE, returnAction, "total := price", null, Constants.DEADLINE_DELAYABLE, "Choose_Begin");
		tiosts.createTransition("Pay_Begin", "(total < price) AND (moneyBack = total) AND NOT (total < price)", Constants.GUARD_TRUE, returnAction, "total := 0", null, Constants.DEADLINE_DELAYABLE, "Idle_Begin");
		tiosts.createTransition("Idle_Begin", "coin > 0", Constants.GUARD_TRUE, coin, "total := total + coin", null, Constants.DEADLINE_LAZY, "Pay_Begin");
		tiosts.createTransition("Pay_Begin", "(total >= price) AND (moneyBack = total - price) AND total < price", Constants.GUARD_TRUE, returnAction, "total := price", null, Constants.DEADLINE_DELAYABLE, "Choose_Reject");
	
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
			System.out.println(e.getMessage());
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
