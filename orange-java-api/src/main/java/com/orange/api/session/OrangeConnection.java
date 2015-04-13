package com.orange.api.session;

import java.util.ArrayList;

import com.datastax.driver.core.*;
import com.orange.api.util.Position;
import com.orange.api.util.SensorData;

import java.util.Date;
import java.util.Arrays;

/**
 * 
 * @author Gulliver Johnson
 *
 */
public class OrangeConnection {
	
	protected static Cluster cluster;
	protected static Session session;

	/**
	 * Initialise an unauthenticated connection to retrieve public data
	 */
	public OrangeConnection() {
		cluster = Cluster.builder().addContactPoint("128.16.80.125").build();
		session = cluster.connect("orangesystem");
	}
	
	/**
	 * To be used to securely kill the connection upon process termination.
	 */
	public void close() {
		cluster.close();
	}
	
	/* METHODS FOR RETRIEVING GLOBAL IDS */

	/**
	 * Returns an array of global_ids filtered by a given column matching a  
	 * value.
	 * <p>
	 * Valid columns to query are limited to:
	 * <ul>
	 * <li>application - the application label as defined by the sensor's owner</li>
	 * <li>measures - the physical attribute measured by the sensor. Examples
	 * include DISTANCE, TEMPERATURE, LIGHT, and ACCELERATION.</li>
	 * <li>unit - the unit of the stored readings. Examples include "cm", "kelvin", and "m/s^2"</li>
	 * </ul>
	 * </p>
	 * Other methods must be used if filtering by a non-text column.
	 * 
	 * @param col	one of application, measures, or unit
	 * @param arg	an argument to filter by
	 * @return		an array of global IDs as filtered by col with value = arg
	 */
	public int[] getIDsWhere(String col, String arg) {
		
		Statement statement = new SimpleStatement("SELECT global_id FROM sensor_details WHERE " + col + " = '" + arg + "';");
		ResultSet results = session.execute(statement);
		
		return parseIDs(results);
	}
	
	/**
	 * Returns an array of global_ids filtered by multiple given columns and values.
	 * 
	 * @param cols	a String array containing column names - one of application, measures, or unit
	 * @param args	an array of string arguments to filter by
	 * @return		an array of global IDs as filtered by cols with value = respective arg
	 */
	public int[] getIDsWhere(String[] cols, String[] args) {
		
		if(cols.length != args.length) {
			System.out.println("Number of columns must match the number of arguments!");
			return null;
		}
		
		String queryString = buildListQuery(cols, args) + "ALLOW FILTERING;";
		
		Statement statement = new SimpleStatement(queryString);
		ResultSet results = session.execute(statement);
		
		return parseIDs(results);
	}
	
	/**
	 * Returns an array of global_ids filtered by both a column with a value and a
	 * timestamp. Any IDs of matching sensors which have sent a reading since the specified
	 * timestamp will be returned.
	 * 
	 * @param col			one of application, measures, or unit
	 * @param arg			a value to filter by
	 * @param timestamp		returned values will have timestamps greater than this
	 * @return				an array of global IDs from rows updated since the given timestamp, matching the given properties
	 */
	public int[] getIDsWhereSince(String col, String arg, Date timestamp) {
		
		Statement statement = new SimpleStatement("SELECT global_id FROM sensor_details WHERE " + col + " = '" + arg + "' AND timestamp >= " + timestamp.getTime() + " ALLOW FILTERING;");
		ResultSet results = session.execute(statement);
		
		return parseIDs(results);
	}
	
	/**
	 * Returns an array of global_ids filtered by a list of columns and values, and a
	 * timestamp. Any IDs of matching sensors which have sent a reading since the specified
	 * timestamp will be returned.
	 * 
	 * @param col			a list containing 'application', 'measures', and/or 'unit'
	 * @param arg			a list containing respective values to filter by
	 * @param timestamp		returned values will have timestamps greater than this
	 * @return				an array of global IDs from rows updated since the given timestamp, matching the given properties
	 */
	public int[] getIDsWhereSince(String[] cols, String[] args, Date timestamp) {
		if(cols.length != args.length) {
			System.out.println("Number of columns must match the number of arguments!");
			return null;
		}
		
		String queryString = buildListQuery(cols, args) + " AND timestamp >= " + timestamp.getTime() + " ALLOW FILTERING;";
		
		Statement statement = new SimpleStatement(queryString);
		ResultSet results = session.execute(statement);
		
		return parseIDs(results);
	}
	
	private static int[] parseIDs(ResultSet results) {
		
		ArrayList<Integer> ids = new ArrayList<Integer>();
		
		for(Row row : results) {
			ids.add(row.getInt("global_id"));
		}
		
		int[] idList = new int[ids.size()];
		
		for(int i = 0; i < ids.size(); i++) {
			idList[i] = ids.get(i);
		}
		Arrays.sort(idList);
		return idList;
	}
	
