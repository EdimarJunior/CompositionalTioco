package br.edu.ufcg.symbolrt.base;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import br.edu.ufcg.symbolrt.base.Action;
import br.edu.ufcg.symbolrt.base.Location;
import br.edu.ufcg.symbolrt.base.TIOSTS;
import br.edu.ufcg.symbolrt.base.TypedData;
import br.edu.ufcg.symbolrt.util.Constants;


public class TIOSTSTest {
	
	private TIOSTS tiosts;

	@Before
	public void setUp() throws Exception {
		tiosts = new TIOSTS("TIOSTS");
		
		// Variables, parameters, and clocks
		TypedData variable1 = new TypedData("variable1", Constants.TYPE_INTEGER);
		TypedData variable2 = new TypedData("variable2", Constants.TYPE_INTEGER);
		TypedData actionParameter1 = new TypedData("actionParameter1", Constants.TYPE_INTEGER);
		String clock1 = "clock1";
		String clock2 = "clock2";
		
		tiosts.addVariable(variable1);
		tiosts.addVariable(variable2);
		tiosts.addActionParameter(actionParameter1);
		tiosts.addClock(clock1);
		tiosts.addClock(clock2);
		
		// Input, output, and internal actions
		Action init = new Action("Init", Constants.ACTION_INTERNAL);
		Action inputAction1 = new Action("InputAction1", Constants.ACTION_INPUT);
		inputAction1.addParameter(actionParameter1.getName());
		Action outputAction1 = new Action("OutputAction1", Constants.ACTION_OUTPUT);
		outputAction1.addParameter(actionParameter1.getName());
		Action outputAction2 = new Action("OutputAction2", Constants.ACTION_OUTPUT);
		outputAction2.addParameter(actionParameter1.getName());
		
		tiosts.addAction(init);
		tiosts.addAction(inputAction1);
		tiosts.addAction(outputAction1);
		tiosts.addAction(outputAction2);
		
		// Locations
		Location start = new Location("START");
		Location location1 = new Location("Location1");
		Location location2 = new Location("Location2");
		
		tiosts.addLocation(start);
		tiosts.addLocation(location1);
		tiosts.addLocation(location2);
		
		// Initial Condition
		tiosts.setInitialCondition("variable1 > 0");
		
		// Initial Location
		tiosts.setInitialLocation(start);
		
		// Transitions
		tiosts.createTransition("START", "variable1 > 0", Constants.GUARD_TRUE, init, null, null, "Location1");
		tiosts.createTransition("Location1", "actionParameter1 > 0", Constants.GUARD_TRUE, inputAction1, "variable2 := actionParameter1", "clock1 := 0", "Location2");
		tiosts.createTransition("Location2", "actionParameter1 = variable2 AND variable2 <= variable1", "clock1 <= 10", outputAction2, "variable1 := variable1 - variable2", null, "Location1");
		tiosts.createTransition("Location2", "actionParameter1 = variable2 AND variable2 > variable1", "clock1 <= 2", outputAction1, null, "clock1 := 0", "Location1");
	}
	
	

	@Test
	public void testGetClockNumber() {
		assertEquals("Test with an inexistent clock", 0, tiosts.getClockId("clock"));
		assertEquals("Test with the first clock", 1, tiosts.getClockId("clock1"));
		assertEquals("Test with the second clock", 2, tiosts.getClockId("clock2"));
	}

}
