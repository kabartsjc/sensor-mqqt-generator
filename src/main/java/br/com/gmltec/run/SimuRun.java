package br.com.gmltec.run;

import java.awt.Color;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
import org.beryx.textio.TextTerminal;

import br.com.gmltec.geo.Coordinate;
import br.com.gmltec.scenario.ScenarioGenerator;
import br.com.gmltec.sensor.ISensor;
import br.com.gmltec.sensor.Sensor;
import br.com.gmltec.utils.ConfigUtils;

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
				loadScenario(textIO);

			}

			else if (option.equals("C")) {
				scenarioCreation(textIO);
			}

			else {
				System.exit(0);
			}

		}

	}

	private static void loadScenario(TextIO textIO) throws InterruptedException {
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

		List<Sensor> sensorL = ConfigUtils.parseSensorFiles(fileName);

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

	private static void scenarioCreation(TextIO textIO) throws InterruptedException {

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
		terminal.println("sensor-mqqt-generator Scenario Editor");

		terminal.println();
		terminal.getProperties().setPromptColor(Color.green);
		terminal.println("This editor will allow you to create complex and straightforward scenarios via commands.");
		terminal.println("The current implementation supports the following sensor types: ultrasonic, temperature,");
		terminal.println("smoke and humidity, and rain.");

		terminal.println();
		terminal.println();

		boolean correct = false;

		String broker_addr = null;
		int default_qos = 0;
		String pathFile = null;
		String fileName = null;

		while (correct == false) {
			broker_addr = textIO.newStringInputReader().withDefaultValue("127.0.0.1:1883").read("Broker Address:");
			default_qos = textIO.newIntInputReader().withDefaultValue(0).read("Default qos value {0,1,2}:");
			pathFile = textIO.newStringInputReader().withDefaultValue("resources")
					.read("What is the directory where you want to save your file (full path)?");
			fileName = textIO.newStringInputReader().withDefaultValue("exe.json")
					.read("What is the name of the scenario file to be generated?");

			terminal.println();
			terminal.getProperties().setPromptColor(Color.yellow);
			terminal.println("You selected these options:");
			terminal.println("broker address:" + broker_addr);
			terminal.println("default qos value:" + default_qos);
			terminal.println("path[/]:" + pathFile);
			terminal.println("scenario file name:" + fileName);
			correct = textIO.newBooleanInputReader().read("Are the information correct? [Y/N]");
		}
		terminal.println();
		terminal.getProperties().setPromptColor(Color.green);

		int total_ultrasom = 1;
		double max_distance_cm = 0;
		double min_distance_cm = 0;
		int ultra_update_rate = 0;
		correct = false;
		while (correct == false) {
			total_ultrasom = textIO.newIntInputReader().read("How many Ultrasonic sensors do you want to create?");
			if (total_ultrasom > 0) {
				max_distance_cm = textIO.newDoubleInputReader().withDefaultValue(40.0)
						.read("Define the max distance value that could be measured by the sensor:");
				min_distance_cm = textIO.newDoubleInputReader().withDefaultValue(0.0)
						.read("Define the min distance value that could be measured by the sensor:");
				ultra_update_rate = textIO.newIntInputReader().withDefaultValue(3000)
						.read("What is the update rate (miliseconds) of the ultrasonic sensor?");
				terminal.println();
				terminal.getProperties().setPromptColor(Color.yellow);
				terminal.println("You selected these options:");
				terminal.println("number of ultrasonic sensors:" + total_ultrasom);
				terminal.println("max distance value:" + max_distance_cm);
				terminal.println("min distance value:" + min_distance_cm);
				terminal.println("update rate value:" + ultra_update_rate);
				correct = textIO.newBooleanInputReader().read("Are the information correct? [Y/N]");
			} else {
				terminal.println();
				terminal.getProperties().setPromptColor(Color.yellow);
				terminal.println("You set the number of ultrasonic sensors = 0");
				correct = textIO.newBooleanInputReader().read("Are the information correct? [Y/N]");
			}

		}
		terminal.println();
		terminal.getProperties().setPromptColor(Color.green);

		int total_temperature = 1;
		double max_temp_celsius = 0;
		double min_temp_celsius = 0;
		int temp_update_rate = 0;
		correct = false;
		while (correct == false) {
			total_temperature = textIO.newIntInputReader().read("How many Temperature sensors do you want to create?");
			if (total_temperature > 0) {
				max_temp_celsius = textIO.newDoubleInputReader().withDefaultValue(70.0)
						.read("Define the max temperature value that could be measured by the sensor:");
				min_temp_celsius = textIO.newDoubleInputReader().withDefaultValue(1.0)
						.read("Define the min temperature value that could be measured by the sensor:");
				temp_update_rate = textIO.newIntInputReader().withDefaultValue(3000)
						.read("What is the update rate (miliseconds) of the temperature sensor?");
				terminal.println();

				terminal.getProperties().setPromptColor(Color.yellow);
				terminal.println("You selected these options:");
				terminal.println("number of temperature sensors:" + total_temperature);
				terminal.println("max temperature value:" + max_temp_celsius);
				terminal.println("min temperature value:" + min_temp_celsius);
				terminal.println("update rate value:" + temp_update_rate);
				correct = textIO.newBooleanInputReader().read("Are the information correct? [Y/N]");
			} else {
				terminal.println();
				terminal.getProperties().setPromptColor(Color.yellow);
				terminal.println("You set the number of temperature sensors = 0");
				correct = textIO.newBooleanInputReader().read("Are the information correct? [Y/N]");
			}

		}
		terminal.println();
		terminal.getProperties().setPromptColor(Color.green);

		int total_smoke = 1;
		int smoke_update_rate = 0;
		correct = false;
		while (correct == false) {
			total_smoke = textIO.newIntInputReader().read("How many Smoke Detector sensors do you want to create?");
			if (total_smoke > 0) {
				smoke_update_rate = textIO.newIntInputReader().withDefaultValue(3000)
						.read("What is the update rate of the smoke sensor detector?");
				terminal.println();

				terminal.getProperties().setPromptColor(Color.yellow);
				terminal.println("You selected these options:");
				terminal.println("number of smoke detector sensors:" + total_smoke);
				terminal.println("update rate (miliseconds) value:" + smoke_update_rate);
				correct = textIO.newBooleanInputReader().read("Are the information correct? [Y/N]");

			} else {
				terminal.println();
				terminal.getProperties().setPromptColor(Color.yellow);
				terminal.println("You set the number of smoke detector sensors = 0");
				correct = textIO.newBooleanInputReader().read("Are the information correct? [Y/N]");
			}
		}
		terminal.println();
		terminal.getProperties().setPromptColor(Color.green);

		int total_humidity_rain = 1;
		int hum_update_rate = 0;
		correct = false;
		while (correct == false) {
			total_humidity_rain = textIO.newIntInputReader()
					.read("How many Humidity / Rain Detector sensors do you want to create?");
		
			if (total_humidity_rain > 0) {
				hum_update_rate = textIO.newIntInputReader().withDefaultValue(3000)
						.read("What is the update rate of the humidit sensor detector?");
				terminal.println();

				terminal.getProperties().setPromptColor(Color.yellow);
				terminal.println("You selected these options:");
				terminal.println("number of humidity / rain detector sensors:" + total_humidity_rain);
				terminal.println("update rate (miliseconds) value:" + hum_update_rate);
				correct = textIO.newBooleanInputReader().read("Are the information correct? [Y/N]");

			} else {
				terminal.println();
				terminal.getProperties().setPromptColor(Color.yellow);
				terminal.println("You set the number of humidity/rain sensors = 0");
				correct = textIO.newBooleanInputReader().read("Are the information correct? [Y/N]");
			}

		}
		terminal.println();
		terminal.getProperties().setPromptColor(Color.green);

		terminal.println();

		Coordinate centralPoint = null;
		double range = 0;
		correct = false;
		while (correct == false) {
			double central_longitude = textIO.newDoubleInputReader()
					.read("Define the central point (latitude) where the sensors are located:");
			double central_latitude = textIO.newDoubleInputReader()
					.read("Define the central point (longitude) where the sensors are located:");
			centralPoint = new Coordinate(central_latitude, central_longitude, 0);
			range = textIO.newDoubleInputReader()
					.read("Define the maximum range from the central point where the sensors could be located:");

			terminal.getProperties().setPromptColor(Color.yellow);
			terminal.println("You selected these options:");
			terminal.println("central point coordination [lat/long]:" + central_latitude + "/" + central_longitude);
			terminal.println("range:" + range);
			correct = textIO.newBooleanInputReader().read("Are the information correct? [Y/N]");
		}

		int count = 0;

		broker_addr = "tcp://" + broker_addr;
		pathFile = pathFile + "/";
		List<ISensor> ultraL = ScenarioGenerator.generateUltrasonicSensors(count, total_ultrasom, max_distance_cm,
				min_distance_cm, ultra_update_rate, centralPoint, range, broker_addr, default_qos);
		count = count + total_ultrasom + 1;

		List<ISensor> tempL = ScenarioGenerator.generateTemperatureSensors(count, total_temperature, max_temp_celsius,
				min_temp_celsius, temp_update_rate, centralPoint, range, broker_addr, default_qos);
		count = count + total_temperature + 1;

		List<ISensor> smokeL = ScenarioGenerator.generateSmokeDetectorSensors(count, total_smoke, smoke_update_rate,
				centralPoint, range, broker_addr, default_qos);
		count = count + total_smoke + 1;

		List<ISensor> humL = ScenarioGenerator.generateHumidityDetectorSensors(count, total_humidity_rain,
				hum_update_rate, centralPoint, range, broker_addr, default_qos);
		count = count + total_humidity_rain + 1;

		ScenarioGenerator.persistJSONFile(terminal, pathFile, fileName, ultraL, tempL, smokeL, humL);

	}

}
