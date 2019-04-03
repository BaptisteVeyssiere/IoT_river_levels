package com.flood.collector;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class DBHelper {

    private String dbUrl;
    private String dbUser;
    private String dbPassword;
    private Connection connection;


    public DBHelper(String url, String user, String password) {
        dbUrl = url;
        dbUser = user;
        dbPassword = password;
    }

    public void connect() {
        try {
            connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
        } catch (SQLException ex) {
            System.err.println(ex.toString());
            // TODO rethrow an exception
        }
    }

    public List<String> getStationList() {
        String query = "SELECT station_id from monitoring_stations";
        List<String> stationList = new LinkedList<String>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                String stationId = resultSet.getString("station_id");
                stationList.add(stationId);
            }
        } catch (SQLException ex) {
            System.err.println(ex.toString());
            // TODO rethrow an exception
        }
        return stationList;
    }

    public void insertStationData(String stationId, double lat, double lon, String name, String type) {
        String sqlQuery = "INSERT INTO monitoring_stations (station_id, latitude, longitude, name, type) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1, stationId);
            preparedStatement.setDouble(2, lat);
            preparedStatement.setDouble(3, lon);
            preparedStatement.setString(4, name);
            preparedStatement.setString(5, type);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            System.err.println(ex.toString());
            // TODO rethrow an exception
        }
    }

    public void insertLevelData(String stationId, long time, double level, int floodWarning, String error) {
        String sqlQuery;
        if (error == null) {
            sqlQuery = "INSERT INTO levels (station_id, timestamp, level, flood_warning) VALUES (?, ?, ?, ?)";
        } else {
            sqlQuery = "INSERT INTO levels (station_id, error_type) VALUES (?, ?)";
        }
        Timestamp timestamp = new Timestamp(time);
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            if (error == null) {
                preparedStatement.setString(1, stationId);
                preparedStatement.setTimestamp(2, timestamp);
                preparedStatement.setDouble(3, level);
                preparedStatement.setInt(4, floodWarning);
            } else {
                preparedStatement.setString(1, stationId);
                preparedStatement.setString(2, error);
            }
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            System.err.println(ex.toString());
            // TODO rethrow an exception
        }
    }

    public void insertWarningData(String id, int severity, long time, String polygon) {
        String query = "INSERT INTO flood_warnings (id, flood_severity, timestamp, flood_polygon) VALUES (?, ?, ?, ?)";
        Timestamp timestamp = new Timestamp(time);
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, id);
            preparedStatement.setInt(2, severity);
            preparedStatement.setTimestamp(3, timestamp);
            preparedStatement.setString(4, polygon);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            System.err.println(ex.toString());
            // TODO rethrow an exception
        }
    }

    public void disconnect() {
        try {
            connection.close();
        } catch (SQLException ex) {
            System.err.println(ex.toString());
            // TODO rethrow an exception
        }
    }

}
