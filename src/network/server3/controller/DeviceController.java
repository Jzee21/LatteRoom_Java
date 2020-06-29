package network.server3.controller;

public class DeviceController implements Controller {

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
	@Override
	public void dataHandler(String jsonData) {
		// TODO Auto-generated method stub

	}

}
