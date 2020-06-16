package network.server4.dao;

import org.apache.ibatis.session.SqlSession;

import network.server4.mybatis.MyBatisFactory;
import network.server4.vo.Guest;

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
