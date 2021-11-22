package br.com.gmltec.run;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.beryx.textio.TextIO;
import org.beryx.textio.TextTerminal;

import br.com.gmltec.geo.Coordinate;
import br.com.gmltec.scenario.RandomGen;
import br.com.gmltec.sensor.ISensor;
import br.com.gmltec.sensor.Sensor;
import br.com.gmltec.sensor.ISensor.SENSOR_TYPE;
import br.com.gmltec.utils.ScenarioUtils;

public class ScenarioCreatorGui {

	public static void scenarioCreation(TextIO textIO) throws InterruptedException {

		TextTerminal<?> terminal = textIO.getTextTerminal();
		terminal.getProperties().setPromptColor(Color.green);

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
		boolean azureSupport = false;

		while (correct == false) {
			azureSupport = textIO.newBooleanInputReader().withDefaultValue(false).read("Do you want azure support?");
			broker_addr = textIO.newStringInputReader().withDefaultValue("127.0.0.1:1883").read("Broker Address:");
			default_qos = textIO.newIntInputReader().withDefaultValue(0).read("Default qos value {0,1,2}:");
			pathFile = textIO.newStringInputReader().withDefaultValue("resources"+File.pathSeparator)
					.read("What is the directory where you want to save your file (full path)?");
			fileName = textIO.newStringInputReader().withDefaultValue("exe.json")
					.read("What is the name of the scenario file to be generated?");

			terminal.println();
			terminal.getProperties().setPromptColor(Color.yellow);
			terminal.println("You selected these options:");
			terminal.println("azure support:" + azureSupport);
			terminal.println("broker address:" + broker_addr);
			terminal.println("default qos value:" + default_qos);
			terminal.println("path[/]:" + pathFile);
			terminal.println("scenario file name:" + fileName);
			correct = textIO.newBooleanInputReader().read("Are the information correct? [Y/N]");
		}

		int count = 0;

		terminal.println();
		terminal.getProperties().setPromptColor(Color.green);
		int total_ultrasom =0;
		int total_temp = 0;
		

		total_ultrasom = textIO.newIntInputReader().read("How many Ultrasonic sensors do you want to create?");
		List<ISensor> ultraL = new ArrayList<>();
		if (total_ultrasom > 0) {
			ultraL = createUltraSonicSensor(textIO, count, azureSupport, total_ultrasom, default_qos, broker_addr);
		} else {
			terminal.println();
			terminal.getProperties().setPromptColor(Color.yellow);
			terminal.println("You set the number of ultrasonic sensors = 0");
			terminal.getProperties().setPromptColor(Color.green);
		}
		count = count + ultraL.size();
		
		terminal.getProperties().setPromptColor(Color.green);
		total_temp = textIO.newIntInputReader().read("How many Temperature sensors do you want to create?");
		List<ISensor> tempL = new ArrayList<>();
		if (total_temp > 0) {
			tempL = createTemperatureSensor(textIO, count, azureSupport, total_temp, default_qos, broker_addr);
		} else {
			terminal.println();
			terminal.getProperties().setPromptColor(Color.yellow);
			terminal.println("You set the number of temperature sensors = 0");
			terminal.getProperties().setPromptColor(Color.green);
		}
		count = count + tempL.size();

		terminal.getProperties().setPromptColor(Color.green);
		int total_smoke = textIO.newIntInputReader().read("How many Smoke sensors do you want to create?");
		List<ISensor> smokeL = new ArrayList<>();
		if (total_smoke > 0) {
			smokeL = createOtherSensor(textIO, count, SENSOR_TYPE.SMOKE, azureSupport, total_temp, default_qos,
					broker_addr);
		} else {
			terminal.println();
			terminal.getProperties().setPromptColor(Color.yellow);
			terminal.println("You set the number of smoke sensors = 0");
			terminal.getProperties().setPromptColor(Color.green);
		}
		count = count + smokeL.size();

		terminal.getProperties().setPromptColor(Color.green);
		int total_hum = textIO.newIntInputReader().read("How many Humidity/Rain sensors do you want to create?");
		List<ISensor> humL = new ArrayList<>();
		if (total_hum > 0) {
			humL = createOtherSensor(textIO, count, SENSOR_TYPE.HUMIDIT, azureSupport, total_temp, default_qos,
					broker_addr);
		} else {
			terminal.println();
			terminal.getProperties().setPromptColor(Color.yellow);
			terminal.println("You set the number of humidity/rain sensors = 0");
			terminal.getProperties().setPromptColor(Color.green);
		}
		count = count + humL.size();
		
		List<ISensor>sensorsL = new ArrayList<>();
		for (ISensor sensor:ultraL)
			sensorsL.add(sensor);
		for (ISensor sensor:tempL)
			sensorsL.add(sensor);
		for (ISensor sensor:smokeL)
			sensorsL.add(sensor);
		for (ISensor sensor:humL)
			sensorsL.add(sensor);
		
		ScenarioUtils.persistJSONFile(terminal, pathFile, fileName, sensorsL);

	}

