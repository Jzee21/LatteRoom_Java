package network.server4.dao;

import org.apache.ibatis.session.SqlSession;

import network.server4.mybatis.MyBatisFactory;
import network.server4.vo.Hope;

public class HopeDAO {
	
	public Hope selectHope(String userNo) {
		try (SqlSession sqlSession = MyBatisFactory.getSqlSession()) {
			return sqlSession.selectOne("HopeMapper.selectItem", userNo);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public int updateTemp(String userNo, String states) {
		try (SqlSession sqlSession = MyBatisFactory.getSqlSession()) {
			return sqlSession.update("HopeMapper.updateTemp", new Hope(userNo, states, null, null));
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	public int updateLight(String userNo, String states) {
		try (SqlSession sqlSession = MyBatisFactory.getSqlSession()) {
			return sqlSession.update("HopeMapper.updateLight", new Hope(userNo, null, states, null));
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	public int updateBlind(String userNo, String states) {
		try (SqlSession sqlSession = MyBatisFactory.getSqlSession()) {
			return sqlSession.update("HopeMapper.updateBlind", new Hope(userNo, null, null, states));
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

}
