package network.server4.dao;

import org.apache.ibatis.session.SqlSession;

import network.server4.mybatis.MyBatisFactory;
import network.server4.vo.NoKey;

public class DeviceDAO {

	public int selectDevice(String deviceNo) {
		try (SqlSession sqlSession = MyBatisFactory.getSqlSession()) {
			return sqlSession.selectOne("DeviceMapper.isExist", deviceNo);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	public String selectDeviceNo(String roomNo, String type) {
		try (SqlSession sqlSession = MyBatisFactory.getSqlSession()) {
			NoKey input = new NoKey(roomNo, type);
			return sqlSession.selectOne("DeviceMapper.selectNoByRoom", input);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public String selectDeviceNo(String sensorNo) {
		try (SqlSession sqlSession = MyBatisFactory.getSqlSession()) {
			return sqlSession.selectOne("DeviceMapper.selectNoBySensor", sensorNo);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
