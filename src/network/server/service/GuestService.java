package network.server.service;

import java.util.List;

import network.server.dao.AlarmDAO;
import network.server.vo.Alarm;
import network.server.vo.AlarmData;

public class GuestService {

	public Alarm getAlarmInfo(String userNo) {
		
		AlarmDAO adao = new AlarmDAO();
		
		return adao.selectAlarm(userNo);
		
	}
		
	public int updateAlarmInfo(Alarm alarm) {
		
		AlarmDAO adao = new AlarmDAO();
		
		return adao.updateAlarm(alarm);
	}
	
	public List<AlarmData> getAlarmJobs(String userNo) {
		
		AlarmDAO adao = new AlarmDAO();
		
		return adao.selectAlarmData(userNo);
		
	}
	
	public int updateAlarmJobs(List<AlarmData> input) {
		
		AlarmDAO adao = new AlarmDAO();
		
		return adao.updateAlarmDataAll(input);
		
	}
	
}
