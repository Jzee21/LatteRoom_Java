package network.server4.dao;

import org.apache.ibatis.session.SqlSession;

import network.server4.mybatis.MyBatisFactory;
import network.server4.vo.*;

public class GuestDAO {
	
	public Guest checkLogin(Guest input) {
		// Registration Information Search
//		System.out.println("checkLogin");
		try (SqlSession sqlSession = MyBatisFactory.getSqlSession()) {
			return sqlSession.selectOne("GuestMapper.isExist", input);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
