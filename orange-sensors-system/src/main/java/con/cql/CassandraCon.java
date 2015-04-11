package con.cql;


import java.util.Date;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

public class CassandraCon {
	
	Cluster cluster;
	Session session;
	
	public CassandraCon() {

		this.cluster = Cluster.builder().addContactPoint("128.16.80.125").build();
		this.session = cluster.connect("orangesystem");

		System.out.println("Successfully initiated Cassandra connection");
	}
	
	public void sendRawData(int id, int reading) {
		
		Date date = new Date();
		long ms = date.getTime();
		
		System.out.println("Date: " + ms);
		
		session.execute("INSERT INTO rawdata(sensor_ID, reading, timestamp) VALUES (" + id + ", " + reading + ", " + ms + ")");
		System.out.println("Successfully executed query");
	}
	
	public void sendNewData(int global_id, int operator_id, int sensor_id, int reading) {
		
		Date date = new Date();
		long ms = date.getTime();
		
		session.execute("UPDATE sensor_details SET reading = " + reading + ", timestamp = " + ms + " WHERE operator_id = " + operator_id + " AND sensor_id = " + sensor_id + ";");
		session.execute("INSERT INTO data_archive(global_id, timestamp, reading) VALUES (" + global_id + ", " + ms + ", " + reading + ");");
		System.out.println("Successfully inserted.");
	}
	
	public void closeCluster() {
		cluster.close();
		System.out.println("Cluster session now ded");
	}
}
