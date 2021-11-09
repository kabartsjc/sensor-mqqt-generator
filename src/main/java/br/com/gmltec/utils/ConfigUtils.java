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
	
	public static List<Sensor> parseConfigFiles() {
		System.out.println("parsing basic config.json");
		String params [] = parseConfig();
		System.out.println("parsing basic sensors.json");
		List<Sensor>sensorL = parseSensorFiles(params);
		return sensorL;
	}
	
	private static String[] parseConfig() {
		String exeFile = "resources/config.json";
		String qos=null;
		String mqqtBrokerAddr=null;
		
		try {
			Object obj = new JSONParser().parse(new FileReader(exeFile));
			JSONObject jo = (JSONObject) obj;
			mqqtBrokerAddr = (String) jo.get("mqqtBrokerAddr");	
			qos =  (String) jo.get("qos");	
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		
		String[] obj = new String[3];
		obj[0]=mqqtBrokerAddr;
		obj[1]=qos;
		
		return obj;
	}
	
	
	private static List<Sensor> parseSensorFiles(String params[]) {
		String sensorFile = "resources/sensors.json";
		List<Sensor> sensorL = new ArrayList<>();
		
		String mqqtBrokerAddr=params[0];
		int qos=Integer.parseInt(params[1]);
		
		try {
			Object obj = new JSONParser().parse(new FileReader(sensorFile));
			JSONArray joArr = (JSONArray) obj;
			for (int i = 0; i < joArr.size(); i++) {
				JSONObject jo = (JSONObject) joArr.get(i);
				String productKey = (String) jo.get("productKey");
				String deviceSecret = (String) jo.get("deviceSecret");
				
				String sensorID = (String) jo.get("sensorID");
				double latitude = (double) jo.get("latitude");
				double longitude = (double) jo.get("longitude");
				int update_rate_ms= Integer.parseInt((String) jo.get("update_rate_ms"));
				double mean=(double) jo.get("mean");
				double stdv=(double) jo.get("stdv");
				String mqqtTopic = (String) jo.get("mqqtTopic");
				
				Sensor sensor = new Sensor(productKey,deviceSecret,sensorID, mqqtTopic, mqqtBrokerAddr,
						qos,update_rate_ms,
						latitude, longitude, mean, stdv);
				sensorL.add(sensor);
			}
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		
		return sensorL;
	}
}
