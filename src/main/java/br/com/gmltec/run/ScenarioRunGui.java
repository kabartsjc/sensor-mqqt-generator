package br.com.gmltec.run;

import java.awt.Color;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

import org.beryx.textio.TextIO;
import org.beryx.textio.TextTerminal;

import br.com.gmltec.sensor.Sensor;
import br.com.gmltec.utils.ScenarioUtils;

public class ScenarioRunGui {
	public static void loadScenario(TextIO textIO) throws InterruptedException {
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

		terminal.println();
		terminal.println();

		terminal.getProperties().setPromptColor(Color.yellow);
		terminal.println("sensor-mqqt-generator Simulation Runner");

		terminal.println();
		terminal.println();

		boolean correct = false;

		String fileName = null;
		int sim_duration = 0;

		while (correct == false) {
			fileName = textIO.newStringInputReader().withDefaultValue("resources/exe.json")
					.read("What is the complete path-name of the scenario file to load ?");
			sim_duration = textIO.newIntInputReader().read("How much time (minutes) does the simulation will run?");

			terminal.println();
			terminal.getProperties().setPromptColor(Color.yellow);
			terminal.println("You selected these options:");
			terminal.println("scenario file name:" + fileName);
			terminal.println("simulation duration:" + sim_duration);

			correct = textIO.newBooleanInputReader().read("Are the information correct? [Y/N]");
		}
		terminal.println();
		terminal.getProperties().setPromptColor(Color.green);

		List<Sensor> sensorL = ScenarioUtils.parseSensorFiles(fileName);

		if (sim_duration != -1) {
			for (Sensor sensor : sensorL) {
				Thread th = new Thread(sensor);
				th.start();
			}

		}

		Instant beginTime = Instant.now();
		Duration deltaTime = Duration.ZERO;

		while (deltaTime.getSeconds() < (sim_duration * 60)) {
			deltaTime = Duration.between(beginTime, Instant.now());
		}

		for (Sensor sensor : sensorL) {
			sensor.finish();
		}

	}



}
