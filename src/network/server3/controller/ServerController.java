package network.server3.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import network.server3.dao.*;
import network.server3.vo.*;

public class ServerController {

	// =================================================
	// field
	private static final int BUFFSIZE = 20480;
	
	private Selector selector = null;
	private Vector<SocketChannel> channels = new Vector<SocketChannel>();
	private ConcurrentHashMap<SocketChannel, String> userChannel = 
			new ConcurrentHashMap<SocketChannel, String>();
	private ConcurrentHashMap<SocketChannel, String> deviceChannel = 
			new ConcurrentHashMap<SocketChannel, String>();
	
	private Controller deviceController = DeviceController.getInstance();
	private Controller guestController = GuestController.getInstance();
	
	private GuestDAO guestDao = new GuestDAO();
	
	private Charset charset = Charset.forName("UTF-8");
	private CharsetDecoder decoder = charset.newDecoder();
	private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").create();
	
	
	// =================================================
	// Singleton
	private ServerController() {
		
	}
	
	private static class InstanceHandler {
		public static final ServerController INSTANCE = new ServerController();
	}
	
	public static ServerController getInstance() {
		return InstanceHandler.INSTANCE;
	}
	
	
	// =================================================
	// methods
	private void initServer() throws IOException {
		// Open Selector
		selector = Selector.open();
		
		// Oepn Channel
		ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);			 // Non-blocking
        serverSocketChannel.bind(new InetSocketAddress(55566));

