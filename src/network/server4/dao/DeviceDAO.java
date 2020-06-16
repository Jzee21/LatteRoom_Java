package network.server4.dao;

import org.apache.ibatis.session.SqlSession;

import network.server4.mybatis.MyBatisFactory;

public class DeviceDAO {

	public int selectDevice(String deviceNo) {
		try (SqlSession sqlSession = MyBatisFactory.getSqlSession()) {
			return sqlSession.selectOne("DeviceMapper.isExist", deviceNo);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
}
