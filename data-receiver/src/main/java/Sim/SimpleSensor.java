package Sim;

import java.util.Random;

public class SimpleSensor {
	
	private static int getReading() {
		Random rand = new Random();
		
		int randomNum = rand.nextInt(41) + 30;
		
		System.out.println(">>>Sensor is reading " + randomNum);
		return randomNum;
	}
/*
	public static void main(String[] args) {
		
		CQLConnection.CassConnect connection = new CQLConnection.CassConnect();
		int i = 1;
		int sensorID;
		int reading;
		while(true) {
			while(i < 1000) {
				sensorID = i;
				reading = getReading();
				connection.sendRawData(sensorID, reading);
				try {
					Thread.sleep(333);
				}catch(Exception e) {
					System.out.println("Interrupt");
				}
				i++;
			}	
			i = 0;
		}
	}
	*/
}