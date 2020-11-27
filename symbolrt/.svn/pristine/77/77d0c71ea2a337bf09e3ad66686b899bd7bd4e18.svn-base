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

import br.edu.ufcg.symbolrt.compiler.parser.Token;

public class Element {

	private Token tk;

	public Element(Token tk) {
		if (tk != null)
			this.tk = tk;
		else
			this.tk = null;
	}

	public Token getToken() {
		return this.tk;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((tk == null) ? 0 : tk.image.hashCode());
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
		Element other = (Element) obj;
		if (tk == null) {
			if (other.tk != null)
				return false;
		} else if (!tk.image.equals(other.tk.image))
			return false;
		return true;
	}

	public String getName() {
		return this.getToken().image;
	}

	@Override
	public String toString() {
		return this.getToken().image;
	}
	
	
	

}
