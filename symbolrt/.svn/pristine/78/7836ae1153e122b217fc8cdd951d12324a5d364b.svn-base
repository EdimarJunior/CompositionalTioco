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
 *
 * REVISION HISTORY:
 * Author                           Date           Brief Description
 * -------------------------------- -------------- ------------------------------
 * Wilkerson de Lucena Andrade      17/04/2012     Initial version
 * 
 */
package br.edu.ufcg.symbolrt.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;


/**
 * <code>Config</code> Class.<br>
 * This class is the configuration class for some SYMBOLRT global constants.  
 * 
 * @author Wilkerson de Lucena Andrade  ( <a href="mailto:wilkerson@computacao.ufcg.edu.br">wilkerson@computacao.ufcg.edu.br</a> )
 * 
 * @version 1.0
 * <br>
 * SYmbolic Model-Based test case generation toOL for Real-Time systems (SYMBOLRT)
 * <br>
 * (C) Copyright 2010-2013 Federal University of Campina Grande (UFCG)
 * <br>
 * <a href="https://sites.google.com/a/computacao.ufcg.edu.br/symbolrt">https://sites.google.com/a/computacao.ufcg.edu.br/symbolrt</a>
 */
public class Config {

	private static Config instance = null;
	
	private static final String PROPERTIES_FILE = "symbolrt.properties";
	
	private final String CVC3_KEY = "cvc3";
	private final String SYMBOLRTDBM_KEY = "symbolrtdbm";
	private final String TESTCASES_KEY = "testcases";
	
	/**
	 * The absolute path where the CVC3 SMT Solver is installed.
	 */
	private String cvc3AbsolutePath;
	
	/**
	 * The path where the symbolrtdbm is installed.
	 */
	public String symbolrtdbmPath;
	
	/**
	 * The path of the folder where the generated test cases are stored.
	 */
	public String testCasesPath;


	private Config(File properties) {
		Properties prop = new Properties();
		FileInputStream input = null;
		
		try {
			input = new FileInputStream(properties);
			prop.load(input);
			this.cvc3AbsolutePath = prop.getProperty(CVC3_KEY);
			this.symbolrtdbmPath = prop.getProperty(SYMBOLRTDBM_KEY);
			this.testCasesPath = prop.getProperty(TESTCASES_KEY);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (input != null) {
					input.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	
	/**
	 * Method used to obtain an instance of this class.
	 * @return An instance of {@link Config}.
	 */
	public static Config getInstance() {
		if (instance == null) {
			File prop = new File(PROPERTIES_FILE);
			instance = new Config(prop);
		}
		return instance;
	}
	
	
	/**
	 * Returns the absolute path where the CVC3 SMT Solver is installed.
	 * @return The absolute path where the CVC3 SMT Solver is installed.
	 */
	public String getCVC3AbsolutePath(){
		return this.cvc3AbsolutePath;
	}
	

	/**
	 * Returns the path where the symbolrtdbm is installed.
	 * @return The path where the symbolrtdbm is installed.
	 */
	public String getSymbolrtdbmPath(){
		return this.symbolrtdbmPath;
	}
	
	/**
	 * Returns the path where the generated test cases are stored.
	 * @return The path where the generated test cases are stored.
	 */
	public String getTestCasesPath(){
		return this.testCasesPath;
	}
	
	
}
