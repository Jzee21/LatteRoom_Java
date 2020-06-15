package network.server4.controller;

import network.server4.vo.Message;

public class DeviceController {

	// =================================================
	// field
	private Dispatcher dispatcher;
	
	// =================================================
	// Singleton
	private DeviceController() {
		dispatcher = Dispatcher.getInstance();
	}
	
	private static class InstanceHandler {
		public static final DeviceController INSTANCE = new DeviceController();
	}
	
	public static DeviceController getInstance() {
		return InstanceHandler.INSTANCE;
	}
	
	
	// =================================================
	// methods
	public void upToDate(Connection conn, Message data) {
		// Message {deviceNo, UPDATE, TYPE, SensorData{sensorNo, time, states, stateDetail}}
		
		// Insert new SensorData
		// Deliver information to guests
	}
	
	public void requestControl(Connection conn, Message data) {
		// Message {roomNo, CONTROL, TYPE, SensorData{time, states, stateDetail}}
		
		// Insert new ControlData
		// Deliver information to device
	}
	
	
}
