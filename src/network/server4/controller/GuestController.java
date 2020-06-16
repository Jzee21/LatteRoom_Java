package network.server4.controller;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import network.server4.service.ReservService;
import network.server4.service.RoomService;
import network.server4.vo.Message;
import network.server4.vo.Reservation;
import network.server4.vo.Room;
import network.server4.vo.RoomDetail;

public class GuestController {

	// =================================================
	// field
	private Dispatcher dispatcher;
	private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
	
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
		
		ReservService rService = new ReservService();
		
		List<Reservation> reservInfo = rService.getReservInfo(data.getClientNo());
		
		Message response = 
				new Message(data.getClientNo(), 
						data.getCode1(), 
						Integer.toString(reservInfo.size()), 
						gson.toJson(reservInfo));
		
		conn.send(response);
	}
	
	public void requestRoomDetial(Connection conn, Message data) {
		// Message {userNo, ROOMDETAIL, null, Room{roomNo}}
		RoomService rService = new RoomService();
		
		Room input = gson.fromJson(data.getJsonData(), Room.class);
		
		RoomDetail roomInfo = rService.getRoomDetail(data.getClientNo(), input);
		
		Message response = 
				new Message(data.getClientNo(), 
						data.getCode1(), 
						"SUCCESS", 
						gson.toJson(roomInfo));
		
		conn.send(response);
		
	}
	
	public void requestAlarmInfo(Connection conn, Message data) {
		// Message {userNo, ALARM, GET, null}
		
	}
	
	public void requestAlarmUpdate(Connection conn, Message data) {
		// Message {userNo, ALARM, UPDATE, Alarm{alarmNo, hour, min, weeks, flag}}
		
	}
	
	public void requestAlarmJobInfo(Connection conn, Message data) {
		// Message {userNo, ALARMJOB, GET, null}
		
	}
	
	public void requestAlarmJobUpdate(Connection conn, Message data) {
		// Message {userNo, ALARMJOB, UPDATE, List[AlarmData{dataNo, alarmNo, type, states, stateDetial}]}
		
	}
	
	
}
