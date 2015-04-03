package Receiver;

import java.sql.*;

import CQLConnection.CassConnect;

public class HandleThread implements Runnable {
	
	private final DataManager controller;
	private static String read;
	private boolean kill = false;
	private static String hash;
	private static String data;
	private static Connection con = null;
	private CassConnect cqlcon = new CassConnect();
	
	private String connection = "jdbc:mysql://127.0.0.1/orangeusers";
	private String user = "GTAdmin";
	private String password = "COMP2014g10";

	
	public HandleThread(DataManager controller) {
		this.controller = controller;

	}
	
	public void run() {
		
		try {
			System.out.println("HT - getting connection");
			con = DriverManager.getConnection(connection, user, password);
		
			while( !kill ) {
				read = new String(controller.readPacket(), "UTF-8");
				hash = read.substring(0, 8);
				System.out.println("HT - read: " + read + ", hash: " + hash);
				if (verifyHash()) {
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
	
	private static boolean verifyHash() {
		
		Statement checkHash = null;
		
		String checkHashString = "SELECT identifier FROM users WHERE identifier = '" + hash + "';";
		
		try {
			checkHash = con.createStatement();
			ResultSet rs = checkHash.executeQuery(checkHashString);
			if (rs.next())
			{
				System.out.println("Hash valid");
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}

}
