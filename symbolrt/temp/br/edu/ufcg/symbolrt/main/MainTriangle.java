package br.edu.ufcg.symbolrt.main;

import java.util.List;

import br.edu.ufcg.symbolrt.base.Action;
import br.edu.ufcg.symbolrt.base.Location;
import br.edu.ufcg.symbolrt.base.TIOSTS;
import br.edu.ufcg.symbolrt.base.TypedData;
import br.edu.ufcg.symbolrt.facade.SYMBOLRT;
import br.edu.ufcg.symbolrt.util.Constants;

public class MainTriangle {

	public static TIOSTS createTIOSTS(){
		TIOSTS tiosts = new TIOSTS("Triangle");
		
		// Variables, parameters, and clocks
		TypedData a = new TypedData("a", Constants.TYPE_INTEGER);
		TypedData b = new TypedData("b", Constants.TYPE_INTEGER);
		TypedData c = new TypedData("c", Constants.TYPE_INTEGER);
		TypedData p = new TypedData("p", Constants.TYPE_INTEGER);
		TypedData q = new TypedData("q", Constants.TYPE_INTEGER);
		TypedData r = new TypedData("r", Constants.TYPE_INTEGER);

		
		tiosts.addVariable(a);
		tiosts.addVariable(b);
		tiosts.addVariable(c);
		tiosts.addActionParameter(p);
		tiosts.addActionParameter(q);
		tiosts.addActionParameter(r);
		
		// Input, output, and internal actions
		Action init = new Action("Init", Constants.ACTION_INTERNAL);
		Action readSides = new Action("ReadSides", Constants.ACTION_INPUT);
		readSides.addParameter(p.getName());
		readSides.addParameter(q.getName());
		readSides.addParameter(r.getName());
		Action notTriangle = new Action("NotTriangle", Constants.ACTION_OUTPUT);
		Action isTriangle = new Action("IsTriangle", Constants.ACTION_OUTPUT);
		Action equilateral = new Action("Equilateral", Constants.ACTION_OUTPUT);
		Action isosceles = new Action("Isosceles", Constants.ACTION_OUTPUT);
		Action scalene = new Action("Scalene", Constants.ACTION_OUTPUT);

		
		tiosts.addAction(init);
		tiosts.addAction(readSides);
		tiosts.addAction(notTriangle);
		tiosts.addAction(isTriangle);
		tiosts.addAction(equilateral);
		tiosts.addAction(isosceles);
		tiosts.addAction(scalene);
		
		// Locations
		Location start = new Location("START");
		Location l1 = new Location("L1");
		Location l2 = new Location("L2");
		Location l3 = new Location("L3");
		Location l4 = new Location("L4");
		Location l5 = new Location("L5");
		Location l6 = new Location("L6");
		Location l7 = new Location("L7");

		
		tiosts.addLocation(start);
		tiosts.addLocation(l1);
		tiosts.addLocation(l2);
		tiosts.addLocation(l3);
		tiosts.addLocation(l4);
		tiosts.addLocation(l5);
		tiosts.addLocation(l6);
		tiosts.addLocation(l7);

		
		// Initial Condition
		tiosts.setInitialCondition("(a > 0) AND (b > 0) AND (c > 0)");
		
		// Initial Location
		tiosts.setInitialLocation(start);
		
		// Transitions
		tiosts.createTransition("START", "(a > 0) AND (b > 0) AND (c > 0)", Constants.GUARD_TRUE, init, null, null, "L1");
		tiosts.createTransition("L1", "(p > 0) AND (q > 0) AND (r > 0)", Constants.GUARD_TRUE, readSides, "a := p | b := q | c := r", null, "L2");
		tiosts.createTransition("L2", "(a > (b + c)) OR (b > (a + c)) OR (c > (a + b))", Constants.GUARD_TRUE, notTriangle, null, null, "L3");
		tiosts.createTransition("L2", "(a < (b + c)) AND (b < (a + c)) AND (c < (a + b))", Constants.GUARD_TRUE, isTriangle, null, null, "L4");
		tiosts.createTransition("L4", "(a = b) AND (b = c)", Constants.GUARD_TRUE, equilateral, null, null, "L5");
		tiosts.createTransition("L4", "(a <> b) AND (a <> c) AND (b <> c)", Constants.GUARD_TRUE, scalene, null, null, "L6");
		tiosts.createTransition("L4", "(NOT ((a = b) AND (b = c))) AND ((a = b) OR (b = c) OR (a = c))", Constants.GUARD_TRUE, isosceles, null, null, "L7");
		return tiosts;
	}
	
	
	// NotTriangle
	public static TIOSTS createTP1(){
		TIOSTS tp = new TIOSTS("TP1");
		
		// Input, output, and internal actions
		Action init = new Action("Init", Constants.ACTION_INTERNAL);
		Action notTriangle = new Action("NotTriangle", Constants.ACTION_OUTPUT);
		
		tp.addAction(init);
		tp.addAction(notTriangle);
		
		
		// Locations
		Location start = new Location("START");
		Location t1 = new Location("T1");
		Location accept = new Location("Accept");
		
		tp.addLocation(start);
		tp.addLocation(t1);
		tp.addLocation(accept);

		
		// Initial Condition
		tp.setInitialCondition(Constants.GUARD_TRUE);
		
		// Initial Location
		tp.setInitialLocation(start);
		
		// Transitions
		tp.createTransition("START", Constants.GUARD_TRUE, Constants.GUARD_TRUE, init, null, null, "T1");
		tp.createTransition("T1", Constants.GUARD_TRUE, Constants.GUARD_TRUE, notTriangle, null, null, "Accept");
		
		return tp;
	}

