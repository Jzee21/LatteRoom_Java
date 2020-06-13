package network.server4.main;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import network.server4.controller.Connection;
import network.server4.controller.Dispatcher;

public class LatteServer {

	// =================================================
	// field
	private static int PORT = 55566;
	
	private static ServerSocket server;
	private static Dispatcher dispatcher;
	private static ExecutorService executor;
	private static Vector<Connection> connections;
	
	
	// =================================================
	// methods
	private static void startServer() throws Exception {
		
		dispatcher = Dispatcher.getInstance();
		executor = Executors.newCachedThreadPool();
		connections = new Vector<Connection>();
		
		server = new ServerSocket();
		server.bind(new InetSocketAddress(PORT));
		server.setSoTimeout(3000);
		
		Runnable accept = () -> {
			Socket socket = null;
			while(true) {
				try {
					socket = server.accept();
					Connection conn = dispatcher.accept(socket);
					connections.add(conn);
					executor.submit(conn);
//					System.out.println("=== 현재 연결 수 : " + connections.size() + " ===");
					
				} catch (SocketTimeoutException e) {
					if(Thread.interrupted()) {
						break;
					} else continue;
				} catch (IOException e) {
//					e.printStackTrace();
					break;
				}
			}
			stopServer();
		};
		executor.submit(accept);
		
	} // startServer()
	
	private static void stopServer() {
		
		try {
			if(connections != null) {
				for(Connection conn : connections) {
					conn.close();
				}
			}
			if(server != null && !server.isClosed())
				server.close();
			if(executor != null && !executor.isShutdown())
				executor.shutdownNow();
			dispatcher.clear();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	} // stopServer()
	
	public static Vector<Connection> getConnections() {
		return connections;
	}
	
	
	// =================================================
	// main
	public static void main(String[] args) {
		
		System.out.println("엔터를 치면 서버가 종료됩니다.");
        try {
        	startServer();
            System.in.read();
            stopServer();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println("서버가 종료되었습니다.");
        
	} // main()
	
}
