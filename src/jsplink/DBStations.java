package jsplink;

public class DBStations {
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
public int getFlood_warning() {
	return flood_warning;
}
public void setFlood_warning(int flood_warning) {
	this.flood_warning = flood_warning;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public String getFlood_timestamp() {
	return flood_timestamp;
}
public void setFlood_timestamp(String flood_timestamp) {
	this.flood_timestamp = flood_timestamp;
}
private String latitude;
private String longitude;
private int flood_warning;
private String name;
private String flood_timestamp;


}
