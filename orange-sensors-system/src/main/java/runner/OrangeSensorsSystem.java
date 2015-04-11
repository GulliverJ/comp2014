package runner;


import manager.data.DataManager;
import manager.data.HandleThread;
import manager.data.ReceiveThread;

public class OrangeSensorsSystem {
	
	public static void main(String args[]) {
		
		DataManager controller = new DataManager();
		(new Thread(new ReceiveThread(controller))).start();
		(new Thread(new HandleThread(controller))).start();
		
	}

}
