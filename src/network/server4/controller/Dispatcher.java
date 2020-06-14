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
	}
	
	public void read(Connection conn, String jsonData) {
//		broadcast(jsonData);
		
		try {
			Message data = gson.fromJson(jsonData, Message.class);
			switch (data.getCode1()) {
			case "LOGIN":
				System.out.println("LOGIN : " + gson.fromJson(data.getJsonData(), Guest.class));
				addGuest(conn, data);
				break;
				
			default:
				break;
			}
		} catch (Exception e) {
//			e.printStackTrace();
		}
		
	}
	
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
	private void addGuest(Connection conn, Message data) {
		/** Member registration confirmation */
		// Registration Information Search
		Guest input = gson.fromJson(data.getJsonData(), Guest.class);
		Guest result = gdao.checkLogin(input);
		
		
		if(result != null) {
			// Registered guest
			conn.setClientNo(result.getUserNo());
			conn.setType("GUEST");
			guestList.put(conn.getClientNo(), conn);
			
			data = new Message(result.getUserNo(), "LOGIN", "SUCCESS", gson.toJson(result));
		} else {
			// No guest information
			data = new Message(null, "LOGIN", "FAILE", null);
		}

		// Send request results
		conn.send(data);
	} // addGuest()
	
}
