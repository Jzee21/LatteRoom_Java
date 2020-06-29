package network.server.test.time;

import java.sql.Date;

public class TimeTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Date now = new Date(System.currentTimeMillis());
		System.out.println(now.toString());
		System.out.println(now.toLocaleString());
		
		int data = 100;
		float data2 = 100.5f;
		
		System.out.println(data + 0.6);
		
		if(data + 0.6 > data2) {
			System.out.println("big");
		}

	}

}
