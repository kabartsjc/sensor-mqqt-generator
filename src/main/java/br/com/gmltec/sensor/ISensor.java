package br.com.gmltec.sensor;

import java.text.SimpleDateFormat;

import org.eclipse.paho.client.mqttv3.IMqttToken;

import br.com.gmltec.geo.Coordinate;
import br.com.gmltec.mqqt.MQTTV3Client;
import br.com.gmltec.mqqt.Mqtt3PostPropertyMessageListener;
import br.com.gmltec.mqqt.MqttSign;
import br.com.gmltec.scenario.RandomGen;

public abstract class ISensor implements Runnable {

	public static enum SENSOR_TYPE {
		ULTRASONIC, TEMPERATURE, HUMIDIT, SMOKE;
	}

	protected String sensorID;
	protected Coordinate coord;

	protected String productKey;
	protected String deviceSecret;
	
	protected boolean azureSupport;

	protected MQTTV3Client clientMQTT;
	protected MqttSign sign;

	protected String mqqtTopic;
	protected int update_rate_ms;

	protected RandomGen random;
	protected boolean running = true;

	protected SENSOR_TYPE type;

	protected double mean;
	protected double stdv;
	
	protected String mqqtBrokerAddr;
	protected int qos;

	protected String prepareMessage() {
		String msg = null;
		String time = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(System.currentTimeMillis());
		String measure = Double.toString(random.nextSample());
		msg = "time=" + time + ";measure=" + measure + ";coordinate=" + coord.getLatitude() + ":"
				+ coord.getLongitude();
		return msg;
	}

	protected void publish() {
		String msg = prepareMessage();
		clientMQTT.publish(mqqtTopic, msg.getBytes(), 0);
	}

	protected void close() {
		clientMQTT.unsubscribe(mqqtTopic);
		clientMQTT.end();
	}

	@Override
	public void run() {
		clientMQTT = new MQTTV3Client(mqqtBrokerAddr, sign);

		IMqttToken subscribe = clientMQTT.subscribe(qos, new Mqtt3PostPropertyMessageListener(), mqqtTopic);
		if (subscribe == null) {
			return;
		}

		while (running) {
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
		this.running = false;
	}

	public String getSensorID() {
		return sensorID;
	}

	public Coordinate getCoord() {
		return coord;
	}

	public int getUpdate_rate_ms() {
		return update_rate_ms;
	}

	public String getMqqtTopic() {
		return mqqtTopic;
	}
	
	public boolean getAzureSupport() {
		return azureSupport;
	}

	public SENSOR_TYPE getType() {
		return type;
	}

	public double getMean() {
		return mean;
	}

	public double getStdv() {
		return stdv;
	}

	public String getProductKey() {
		return productKey;
	}

	public String getDeviceSecret() {
		return deviceSecret;
	}

	public String getMqqtBrokerAddr() {
		return mqqtBrokerAddr;
	}

	public int getQos() {
		return qos;
	}

	
}
