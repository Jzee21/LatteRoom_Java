package network.server.dao;

import java.util.Calendar;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

import network.server.mybatis.MyBatisFactory;
import network.server.vo.Reservation;

public class ReservDAO {
	
	public List<Reservation> selectReserv(String userNo) {
		try (SqlSession sqlSession = MyBatisFactory.getSqlSession()) {
			return sqlSession.selectList("ReservMapper.selectItem", userNo);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<String> selectGuestByDevice(String deviceNo) {
		try (SqlSession sqlSession = MyBatisFactory.getSqlSession()) {
			return sqlSession.selectList("ReservMapper.selectUserNo", deviceNo);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
