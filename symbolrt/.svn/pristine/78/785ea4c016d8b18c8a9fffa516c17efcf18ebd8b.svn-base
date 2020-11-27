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
 * 
 */
package br.edu.ufcg.symbolrt.compiler.facade;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import br.edu.ufcg.symbolrt.base.TIOSTS;
import br.edu.ufcg.symbolrt.compiler.parser.TranslationUnit;
import br.edu.ufcg.symbolrt.compiler.synthesis.SynthesisUnit;

/**
 * <code>Compiler</code> Class. <br>
 * This class is the facade of the compiler component. In order to get the
 * proper information three steps are needed: (1) call the method compile; (2)
 * call the method getSpecification; and (3) call the method getTestPurpose.
 * 
 * @author Wilkerson de Lucena Andrade ( <a
 *         href="mailto:wilkerson@computacao.ufcg.edu.br"
 *         >wilkerson@computacao.ufcg.edu.br</a> )
 * 
 * @author Jeanderson Barros Candido ( <a
 *         href="mailto:jbc.ufcg@gmail.com"
 *         >jbc.ufcg@gmail.com</a> )
 * 
 * @version 1.0 <br>
 *          SYmbolic Model-Based test case generation toOL for Real-Time systems
 *          (SYMBOLRT) <br>
 *          (C) Copyright 2010-2013 Federal University of Campina Grande (UFCG) <br>
 *          <a
 *          href="https://sites.google.com/a/computacao.ufcg.edu.br/symbolrt">
 *          https://sites.google.com/a/computacao.ufcg.edu.br/symbolrt</a>
 */
public class Compiler {

	/**
	 * Method used to compile the source file. If the given path doesn't exists
	 * then the compilation process will be aborted.
	 * 
	 * @param filePath
	 *            The absolute path to the given fileName
	 * @param specName 
	 */
	public static void compile(String filePath, String specName) {
		BufferedInputStream file = createFile(filePath);
		if (file != null) {
			System.out.println("Starting compilation process");
			TranslationUnit.init(file, specName);
		} else
			System.out.println("Aborting compilation process");
	}

	/**
	 * Method used to get a TIOSTS model representing a specification for the
	 * given identifier.
	 * 
	 * @param specName
	 *            The specification identifier
	 * @return a {@link TIOSTS} representing this specification
	 */
	public static TIOSTS getSpecification(String specName) {
		return SynthesisUnit.getProcessedModel(specName);
	}

	/**
	 * Method used to get a TIOSTS model representing a test purpose for the
	 * given name.
	 * 
	 * @param tpName
	 *            The test purpose identifier
	 * @return a {@link TIOSTS} representing this test purpose
	 */
	public static TIOSTS getTestPurpose(String tpName) {
		return SynthesisUnit.getProcessedModel(tpName);
	}

	/**
	 * @param filePath
	 *            The absolute path to the given file
	 * @return Instantiated file
	 */
	private static BufferedInputStream createFile(String filePath) {
		BufferedInputStream file = null;
		if (filePath != null) {
			try {
				file = new BufferedInputStream(new FileInputStream(new File(
						filePath)));
			} catch (FileNotFoundException e) {
				System.err.println("File not found");
			}
		}
		return file;
	}

}