	private String buildListQuery(String[] cols, String[] args) {
		String queryString = "SELECT global_id FROM sensor_details WHERE ";
		String addConditions = "";
		
		for(int i = 0; i < cols.length; i++) {
			addConditions = cols[i] + " = '" + args[i];
			if(i == cols.length - 1) {
				addConditions += "' ";
			} else {
				addConditions += "' AND ";
			}
			queryString += addConditions;
		}
		
		return queryString;
	}
	
	/* METHODS FOR RETRIEVING STRING META DATA BY GLOBAL*/

	/**
	 * Returns the arbitrary application label for the given sensor ID. An application label
	 * is given as a descriptor by the sensor's operator upon registration, and is neither
	 * unique nor constrained. May be null.
	 * 
	 * @param global_id		a sensor's global, unique identifier
	 * @return				the sensor's application label, or null if none.
	 */
	public String getApplication(int global_id) {
		Statement statement = new SimpleStatement("Select application FROM sensor_details WHERE global_id = " + global_id + ";");
		ResultSet results = session.execute(statement);
		return results.one().getString("application");
	}
	
	/**
	 * Returns an array comprised of the application label for each identified sensor. Never
	 * null for at least one valid global_id.
	 * 
	 * @param ids			an array of unique sensor global_ids
	 * @return				an array with the application label for each identified sensor
	 */
	public String[] getApplicationList(int[] ids) {
		return getStringList("application", ids);
	}
	
	/**
	 * Returns the 'measures' attribute for a given sensor ID. Never null for a valid
	 * global_id. The measures attribute describes a physical property, such as distance,
	 * temperature, light intensity, or wind speed.
	 * 
	 * @param global_id		a sensor's global, unique identifier
	 * @return				the property measured by the sensor
	 */
	public String getMeasures(int global_id) {
		Statement statement = new SimpleStatement("SELECT measures FROM sensor_details WHERE global_id = " + global_id + ";");
		ResultSet results = session.execute(statement);
		return results.one().getString("measures");
	}
	
	/**
	 * Returns an array comprised of the measures attribute for each identified sensor. Never
	 * null for at least one valid global_id.
	 * 
	 * @param ids			an array of unique sensor global_ids
	 * @return				an array with the measures attribute for each identified sensor
	 * @see #getMeasures(int)
	 */
	public String[] getMeasuresList(int[] ids) {
		return getStringList("measures", ids);
	}

	/**
	 * Returns the unit of measurement for the identified sensor. Never null for a vlid global_id.
	 * 
	 * @param global_id		a sensor's global, unique identifier
	 * @return				the unit of measurement for the identified sensor
	 */
	public String getUnit(int global_id) {
		Statement statement = new SimpleStatement("SELECT unit FROM sensor_details WHERE global_id = " + global_id + ";");
		ResultSet results = session.execute(statement);
		return results.one().getString("unit");
	}
	
	/**
	 * Returns an array comprised of the unit of measurement for each identified sensor. Never null
	 * for at least one valid global_id.
	 * 
	 * @param ids			an array of unique sensor IDS (global_ids)
	 * @return				an array with the unit of measurement for each identified sensor
	 */
	public String[] getUnitList(int[] ids) {
		return getStringList("unit", ids);
	}

	private String[] getStringList(String col, int[] ids) { 
		String[] strList = new String[ids.length];
		String query = "SELECT " + col + " FROM sensor_details WHERE global_id IN " + buildInList(ids) + ";";
		Statement statement = new SimpleStatement(query);
		ResultSet results = session.execute(statement);
		int i = 0;
		for(Row row : results) {
			strList[i] = row.getString(col);
			i++;
		}
		return strList;
	}

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
	
	/* METHODS FOR RETIEVING POSITION DATA */
	
	/**
	 * Returns the latitude of the identified sensor as a double to six decimal places. Never null for
	 * a valid sensor_id.
	 * 
	 * @param global_id		a sensor's global, unique identifier
	 * @return				the sensor's latitude as a double to six decimal places
	 */
	public double getLat(int global_id) {
		Statement statement = new SimpleStatement("SELECT lat FROM sensor_details WHERE global_id = " + global_id + ";");
		ResultSet results = session.execute(statement);
		return results.one().getDouble("lat");
	}
	
	/**
	 * Returns the latitude of each identified sensor as an element in an array of doubles. Never null for
	 * a valid sensor_id.
	 * 
	 * @param ids			an array of unique sensor IDs (global_ids)
	 * @return				the latitude of each identified sensor as an array of doubles
	 * @see #getLat(int)
	 */
	public double[] getLatList(int[] ids) {
		return getDoubleList("lat", ids);
	}
	
