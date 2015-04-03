package Receiver;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import CQLConnection.CassConnect;

public class InputThread implements Runnable {
	
	private String hash;
	private String data;
	private Connection con = null;
	private CassConnect cqlcon;
	
	public InputThread(String hash, String data, CassConnect cqlcon, Connection con) {
		this.hash = hash;
		this.data = data;
		this.cqlcon = cqlcon;
		this.con = con;
	}
	
	public void run() {
		System.out.println("Hash: " + hash);
		System.out.println("Data: " + data);
		
		Statement userIDQuery = null;
		String userIDString = "SELECT user_id FROM users WHERE identifier = '" + hash + "';";
		
		int user_id = 0;
		
		try {
			userIDQuery = con.createStatement();
			ResultSet rs = userIDQuery.executeQuery(userIDString);
			rs.next();
			user_id = rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		int id = -1;
		int sensor_id = -1;
		int reading = -1;
		int sep;
		int end;
		
		// TODO: JUnit test documentation for the following method
		
		while(data.length() > 0) {
			sep = data.indexOf(':');
			end = data.indexOf(';');
			
			// Should in theory handle rare cases where the semicolon is omitted
			if(end < 0)
				end = data.length();
			
			System.out.println(sep + " " + end);
			
			// Separate the IDs as ints
			id = Integer.parseInt(data.substring(0, sep));
			reading = Integer.parseInt(data.substring(sep+1, end));
			
			sensor_id = buildID(user_id, id);
			
			// TODO: Handle -1 results (i.e. not responding, or errors - MAINTENANCE)
			
			// Sends data to the rawdata table in Cassandra
			cqlcon.sendRawData(sensor_id, reading);
			
			// Either decide we've reached the end of the string OR cut off the bit we just sent
			if (end == data.length())
				break;
			else
				data = data.substring(end+1, data.length());
		}
		return;
	}
	
	// Gives sensor id with format  1-[user_id]-[user's-sensor-id], e.g. 198765001 - user 98765's 1st sensor
	private static int buildID(int user_id, int id) {
		user_id *= 10000;
		return 1000000000 + user_id + id;
	}
	
}
