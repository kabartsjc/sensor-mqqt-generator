package br.com.gmltec.run;

import java.util.List;

import br.com.gmltec.sensor.Sensor;
import br.com.gmltec.utils.ConfigUtils;

public class Main {
	
	public static void main (String args[]) throws InterruptedException {
		ConfigUtils config = new ConfigUtils();
		List<Sensor>sensorL = config.getSensorL();
		while (true) {
			for (Sensor sensor:sensorL) {
				Thread.sleep(1000);
				sensor.publish();
			}
		}
		
	}

}
