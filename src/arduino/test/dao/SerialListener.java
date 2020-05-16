package arduino.test.dao;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import arduino.test.vo.*;

public class SerialListener implements SerialPortEventListener {
	
	private static final String COMPORT_NAMES = "COM11";
	SerialPort serialPort;
	
	private static final int TIME_OUT = 2000;
	private static final int DATA_RATE = 9600;
	private BufferedReader serialIn;
	private PrintWriter serialOut;
	
	private TestSharedObject sharedObject;
	private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").create();
	
	public SerialListener(TestSharedObject sharedObject) {
		this.sharedObject = sharedObject;
	}
	
	public void initialize() {
		CommPortIdentifier portId = null;
		try {
			portId = CommPortIdentifier.getPortIdentifier(COMPORT_NAMES);
		} catch (NoSuchPortException e1) {
			e1.printStackTrace();
		};
		
		if (portId == null) {
			System.out.println("Could not find COM port.");
			return;
		}

		try {
			// open serial port, and use class name for the appName.
			serialPort = (SerialPort) portId.open(this.getClass().getName(), TIME_OUT);

			// set port parameters
			serialPort.setSerialPortParams(
					DATA_RATE,					// 9600 
					SerialPort.DATABITS_8, 
					SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);

			// open the streams
			serialIn = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
			serialOut = new PrintWriter(serialPort.getOutputStream());

			// add event listeners
			serialPort.addEventListener(this);
			serialPort.notifyOnDataAvailable(true);
		} catch (Exception e) {
			System.err.println(e.toString());
		}
	}

	/**
	 * This should be called when you stop using the port. This will prevent port
	 * locking on platforms like Linux.
	 */
	public synchronized void close() {
		if (serialPort != null) {
			serialPort.removeEventListener();
			serialPort.close();
		}
	}
	
	public synchronized void send(String msg) {
		serialOut.println(msg);
		serialOut.flush();
	}

	/**
	 * Handle an event on the serial port. Read the data and print it.
	 */
	public synchronized void serialEvent(SerialPortEvent oEvent) {
		if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			try {
				String line = serialIn.readLine();
				float eventTemp = Float.parseFloat(line);
				
			} catch (Exception e) {
			}
		}
		// Ignore all the other eventTypes, but you should consider the other ones.
	}
	
	
	
}