package network.server2.test.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class TestClient extends Application {
	
	// =========================================================
	// field
	static Selector selector = null;
    private SocketChannel socketChannel = null;
//    private ByteBuffer byteBuffer;
    private CharsetDecoder decoder = null;
    private Charset charset = null;
    
    private TextArea ta;
	private Button connBtn;
	private TextField tf;
	
	private ExecutorService executor = Executors.newCachedThreadPool();

	// =========================================================
	// method
    public void startServer() throws IOException {
        initServer();
        receive();
    }

    private void initServer() throws IOException {
        selector = Selector.open();
        socketChannel = SocketChannel.open(new InetSocketAddress("localhost", 55577));
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);
        
//        byteBuffer = ByteBuffer.allocateDirect(1024);
        charset = Charset.forName("UTF-8");
		decoder = charset.newDecoder();
    }
    
    private void receive() {
    	Runnable runnable = () -> {
    		while(true) {
    			try {
    				if(selector.select() > 0) {
    					Set<SelectionKey> selectionKeySet = selector.selectedKeys();
    					displayText("ready : " + selectionKeySet.size());
    					Iterator iterator = selectionKeySet.iterator();
    					
    					while (iterator.hasNext()) {
    						SelectionKey key = (SelectionKey) iterator.next();
    						
    						if (key.isReadable()) {
    							// read()
    							SocketChannel channel = (SocketChannel) key.channel();
    							ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024);
    							
    							try {
    								channel.read(byteBuffer);
    							} catch (Exception e) {
    								// TODO: handle exception
    								System.out.println("server disconnected");
    								channel.close();
    								return;
    							}
    							
    							byteBuffer.flip();
    							String data = decoder.decode(byteBuffer).toString();
    							if(data != null) {
    								displayText("get : " + data);
    							} else {
    								displayText("get : null");
    							}
    						} // if
    						iterator.remove();
    					}
    				}
    				
    			} catch (Exception e) {
					// TODO: handle exception
					return;
				}
    		}
    	};
    	executor.submit(runnable);
    }
    
    private void send(String text) throws Exception {
    	ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024);
    	if(this.socketChannel != null) {
    		byteBuffer.clear();
    		byteBuffer.put(text.getBytes());
    		byteBuffer.flip();
    		this.socketChannel.write(byteBuffer);
    	}
    }
    
    static void clearBuffer(ByteBuffer buffer) {
        if (buffer != null) {
            buffer.clear();
        }
    }
    
	// =========================================================
    // main
	public static void main(String[] args) {
		launch(args);
	}
	
	// =========================================================
	// UI method
	private void displayText(String msg) {
		Platform.runLater(() -> {
			ta.appendText(msg + "\n");
		});
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		BorderPane root = new BorderPane();
		root.setPrefSize(700, 500);
		
		ta = new TextArea();
		root.setCenter(ta);
		
		//		
		connBtn = new Button("Server Conn");
		connBtn.setPrefSize(150, 50);
		connBtn.setOnAction((e) -> {
			ta.clear();
			try {
				startServer();
			} catch (Exception e2) {
				// TODO: handle exception
			}
		});
		
		tf = new TextField();
		tf.setPrefSize(500, 50);
		tf.setOnAction((e) -> {
			String msg = tf.getText();
			try {
				send(msg);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			tf.clear();
			
		});
		
		FlowPane flowpane = new FlowPane();
		flowpane.setPrefSize(700, 50);
		//flowpane.getChildren().add(startBtn);
		flowpane.getChildren().addAll(connBtn, tf);
		
		root.setBottom(flowpane);
		
		//
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Chat Client");
		primaryStage.setOnCloseRequest(e -> {
			// System.exit(0);
			executor.shutdownNow();
		});
		primaryStage.show();
	} // start()

	// =========================================================
//	class Receive implements Runnable {
//		private CharsetDecoder decoder = null;
//		private boolean flag = false;
//		
//		public void run() {
//			Charset charset = Charset.forName("UTF-8");
//			decoder = charset.newDecoder();
//			try {
//				while (!flag) {
//					TestClient.selector.select();
//					Iterator iterator = TestClient.selector.selectedKeys().iterator();
//					
//					while (iterator.hasNext()) {
//						SelectionKey key = (SelectionKey) iterator.next();
//						
//						if(key.isReadable())
//							read(key);
//						
//						iterator.remove();
//					}
//				}
//			}
//			catch (Exception ex) {
//				ex.printStackTrace();
//			}
//		} // run()
//		
//		private void read(SelectionKey key) {
//			SocketChannel socketChannel = (SocketChannel) key.channel();
//			ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024);
//			
//			try {
//				byteBuffer.flip();
//				String data = decoder.decode(byteBuffer).toString();
//				
//				if(data.equals(null)) {
////					System.out.println("get null");
//					displayText(data);
//					this.flag = !this.flag;
//				}
//				
////				System.out.println("Receive Message - " + data);
//				displayText("Receive Message - " + data);
//				TestClient.clearBuffer(byteBuffer);
//			}
//			catch (IOException ex){
//				try {
//					socketChannel.close();
//				}
//				catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//		} // read()
//	} // Receive.class
} // TestClient.class
