package network.server.service;

import network.server.dao.AlarmDAO;
import network.server.dao.HopeDAO;
import network.server.dao.SensorDAO;
import network.server.vo.Room;
import network.server.vo.RoomDetail;

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
