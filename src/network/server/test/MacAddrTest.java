package network.server.test;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import sun.security.action.GetLongAction;

public class MacAddrTest {
	
	public String getLocalMacAddress() {
		String result = "";
		InetAddress ip;

		try {
			ip = InetAddress.getLocalHost();
		   
			NetworkInterface network = NetworkInterface.getByInetAddress(ip);
			byte[] mac = network.getHardwareAddress();
		   
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < mac.length; i++) {
//				sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
				sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "" : ""));
			}
				result = sb.toString();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (SocketException e){
			e.printStackTrace();
		}
		    
		return result;
	}
	
	public int getIntLocalMacAdderss() {
		int result = 0;
		InetAddress ip;
		
		try {
			ip = InetAddress.getLocalHost();
			
			NetworkInterface network = NetworkInterface.getByInetAddress(ip);
			byte[] mac = network.getHardwareAddress();
			System.out.println("byte mac size : " + mac.length);
			
			result = byteArrayToInt(mac);
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (SocketException e){
			e.printStackTrace();
		}
		
		return result;
	}
	
	private static int byteArrayToInt(byte[] bytes) {
		//						Mac Addr : 24-F5-AA-EC-52-62
//		final int size = Integer.SIZE / 8;		// aaec5262
		final int size = 6;						// 24f5aaec
		System.out.println("int size : " + size);
		ByteBuffer buff = ByteBuffer.allocate(size);
		final byte[] newBytes = new byte[size];
		for (int i = 0; i < size; i++) {
			if (i + bytes.length < size) {
				newBytes[i] = (byte) 0x00;
			} else {
				newBytes[i] = bytes[i + bytes.length - size];
			}
		}
		buff = ByteBuffer.wrap(newBytes);
		buff.order(ByteOrder.BIG_ENDIAN); // Endian에 맞게 세팅
		return buff.getInt();
	}

	public static void main(String[] args) {
		MacAddrTest test = new MacAddrTest();
		
		System.out.println("Mac Addr : " + test.getLocalMacAddress());
		System.out.println("Mac Addr : " + Integer.toHexString(test.getIntLocalMacAdderss()));
		System.out.println("Mac Addr : " + test.getIntLocalMacAdderss());
	}

}
