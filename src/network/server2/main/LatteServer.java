package network.server2.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import network.server2.controller.*;
import network.server2.service.*;
import network.server2.vo.*;

public class LatteServer {
	
	// =================================================
	// field
	private static final int PORT = 55566;
	
	private ServerSocket server;
	private ExecutorService executor;
	private ServerController controller = ServerController.getInstance();
	
	
	// =================================================
	// main
	public static void main(String[] args) {
		
		LatteServer server = new LatteServer();
		
		System.out.println("엔터를 치면 서버가 종료됩니다.");
		try {
			server.startServer();
			System.in.read();
			server.stopServer();
		} catch (Exception e) {
			System.out.println("[Server Main] " + e);
			e.printStackTrace();
		}
		System.out.println("서버가 종료되었습니다.");
	}
	
	// =================================================
	// methods
	private void startServer() {
		executor = Executors.newCachedThreadPool();
		
		try {
			server = new ServerSocket();
			server.bind(new InetSocketAddress(PORT));
			server.setSoTimeout(3000);
		} catch (Exception e) {
			System.out.println("[Server - startServer] " + e);
			e.printStackTrace();
			if(!server.isClosed()) {
				stopServer();
			}
		}
		
		System.out.println("접속 대기중.....");
		
		Runnable accepter = () -> {
			Socket socket = null;
			String deviceID = "";
			String deviceType = "";
			while(true) {
				try {
					socket = server.accept();
					
					BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//					if(input.ready()) deviceID = input.readLine();
//					if(input.ready()) deviceType = input.readLine();
					
//					if(!deviceID.equals("") && !deviceType.equals("")) {
//						Client client = controller.add(deviceID, deviceType, socket);
//						executor.submit(client);
//						System.out.println("[" + socket.getInetAddress().toString() + "] connected");
//					}
//					executor.submit(c);
					
					Client client = controller.add(socket);
					executor.submit(client);
//					System.out.println("[" + socket.getInetAddress().toString() + "] connected");
					
				} catch (SocketTimeoutException e) {
					if(Thread.interrupted()) {
						break;
					} else continue;
				} catch (IOException e) {
					e.printStackTrace();
					break;
				}
			}
			stopServer();
		};
		executor.submit(accepter);
		
//		Runnable checker = () -> {
//			for(String key : controller.getKeyset()) {
//				Client c = controller.get(key);
//				try {
//					c.send("LATTE");
//				} catch (Exception e) {
//					c.close();
//					controller.remove(c);
//				}
//			}
//			try {
//				Thread.sleep(5000);
//			} catch (Exception e2) {
//			}
//		};
//		executor.submit(checker);
	}
	
	private void stopServer() {
		try {
			for (String key : controller.getConnections().keySet()) {
				Client c = controller.get(key);
				c.close();
			}
			if(server != null && !server.isClosed()) {
				server.close();
			}
			if(executor != null && !executor.isShutdown()) {
				executor.shutdownNow();
			}
		} catch (Exception e) {
			System.out.println("[Server - stopServer] " + e);
			e.printStackTrace();
		}
		
	}

}
