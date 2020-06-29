package network.server.test;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import network.server.dao.AlarmDAO;
import network.server.mybatis.MyBatisFactory;
import network.server.vo.*;

public class AlarmDataSelect {

	public static void main(String[] args) {
		
		// 1. DB 접속
//		SqlSession sqlSession = MyBatisFactory.getSqlSession();
		
		// 2. 조회할 데이터
//		Guest input = new Guest();
//		input.setLoginID("latte1");
//		System.out.println("input : " + input.toString());
		String input = "GUEST0002";
		
		// 3. 데이터 조회
//		List<AlarmData> output = sqlSession.selectList("AlarmDataMapper.selectItem", input);
//		Guest output = sqlSession.selectOne("GuestMapper.selectItemByString", input);
		
		AlarmDAO adao = new AlarmDAO();
		List<AlarmData> output = adao.selectAlarmData(input);
				
		// 4. 결과 확인
		if(output == null) {
			System.out.println("조회 결과 없음");
		} else {
			System.out.println("결과 : " + output.toString());
		}
		
//		sqlSession.close();

	}

}