	// Equilateral
	public static TIOSTS createTP2(){
		TIOSTS tp = new TIOSTS("TP2");
		
		// Input, output, and internal actions
		Action init = new Action("Init", Constants.ACTION_INTERNAL);
		Action equilateral = new Action("Equilateral", Constants.ACTION_OUTPUT);
		
		tp.addAction(init);
		tp.addAction(equilateral);
		
		
		// Locations
		Location start = new Location("START");
		Location t1 = new Location("T1");
		Location accept = new Location("Accept");
		
		tp.addLocation(start);
		tp.addLocation(t1);
		tp.addLocation(accept);

		
		// Initial Condition
		tp.setInitialCondition(Constants.GUARD_TRUE);
		
		// Initial Location
		tp.setInitialLocation(start);
		
		// Transitions
		tp.createTransition("START", Constants.GUARD_TRUE, Constants.GUARD_TRUE, init, null, null, "T1");
		tp.createTransition("T1", Constants.GUARD_TRUE, Constants.GUARD_TRUE, equilateral, null, null, "Accept");
		
		return tp;
	}
	
	
	// Isosceles
	public static TIOSTS createTP3(){
		TIOSTS tp = new TIOSTS("TP3");
		
		// Input, output, and internal actions
		Action init = new Action("Init", Constants.ACTION_INTERNAL);
		Action isosceles = new Action("Isosceles", Constants.ACTION_OUTPUT);
		
		tp.addAction(init);
		tp.addAction(isosceles);
		
		
		// Locations
		Location start = new Location("START");
		Location t1 = new Location("T1");
		Location accept = new Location("Accept");
		
		tp.addLocation(start);
		tp.addLocation(t1);
		tp.addLocation(accept);

		
		// Initial Condition
		tp.setInitialCondition(Constants.GUARD_TRUE);
		
		// Initial Location
		tp.setInitialLocation(start);
		
		// Transitions
		tp.createTransition("START", Constants.GUARD_TRUE, Constants.GUARD_TRUE, init, null, null, "T1");
		tp.createTransition("T1", Constants.GUARD_TRUE, Constants.GUARD_TRUE, isosceles, null, null, "Accept");
		
		return tp;
	}

	
	// Scaelene
	public static TIOSTS createTP4(){
		TIOSTS tp = new TIOSTS("TP3");
		
		// Input, output, and internal actions
		Action init = new Action("Init", Constants.ACTION_INTERNAL);
		Action scalene = new Action("Scalene", Constants.ACTION_OUTPUT);
		
		tp.addAction(init);
		tp.addAction(scalene);
		
		
		// Locations
		Location start = new Location("START");
		Location t1 = new Location("T1");
		Location accept = new Location("Accept");
		
		tp.addLocation(start);
		tp.addLocation(t1);
		tp.addLocation(accept);

		
		// Initial Condition
		tp.setInitialCondition(Constants.GUARD_TRUE);
		
		// Initial Location
		tp.setInitialLocation(start);
		
		// Transitions
		tp.createTransition("START", Constants.GUARD_TRUE, Constants.GUARD_TRUE, init, null, null, "T1");
		tp.createTransition("T1", Constants.GUARD_TRUE, Constants.GUARD_TRUE, scalene, null, null, "Accept");
		
		return tp;
	}

	
	public static void main(String[] args) {
		
		SYMBOLRT symbolrt = SYMBOLRT.getInstance();
		TIOSTS tp = createTP1(); // NotTriangle
		//TIOSTS tp = createTP2(); // Equilateral
		//TIOSTS tp = createTP3(); // Isosceles
		//TIOSTS tp = createTP4(); // Scalene
		List<TIOSTS> testCases = symbolrt.generateTestCases(createTIOSTS(), tp, true);
		symbolrt.show(testCases);
		symbolrt.store(testCases);
	
	}

	

	
}
