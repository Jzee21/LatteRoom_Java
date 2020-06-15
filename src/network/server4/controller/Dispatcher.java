package network.server4.controller;

import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import network.server4.dao.GuestDAO;
import network.server4.main.LatteServer;
import network.server4.vo.*;

public class Dispatcher {
	/*
	 * 	Dispatcher
	 *  - Manage socket connections and distribute messages to controllers.
	*/
	
	// =================================================
	// field
	private GuestController gController = GuestController.getInstance();
	private DeviceController dController = DeviceController.getInstance();
	
	private GuestDAO gdao = new GuestDAO();
	
	private Map<String, Connection> guestList = new ConcurrentHashMap<String, Connection>();
	private Map<String, Connection> deviceList = new ConcurrentHashMap<String, Connection>();
	private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").create();
	

	// =================================================
	// Singleton
	private Dispatcher() {
	}
	
	private static class InstanceHandler {
		public static final Dispatcher INSTANCE = new Dispatcher();
	}
	
	public static Dispatcher getInstance() {
		return InstanceHandler.INSTANCE;
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
			
			switch (data.getCode1()) {
			/** Device */
			// Device Connect
			case "CONNECT":
				deviceAuth(conn, data);
				break;
				
			// Device status changes
			case "UPDATE":
//				deviceController.dataHandler(data);
				dController.upToDate(conn, data);
				break;
			
				
			/** Guest */
			// Guest Login
			case "LOGIN":
				System.out.println("LOGIN : " + gson.fromJson(data.getJsonData(), Guest.class));
				guestAuth(conn, data);
				break;
				
			// Connection renewal
			case "RECONNECT":
				guestReconn(conn, data);
				break;
				
			// Guest requests device control
			case "CONTROL":
				dController.requestControl(conn, data);
				break;
				
			// Guest requests reservation list
			case "RESERVLIST":
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
					gController.requestAlarmSet(conn, data);
				break;
				
			// Guest requests detail alarm infos
			case "ALARMJOB":
				if(data.getCode2().equals("GET"))
					gController.requestAlarmJobInfo(conn, data);
				else
					gController.requestAlarmJobSet(conn, data);
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
	
	public Connection getDeviceConn() {
		Connection conn = null;
		
		return conn;
	}
	
	public Connection getGuestConn() {
		Connection conn = null;
		
		return conn;
	}
	
	public void broadcast(String data) {
		for(Connection conn : LatteServer.getConnections()) {
			conn.send(data);
		}
	}
	
	public void clear() {
		guestList.clear();
		deviceList.clear();
	}
	
	
	// =================================================
	// private methods
	private void guestAuth(Connection conn, Message data) {
		/** Member registration confirmation */
		// Message {null, LOGIN, null, Guest{null, ID, PW, null, null}}
		
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
		
		String input = data.getClientNo();
		int count = 1;	// dao
		
		if(count == 1) {
			// 기존 연결 확인, 제거
			Connection prev = deviceList.get(data.getClientNo());
			if(prev != null) {
				prev.close();
				deviceList.remove(data.getClientNo());
			}
			// 신규 연결 등록
			conn.setClientNo(data.getClientNo());
			conn.setType("DEVICE");
			deviceList.put(conn.getClientNo(), conn);
		}
	} // deviceAuth()
	
}
