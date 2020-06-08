package network.server2.vo;

import java.net.Socket;

public interface Client extends Runnable {
	abstract void close();
	abstract void send(String data);
	abstract void setSocket(Socket socket);
	abstract String getId();
}
