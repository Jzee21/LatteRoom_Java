package network.server.test.network;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import network.server.vo.Message;
import network.server.vo.Sensor;

public class DatagramServer {

	private static final String ADDR = "localhost";
	private static final int PORT = 55000;
	private static final int SIZE = 10240;
	
	private DatagramSocket ds;
	private DatagramPacket dp;

	private ExecutorService executor;
	
	private Map<String, Client> list = new ConcurrentHashMap<String, Client>();
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
//						dp.setLength(SIZE);
						Arrays.fill(buffer,(byte)0);
						ds.receive(dp);
						int dataLen = dp.getLength();
//						String line = new String(dp.getData(), 0, dataLen);
						String line = new String(dp.getData(), StandardCharsets.UTF_8);
						System.out.println("[data (" + dataLen + ")] " + line);
						
						Client client = new Client(dp);
						list.putIfAbsent(client.ip, client);
						
//						Message message = gson.fromJson(line, Message.class);
//						System.out.println("[json data] " + message.getJsonData());
						
						if(line.equals("@EXIT")) {
							close();
						}
//						send(dp);
						for (String key : list.keySet()) {
							Client c = list.get(key);
							send(c.getPacket(line));
						}
						
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
	
	class Client {
		private InetAddress addr;
		private int port;
		private String ip;
		
		private Client(InetAddress ip) {
			this.addr = ip;
			this.ip = ip.toString();
		}
		
		public Client(DatagramPacket dp) {
			this(dp.getAddress());
			this.port = dp.getPort();
		}
		
		public String getIp() {
			return this.ip;
		}
		
		public DatagramPacket getPacket(String text) {
			byte[] buf = text.getBytes(StandardCharsets.UTF_8);
			return new DatagramPacket(buf, buf.length, this.addr, this.port);
			
		}
		
	}

}
