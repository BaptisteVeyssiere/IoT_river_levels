package jsplink;

public class DBKent_mbed {
private String station_id;
public String getStation_id() {
	return station_id;
}
public void setStation_id(String station_id) {
	this.station_id = station_id;
}
public String getLatitude() {
	return latitude;
}
public void setLatitude(String latitude) {
	this.latitude = latitude;
}
public String getLongitude() {
	return longitude;
}
public void setLongitude(String longitude) {
	this.longitude = longitude;
}
public int getWarning() {
	return warning;
}
public void setWarning(int warning) {
	this.warning = warning;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public String getTimestamp() {
	return timestamp;
}
public void setTimestamp(String timestamp) {
	this.timestamp = timestamp;
}
private String latitude;
private String longitude;
private int warning;
private String name;
private String timestamp;


	public int getRiver_levels() {
		return river_levels;
	}

	public void setRiver_levels(int river_levels) {
		this.river_levels = river_levels;
	}

	private int river_levels;


}
