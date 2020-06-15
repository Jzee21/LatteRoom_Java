package network.server4.test;

import org.apache.ibatis.session.SqlSession;

import network.server4.dao.AlarmDAO;
import network.server4.mybatis.MyBatisFactory;
import network.server4.vo.*;

public class AlarmSelectOne {

	public static void main(String[] args) {
		
		// 1. DB 접속
//		SqlSession sqlSession = MyBatisFactory.getSqlSession();
		
		// 2. 조회할 데이터
//		Guest input = new Guest();
//		input.setLoginID("latte1");
//		System.out.println("input : " + input.toString());
		String input = "GUEST0002";
		
		AlarmDAO adao = new AlarmDAO();
		
		// 3. 데이터 조회
//		Alarm output = sqlSession.selectOne("AlarmMapper.selectItem", input);
//		Guest output = sqlSession.selectOne("GuestMapper.selectItemByString", input);
		Alarm output = adao.selectAlarm(input);
		
		// 4. 결과 확인
		if(output == null) {
			System.out.println("조회 결과 없음");
		} else {
			System.out.println("결과 : " + output.toString());
		}
		
//		sqlSession.close();

	}

}
