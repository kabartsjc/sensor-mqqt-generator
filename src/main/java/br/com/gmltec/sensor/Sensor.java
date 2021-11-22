package br.com.gmltec.sensor;

import br.com.gmltec.geo.Coordinate;
import br.com.gmltec.mqqt.MqttSign;
import br.com.gmltec.scenario.RandomGen;

public class Sensor extends ISensor{
	
	public Sensor (String sensorID,SENSOR_TYPE type, boolean azureSupport, String brokerAddr, int qos, 
			String productKey, String deviceSecret,
			double max_distance, double min_distance, int update_rate_ms, Coordinate coord) {
		this.sensorID = sensorID;
		this.type=type;
		this.azureSupport=azureSupport;
		
		if (type == SENSOR_TYPE.ULTRASONIC)
			this.mqqtTopic="ultrasom";
		else if (type == SENSOR_TYPE.TEMPERATURE)
			this.mqqtTopic="temperature";
		else if (type == SENSOR_TYPE.HUMIDIT)
			this.mqqtTopic="humidity";
		else if (type == SENSOR_TYPE.SMOKE)
			this.mqqtTopic="smoke";
		
		this.update_rate_ms=update_rate_ms;
		
		this.qos=qos;
		this.mqqtBrokerAddr=brokerAddr;
		
		mean = (max_distance+ min_distance)/2;
		stdv = RandomGen.generateDouble(max_distance,min_distance);
		
		sign = new MqttSign();
		this.productKey=productKey;
		this.deviceSecret=deviceSecret;
		
        sign.calculate(productKey, sensorID, deviceSecret);
		this.coord=coord;
		random = new RandomGen(mean, stdv);
	}
	
	
	public Sensor (String sensorID,SENSOR_TYPE  type,boolean azure_support, String mqqtBrokerAddr,String mqqtTopic, int qos,int update_rate_ms, 
			Coordinate coord,double mean, double stvd, String deviceSecret, String productKey) {
		
		this.sensorID = sensorID;
		this.type=type;
		this.mqqtTopic=sensorID+"/"+mqqtTopic;
		
		this.azureSupport=azure_support;
		
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
