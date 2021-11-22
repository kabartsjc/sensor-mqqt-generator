package br.com.gmltec.run;

//Copyright (c) Microsoft. All rights reserved.

//Licensed under the MIT license. See LICENSE file in the project root for full license information.

import com.microsoft.azure.sdk.iot.device.*;
import com.microsoft.azure.sdk.iot.device.transport.IotHubConnectionStatus;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Handles messages from an IoT Hub. Default protocol is to use MQTT transport.
 */
public class SendReceive {
	private static final int D2C_MESSAGE_TIMEOUT = 2000; // 2 seconds
	private static final List<String> failedMessageListOnClose = new ArrayList<>(); // List of messages that failed on
																					// close
	/**
	 * Receives requests from an IoT Hub. Default protocol is to use use MQTT
	 * transport.
	 *
	 * @param args args[0] = IoT Hub connection string args[1] = number of requests
	 *             to send args[2] = protocol (optional, one of 'mqtt' or 'amqps' or
	 *             'https' or 'amqps_ws') args[3] = path to certificate to enable
	 *             one-way authentication over ssl for amqps (optional, default
	 *             shall be used if unspecified).
	 */

	public static void main(String[] args) throws IOException, URISyntaxException {
		System.out.println("Starting...");
		System.out.println("Beginning setup.");

		String pathToCertificate = null;
		String connString = "HostName=CourseIoTHub.azure-devices.net;DeviceId=my-iot-device;SharedAccessKey=mxnbn2gCvs3jroC+UqAUjG2R6Fg2+eCAjXS06DA6jp0=";
		int numRequests = 50;
		IotHubClientProtocol protocol= IotHubClientProtocol.MQTT;
		

		System.out.println("Successfully read input parameters.");
		System.out.format("Using communication protocol %s.\n", protocol.name());

		DeviceClient client = new DeviceClient(connString, protocol);
		if (pathToCertificate != null) {
			client.setOption("SetCertificatePath", pathToCertificate);
		}

		System.out.println("Successfully created an IoT Hub client.");

		if (protocol == IotHubClientProtocol.MQTT) {
			MessageCallbackMqtt callback = new MessageCallbackMqtt();
			Counter counter = new Counter(0);
			client.setMessageCallback(callback, counter);
		} else {
			MessageCallback callback = new MessageCallback();
			Counter counter = new Counter(0);
			client.setMessageCallback(callback, counter);
		}

		System.out.println("Successfully set message callback.");

		// Set your token expiry time limit here
		long time = 2400;
		client.setOption("SetSASTokenExpiryTime", time);

		client.registerConnectionStatusChangeCallback(new IotHubConnectionStatusChangeCallbackLogger(), new Object());

		client.open();

		System.out.println("Opened connection to IoT Hub.");

		System.out.println("Beginning to receive messages...");

		System.out.println("Sending the following event messages: ");

		System.out.println("Updated token expiry time to " + time);

		String deviceId = "MyJavaDevice";
		double temperature;
		double humidity;

		for (int i = 0; i < numRequests; ++i) {
			temperature = 20 + Math.random() * 10;
			humidity = 30 + Math.random() * 20;

			String msgStr = "{\"deviceId\":\"" + deviceId + "\",\"messageId\":" + i + ",\"temperature\":" + temperature
					+ ",\"humidity\":" + humidity + "}";

			try {
				Message msg = new Message(msgStr);
				msg.setContentTypeFinal("application/json");
				msg.setProperty("temperatureAlert", temperature > 28 ? "true" : "false");
				msg.setMessageId(java.util.UUID.randomUUID().toString());
				msg.setExpiryTime(D2C_MESSAGE_TIMEOUT);
				System.out.println(msgStr);
				EventCallback eventCallback = new EventCallback();
				client.sendEventAsync(msg, eventCallback, msg);
			}

			catch (Exception e) {
				e.printStackTrace(); // Trace the exception
			}

		}

		System.out.println("Wait for " + D2C_MESSAGE_TIMEOUT / 1000 + " second(s) for response from the IoT Hub...");

		// Wait for IoT Hub to respond.
		try {
			Thread.sleep(D2C_MESSAGE_TIMEOUT);
		}

		catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println(
				"In receive mode. Waiting for receiving C2D messages (only for MQTT and AMQP). Press ENTER to close. To recieve in Https, send message and then start the sample.");

		Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8.name());
		scanner.nextLine();