	public static List<ISensor> createUltraSonicSensor(TextIO textIO, int sensorIdStarter, boolean azureSupport,
			int total_sensor_number, int qos, String broker) {

		List<ISensor> sensorL = new ArrayList<>();

		TextTerminal<?> terminal = textIO.getTextTerminal();
		
		for (int i = sensorIdStarter; i < total_sensor_number; i++) {
			boolean correct = false;
			double max_distance_cm = 0;
			double min_distance_cm = 0;
			int ultra_update_rate = 0;

			String deviceSecret = null;
			String sensor_id = null;
			String productKey = null;
			double latitude = 0;
			double longitude = 0;

			while (correct == false) {
				terminal.getProperties().setPromptColor(Color.green);
				max_distance_cm = 0;
				min_distance_cm = 0;
				ultra_update_rate = 0;
				deviceSecret = null;
				sensor_id = Integer.toString(i + 1);
				productKey = RandomGen.generateRandomString(43);
				
				max_distance_cm = textIO.newDoubleInputReader().withDefaultValue(40.0)
						.read("Define the max distance (cm) value that could be measured by the sensor:");
				min_distance_cm = textIO.newDoubleInputReader().withDefaultValue(0.0)
						.read("Define the min distance (cm) value that could be measured by the sensor:");
				ultra_update_rate = textIO.newIntInputReader().withDefaultValue(3000)
						.read("What is the update rate (miliseconds) of the sensor?");

				if (azureSupport) {
					deviceSecret = textIO.newStringInputReader()
							.read("What shared-access-key (this value you find in the Azure IoT Hub)?");
				} else {
					deviceSecret = RandomGen.generateRandomString(43);
				}

				latitude = textIO.newDoubleInputReader().withDefaultValue(latitude)
						.read("Define the latitude value (double):");
				longitude = textIO.newDoubleInputReader().withDefaultValue(longitude)
						.read("Define the longitude value (double):");

				terminal.println();
				terminal.getProperties().setPromptColor(Color.yellow);
				terminal.println("You selected these options:");
				terminal.println("max measure distance value:" + max_distance_cm);
				terminal.println("min measure distance value:" + min_distance_cm);
				terminal.println("update rate value:" + ultra_update_rate);
				terminal.println("latitude:" + latitude);
				terminal.println("longitude:" + longitude);
				terminal.println("sensor-id:" + sensor_id);
				terminal.println("product-key:" + productKey);
				terminal.println("device-secret:" + deviceSecret);
				correct = textIO.newBooleanInputReader().read("Are the information correct? [Y/N]");
			}

			ISensor sensor = new Sensor(sensor_id, SENSOR_TYPE.ULTRASONIC, azureSupport, broker, qos, productKey,
					deviceSecret, max_distance_cm, min_distance_cm, ultra_update_rate,
					new Coordinate(latitude, longitude, 0));
			sensorL.add(sensor);
		}

		return sensorL;
	}

