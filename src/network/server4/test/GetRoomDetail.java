package network.server4.test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import network.server4.service.RoomService;
import network.server4.vo.Room;
import network.server4.vo.RoomDetail;

public class GetRoomDetail {

	public static void main(String[] args) {

		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
		
		RoomService service = new RoomService();
		
		RoomDetail info = service.getRoomDetail("TESTER0001", new Room("R000123"));
		System.out.println(info.toString());
		
		String jsonData = gson.toJson(info);
		System.out.println(jsonData);
		
	}

}