		// close the connection
		System.out.println("Closing");
		client.closeNow();

		if (!failedMessageListOnClose.isEmpty()) {
			System.out.println("List of messages that were cancelled on close:" + failedMessageListOnClose.toString());
		}

		System.out.println("Shutting down...");
	}
	
	
	
	/** Used as a counter in the message callback. */
	protected static class Counter {
		protected int num;

		public Counter(int num) {
			this.num = num;
		}

		public int get() {
			return this.num;
		}

		public void increment() {
			this.num++;
		}

		@Override
		public String toString() {
			return Integer.toString(this.num);
		}
	}

	protected static class MessageCallback implements com.microsoft.azure.sdk.iot.device.MessageCallback {
		public IotHubMessageResult execute(Message msg, Object context) {
			Counter counter = (Counter) context;
			System.out.println("Received message " + counter.toString() + " with content: "
					+ new String(msg.getBytes(), Message.DEFAULT_IOTHUB_MESSAGE_CHARSET));
			for (MessageProperty messageProperty : msg.getProperties()) {
				System.out.println(messageProperty.getName() + " : " + messageProperty.getValue());
			}

			int switchVal = counter.get() % 3;
			IotHubMessageResult res;
			switch (switchVal) {
			case 0:
				res = IotHubMessageResult.COMPLETE;
				break;
			case 1:
				res = IotHubMessageResult.ABANDON;
				break;
			case 2:
				res = IotHubMessageResult.REJECT;
				break;
			default:
				// should never happen.
				throw new IllegalStateException("Invalid message result specified.");
			}

			System.out.println("Responding to message " + counter.toString() + " with " + res.name());

			counter.increment();

			return res;
		}
	}

	// Our MQTT doesn't support abandon/reject, so we will only display the messaged
	// received
	// from IoTHub and return COMPLETE
	protected static class MessageCallbackMqtt implements com.microsoft.azure.sdk.iot.device.MessageCallback {
		public IotHubMessageResult execute(Message msg, Object context) {
			Counter counter = (Counter) context;
			System.out.println("Received message " + counter.toString() + " with content: "
					+ new String(msg.getBytes(), Message.DEFAULT_IOTHUB_MESSAGE_CHARSET));
			for (MessageProperty messageProperty : msg.getProperties()) {
				System.out.println(messageProperty.getName() + " : " + messageProperty.getValue());
			}

			counter.increment();

			return IotHubMessageResult.COMPLETE;
		}
	}

	protected static class EventCallback implements IotHubEventCallback {
		public void execute(IotHubStatusCode status, Object context) {
			Message msg = (Message) context;
			System.out.println("IoT Hub responded to message " + msg.getMessageId() + " with status " + status.name());
			if (status == IotHubStatusCode.MESSAGE_CANCELLED_ONCLOSE) {
				failedMessageListOnClose.add(msg.getMessageId());
			}
		}
	}

	protected static class IotHubConnectionStatusChangeCallbackLogger implements IotHubConnectionStatusChangeCallback {
		@Override
		public void execute(IotHubConnectionStatus status, IotHubConnectionStatusChangeReason statusChangeReason,
				Throwable throwable, Object callbackContext) {
			System.out.println();
			System.out.println("CONNECTION STATUS UPDATE: " + status);
			System.out.println("CONNECTION STATUS REASON: " + statusChangeReason);
			System.out.println("CONNECTION STATUS THROWABLE: " + (throwable == null ? "null" : throwable.getMessage()));
			System.out.println();

			if (throwable != null) {
				throwable.printStackTrace();
			}

			if (status == IotHubConnectionStatus.DISCONNECTED) {
				System.out.println("The connection was lost, and is not being re-established."
						+ " Look at provided exception for how to resolve this issue."
						+ " Cannot send messages until this issue is resolved, and you manually re-open the device client");
			} else if (status == IotHubConnectionStatus.DISCONNECTED_RETRYING) {
				System.out.println("The connection was lost, but is being re-established."
						+ " Can still send messages, but they won't be sent until the connection is re-established");
			} else if (status == IotHubConnectionStatus.CONNECTED) {
				System.out.println("The connection was successfully established. Can send messages.");
			}
		}
	}

	
}