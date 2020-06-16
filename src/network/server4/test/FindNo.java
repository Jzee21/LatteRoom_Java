package network.server4.test;

import network.server4.dao.DeviceDAO;
import network.server4.dao.SensorDAO;
import network.server4.vo.NoKey;

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
