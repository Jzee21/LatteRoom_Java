package network.server.test;

import network.server.dao.DeviceDAO;
import network.server.dao.SensorDAO;
import network.server.vo.NoKey;

public class FindNo {

	public static void main(String[] args) {
		
		SensorDAO sdao = new SensorDAO();
		
		String sensorNo = sdao.selectSensorNo("ROOM0002", "LIGHT");
		System.out.println(sensorNo);
		
		DeviceDAO ddao = new DeviceDAO();
		
		String deviceNo = ddao.selectDeviceNo("ROOM0002", "LIGHT");
		System.out.println(deviceNo);

		
		
	}

}
