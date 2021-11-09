package br.com.gmltec.mqqt;

import java.util.Arrays;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;

public class ClientMQTT implements MqttCallbackExtended {

	private final String serverURI;
	private MqttClient client;
	private final MqttConnectOptions mqttOptions;

	public ClientMQTT(String serverURI, String usuario, String senha) {
		this.serverURI = serverURI;

		mqttOptions = new MqttConnectOptions();
		mqttOptions.setMaxInflight(200);
		mqttOptions.setConnectionTimeout(3);
		mqttOptions.setKeepAliveInterval(10);
		mqttOptions.setAutomaticReconnect(true);
		mqttOptions.setCleanSession(false);

		if (usuario != null && senha != null) {
			mqttOptions.setUserName(usuario);
			mqttOptions.setPassword(senha.toCharArray());
		}
	}

	public IMqttToken subscribe(int qos, IMqttMessageListener gestorMensagemMQTT, String... topicos) {
		if (client == null || topicos.length == 0) {
			return null;
		}
		int tamanho = topicos.length;
		int[] qoss = new int[tamanho];
		IMqttMessageListener[] listners = new IMqttMessageListener[tamanho];

		for (int i = 0; i < tamanho; i++) {
			qoss[i] = qos;
			listners[i] = gestorMensagemMQTT;
		}
		try {
			return client.subscribeWithResponse(topicos, qoss, listners);
		} catch (MqttException ex) {
			System.out.println(String.format("Error subscribing to topics %s - %s", Arrays.asList(topicos), ex));
			return null;
		}
	}

	public void unsubscribe(String... topicos) {
		if (client == null || !client.isConnected() || topicos.length == 0) {
			return;
		}
		try {
			client.unsubscribe(topicos);
		} catch (MqttException ex) {
			System.out.println(String.format("Error when unsubscribe from topic %s - %s", Arrays.asList(topicos), ex));
		}
	}

	public void start(String clientID) {
		try {
			System.out.println("Connecting the broker MQTT at " + serverURI);
			client = new MqttClient(serverURI, clientID,
					new MqttDefaultFilePersistence(System.getProperty("java.io.tmpdir")));
			client.setCallback(this);
			client.connect(mqttOptions);
		} catch (MqttException ex) {
			System.out.println("Error connecting to broker mqtt " + serverURI + " - " + ex);
		}
	}

	public void end() {
		if (client == null || !client.isConnected()) {
			return;
		}
		try {
			client.disconnect();
			client.close();
		} catch (MqttException ex) {
			System.out.println("Error disconnecting from broker mqtt - " + ex);
		}
	}

	public void publish(String topic, byte[] payload, int qos) {
		publish(topic, payload, qos, false);
	}

	public synchronized void publish(String topic, byte[] payload, int qos, boolean retained) {
		try {
			if (client.isConnected()) {
				client.publish(topic, payload, qos, retained);
				System.out.println(String.format("Topic %s posted. %dB", topic, payload.length));
			} else {
				System.out.println("Client disconnected, could not post topic " + topic);
			}
		} catch (MqttException ex) {
			System.out.println("Error publishing " + topic + " - " + ex);
		}
	}

	@Override
	public void connectionLost(Throwable thrwbl) {
		System.out.println("Connection to broker lost -" + thrwbl);
	}

	@Override
	public void connectComplete(boolean reconnect, String serverURI) {
		System.out.println("MQTT Client " + (reconnect ? "reconnect " : "connect") + " with the broker " + serverURI);
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken imdt) {
	}

	@Override
	public void messageArrived(String topic, MqttMessage mm) throws Exception {
	}

}