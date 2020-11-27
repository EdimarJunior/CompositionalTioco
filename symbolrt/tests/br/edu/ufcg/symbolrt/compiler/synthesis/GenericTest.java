/*
 * SYmbolic Model-Based test case generation toOL for Real-Time systems (SYMBOLRT)
 * (C) Copyright 2010-2012 Federal University of Campina Grande (UFCG)
 * 
 * This file is part of SYMBOLRT.
 *
 * SYMBOLRT is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * SYMBOLRT is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with SYMBOLRT.  If not, see <http://www.gnu.org/licenses/>.
 */
package br.edu.ufcg.symbolrt.compiler.synthesis;

import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import br.edu.ufcg.symbolrt.base.Action;
import br.edu.ufcg.symbolrt.base.TIOSTS;
import br.edu.ufcg.symbolrt.base.Transition;
import br.edu.ufcg.symbolrt.base.TypedData;

public abstract class GenericTest {

	private TIOSTS processedModel;
	private TIOSTS model;

	@Before
	public void setUp() {
		processedModel = setupProcessedModel();
		model = setupModel();
	}

	protected abstract TIOSTS setupProcessedModel();

	protected abstract TIOSTS setupModel();

	@Test
	public void variablesTest() {
		Assert.assertEquals("Wrong size", model.getVariables().size(),
				processedModel.getVariables().size());

		TypedData td1 = null;
		TypedData td2 = null;
		for (int i = 0; i < model.getVariables().size(); i++) {
			td1 = model.getVariables().get(i);
			td2 = processedModel.getVariables().get(i);
			Assert.assertEquals("Expected '" + td1.getName() + "' but found '"
					+ td2.getName() + "'", td1, td2);
		}
		Assert.assertEquals(model.getVariables(), processedModel.getVariables());

	}

	@Test
	public void paramatersTest() {
		Assert.assertEquals("Wrong size", model.getActionParameters().size(),
				processedModel.getActionParameters().size());

		TypedData td1 = null;
		TypedData td2 = null;
		for (int i = 0; i < model.getActionParameters().size(); i++) {
			td1 = model.getActionParameters().get(i);
			td2 = processedModel.getActionParameters().get(i);
			Assert.assertEquals("Expected '" + td1.getName() + "' but found '"
					+ td2.getName() + "'", td1, td2);
		}
		Assert.assertEquals(model.getActionParameters(),
				processedModel.getActionParameters());
	}

	@Test
	public void clocksTest() {
		Assert.assertEquals(model.getClocks(), processedModel.getClocks());
	}

	// Input, output, and internal actions
	@Test
	public void actionTest() {
		actionTest(model.getOutputActions(), processedModel.getOutputActions());
		actionTest(model.getInputActions(), processedModel.getInputActions());
		actionTest(model.getInternalActions(),
				processedModel.getInternalActions());
	}

	private void actionTest(List<Action> expectedActions, List<Action> actions) {
		Assert.assertNotNull(expectedActions);
		Assert.assertNotNull(actions);
		Assert.assertEquals("Expected: " + expectedActions + "// Found: "
				+ actions, expectedActions.size(), actions.size());
		for (Action act : actions) {
			Assert.assertTrue(expectedActions.contains(act));
		}
	}

	// Locations
	@Test
	public void locationTest() {
		Assert.assertEquals(model.getLocations().size(), processedModel
				.getLocations().size());
		Assert.assertEquals(model.getLocations().toString(), processedModel
				.getLocations().toString());
	}

	// Initial Condition
	@Test
	public void initialConditionsTest() {
		Assert.assertEquals(model.getInitialCondition(),
				processedModel.getInitialCondition());
	}

	// Initial Location
	@Test
	public void initialLocationTest() {
		Assert.assertEquals(model.getInitialLocation(),
				processedModel.getInitialLocation());
	}

	// Transitions
	@Test
	public void transitionsTest() {
		Assert.assertEquals(model.getTransitions().size(), processedModel
				.getTransitions().size());
		int value = 1;
		for (Transition trans : processedModel.getTransitions()) {

			Assert.assertTrue("#" + value + " Transition not found:" + trans,
					model.getTransitions().contains(trans));
			value++;
		}
	}

}
