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

import java.util.HashMap;
import java.util.Map;

import br.edu.ufcg.symbolrt.base.TIOSTS;
import br.edu.ufcg.symbolrt.compiler.parser.TranslationUnit;
import br.edu.ufcg.symbolrt.compiler.parser.base.AbstractTIOSTS;

/**
 * <code>DataManager</code> Class. <br>
 * 
 * This class is responsible for all compilation process.
 * 
 * @author Jeanderson Barros Candido ( <a
 *         href="mailto:jeandersonbc@gmail.com">jeandersonbc@gmail.com</a> )
 * 
 * @version 1.0 <br>
 *          (C) Copyright 2013 SYMBOL-RT Translator.
 */
public class SynthesisUnit {

	private static Map<String, TIOSTS> models = new HashMap<String, TIOSTS>();

	/**
	 * Initializes the compilation process
	 */
	public static void init() {
		Map<String, AbstractTIOSTS> abstractModels = TranslationUnit
				.getModels();
		TIOSTS model = null;
		for (AbstractTIOSTS absModel : abstractModels.values()) {
			System.out.print("Process'" + absModel.getName() + "'... ");
			model = Builder.compileModel(absModel);
			if (model != null) {
				models.put(model.getName(), model);
				System.out.println("Success");
			} else {
				System.out.println("Fail");
			}
		}

	}

	public static TIOSTS getProcessedModel(String identifier) {
		TIOSTS model = null;
		model = models.get(identifier);
		if (model == null) {
			// FIXME THROW EXCEPTION
			System.out.println("Symbolrt: process '" + identifier
					+ "' not found");
		}
		return model;
	}

}
