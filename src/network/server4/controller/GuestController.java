package network.server4.controller;

import java.util.List;

import network.server4.dao.ReservDAO;
import network.server4.vo.Message;
import network.server4.vo.Reservation;

public class GuestController {

	// =================================================
	// field
	private Dispatcher dispatcher;
	
	// =================================================
	// Singleton
	private GuestController() {
		dispatcher = Dispatcher.getInstance();
	}
	
	private static class InstanceHandler {
		public static final GuestController INSTANCE = new GuestController();
	}
	
	public static GuestController getInstance() {
		return InstanceHandler.INSTANCE;
	}
	
	
	// =================================================
	// methods
	public void requestReserv(Connection conn, Message data) {
		// Message {userNo, RESERVLIST, null, null}
		
//		List<Reservation> reservInfo;
	}
	
	public void requestRoomDetial(Connection conn, Message data) {
		// Message {userNo, ROOMDETAIL, null, Room{roomNo}}
		
	}
	
	public void requestAlarmInfo(Connection conn, Message data) {
		// Message {userNo, ALARM, GET, null}
		
	}
	
	public void requestAlarmSet(Connection conn, Message data) {
		// Message {userNo, ALARM, UPDATE, Alarm{alarmNo, hour, min, weeks, flag}}
		
	}
	
	public void requestAlarmJobInfo(Connection conn, Message data) {
		// Message {userNo, ALARMJOB, GET, null}
		
	}
	
	public void requestAlarmJobSet(Connection conn, Message data) {
		// Message {userNo, ALARMJOB, UPDATE, List[AlarmData{dataNo, alarmNo, type, states, stateDetial}]}
		
	}
	
	
}
