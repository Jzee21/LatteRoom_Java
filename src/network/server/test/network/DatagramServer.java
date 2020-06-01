package network.server.test.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import network.server.vo.Message;

public class DatagramServer {

	private static final String ADDR = "localhost";
	private static final int PORT = 55000;
	private static final int SIZE = 10240;
	
	private DatagramSocket ds;
	private DatagramPacket dp;

	private ExecutorService executor;
	
	private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").create();
	
	public static void main(String[] args) {
		
		DatagramServer server = new DatagramServer();
		try {
			System.out.println("엔터를 치면 서버가 종료됩니다.");
			server.init();
			System.in.read();
			server.close();
		} catch (Exception e) {
			e.printStackTrace();
			server.close();
		}
		System.out.println("서버가 종료되었습니다.");

	}
	
	private void init() {
		try {
			ds = new DatagramSocket(PORT);
			byte[] buffer = new byte[SIZE];
			dp = new DatagramPacket(buffer, SIZE);
			
			executor = Executors.newFixedThreadPool(4);
			
			Runnable receiver = () -> {
				while(true) {
					try {
						ds.receive(dp);
						int dataLen = dp.getLength();
						String line = new String(dp.getData(), 0, dataLen);
						System.out.println("[data (" + dataLen + ")] " + line);
						
						Message message = gson.fromJson(line, Message.class);
						System.out.println("[json data] " + message.getJsonData());
						
						if(line.equals("@EXIT")) {
							close();
						}
						send(dp);
						
					} catch (IOException e) {
//						e.printStackTrace();
						close();
					}
				}
			};
			executor.submit(receiver);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void close() {
		ds.close();
		executor.shutdownNow();
	}
	
	private void send(DatagramPacket dp) {
		try {
			dp = new DatagramPacket(dp.getData(), dp.getLength(), dp.getAddress(), dp.getPort());
			ds.send(dp);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
