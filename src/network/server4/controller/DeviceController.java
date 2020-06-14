package network.server4.controller;

public class DeviceController {

	// =================================================
	// Singleton
	private DeviceController() {
	}
	
	private static class InstanceHandler {
		public static final DeviceController INSTANCE = new DeviceController();
	}
	
	public static DeviceController getInstance() {
		return InstanceHandler.INSTANCE;
	}
	
	
	// =================================================
	// methods
	
	
	
}
