package br.com.gmltec.utils;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import br.com.gmltec.sensor.Sensor;

public class ConfigUtils {
	private String mqqtTopic;
	private String mqqtBrokerAddr;
	private String username;
	private String password;
	
	private List<Sensor> sensorL;
	
	public ConfigUtils() {
		parse();
	}
	
	private void parse() {
		System.out.println("parsing basic config.json");
		parseConfig();
		System.out.println("parsing basic sensors.json");
		parseSensorFiles();
	}
	
	private void parseConfig() {
		String exeFile = "resources/config.json";
		try {
			Object obj = new JSONParser().parse(new FileReader(exeFile));
			JSONObject jo = (JSONObject) obj;
			mqqtTopic = (String) jo.get("mqqtTopic");
			mqqtBrokerAddr = (String) jo.get("mqqtBrokerAddr");
			username = (String) jo.get("username");
			password = (String) jo.get("password");
			
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}
	
	
	private void parseSensorFiles() {
		String sensorFile = "resources/sensors.json";
		sensorL = new ArrayList<>();
		
		try {
			Object obj = new JSONParser().parse(new FileReader(sensorFile));
			JSONArray joArr = (JSONArray) obj;
			for (int i = 0; i < joArr.size(); i++) {
				JSONObject jo = (JSONObject) joArr.get(i);
				String sensorID = (String) jo.get("sensorID");
				double latitude = (double) jo.get("latitude");
				double longitude = (double) jo.get("longitude");
				double mean=(double) jo.get("mean");
				double stdv=(double) jo.get("stdv");
				Sensor sensor = new Sensor(sensorID, mqqtTopic, mqqtBrokerAddr, username, password, latitude, longitude, mean, stdv);
				sensorL.add(sensor);
			}
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}

	public List<Sensor> getSensorL() {
		return sensorL;
	}
	

}
