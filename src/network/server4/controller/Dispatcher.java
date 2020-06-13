package network.server4.controller;

import java.net.Socket;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
	//	private Map<>
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
	// methods
	public Connection accept(Socket socket) {
		Connection conn = null;
		conn = new Connection(socket);
		return conn;
	}
	
	public void read(Connection conn, String jsonData) {
		broadcast(jsonData);
		
		Message data = gson.fromJson(jsonData, Message.class);
		switch (data.getCode1()) {
		case "LOGIN":
			System.out.println(gson.fromJson(data.getJsonData(), Guest.class));
			break;

		default:
			break;
		}
		
	}
	
	public void removeConn(Connection conn) {
		LatteServer.getConnections().remove(conn);
	}
	
	public void broadcast(String data) {
		for(Connection conn : LatteServer.getConnections()) {
			conn.send(data);
		}
	}
	
	public void clear() {
		
	}
	
}
