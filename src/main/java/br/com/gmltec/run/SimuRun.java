package br.com.gmltec.run;

import java.awt.Color;

import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
import org.beryx.textio.TextTerminal;

public class SimuRun {

	public static void main(String args[]) throws InterruptedException {

		TextIO textIO = TextIoFactory.getTextIO();
		TextTerminal<?> terminal = textIO.getTextTerminal();
		terminal.getProperties().setPaneDimension(850, 480);
		terminal.getProperties().setInputColor(Color.yellow);

		banner(textIO);

		while (true) {
			String option = selectionMode(textIO).toUpperCase();

			if (option.equals("R")) {
				ScenarioRunGui.loadScenario(textIO);

			}

			else if (option.equals("C")) {
				ScenarioCreatorGui.scenarioCreation(textIO);
			}

			else {
				System.exit(0);
			}

		}

	}

	private static void banner(TextIO textIO) throws InterruptedException {
		TextTerminal<?> terminal = textIO.getTextTerminal();
		String print = "#";
		for (int j = 0; j < 3; j++) {
			for (int i = 0; i < 90; i++) {
				print.concat("##");
				terminal.printf(print);
				Thread.sleep(5);
			}
			terminal.println();
		}

		terminal.println("\r\n" + "                                   ,                         ,       \r\n"
				+ " __ _ ._  __ _ ._. ___ ._ _  _. _.-+- ___  _  _ ._  _ ._. _.-+- _ ._.\r\n"
				+ "_) (/,[ )_) (_)[       [ | )(_](_] |      (_](/,[ )(/,[  (_] | (_)[  \r\n"
				+ "                              |  |        ._|                        \r\n" + "");

		terminal.getProperties().setPromptColor(Color.yellow);
		terminal.println("Welcome to sensor-mqqt-generator !!");

		terminal.println();
		terminal.getProperties().setPromptColor(Color.green);
		terminal.println("The project sensor-mqqt-generator is a Java opensource (GNU) project which aims");
		terminal.println("to simulate a vast sensor network that communicates with a Broker using MQQT");
		terminal.println("messages.Unfortunately, the current implementation does not support security");
		terminal.println("schema like TLS/SSL connections.");

		terminal.println();

		print = "#";
		for (int j = 0; j < 3; j++) {
			for (int i = 0; i < 90; i++) {
				print.concat("##");
				terminal.printf(print);
				Thread.sleep(10);
			}

			terminal.println();
		}
	}

	private static String selectionMode(TextIO textIO) {
		TextTerminal<?> terminal = textIO.getTextTerminal();

		terminal.println();
		terminal.println();
		terminal.println("Select the desired option:");
		terminal.println("Press R (r) to run a simulation using a existent scenario!");
		terminal.println("Press C (c) to create a new scenario!");
		terminal.println("Press X (x) to exit!");

		String option = textIO.newStringInputReader().withDefaultValue("R").read("Option").toUpperCase();
		terminal.println("You selected: " + option + " !!");

		return option;
	}

	
}
