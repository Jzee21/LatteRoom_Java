package network.server.dao;

import org.apache.ibatis.session.SqlSession;

import network.server.mybatis.MyBatisFactory;
import network.server.vo.Guest;

public class GuestDAO {
	
	public Guest selectGuest(Guest input) {
		// Registration Information Search
		try (SqlSession sqlSession = MyBatisFactory.getSqlSession()) {
			input.nullCheck();
			return sqlSession.selectOne("GuestMapper.isExist", input);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
