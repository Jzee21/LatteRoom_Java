package network.server3.dao;

import org.apache.ibatis.session.SqlSession;

import network.server3.mybatis.MyBatisFactory;
import network.server3.vo.Guest;

public class GuestDAO {
	
	public Guest checkLogin(Guest input) {
		System.out.println("checkLogin");
		Guest result = null;
		try (SqlSession sqlSession = MyBatisFactory.getSqlSession()) {
			result = sqlSession.selectOne("GuestMapper.isExist", input);
		}
		System.out.println("checkLogin - " + result.toString());
		return result;
	}
	

}
