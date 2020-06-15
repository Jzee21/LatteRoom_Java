package network.server4.test;

import org.apache.ibatis.session.SqlSession;

import network.server4.dao.AlarmDAO;
import network.server4.mybatis.MyBatisFactory;
import network.server4.vo.*;

public class AlarmDataUpdateOne {

	public static void main(String[] args) {
		
		// 1. DB 접속
//		SqlSession sqlSession = MyBatisFactory.getSqlSession();
		
		// 2. 조회할 데이터
//		Guest input = new Guest();
//		input.setLoginID("latte1");
//		System.out.println("input : " + input.toString());
//		String input = "GUEST0002";
//		Alarm input = new Alarm("GUEST0002", "6", "20", "MON", 'N');
		AlarmData input = new AlarmData("GUEST0002", "LIGHT", "65", null);
		
		// 3. 데이터 조회
//		int output = sqlSession.update("AlarmDataMapper.updateItem", input);
//		Guest output = sqlSession.selectOne("GuestMapper.selectItemByString", input);
		
		AlarmDAO adao = new AlarmDAO();
		int output = adao.updateAlarmDataOne(input);
		
		// 4. 결과 확인
		if(output == 0) {
			System.out.println("조회 결과 없음");
		} else {
			System.out.println("결과 : " + output);
		}
		
//		sqlSession.close();

	}

}
