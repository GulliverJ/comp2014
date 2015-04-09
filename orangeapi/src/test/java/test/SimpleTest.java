package test;

import session.*;
import util.*;

public class SimpleTest {

	public static void main(String[] args) {
		OrangeConnection con = new OrangeConnection();
		int[] ids = con.getIDsWhere("application", "Test Parking");
		for (int i = 0; i < ids.length; i++) {
			SensorData[] readings = con.getDataSince(ids[i], con.getDateAdded(ids[i]));
			for(int j = 0; j < readings.length; j++) {
				System.out.println(readings[j].getID() + ": " + readings[j].getReading());
			}
		}
	}

}
