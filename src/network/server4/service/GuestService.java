package network.server4.service;

import java.util.List;

import network.server4.dao.AlarmDAO;
import network.server4.vo.Alarm;
import network.server4.vo.AlarmData;

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
