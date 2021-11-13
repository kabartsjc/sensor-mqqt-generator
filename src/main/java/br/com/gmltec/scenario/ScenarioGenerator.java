package br.com.gmltec.scenario;

import java.awt.Color;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.beryx.textio.TextTerminal;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import br.com.gmltec.geo.Coordinate;
import br.com.gmltec.geo.GeoUtils;
import br.com.gmltec.sensor.ISensor;
import br.com.gmltec.sensor.Sensor;

public class ScenarioGenerator {
	
	public static List<ISensor> generateUltrasonicSensors(int init_pos, int total_ultrasom,
			double max_distance_cm,double min_distance_cm, int ultra_update_rate,
			Coordinate centralPoint,double range, 
			String mqqtBrokerAddr, int qos ) {
		
		List<ISensor>sensorL = new ArrayList<>();
		
		for (int i=0; i<total_ultrasom;i++) {
			Coordinate coord=GeoUtils.generateRandomCoordinate(centralPoint, range);
			ISensor sensor = new Sensor ("sensor"+i,Sensor.SENSOR_TYPE.ULTRASONIC, mqqtBrokerAddr,qos,ultra_update_rate, 
					coord,max_distance_cm, min_distance_cm);
			sensorL.add(sensor);
		}
		
		return sensorL;
	}
	
	public static List<ISensor> generateTemperatureSensors(int init_pos,int total_temperature,
			double max_temp_celsius,double min_temp_celsius, int temp_update_rate,
			Coordinate centralPoint,double range, 
			String mqqtBrokerAddr, int qos ) {
		
		List<ISensor>sensorL = new ArrayList<>();
		
		for (int i=0; i<total_temperature;i++) {
			Coordinate coord=GeoUtils.generateRandomCoordinate(centralPoint, range);
			ISensor sensor = new Sensor ("sensor"+i,Sensor.SENSOR_TYPE.TEMPERATURE, mqqtBrokerAddr,qos,temp_update_rate, 
					coord,max_temp_celsius, min_temp_celsius);
			sensorL.add(sensor);
		}
		
		return sensorL;
	}
	
	public static List<ISensor> generateSmokeDetectorSensors(int init_pos,int total_smoke,
			int smoke_update_rate,
			Coordinate centralPoint,double range, 
			String mqqtBrokerAddr, int qos ) {
		List<ISensor>sensorL = new ArrayList<>();
		
		for (int i=0; i<total_smoke;i++) {
			Coordinate coord=GeoUtils.generateRandomCoordinate(centralPoint, range);
			ISensor sensor = new Sensor ("sensor"+i,Sensor.SENSOR_TYPE.SMOKE, mqqtBrokerAddr,
					qos,smoke_update_rate, coord) ;
			sensorL.add(sensor);
		}
		
		return sensorL;
	}
	
	public static List<ISensor> generateHumidityDetectorSensors(int init_pos,int total_hum,
			int hum_update_rate,
			Coordinate centralPoint,double range, 
			String mqqtBrokerAddr, int qos ) {
		List<ISensor>sensorL = new ArrayList<>();
		
		for (int i=0; i<total_hum;i++) {
			Coordinate coord=GeoUtils.generateRandomCoordinate(centralPoint, range);
			ISensor sensor = new Sensor ("sensor"+i,Sensor.SENSOR_TYPE.SMOKE, mqqtBrokerAddr,
					qos,hum_update_rate, coord) ;
			sensorL.add(sensor);
		}
		
		return sensorL;
	}

