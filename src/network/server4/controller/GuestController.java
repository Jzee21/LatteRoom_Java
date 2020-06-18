package network.server4.controller;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import network.server4.service.GuestService;
import network.server4.service.ReservService;
import network.server4.service.RoomService;
import network.server4.vo.Alarm;
import network.server4.vo.AlarmData;
import network.server4.vo.Message;
import network.server4.vo.Reservation;
import network.server4.vo.Room;
import network.server4.vo.RoomDetail;

public class GuestController {

	// =================================================
	// field
	private Dispatcher dispatcher;
	private Gson gson;
//	private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
	
	// =================================================
	public GuestController() {
		dispatcher = Dispatcher.getInstance();
		gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
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
		GuestService gService = new GuestService();
		
		Alarm result = gService.getAlarmInfo(data.getClientNo());
		
		data.setJsonData(gson.toJson(result));
		
		conn.send(data);
		
		System.out.println("[ALARM, GET] " + data.toString());
	}
	
	public void requestAlarmUpdate(Connection conn, Message data) {
		// Message {userNo, ALARM, UPDATE, Alarm{alarmNo, hour, min, weeks, flag}}
		GuestService gService = new GuestService();
		
		Alarm input = gson.fromJson(data.getJsonData(), Alarm.class);
		System.out.println("requestAlarmUpdate : " + input.toString());
		System.out.println(input.getWeeks());
//		String[] weeks = input.getWeeks();
//		String weeks = input.getWeeks();
//		weeks = weeks.replaceAll("[", "");
//		weeks = weeks.replaceAll("]", "");
//		weeks = weeks.replaceAll("\"", "");
//		input.setWeeks(weeks.toUpperCase());
//		System.out.println(input.getWeeks());
		
		int result = gService.updateAlarmInfo(input);
		if(result == 1) System.out.println(conn.getConnAddr() + " [Alarm] Updated");
		
		System.out.println("[ALARM, UPDATE] end");
//		System.out.println("[ALARM, UPDATE] " + data.toString());
	}
	
	public void requestAlarmJobInfo(Connection conn, Message data) {
		// Message {userNo, ALARMJOB, GET, null}
		GuestService gService = new GuestService();
		
		List<AlarmData> result = gService.getAlarmJobs(data.getClientNo());
		
		data.setJsonData(gson.toJson(result));
		
		conn.send(data);
		
	}
	
	public void requestAlarmJobUpdate(Connection conn, Message data) {
		// Message {userNo, ALARMJOB, UPDATE, List[AlarmData{dataNo, alarmNo, type, states, stateDetial}]}
		GuestService gService = new GuestService();
		
		AlarmData[] array = gson.fromJson(data.getJsonData(), AlarmData[].class);
		List<AlarmData> input = Arrays.asList(array);
		
		int result = gService.updateAlarmJobs(input);
		if(result == 1) System.out.println(conn.getConnAddr() + "[AlarmJobs] Updated");
		
	}
	
	
}
