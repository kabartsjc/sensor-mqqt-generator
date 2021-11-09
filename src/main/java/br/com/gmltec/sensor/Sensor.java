package br.com.gmltec.sensor;

import java.text.SimpleDateFormat;

import br.com.gmltec.gen.RandomGen;
import br.com.gmltec.mqqt.ClientMQTT;

public class Sensor {
	private String sensorID;
	private double latitude;
	private double longitude;
	private ClientMQTT clientMQTT;
	
	private String mqqtTopic;
	
	private RandomGen random;
	
	public Sensor(String sensorID, 
			String mqqtTopic, String mqqtBrokerAddr, 
			String username, String password,
			double latitude, double longitude, double mean, double stdv) {
		super();
		this.sensorID = sensorID;
		this.mqqtTopic=mqqtTopic;
		this.latitude = latitude;
		this.longitude = longitude;
		random = new RandomGen(mean, stdv);
		clientMQTT = new ClientMQTT(mqqtBrokerAddr, username, password);
		clientMQTT.start(sensorID);
	}

	public String getSensorID() {
		return sensorID;
	}

	public String getMqqtTopic() {
		return mqqtTopic;
	}
	
	private String update() {
		String msg = null;
		String time = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(System.currentTimeMillis());
		String measure = Double.toString(random.nextSample());
		msg="nodeID="+sensorID+";time="+time+";measure="+measure+";coordinate="+Double.toString(latitude)+":"+Double.toString(longitude);
		return msg;
	}
	
	
	public void publish() {
		String msg = update();
		clientMQTT.publish(mqqtTopic, msg.getBytes(), 0);
	}
}
