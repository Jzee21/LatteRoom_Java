package network.server.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import network.server.main.LatteServer;
import network.server.vo.Message;

public class Connection implements Runnable {
	/*
	 * 	Connection
	 * 	- Indicate a connection to a client
	*/
	
	// =================================================
	// field
	private Socket socket;
	private BufferedReader br;
	private PrintWriter pw;
	
	private String clientNo;	// Guest.userNo or Device.deviceNo
	private String type;		// Guest or Device (default : null)
	
	private Dispatcher dispatcher = Dispatcher.getInstance();
	private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
	
	
	// =================================================
	// Constructor
	public Connection (Socket socket) {
		this.socket = socket;
	}
	
	
	// =================================================
	// get, set
	public String getClientNo() {
		return clientNo;
	}
	
	public void setClientNo(String clientNo) {
		this.clientNo = clientNo;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	
	// =================================================
	// methods
	@Override
	public void run() {
		/** Receive a message */
		try {
			this.br = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			this.pw = new PrintWriter(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
			this.close();
		}
		System.out.println("[" + socket.getRemoteSocketAddress() + "] connected");
		System.out.println("=== 현재 연결 수 : " + LatteServer.getConnections().size() + " ===");
		
		String line = "";
		while(true) {
			try {
				line = br.readLine();
//				System.out.println("line = " + line);
				if(line == null) {
					throw new IOException();
				} else {
					dispatcher.read(this, line);
				}
			} catch (IOException e) {
//				e.printStackTrace();
				break;
			}
		} // while(true)
		this.close();
		
	} // run()
	
	public void send(String msg) {
		/** Send a message */
		if(this.socket != null && !socket.isClosed()) {
			pw.println(msg);
			pw.flush();
		}
	}
	
	public void send(Message msg) {
		send(gson.toJson(msg));
	}
	
	public synchronized void close() {
		/** Close the socket to close the connection */
		if(socket != null) {
			String addr = socket.getRemoteSocketAddress().toString();
			try {
				if(socket != null && !socket.isClosed()) {
					socket.close();
					br.close();
					pw.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			} // try
			this.socket = null;
			this.br = null;
			this.pw = null;
			
			dispatcher.removeOne(this);
			System.out.println("[" + addr + "] closed");
			System.out.println("=== 현재 연결 수 : " + LatteServer.getConnections().size() + " ===");
		}
	} // close()
	
	public String getConnAddr() {
		return socket.getRemoteSocketAddress().toString();
	}


	@Override
	public String toString() {
		return "Connection [" + getConnAddr() + "][clientNo=" + clientNo + ", type=" + type + "]";
	}
	
	
}
