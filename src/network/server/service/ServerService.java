package network.server.service;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import network.server.dao.Device;
import network.server.test.AbstractClient;
import network.server.test.SharedMessage;
import network.server.test.User;
import network.server.vo.Message;
import network.server.vo.Sensor;
import network.server.vo.SensorData;


public class ServerService {
	
	private Map<String, Device> deviceList = new ConcurrentHashMap<String, Device>();
	private Map<String, Sensor> sensorList = new ConcurrentHashMap<String, Sensor>();
	private List<String> typeHEAT = new ArrayList<String>();		// List<DeviceID>
	private List<String> typeCOOL = new ArrayList<String>();
	private List<String> typeTEMP = new ArrayList<String>();
	private List<String> typeBED = new ArrayList<String>();
	private List<String> typeLIGHT = new ArrayList<String>();
	
	private Gson gson=  new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").create();
	
	private ServerService() {}
	
	private static class InstanceHandler {
		public static final ServerService INSTANCE = new ServerService();
	}
	
	public static ServerService getInstance() {
		return InstanceHandler.INSTANCE;
	}
	
	
	// method
	public Device add(String deviceID, Socket socket) {
//		this.list.put(c.getDeviceID(), c);
		Device device = null;
		if((device = deviceList.get(deviceID)) == null) {
			/* First connection check : true
			 * Create new Device.class			*/
			device = new Device(deviceID, socket);
			deviceList.put(deviceID, device);
		} else {
			/* First connection check : false
			 * Update the socket				*/
			device.setSocket(socket);
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
		Map<String, Sensor> map = gson.fromJson(data.getJsonData(), HashMap.class);
		
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
	
	public void dataHandler(Device receiver, String jsonData) {
		Message data = gson.fromJson(jsonData, Message.class);
		System.out.println(data.getDeviceID() + " : " + data.getJsonData());
		
		if (data.getDataType().equals("SENSOR_LIST")) {
			setDeviceSensorList(receiver, data);
			
		} else if (data.getDataType().equals("SensorData")) {
//			SensorData sensorData = gson.fromJson(data.getJsonData(), SensorData.class);
			SensorData sensorData = data.getSensorData();
		}
		
	}
	
}
