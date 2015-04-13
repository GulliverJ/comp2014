package main;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Parking {
	
	// User details for accessing the SQL database
	private static final String username = "root"; 
	private static final String password = "orange";
	private static final String connectionString = "jdbc:mysql://127.0.0.1:3306/orangeparking?user=root";
	
	public static List<Bay> getBays() {
		List<Bay> bays = new ArrayList<Bay>();
		try {
			Connection connection = DriverManager.getConnection(connectionString, username, password);
			Statement statement = connection.createStatement();
			String query = "select * from bays inner join bay_data on bays.bay_id = bay_data.bay_id inner join bay_analysis on bays.bay_id = bay_analysis.bay_id inner join restrictions on bays.restriction_id = restrictions.restriction_id order by bays.bay_id asc;";
			ResultSet resultSet = statement.executeQuery(query);
			while (resultSet.next()) {
				long restrictionId = resultSet.getLong("restriction_id");
				Time restrictionStart = resultSet.getTime("start");
				Time restrictionEnd = resultSet.getTime("end");
				boolean restrictionWeekendIncluded = resultSet.getByte("includes_weekend") == 1;
				Time restrictionMaximumStay = resultSet.getTime("max_stay");
				double restrictionHourlyCharge = resultSet.getDouble("hourly_charge");
				Restriction restriction = new Restriction(restrictionId, restrictionStart,
						restrictionEnd, restrictionWeekendIncluded,
						restrictionMaximumStay, restrictionHourlyCharge);
				long bayId = resultSet.getLong("bay_id");
				long sensorId = resultSet.getLong("global_id");
				boolean occupied = resultSet.getByte("occupied") == 1;
				Timestamp state = resultSet.getTimestamp("state_timespan");
				Time averageDailyAvailability = resultSet.getTime("ave_daily_availability");
				Bay bay = new Bay(bayId, sensorId, restriction,
						occupied, state, averageDailyAvailability);
				bays.add(bay);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Couldn't connect to SQL database!");
		}
		
		return bays;
	}
	
	public static void update(Bay bay) {
		try {
			Connection connection = DriverManager.getConnection(connectionString, username, password);
			PreparedStatement statement = connection.prepareStatement("update bay_data set "
					+ "occupied = ?,"
					+ "state_timespan = ?"
					+ " where bay_id = ?;");
			statement.setByte(1, (byte) (bay.isOccupied() ? 1 : 0));
			statement.setTimestamp(3, bay.getState());
			statement.setInt(4, (int) bay.getId());
			statement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Couldn't connect to SQL database!");
		}
	}
	
}
