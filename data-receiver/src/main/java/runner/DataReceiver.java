package runner;


import Receiver.DataManager;
import Receiver.HandleThread;
import Receiver.ReceiveThread;

public class DataReceiver {
	
	public static void main(String args[]) {
		
		com.sun.org.apache.xml.internal.security.Init.init();
		
		
		DataManager controller = new DataManager();
		(new Thread(new ReceiveThread(controller))).start();
		(new Thread(new HandleThread(controller))).start();
		
	}

}
