package network.server4.dao;

import org.apache.ibatis.session.SqlSession;

import network.server4.mybatis.MyBatisFactory;

public class DeviceDAO {

	public int isExist(String input) {
		try (SqlSession sqlSession = MyBatisFactory.getSqlSession()) {
			return sqlSession.selectOne("DeviceMapper.isExist", input);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
}
