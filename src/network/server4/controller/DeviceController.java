package network.server4.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import network.server4.service.DeviceService;
import network.server4.service.ReservService;
import network.server4.vo.Message;
import network.server4.vo.Sensor;
import network.server4.vo.SensorData;

public class DeviceController {

	// =================================================
	// field
	private Dispatcher dispatcher;
	private Gson gson;
//	private Dispatcher dispatcher = Dispatcher.getInstance();
//	private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
	
	// =================================================
	// Singleton
	private DeviceController() {
		dispatcher = Dispatcher.getInstance();
		gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
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
		/** New information from the device */
		// Message {deviceNo, UPDATE, TYPE, SensorData{sensorNo, time, states, stateDetail}}
		
		DeviceService dService = new DeviceService();
		SensorData input = gson.fromJson(data.getJsonData(), SensorData.class);
		
		// Insert new SensorData and get Result
		Sensor result = dService.insertSensorData(input);
		
		// Deliver information to guests
		if(result != null) {
			ReservService rService = new ReservService();
			String userNo = rService.getGuestNoByDevice(result.getDeviceNo());
			
			// Check guest access
			Connection nowGuest = dispatcher.getGuestConn(userNo);
			if(nowGuest != null) {
				Message response = 
						new Message(userNo, 
									"UPDATE", 
									result.getType(), 
									gson.toJson(result));
				
				nowGuest.send(response);
			}
			
		} // if
	} // upToDate()
	
	public void requestControl(Connection conn, Message data) {
		/** Guest requests device control */
		// Message {roomNo, CONTROL, TYPE, SensorData{time, states, stateDetail}}
		
		System.out.println("[DeviceController]");
		
		DeviceService service = new DeviceService();
		SensorData input = gson.fromJson(data.getJsonData(), SensorData.class);
		
		// Insert new ControlData
		SensorData result = service.insertControlData(input);
		
		System.out.println("[DeviceController][result] " + result.toString());
		
		// Deliver information to device
		try {
			if(result != null) {
				DeviceService dService = new DeviceService();
				String deviceNo = dService.getDeviceNoBySensor(result.getSensorNo());
				
				System.out.println("[DeviceController][destination] " + deviceNo);

				// Check device access
//				dispatcher.getDeviceConn(deviceNo);
//				System.out.println("dispatcher :" + dispatcher.toString());
//				Connection targetDevice = dispatcher.getDeviceConn(deviceNo);
//				Connection targetDevice = dispatcher.getGuestList().get(deviceNo);
//				if(targetDevice != null)
//					System.out.println("[DeviceController][destination] " + targetDevice.getConnAddr());
//				else
//					System.out.println("[DeviceController][destination] " + "not found");
//				if(targetDevice != null) {
//					Message response = 
//							new Message(deviceNo, 
//										"CONTROL", 
//										data.getCode2(), 
//										gson.toJson(result));
//					
//					System.out.println("[DeviceController][response] " + response.toString());
//					
//					targetDevice.send(response);
//				}
				Message response = 
						new Message(deviceNo, 
									"CONTROL", 
									data.getCode2(), 
									gson.toJson(result));
				
				System.out.println("[DeviceController][response] " + response.toString());
//				dispatcher.broadcastDevice(response);
//				System.out.println("[DeviceController][response][finish] " + response.toString());
				System.out.println(dispatcher.hashCode());
				dispatcher.sendOneDevice(deviceNo, response);
				System.out.println("[DeviceController][response][finish] " + response.toString());
				
			} // if
		} catch (Exception e) {
			e.printStackTrace();
		}
	} // requestControl
	
	
}
