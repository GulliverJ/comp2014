package main;

import java.sql.Time;

public class Restriction {

	private long id;
	private Time start;
	private Time end;
	private boolean weekendIncluded;
	private Time maximumStay;
	private double hourlyCharge;
	
	public Restriction(long id, Time start, Time end, boolean weekendIncluded,
			Time maximumStay, double hourlyCharge) {
		super();
		this.id = id;
		this.start = start;
		this.end = end;
		this.weekendIncluded = weekendIncluded;
		this.maximumStay = maximumStay;
		this.hourlyCharge = hourlyCharge;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public java.sql.Time getStart() {
		return start;
	}

	public void setStart(java.sql.Time start) {
		this.start = start;
	}

	public java.sql.Time getEnd() {
		return end;
	}

	public void setEnd(java.sql.Time end) {
		this.end = end;
	}

	public boolean isWeekendIncluded() {
		return weekendIncluded;
	}

	public void setWeekendIncluded(boolean weekendIncluded) {
		this.weekendIncluded = weekendIncluded;
	}

	public java.sql.Time getMaximumStay() {
		return maximumStay;
	}

	public void setMaximumStay(java.sql.Time maximumStay) {
		this.maximumStay = maximumStay;
	}

	public double getHourlyCharge() {
		return hourlyCharge;
	}

	public void setHourlyCharge(double hourlyCharge) {
		this.hourlyCharge = hourlyCharge;
	}
	
	
	
}
