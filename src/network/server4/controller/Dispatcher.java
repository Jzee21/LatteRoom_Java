package network.server4.controller;

import java.net.Socket;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import network.server4.dao.DeviceDAO;
import network.server4.dao.GuestDAO;
import network.server4.main.LatteServer;
import network.server4.vo.Guest;
import network.server4.vo.Message;

public class Dispatcher {
	/*
	 * 	Dispatcher
	 *  - Manage socket connections and distribute messages to controllers.
	*/
	
	// =================================================
	// field
	private Dispatcher dispatcher = Dispatcher.getInstance();
	private GuestController gController;
	private DeviceController dController;
	
	private Map<String, Connection> guestList;
	private Map<String, Connection> deviceList;
	private Gson gson;
//	private GuestController gController = GuestController.getInstance();
//	private DeviceController dController = DeviceController.getInstance();
//	
//	private GuestDAO gdao = new GuestDAO();
//	
//	private Map<String, Connection> guestList = new ConcurrentHashMap<String, Connection>();
//	private Map<String, Connection> deviceList = new ConcurrentHashMap<String, Connection>();
//	private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
	

	// =================================================
	// Singleton
	private Dispatcher() {
		gController = GuestController.getInstance();
		dController = DeviceController.getInstance();
		
		guestList = new ConcurrentHashMap<String, Connection>();
		deviceList = new ConcurrentHashMap<String, Connection>();
		gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
	}
	
	private static class LazyHolder {
		public static final Dispatcher INSTANCE = new Dispatcher();
	}
	
	public static Dispatcher getInstance() {
		return LazyHolder.INSTANCE;
	}
	
	// =================================================
	// public methods
	public Connection accept(Socket socket) {
		Connection conn = null;
		conn = new Connection(socket);
		return conn;
		
	} // accept()
	
	public void read(Connection conn, String jsonData) {
//		broadcast(jsonData);
		
		try {
			Message data = gson.fromJson(jsonData, Message.class);
			if(data.getCode1() != null) data.setCode1(data.getCode1().toUpperCase());
			if(data.getCode2() != null) data.setCode2(data.getCode2().toUpperCase());
			System.out.println("[" + conn.getConnAddr() + "] " + data.toString());
			
			switch (data.getCode1()) {
			/** Device */
			// Device Connect
			case "CONNECT":
				this.deviceAuth(conn, data);
				break;
				
			// Device status changes
			case "UPDATE":
				System.out.println("[Code : UPDATE]");
				dController.upToDate(conn, data);
				break;
			
				
			/** Guest */
			// Guest Login
			case "LOGIN":
//				System.out.println("LOGIN : " + gson.fromJson(data.getJsonData(), Guest.class));
				this.guestAuth(conn, data);
				break;
				
			// Connection renewal
			case "RECONNECT":
				this.guestReconn(conn, data);
				break;
				
			// Guest requests device control
			case "CONTROL":
				System.out.println("[Code : CONTROL]");
				dController.requestControl(conn, data);
				break;
				
			// Guest requests reservation list
			case "RESERVELIST":
				gController.requestReserv(conn, data);
				break;
				
			// Guest requests reserved room info
			case "ROOMDETAIL":
				gController.requestRoomDetial(conn, data);
				break;
				
			// Guest requests personal alarm info
			case "ALARM":
				if(data.getCode2().equals("GET"))
					gController.requestAlarmInfo(conn, data);
				else
					gController.requestAlarmUpdate(conn, data);
				break;
				
			// Guest requests detail alarm infos
			case "ALARMJOB":
				if(data.getCode2().equals("GET"))
					gController.requestAlarmJobInfo(conn, data);
				else
					gController.requestAlarmJobUpdate(conn, data);
				break;
				
			default:
				break;
			}
			
		} catch (Exception e) {
//			e.printStackTrace();
		}
		
	} // read()
	
	
	public void removeOne(Connection conn) {
		LatteServer.getConnections().remove(conn);
		if(conn.getType() != null) {
			switch(conn.getType()) {
			case "GUEST":
				guestList.remove(conn.getClientNo());
				break;
			case "DEVICE":
				deviceList.remove(conn.getClientNo());
				break;
			default :
				break;
			}
		}
	} // removeOne()
	