	public static List<ISensor> createTemperatureSensor(TextIO textIO, int sensorIdStarter, boolean azureSupport,
			int total_sensor_number, int qos, String broker) {
		List<ISensor> sensorL = new ArrayList<>();
		
		total_sensor_number = total_sensor_number +sensorIdStarter;

		TextTerminal<?> terminal = textIO.getTextTerminal();
		
		for (int i = sensorIdStarter; i < total_sensor_number; i++) {
			boolean correct = false;
			double max_distance_cm = 0;
			double min_distance_cm = 0;
			int ultra_update_rate = 0;

			String deviceSecret = null;
			String sensor_id = null;
			String productKey = null;
			double latitude = 0;
			double longitude = 0;

			while (correct == false) {
				terminal.getProperties().setPromptColor(Color.green);
				max_distance_cm = 0;
				min_distance_cm = 0;
				ultra_update_rate = 0;
				deviceSecret = null;
				sensor_id = Integer.toString(i + 1);
				productKey = RandomGen.generateRandomString(43);
				;

				max_distance_cm = textIO.newDoubleInputReader().withDefaultValue(70.0)
						.read("Define the max temperature value (celsius degree) could be measured by the sensor:");
				min_distance_cm = textIO.newDoubleInputReader().withDefaultValue(0.0).read(
						"Define the min temperature value (celsius degree) that could be measured by the sensor:");
				ultra_update_rate = textIO.newIntInputReader().withDefaultValue(3000)
						.read("What is the update rate (miliseconds) of the sensor?");

				if (azureSupport) {
					deviceSecret = textIO.newStringInputReader()
							.read("What shared-access-key (this value you find in the Azure IoT Hub)?");
				} else {
					deviceSecret = RandomGen.generateRandomString(43);
				}

				latitude = textIO.newDoubleInputReader().withDefaultValue(latitude)
						.read("Define the latitude value (double):");
				longitude = textIO.newDoubleInputReader().withDefaultValue(longitude)
						.read("Define the longitude value (double):");

				terminal.println();
				terminal.getProperties().setPromptColor(Color.yellow);
				terminal.println("You selected these options:");
				terminal.println("max measure distance value:" + max_distance_cm);
				terminal.println("min measure distance value:" + min_distance_cm);
				terminal.println("update rate value:" + ultra_update_rate);
				terminal.println("latitude:" + latitude);
				terminal.println("longitude:" + longitude);
				terminal.println("sensor-id:" + sensor_id);
				terminal.println("product-key:" + productKey);
				terminal.println("device-secret:" + deviceSecret);
				correct = textIO.newBooleanInputReader().read("Are the information correct? [Y/N]");
			}

			ISensor sensor = new Sensor(sensor_id, SENSOR_TYPE.TEMPERATURE, azureSupport, broker, qos, productKey,
					deviceSecret, max_distance_cm, min_distance_cm, ultra_update_rate,
					new Coordinate(latitude, longitude, 0));
			sensorL.add(sensor);
		}

		return sensorL;
	}

	public static List<ISensor> createOtherSensor(TextIO textIO, int sensorIdStarter, SENSOR_TYPE type,
			boolean azureSupport, int total_sensor_number, int qos, String broker) {
		List<ISensor> sensorL = new ArrayList<>();

		total_sensor_number = total_sensor_number +sensorIdStarter;
		
		TextTerminal<?> terminal = textIO.getTextTerminal();
		
		for (int i = sensorIdStarter; i < total_sensor_number; i++) {
			boolean correct = false;
			double max_distance = 0;
			double min_distance = 0;
			int ultra_update_rate = 0;

			String deviceSecret = null;
			String sensor_id = null;
			String productKey = null;
			double latitude = 0;
			double longitude = 0;

			while (correct == false) {
				terminal.getProperties().setPromptColor(Color.green);
				max_distance = 1;
				min_distance = 0;
				ultra_update_rate = 0;
				deviceSecret = null;
				sensor_id = Integer.toString(i + 1);
				productKey = RandomGen.generateRandomString(43);
				;

				ultra_update_rate = textIO.newIntInputReader().withDefaultValue(3000)
						.read("What is the update rate (miliseconds) of the sensor?");

				if (azureSupport) {
					deviceSecret = textIO.newStringInputReader()
							.read("What shared-access-key (this value you find in the Azure IoT Hub)?");
				} else {
					deviceSecret = RandomGen.generateRandomString(43);
				}

				latitude = textIO.newDoubleInputReader().withDefaultValue(latitude)
						.read("Define the latitude value (double):");
				longitude = textIO.newDoubleInputReader().withDefaultValue(longitude)
						.read("Define the longitude value (double):");

				terminal.println();
				terminal.getProperties().setPromptColor(Color.yellow);
				terminal.println("You selected these options:");
				terminal.println("max measure distance value:" + max_distance);
				terminal.println("min measure distance value:" + min_distance);
				terminal.println("update rate value:" + ultra_update_rate);
				terminal.println("latitude:" + latitude);
				terminal.println("longitude:" + longitude);
				terminal.println("sensor-id:" + sensor_id);
				terminal.println("product-key:" + productKey);
				terminal.println("device-secret:" + deviceSecret);
				correct = textIO.newBooleanInputReader().read("Are the information correct? [Y/N]");
			}

			ISensor sensor = new Sensor(sensor_id, type, azureSupport, broker, qos, productKey, deviceSecret,
					max_distance, min_distance, ultra_update_rate, new Coordinate(latitude, longitude, 0));
			sensorL.add(sensor);
		}

		return sensorL;
	}

	
}
