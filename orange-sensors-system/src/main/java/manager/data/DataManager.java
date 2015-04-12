package manager.data;

import java.util.ArrayList;

public class DataManager {
	
	private volatile ArrayList<byte[]> received = new ArrayList<byte[]>();
	
	public synchronized void addPacket(byte[] packet) {
		
		System.out.println("DM - adding packet and notifying.");		
		
		received.add(packet);
		notify();

	}
	
	public synchronized byte[] readPacket() {
		
		try {
			while(received == null || received.size() == 0) {
				wait();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("Received: " + received.get(0).toString());
		
		byte[] result = received.get(0);
		received.remove(0);
		return result;
	}

}
