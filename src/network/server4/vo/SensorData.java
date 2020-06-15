package network.server4.vo;

import java.sql.Date;

import org.apache.ibatis.type.Alias;

@Alias("SensorData")
public class SensorData {
	private String 	dataNo;
	private String 	sensorNo;
	private Date 	time;
	private String 	states;
	private String 	stateDetail;
	
	
	public SensorData() {}
	public SensorData(String sensorNo, String states, String stateDetail) {
		this.dataNo = "-";
		this.sensorNo = sensorNo;
		this.time = new Date(System.currentTimeMillis());
		this.states = states;
		if(stateDetail==null) stateDetail = "-";
		this.stateDetail = stateDetail;
	}
	
	
	public String getDataNo() {
		return dataNo;
	}
	
	public void setDataNo(String dataNo) {
		this.dataNo = dataNo;
	}
	
	public String getSensorNo() {
		return sensorNo;
	}
	
	public void setSensorNo(String sensorNo) {
		this.sensorNo = sensorNo;
	}
	
	public Date getTime() {
		return time;
	}
	
	public void setTime(Date time) {
		this.time = time;
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
		return "SensorData [dataNo=" + dataNo + ", sensorNo=" + sensorNo + ", time=" + time + ", states=" + states
				+ ", stateDetail=" + stateDetail + "]";
	}
	
}