	/**
	 * Returns the longitude of the identified sensor as a double to six decimal places. Never null for
	 * a valid sensor_id.
	 * 
	 * @param global_id		a sensor's global, unique identifier
	 * @return				the sensor's longitude as a double to six decimal places
	 */
	public double getLng(int global_id) {
		Statement statement = new SimpleStatement("SELECT lng FROM sensor_details WHERE global_id = " + global_id + ";");
		ResultSet results = session.execute(statement);
		return results.one().getDouble("lng");
	}
	
	/**
	 * Returns the longitude of each identified sensor as an element in an array of doubles. Never null for
	 * a valid sensor_id.
	 * 
	 * @param ids			an array of unique sensor IDs (global_ids)
	 * @return				the longitude for each identified sensor
	 * @see #getLng(int)
	 */
	public double[] getLngList(int[] ids) {
		return getDoubleList("lng", ids);
	}
	
	/**
	 * Returns the Position of the identified sensor, containing the global_id with its latitude and longitude.
	 * Include com.orange.api.util.Position. Never null for a valid global_id.
	 * 
	 * @param global_id		a sensor's global, unique identifier
	 * @return				the Position describing the named sensor's location
	 * @see Position		
	 */
	public Position getPosition(int global_id) { 
		double lat;
		double lng;
		Statement statement = new SimpleStatement("SELECT lat, lng FROM sensor_details WHERE global_id = " + global_id + ";");
		ResultSet results = session.execute(statement);
		Row row = results.one();
		lat = row.getDouble("lat");
		lng = row.getDouble("lng");
		return new Position(global_id, lat, lng);
	}
	
	/**
	 * Returns the Positions of the identified sensors as an array.
	 * Include com.orange.api.util.Position. Never null for a valid global_id.
	 * 
	 * @param ids			an array of unique sensor IDs (global_ids)
	 * @return				a Position array describing each named sensor's location
	 * @see Position		
	 */
	public Position[] getPositionList(int[] ids) { 
		
		Position[] posList = new Position[ids.length];
		String query = "SELECT lat, lng FROM sensor_details WHERE global_id IN " + buildInList(ids) + ";";
		Statement statement = new SimpleStatement(query);
		ResultSet results = session.execute(statement);
		int i = 0;
		for(Row row : results) {
			Position pos = new Position(ids[i], row.getDouble("lat"), row.getDouble("lng"));
			posList[i] = pos;
			i++;
		}
		return posList;
	}
	
	private double[] getDoubleList(String col, int[] ids) { 
		double[] dblList = new double[ids.length];
		String query = "SELECT " + col + " FROM sensor_details WHERE global_id IN " + buildInList(ids) + ";";
		Statement statement = new SimpleStatement(query);
		ResultSet results = session.execute(statement);
		int i = 0;
		for(Row row : results) {
			dblList[i] = row.getDouble(col);
			i++;
		}
		return dblList;
	}
	
	/* METHODS FOR DEALING WITH READING DATA */
	
	/**
	 * Returns a list of SensorData objects containing the latest reading and timestamp for each
	 * named sensor ID.
	 * 
	 * @param ids	an array of unique sensor IDs (global_ids)
	 * @return		A list of SensorData objects containing the latest reading and timestamp for each named sensor ID
	 */
	public SensorData[] getLatestList(int[] ids) {
		
		SensorData[] dataList = new SensorData[ids.length];
		
		String query = "SELECT reading, timestamp FROM sensor_details WHERE global_id IN " + buildInList(ids) + ";";
		Statement statement = new SimpleStatement(query);
		ResultSet results = session.execute(statement);
		int i = 0;
		for(Row row : results) {
			SensorData data = new SensorData(ids[i], row.getFloat("reading"), row.getDate("timestamp"));
			dataList[i] = data;
			i++;
		}
		return dataList;
	}
	
	
	/**
	 * Returns the timestamp for the very first reading received from the identified sensor.
	 * Never null for a valid global_id.
	 * 
	 * @param global_id		a sensor's global, unique identifier
	 * @return				the timestamp for the named sensor's first reading
	 */
	public Date getDateAdded(int global_id) {
		Statement statement = new SimpleStatement("SELECT first_added FROM sensor_details WHERE global_id = " + global_id + ";");
		ResultSet results = session.execute(statement);
		return results.one().getDate("first_added");
	}
	
	/**
	 * Returns a Date array containing the timestamp for each identified sensor's very first received reading.
	 * Never null for a valid global_id.
	 * 
	 * @param ids			an array of unique sensor IDs (global_ids)
	 * @return				a Date array describing each named sensors' first reading's timestamp
	 */
	public Date[] getDateAddedList(int[] ids) {
		return getDateList("first_added", ids);
	}
	
