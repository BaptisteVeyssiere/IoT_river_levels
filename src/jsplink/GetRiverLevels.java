package jsplink;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.sql.Timestamp;

import javax.sql.DataSource;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

public class GetRiverLevels {
	
	DataSource dataSource = CustomDataSource.getInstance();
	QueryRunner run = new QueryRunner(dataSource);
	ResultSetHandler<DBLevels> level_results = new BeanHandler<DBLevels>(DBLevels.class);

public List<DBLevels> getLevels(int timescale, String station_id) {
	Timestamp timestamp = new Timestamp(System.currentTimeMillis());
	
	List<DBLevels> db_levels = null;
	try {
		db_levels = (List<DBLevels>) run.query(
				"select * from levels where station_id = ? and timestamp >= DATE_SUB(NOW(),interval ? day) order by timestamp asc", new BeanListHandler(DBLevels.class),
				station_id, timescale);
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	
	return db_levels;
}

public List<DBStations> getStations() {
	List<DBStations>  stations = new ArrayList<DBStations>();
	List<DBStations> stations_list = null;
	try {
		 stations_list = (List<DBStations>) run.query(
				"select * from monitoring_stations", new BeanListHandler(DBStations.class));
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	for (int i =  0;  i < stations_list.size(); i++) {
		stations.add(stations_list.get(i));
	}
	return stations;
}
public void panic(String station_id, Timestamp timestamp, String polygon) {
if (polygon != null) {
        
		try {
			run.update("update monitoring_stations set flood_timestamp = ?, flood_polygon= ?, flood_warning = 1 where station_id = ? ",timestamp,polygon,station_id);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
}
