package network.server4.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import network.server4.mybatis.MyBatisFactory;
import network.server4.vo.Guest;
import network.server4.vo.Reservation;

public class ReservDAO {
	
	public List<Reservation> getReserv(Guest guest) {
		try (SqlSession sqlSession = MyBatisFactory.getSqlSession()) {
			return sqlSession.selectList("ReservMapper.selectItem", guest);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