	public Connection getDeviceConn(String deviceNo) throws Exception {
		Connection conn = null;

		System.out.println("getDeviceConn - " + deviceNo);
		if(deviceList.containsKey(deviceNo))
			conn = deviceList.get(deviceNo);
		System.out.println("getDeviceConn - " + conn.toString());
		
		return conn;
	}
	
	public Connection getGuestConn(String guestNo) {
		Connection conn = null;
		
		if(guestList.containsKey(guestNo))
			conn = deviceList.get(guestNo);
		
		return conn;
	}
	
	public Map<String, Connection> getGuestList() {
		return guestList;
	}

	public Map<String, Connection> getDeviceList() {
		return deviceList;
	}

	public void broadcast(String data) {
		Iterator<Connection> iterator = LatteServer.getConnections().iterator();
		while(iterator.hasNext()) {
			Connection conn = iterator.next();
			conn.send(data);
		}
//		for(Connection conn : LatteServer.getConnections()) {
//			conn.send(data);
//		}
	}
	
	public void broadcastDevice(Message response) {
		for(String key : deviceList.keySet()) {
			Connection conn = deviceList.get(key);
			conn.send(response);
		}
	}
	
	public void clear() {
		guestList.clear();
		deviceList.clear();
	}
	
	public void sendOneGuest(String guestNo, Message data) {
		try {
			this.guestList.get(guestNo).send(data);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public void sendOneDevice(String deviceNo, Message data) {
		try {
			this.deviceList.get(deviceNo).send(data);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	
	// =================================================
	// private methods
	private void guestAuth(Connection conn, Message data) {
		/** Member registration confirmation */
		// Message {null, LOGIN, null, Guest{null, ID, PW, null, null}}
		
		GuestDAO gdao = new GuestDAO();
		
		// Registration Information Search
		Guest input = gson.fromJson(data.getJsonData(), Guest.class);
		Guest result = gdao.selectGuest(input);
		
		
		if(result != null) {
			// Registered guest
			conn.setClientNo(result.getUserNo());
			conn.setType("GUEST");
			guestList.put(conn.getClientNo(), conn);
			
			data = new Message(result.getUserNo(), "LOGIN", "SUCCESS", gson.toJson(result));
		} else {
			// No guest information
			data = new Message(null, "LOGIN", "FAIL", null);
		}
		

		// Send request results
		conn.send(data);
		
	} // addGuest()
	
	
	private void guestReconn(Connection conn, Message data) {
		// Message {guestNo, RECONNECT, null, null}
		
		Connection prev = guestList.get(data.getClientNo());
		if(prev != null) {
			prev.close();
			guestList.remove(data.getClientNo());
		}
		conn.setClientNo(data.getClientNo());
		conn.setType("GUEST");
		guestList.put(conn.getClientNo(), conn);
		
	} // guestReconn()
	
	
	private void deviceAuth(Connection conn, Message data) {
		// Message {deviceNo, CONNECT, null, null}
		
		DeviceDAO ddao = new DeviceDAO();
		
		// DB 등록 확인
		int count = ddao.selectDevice(data.getClientNo());
		
		if(count == 1) {
			System.out.println("DB에 있다");
			
			Connection prev = null;
			if((prev=deviceList.get(data.getClientNo())) != null) {
				prev.close();
				deviceList.remove(data.getClientNo());
			}
			
			if(conn != null) {
				System.out.println("연결객체 쏴라있다");
				conn.setClientNo(data.getClientNo());
				conn.setType("DEVICE");
				deviceList.put(conn.getClientNo(), conn);
				System.out.println("[deviceAuth] _ " + deviceList.get(data.getClientNo()).toString());
			}
			
		}
//		System.out.println(deviceList.get(data.getClientNo()).toString());
		
		
//		DeviceDAO ddao = new DeviceDAO();
//		String input = data.getClientNo();
////		int count = 1;	// dao
//		int count = ddao.selectDevice(input);
//		
//		if(count == 1) {
//			// 기존 연결 확인, 제거
//			Connection prev = deviceList.get(data.getClientNo());
//			if(prev != null) {
//				prev.close();
//				deviceList.remove(data.getClientNo());
//			}
//		}
//		
//		// 연결 등록
//		conn.setClientNo(data.getClientNo());
//		conn.setType("DEVICE");
//		deviceList.put(data.getClientNo(), conn);
//		
//		System.out.println("deviceAuth " + conn.toString());
		
	} // deviceAuth()
	
}
