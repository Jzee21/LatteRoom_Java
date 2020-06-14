package network.server4.dao;

import org.apache.ibatis.session.SqlSession;

import network.server4.mybatis.MyBatisFactory;
import network.server4.vo.*;

public class GuestDAO {
	
	public Guest checkLogin(Guest input) {
		System.out.println("checkLogin");
		Guest result = null;
		try (SqlSession sqlSession = MyBatisFactory.getSqlSession()) {
			result = sqlSession.selectOne("GuestMapper.isExist", input);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		System.out.println("checkLogin - " + result.toString());
		return result;
	}

}
