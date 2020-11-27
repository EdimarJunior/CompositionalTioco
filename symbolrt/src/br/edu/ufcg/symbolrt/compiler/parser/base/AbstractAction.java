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
import java.util.List;

/**
 * <code>AbstractTIOSTS</code> Class. <br>
 * 
 * Intermediate representation of a TIOSTS action
 * 
 * @author Jeanderson Barros Candido ( <a
 *         href="mailto:jeandersonbc@gmail.com">jeandersonbc@gmail.com</a> )
 * 
 * @version 1.0 <br>
 *          (C) Copyright 2013 SYMBOL-RT Translator.
 */
public class AbstractAction {

	private List<Element> parameters;
	private Element identifier;
	private int type;

	public AbstractAction() {
		this.type = 0;
		this.identifier = null;
		this.parameters = new ArrayList<Element>();
	}

	public void setType(int type) {
		this.type = type;
	}

	public void setIdentifier(Element identifier) {
		if (identifier != null)
			this.identifier = identifier;
	}

	public void addParameter(Element parameter) {
		if ((parameter != null) && (!parameters.contains(parameter)))
			this.parameters.add(parameter);
	}

	public List<Element> getParameters() {
		return this.parameters;
	}

	public int getType() {
		return this.type;
	}

	public String getName() {
		return this.identifier.getToken().image;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((identifier == null) ? 0 : identifier.hashCode());
		result = prime * result
				+ ((parameters == null) ? 0 : parameters.hashCode());
		result = prime * result + type;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		try {
			AbstractAction otherAction = (AbstractAction) obj;
			return this.getName().equals(otherAction.getName());
		} catch (ClassCastException e) {
			return false;
		}
	}

	@Override
	public String toString() {
		return identifier + "[" + parameters + "]";
	}

}
