package network.server4.service;

import java.util.Calendar;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

import network.server4.dao.ReservDAO;
import network.server4.mybatis.MyBatisFactory;
import network.server4.vo.Reservation;

public class ReservService {
	
	public List<Reservation> getReservInfo(String userNo) {
		
		ReservDAO rdao = new ReservDAO();
		
		List<Reservation> reservInfo = rdao.selectReserv(userNo);

		return reservInfo;
		
	}
	
	public String getGuestNoByDevice(String deviceNo) {
		
		ReservDAO rdao = new ReservDAO();
		
		List<String> list = rdao.selectGuestByDevice(deviceNo);
		
		if(list.size()==1) {
			return list.get(0);
		} else {
			if(list.size()==2) {
				Calendar c = Calendar.getInstance();
				if(c.get(Calendar.AM_PM)==0)
					return list.get(0);		// AM
				else
					return list.get(1);		// PM
			} else
				return null;
		}
	}
	
}
