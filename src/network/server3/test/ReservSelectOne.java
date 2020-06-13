package network.server3.test;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import network.server3.mybatis.MyBatisFactory;
import network.server3.vo.Guest;
import network.server3.vo.Reservation;

public class ReservSelectOne {

	public static void main(String[] args) {
		
		// 1. DB 접속
		SqlSession sqlSession = MyBatisFactory.getSqlSession();
		
		// 2. 조회할 데이터
		Guest input = new Guest();
		input.setUserNo("GU0000000002");		
		System.out.println("input : " + input.toString());
		
		// 3. 데이터 조회
		List<Reservation> output = sqlSession.selectList("ReservMapper.selectItem", input);
		
		// 4. 결과 확인
		if(output == null) {
			System.out.println("조회 결과 없음");
		} else {
			System.out.println(output.toString());
		}
		
		sqlSession.close();

	}

}
