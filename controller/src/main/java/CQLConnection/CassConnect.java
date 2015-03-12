package CQLConnection;


import java.util.Date;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

public class CassConnect {
	
	Cluster cluster;
	Session session;
	
	public CassConnect() {

		this.cluster = Cluster.builder().addContactPoint("128.16.80.125").build();
		this.session = cluster.connect("orangesensors");

		System.out.println("Successfully initiated connection");
	}
	
	public void sendRawData(int id, int reading) {
		
		Date date = new Date();
		long ms = date.getTime();
		
		System.out.println("Date: " + ms);
		
		session.execute("INSERT INTO rawdata(sensor_ID, reading, timestamp) VALUES (" + id + ", " + reading + ", " + ms + ")");
		System.out.println("Successfully executed query");
	}
	
	public void closeCluster() {
		cluster.close();
		System.out.println("Cluster session now ded");
	}
}
