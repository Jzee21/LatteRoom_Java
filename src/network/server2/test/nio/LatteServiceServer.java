package network.server2.test.nio;

public class LatteServiceServer {
	
	// =========================================================
	// main
	public static void main(String[] args) {
		
		ServerService service = ServerService.getInstance();
		
		try {
			System.out.println("엔터를 누르면 서버거 종료됩니다.");
//			service.initServer();
			service.startServer();
			System.in.read();
			service.stopServer();
			System.out.println("서버가 종료되었습니다.");
		} catch (Exception e) {
			System.out.println("Error] Main - " + e);
		}
	}

}
