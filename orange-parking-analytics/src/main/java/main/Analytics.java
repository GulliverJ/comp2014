package main;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import com.orange.api.session.*;
import com.orange.api.util.SensorData;


public class Analytics {

	private class SensorReading {
		private Long id;
		private Float reading;
		private Date timestamp;
		
		public SensorReading(Long id, Float reading, Date timestamp) {
			this.id = id;
			this.reading = reading;
			this.timestamp = timestamp;
		}
		
		public void update(Float reading, Date timestamp) {
			this.reading = reading;
			this.timestamp = timestamp;
		}
	}
	
	private Map<Long, SensorReading> sensorsReadings;
	private Map<Long, Bay> bays;
	private int[] sensorsIds;
	
	public Analytics() {
		sensorsReadings = new HashMap<Long, SensorReading>();
		bays = new HashMap<Long, Bay>();
		
		for (Bay bay : Parking.getBays()) {
			bays.put(bay.getSensorId(), bay);
			
			final Bay bay2 = bay;
			
			bay.addObserver(new Observer() {

				public void update(Observable observable, Object object) {
					System.out.println("Update! bay_id=" + bay2.getId() + " sensor_id= " + bay2.getSensorId() + " is occupied to " + bay2.isOccupied());
					Parking.update(bay2);
				}
				
			});
		}
	}
	
	private void initialiseReadings() {
		OrangeConnection api = new OrangeConnection();
		SensorData[] sensorsData = api.getLatestList(sensorsIds);
		for (SensorData sensorData: sensorsData) {
			SensorReading sensorReading = new SensorReading((long) sensorData.getID(), sensorData.getReading(), sensorData.getTimestamp());
			this.sensorsReadings.put(sensorReading.id, sensorReading);
		}
		api.close();
	}
	
	public void run() {
		initialiseReadings();
		while (true) {
			try {
				Thread.sleep(30000);
			} catch (InterruptedException e) {
			}
			System.out.println("Starting reading");
			OrangeConnection api = new OrangeConnection();
			SensorData[] sensorsData = api.getLatestList(sensorsIds);
			for (SensorData sensorData : sensorsData) {				
				long sensorId = sensorData.getID();
				if (!sensorData.getTimestamp().equals(this.sensorsReadings.get(sensorId).timestamp)) {
					System.out.println("New timestamp...");
					this.sensorsReadings.get(sensorId).update(sensorData.getReading(), sensorData.getTimestamp());
					bays.get(sensorId).setOccupied(sensorData.getReading() < 70);
				}
			}
			api.close();
		}
		
		//---------------------data_analysis 
		
		//nearest_unoccupied_bay 
		/*
		 * I guess this would probably trawl through all currently occupied bays in bay_data.occupied 
		 *                                                   and   currently unoccupied bays 
		 *                                                   
		 * then performs some algorithm which based on lat and lng
		 * 					deduces what the shortest distance is 
		 * 					
		 * 					if(unoccupied_bay.id is closest to occupied_bay.id)
		 * 						then UPDATE nearest unoccupied bay with that unoccupied bay's global id (sensor id)
		 * 
		 * this will have to keep running to keep track of changes in occupied and unoccupied bays
		 * 		in order to recalculate the nearest unoccupied bays.
		 * 
		 * 
		 * 
		 */
		
		//ave_daily availability 
		/*
		 * 
		 * IGNORE for now
		 * 
		 * 
		 */
		
		
		
		
		
		
		
		
		/*
		 * for each sensor id and current reading
		 * 		check if the current reading is different than the previous
		 * 
		 */
		
	}

	// This main function will instantiate the threads
	public static void main(String[] args) {
		Analytics analytics = new Analytics();
		analytics.run();
	}
}
