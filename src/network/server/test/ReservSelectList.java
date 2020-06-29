package network.server.test;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import network.server.dao.ReservDAO;
import network.server.mybatis.MyBatisFactory;
import network.server.vo.Guest;
import network.server.vo.Reservation;

public class ReservSelectList {

	public static void main(String[] args) {
		
//		// 1. DB 접속
//		SqlSession sqlSession = MyBatisFactory.getSqlSession();
//		
//		// 2. 조회할 데이터
//		Guest input = new Guest();
//		input.setUserNo("GUEST0002");		
//		System.out.println("input : " + input.toString());
//		
//		// 3. 데이터 조회
//		List<Reservation> output = sqlSession.selectList("ReservMapper.selectItem", input);
//		
//		// 4. 결과 확인
//		if(output == null) {
//			System.out.println("조회 결과 없음");
//		} else {
//			System.out.println(output.toString());
//		}
//		
//		sqlSession.close();
		ReservDAO rdao = new ReservDAO();
		
//		Guest input = new Guest();
//		input.setUserNo("GUEST0002");
		String input = "GUEST0002";
		
		List<Reservation> output = rdao.selectReserv(input);

		if (output == null) {
			System.out.println("조회 결과 없음");
		} else {
			System.out.println(output.toString());
		}
	}

}
