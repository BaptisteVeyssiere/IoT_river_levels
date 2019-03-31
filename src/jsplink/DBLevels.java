package jsplink;

public class DBLevels {
	
	private String timestamp;
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public double getLevel() {
		return level;
	}
	public void setLevel(double level) {
		this.level = level;
	}
	private double level;
	private String station_id;

	public int getFlood_warning() {
		return flood_warning;
	}

	public void setFlood_warning(int flood_warning) {
		this.flood_warning = flood_warning;
	}

	private int flood_warning;
	public String getStation_id() {
		return station_id;
	}
	public void setStation_id(String station_id) {
		this.station_id = station_id;
	}
	
	
	
}
