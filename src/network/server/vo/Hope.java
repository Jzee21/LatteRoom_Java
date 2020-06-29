package network.server.vo;

import org.apache.ibatis.type.Alias;

//@Alias("Hope")
public class Hope {
	private String hopeNo;
	private String temp;
	private String light;
	private String bed;
	private String blind;
	
	
	public Hope() {}
	public Hope(String hopeNo, String temp, String light, String blind) {
		this.hopeNo = hopeNo;
		this.temp = temp;
		this.light = light;
		this.blind = blind;
	}
	
	
	
	public void nullCheck() {
		if(this.hopeNo == null) this.hopeNo = "-";
		if(this.temp == null) this.temp = "-";
		if(this.light == null) this.light = "-";
		if(this.bed == null) this.bed = "-";
		if(this.blind == null) this.blind = "-";
	}
	
	
	
	public String getHopeNo() {
		return hopeNo;
	}
	
	public void setHopeNo(String hopeNo) {
		this.hopeNo = hopeNo;
	}
	
	public String getTemp() {
		return temp;
	}
	
	public void setTemp(String temp) {
		this.temp = temp;
	}
	
	public String getLight() {
		return light;
	}
	
	public void setLight(String light) {
		this.light = light;
	}
	
	public String getBed() {
		return bed;
	}
	
	public void setBed(String bed) {
		this.bed = bed;
	}
	
	public String getBlind() {
		return blind;
	}
	
	public void setBlind(String blind) {
		this.blind = blind;
	}

	@Override
	public String toString() {
		return "Hope [hopeNo=" + hopeNo + ", temp=" + temp + ", light=" + light + ", bed=" + bed + ", blind=" + blind
				+ "]";
	}
	
}
