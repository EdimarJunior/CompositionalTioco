package br.edu.ufcg.symbolrt.base;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import br.edu.ufcg.symbolrt.base.Action;
import br.edu.ufcg.symbolrt.util.Constants;


public class ActionTest {

	private Action action1;
	private Action action2;
	private Action action3;
	
	@Before
	public void setUp() throws Exception {
		action1 = new Action("InputAction", Constants.ACTION_INPUT);
		action1.addParameter("param1");
		action1.addParameter("param2");
		action1.addParameter("param3");
		
		action2 = new Action("OutputAction", Constants.ACTION_OUTPUT);
		action2.addParameter("param1");
		
		action3 = new Action("InternalAction", Constants.ACTION_INTERNAL);
	}

	@Test
	public void testToString() {
		assertEquals("Test01", "InputAction?(param1,param2,param3)", action1.toString());
		assertEquals("Test02", "OutputAction!(param1)", action2.toString());
		assertEquals("Test03", "InternalAction", action3.toString());
	}

}
