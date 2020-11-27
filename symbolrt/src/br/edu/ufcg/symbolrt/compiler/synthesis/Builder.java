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
package br.edu.ufcg.symbolrt.compiler.synthesis;

import java.util.List;
import java.util.Set;

import br.edu.ufcg.symbolrt.base.Action;
import br.edu.ufcg.symbolrt.base.Location;
import br.edu.ufcg.symbolrt.base.TIOSTS;
import br.edu.ufcg.symbolrt.base.TypedData;
import br.edu.ufcg.symbolrt.compiler.parser.TranslationUnit;
import br.edu.ufcg.symbolrt.compiler.parser.base.AbstractAction;
import br.edu.ufcg.symbolrt.compiler.parser.base.AbstractExpression;
import br.edu.ufcg.symbolrt.compiler.parser.base.AbstractTIOSTS;
import br.edu.ufcg.symbolrt.compiler.parser.base.AbstractTransition;
import br.edu.ufcg.symbolrt.compiler.parser.base.Element;
import br.edu.ufcg.symbolrt.compiler.parser.base.StateElement;
import br.edu.ufcg.symbolrt.compiler.parser.base.TypedElement;
import br.edu.ufcg.symbolrt.util.Constants;

/**
 * <code>Builder</code> Class. <br>
 * 
 * This class is responsible for TIOSTS models instantiations
 * 
 * @author Jeanderson Barros Candido ( <a
 *         href="mailto:jeandersonbc@gmail.com">jeandersonbc@gmail.com</a> )
 * 
 * @version 1.0 <br>
 *          (C) Copyright 2013 SYMBOL-RT Translator.
 */
class Builder {

	/**
	 * @param abstractModel
	 * @return
	 */
	public static final TIOSTS compileModel(AbstractTIOSTS abstractModel) {

		TIOSTS model = null;
		if (abstractModel != null) {
			model = new TIOSTS(abstractModel.getName());

			if (abstractModel.isSpecification()) {
				setClocks(model);
				setVariables(model, abstractModel);
			}
			setParameters(model, abstractModel);
			setInternalActions(model, abstractModel);
			setInputActions(model, abstractModel);
			setOutputActions(model, abstractModel);
			setLocations(model, abstractModel);
			setTransitions(model, abstractModel);
		}
		return model;
	}

	/**
	 * @param model
	 * @param abstractModel
	 */
	private static void setTransitions(TIOSTS model,
			AbstractTIOSTS abstractModel) {
		Set<AbstractTransition> transitions = abstractModel.getTransitions();
		for (AbstractTransition trans : transitions) {

			// Assignments
			String clockAssignments = transformAssignment(trans
					.getClockAssignments());
			String dataAssignments = transformAssignment(trans
					.getDataAssignments());

			// Locations
			String sourceLocation = trans.getSource();
			String targetLocation = trans.getTarget();

			// Guards
			String clockGuard = createGuard(trans.getClockGuard());
			String dataGuard = createGuard(trans.getDataGuard());
			if (model.getLocation(sourceLocation).isInitialLocation()) {
				model.setInitialCondition(dataGuard);
			}
			// Deadline
			String deadline = trans.getDeadline();

			// Action
			AbstractAction absAction = trans.getAction();
			Action action = null;
			if (absAction != null) {
				action = model.getAction(absAction.getType(),
						absAction.getName());

			} else
				action = model.getAction(Constants.ACTION_INTERNAL, "tau");

			if (deadline == null) {
				model.createTransition(sourceLocation, dataGuard, clockGuard,
						action, dataAssignments, clockAssignments,
						targetLocation);
			} else {
				model.createTransition(sourceLocation, dataGuard, clockGuard,
						action, dataAssignments, clockAssignments, deadline,
						targetLocation);
			}
		}
	}

