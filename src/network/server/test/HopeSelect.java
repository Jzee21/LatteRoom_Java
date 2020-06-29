package network.server.test;

import org.apache.ibatis.session.SqlSession;

import network.server.mybatis.MyBatisFactory;
import network.server.vo.Hope;

public class HopeSelect {

	public static void main(String[] args) {
		
//		int result;
		Hope result;
		try(SqlSession sqlSession = MyBatisFactory.getSqlSession()) {
//			String[] input = {"GUEST0002", "27"};
//			String[] input = {"27", "GUEST0002"};
			String input = "GUEST0002";
//			String input2 = "27";
//			Hope input = new Hope("GUEST0002", "27", null, null);
			result = sqlSession.selectOne("HopeMapper.selectItem", input);
		} catch (Exception e) {
			e.printStackTrace();
			result = null;
		}
		
		System.out.println("실행결과 : " + result.toString());

	}

}
