package network.server3.controller;

import network.server3.vo.Message;

public class GuestController implements Controller {

	// =================================================
	// Singleton
	private GuestController() {
		
	}
	
	private static class InstanceHandler {
		public static final GuestController INSTANCE = new GuestController();
	}
	
	public static GuestController getInstance() {
		return InstanceHandler.INSTANCE;
	}
	
	
	// =================================================
	// methods
	@Override
	public void dataHandler(String jsonData) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void dataHandler(Message data) {
		// TODO Auto-generated method stub
		
	}

}
