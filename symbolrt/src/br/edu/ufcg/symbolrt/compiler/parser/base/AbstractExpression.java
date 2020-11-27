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

import java.util.LinkedList;
import java.util.List;
/**
 * <code>AbstractTIOSTS</code> Class. <br>
 * 
 * Intermediate representation of a Expression
 * 
 * @author Jeanderson Barros Candido ( <a
 *         href="mailto:jeandersonbc@gmail.com">jeandersonbc@gmail.com</a> )
 * 
 * @version 1.0 <br>
 *          (C) Copyright 2013 SYMBOL-RT Translator.
 */
public class AbstractExpression {

	List<Element> expression;
	private boolean isClockAssignment;

	public AbstractExpression(boolean isClockAssignment) {
		this.expression = new LinkedList<Element>();
		this.isClockAssignment = isClockAssignment;
	}

	public AbstractExpression() {
		this.expression = new LinkedList<Element>();
		this.isClockAssignment = false;
	}

	public void addElement(Element e) {
		if (e != null) {
			expression.add(e);
		}
	}

	@Override
	public String toString() {
		String str = "";
		for (Element e : expression) {
			if (e.getToken().image.equals("(")) {
				str += e.getToken().image;
			} else if (e.getToken().image.equals(")")) {
				str = str.trim() + e.getToken().image + " ";
			} else {
				str += e.getToken().image + " ";
			}
		}
		return str.trim();
	}

	public boolean isClockAssignment() {
		return this.isClockAssignment;
	}

}
