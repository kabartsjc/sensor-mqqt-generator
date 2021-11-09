package br.com.gmltec.run;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import br.com.gmltec.sensor.Sensor;
import br.com.gmltec.utils.ConfigUtils;

public class SimuRun {
	
	public static void main (String args[]) throws InterruptedException {
		List<Sensor>sensorL = ConfigUtils.parseConfigFiles();
		int duration_min = Integer.parseInt(args[0]);
		
		if (duration_min!=-1) {
			for (Sensor sensor:sensorL) {
				Thread th = new Thread(sensor);
				th.start();
			}
			
		}
		
		Instant beginTime = Instant.now();
		Duration deltaTime = Duration.ZERO;
		
		
		while (deltaTime.getSeconds( )< (duration_min*60)) {
			deltaTime = Duration.between(beginTime, Instant.now());
		}
		
		for (Sensor sensor:sensorL) {
			sensor.finish();
		}
		
	}

}
