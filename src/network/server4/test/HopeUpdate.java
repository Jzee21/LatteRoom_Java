package network.server4.test;

import org.apache.ibatis.session.SqlSession;

import network.server4.mybatis.MyBatisFactory;
import network.server4.vo.Hope;

public class HopeUpdate {

	public static void main(String[] args) {
		
		int result;
		try(SqlSession sqlSession = MyBatisFactory.getSqlSession()) {
//			String[] input = {"GUEST0002", "27"};
//			String[] input = {"27", "GUEST0002"};
//			String input1 = "GUEST0002";
//			String input2 = "27";
			Hope input = new Hope("GUEST0002", "27", null, null);
			result = sqlSession.update("HopeMapper.updateTemp", input);
		} catch (Exception e) {
			e.printStackTrace();
			result = 0;
		}
		
		System.out.println("실행결과 : " + result);

	}

}
