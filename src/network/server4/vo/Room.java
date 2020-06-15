package network.server4.vo;

import org.apache.ibatis.type.Alias;

@Alias("Room")
public class Room {
	private String roomNo;
	private String roomName;
	private String roomSSID;
	private String imgUrl;
	
	
	
	public void nullCheck() {
		if(this.roomNo == null) this.roomNo = "-";
		if(this.roomName == null) this.roomName = "-";
		if(this.roomSSID == null) this.roomSSID = "-";
		if(this.imgUrl == null) this.imgUrl = "-";
	}
	
	
	
	public String getRoomNo() {
		return roomNo;
	}
	
	public void setRoomNo(String roomNo) {
		this.roomNo = roomNo;
	}
	
	public String getRoomName() {
		return roomName;
	}
	
	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}
	
	public String getRoomSSID() {
		return roomSSID;
	}
	
	public void setRoomSSID(String roomSSID) {
		this.roomSSID = roomSSID;
	}
	
	public String getImgUrl() {
		return imgUrl;
	}
	
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	@Override
	public String toString() {
		return "Room [roomNo=" + roomNo + ", roomName=" + roomName + ", roomSSID=" + roomSSID + ", imgUrl=" + imgUrl
				+ "]";
	}
	
}
