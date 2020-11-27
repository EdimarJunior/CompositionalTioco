package br.edu.ufcg.symbolrt.base;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import br.edu.ufcg.symbolrt.base.ClockGuard;
import br.edu.ufcg.symbolrt.base.SimpleClockGuard;
import br.edu.ufcg.symbolrt.util.Constants;


public class ClockGuardTest {
	
	ClockGuard clockGuard1; // Empty clock guard
	ClockGuard clockGuard2; // Only one constraint
	ClockGuard clockGuard3; // With more than one constraint
	List<String> allClocks; 
	
	SimpleClockGuard simpleClockGuard1;
	SimpleClockGuard simpleClockGuard2;

	@Before
	public void setUp() throws Exception {
		allClocks = new ArrayList<String>();
		allClocks.add("clock1");
		allClocks.add("clock2");
		allClocks.add("clock3");
		
		clockGuard1 = new ClockGuard();
		clockGuard2 = new ClockGuard("clock1 <=     5", allClocks);
		clockGuard3 = new ClockGuard("clock1 <= 5 AND clock2 > 4 AND clock3 >= 10", allClocks);
		
		simpleClockGuard1 = new SimpleClockGuard(1, "clock1", "<=", "5");
		simpleClockGuard2 = new SimpleClockGuard(2, "clock2", ">", "4");
	}

	@Test
	public void testClockGuard() {
		assertEquals(simpleClockGuard1, clockGuard2.getClockGuard().get(0));
		assertEquals(simpleClockGuard2, clockGuard3.getClockGuard().get(1));
	}
	

	@Test
	public void testToString() {
		assertEquals(Constants.GUARD_TRUE, clockGuard1.toString());
		assertEquals("clock1 <= 5", clockGuard2.toString());
		assertEquals("clock1 <= 5 AND clock2 > 4 AND clock3 >= 10", clockGuard3.toString());
	}
	

}
