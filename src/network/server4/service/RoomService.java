package network.server4.service;

import network.server4.dao.AlarmDAO;
import network.server4.dao.HopeDAO;
import network.server4.dao.SensorDAO;
import network.server4.vo.Room;
import network.server4.vo.RoomDetail;

public class RoomService {

	public RoomDetail getRoomDetail(String userNo, Room data) {
		
		SensorDAO 	sdao = new SensorDAO();
		HopeDAO 	hdao = new HopeDAO();
		AlarmDAO 	adao = new AlarmDAO();
		
		RoomDetail roomInfo = new RoomDetail();
		roomInfo.setRoomNo(data.getRoomNo());
		roomInfo.setSensorList(sdao.selectSensorAll(data.getRoomNo()));
		roomInfo.setHope(hdao.selectHope(userNo));
		roomInfo.setAlarm(adao.selectAlarm(userNo));
		
		return roomInfo;
		
	}
	
}
