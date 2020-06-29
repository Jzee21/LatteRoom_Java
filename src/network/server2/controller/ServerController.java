package network.server2.controller;

import java.net.Socket;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import network.server2.vo.Client;
import network.server2.vo.Device;
import network.server2.vo.Guest;

public class ServerController {
	/*
	 *	Controller Package
	 *	Management of connected clients 
	 */
	
	// =================================================
	// field
	private Map<String, Client> connections = new ConcurrentHashMap<String, Client>();
	
	private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").create();
	
	// =================================================
	// Singleton
	private ServerController() {
		
	}
	
	private static class InstanceHandler {
		public static final ServerController INSTANCE = new ServerController();
	}
	
	public static ServerController getInstance() {
		return InstanceHandler.INSTANCE;
	}
	
	
	// =================================================
	// get, set
	public Map<String, Client> getConnections() {
		return connections;
	}
	
	
	// =================================================
	// methods
	public Client add(String deviceID, String deviceType, Socket socket) {
		Client client = null;
		// Client not connected
		if((client = connections.get(deviceID)) == null) {
			if(deviceType.equals("USER")) {
				client = new Guest(deviceID, socket);
				connections.put(deviceID, client);
			} else if(deviceType.equals("DEVICE")) {
				client = new Device(deviceID, socket);
				connections.put(deviceID, client);
			}
		} else {
			client.setSocket(socket);
		}
		
		return client;
	}
	
	public Client add(Socket socket) {
		Client client = null;
		
		client = new Device("" + socket.hashCode(), socket);
		connections.put("" + socket.hashCode(), client);
		
		return client;
	}
	
	
	public void remove(Client client) {
		client.close();
		connections.remove(client, client);
	}
	
	public Client get(String key) {
		return this.connections.get(key);
	}
	
	public Set<String> getKeyset() {
		return connections.keySet();
	}
	
	public void dataHandler() {
		
	}
	
	// test
	public void broadcast(String data) {
		for(String key : connections.keySet()) {
			Client c = connections.get(key);
			c.send(data);
		}
	}
	
}
