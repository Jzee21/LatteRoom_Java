package network.server2.vo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import network.server2.controller.ServerController;

public class Guest implements Client {

	private String userID;
	private String loginID;
	private String loginPW;
	private String userRole;
	private String authCode;
	
	private Socket socket;
	private BufferedReader input;
	private PrintWriter output;
	
	private ServerController controller = ServerController.getInstance();
	
	public Guest(String id, Socket socket) {
		this.userID = id;
		this.socket = socket;
	}
	
	
	// =================================================
	@Override
	public void run() {
		try {
			this.input = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			this.output = new PrintWriter(socket.getOutputStream());
		} catch (IOException e) {
			this.close();
		} // try
		System.out.println("[" + socket.getInetAddress().toString() + "][" + userID + "] connected");
		
		String line = "";
		while(true) {
			try {
				line = input.readLine();
				if(line == null) {
					throw new IOException();
				} else {
					System.out.println("[" + userID + "] " + line);
//						service.dataHandler(this, line);
					controller.broadcast(line);
				}
			} catch (IOException e) {
				break;
			}
		} // while()
		this.close();
	}
	
	@Override
	public void close() {
		if(socket != null) {
			String addr = socket.getInetAddress().toString();
			try {
				if(socket != null && !socket.isClosed()) {
					socket.close();
					input.close();
					output.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			} // try
			this.socket = null;
			this.input = null;
			this.output = null;
			System.out.println("[" + addr + "] closed");
		}
	}
	
	@Override
	public void send(String data) {
		// TODO Auto-generated method stub
		try {
			this.output.println(data);
			this.output.flush();
		} catch (Exception e) {
			close();
		}
	}
	
	@Override
	public void setSocket(Socket socket) {
		this.close();
		this.socket = socket;
	}
	
	@Override
	public String getId() {
		return this.userID;
	}
	
	
	// =================================================
	// get, set
	
}