	@SuppressWarnings("unchecked")
	public static void persistJSONFile(TextTerminal<?> terminal,String pathFile, String fileName, List<ISensor> ultraL, 
			List<ISensor> tempL, List<ISensor> smokeL,
			List<ISensor> humL) {
		
		JSONArray sensorAl = new JSONArray();
		
		terminal.getProperties().setPromptColor(Color.yellow);
		terminal.println("generating json file ....");
		
		terminal.println();
		
		
		for (ISensor sensor: ultraL) {
			terminal.getProperties().setPromptColor(Color.green);
			terminal.println("generating sensor "+sensor.getSensorID());
	        JSONObject sensorObj = new JSONObject();
	        sensorObj.put("sensorID", sensor.getSensorID());
	        sensorObj.put("qos", sensor.getQos());
	        sensorObj.put("brokerAddr", sensor.getMqqtBrokerAddr());
	        sensorObj.put("latitude", sensor.getCoord().getLatitude());
	        sensorObj.put("longitude", sensor.getCoord().getLongitude());
	        sensorObj.put("mqqtTopic", sensor.getMqqtTopic());
	        sensorObj.put("update_rate_ms", sensor.getUpdate_rate_ms());
	        sensorObj.put("sensor_type", sensor.getType().toString());
	        sensorObj.put("mean", sensor.getMean());
	        sensorObj.put("stdv", sensor.getStdv());
	        sensorObj.put("productKey", sensor.getProductKey());
	        sensorObj.put("deviceSecret", sensor.getDeviceSecret());
	        sensorAl.add(sensorObj);
		}
		
		for (ISensor sensor: tempL) {
	        JSONObject sensorObj = new JSONObject();
	    	terminal.println("generating sensor "+sensor.getSensorID());
		    sensorObj.put("sensorID", sensor.getSensorID());
	        sensorObj.put("qos", sensor.getQos());
	        sensorObj.put("brokerAddr", sensor.getMqqtBrokerAddr());
	        sensorObj.put("latitude", sensor.getCoord().getLatitude());
	        sensorObj.put("longitude", sensor.getCoord().getLongitude());
	        sensorObj.put("mqqtTopic", sensor.getMqqtTopic());
	        sensorObj.put("update_rate_ms", sensor.getUpdate_rate_ms());
	        sensorObj.put("sensor_type", sensor.getType().toString());
	        sensorObj.put("mean", sensor.getMean());
	        sensorObj.put("stdv", sensor.getStdv());
	        sensorObj.put("productKey", sensor.getProductKey());
	        sensorObj.put("deviceSecret", sensor.getDeviceSecret());
	        sensorAl.add(sensorObj);
		}
		
		
		for (ISensor sensor: smokeL) {
	        JSONObject sensorObj = new JSONObject();
	        terminal.println("generating sensor "+sensor.getSensorID());
		    sensorObj.put("sensorID", sensor.getSensorID());
	        sensorObj.put("qos", sensor.getQos());
	        sensorObj.put("brokerAddr", sensor.getMqqtBrokerAddr());
	        sensorObj.put("latitude", sensor.getCoord().getLatitude());
	        sensorObj.put("longitude", sensor.getCoord().getLongitude());
	        sensorObj.put("mqqtTopic", sensor.getMqqtTopic());
	        sensorObj.put("update_rate_ms", sensor.getUpdate_rate_ms());
	        sensorObj.put("sensor_type", sensor.getType().toString());
	        sensorObj.put("mean", sensor.getMean());
	        sensorObj.put("stdv", sensor.getStdv());
	         sensorObj.put("productKey", sensor.getProductKey());
	        sensorObj.put("deviceSecret", sensor.getDeviceSecret());
	        sensorAl.add(sensorObj);
		}
		
		for (ISensor sensor: humL) {
	        JSONObject sensorObj = new JSONObject();
	        terminal.println("generating sensor "+sensor.getSensorID());
		    sensorObj.put("sensorID", sensor.getSensorID());
	        sensorObj.put("qos", sensor.getQos());
	        sensorObj.put("brokerAddr", sensor.getMqqtBrokerAddr());
	        sensorObj.put("latitude", sensor.getCoord().getLatitude());
	        sensorObj.put("longitude", sensor.getCoord().getLongitude());
	        sensorObj.put("mqqtTopic", sensor.getMqqtTopic());
	        sensorObj.put("update_rate_ms", sensor.getUpdate_rate_ms());
	        sensorObj.put("sensor_type", sensor.getType().toString());
	        sensorObj.put("mean", sensor.getMean());
	        sensorObj.put("stdv", sensor.getStdv());
	        sensorObj.put("productKey", sensor.getProductKey());
	        sensorObj.put("deviceSecret", sensor.getDeviceSecret());
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
	}
	

}
