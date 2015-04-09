package com.orange.api.util;
/**
 * 
 * @author Gulliver Johnson
 * 
 */
public class Position {
	private int global_id;
	private double lat;
	private double lng;
	
	/**
	 * Stores a sensor's unique id, latitude, and longitude.
	 * 
	 * @param global_id	a sensor's global_id
	 * @param lat		the sensor's latitude
	 * @param lng		the sensor's longitude
	 */
	public Position(int global_id, double lat, double lng) {
		this.global_id = global_id;
		this.lat = lat;
		this.lng = lng;
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
	 * @return the sensor's latitude
	 */
	public double getLat() {
		return lat;
	}
	
	/**
	 * 
	 * @return the sensors longitude
	 */
	public double getLng() {
		return lng;
	}
	
}


