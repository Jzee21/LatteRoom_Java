package network.server3.controller;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

public class ServerController {

	// =================================================
	// field
	private Selector selector = null;
	private Vector<SocketChannel> channels = new Vector<SocketChannel>();
	
	
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
	                }
	                if (selectionKey.isReadable()) {
	                    read(selectionKey);
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
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024);
        
        try {
			int count = channel.read(byteBuffer);	// Reading data from the socket
			if(count < 0) {							// Socket closed from client
				System.out.println(channel.getRemoteAddress().toString() + "] closed");
				channel.close();
				key.cancel();
				return;
			}
			
			// test
			broadcast(byteBuffer);
			
		} catch (IOException e) {
//			e.printStackTrace();
			try {
				System.out.println(channel.getRemoteAddress().toString() + "] closed");
				channel.close();
				key.cancel();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
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
	
}
