package com.orange.api.util;

import java.util.Date;
/**
 * 
 * @author Gulliver Johnson
 *
 */
public class SensorData {

	private int global_id;
	private float reading;
	private Date timestamp;
	
	/**
	 * Stores a row of a given sensor's data history
	 * 
	 * @param global_id		a sensor's global_id
	 * @param reading		that sensor's reading
	 * @param timestamp		that reading's upload timestamp
	 */
	public SensorData(int global_id, float reading, Date timestamp) {
		this.global_id = global_id;
		this.reading = reading;
		this.timestamp = timestamp;
	}
	
	/**
	 * 
	 * @return the sensor's global_id
	 */
	public int getID() {
		return global_id;
	}
	
	/**
	 * 
	 * @return the sensor's global_id
	 */
	public float getReading() {
		return reading;
	}
	
	/**
	 * 
	 * @return the sensor's global_id
	 */
	public Date getTimestamp() {
		return timestamp;
	}
	
}
