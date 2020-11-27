/*
 * SYmbolic Model-Based test case generation toOL for Real-Time systems (SYMBOLRT)
 * (C) Copyright 2010-2013 Federal University of Campina Grande (UFCG)
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
package br.edu.ufcg.symbolrt.compiler.parser.base;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * <code>AbstractTIOSTS</code> Class. <br>
 * 
 * Intermediate representation of a TIOSTS model
 * 
 * @author Jeanderson Barros Candido ( <a
 *         href="mailto:jeandersonbc@gmail.com">jeandersonbc@gmail.com</a> )
 * 
 * @version 1.0 <br>
 *          (C) Copyright 2013 SYMBOL-RT Translator.
 */
public class AbstractTIOSTS {

	private Set<AbstractTransition> transitions;
	private Set<AbstractAction> internalActions;
	private Set<AbstractAction> outputActions;
	private Set<AbstractAction> inputActions;
	private Set<Element> variables;
	private List<Element> states;
	private boolean finished;
	private Element processName;
	private List<TypedElement> parameters;
	private boolean isSpecification;

	public AbstractTIOSTS(Element process) {
		this.transitions = new LinkedHashSet<AbstractTransition>();
		this.internalActions = new LinkedHashSet<AbstractAction>();
		this.outputActions = new LinkedHashSet<AbstractAction>();
		this.inputActions = new LinkedHashSet<AbstractAction>();
		this.variables = new LinkedHashSet<Element>();
		this.states = new ArrayList<Element>();
		this.parameters = new ArrayList<TypedElement>();
		this.finished = false;
		this.isSpecification = false;
		this.processName = process;
	}

	public void addInternalAction(AbstractAction internalAction) {
		if ((internalAction != null) && (!finished)) {
			this.internalActions.add(internalAction);
		}
	}

	public void addOutputAction(AbstractAction outputAction) {
		if ((outputAction != null) && (!finished)) {
			this.outputActions.add(outputAction);
		}
	}

	public void addInputAction(AbstractAction inputAction) {
		if ((inputAction != null) && (!finished)) {
			this.inputActions.add(inputAction);
		}
	}

	public void addVariable(Element variable) {
		if ((variable != null) && (!finished)) {
			this.variables.add(variable);
		}
	}

	public void addParameter(TypedElement parameter) {
		if ((parameter != null) && (!finished)) {
			this.parameters.add(parameter);
		}
	}

	public void addState(Element state) {
		if ((state != null) && (!finished)) {
			if (!this.states.contains(state))
				this.states.add(state);
		}
	}

	public void addTransition(AbstractTransition transition) {
		if ((transition != null) && (!finished)) {
			this.transitions.add(transition);
		}
	}

	public String getName() {
		return this.processName.getToken().image;
	}

	public void finishProcess() {
		this.finished = true;
	}

	public Set<Element> getVariables() {
		return this.variables;
	}

	public List<Element> getStates() {
		return this.states;
	}

	public Set<AbstractTransition> getTransitions() {
		return this.transitions;
	}

	public List<TypedElement> getParameters() {
		return this.parameters;
	}

	public Set<AbstractAction> getInternalActions() {
		return internalActions;
	}

	public Set<AbstractAction> getOutputActions() {
		return outputActions;
	}

	public Set<AbstractAction> getInputActions() {
		return inputActions;
	}
	
	public boolean isSpecification() {
		return this.isSpecification;
	}
	public void setIsSpecification(boolean condition) {
		isSpecification = condition;
	}
}
