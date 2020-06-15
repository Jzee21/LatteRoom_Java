package network.server4.test;

import network.server4.dao.DeviceDAO;

public class DeviceSelectOne {

	public static void main(String[] args) {

		DeviceDAO ddao = new DeviceDAO();
		
		String input = "DEVICE014";
		int result = ddao.getDevice(input);
		
		System.out.println("result : " + result);
		
		
	}

}
