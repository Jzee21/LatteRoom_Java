package network.server4.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import network.server4.mybatis.MyBatisFactory;
import network.server4.vo.Reservation;

public class ReservDAO {
	
	public List<Reservation> selectReserv(String userNo) {
		try (SqlSession sqlSession = MyBatisFactory.getSqlSession()) {
			return sqlSession.selectList("ReservMapper.selectItem", userNo);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
