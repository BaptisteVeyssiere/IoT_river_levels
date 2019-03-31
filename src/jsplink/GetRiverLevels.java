package jsplink;

import java.sql.SQLException;
import java.util.ArrayList;
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

public List<DBKent_mbed> getStations() {
	List<DBKent_mbed>  stations = new ArrayList<DBKent_mbed>();
	List<DBKent_mbed> stations_list = null;
	try {
		 stations_list = (List<DBKent_mbed>) run.query(
				"select * from monitoring_stations", new BeanListHandler(DBKent_mbed.class));
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
			run.update("insert into flood_warnings values (?, 5, ?, NULL,1) ",timestamp,"[[1.046327,51.303922],[1.099886,51.304673],[1.104521,51.265056],[1.049932,51.264411]]");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.print(e.getMessage());
		}
	}
}

	public void dontpanic() {
		try {
			run.update("delete from flood_warnings where panic = 1");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
