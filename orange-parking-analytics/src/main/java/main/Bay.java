package main;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Observable;

public class Bay extends Observable {

	private long id;
	private long sensorId;
	private Restriction restriction;
	private boolean occupied;
	private Timestamp state;
	private Time averageDailyAvailability;
	
	public Bay(long id, long sensorId, Restriction restriction, boolean occupied,
			Timestamp state, Time averageDailyAvailability) {
		this.id = id;
		this.sensorId = sensorId;
		this.restriction = restriction;
		this.occupied = occupied;
		this.state = state;
		this.averageDailyAvailability = averageDailyAvailability;
	}

	public long getId() {
		return id;
	}

	public long getSensorId() {
		return sensorId;
	}

	public Restriction getRestriction() {
		return restriction;
	}

	public void setRestriction(Restriction restriction) {
		this.restriction = restriction;
	}

	public boolean isOccupied() {
		return occupied;
	}

	public void setOccupied(boolean occupied) {
		if (this.occupied != occupied) {
			this.occupied = occupied;
			this.state = new Timestamp(new java.util.Date().getTime());
			this.setChanged();
			this.notifyObservers();
		}
	}

	public Timestamp getState() {
		return state;
	}

	public Time getAverageDailyAvailability() {
		return averageDailyAvailability;
	}

	public void setAverageDailyAvailability(Time averageDailyAvailability) {
		this.averageDailyAvailability = averageDailyAvailability;
	}
	
}
