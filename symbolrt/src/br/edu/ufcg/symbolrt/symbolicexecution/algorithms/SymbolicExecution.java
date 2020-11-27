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
 * Wilkerson de Lucena Andrade      11/01/2011     Initial version
 * 
 */
package br.edu.ufcg.symbolrt.symbolicexecution.algorithms;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import br.edu.ufcg.symbolrt.base.Action;
import br.edu.ufcg.symbolrt.base.ClockGuard;
import br.edu.ufcg.symbolrt.base.Location;
import br.edu.ufcg.symbolrt.base.TIOSTS;
import br.edu.ufcg.symbolrt.base.Transition;
import br.edu.ufcg.symbolrt.base.TypedData;
import br.edu.ufcg.symbolrt.symbolicexecution.base.SymbolicAction;
import br.edu.ufcg.symbolrt.symbolicexecution.base.SymbolicExecutionTree;
import br.edu.ufcg.symbolrt.symbolicexecution.base.SymbolicState;
import br.edu.ufcg.symbolrt.symbolicexecution.base.SymbolicTransition;
import br.edu.ufcg.symbolrt.symbolicexecution.exception.SymbolicValueNameException;
import br.edu.ufcg.symbolrt.util.Config;
import br.edu.ufcg.symbolrt.util.Constants;


