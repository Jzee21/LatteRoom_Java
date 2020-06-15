package network.server4.test;

import org.apache.ibatis.session.SqlSession;

import network.server4.dao.SensorDAO;
import network.server4.mybatis.MyBatisFactory;
import network.server4.vo.SensorData;

public class SensorDataInsert {

	public static void main(String[] args) {
		
		// 1. DB 접속
//		SqlSession sqlSession = MyBatisFactory.getSqlSession();
		
		SensorDAO sdao = new SensorDAO();
		
		// 2. 조회할 데이터
//				Guest input = new Guest();
//				input.setLoginID("latte1");
//				System.out.println("input : " + input.toString());
//		int temp = 70;
		SensorData input = new SensorData("SN01101", "65", null);
		
		// 3. 데이터 조회
//		int output = sqlSession.insert("SensorDataMapper.insertSD", input);
//				Guest output = sqlSession.selectOne("GuestMapper.selectItemByString", input);
		int output = sdao.insertSensorData(input);
		
		// 4. 결과 확인
		if(output == 0) {
			System.out.println("조회 결과 없음");
		} else {
			System.out.println("결과 : " + output);
		}
		
//		sqlSession.close();

	}

}
