package network.server2.test.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import network.server2.dao.Device;

public class ServerService {
	
	// =========================================================
	// field
	private static final int PORT = 55577;
	
	private Selector selector;
	private ServerSocketChannel server;
	
	private Charset charset = null;
	private CharsetDecoder decoder = null;
	
	private Map<Integer, Device> list;
	private ExecutorService executor;
	
	// =========================================================
	// Singleton Pattern
	private ServerService() {
		
	}
	
	private static class InstanceHandler {
		public static final ServerService INSTANCE = new ServerService();
	}
	
	public static ServerService getInstance() {
		return InstanceHandler.INSTANCE;
	}
	
	// =========================================================
	// get, set
	
	
	// =========================================================
	// methods
	private void initServer() {
		try {
			selector = Selector.open();
			server = ServerSocketChannel.open();
			server.configureBlocking(false);
			server.bind(new InetSocketAddress(PORT));
			server.register(this.selector, SelectionKey.OP_ACCEPT);
			
			charset = Charset.forName("UTF-8");
	        decoder = charset.newDecoder();
	        
	        this.list = new HashMap<Integer, Device>();
	        this.executor = Executors.newCachedThreadPool();
	        
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void startServer() {
		// initialization
		initServer();
		
		Runnable server = () -> {
			while (true) {
				try {
					// check event
					if(selector.select() > 0) {
						Set<SelectionKey> selectionKeySet = selector.selectedKeys();
						Iterator iterator = selectionKeySet.iterator();
						
						while (iterator.hasNext()) {
							SelectionKey key = (SelectionKey) iterator.next();
							
							if (key.isAcceptable()) {
								accept(key);
							} else if (key.isReadable()) {
								receive(key);
							} else if (key.isWritable()) {
								//
							}
							iterator.remove();
						} // while (iterator)
					} // if (selector.select())
				} catch (Exception e) {
					System.out.println("Error] startServer - " + e);
					break;
				}
			} // while (true)
			
			// server stop
			try {
				stopServer();
			} catch (Exception e) {
				e.printStackTrace();
			}
		};
		executor.submit(server);
	}
	
	public void stopServer() throws Exception {
		if(server != null && server.isOpen()) {
			server.close();
			selector.close();
		}
		if(executor != null && !executor.isShutdown()) {
			executor.shutdownNow();
		}
	}
	
	private void accept(SelectionKey key) throws Exception {
		ServerSocketChannel server = (ServerSocketChannel) key.channel();
		
		SocketChannel channel = server.accept();
		if(channel == null) return;
		
		channel.configureBlocking(false);
		channel.register(selector, SelectionKey.OP_READ);
		
		list.put(channel.hashCode(), new Device(channel));
		System.out.println(channel.getLocalAddress().toString() + "] connected");
	}
	
	private void receive(SelectionKey key) {
		try {
			SocketChannel channel = (SocketChannel) key.channel();
			channel.configureBlocking(false);
			
			ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
			int size = channel.read(buffer);
			
			// Nothing received
			if(size == -1) {
				System.out.println(channel.getLocalAddress() + "] closed");
				list.remove(channel.hashCode());
				channel.close();
				key.channel();
				return;
			}
			
			buffer.flip();
			String data = decoder.decode(buffer).toString();
			buffer.rewind();
			System.out.println(channel.getLocalAddress().toString() + "] " + data);
			
			if(data != null) {
				broadcast(buffer);
			}
			
		}catch (Exception e) {
			System.out.println(e);
		}
	}
	
	private void broadcast(ByteBuffer byteBuffer) throws Exception {
		for(int key : list.keySet()) {
			SocketChannel channel = list.get(key).getChannel();
			if (channel != null) {
				channel.write(byteBuffer);
				byteBuffer.rewind();
			}
		}
		byteBuffer.clear();
	}
	
	
}
