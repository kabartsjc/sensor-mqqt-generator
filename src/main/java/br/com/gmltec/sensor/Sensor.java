package br.com.gmltec.sensor;

import java.text.SimpleDateFormat;

import org.eclipse.paho.client.mqttv3.IMqttToken;

import br.com.gmltec.gen.RandomGen;
import br.com.gmltec.mqqt.MQTTV3Client;
import br.com.gmltec.mqqt.Mqtt3PostPropertyMessageListener;
import br.com.gmltec.mqqt.MqttSign;


public class Sensor implements Runnable {
	private String sensorID;
	private double latitude;
	private double longitude;

	private MQTTV3Client clientMQTT;
	private MqttSign sign;

	private String mqqtTopic;
	
	private int update_rate_ms;
	
	private RandomGen random;
	
	private boolean running=true;
	
	public Sensor(String productKey, String deviceSecret, String sensorID, 
			String mqqtTopic, String mqqtBrokerAddr, int qos,int update_rate_ms,
			double latitude, double longitude, double mean, double stdv) {
		super();
		
		sign = new MqttSign();
        sign.calculate(productKey, sensorID, deviceSecret);
        
		this.sensorID = sensorID;
		this.mqqtTopic=mqqtTopic;
		this.latitude = latitude;
		this.longitude = longitude;
		random = new RandomGen(mean, stdv);
		
		this.update_rate_ms=update_rate_ms;
		
		clientMQTT = new MQTTV3Client(mqqtBrokerAddr, sign);
		
		IMqttToken subscribe = clientMQTT.subscribe(qos,  new Mqtt3PostPropertyMessageListener(), mqqtTopic);
		if (subscribe==null) {
			return;
		}
	}

	private String prepareMessage() {
		String msg = null;
		String time = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(System.currentTimeMillis());
		String measure = Double.toString(random.nextSample());
		msg="nodeID="+sensorID+";time="+time+";measure="+measure+";coordinate="+Double.toString(latitude)+":"+Double.toString(longitude);
		return msg;
	}
	
	
	private void publish() {
		String msg = prepareMessage();
		clientMQTT.publish(mqqtTopic, msg.getBytes(), 0);
	}
	
	
	private void close() {
		clientMQTT.unsubscribe(mqqtTopic);
		clientMQTT.end();
	}
	
	@Override
	public void run() {
		
		while(running) {
			try {
				this.publish();
				Thread.sleep(this.update_rate_ms);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		this.close();
	}
	
	public void finish() {
		this.running=false;
	}
}
