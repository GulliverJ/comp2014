package sensors.initialise;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import connection.cql.CassandraCon;

public class InitialisationManager implements Runnable {
	
	private boolean kill = false;
	
	public void run() {
		
		Socket sock;
		ServerSocket listen;
		BufferedReader br;
		CassandraCon cqlcon;
		
		while(!kill) {
			try {
				sock = new Socket();
				listen = new ServerSocket(2015);
				
				sock = listen.accept();
				
				cqlcon = new CassandraCon();
				
				br = new BufferedReader(new InputStreamReader(sock.getInputStream()));
				String query = "INSERT INTO sensor_details(global_id, lat, lng, application, measures, unit, first_added) VALUES (";
				
				for(int i = 0; i < 3; i++) {
					query += br.readLine() + ", ";
				}
				for(int i = 0; i < 3; i++) {
					query += "'" + br.readLine() + "', ";
				}
				
				query += "dateof(now()));";
				
				cqlcon.initialiseSensor(query);
				
				br.close();
				sock.close();
				listen.close();
				cqlcon.closeCluster();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void kill() {
		this.kill = true;
	}
	
}
