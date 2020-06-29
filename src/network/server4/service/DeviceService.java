package network.server4.service;

import network.server4.dao.DeviceDAO;
import network.server4.dao.HopeDAO;
import network.server4.dao.SensorDAO;
import network.server4.vo.Message;
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
	
	public SensorData insertControlData(Message message, SensorData data) {
		SensorData result = null;
		
		SensorDAO sdao = new SensorDAO();
		System.out.println("Service(CD) - " + data.toString());
		
		int row = sdao.insertControlData(data);
		System.out.println("Service(CD)(row) - " + row);
		
		if(row == 1) {
			// Hope update
			// Message {roomNo, CONTROL, TYPE, SensorData{time, states, stateDetail}}
			
			HopeDAO hdao = new HopeDAO();
			int hopeResult = 0;
			
			switch(message.getCode2()) {
			case "TEMP" :
				hopeResult = hdao.updateTemp(message.getClientNo(), data.getStates());
				break;
				
			case "LIGHT" :
				hopeResult = hdao.updateLight(message.getClientNo(), data.getStateDetail());
				break;
				
			case "BLIND" :
				hopeResult = hdao.updateBlind(message.getClientNo(), data.getStates());
				break;
			
			case "BED" :
				hopeResult = 1;
				break;
				
			default :
				break;
			}
			
			if(hopeResult == 1) {
				System.out.println("[DeviceService] Success : Update Hope");
			}
			
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
