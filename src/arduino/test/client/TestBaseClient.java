package arduino.test.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import arduino.device.vo.Sensor;
import arduino.test.dao.*;
import arduino.test.vo.Message;
import arduino.test.vo.SensorData;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class TestBaseClient extends Application implements TestClient {

	private static final String DEVICE_ID = "LATTE01";
	private static final String DEVICE_TYPE = "DEVICE";

	private BorderPane root;
	private TextArea textarea;
	
	private TestSharedObject sharedObject;
	private ServerListener toServer;
	private SerialListener toSerial;
	private Thread receiver;
	
	private static Sensor temp = new Sensor("TEMP", "TEMP");
	private static Sensor heat = new Sensor("HEAT", "HEAT");
	private static Sensor cool = new Sensor("COOL", "COOL");
	
	private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").create();
	
	// ======================================================
	public void displayText(String msg) {
		Platform.runLater(() -> {
			textarea.appendText(msg + "\n");
		});
	}
	
	public static Gson getGson() {
		return gson;
	}
	
	@Override
	public String getDeviceID() {
		// TODO Auto-generated method stub
		return DEVICE_ID;
	}

	@Override
	public String getDeviceType() {
		// TODO Auto-generated method stub
		return DEVICE_TYPE;
	}
	
	@Override
	public String getSensorList() {
		List<String> sensorList = new ArrayList<String>();
		sensorList.add(gson.toJson(temp));
		sensorList.add(gson.toJson(heat));
		sensorList.add(gson.toJson(cool));
		return gson.toJson(sensorList);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		this.sharedObject = new TestSharedObject();
		this.toServer = new ServerListener(this, sharedObject);
//		this.toServer.setDeviceInfo(DEVICE_ID, DEVICE_TYPE);
		this.toSerial = new SerialListener(sharedObject);
		
		this.toServer.initialize();
		this.toSerial.initialize();
		
		Runnable runnable = () -> {
			SensorData data = this.sharedObject.pop();
			String msg = data.getSensorID() + data.getStates();
			
			this.toServer.send(new Message(this, data));
			this.toSerial.send(msg);
			
		};
		receiver = new Thread(runnable);
		receiver.start();
		
		// UI ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		root = new BorderPane();
		root.setPrefSize(700, 500);
		
		// Center ----------------------------------------------
		textarea = new TextArea();
		textarea.setEditable(false);
		root.setCenter(textarea);
		
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle("DeviceTemp");
		primaryStage.setOnCloseRequest((e) -> {
			this.toServer.close();
			this.toSerial.close();
			this.receiver.interrupt();
		});
		primaryStage.show();
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
