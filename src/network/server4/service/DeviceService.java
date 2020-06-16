package network.server4.service;

import network.server4.dao.DeviceDAO;
import network.server4.dao.SensorDAO;
import network.server4.vo.Sensor;
import network.server4.vo.SensorData;

public class DeviceService {

	public Sensor insertSensorData(SensorData data) {
		Sensor result = null;
		
		SensorDAO sdao = new SensorDAO();
		data.nullCheck();
		
		int row = sdao.insertSensorData(data);
		
		if(row == 1) {
			result = sdao.selectSensorOne(data.getSensorNo());
		}
		
		return result;
	}
	
	public SensorData insertControlData(SensorData data) {
		SensorData result = null;
		
		SensorDAO sdao = new SensorDAO();
		data.nullCheck();
		
		int row = sdao.insertControlData(data);
		
		if(row == 1) {
			return data;
		} else
			return null;
		
	}
	
	public String getDeviceNoBySensor(String sensorNo) {
		
		DeviceDAO ddao = new DeviceDAO();
		
		String deviceNo = ddao.selectDeviceNo(sensorNo);
		
		return deviceNo;
		
	}
	
}
