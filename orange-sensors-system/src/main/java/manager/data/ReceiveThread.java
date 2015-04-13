package manager.data;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ReceiveThread implements Runnable {
	
	private final DataManager controller;
	private boolean kill = false;
	private DatagramSocket socket = null;
	
	public ReceiveThread(DataManager controller) {
		this.controller = controller;
		
		try {
			socket = new DatagramSocket(2014);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void run() {

		while(!kill) {

			try {
				byte[] buf = new byte[256];
				
				DatagramPacket packet = new DatagramPacket(buf, buf.length);
				if(socket == null) {
					System.out.println("No connection!");
					return;
				}
				socket.receive(packet);
					
				System.out.print("Received: " + new String(packet.getData(), "UTF-8") + "   Status: ");
				
				controller.addPacket(packet.getData());
								
			} catch (Exception e) {
				e.printStackTrace();
			}
		
		}
		socket.close();
	}
	
	public synchronized void kill() {
		kill = true;
	}
}
