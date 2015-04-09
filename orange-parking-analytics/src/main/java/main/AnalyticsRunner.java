package main;

import com.orange.api.session.*;

public class AnalyticsRunner {

	public static void main(String[] args) {
		demoAPI();

		
	}
	
	private static void demoAPI() {
		OrangeConnection api = new OrangeConnection();
		int[] allParkingSensors = api.getIDsWhere("application", "Orange Parking");
		
		float[] curReadings = api.getLastReadingList(allParkingSensors);
		
		for(int i = 0; i < curReadings.length; i++) {
			System.out.println(allParkingSensors[i] + ": " + curReadings[i]);
		}
	}

}
