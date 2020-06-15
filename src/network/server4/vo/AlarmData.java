package network.server4.vo;

import org.apache.ibatis.type.Alias;

@Alias("AlarmData")
public class AlarmData {
	private String dataNo;
	private String alarmNo;
	private String type;
	private String states;
	private String stateDetail;
	
	
	public AlarmData() {}
	public AlarmData(String userNo, String type, String states, String stateDetail) {
		this.alarmNo = userNo;
		this.type = type;
		this.states = states;
		if(stateDetail==null) stateDetail = "-";
		this.stateDetail = stateDetail;
	}
	
	
	
	public void nullCheck() {
		if(this.dataNo == null) this.dataNo = "-";
		if(this.alarmNo == null) this.alarmNo = "-";
		if(this.type == null) this.type = "-";
		if(this.states == null) this.states = "-";
		if(this.stateDetail == null) this.stateDetail = "-";
	}
	
	
	
	public String getDataNo() {
		return dataNo;
	}
	
	public void setDataNo(String dataNo) {
		this.dataNo = dataNo;
	}
	
	public String getAlarmNo() {
		return alarmNo;
	}
	
	public void setAlarmNo(String alarmNo) {
		this.alarmNo = alarmNo;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
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
		return "AlarmData [dataNo=" + dataNo + ", userNo=" + alarmNo + ", type=" + type + ", states=" + states
				+ ", stateDetail=" + stateDetail + "]";
	}
	
}
