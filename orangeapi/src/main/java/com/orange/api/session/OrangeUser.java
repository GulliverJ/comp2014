package com.orange.api.session;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * 
 * @author Gulliver Johnson
 *
 */
public class OrangeUser {
	
	protected String username;
	protected String password;
	protected static UserAccount account;
	protected boolean verified = false;

	/**
	 * Instantiates an connetion to the database containing user details in order
	 * to allow for personal data retrieval.
	 * 
	 * @param username			the company name chosen at user registration
	 * @param password			the password chosen at user registration
	 * @throws SQLException		if a connection to the database couldn't be made
	 */
	public OrangeUser(String username, String password) throws SQLException{
		this.username = username;
		this.password = password;
		
		account = new UserAccount(username, password);
		
		if(account.isVerified()) {
			this.verified = true;
		}
	}
	
	/**
	 * Returns the global_ids of all of the authenticated user's sensors.
	 * 
	 * @return					an array containing all global_ids of sensors belonging to the user
	 * @throws SQLException		if a connection to the database couldn't be made
	 */
	public int[] getMyIDs() throws SQLException {
		//Need a better way of handling a non-verified request
		if(account.isVerified()) {
			return account.getGlobalIDs();
		}
		return null;
	}
	
	/**
	 * Returns the global_ids of all of the authenticated user's sensors filtered by
	 * a collection of internal IDs. Aside from unique, global_id, each sensor
	 * has an ID number describing the order in which it was created for its
	 * owner. The first sensor that a new user registers will always have
	 * sensor_id = 1, whilst its global_id will be set to one higher than the
	 * sensor created (by any user) before it.
	 * 
	 * @param sensor_ids		an array containing a collection of sensor_ids describing sensors registered  by the user
	 * @return					an array containing all global_ids of sensors belonging to the user which match the filtering set
	 * @throws SQLException		if a connection to the database couldn't be made
	 */
	public int[] getMyIDs(int[] sensor_ids) throws SQLException {
		
		if(account.isVerified()) {
			return account.getGlobalIDs(sensor_ids);
		}
		return null;
	}
	
	/**
	 * Returns the global_ids of all of the authenticated user's sensors
	 * filtered by a column and its value argument. The columns used are
	 * limited to 'application', 'measures', and 'unit'.
	 * 
	 * @param col				the column to filter by. One of 'application', 'measures', or 'unit'
	 * @param arg				the argument to filter by
	 * @return					an array containing all global_ids of sensors belonging to the user which match the filtering column argument
	 * @throws SQLException		if a connection to the database couldn't be made.
	 */
	public int[] getMyIDsWhere(String col, String arg) throws SQLException {
		
		if(account.isVerified()) {
			return account.getGlobalIDs(col, arg);
		}
		return null;
	}
	
	/**
	 * Returns the global_ids of all of the authenticated user's sensors
	 * filtered by a set of columns and their respective arguments.
	 * 
	 * @param cols				the columns to filter by
	 * @param args				the respective arguments to filter by
	 * @return					an array containing all global_ids of sensors belonging to the user which match the filter
	 * @throws SQLException		if a connection to the database couldn't be made
	 */
	public int[] getMyIDsWhere(String[] cols, String[] args) throws SQLException {
	
		if(account.isVerified()) {
			return account.getGlobalIDs(cols, args);
		}
		return null;
	}	
}

class UserAccount {
	
	private static Connection con = null;
	private String username;
	private String password;
	private static int operator_id;
	private static boolean verified = false;
	
	public UserAccount(String username, String password) throws SQLException{
		
		this.username = username;
		this.password = password;
		
		String connection = "jdbc:mysql://localhost/orangesystem";
		String user = "orangeapi";
		con = DriverManager.getConnection(connection, user, user);
		
		if(userExists(username, password)) {
			verified = true;
		} else {
			// Something about the user credentials not matching
		}
		
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public int getID() {
		return operator_id;
	}
	
	public boolean isVerified() {
		return verified;
	}
	
	public int[] getGlobalIDs() throws SQLException {
		Statement stmt = null;
		String query = "SELECT global_id FROM sensors WHERE operator_id = " + operator_id + ";";
		return parseIDs(getIDArrayList(stmt, query));
	}
	
	public int[] getGlobalIDs(int[] sensors) throws SQLException {
		Statement stmt = null;
		String query = "SELECT global_id FROM sensors WHERE operator_id = " + operator_id + " AND sensor_id IN " + buildInList(sensors) + ";";
		return parseIDs(getIDArrayList(stmt, query));
	}
	
	public int[] getGlobalIDs(String col, String arg) throws SQLException {
		Statement stmt = null;
		String query = "SELECT global_id FROM sensors WHERE operator_id = " + operator_id + " AND " + col + " = '" + arg + "';";
		return parseIDs(getIDArrayList(stmt, query));
	}
	
	public int[] getGlobalIDs(String[] cols, String[] args) throws SQLException {
		Statement stmt = null;
		String query = idQueryBuilder(cols, args);
		return parseIDs(getIDArrayList(stmt, query));
	}
	
	// (nearly) DUPLICATE METHOD! DO SOMETHING ABOUT IT
	private static String idQueryBuilder(String[] cols, String[] args) {
		if(cols.length != args.length) {
			System.out.println("Number of columns must match the number of arguments!");
			return "";
		}
		
		String queryString = "SELECT global_id FROM sensors WHERE ";
		String addConditions = "";
		
		for(int i = 0; i < cols.length; i++) {
			addConditions = cols[i] + " = '" + args[i];
			if(i == cols.length - 1) {
				addConditions += "';";
			} else {
				addConditions += "' AND ";
			}
			queryString += addConditions;
		}
		return queryString;
	}
	
	private static int[] parseIDs(ArrayList<Integer> ids) {
		int[] idList = new int[ids.size()];
		for(int i = 0; i < ids.size(); i++) {
			idList[i] = ids.get(i);
		}
		return idList;
	}
	
	// Returns an ArrayList with all ints inside (needs to throw an SQL Exception!)
	private static ArrayList<Integer> getIDArrayList(Statement stmt, String query) throws SQLException {
		ArrayList<Integer> ids = new ArrayList<Integer>();
		stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		while (rs.next()) {
			ids.add(rs.getInt("global_id"));
		}
		return ids;
	}
	
	// Stringifies an int array, enclosed by parentheses
	private static String buildInList(int[] sensors) {
		String result = "(";
		for(int i = 0; i < sensors.length; i++) {
			result += sensors[i];
			if (i == sensors.length - 1)
				result += ")";
			else
				result += ", ";
		}
		return result;
	}
	
	// Verifies the user's credentials
	private static boolean userExists(String company, String password) throws SQLException {
		
		Statement checkPass = null;
		
		String checkPassString = "SELECT password, operator_id FROM operators WHERE company = '" + company + "';";
		
		checkPass = con.createStatement();
		ResultSet rs = checkPass.executeQuery(checkPassString);
		if (rs.next()) {
			if (rs.getString("password").equals(password)) { 
				operator_id = rs.getInt(2);
				return true;
			}
			System.out.println("Incorrect password!");
		}
		else {
			System.out.println("Company name not recognised!");
		}
		
		return false;
	}
}
