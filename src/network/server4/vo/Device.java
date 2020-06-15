package network.server4.vo;

import java.util.Map;

public class Device {
	private String deviceNo;
	private String roomNo;
	private Map<String, String> sensorList;
	//			type,	sensorNo
	
	
	
	public void nullCheck() {
		if(this.deviceNo == null) this.deviceNo = "-";
		if(this.roomNo == null) this.roomNo = "-";
	}
	
	
	
	public String getDeviceNo() {
		return deviceNo;
	}
	
	public void setDeviceNo(String deviceNo) {
		this.deviceNo = deviceNo;
	}
	
	public String getRoomNo() {
		return roomNo;
	}
	
	public void setRoomNo(String roomNo) {
		this.roomNo = roomNo;
	}
	
	public Map<String, String> getSensorList() {
		return sensorList;
	}
	
	public void setSensorList(Map<String, String> sensorList) {
		this.sensorList = sensorList;
	}

	@Override
	public String toString() {
		return "Device [deviceNo=" + deviceNo + ", roomNo=" + roomNo + ", sensorList=" + sensorList.toString() + "]";
	}
	
}
