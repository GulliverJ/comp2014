import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;

import gnu.io.CommPortIdentifier; 
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent; 
import gnu.io.SerialPortEventListener; 

import java.util.Enumeration;

public class SerialReader implements SerialPortEventListener {
	
	SerialPort serialPort;
	private String portToRead = null;
	private BufferedReader input;
	private OutputStream output;
	public String inputLine = "";
	public String temp = "";
	
	/* Milliseconds to block while waiting for port open */
	private static final int TIME_OUT = 2000;
	/* Default bits per second for COM port. */
	private static final int DATA_RATE = 115200;
	
	public SerialReader(String portToRead) {
		this.portToRead = portToRead;
	}
	public synchronized String getInput(){
		
        return inputLine;
    }
	
	public synchronized String setInput(){
		inputLine = "";
        return inputLine;
    }
	public void initialize() {

		CommPortIdentifier portId = null;
		Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

		// Loop through all recognised serial ports looking for the one we've specified
		while (portEnum.hasMoreElements()) {
			CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
			if (currPortId.getName().equals(portToRead)) {
				portId = currPortId;
				break;
			}
		}
		if (portId == null) {
			System.out.println("Could not find COM port.");
			return;
		}
		
		try {
			// Open serial port, and use class name for the appName.
			serialPort = (SerialPort) portId.open(this.getClass().getName(),
					TIME_OUT);

			// Set port parameters
			serialPort.setSerialPortParams(DATA_RATE,
					SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);

			// Open the streams
			input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
			output = serialPort.getOutputStream();

			// Add event listeners
			serialPort.disableReceiveTimeout();
			serialPort.enableReceiveThreshold(1);
			serialPort.addEventListener(this);
			serialPort.notifyOnDataAvailable(true);
		} catch (Exception e) {
			System.err.println(e.toString());
		}
	}

	/* Closes the port after use */
	public synchronized void close() {
		if (serialPort != null) {
			serialPort.removeEventListener();
			serialPort.close();
		}
	}

	/* Runs when a serial event occurs (such as when it reads a return from the Arduino) */
	public synchronized void serialEvent(SerialPortEvent oEvent) {
		
		if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			try {
				if (input.ready()) {
					/* IMPORTANT - NEEDS TO BE TESTED
					 * 
					 * Last term, I set this up to send a single bit to the arduino, which initiated a response.
					 * This time, we're just waiting for the arduino to send data.
					 * As such, I THINK this code will work but I can't guarantee it; I just deleted the
					 * lines which send a bit first. In principal, the serialEvent method should wait
					 * until the arduino sends it some data, at which point it checks if there's data
					 * available, and if it's ready, it'll assign it to inputLine where the
					 * DataSender method can take it.
					 * 
					 * */
					if(input.readLine() != inputLine)
					{
					inputLine = input.readLine();
					
					Thread.sleep(5000);
					}
					notifyAll();
					// If everything's worked, this should print "efgh5678;1:57;" or whatever to the console (for testing)
					input.skip(100000);
		
				}
			} catch (Exception e) {
				System.err.println(e.toString());
			}
		}
	}

}
