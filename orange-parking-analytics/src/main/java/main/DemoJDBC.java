package main;

import java.sql.*;

public class DemoJDBC {
	
	public static void main(String[] args) {
		
		// User details for accessing the SQL database
		String username = "orangeadmin"; 
		String password = "orangejuice";
		Connection con = null;
		String connection = "jdbc:mysql://localhost/orangeparking";
		
		try {
			con = DriverManager.getConnection(connection, username, password);
			Statement stmt = con.createStatement();
			String query = "INSERT INTO bay_data(occupied) VALUES (1) WHERE bay_id = 1;";
			stmt.execute(query);
		} catch (SQLException e) {
			System.out.println("Couldn't connect to SQL database!");
		}
		
		// That's how you insert stuff, anyway. Request results like this: 
		
		try {
			con = DriverManager.getConnection(connection, username, password);
			Statement stmt = con.createStatement();
			String query = "SELECT road FROM bays WHERE global_id = 14;";
			ResultSet rs = stmt.executeQuery(query);
			if(rs.next()) {
				System.out.println(rs.getString("road"));
			}
		} catch (SQLException e) {
			System.out.println("Ya dun goof'd");
		}

	}

}
