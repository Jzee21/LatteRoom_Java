package network.server3.main;

import network.server3.controller.ServerController;

public class LatteNioServer {
	
	public static void main(String[] args) {
		ServerController server = ServerController.getInstance();
		
		System.out.println("엔터를 치면 서버가 종료됩니다.");
		try {
			server.startServer();
			System.in.read();
			server.stopServer();
		} catch (Exception e) {
			System.out.println("[Server Main] " + e);
		}
		System.out.println("서버가 종료되었습니다.");
	}

}
