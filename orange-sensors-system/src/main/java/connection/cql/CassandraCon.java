package connection.cql;


import java.util.Date;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

public class CassandraCon {
	
	Cluster cluster;
	Session session;
	
	public CassandraCon() {

		this.cluster = Cluster.builder().addContactPoint("128.16.80.125").build();
		this.session = cluster.connect("orangesystem");
	}
	
	public void addReading(int global_id, float reading) {
		
		Date date = new Date();
		long ms = date.getTime();
		
		session.execute("UPDATE sensor_details SET reading = " + reading + ", timestamp = " + ms + " WHERE global_id = " + global_id +  ";");
		session.execute("INSERT INTO data_archive(global_id, timestamp, reading) VALUES (" + global_id + ", " + ms + ", " + reading + ");");
		System.out.println("Successfully inserted.");
	}
	
	public void initialiseSensor(String query) {
		session.execute(query);
		System.out.println("New sensor added to the network!");
	}
	
	public void closeCluster() {
		cluster.close();
	}
}
