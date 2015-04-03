package Receiver;

import java.util.ArrayList;

public class DataManager {
	
	private volatile ArrayList<byte[]> received = new ArrayList<byte[]>();
	
	public synchronized void addPacket(byte[] packet) {
		
		System.out.println("DM - adding packet and notifying.");		
		
		received.add(packet);
		notify();
		try {
			wait();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public synchronized byte[] readPacket() {
		
		try {
			while(received == null || received.size() == 0) {
				System.out.println("DM - readPacket waiting...");
				wait();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("DM - readPacket retrieving " + received.get(0).toString());
		
		byte[] result = received.get(0);
		received.remove(0);
		notify();
		return result;
		
	}

}
