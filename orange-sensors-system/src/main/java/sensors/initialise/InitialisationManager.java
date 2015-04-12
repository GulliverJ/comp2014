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
		while(!kill) {
			try {
				Socket sock = new Socket();
				ServerSocket listen = new ServerSocket(2015);
				
				sock = listen.accept();
				
				CassandraCon cqlcon = new CassandraCon();
				
				BufferedReader br = new BufferedReader(new InputStreamReader(sock.getInputStream()));
				String query = "INSERT INTO sensor_details(global_id, lat, lng, application, measures, unit, reading) VALUES (";
				
				for(int i = 0; i < 3; i++) {
					query += br.readLine() + ", ";
				}
				for(int i = 0; i < 3; i++) {
					query += "'" + br.readLine() + "', ";
				}
				query += "dateof(now()));";
				
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
