package network.server.test;

import network.server.dao.DeviceDAO;

public class DeviceSelectOne {

	public static void main(String[] args) {

		DeviceDAO ddao = new DeviceDAO();
		
		String input = "DEVICE014";
		int result = ddao.selectDevice(input);
		
		System.out.println("result : " + result);
		
		
	}

}
