package network.server4.test;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import network.server4.dao.ReservDAO;
import network.server4.vo.Reservation;

public class ListToGson {

	public static void main(String[] args) {

		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
//		Gson gson = new Gson();
//		List<Reservation> list = null;
		ReservDAO rdao = new ReservDAO();
		System.out.println("ReservService - getReservInfo - DAO");
		
		List<Reservation> reservInfo = rdao.selectReserv("TESTER0001");
		
		String jsonData = gson.toJson(reservInfo);
		
		Reservation r = reservInfo.get(0);
		System.out.println(r.toString());
		
		
		System.out.println(jsonData);

	}

}
