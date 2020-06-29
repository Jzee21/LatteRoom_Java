package network.server3.test;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import network.server3.mybatis.MyBatisConnectionFactory;

public class DeviceSelect {

	public static void main(String[] args) {
		
		// 1. DB 접속
		SqlSession sqlSession = MyBatisConnectionFactory.getSqlSession();
		
		// 2. 조회할 데이터
//		Guest input = new Guest();
//		input.setUserNo("DEVICE011");		
//		System.out.println("input : " + input.toString());
		String input = "DEVICE011";
		
		// 3. 데이터 조회
		List<Map<String, String>> output = sqlSession.selectList("DeviceMapper.selectTypeMap", input);
//		Map<String, String> output = sqlSession.selectMap("DeviceMapper.selectTypeMap", input);
		// Error
		
		// 4. 결과 확인
		if(output == null) {
			System.out.println("조회 결과 없음");
		} else {
			System.out.println(output.toString());
		}
		
		sqlSession.close();

	}

}
