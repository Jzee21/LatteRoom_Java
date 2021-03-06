package network.server.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import network.server.mybatis.MyBatisFactory;
import network.server.vo.NoKey;
import network.server.vo.Sensor;
import network.server.vo.SensorData;

public class SensorDAO {
	
	public String selectSensorNo(String roomNo, String type) {
		try (SqlSession sqlSession = MyBatisFactory.getSqlSession()) {
			NoKey input = new NoKey(roomNo, type);
			return sqlSession.selectOne("SensorMapper.selectSensorNo", input);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Sensor selectSensorOne(String sensorNo) {
		try (SqlSession sqlSession = MyBatisFactory.getSqlSession()) {
			return sqlSession.selectOne("SensorMapper.selectByNo", sensorNo);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Sensor> selectSensorAll(String roomNo) {
		try (SqlSession sqlSession = MyBatisFactory.getSqlSession()) {
			return sqlSession.selectList("SensorMapper.selectByRoom", roomNo);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public int insertSensorData(SensorData data) {
		try (SqlSession sqlSession = MyBatisFactory.getSqlSession()) {
			data.nullCheck();
			return sqlSession.insert("SensorDataMapper.insertSD", data);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	public int insertControlData(SensorData data) {
		try (SqlSession sqlSession = MyBatisFactory.getSqlSession()) {
			data.nullCheck();
			return sqlSession.insert("SensorDataMapper.insertCD", data);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
}
