package br.com.gmltec.sensor;

import br.com.gmltec.geo.Coordinate;
import br.com.gmltec.mqqt.MqttSign;
import br.com.gmltec.scenario.RandomGen;


public class Sensor extends ISensor{
	
	public Sensor (String sensorID,SENSOR_TYPE  type, String mqqtBrokerAddr,int qos,int update_rate_ms, 
			Coordinate coord,double max_value, double min_value) {
		
		this.sensorID = sensorID;
		this.type=type;
		if (type == SENSOR_TYPE.ULTRASONIC)
			this.mqqtTopic="ultrasom";
		else if (type == SENSOR_TYPE.TEMPERATURE)
			this.mqqtTopic="temperature";
		else 
			return;
		
		this.update_rate_ms=update_rate_ms;
		
		this.qos=qos;
		this.mqqtBrokerAddr=mqqtBrokerAddr;
		
		
		mean = (max_value+ min_value)/2;
		stdv = RandomGen.generateDouble(max_value, min_value);
		 
		sign = new MqttSign();
        sign.calculate(productKey, sensorID, deviceSecret);
        
        this.coord=coord;
		
        random = new RandomGen(mean, stdv);
		productKey = RandomGen.generateRandomString(7);
		deviceSecret = RandomGen.generateRandomString(7);
		 
	}
	
	public Sensor (String sensorID,SENSOR_TYPE  type, String mqqtBrokerAddr,
			int qos,int update_rate_ms, Coordinate coord) {
		
		this.sensorID = sensorID;
		this.type=type;
		
		this.coord=coord;
		
		this.qos=qos;
		this.mqqtBrokerAddr=mqqtBrokerAddr;
		
		if (type == SENSOR_TYPE.HUMIDIT)
			this.mqqtTopic="humidit";
		else if (type == SENSOR_TYPE.SMOKE)
			this.mqqtTopic="smoke";
		else 
			return;
		
		this.update_rate_ms=update_rate_ms;
		
		productKey = RandomGen.generateRandomString(7);
		
		deviceSecret = RandomGen.generateRandomString(7);
		
		mean = 0.5;
		stdv = RandomGen.generateDouble(0.5, 0.2);
		 
		sign = new MqttSign();
        sign.calculate(productKey, sensorID, deviceSecret);
		
        random = new RandomGen(mean, stdv);
		
       
	}
	
	
	public Sensor (String sensorID,SENSOR_TYPE  type, String mqqtBrokerAddr,String mqqtTopic, int qos,int update_rate_ms, 
			Coordinate coord,double mean, double stvd, String deviceSecret, String productKey) {
		
		this.sensorID = sensorID;
		this.type=type;
		this.mqqtTopic=sensorID+"/"+mqqtTopic;
		
		this.update_rate_ms=update_rate_ms;
		
		this.qos=qos;
		this.mqqtBrokerAddr=mqqtBrokerAddr;
		
		this.productKey = productKey;
		this.deviceSecret = deviceSecret;
		 
		this.mean = mean;
		this.stdv =stvd;
		 
		sign = new MqttSign();
        sign.calculate(productKey, sensorID, deviceSecret);
        
        this.coord=coord;
		
        random = new RandomGen(mean, stdv);
		
       
	}
	
	
	
	
	
}
