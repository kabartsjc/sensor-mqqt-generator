package br.com.gmltec.utils;

import java.awt.Color;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.beryx.textio.TextTerminal;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import br.com.gmltec.geo.Coordinate;
import br.com.gmltec.sensor.ISensor;
import br.com.gmltec.sensor.Sensor;

public class ScenarioUtils {
	
	@SuppressWarnings("unchecked")
	public static void persistJSONFile(TextTerminal<?> terminal,String pathFile, String fileName, List<ISensor> sensorL) {
		
		JSONArray sensorAl = new JSONArray();
		
		terminal.getProperties().setPromptColor(Color.yellow);
		terminal.println("generating json file ....");
		
		terminal.println();
		
		for (ISensor sensor: sensorL) {
			terminal.getProperties().setPromptColor(Color.green);
			terminal.println("generating sensor "+sensor.getSensorID());
	        JSONObject sensorObj = new JSONObject();
	        sensorObj.put("sensorID", sensor.getSensorID());
	        sensorObj.put("sensor_type", sensor.getType().toString());
	        sensorObj.put("azure_support", String.valueOf(sensor.getAzureSupport()));
	        sensorObj.put("brokerAddr", sensor.getMqqtBrokerAddr());
	        sensorObj.put("qos", sensor.getQos());
	        sensorObj.put("productKey", sensor.getProductKey());
	        sensorObj.put("deviceSecret", sensor.getDeviceSecret());
	        sensorObj.put("mean", sensor.getMean());
	        sensorObj.put("stdv", sensor.getStdv());
	        sensorObj.put("update_rate_ms", sensor.getUpdate_rate_ms());
	        sensorObj.put("latitude", sensor.getCoord().getLatitude());
	        sensorObj.put("longitude", sensor.getCoord().getLongitude());
	        sensorObj.put("mqqtTopic", sensor.getMqqtTopic());
	        sensorAl.add(sensorObj);
		}
		 //Write JSON file
        try (FileWriter file = new FileWriter(pathFile+fileName)) {
            file.write(sensorAl.toJSONString()); 
            file.flush();
        	terminal.getProperties().setPromptColor(Color.yellow);
    		terminal.println("finish the generation of json file!");
          } catch (IOException e) {
            e.printStackTrace();
        }
        
     	terminal.getProperties().setPromptColor(Color.green);
	}
	
	
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
				else 
					type = ISensor.SENSOR_TYPE.HUMIDIT;
				
				boolean azure_support= (boolean) jo.get("azure_support");
				
				Sensor sensor = new Sensor (sensorID,type, azure_support,mqqtBrokerAddr,mqqtTopic, qos,update_rate_ms, 
						coord,mean, stdv, deviceSecret, productKey);
				
				sensorL.add(sensor);
			}
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		
		return sensorL;
	}
	

}
