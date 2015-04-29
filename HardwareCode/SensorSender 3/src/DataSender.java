
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.CountDownLatch;;
public class DataSender {
	
	// Takes the Port ID (e.g. COM3 or COM7) as an argument when you run it - e.g. java -jar DataSender.jar "COM7"
	public synchronized static void main(String[] args) {
		
		// Set up everything required for sending a packet to the server
		String rawAddress = "128.16.80.125";
		byte[] buf = new byte[256];
		DatagramSocket socket;
		InetAddress address;
		try {
			socket = new DatagramSocket();
			address = InetAddress.getByName(rawAddress);
			
		} catch (Exception e) {
			System.out.println("Failed on socket initialisation");
			return;
		}
		SerialReader getData = new SerialReader(args[0]);
		getData.initialize();
		
		String result = "";
		
		// This will just run forever, until you stop it yourself
		while (true) {
			
			// Loop until we get data
			while (result.equals("")) {
				result = getData.getInput();
				
			}
			System.out.println(result);
			
			
			System.out.println(result);
			// Make input empty again so you can wait next time
			
			
			// Converts resulting string into bytes
			try {
				buf = result.getBytes("UTF-8");
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
			result = getData.setInput();
			// Assembles a packet of the data
			DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 2014);
		
			// Sends the data packet
			try {
				socket.send(packet);
				System.out.println("Sent.");
			} catch (IOException e) {
				System.out.println("IO Exception; couldn't send packet!");
			}
		}
	}
}
