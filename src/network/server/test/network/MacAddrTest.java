package network.server.test.network;

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
		
		System.out.println("" + Integer.parseInt("24F5AA", 16));	// 2422186
		System.out.println("" + Integer.parseInt("FFFFFF", 16));	// 16777215
		System.out.println("" + Integer.parseInt("FFFFFFF", 16));	// 268435455
		System.out.println("" + Integer.parseInt("1FFFFFFF", 16));	// 536870911
		System.out.println("" + Integer.parseInt("2FFFFFFF", 16));	// 805306367
		System.out.println("" + Integer.parseInt("3FFFFFFF", 16));	// 1073741823
		System.out.println("" + Integer.parseInt("4FFFFFFF", 16));	// 1342177279
		System.out.println("" + Integer.parseInt("5FFFFFFF", 16));	// 1610612735
		System.out.println("" + Integer.parseInt("6FFFFFFF", 16));	// 1879048191
		System.out.println("" + Integer.parseInt("7FFFFFFF", 16));	// 2147483647
		System.out.println("" + Integer.parseInt("8FFFFFFF", 16));	// Error
		System.out.println("" + Integer.parseInt("FFFFFFFF", 16));
	}

}
