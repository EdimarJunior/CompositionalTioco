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
 * Wilkerson de Lucena Andrade      07/08/2010     Initial version
 * Wilkerson de Lucena Andrade      03/01/2011     Guard and assignment symbols added
 * Wilkerson de Lucena Andrade      11/01/2011     Separator and initial value added to be used in symbolic value names
 * 
 */
package br.edu.ufcg.symbolrt.util;

/**
 * <code>Constants</code> Class.<br>
 * This class contains all global constants.  
 * 
 * @author Wilkerson de Lucena Andrade  ( <a href="mailto:wilkerson@computacao.ufcg.edu.br">wilkerson@computacao.ufcg.edu.br</a> )
 * 
 * @version 1.3
 * <br>
 * SYmbolic Model-Based test case generation toOL for Real-Time systems (SYMBOLRT)
 * <br>
 * (C) Copyright 2010-2013 Federal University of Campina Grande (UFCG)
 * <br>
 * <a href="https://sites.google.com/a/computacao.ufcg.edu.br/symbolrt">https://sites.google.com/a/computacao.ufcg.edu.br/symbolrt</a>
 */
public class Constants {
	
	/** 
	 * A constant used to indicate input actions. 
	 */
 	public static final int ACTION_INPUT = 1;
	
	/**
	 * A constant used to indicate internal actions.
	 */
	public static final int ACTION_INTERNAL = 0;
	
	/**
	 * A constant used to indicate output actions.
	 */
	public static final int ACTION_OUTPUT = -1;
	
	
	
	
	/**
	 * The accept label of a special location used in a test purpose in order specify scenarios to test.
	 */
	public static final String LOCATION_LABEL_ACCEPT = "Accept";
	
	/**
	 * The reject label of a special location used in a test purpose in order to discard scenarios during 
	 * the test case generation.
	 */
	public static final String LOCATION_LABEL_REJECT = "Reject";
	
	/**
	 * The don'care label of a special location used in a test purpose in order to discard scenarios during 
	 * the input-completion process.
	 */
	public static final String LOCATION_LABEL_DONTCARE = "DontCare";
	
	
	/**
	 * The Pass verdict of a test case.
	 */
	public static final String VERDICT_PASS = "PASS";
	
	/**
	 * The Inconclusive verdict of a test case.
	 */
	public static final String VERDICT_INCONCLUSIVE = "INCONCLUSIVE";
	
	
	
	
	/**
	 * A constant used to indicate that the typed data is of type boolean.
	 */
	public static final int TYPE_BOOLEAN = 0;
	
	/**
	 * A constant used to indicate that the typed data is of type integer.
	 */
	public static final int TYPE_INTEGER = 1;

	
	
	
	/**
	 * The lazy deadline imposes no urgency to the transition to be taken.
	 */
	public static final String DEADLINE_LAZY = "lazy";
	
	/**
	 * The delayable means that once enabled the transition must be taken before it 
	 * becomes disabled.
	 */
	public static final String DEADLINE_DELAYABLE = "delayable";
	
	/**
	 * The eager deadline means the transition must be taken as soon as it becomes enabled.
	 */
	public static final String DEADLINE_EAGER = "eager";
	
	
	
	
	/**
	 * The true logical value. This is the default value used in transitions where no guards are declared.
	 */
	public static final String GUARD_TRUE = "TRUE";
	
	/**
	 * The false logical value.
	 */
	public static final String GUARD_FALSE = "FALSE";
	
	/**
	 * The connector used to denote a conjunction in Boolean expressions.
	 */
	public static final String GUARD_CONJUNCTION = "AND";
	
	/**
	 * The connector used to denote a disjunction in Boolean expressions.
	 */
	public static final String GUARD_DISJUNCTION = "OR";
	
	/**
	 * The negation symbol used to negate Boolean expressions.
	 */
	public static final String GUARD_NEGATION = "NOT";
	
	/**
	 * A constant used to represent "Equal".
	 */
	public static final String GUARD_EQUAL = "=";

	/**
	 * A constant used to represent "Less than".
	 */
	public static final String GUARD_LT = "<";
	
	/**
	 * A constant used to represent "Less than or equal".
	 */
	public static final String GUARD_LEQ = "<=";
	
	/**
	 * A constant used to represent "Greater than or equal".
	 */
	public static final String GUARD_GEQ = ">=";
	
	/**
	 * A constant used to represent "Greater than".
	 */
	public static final String GUARD_GT = ">";
	
	/**
	 * A constant used to represent "Different".
	 */
	public static final String GUARD_DIF = "<>";
	
	
	
	
	/**
	 * A constant used to separate assignments.
	 */
	public static final String ASSIGNMENT_SEPARATOR = " | ";
	
	/**
	 * A constant used in assignments commands to associate a value to a variable.
	 */
	public static final String ASSIGNMENT_SYMBOL = ":=";
	
	
	
	
	/**
	 * A constant used in symbolic value names to separate the concrete name and the counter.
	 */
	public static final String SYMBOLIC_VALUE_SEPARATOR = "_";
	
	/**
	 * A constant used to initialize all symbolic value names.
	 */
	public static final String SYMBOLIC_VALUE_INITIAL = "0";
	
	
	
	
	/**
	 * The name of the file used to communicate with the CVC3 SMT Solver. 
	 */
	public static final String CVC3_FILE_NAME = "symbolrt.cvc";
	
	/**
	 * A constant that represents the satisfiable response of CVC3.
	 */
	public static final String CVC3_RESPONSE_SATISFIABLE = "Satisfiable.";
	
	
	
	/**
	 * The name of the file used to communicate with the symbolrtdbm tool. 
	 */
	public static final String SYMBOLRTDBM_FILE_NAME = "symbolrt.dbm";
	
	/**
	 * The name the file used to communicate with the symbolrtdbm tool. 
	 */
	public static final String SYMBOLRTDBM_FILE_NAME_GUARD = "symbolrt_guard.dbm";
	
	/**
	 * The name the file used to communicate with the symbolrtdbm tool. 
	 */
	public static final String SYMBOLRTDBM_FILE_NAME_RESETS = "symbolrt_resets.dbm";

	
	
	
	public static final int DBM_OPCODE_CREATE_INITIAL_DBM = 1;
	
	public static final int DBM_OPCODE_IS_DBM_EMPTY = 2;
	
	public static final int DBM_OPCODE_NEXT_ZONE = 3;
	
	public static final String DBM_CLOCK_REFERENCE = "0";
	
	public static final String DBM_STRICT = "0";
	
	public static final String DBM_WEAK = "1";

		
}
