package network.server3.main;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class DeviceTest extends Application{
	
	private String userID;
	
	private TextArea textarea;
	private Button connBtn, disconnBtn;
	private Button createRoomBtn;
	private Button connBoomBtn;
	
	private ListView<String> deviceList;

	
	public void printMsg(String msg) {
		Platform.runLater(() -> {
			textarea.appendText(msg + "\n");
		});
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		BorderPane root = new BorderPane();
		root.setPrefSize(700, 500);
		
		// Center
		textarea = new TextArea();
		textarea.setEditable(false);
		root.setCenter(textarea);
		
		// Right
		deviceList = new ListView<String>();
		// Set List
		deviceList.getItems().addAll("latte1", "latte2", "latte3", "latte4", "tester1");
		GridPane gridpane = new GridPane();
		gridpane.setPadding(new Insets(5,5,5,5));
		gridpane.setVgap(10);
		gridpane.add(deviceList, 0, 0);
		
		root.setRight(gridpane);
		
		// Bottom
		connBtn = new Button("Conn");
		connBtn.setPrefSize(150, 40);
		connBtn.setOnAction((e) -> {
			
			
			
//			printMsg("Chat Server Connected!");
//			printMsg("Welcome, " + entered + " !");
			
		});
		
		disconnBtn = new Button("Disconn");
		disconnBtn.setPrefSize(150, 40);
		disconnBtn.setOnAction((e) -> {
			
		});
		
		createRoomBtn = new Button();
		createRoomBtn.setPrefSize(150,  40);
		createRoomBtn.setOnAction((e) -> {
			
		});
		
		connBoomBtn = new Button("Room Connect");
		connBoomBtn.setPrefSize(150,  40);
		connBoomBtn.setOnAction((e) -> {
			// Open Stream
			String roomName = deviceList.getSelectionModel().getSelectedItem();
			
			//
			printMsg("Enter Room " + roomName);
			
			// 화면 하단 레이아웃 전환
			FlowPane inputFlowPane = new FlowPane();
			inputFlowPane.setPrefSize(700, 40);
			inputFlowPane.setPadding(new Insets(5,5,5,5));
			inputFlowPane.setHgap(10);
			
			TextField inputTF = new TextField();
			inputTF.setPrefSize(500, 40);
			
			inputFlowPane.getChildren().addAll(inputTF);
			
			root.setBottom(inputFlowPane);
			
		});
		
		FlowPane menuFlowPane = new FlowPane();
		menuFlowPane.setPrefSize(700, 40);
		menuFlowPane.setPadding(new Insets(5,5,5,5));
		menuFlowPane.setHgap(10);
		menuFlowPane.getChildren().addAll(connBtn, disconnBtn, connBoomBtn);
		root.setBottom(menuFlowPane);
		
		
		//
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Multi Room Chat Client");
		primaryStage.setOnCloseRequest((e) -> {
			//
		});
		primaryStage.show();
		
	} // start(Stage primaryStage)

	public static void main(String[] args) {
		launch();
	}

}