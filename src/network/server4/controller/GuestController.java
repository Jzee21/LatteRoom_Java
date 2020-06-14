package network.server4.controller;

public class GuestController {

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
	
	
	
}