        // Register a channel with a selector
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
	}
	
	public void startServer() {
		try {
			initServer();
			
			while(true) {
				selector.select();			// Check prepared events
				
				Set<SelectionKey> selectionKeySet = selector.selectedKeys();
	            Iterator iterator = selectionKeySet.iterator();

	            while (iterator.hasNext()) {
	                SelectionKey selectionKey = (SelectionKey) iterator.next();

	                if (selectionKey.isAcceptable()) {
	                    accept(selectionKey);
	                } else if (selectionKey.isReadable()) {
	                    read(selectionKey);
	                } else if (selectionKey.isWritable()) {
//	                	send(selectionKey);
	                }

	                iterator.remove();
	            }
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			stopServer();
		}		
	}
	
	public void stopServer() {
		try {
			if(selector != null) {
				selector.close();
				for(SocketChannel channel : channels) {
					channel.close();
				}
			}
		} catch (Exception e) {
		}
	}
	
	private void accept(SelectionKey key) {
		
		try {
			ServerSocketChannel server = (ServerSocketChannel) key.channel();
			
			// Create socket channel
			SocketChannel channel = server.accept();
			if(channel == null) return;
			
			// Add the created channel to the selector
			// (non-blocking, read mode)
			channel.configureBlocking(false);
			channel.register(selector, SelectionKey.OP_READ);
			
			this.channels.add(channel);
			System.out.println(channel.getRemoteAddress().toString() + "] connected");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void read(SelectionKey key) {
		// Get a socket channel from SelectionKey
		SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(BUFFSIZE);
        
        try {
			int count = channel.read(byteBuffer);	// Reading data from the socket
			if(count < 0) {							// Socket closed from client
				closeChannel(channel);
				key.cancel();
				return;
			}
			
			// test
//			broadcast(byteBuffer);
			
			byteBuffer.flip();
            String jsonString = decoder.decode(byteBuffer).toString();
//            System.out.print(jsonString);
//            broadcast(jsonString);
            Message data = gson.fromJson(jsonString, Message.class);
            System.out.println(data.toString());
            
            switch (data.getCode1()) {
			/** Device */
			// Device Connect
			case "CONNECT":
				
				break;
			// Device Change States
			case "UPDATE":
				deviceController.dataHandler(data);
				break;
			
			/** Guest */
			// Guest Login
			case "LOGIN":
				System.out.println("Code : LOGIN");
				connectUser(channel, data.getJsonData());
				break;
			// Change channel
			case "RECONNECT":
				
				break;
			// Guest requests device control
			case "CONTROL":
				
				break;
			//
			case "RESERVLIST":
				
				break;
			//
			case "ROOMDETAIL":
				
				break;
			//
			case "ALARM":
				
				break;
			default:
				break;
			}
			
			
			
		} catch (IOException e) {
			closeChannel(channel);
			key.cancel();
		}
	}
	
	private void closeChannel(SocketChannel channel) {
		try {
			System.out.println(channel.getRemoteAddress().toString() + "] closed");
			channel.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	private void connectUser(SocketChannel channel, String jsonData) {
		System.out.println("connectUser");
		Guest input = gson.fromJson(jsonData, Guest.class);
		Guest result = guestDao.checkLogin(input);
		
		System.out.println("connectUser : Message");
		Message data = null;
		if(result != null) {
			userChannel.put(channel, result.getUserNo());
			data = new Message(result.getUserNo(), "LOGIN", "SUCCESS", gson.toJson(result));
		} else {
			data = new Message(null, "LOGIN", "FAILE", null);
		}
		System.out.println("connectUser : Send " + data.toString());
//		send(channel, gson.toJson(jsonData));
		
		try {
			broadcast(gson.toJson(jsonData));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
//		try {
//			channel.register(selector, SelectionKey.OP_WRITE, gson.toJson(data));
//		} catch (ClosedChannelException e) {
//			System.out.println("전송 등록 실패");
//			e.printStackTrace();
//		}
		
	}
	
	private void send(SocketChannel channel, String jsonData) {
		
		ByteBuffer buffer = ByteBuffer.allocateDirect(BUFFSIZE);
		buffer.clear();
		buffer.put(jsonData.getBytes());
		buffer.flip();
		
//		Iterator iterator = channels.iterator();
//		while (iterator.hasNext()) {
//			SocketChannel socketChannel = (SocketChannel) iterator.next();
//			if (socketChannel != null) {
//				socketChannel.write(buffer);
//				buffer.rewind();
//				}
//			}
		if(channel != null) {
			try {
				channel.write(buffer);
			} catch (IOException e) {
				e.printStackTrace();
			}
			buffer.rewind();
		}
		buffer.clear();
		
	}
	
//	private void send(SelectionKey key) {
//		
//		try {
//			SocketChannel channel = (SocketChannel) key.channel();
//			channel.configureBlocking(true);
//			
//			String jsonData = (String) key.attachment();
//			System.out.println("before send : " + jsonData);
////			ByteBuffer buffer = ByteBuffer.allocateDirect(BUFFSIZE);
////			buffer.clear();
////			buffer.put(jsonData.getBytes());
////			buffer.flip();
////			channel.write(buffer);
////			buffer.clear();
//			
//			Socket socket = channel.socket();
//			PrintWriter out = new PrintWriter(socket.getOutputStream());
//			out.println(jsonData);
//			out.flush();
//			out.close();
//			
//			channel.configureBlocking(false);
//			channel.register(selector, SelectionKey.OP_READ);
//			System.out.println("buffer clear");
//			
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
//	}
	
//	private void send(SocketChannel channel, Message data) {
//		if(channel != null && data != null) {
//			System.out.println("보내자");
//			try {
//				ByteBuffer buffer = ByteBuffer.allocateDirect(BUFFSIZE);
//				buffer.clear();
//				
//				String jsonString = gson.toJson(data);
//				buffer.put(jsonString.getBytes());
//				buffer.flip();
//				
//				channel.write(buffer);
//				buffer.clear();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//	}
	
	
	private void broadcast(ByteBuffer byteBuffer) throws IOException {
        byteBuffer.flip();
        Iterator iterator = channels.iterator();

        while (iterator.hasNext()) {
            SocketChannel socketChannel = (SocketChannel) iterator.next();

            if (socketChannel != null) {
                socketChannel.write(byteBuffer);
                byteBuffer.rewind();
            }
        }
        byteBuffer.clear();
    }
	
	private void broadcast(String data) throws IOException {
		ByteBuffer buffer = ByteBuffer.allocateDirect(BUFFSIZE);
//        byteBuffer.flip();
//        byteBuffer.clear();
//        byteBuffer.put(message.getBytes());
//        byteBuffer.flip();
		buffer.clear();
		buffer.put(data.getBytes());
		buffer.flip();
		
        Iterator iterator = channels.iterator();
        while (iterator.hasNext()) {
            SocketChannel socketChannel = (SocketChannel) iterator.next();

            if (socketChannel != null) {
                socketChannel.write(buffer);
                buffer.rewind();
            }
        }
        buffer.clear();
    }
	
}