	/**
	 * Returns the most recently received reading from a given sensor. Never null for
	 * a valid global_id.
	 * 
	 * @param global_id		a sensor's global, unique identifier
	 * @return				the latest reading from this sensor
	 */
	public float getLastReading(int global_id) {
		Statement statement = new SimpleStatement("SELECT reading FROM sensor_details WHERE global_id = " + global_id + ";");
		ResultSet results = session.execute(statement);
		return results.one().getFloat("reading");
	}
	
	/**
	 * Returns an array composed of the most recently received reading from each sensor
	 * identified in ids. Never null for at least one valid global_id.
	 * 
	 * @param ids			an array of unique sensor IDs (global_id)
	 * @return				an array composed of each named sensor's latest reading
	 */
	public float[] getLastReadingList(int[] ids) {
		float[] readingList = new float[ids.length];
		String query = "SELECT reading FROM sensor_details WHERE global_id IN " + buildInList(ids) + ";";
		Statement statement = new SimpleStatement(query);
		ResultSet results = session.execute(statement);
		int i = 0;
		for(Row row : results) {
			readingList[i] = row.getFloat("reading");
			i++;
		}
		return readingList;
	}
	
	/**
	 * Returns the timestamp for the most recently received reading from a given sensor.
	 * Never null for a valid global_id.
	 * 
	 * @param global_id		a sensor's global, unique identifier
	 * @return				the timestamp for the named sensor's most recent reading
	 */
	public Date getLastTimestamp(int global_id) {
		Statement statement = new SimpleStatement("SELECT timestamp FROM sensor_details WHERE global_id = " + global_id + ";");
		ResultSet results = session.execute(statement);
		return results.one().getDate("timestamp");
	}
	
	/**
	 * Returns an array composed of the timestamp for each identified sensor's latest reading.
	 * Never null for at least one valid global_id.
	 * 
	 * @param ids			an array of unique sensor ID (global_id)
	 * @return				an array of timestamps for each named sensor's latest reading
	 */
	public Date[] getLastTimestampList(int[] ids) {
		return getDateList("timestamp", ids);
	}
	
	/**
	 * Returns an array of SensorData types, each describing one single reading update
	 * sent by the named sensor by both reading and timestamp.
	 * 
	 * @param global_id		a sensor's global, unique identifier
	 * @param time			an earlier date or time from which all reading updates from the named sensor will be retrieved
	 * @return				an array of SenorData objects describing a distinct timestamp and reading value for the named sensor
	 * @see					SensorData
	 */
	public SensorData[] getDataSince(int global_id, Date time) { 
		String query = "SELECT reading, timestamp FROM data_archive WHERE global_id = " + global_id + " AND timestamp > " + time.getTime() + ";";
		return getDataRange(global_id, query);
	}
	
	/**
	 * Returns an array of SensorData types, each describing one single reading update
	 * sent by the named sensor by both reading and timestamp.
	 * 
	 * @param global_id		a sensor's global, unique identifier
	 * @param start			the date from which data will be retrieved
	 * @param end			the date to which data will be retrieved
	 * @return				an array of SensorData objects describing a distinct timestamp and reading value for the named sensor.
	 * @see					SensorData
	 */
	public SensorData[] getDataBetween(int global_id, Date start, Date end) { 
		String query = "SELECT reading, timestamp FROM data_archive WHERE global_id = " + global_id + " AND timestamp > " + start.getTime() + " AND timestamp < " + end.getTime() + ";";
		return getDataRange(global_id, query);
	}

	private SensorData[] getDataRange(int id, String query) { 
		ArrayList<SensorData> dataArrayList = new ArrayList<SensorData>();
		Statement statement = new SimpleStatement(query);
		ResultSet results = session.execute(statement);
		for(Row row : results) {
			SensorData data = new SensorData(id, row.getFloat("reading"), row.getDate("timestamp"));
			dataArrayList.add(data);
		}
		
		SensorData[] dataList = new SensorData[dataArrayList.size()];
		
		for(int i = 0; i < dataArrayList.size(); i++) {
			dataList[i] = dataArrayList.get(i);
		}
		
		return dataList;
	}

	private Date[] getDateList(String col, int[] ids) { 
		Date[] dateList = new Date[ids.length];
		String query = "SELECT " + col + " FROM sensor_details WHERE global_id IN " + buildInList(ids) + ";";
		Statement statement = new SimpleStatement(query);
		ResultSet results = session.execute(statement);
		int i = 0;
		for(Row row : results) {
			dateList[i] = row.getDate(col);
			i++;
		}
		return dateList;
	}
}
