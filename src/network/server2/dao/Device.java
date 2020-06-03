package network.server2.dao;

import java.nio.channels.SocketChannel;

public class Device {
	
	SocketChannel channel;
	
	public Device(SocketChannel channel) {
		this.channel = channel;
	}
	
	public SocketChannel getChannel() {
		return this.channel;
	}

}
