package network.server3.test;

import org.apache.ibatis.session.SqlSession;

import network.server3.mybatis.MyBatisFactory;
import network.server3.vo.Guest;

public class GuestSelectOne {

	public static void main(String[] args) {
		
		// 1. DB 접속
		SqlSession sqlSession = MyBatisFactory.getSqlSession();
		
		// 2. 조회할 데이터
//		Guest input = new Guest();
//		input.setLoginID("latte1");
//		System.out.println("input : " + input.toString());
		String input = "latte1";
		
		// 3. 데이터 조회
//		Guest output = sqlSession.selectOne("GuestMapper.selectItem", input);
		Guest output = sqlSession.selectOne("GuestMapper.selectItemByString", input);
		
		// 4. 결과 확인
		if(output == null) {
			System.out.println("조회 결과 없음");
		} else {
			System.out.println("결과 : " + output.toString());
		}
		
		sqlSession.close();

	}

}
