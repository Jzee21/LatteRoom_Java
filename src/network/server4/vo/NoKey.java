package network.server4.vo;

public class NoKey {
	private String roomNo;
	private String type;
	
	
	
	public NoKey(String roomNo, String type) {
		this.roomNo = roomNo;
		this.type = type;
	}
	
	
	
	public String getRoomNo() {
		return roomNo;
	}
	public void setRoomNo(String roomNo) {
		this.roomNo = roomNo;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}
