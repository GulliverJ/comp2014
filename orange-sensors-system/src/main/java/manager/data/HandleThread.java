package manager.data;

import java.sql.*;

import connection.cql.CassandraCon;

public class HandleThread implements Runnable {
	
	private final DataManager controller;
	private static String read;
	private boolean kill = false;
	//private static String hash;
	private static String data;
	private static Connection con = null;
	private CassandraCon cqlcon = new CassandraCon();
	
	
	public HandleThread(DataManager controller) {
		this.controller = controller;
	}
	
	public void run() {
		
		try {
			String connection = "jdbc:mysql://127.0.0.1/orangesystem";
			String user = "data-receiver";
			String password = "comp2014";
			con = DriverManager.getConnection(connection, user, password);
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Couldn't connect to SQL database.");
			System.exit(1);
			// Implement shutdown method
		}
		
		try {
			String hash;
			
			while( !kill ) {
				// TODO: handling of multiple hashes in a packet (e.g. abcd1234:5:12;efgh5678;3:13) - colon colon more efficient/easier?
				
				read = new String(controller.readPacket(), "UTF-8");
				hash = read.substring(0, 8);
				if (verifyHash(hash, con)) {
					data = read.substring(9, read.lastIndexOf(';'));
					(new Thread(new InputThread(hash, data, cqlcon, con))).start();
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(con != null) {
					con.close();
					cqlcon.closeCluster();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return;
	}
	
	public void stop() {
		kill = true;
	}
	
	private static boolean verifyHash(String hash, Connection con) {
		
		Statement checkHash = null;
		
		String checkHashString = "SELECT identifier FROM identifiers WHERE identifier = '" + hash + "';";
		
		try {
			checkHash = con.createStatement();
			ResultSet rs = checkHash.executeQuery(checkHashString);
			if (rs.next()) {
				return true;
			}
			else {
				System.out.println("Hash not recognised! Discarding string.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}

}
