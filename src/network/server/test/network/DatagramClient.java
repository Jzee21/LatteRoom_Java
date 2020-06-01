package network.server.test.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import network.server.vo.Message;

public class DatagramClient extends Application {

	private static final String ADDR = "localhost";
	private static final int PORT = 55000;
	private static final int SIZE = 10240;
	
	private DatagramSocket ds;
	private DatagramPacket dp;

	private ExecutorService executor;
	
	private BorderPane root;
	private FlowPane bottom;
	private TextArea ta;
	private TextField tf;
	
	private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").create();
	
	private void displayText(String text) {
		Platform.runLater(() -> {
			ta.appendText(text + "\n");
		});
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		initialize();
		
		root = new BorderPane();
		root.setPrefSize(700, 500);
		
		// Center ----------------------------------------------
		ta = new TextArea();
		ta.setEditable(false);
		root.setCenter(ta);
		
		//
		bottom = new FlowPane();
		bottom.setPrefSize(700, 50);
		
		tf = new TextField();
		tf.setPrefSize(500, 50);
		tf.setOnAction((e) -> {
			String text = tf.getText();
//			send(text);
			send(new Message("TEST", "TEST", text));
			
			tf.clear();
		});
		
		bottom.getChildren().addAll(tf);
		root.setBottom(bottom);
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle("DeviceBed");
		primaryStage.setOnCloseRequest((e) -> {
			close();
		});
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	private void initialize() {
		try {
			ds = new DatagramSocket();
			byte[] buffer = new byte[SIZE];
			dp = new DatagramPacket(buffer, SIZE);
			
			executor = Executors.newFixedThreadPool(4);
			
			Runnable receiver = () -> {
				while(true) {
					try {
						ds.receive(dp);
						int dataLen = dp.getLength();
						String line = new String(dp.getData(), 0, dataLen);
//						System.out.println("[data (" + dataLen + ")] " + line);
						displayText("[Echo data (" + dataLen + ")] " + line);
						
						if(line.equals("@EXIT")) {
							break;
						}
					} catch (IOException e) {
//						e.printStackTrace();
						break;
					}
				}
			};
			executor.submit(receiver);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void close() {
		ds.close();
		executor.shutdownNow();
	}
	
	private void send(Message message) {
		this.send(gson.toJson(message));
	}
	
	private void send(String text) {
		try {
			byte[] buf = text.getBytes();
//			displayText("Data Length : " + buf.length);
//			int loopTime = (buf.length)/SIZE+1;
//			if((buf.length)%SIZE == 0) loopTime--;
//			for (int i = 0; i < loopTime; i++) {
//				byte[] data = Arrays.copyOfRange(buf, i*SIZE, (i+1)*SIZE);
//				dp = new DatagramPacket(data, SIZE, InetAddress.getByName(ADDR), PORT);
//				ds.send(dp);
//			}
//			displayText("1024 * " + (buf.length)/SIZE);
			dp = new DatagramPacket(buf, buf.length, InetAddress.getByName(ADDR), PORT);
			ds.send(dp);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void send(DatagramPacket dp) {
		try {
			dp = new DatagramPacket(dp.getData(), dp.getLength(), dp.getAddress(), dp.getPort());
			ds.send(dp);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
