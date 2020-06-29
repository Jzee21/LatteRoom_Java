package network.server4.main;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import network.server4.controller.Connection;
import network.server4.controller.Dispatcher;

public class LatteServer {

	// =================================================
	// field
	private static int PORT = 55566;
	
	private static ServerSocket server;
	private static Dispatcher dispatcher = Dispatcher.getInstance();
	private static ExecutorService executor;
	private static List<Connection> connections;
	
	
	// =================================================
	// methods
	private static void startServer() throws Exception {
		
//		dispatcher = Dispatcher.getInstance();
		dispatcher.setController();
		executor = Executors.newCachedThreadPool();
		connections = new CopyOnWriteArrayList<Connection>();
		
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
			
			if(connections != null && connections.size()>0) {
				Iterator<Connection> conns = connections.iterator();
				while(conns.hasNext()) {
					Connection conn = conns.next();
					conn.close();
				}
				connections = null;
			}
			if(server != null && !server.isClosed())
				server.close();
			if(executor != null && !executor.isShutdown()) {
//				executor.shutdownNow();
				executor.shutdown();
				do {
					if (executor.isTerminated()) {
						List<Runnable> list = executor.shutdownNow();
						System.out.println(list.size() + " job is alive...");
					}
				} while (!executor.awaitTermination(10, TimeUnit.SECONDS));
			}
			dispatcher.clear();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	} // stopServer()
	
	public static List<Connection> getConnections() {
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
