/*
 * SYmbolic Model-Based test case generation toOL for Real-Time systems (SYMBOLRT)
 * (C) Copyright 2010-2012 Federal University of Campina Grande (UFCG)
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

import br.edu.ufcg.symbolrt.base.Action;
import br.edu.ufcg.symbolrt.base.Location;
import br.edu.ufcg.symbolrt.base.TIOSTS;
import br.edu.ufcg.symbolrt.base.TypedData;
import br.edu.ufcg.symbolrt.util.Constants;

public class UtilTest {

	public static TIOSTS createCoffeeSPEC() {
		TIOSTS tiosts = new TIOSTS("CoffeeMachine");

		// Variables
		TypedData price = new TypedData("price", Constants.TYPE_INTEGER);
		tiosts.addVariable(price);
		TypedData total = new TypedData("total", Constants.TYPE_INTEGER);
		tiosts.addVariable(total);
		TypedData drink = new TypedData("drink", Constants.TYPE_BOOLEAN);
		tiosts.addVariable(drink);

		// Action parameters
		TypedData coinParameter = new TypedData("coin", Constants.TYPE_INTEGER);
		tiosts.addActionParameter(coinParameter);
		TypedData drinkRequestParameter = new TypedData("drinkRequest",
				Constants.TYPE_BOOLEAN);
		tiosts.addActionParameter(drinkRequestParameter);
		TypedData moneyBackParameter = new TypedData("moneyBack",
				Constants.TYPE_INTEGER);
		tiosts.addActionParameter(moneyBackParameter);

		// Input, output, and internal actions
		Action init = new Action("Init", Constants.ACTION_INTERNAL);
		tiosts.addAction(init);
		Action coin = new Action("Coin", Constants.ACTION_INPUT);
		coin.addParameter("coin");
		tiosts.addAction(coin);
		Action cancel = new Action("Cancel", Constants.ACTION_INPUT);
		tiosts.addAction(cancel);
		Action chooseBeverage = new Action("ChooseBeverage",
				Constants.ACTION_INPUT);
		chooseBeverage.addParameter("drinkRequest");
		tiosts.addAction(chooseBeverage);
		Action returnAction = new Action("Return", Constants.ACTION_OUTPUT);
		returnAction.addParameter("moneyBack");
		tiosts.addAction(returnAction);
		Action deliver = new Action("Deliver", Constants.ACTION_OUTPUT);
		deliver.addParameter("drinkRequest");
		tiosts.addAction(deliver);

		// Locations
		Location start = new Location("START");
		tiosts.addLocation(start);
		Location idle = new Location("Idle");
		tiosts.addLocation(idle);
		Location pay = new Location("Pay");
		tiosts.addLocation(pay);
		Location choose = new Location("Choose");
		tiosts.addLocation(choose);
		Location returnLocation = new Location("Return");
		tiosts.addLocation(returnLocation);
		Location delivery = new Location("Delivery");
		tiosts.addLocation(delivery);

		// Initial Condition
		tiosts.setInitialCondition("price > 0");

		// Initial Location
		tiosts.setInitialLocation(start);

		// Transitions
		tiosts.createTransition("START", "price > 0", Constants.GUARD_TRUE,
				init, "total := 0", null, "Idle");
		tiosts.createTransition("Idle", "coin > 0", Constants.GUARD_TRUE, coin,
				"total := total + coin", null, "Pay");
		tiosts.createTransition("Idle", Constants.GUARD_TRUE,
				Constants.GUARD_TRUE, cancel, null, null, "Return");
		tiosts.createTransition("Return", "moneyBack = total",
				Constants.GUARD_TRUE, returnAction, "total := 0", null, "Idle");
		tiosts.createTransition("Pay",
				"(total < price) AND (moneyBack = total)",
				Constants.GUARD_TRUE, returnAction, "total := 0", null, "Idle");
		tiosts.createTransition("Pay",
				"(total >= price) AND (moneyBack = total - price)",
				Constants.GUARD_TRUE, returnAction, "total := price", null,
				"Choose");
		tiosts.createTransition("Choose", "total = price",
				Constants.GUARD_TRUE, chooseBeverage, "drink := drinkRequest",
				null, "Delivery");
		tiosts.createTransition("Choose", Constants.GUARD_TRUE,
				Constants.GUARD_TRUE, cancel, null, null, "Return");
		tiosts.createTransition("Delivery", "drink <=> drinkRequest",
				Constants.GUARD_TRUE, deliver, "total := 0", null, "Idle");

		return tiosts;
	}

	public static TIOSTS createCoffeeTP() {
		TIOSTS tiosts = new TIOSTS("TP");

		// Variables
		TypedData price = new TypedData("price", Constants.TYPE_INTEGER);
		tiosts.addActionParameter(price);
		TypedData total = new TypedData("total", Constants.TYPE_INTEGER);
		tiosts.addActionParameter(total);

		// Input, output, and internal actions
		Action init = new Action("Init", Constants.ACTION_INTERNAL);
		tiosts.addAction(init);
		Action cancel = new Action("Cancel", Constants.ACTION_INPUT);
		tiosts.addAction(cancel);
		Action returnAction = new Action("Return", Constants.ACTION_OUTPUT);
		returnAction.addParameter("moneyBack");
		tiosts.addAction(returnAction);
		Action deliver = new Action("Deliver", Constants.ACTION_OUTPUT);
		deliver.addParameter("drinkRequest");
		tiosts.addAction(deliver);

		// Locations
		Location start = new Location("START");
		tiosts.addLocation(start);
		Location begin = new Location("Begin");
		tiosts.addLocation(begin);
		Location accept = new Location("Accept");
		tiosts.addLocation(accept);
		Location reject = new Location("Reject");
		tiosts.addLocation(reject);

		// Initial Condition
		tiosts.setInitialCondition(Constants.GUARD_TRUE);

		// Initial Location
		tiosts.setInitialLocation(start);

		// Transitions
		tiosts.createTransition("START", Constants.GUARD_TRUE,
				Constants.GUARD_TRUE, init, null, null, "Begin");
		tiosts.createTransition("Begin", "drinkRequest <=> TRUE",
				Constants.GUARD_TRUE, deliver, null, null, "Accept");
		tiosts.createTransition("Begin", Constants.GUARD_TRUE,
				Constants.GUARD_TRUE, cancel, null, null, "Reject");
		tiosts.createTransition("Begin", "total < price", Constants.GUARD_TRUE,
				returnAction, null, null, "Reject");

		return tiosts;
	}

	public static TIOSTS createAtmSPEC() {
		TIOSTS tiosts = new TIOSTS("WithdrawalTransation1");

		// Variables, parameters, and clocks
		TypedData balance = new TypedData("balance", Constants.TYPE_INTEGER);
		TypedData withdrawalValue = new TypedData("withdrawalValue",
				Constants.TYPE_INTEGER);
		TypedData amount = new TypedData("amount", Constants.TYPE_INTEGER);
		String clock = "clock";

		tiosts.addVariable(balance);
		tiosts.addVariable(withdrawalValue);
		tiosts.addActionParameter(amount);
		tiosts.addClock(clock);

		// Input, output, and internal actions
		Action init = new Action("Init", Constants.ACTION_INTERNAL);
		Action withdrawal = new Action("Withdrawal", Constants.ACTION_INPUT);
		withdrawal.addParameter(amount.getName());
		Action insufficientFunds = new Action("InsufficientFunds",
				Constants.ACTION_OUTPUT);
		insufficientFunds.addParameter(amount.getName());
		Action printBalance = new Action("PrintBalance",
				Constants.ACTION_OUTPUT);
		printBalance.addParameter(amount.getName());
		Action dispenseCash = new Action("DispenseCash",
				Constants.ACTION_OUTPUT);
		dispenseCash.addParameter(amount.getName());

		tiosts.addAction(init);
		tiosts.addAction(withdrawal);
		tiosts.addAction(insufficientFunds);
		tiosts.addAction(printBalance);
		tiosts.addAction(dispenseCash);

		// Locations
		Location start = new Location("START");
		Location idle = new Location("Idle");
		Location verify = new Location("Verify");
		Location print = new Location("Print");

		tiosts.addLocation(start);
		tiosts.addLocation(idle);
		tiosts.addLocation(verify);
		tiosts.addLocation(print);

		// Initial Condition
		tiosts.setInitialCondition("balance > 0");

		// Initial Location
		tiosts.setInitialLocation(start);

		// Transitions
		tiosts.createTransition("START", "balance > 0", Constants.GUARD_TRUE,
				init, null, null, "Idle");
		tiosts.createTransition("Idle", "amount > 0", Constants.GUARD_TRUE,
				withdrawal, "withdrawalValue := amount", "clock := 0", "Verify");
		tiosts.createTransition("Verify",
				"amount = withdrawalValue AND withdrawalValue <= balance",
				"clock <= 10", dispenseCash,
				"balance := balance - withdrawalValue", null, "Idle");
		tiosts.createTransition("Verify",
				"amount = withdrawalValue AND withdrawalValue > balance",
				"clock <= 2", insufficientFunds, null, "clock := 0", "Print");
		tiosts.createTransition("Print", "amount = balance", "clock <= 5",
				printBalance, null, null, "Idle");
		return tiosts;
	}

	public static TIOSTS createAtmTP() {
		TIOSTS tp = new TIOSTS("TP1");

		// Variables, parameters, and clocks
		TypedData amount = new TypedData("amount", Constants.TYPE_INTEGER);
		tp.addActionParameter(amount);

		// Input, output, and internal actions
		Action init = new Action("Init", Constants.ACTION_INTERNAL);
		Action withdrawal = new Action("Withdrawal", Constants.ACTION_INPUT);
		withdrawal.addParameter(amount.getName());
		Action insufficientFunds = new Action("InsufficientFunds",
				Constants.ACTION_OUTPUT);
		insufficientFunds.addParameter(amount.getName());
		Action dispenseCash = new Action("DispenseCash",
				Constants.ACTION_OUTPUT);
		dispenseCash.addParameter(amount.getName());

		tp.addAction(init);
		tp.addAction(withdrawal);
		tp.addAction(insufficientFunds);
		tp.addAction(dispenseCash);

		// Locations
		Location start = new Location("START");
		Location enterAmount = new Location("EnterAmount");
		Location verify = new Location("Verify");
		Location accept = new Location("Accept");
		Location reject = new Location("Reject");

		tp.addLocation(start);
		tp.addLocation(enterAmount);
		tp.addLocation(verify);
		tp.addLocation(accept);
		tp.addLocation(reject);

		// Initial Condition
		tp.setInitialCondition(Constants.GUARD_TRUE);

		// Initial Location
		tp.setInitialLocation(start);

		// Transitions
		tp.createTransition("START", Constants.GUARD_TRUE,
				Constants.GUARD_TRUE, init, null, null, "EnterAmount");
		tp.createTransition("EnterAmount", Constants.GUARD_TRUE,
				Constants.GUARD_TRUE, withdrawal, null, null, "Verify");
		tp.createTransition("Verify", Constants.GUARD_TRUE,
				Constants.GUARD_TRUE, dispenseCash, null, null, "Accept");
		tp.createTransition("Verify", Constants.GUARD_TRUE,
				Constants.GUARD_TRUE, insufficientFunds, null, null, "Reject");

		return tp;
	}

	public static TIOSTS createAlarmSPEC() {
		TIOSTS tiosts = new TIOSTS("BurglarAlarmSystem");

		// Variables
		TypedData room = new TypedData("room", Constants.TYPE_INTEGER);
		TypedData invasionType = new TypedData("invasionType",
				Constants.TYPE_INTEGER);
		TypedData choice = new TypedData("choice", Constants.TYPE_INTEGER);
		// Action Parameters
		TypedData roomNumber = new TypedData("roomNumber",
				Constants.TYPE_INTEGER);
		TypedData windowNumber = new TypedData("windowNumber",
				Constants.TYPE_INTEGER);
		TypedData doorNumber = new TypedData("doorNumber",
				Constants.TYPE_INTEGER);
		TypedData movSensorNumber = new TypedData("movSensorNumber",
				Constants.TYPE_INTEGER);
		TypedData invType = new TypedData("invType", Constants.TYPE_INTEGER);
		TypedData intCode = new TypedData("intCode", Constants.TYPE_INTEGER);
		// Clocks
		String clock = "clock";
		String interruptionClock = "interruptionClock";

		tiosts.addVariable(room);
		tiosts.addVariable(invasionType);
		tiosts.addVariable(choice);

		tiosts.addActionParameter(roomNumber);
		tiosts.addActionParameter(invType);
		tiosts.addActionParameter(intCode);
		tiosts.addActionParameter(windowNumber);
		tiosts.addActionParameter(doorNumber);
		tiosts.addActionParameter(movSensorNumber);

		tiosts.addClock(clock);
		tiosts.addClock(interruptionClock);

		// Internal Action
		Action init = new Action("Init", Constants.ACTION_INTERNAL);

		// Input Actions
		Action verifyInvasion = new Action("VerifyInvasion",
				Constants.ACTION_INPUT);
		verifyInvasion.addParameter(roomNumber.getName());
		verifyInvasion.addParameter(invType.getName());

		Action interrupt = new Action("Interrupt", Constants.ACTION_INPUT);
		interrupt.addParameter(intCode.getName());

		Action powerFailInterrupt = new Action("PowerFailInterrupt",
				Constants.ACTION_INPUT);

		// Output Actions
		Action noInvasionDetected = new Action("NoInvasionDetected",
				Constants.ACTION_OUTPUT);

		Action error = new Action("Error", Constants.ACTION_OUTPUT);

		Action triggerWindowBreakingAlarm = new Action(
				"TriggerWindowBreakingAlarm", Constants.ACTION_OUTPUT);
		triggerWindowBreakingAlarm.addParameter(windowNumber.getName());

		Action triggerDoorOpeningAlarm = new Action("TriggerDoorOpeningAlarm",
				Constants.ACTION_OUTPUT);
		triggerDoorOpeningAlarm.addParameter(doorNumber.getName());

		Action triggerRoomMovementAlarm = new Action(
				"TriggerRoomMovementAlarm", Constants.ACTION_OUTPUT);
		triggerRoomMovementAlarm.addParameter(movSensorNumber.getName());

		Action callPolice = new Action("CallPolice", Constants.ACTION_OUTPUT);
		callPolice.addParameter(roomNumber.getName());

		Action turnOnLights = new Action("TurnOnLights",
				Constants.ACTION_OUTPUT);
		turnOnLights.addParameter(roomNumber.getName());

		Action transferToBackupPower = new Action("TransferToBackupPower",
				Constants.ACTION_OUTPUT);

		Action reactivateAlarmSystem = new Action("ReactivateAlarmSystem",
				Constants.ACTION_OUTPUT);

		tiosts.addAction(init);
		tiosts.addAction(verifyInvasion);
		tiosts.addAction(interrupt);
		tiosts.addAction(powerFailInterrupt);

		tiosts.addAction(noInvasionDetected);
		tiosts.addAction(error);
		tiosts.addAction(triggerWindowBreakingAlarm);
		tiosts.addAction(triggerDoorOpeningAlarm);
		tiosts.addAction(triggerRoomMovementAlarm);
		tiosts.addAction(callPolice);
		tiosts.addAction(turnOnLights);
		tiosts.addAction(transferToBackupPower);
		tiosts.addAction(reactivateAlarmSystem);

		// Locations
		Location start = new Location("START");
		Location s1 = new Location("S1");
		Location s2 = new Location("S2");
		Location s3 = new Location("S3");
		Location s4 = new Location("S4");
		Location s5 = new Location("S5");
		Location s6 = new Location("S6");
		Location i1 = new Location("I1");
		Location i2 = new Location("I2");
		Location i3 = new Location("I3");

		tiosts.addLocation(start);
		tiosts.addLocation(s1);
		tiosts.addLocation(s2);
		tiosts.addLocation(s3);
		tiosts.addLocation(s4);
		tiosts.addLocation(s5);
		tiosts.addLocation(s6);
		tiosts.addLocation(i1);
		tiosts.addLocation(i2);
		tiosts.addLocation(i3);

		// Initial Condition
		tiosts.setInitialCondition(Constants.GUARD_TRUE);

		// Initial Location
		tiosts.setInitialLocation(start);

		// Transitions
		tiosts.createTransition("START", Constants.GUARD_TRUE,
				Constants.GUARD_TRUE, init, null, null, "S1");
		tiosts.createTransition("S1", Constants.GUARD_TRUE,
				Constants.GUARD_TRUE, verifyInvasion,
				"room := roomNumber | invasionType := invType", "clock := 0",
				"S2");
		tiosts.createTransition("S2", "invasionType = 0", Constants.GUARD_TRUE,
				noInvasionDetected, null, null, "S1");
		tiosts.createTransition("S2", "invasionType > 280",
				Constants.GUARD_TRUE, error, null, null, "S6");
		tiosts.createTransition(
				"S2",
				"invasionType >= 1 AND invasionType <= 50 AND invasionType = windowNumber",
				"clock <= 500", triggerWindowBreakingAlarm, null, "clock := 0",
				"S3");
		tiosts.createTransition(
				"S2",
				"invasionType >= 51 AND invasionType <= 80 AND invasionType = doorNumber",
				"clock <= 500", triggerDoorOpeningAlarm, null, "clock := 0",
				"S3");
		tiosts.createTransition(
				"S2",
				"invasionType >= 81 AND invasionType <= 280 AND invasionType = movSensorNumber",
				"clock <= 500", triggerRoomMovementAlarm, null, "clock := 0",
				"S3");
		tiosts.createTransition("S3", "room = roomNumber", "clock <= 4000",
				callPolice, null, "clock := 0", "S4");
		tiosts.createTransition("S4", "room = roomNumber", "clock <= 500",
				turnOnLights, null, null, "S5");

		tiosts.createTransition("I1", Constants.GUARD_TRUE,
				Constants.GUARD_TRUE, powerFailInterrupt, null,
				"interruptionClock := 0", "I2");
		tiosts.createTransition("I2", Constants.GUARD_TRUE,
				Constants.GUARD_TRUE, transferToBackupPower, null, null, "I3");

		// intCode = 1
		tiosts.createTransition("S1", "intCode = 1 AND choice = 0",
				Constants.GUARD_TRUE, interrupt, "choice := intCode", null,
				"I1");
		tiosts.createTransition("I3", "choice = 1", "interruptionClock <= 50",
				reactivateAlarmSystem, null, null, "S1");

		// intCode = 2
		tiosts.createTransition("S6", "intCode = 2 AND choice = 0",
				Constants.GUARD_TRUE, interrupt, "choice := intCode", null,
				"I1");
		tiosts.createTransition("I3", "choice = 2", "interruptionClock <= 50",
				reactivateAlarmSystem, null, null, "S6");

		// intCode = 3
		tiosts.createTransition("S3", "intCode = 3 AND choice = 0",
				Constants.GUARD_TRUE, interrupt, "choice := intCode", null,
				"I1");
		tiosts.createTransition("I3", "choice = 3", "interruptionClock <= 50",
				reactivateAlarmSystem, null, null, "S3");

		// intCode = 4
		tiosts.createTransition("S4", "intCode = 4 AND choice = 0",
				Constants.GUARD_TRUE, interrupt, "choice := intCode", null,
				"I1");
		tiosts.createTransition("I3", "choice = 4", "interruptionClock <= 50",
				reactivateAlarmSystem, null, null, "S4");

		// intCode = 5
		tiosts.createTransition("S5", "intCode = 5 AND choice = 0",
				Constants.GUARD_TRUE, interrupt, "choice := intCode", null,
				"I1");
		tiosts.createTransition("I3", "choice = 5", "interruptionClock <= 50",
				reactivateAlarmSystem, null, null, "S5");

		return tiosts;
	}

	public static TIOSTS createAlarmTP() {
		TIOSTS tp = new TIOSTS("TP1");

		// Variables, parameters, and clocks
		TypedData intCode = new TypedData("intCode", Constants.TYPE_INTEGER);
		tp.addActionParameter(intCode);
		TypedData roomNumber = new TypedData("roomNumber",
				Constants.TYPE_INTEGER);
		tp.addActionParameter(roomNumber);

		// Input, output, and internal actions
		Action init = new Action("Init", Constants.ACTION_INTERNAL);

		Action interrupt = new Action("Interrupt", Constants.ACTION_INPUT);
		interrupt.addParameter(intCode.getName());

		Action turnOnLights = new Action("TurnOnLights",
				Constants.ACTION_OUTPUT);
		turnOnLights.addParameter(roomNumber.getName());

		Action error = new Action("Error", Constants.ACTION_OUTPUT);

		Action noInvasionDetected = new Action("NoInvasionDetected",
				Constants.ACTION_OUTPUT);

		tp.addAction(init);
		tp.addAction(interrupt);
		tp.addAction(turnOnLights);
		tp.addAction(error);
		tp.addAction(noInvasionDetected);

		// Locations
		Location start = new Location("START");
		Location tp1 = new Location("TP1");
		Location tp2 = new Location("TP2");
		Location accept = new Location("Accept");
		Location reject = new Location("Reject");

		tp.addLocation(start);
		tp.addLocation(tp1);
		tp.addLocation(tp2);
		tp.addLocation(accept);
		tp.addLocation(reject);

		// Initial Condition
		tp.setInitialCondition(Constants.GUARD_TRUE);

		// Initial Location
		tp.setInitialLocation(start);

		// Transitions
		tp.createTransition("START", Constants.GUARD_TRUE,
				Constants.GUARD_TRUE, init, null, null, "TP1");
		tp.createTransition("TP1", "intCode = 3", Constants.GUARD_TRUE,
				interrupt, null, null, "TP2");
		tp.createTransition("TP2", Constants.GUARD_TRUE, Constants.GUARD_TRUE,
				turnOnLights, null, null, "Accept");
		tp.createTransition("TP1", Constants.GUARD_TRUE, Constants.GUARD_TRUE,
				error, null, null, "Reject");
		tp.createTransition("TP1", Constants.GUARD_TRUE, Constants.GUARD_TRUE,
				noInvasionDetected, null, null, "Reject");

		return tp;
	}

	public static TIOSTS createTriangleSPEC() {
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
		tiosts.createTransition("START", "(a > 0) AND (b > 0) AND (c > 0)",
				Constants.GUARD_TRUE, init, null, null, "L1");
		tiosts.createTransition("L1", "(p > 0) AND (q > 0) AND (r > 0)",
				Constants.GUARD_TRUE, readSides, "a := p | b := q | c := r",
				null, "L2");
		tiosts.createTransition("L2",
				"(a > (b + c)) OR (b > (a + c)) OR (c > (a + b))",
				Constants.GUARD_TRUE, notTriangle, null, null, "L3");
		tiosts.createTransition("L2",
				"(a < (b + c)) AND (b < (a + c)) AND (c < (a + b))",
				Constants.GUARD_TRUE, isTriangle, null, null, "L4");
		tiosts.createTransition("L4", "(a = b) AND (b = c)",
				Constants.GUARD_TRUE, equilateral, null, null, "L5");
		tiosts.createTransition("L4", "(a <> b) AND (a <> c) AND (b <> c)",
				Constants.GUARD_TRUE, scalene, null, null, "L6");
		tiosts.createTransition(
				"L4",
				"(NOT ((a = b) AND (b = c))) AND ((a = b) OR (b = c) OR (a = c))",
				Constants.GUARD_TRUE, isosceles, null, null, "L7");
		return tiosts;
	}

}