	/**
	 * @param model
	 * @param abstractModel
	 */
	private static void setLocations(TIOSTS model, AbstractTIOSTS abstractModel) {
		Location location = null;
		List<Element> states = abstractModel.getStates();
		for (Element e : states) {
			StateElement e2 = (StateElement) e;
			if (e2.isInitial()) {
				location = new Location(e2.getName(), true);
				model.setInitialLocation(location);
			} else {
				location = new Location(e2.getName(), false);
			}
			model.addLocation(location);
		}
	}

	/**
	 * @param model
	 * @param abstractModel
	 */
	private static void setOutputActions(TIOSTS model,
			AbstractTIOSTS abstractModel) {
		Action action = null;
		for (AbstractAction act : abstractModel.getOutputActions()) {
			action = new Action(act.getName(), Constants.ACTION_OUTPUT);
			setActionParameters(action);
			model.addOutputAction(action);
		}
	}

	/**
	 * @param action
	 */
	private static void setActionParameters(Action action) {
		List<Element> params = TranslationUnit.getParamsTable().get(
				action.getName());
		for (Element e : params) {
			action.addParameter(e.getName());
		}
	}

	/**
	 * @param model
	 * @param abstractModel
	 */
	private static void setInputActions(TIOSTS model,
			AbstractTIOSTS abstractModel) {
		Action action = null;
		for (AbstractAction act : abstractModel.getInputActions()) {
			action = new Action(act.getName(), Constants.ACTION_INPUT);
			setActionParameters(action);
			model.addInputAction(action);
		}
	}

	/**
	 * @param model
	 * @param abstractModel
	 */
	private static void setInternalActions(TIOSTS model,
			AbstractTIOSTS abstractModel) {
		Set<AbstractAction> internalActions = abstractModel
				.getInternalActions();
		if (internalActions == null || internalActions.size() == 0) {
			model.addInternalAction(new Action("tau", Constants.ACTION_INTERNAL));

		} else {
			Action action = null;
			for (AbstractAction act : internalActions) {
				action = new Action(act.getName(), Constants.ACTION_INTERNAL);
				if (act.getParameters() != null
						&& act.getParameters().size() > 0) {
					System.err
							.println("Warning: Internal action '"
									+ act.getName()
									+ "' has been declared with parameters. Deleting parameters...");
				}
				model.addInternalAction(action);
			}
		}
	}

	/**
	 * @param model
	 * @param abstractModel
	 */
	private static void setParameters(TIOSTS model, AbstractTIOSTS abstractModel) {
		TypedData actionParameter = null;
		for (TypedElement e : abstractModel.getParameters()) {
			actionParameter = new TypedData(e.getName(), e.getType());
			model.addActionParameter(actionParameter);
		}
	}

	/**
	 * @param model
	 */
	private static void setClocks(TIOSTS model) {
		for (Element e : TranslationUnit.getClocks()) {
			model.addClock(e.getName());
		}
	}

	/**
	 * @param model
	 * @param abstractModel
	 */
	private static void setVariables(TIOSTS model, AbstractTIOSTS abstractModel) {
		TypedData variable = null;
		for (Element e : abstractModel.getVariables()) {
			variable = new TypedData(e.getName(), ((TypedElement) e).getType());
			model.addVariable(variable);
		}
	}

	/**
	 * @param expr
	 * @return
	 */
	private static String createGuard(AbstractExpression expr) {
		String guard;
		if (expr == null) {
			guard = Constants.GUARD_TRUE;
		} else {
			guard = transformExpression(expr);
		}
		return guard;
	}

	/**
	 * @param expr
	 * @return
	 */
	private static String transformExpression(AbstractExpression expr) {
		if (expr != null)
			return expr.toString();
		else
			return null;
	}

	/**
	 * @param assignments
	 * @return
	 */
	private static String transformAssignment(
			Set<AbstractExpression> assignments) {
		if (assignments != null && !assignments.isEmpty()) {
			String str = "";
			for (AbstractExpression e : assignments) {
				str += " | " + e.toString();
			}
			return str.substring(2).trim();
		}
		return null;
	}

}