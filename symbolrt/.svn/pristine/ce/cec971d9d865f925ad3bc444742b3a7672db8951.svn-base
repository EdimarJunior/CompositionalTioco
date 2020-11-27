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

import java.util.LinkedHashSet;
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
public class AbstractTransition {

	private Set<AbstractExpression> assignments;
	private AbstractExpression clockGuard;
	private AbstractExpression dataGuard;
	private AbstractAction action;
	private String deadline;

	private Element destination;
	private Element source;

	private int lineDeclaration;

	public AbstractTransition(int lineDeclaration) {
		this.lineDeclaration = lineDeclaration;
		this.assignments = null;
		this.clockGuard = null;
		this.dataGuard = null;
		this.action = null;
	}

	public void setSource(Element e) {
		if (e != null)
			this.source = e;
	}

	public void setDestination(Element e) {
		if (e != null)
			this.destination = e;
	}

	public void setDeadline(String type) {
		if (type != null)
			this.deadline = type;
	}

	public void setDataGuard(AbstractExpression expr) {
		if (expr != null)
			this.dataGuard = expr;
	}

	public void setClockGuard(AbstractExpression expr) {
		if (expr != null)
			this.clockGuard = expr;
	}

	public void setAssignments(Set<AbstractExpression> expr) {
		if (expr != null)
			this.assignments = expr;
	}

	public void setAction(AbstractAction act) {
		if (act != null)
			this.action = act;
	}

	public String getTarget() {
		return this.destination.getToken().image;
	}

	public String getSource() {
		return this.source.getToken().image;
	}

	public AbstractExpression getClockGuard() {
		return this.clockGuard;
	}

	public AbstractExpression getDataGuard() {
		return this.dataGuard;
	}

	public AbstractAction getAction() {
		return this.action;
	}

	public String getDeadline() {
		return this.deadline;
	}

	public Set<AbstractExpression> getAssignments() {
		return this.assignments;
	}

	public Set<AbstractExpression> getDataAssignments() {
		Set<AbstractExpression> dataAssignments = null;
		if (this.assignments != null) {
			dataAssignments = new LinkedHashSet<AbstractExpression>();
			for (AbstractExpression expr : this.assignments) {
				if (!expr.isClockAssignment())
					dataAssignments.add(expr);
			}
		}
		return dataAssignments;
	}

	public Set<AbstractExpression> getClockAssignments() {
		Set<AbstractExpression> clockAssignments = null;
		if (this.assignments != null) {
			for (AbstractExpression expr : this.assignments) {
				if (expr.isClockAssignment()) {
					if (clockAssignments == null) {
						clockAssignments = new LinkedHashSet<AbstractExpression>();
					}
					clockAssignments.add(expr);
				}
			}
		}
		return clockAssignments;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((action == null) ? 0 : action.hashCode());
		result = prime * result
				+ ((assignments == null) ? 0 : assignments.hashCode());
		result = prime * result
				+ ((clockGuard == null) ? 0 : clockGuard.hashCode());
		result = prime * result
				+ ((dataGuard == null) ? 0 : dataGuard.hashCode());
		result = prime * result
				+ ((deadline == null) ? 0 : deadline.hashCode());
		result = prime * result
				+ ((destination == null) ? 0 : destination.hashCode());
		result = prime * result + lineDeclaration;
		result = prime * result + ((source == null) ? 0 : source.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractTransition other = (AbstractTransition) obj;
		if (action == null) {
			if (other.action != null)
				return false;
		} else if (!action.equals(other.action))
			return false;
		if (assignments == null) {
			if (other.assignments != null)
				return false;
		} else if (!assignments.equals(other.assignments))
			return false;
		if (clockGuard == null) {
			if (other.clockGuard != null)
				return false;
		} else if (!clockGuard.equals(other.clockGuard))
			return false;
		if (dataGuard == null) {
			if (other.dataGuard != null)
				return false;
		} else if (!dataGuard.equals(other.dataGuard))
			return false;
		if (deadline == null) {
			if (other.deadline != null)
				return false;
		} else if (!deadline.equals(other.deadline))
			return false;
		if (destination == null) {
			if (other.destination != null)
				return false;
		} else if (!destination.equals(other.destination))
			return false;
		if (lineDeclaration != other.lineDeclaration)
			return false;
		if (source == null) {
			if (other.source != null)
				return false;
		} else if (!source.equals(other.source))
			return false;
		return true;
	}
}