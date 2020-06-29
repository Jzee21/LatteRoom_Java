package network.server.vo;

import org.apache.ibatis.type.Alias;

//@Alias("Sensor")
public class Sensor {
	private String 	sensorNo;
	private String 	type;
	private String  deviceNo;
	private String 	states;
	private String 	stateDetail;
	
	
	
	public void nullCheck() {
		if(this.sensorNo == null) this.sensorNo = "-";
		if(this.type == null) this.type = "-";
		if(this.deviceNo == null) this.deviceNo = "-";
		if(this.states == null) this.states = "-";
		if(this.stateDetail == null) this.stateDetail = "-";
	}
	
	
	
	public String getSensorNo() {
		return sensorNo;
	}
	
	public void setSensorNo(String sensorNo) {
		this.sensorNo = sensorNo;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getDeviceNo() {
		return deviceNo;
	}
	
	public void setDeviceNo(String deviceNo) {
		this.deviceNo = deviceNo;
	}
	
	public String getStates() {
		return states;
	}
	
	public void setStates(String states) {
		this.states = states;
	}
	
	public String getStateDetail() {
		return stateDetail;
	}
	
	public void setStateDetail(String stateDetail) {
		this.stateDetail = stateDetail;
	}
	
	@Override
	public String toString() {
		return "Sensor [sensorNo=" + sensorNo + ", type=" + type + ", deviceNo=" + deviceNo + ", states=" + states
				+ ", stateDetail=" + stateDetail + "]";
	}
	
}
