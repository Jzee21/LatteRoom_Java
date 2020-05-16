package arduino.test.dao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import arduino.test.client.TestClient;
import arduino.test.vo.*;


public class ServerListener {
	
	private static final String SERVER_ADDR = "localhost";
	private static final int SERVER_PORT = 55566;
	private TestClient client;
	private String deviceID;
	private String deviceType;
	
	private Socket socket;
	private BufferedReader serverIn;
	private PrintWriter serverOut;
	private ExecutorService executor;
	
	private TestSharedObject sharedObject;
	private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").create();
	
	public ServerListener(TestClient client, TestSharedObject sharedObject) {
		this.sharedObject = sharedObject;
	}
	
	public void setDeviceInfo(String deviceID, String deviceType) {
		this.deviceID = deviceID;
		this.deviceType = deviceType;
	}
	
	public void initialize() {
		
		executor = Executors.newFixedThreadPool(1);
		
		Runnable runnable = () -> {
			try {
				socket = new Socket();
				socket.connect(new InetSocketAddress(SERVER_ADDR, SERVER_PORT));
				serverIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				serverOut = new PrintWriter(socket.getOutputStream());
			} catch (IOException e) {
//				e.printStackTrace();
				close();
				return;
			}
			
			// 
			send(this.client.getDeviceID());
			send(this.client.getDeviceType());
			send(new Message(this.client.getDeviceID()
					, "SENSOR_LIST"
					, this.client.getSensorList()));
			
			
			String line = "";
			while(true) {
				try {
					line = serverIn.readLine();
					
					if(line == null) {
//						displayText("server error. disconnected");
//						throw new IOException();
//					} else {
//						displayText("Server ] " + line);
//						
//						Message messate = gson.fromJson(line, Message.class);
//						int hopeTemp = Integer.parseInt(gson.fromJson(messate.getJsonData(), SensorData.class).getStates());
//						sharedObject.setHopeStates(hopeTemp);
						
					}
				} catch (IOException e) {
//					e.printStackTrace();
					close();
					break;
				}
			} // while()
		};
		executor.submit(runnable);
	} // startClient()
	
	public void close() {
		try {
			if(socket != null && !socket.isClosed()) {
				socket.close();
				if(serverIn != null) serverIn.close();
				if(serverOut != null) serverOut.close();
			}
			if(executor != null && !executor.isShutdown()) {
				executor.shutdownNow();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	} // stopClient()
	
	public void send(String msg) {
		serverOut.println(msg);
		serverOut.flush();
	}
	
	public void send(Message msg) {
		serverOut.println(gson.toJson(msg));
//		serverOut.println("서버야 좀 받아라~!");
		serverOut.flush();
//		displayText("서버로 보냈다!!! "+msg);
	}
	
	public void send(String sensorID, String states) {
		Message message = new Message(new SensorData(sensorID, states));
		send(message);
	}
	
}
