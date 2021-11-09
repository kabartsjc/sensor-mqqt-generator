# sensor-mqqt-generator

The project sensor-mqqt-generator is a Java project which aims to simulate a huge sensor network that communicates with a Broker using MQQT messages. The current implementation does not support security schema like TLS/SSL connections. 

<b>MQTT (MQ Telemetry Transport)</b> is a lightweight messaging protocol designed for high-latency, low-bandwidth, and unreliable networks. It consumes very little power on the device it’s running on, and it works well over unreliable networks.

The MQQT protocol has these main features:
-	Quality of Service Levels: A Quality of Service (QoS) level is simply an agreement between the sender and receiver of a message that guarantees the deliverability of said message.
-	Persistent Sessions
-	Last Will: A "last will" message is automatically sent to the MQTT broker if the client ungracefully disconnects. This allows the broker to notify the other clients easily and efficiently when one disconnects.

# Installation Process:

The installation process is quite simple, as the project uses Maven. A special attention is related to the use of Java 15 during the performance tests. The easiest way to run the project is using the Eclipse IDE.

# The General Architecture

The main class is the Sensor, which simulates a real device in a MQQT network. The configuration of this is made by the sensors.json file, located in the resources folder. This file needs to describe the basic information about the sensor. 
-	"productKey": it is a special key for each device based on the product brand. The current version does not use this field; however, you need to set it.
-	"deviceSecret": it is the secret for the device based on the product brand. The current version does not use this field; however, you need to set it.
-	"sensorID": it is the id of the sensor. This information needs to be a unique value in the sensor network.
-	"latitude": it is the latitude where the sensor is, using a double representation of it.
-	"longitude": it is the longitude where the sensor is, using a double representation of it.
-	"update_rate_ms": this value is the interval of time that sensor will publish its information and send it to the Broker.
-	"mean": sets the average of the magnitude measured by the sensor (average between the minimum and maximum value). The current implementation has only the gaussian distribution. There are plans to in the future has other distributions to define the data samples.
-	"mqqtTopic": defines the topic where the information is published, for example: "sensors/moscatest". The current implementation supports only one topic for device.
-	"stdv": it is the standard deviation of the measures of sensor. The current implementation has only the gaussian distribution. There are plans to in the future has other distributions to define the data samples.


There is another important file that is required to the Sensor class - the config.json. This file has three variables:
-	"mqqtBrokerAddr": It represents the address of broker, for example: "tcp://127.0.0.1:1883",
-	"qos": it is the level of the agreement between the sender of a message and the receiver of a message that defines the guarantee of delivery for a specific message.  The value can be “0”, “1”, or “2”.

# Running an experiment
To run the simulator, you need to call the class SimuRun and pass (as a parameter) the amount of minutes that you want that simulation will run.
