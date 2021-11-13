package br.com.gmltec.utils;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import br.com.gmltec.geo.Coordinate;
import br.com.gmltec.sensor.ISensor;
import br.com.gmltec.sensor.Sensor;

public class ConfigUtils {
	
	
	
	public static List<Sensor> parseSensorFiles(String filePath) {
		String sensorFile = filePath;
		List<Sensor> sensorL = new ArrayList<>();
		
		
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
				Coordinate coord = new Coordinate(latitude, longitude, 0);
				
				int update_rate_ms= ((Long)jo.get("update_rate_ms")).intValue();
				double mean=(double) jo.get("mean");
				double stdv=(double) jo.get("stdv");
				String mqqtTopic = (String) jo.get("mqqtTopic");
				int qos=(int) ((Long)jo.get("qos")).intValue();
				String sensorType= (String) jo.get("sensor_type");
				String mqqtBrokerAddr = (String) jo.get("brokerAddr");
				
				ISensor.SENSOR_TYPE type = null;
				if (sensorType.equals("ULTRASONIC")) 
					type = ISensor.SENSOR_TYPE.ULTRASONIC;
				else if (sensorType.equals("TEMPERATURE"))
					type = ISensor.SENSOR_TYPE.TEMPERATURE;
				else if (sensorType.equals("SMOKE"))
					type = ISensor.SENSOR_TYPE.SMOKE;
				
				Sensor sensor = new Sensor (sensorID,type, mqqtBrokerAddr,mqqtTopic, qos,update_rate_ms, 
						coord,mean, stdv, deviceSecret, productKey);
				
				sensorL.add(sensor);
			}
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		
		return sensorL;
	}
}
