package manager.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import connection.cql.CassandraCon;

public class InputThread implements Runnable {
	
	private String hash;
	private String data;
	private Connection con = null;
	private CassandraCon cqlcon;
	
	public InputThread(String hash, String data, CassandraCon cqlcon, Connection con) {
		this.hash = hash;
		this.data = data;
		this.cqlcon = cqlcon;
		this.con = con;
	}
	
	public void run() {
		
		//Retrieves Operator ID
		Statement opIDQuery = null;
		String opIDString = "SELECT operator_id FROM identifiers WHERE identifier = '" + hash + "';";
		int operator_id = -1;
		
		try {
			opIDQuery = con.createStatement();
			ResultSet rs = opIDQuery.executeQuery(opIDString);
			rs.next();
			operator_id = rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
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
			
			// Separate the IDs as ints
			// TODO: What about floats or doubles? test.
			sensor_id = Integer.parseInt(data.substring(0, sep));
			reading = Integer.parseInt(data.substring(sep+1, end));
			
			// TODO: Handle -1 results (i.e. not responding, or errors - MAINTENANCE)
			// TODO: Handle the appropriate rows not existing in either table (shouldn't happen)
			
			//Get global key too
			Statement globIDQuery = null;
			String globIDString = "SELECT global_id FROM sensors WHERE operator_id = " + operator_id + " AND sensor_id = " + sensor_id;
			int global_id = -1;
			
			try {
				globIDQuery = con.createStatement();
				ResultSet rs = globIDQuery.executeQuery(globIDString);
				rs.next();
				global_id = rs.getInt(1);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			// Sends data to the rawdata table in Cassandra
			// Todo: send a list to cqlcon?
			cqlcon.addReading(global_id, reading);
			
			// Either decide we've reached the end of the string OR cut off the bit we just sent
			if (end == data.length())
				break;
			else
				data = data.substring(end+1, data.length());
		}
		return;
	}
	
}
