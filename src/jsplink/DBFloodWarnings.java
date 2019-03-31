package jsplink;

public class DBFloodWarnings {
	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public int getFlood_severity() {
		return flood_severity;
	}

	public void setFlood_severity(int flood_severity) {
		this.flood_severity = flood_severity;
	}

	public String getFlood_polygon() {
		return flood_polygon;
	}

	public void setFlood_polygon(String flood_polygon) {
		this.flood_polygon = flood_polygon;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	private String timestamp;
	private int flood_severity;
	private String flood_polygon;
	private String id;
}
