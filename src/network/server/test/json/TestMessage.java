package network.server.test.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import network.server.vo.Message;
import network.server.vo.Sensor;
import network.server.vo.SensorData;

public class TestMessage {

	public static void main(String[] args) {
		
		Gson gson = new Gson();
		
//		SensorData currData = new SensorData("unknown", "30");
//		
//		Message data = new Message(currData);
//		
//		String jsonData = gson.toJson(data);
//		
//		System.out.println(jsonData);
//		
//		Message fromJson = gson.fromJson(jsonData, Message.class);
//		System.out.println(fromJson.toString());
//		
//		SensorData msgJsonData = gson.fromJson(fromJson.getJsonData(), SensorData.class);
//		System.out.println(msgJsonData.toString());
		
		Sensor s1 = new Sensor("A");
		Sensor s2 = new Sensor("B");
		Sensor s3 = new Sensor("C");
		
		Map<String, Sensor> list = new HashMap<String, Sensor>();
		list.put("A", s1);
		list.put("B", s2);
		list.put("C", s3);
		
		String jsonData = gson.toJson(list);
		System.out.println(jsonData);
		
		Map<String, Sensor> result = gson.fromJson(jsonData, Map.class);
		System.out.println(result.toString());
		
		// error
//		Map<String, Sensor> map = gson.fromJson(jsonData, HashMap.class);
//		System.out.println(map.toString());
		
//		String test = gson.toJson(s1);
//		System.out.println(test);
//		
//		Sensor sss = gson.fromJson(test, Sensor.class);
//		System.out.println(sss.toString());
//		System.out.println(sss.getDeviceID());
//		System.out.println(sss.getSensorID());
//		System.out.println(sss.getSensorType());
		
	}

}
