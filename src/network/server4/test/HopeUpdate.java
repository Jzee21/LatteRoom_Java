package network.server4.test;

import org.apache.ibatis.session.SqlSession;

import network.server4.dao.HopeDAO;
import network.server4.mybatis.MyBatisFactory;
import network.server4.vo.Hope;

public class HopeUpdate {

	public static void main(String[] args) {
		
//		int result;
//		try(SqlSession sqlSession = MyBatisFactory.getSqlSession()) {
//			Hope input = new Hope("GUEST0002", "27", null, null);
//			result = sqlSession.update("HopeMapper.updateTemp", input);
//
//			Hope input2 = new Hope("GUEST0002", null, "50", null);
//			result = sqlSession.update("HopeMapper.updateLight", input2);
//			
//			Hope input3 = new Hope("GUEST0002", null, null, "CLOSE");
//			result = sqlSession.update("HopeMapper.updateBlind", input3);
//		} catch (Exception e) {
//			e.printStackTrace();
//			result = 0;
//		}
		HopeDAO hdao = new HopeDAO();
//		int result = hdao.updateTemp(new Hope("GUEST0002", "26", null, null));
//		int result = hdao.updateLight(new Hope("GUEST0002", null, "50", null));
//		int result = hdao.updateBlind(new Hope("GUEST0002", null, null, "OPEN"));
		
//		int result = hdao.updateTemp("GUEST0002", "27");
//		int result = hdao.updateLight("GUEST0002", "60");
		int result = hdao.updateBlind("GUEST0002", "CLOSE");
		
		System.out.println("실행결과 : " + result);

	}

}
