package arduino.test.dao;

import java.util.LinkedList;

import arduino.test.vo.*;

public class TestSharedObject {
	
//	private String deviceID;
//	private String deviceType;
	
	private int hopeStates = 24;
	private int states = 1000;
	private String heat = "OFF";
	private String cool = "OFf";
	
	private LinkedList<SensorData> list;
	
	
	public TestSharedObject() {
		this.list = new LinkedList<SensorData>();
	}


	public synchronized int getHopeStates() {
		return hopeStates;
	}

	public synchronized void setHopeStates(int hopeStates) {
		this.hopeStates = hopeStates;
		changeStates();
	}

	public synchronized int getStates() {
		return states;
	}

	public synchronized void setStates(int states) {
		if(this.states == 1000) {
			this.states = states;
		} else {
			this.states = states;
			changeStates();
		}
	}
	
	public synchronized SensorData pop() {
		SensorData data = null;
		
		try {
			while(list.isEmpty()) {
				this.wait();
			}
			data = list.removeFirst();
		} catch (Exception e) {
		}
		return data;
	}
	
	public void changeStates() {
		SensorData data = null;
		
		if (hopeStates > states) {
			if(cool.equals("ON")) {
//				toArduino.send("COOLOFF");
//				toServer.send("COOL","OFF");
				data = new SensorData("COOL", "OFF");
				cool = "OFF";
			}
			
			if(heat.equals("OFF")) {
//				toArduino.send("HEATON");
//				toServer.send("HEAT","ON");
				data = new SensorData("HEAT", "ON");
				heat = "ON";
			}
		} else if (hopeStates < states) {
			if(heat.equals("ON")) {
//				toArduino.send("HEATOFF");
//				toServer.send("HEAT", "OFF");
				data = new SensorData("HEAT", "OFF");
				heat = "ON";
			}
			
			if(cool.equals("OFF")) {
//				toArduino.send("COOLON");
//				toServer.send("COOL", "ON");
				data = new SensorData("COOL", "ON");
				cool = "ON";
			}
		} else {
			
			data = new SensorData("BOTH", "OFF");
//			if(heat.equals("ON")) {
////				toArduino.send("HEATOFF");
////				toServer.send("HEAT", "OFF");
//				heat = "OFF";
//			}
//			
//			if(cool.equals("ON")) {
////				toArduino.send("COOLOFF");
////				toServer.send("COOL","OFF");
//				cool = "OFF";
//			}
		}
		
		synchronized (this) {
			list.add(data);
			this.notify();
		}
		
	}
	
	
	
}