/**
 * <code>SymbolicExecution</code> Class. <br>
 * This class contains the main algorithms needed to symbolically execute a TIOSTS. 
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
public class SymbolicExecution {

	private static SymbolicExecution instance = null;


	private SymbolicExecution() {
	}

	
	/**
	 * Method used to obtain an instance of this class.
	 * @return An instance of {@link SymbolicExecution}.
	 */
	public static SymbolicExecution getInstance() {
		if (instance == null) {
			instance = new SymbolicExecution();
		}
		return instance;
	}
	
	/**
	 * Returns the symbolic execution of a TIOSTS.
	 * @param tiosts The TIOSTS to symbolically execute.
	 * @return The ZSET representing the symbolic execution of TIOSTS.
	 * @throws SymbolicValueNameException If there is some problem with variable and action parameter names.
	 */
	public SymbolicExecutionTree execute(TIOSTS tiosts) throws SymbolicValueNameException {
		// Map of variables and action parameters used to get types
		Map<String,TypedData> varParamMapping = initializeVarParamMapping(tiosts.getVariables(), tiosts.getActionParameters());

		// ********** Definition of the initial ZSES **********
		String initialPathCondition = Constants.GUARD_TRUE;
		// Map of symbolic values of variables and action parameters  
		Map<String,String> symbolicValues = initializeSymbolicValues(varParamMapping);
		String initialZone = executeUPPAALDBM(Constants.DBM_OPCODE_CREATE_INITIAL_DBM, tiosts.getClocks().size()+1+""); // All clocks are set to zero in the initial zone
		SymbolicState initialZSES = new SymbolicState(tiosts.getInitialLocation(), initialPathCondition, symbolicValues, initialZone);
		initialZSES.setParentTransition(null);
		// ****************************************************
		
		// The ZSET to be generated and returned by this method
		String name = "ZSET_" + tiosts.getName();
		SymbolicExecutionTree zset = new SymbolicExecutionTree(name, initialZSES);
		
		
		List<SymbolicState> unvisited = new ArrayList<SymbolicState>();
		unvisited.add(initialZSES);
		
		while (unvisited.size() != 0) {
			SymbolicState currentZSES = unvisited.remove(0); // Pick and remove the first element of the unvisited list
			
			for (Transition transition : currentZSES.getLocation().getOutTransitions()) {
				Action action = transition.getAction();
				
				// List of unique symbolic values for every parameter of the current symbolic action
				List<String> paramIds = new ArrayList<String>();
				// Map of action parameters to symbolic values of the current symbolic action
				Map<String,String> paramMapping = new HashMap<String, String>();
				
				// Update the general mapping of symbolic values
				Map<String,String> nextSymbolicValues = new HashMap<String, String>(currentZSES.getMapping());
				
				for (String actionParameter : action.getParameters()) {
					String newParamId = updateSymbolicValue(nextSymbolicValues.get(actionParameter));
					paramIds.add(newParamId);
					paramMapping.put(actionParameter, newParamId);
					nextSymbolicValues.put(actionParameter, newParamId);
				}
				
				SymbolicAction symbolicAction = new SymbolicAction(action, paramIds, paramMapping);
				
				// ********** Definition of the next Path Condition **********
				String dataGuard = transition.getDataGuard();
				if (!dataGuard.equals(Constants.GUARD_TRUE)) {
					dataGuard = updateExpression(dataGuard, nextSymbolicValues);
				}
				String nextPathCondition = createNextPathCondition(currentZSES.getPathCondition(), dataGuard);
				// ***********************************************************
				
				// Definition of the next assignments
				updateSymbolicValueWithAssignments(transition.getDataAssignments(), nextSymbolicValues);
				
				// Definition of the next zone
				String nextZone = createNextZone(currentZSES.getZone(), transition.getClockGuard(), transition.getClockAssignments(), tiosts);
				
				SymbolicState nextZSES = new SymbolicState(transition.getTarget(), nextPathCondition, nextSymbolicValues, nextZone);
				
				
				if (isReachable(nextZSES, varParamMapping) && !upperBoundReached(transition.getTarget(), currentZSES)) {
					unvisited.add(nextZSES);
					zset.addSymbolicState(nextZSES);
					SymbolicTransition symbolicTransition = new SymbolicTransition(currentZSES, symbolicAction, transition.getClockGuard().clone(), transition.getClockAssignments(), transition.getDeadline(), nextZSES);
					currentZSES.addChildTransition(symbolicTransition);
					nextZSES.setParentTransition(symbolicTransition);
				}
				
			}
		}
		
		return zset;
	}
	

	// Initialize the variables and action parameters mapping.
	private Map<String,TypedData> initializeVarParamMapping(List<TypedData> variables, List<TypedData> actionParameters){
		Map<String,TypedData> result = new HashMap<String, TypedData>();
		
		for (TypedData variable : variables) {
			result.put(variable.getName(), variable);
		}
		
		for (TypedData actionParameter : actionParameters) {
			result.put(actionParameter.getName(), actionParameter);
		}

		return result;
	}
	
	
	// Initialize the symbolic values of variables and action parameters.
	private Map<String,String> initializeSymbolicValues(Map<String,TypedData> varParamMapping) {
		Map<String,String> result = new HashMap<String, String>();
		
		for (String name : varParamMapping.keySet()) {
			result.put(name, name + Constants.SYMBOLIC_VALUE_SEPARATOR + Constants.SYMBOLIC_VALUE_INITIAL);
		}
		
		return result;
	}
	
	
	// Update the variable and action parameter names to their symbolic value.
	/*private String updateExpression2(String condition, Map<String,String> symbolicValues) {
		String result = condition;
		for (String name : symbolicValues.keySet()) {
			result = result.replaceAll(name, symbolicValues.get(name));
		}
		return result;
	}*/
	
	
	private String updateExpression(String condition, Map<String,String> symbolicValues) {
		StringBuffer result = new StringBuffer();
		StringTokenizer st = new StringTokenizer(condition, " +-*/<=>()", true);
	     while (st.hasMoreTokens()) {
	         String token = st.nextToken();
	         if (symbolicValues.containsKey(token)) {
	        	 result.append(symbolicValues.get(token));
	         } else {
	        	 result.append(token);
	         }
	     }
		
		return result.toString();
	}
	
	
	// Update the name of the symbolic value adding 1 to its counter.
	private static String updateSymbolicValue(String symbolicValue) throws SymbolicValueNameException {
		String[] result = symbolicValue.split(Constants.SYMBOLIC_VALUE_SEPARATOR);
		
		if (result.length != 2) {
			throw new SymbolicValueNameException("The character \"" + Constants.SYMBOLIC_VALUE_SEPARATOR + "\" cannot be used in variables and action parameter names.");
		}
		
		int counter = Integer.parseInt(result[1]);
		return result[0] + Constants.SYMBOLIC_VALUE_SEPARATOR + ++counter;
	}
	

	// Create the next path condition considering the previous path condition and the next data guard.
	private String createNextPathCondition(String previousPathCondition, String dataGuard){
		if (previousPathCondition.equals(Constants.GUARD_TRUE))
			return dataGuard;
		else if (dataGuard.equals(Constants.GUARD_TRUE)) {
			return previousPathCondition;
		} else {
			return previousPathCondition + " " + Constants.GUARD_CONJUNCTION + " " + dataGuard;
		}
	}
	
	
	// Update the symbolic values considering some assignments.
	private void updateSymbolicValueWithAssignments(String dataAssignments, Map<String,String> symbolicValues){
		if (dataAssignments != null) {
			String[] assignments = dataAssignments.split("\\" + Constants.ASSIGNMENT_SEPARATOR.trim());
			for (String assignment : assignments) {
				String[] splitAssignment = assignment.split(Constants.ASSIGNMENT_SYMBOL);
				String variable = splitAssignment[0].trim();
				String value = splitAssignment[1].trim();
				symbolicValues.put(variable, updateExpression(value, symbolicValues));
			}
		}
	}
	
	
	// Returns a list with the names of the clocks to be reset
	private List<String> getClocksToReset(String clockAssignments){
		List<String> result = new ArrayList<String>();
		
		if (clockAssignments != null) {
			String[] assignments = clockAssignments.split("\\" + Constants.ASSIGNMENT_SEPARATOR.trim());
			for (String assignment : assignments) {
				String[] splitAssignment = assignment.split(Constants.ASSIGNMENT_SYMBOL);
				String clockName = splitAssignment[0].trim();
				result.add(clockName);
			}
		}
		
		return result;
	}
	
	
	// Returns a list with the ids of the clocks to be reset
	private List<Integer> getClockIdsToReset(List<String> clockNames, TIOSTS tiosts){
		List<Integer> result = new ArrayList<Integer>();
		
		for (String clockName : clockNames) {
			result.add(tiosts.getClockId(clockName));
		}
		
		return result;
	}
	

	// Creates the next zone
	private String createNextZone(String currentZone, ClockGuard clockGuard, String clockAssignments, TIOSTS tiosts){

		// Current zone file
		File currentZoneFile = new File(Constants.SYMBOLRTDBM_FILE_NAME);
	    Writer output = null;
		 try {
			 output = new BufferedWriter( new FileWriter(currentZoneFile) );
			 output.write(currentZone);
		 } catch (IOException e) {
			 e.printStackTrace();
		 }	finally {
			 if (output != null)
				 try {
					 output.close();
				 } catch (IOException e) {
					 e.printStackTrace();
				 }
		 }
		
		// File with the guard to intersect to the current zone
		File guardFile = new File(Constants.SYMBOLRTDBM_FILE_NAME_GUARD);
		 try {
			 output = new BufferedWriter( new FileWriter(guardFile) );
			 output.write(clockGuard.toDBMFormat());
		 } catch (IOException e) {
			 e.printStackTrace();
		 }	finally {
			 if (output != null)
				 try {
					 output.close();
				 } catch (IOException e) {
					 e.printStackTrace();
				 }
		 }
		 
		 // File with the clocks to reset
		 File clocksToResetFile = new File(Constants.SYMBOLRTDBM_FILE_NAME_RESETS);
		 try {
			 output = new BufferedWriter( new FileWriter(clocksToResetFile) );
			 String clocksToReset = "";
			 List<Integer> clockIdsToResetList = getClockIdsToReset(getClocksToReset(clockAssignments), tiosts);
			 if (clockIdsToResetList.size() > 0) {
				 for (int clockId : clockIdsToResetList) {
					 clocksToReset += clockId + " ";
				 }
				 output.write(clocksToReset.substring(0, clocksToReset.length() - 1));
			 } else {
				 output.write(clocksToReset);
			 }
		 } catch (IOException e) {
			 e.printStackTrace();
		 }	finally {
			 if (output != null)
				 try {
					 output.close();
				 } catch (IOException e) {
					 e.printStackTrace();
				 }
		 }


		return executeUPPAALDBM(Constants.DBM_OPCODE_NEXT_ZONE, currentZoneFile.getAbsolutePath() + " " + guardFile.getAbsolutePath()
				+ " " + clocksToResetFile.getAbsolutePath());
	}
	

	// Verify if the upper bound has been reached. The upper bound is 2, that is, each 
	// path of the ZSET has at most two ZSES corresponding to the same TIOSTS location.
	private boolean upperBoundReached(Location targetLocation, SymbolicState currentZSES){
		int counter = 0;
		
		// In the case of self loops.
		if (currentZSES.getLocation().equals(targetLocation)) {
			counter++;
		}
		
		SymbolicTransition symbolicTransition = currentZSES.getParentTransition();
		while (symbolicTransition != null) {
			SymbolicState parentZSES = symbolicTransition.getSourceZSES();
			
			if (parentZSES.getLocation().equals(targetLocation)) {
				counter++;
			}
			
			symbolicTransition = parentZSES.getParentTransition();
		}
		
		return counter >= 2;
	}
	
	
	// Verify if the zses is reachable, that is, if its path condition is satisfied and its zone is not empty.
	private boolean isReachable(SymbolicState zses, Map<String,TypedData> mapping) {
		return isPathConditionSatisfied(zses.getPathCondition(), mapping) && !isZoneEmpty(zses.getZone());
	}
	

	// Verify if a zone is empty.
	private boolean isZoneEmpty(String zone) {
		File file = new File(Constants.SYMBOLRTDBM_FILE_NAME);
	    Writer output = null;
		 try {
			 output = new BufferedWriter( new FileWriter(file) );
			 output.write(zone);
		 } catch (IOException e) {
			 e.printStackTrace();
			 return true;
		 }	finally {
			 if (output != null)
				 try {
					 output.close();
				 } catch (IOException e) {
					 e.printStackTrace();
					 return true;
				 }
		 }

		 String result = executeUPPAALDBM(Constants.DBM_OPCODE_IS_DBM_EMPTY, file.getAbsolutePath());
		
		 return !result.trim().equals("0");
	}
	
	
	// Verify if the path condition is satisfied using the CVC3 SMT Solver.
	private boolean isPathConditionSatisfied(String pathCondition, Map<String,TypedData> mapping) {
	     File file = new File(Constants.CVC3_FILE_NAME);
	     Writer output = null;
		 try {
			 output = new BufferedWriter( new FileWriter(file) );
		 
			 StringTokenizer st = new StringTokenizer(pathCondition, " +-*/<=>()", false);
			 while (st.hasMoreTokens()) {
				 String current = st.nextToken();
				 if (current.contains("_")) {
					 String[] str = current.split("_");
					 switch (mapping.get(str[0]).getType()) {
					 case Constants.TYPE_BOOLEAN:
						 output.write(current + " : BOOLEAN;\n");
						 break;
					 case Constants.TYPE_INTEGER:
						 output.write(current + " : INT;\n");
						 break;
					 default:
						 break;
					 }
				 }
			 }
			 output.write("\nASSERT(" + pathCondition + ");");
			 output.write("\nCHECKSAT;");
		     
		 } catch (IOException e) {
			 e.printStackTrace();
			 return false;
		 }	finally {
			 if (output != null)
				 try {
					 output.close();
				 } catch (IOException e) {
					 e.printStackTrace();
					 return false;
				 }
		 }
		 
		 String result = executeCVC3(file);
		 
		 return Constants.CVC3_RESPONSE_SATISFIABLE.equals(result);
	}
	
	
	/*private boolean isContainedInExistingZSESs(SymbolicState newZSES, List<SymbolicState> existingZSESs){
		for (SymbolicState zses : existingZSESs) {
		}
		return false;
	}*/
	
	// Verifies if newZSES is contained in existingZSES according to the definition of ZSES inclusion
	/*private boolean isContained(SymbolicState newZSES, SymbolicState existingZSES) {
		if (newZSES.getLocation().equals(existingZSES.getLocation())) {
			
		} else {
			return false;
		}
		
		
		StringBuffer expression = new StringBuffer();
		expression.append(newZSES.getPathCondition() + " " + Constants.GUARD_CONJUNCTION + " ");
		
		
		return false;
	}*/
	

	// Execute the CVC3 SMT Solver using a file containing all commands.
	private String executeCVC3(File inputFile){
		 Process process = null;
		 try {
			process = Runtime.getRuntime().exec(Config.getInstance().getCVC3AbsolutePath() + " " + inputFile.getAbsolutePath());
			process.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		BufferedReader buffer = new BufferedReader(new InputStreamReader(process.getInputStream()));
		String result = "";
		try {
			if (buffer.ready()) {
				result = buffer.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	// Execute UPPAAL DBM.
	private String executeUPPAALDBM(int operationCode, String parameters){
		 Process process = null;
		 try {
			process = Runtime.getRuntime().exec(Config.getInstance().getSymbolrtdbmPath() + " " + operationCode + " " + parameters);
			process.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		 
		
		BufferedReader buffer = new BufferedReader(new InputStreamReader(process.getInputStream()));
		String result = "";
		try {
			while (buffer.ready()) {
				result += buffer.readLine() + "\n";
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//Adriana: Clearing memory to avoid memory leaks
		process.destroy();
		try {
			buffer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}
	
	
	// Verify if the path condition is satisfied using the CVC3 SMT Solver. Used for debugging.
	@SuppressWarnings("unused")
	private boolean isPathConditionSatisfied2(String pathCondition, Map<String,TypedData> mapping){
		StringBuffer text = new StringBuffer();
		
		StringTokenizer st = new StringTokenizer(pathCondition, " +-*/<=>()", false);
	     while (st.hasMoreTokens()) {
	         String current = st.nextToken();
	    	 if (current.contains("_")) {
	    		 String[] str = current.split("_");
				 switch (mapping.get(str[0]).getType()) {
				 case Constants.TYPE_BOOLEAN:
					 text.append(current + " : BOOLEAN;\n");
					 break;
				 case Constants.TYPE_INTEGER:
					 text.append(current + " : INT;\n");
					 break;
				 default:
					 break;
				 }
	    	 }
	     }
	     
	     text.append("\nASSERT(" + pathCondition + ");");
	     text.append("\nCHECKSAT;");
	     
	     System.out.println("\n\n***********************************\n" + text.toString() + "\n***********************************");
	     
	     File file = new File(Constants.CVC3_FILE_NAME);
	     Writer output = null;
		 try {
			 output = new BufferedWriter( new FileWriter(file) );
		     output.write( text.toString() );
		 } catch (IOException e) {
			 return false;
		 }	finally {
			 if (output != null)
				 try {
					 output.close();
				 } catch (IOException e) {
					 e.printStackTrace();
					 return false;
				 }
		 }

		 String result = executeCVC3(file);
		 //String result = "Satisfiable.";
		 
		 return Constants.CVC3_RESPONSE_SATISFIABLE.equals(result);
	}
	
	
}
