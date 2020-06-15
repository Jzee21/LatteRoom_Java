package network.server4.vo;

import org.apache.ibatis.type.Alias;

@Alias("Alarm")
public class Alarm {
	private String 	alarmNo;
	private String 	hour;
	private String 	min;
	private String 	weeks;
	private String flag;
	
	
	public Alarm() {}
	public Alarm(String alarmNo, String hour, String min, String weeks, String flag) {
		this.alarmNo = alarmNo;
		this.hour = hour;
		this.min = min;
		this.weeks = weeks;
		this.flag = flag;
	}
	
	
	
	public void nullCheck() {
		if(this.alarmNo == null) this.alarmNo = "-";
		if(this.hour == null) this.hour = "-";
		if(this.min == null) this.min = "-";
		if(this.weeks == null) this.weeks = "-";
		if(this.flag == null) this.flag = "-";
	}
	
	
	
	public String getAlarmNo() {
		return alarmNo;
	}
	
	public void setAlarmNo(String alarmNo) {
		this.alarmNo = alarmNo;
	}
	
	public String getHour() {
		return hour;
	}
	
	public void setHour(String hour) {
		this.hour = hour;
	}
	
	public String getMin() {
		return min;
	}
	
	public void setMin(String min) {
		this.min = min;
	}
	
	public String getWeeks() {
		return weeks;
	}
	
	public void setWeeks(String weeks) {
		this.weeks = weeks;
	}
	
	public String isFlag() {
		return flag;
	}
	
	public void setFlag(String flag) {
		this.flag = flag;
	}

	@Override
	public String toString() {
		return "Alarm [alarmNo=" + alarmNo + ", hour=" + hour + ", min=" + min + ", weeks=" + weeks + ", flag=" + flag
				+ "]";
	}
	
}
