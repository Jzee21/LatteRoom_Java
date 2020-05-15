package network.server.service;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import network.server.dao.AlertScheduler;
import network.server.dao.Device;
import network.server.vo.Alert;
import network.server.vo.Message;
import network.server.vo.Sensor;
import network.server.vo.SensorData;


public class ServerService {
	
	private static final String DEVICE_ID = "SERVER";
	private static final String DEVICE_TYPE = "SERVER";

	private Map<String, Device> deviceList = new ConcurrentHashMap<String, Device>();
	private Map<String, Device> userList = new ConcurrentHashMap<String, Device>();
	private Map<String, Sensor> sensorList = new ConcurrentHashMap<String, Sensor>();
	private List<SensorData> dataList = new ArrayList<SensorData>();
	private List<SensorData> controlList = new ArrayList<SensorData>();
	private List<String> typeHEAT = new ArrayList<String>();		// List<DeviceID>
	private List<String> typeCOOL = new ArrayList<String>();
	private List<String> typeTEMP = new ArrayList<String>();
	private List<String> typeBED = new ArrayList<String>();
	private List<String> typeLIGHT = new ArrayList<String>();
	
	private Sensor hopeTemp	= new Sensor("HopeTemp");
	private Sensor hopeLight = new Sensor("HopeLight");
	private Sensor hopeBed	= new Sensor("HopeBed");
	private Alert  hopeAlert = new Alert(0, 0, "", false);
	
	private AlertScheduler alertScheduler = new AlertScheduler();
	
	private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").create();
	
	// Singleton
	private ServerService() {
		this.hopeTemp.setRecentData("22");
		this.hopeLight.setRecentData("OFF", "50");
		this.hopeBed.setRecentData("0");
	}
	
	private static class InstanceHandler {
		public static final ServerService INSTANCE = new ServerService();
	}
	
	public static ServerService getInstance() {
		return InstanceHandler.INSTANCE;
	}
	
	//
	public static String getDeviceId() {
		return DEVICE_ID;
	}
	
	public static String getDeviceType() {
		return DEVICE_TYPE;
	}
	
	
	// method
	public Device add(String deviceID, String deviceType, Socket socket) {
//		this.list.put(c.getDeviceID(), c);
		Device device = null;
		if(deviceType.equals("USER")) {
			if((device = userList.get(deviceID)) == null) {
				/* First connection check : true
				 * Create new Device.class			*/
				device = new Device(deviceID, deviceType, socket);
				userList.put(deviceID, device);
			} else {
				/* First connection check : false
				 * Update the socket				*/
				device.setSocket(socket);
			}
		} else {
			if((device = deviceList.get(deviceID)) == null) {
				/* First connection check : true
				 * Create new Device.class			*/
				device = new Device(deviceID, deviceType, socket);
				deviceList.put(deviceID, device);
			} else {
				/* First connection check : false
				 * Update the socket				*/
				device.setSocket(socket);
			}
		}
		
		return device;
	}

	public void remove(Device c) {
		this.deviceList.remove(c.getDeviceID());
	}
	
	public Device get(String id) {
		return this.deviceList.get(id);
	}
	
	public Map<String, Device> getList() {
		return this.deviceList;
	}
	
	public void setDeviceSensorList(Device device, Message data) {
		HashMap<String, Sensor> map = gson.fromJson(data.getJsonData(), HashMap.class);
		
		for(String key : map.keySet()) {
			
			Sensor sensor = sensorList.putIfAbsent(key, map.get(key));
			String id = sensor.getDeviceID();		// Sensor's DeviceID
			
			switch (sensor.getSensorType()) {
			case "HEAT":
				if(!typeHEAT.contains(id))
					typeHEAT.add(id);
				break;
			case "COOL":	
				if(!typeCOOL.contains(id))
					typeCOOL.add(id);
				break;
			case "TEMP":
				if(!typeTEMP.contains(id))
					typeTEMP.add(id);
				break;
			case "BED":
				if(!typeBED.contains(id))
					typeBED.add(id);
				break;
			case "LIGHT":
				if(!typeLIGHT.contains(id))
					typeLIGHT.add(id);
				break;

			default:
				break;
			} // switch
		} // for
	}
	
	public void deviceControl(SensorData data) {
		
		Sensor sensor = this.sensorList.get(data.getSensorID());
		Device target = this.deviceList.get(sensor.getDeviceID());
		target.send(new Message(data));
		
	}
	
	public void checkSensorData(SensorData data) {
		if(data.getSensorID().toUpperCase().equals("TEMP")) {
			int hope = Integer.parseInt(this.hopeTemp.getStates());
			int curr = Integer.parseInt(data.getStates());
			
			if(hope > curr) {
				 // do nothing
			}
			
		} else if (data.getSensorID().toUpperCase().equals("HEAT")) {
			
			
		} else if (data.getSensorID().toUpperCase().equals("COOL")) {
			
			
		} else if (data.getSensorID().toUpperCase().equals("BED")) {
			
			
		} else if (data.getSensorID().toUpperCase().equals("LIGHT")) {
			
			
		}
		
		Sensor target = sensorList.get(data.getSensorID());
		target.setRecentData(data.getStates(), data.getStateDetail());
	}
	
	public void dataHandler(Device receiver, String jsonData) {
		Message data = gson.fromJson(jsonData, Message.class);
		System.out.println(data.getDeviceID() + " : " + data.getJsonData());
		
		if (data.getDataType().equals("SENSOR_LIST")) {
			this.setDeviceSensorList(receiver, data);
			
		} else if (data.getDataType().equals("Request")) {
			Sensor target = this.sensorList.get(data.getJsonData());
			receiver.send(new Message(target.getRecentData()));
			
		} else if (data.getDataType().equals("SensorData")) {
			SensorData receiveData = data.getSensorData();
			
			if(receiver.getDeviceType().equals("USER")) {
				// "USER" : Request Device Control
				this.controlList.add(receiveData);
				this.deviceControl(receiveData);
			} else {
				// "DEVICE" : Update recently SensorData
				this.dataList.add(receiveData);
				this.checkSensorData(receiveData);
			}
			
		} else if (data.getDataType().equals("Alert")) {
			alertScheduler.set(data.getAlertData());
		}
		
	}
	
}
