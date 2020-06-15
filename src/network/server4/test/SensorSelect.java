package network.server4.test;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import network.server4.mybatis.MyBatisFactory;
import network.server4.vo.*;

public class SensorSelect {

	public static void main(String[] args) {
		
		// 1. DB 접속
		SqlSession sqlSession = MyBatisFactory.getSqlSession();
		
		// 2. 조회할 데이터
//		Guest input = new Guest();
//		input.setLoginID("latte1");
//		System.out.println("input : " + input.toString());
		String input = "ROOM0002";
		
		// 3. 데이터 조회
		List<Sensor> output = sqlSession.selectList("SensorMapper.selectByRoom", input);
//		Guest output = sqlSession.selectOne("GuestMapper.selectItemByString", input);
		
		// 4. 결과 확인
		if(output == null) {
			System.out.println("조회 결과 없음");
		} else {
			System.out.println("결과 : " + output.toString());
		}
		
		System.out.println("===");
		
		String input2 = "SN02101";
		Sensor output2 = sqlSession.selectOne("SensorMapper.selectByNo", input2);
		
		if(output2 == null) {
			System.out.println("조회 결과 없음");
		} else {
			System.out.println("결과 : " + output2.toString());
		}
		
		sqlSession.close();

	}

}
