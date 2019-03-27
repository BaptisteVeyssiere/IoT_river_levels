<%@ page import="javax.sql.DataSource" %>
<%@ page import="jsplink.CustomDataSource" %>
<%@ page import="org.apache.commons.dbutils.QueryRunner" %>
<%@ page import="jsplink.DBStations" %>
<%@ page import="org.apache.commons.dbutils.ResultSetHandler" %>
<%@ page import="org.apache.commons.dbutils.handlers.BeanHandler" %>
<%@ page import="java.util.List" %>
<%@ page import="org.apache.commons.dbutils.handlers.BeanListHandler" %>

<%
  DataSource dataSource = CustomDataSource.getInstance();
  QueryRunner run = new QueryRunner(dataSource);
%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Hello, I am a Java web app!</title>
  <meta name="viewport" content="initial-scale=1.0">
  <meta charset="utf-8">
  <link rel="stylesheet" href="css/dashboard.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
  <script src="js/map.js"></script>
  <script src="js/events.js"></script>
</head>
<body>
<div id="dashboard">
  <div id="map"></div>
  <div id="levels">
    <div id="selected" style="display: none">
      <div class="sensor_name"><span style="font-weight: bold">Name</span> sensor</div>
      <div class="sensor_water_level">Water level: <span style="font-weight: bold">0.75m</span></div>
      <div class="sensor_level_indicator">Alert level: <span></span></div>
      <div class="sensor_timestamp">At time: 12:40:00 26 Mar 2019</div>
      <div class="sensor_source">Source: Environment Agency</div>
    </div>
    <div class="dropdown">
      <button class="dropbtn">Pick a different time record</button>
      <div class="dropdown-content">
        <a href="#">Datetime 1</a>
        <a href="#">Datetime 2</a>
        <a href="#">Datetime 3</a>
      </div>
    </div>
    <div id="sensor_list">
      <div>
        <%
          ResultSetHandler<List<DBStations>> resultSetHandler = new BeanListHandler<>(DBStations.class);
          try {
            List<DBStations> monitoring_stations = run.query("SELECT * FROM monitoring_stations", resultSetHandler);
            for (DBStations station : monitoring_stations) {
        %>
        <a href="#"><%=station.getName()%></a>
        <script>addMarker(<%=station.getName()%>, <%=station.getLatitude()%>, <%=station.getLongitude()%>, "own")</script>
        <%
            }
          } catch (Exception e) {
            e.printStackTrace();
          }
        %>
      </div>
    </div>
  </div>
</div>
<div id="legend">
  <span>Legend</span>
  <ul>
    <li id="level_legend">
      High level
      <span></span>
      <span></span>
      <span></span>
      <span></span>
      Low level
    </li>
    <li id="no_data">
      <span></span>
      No data
    </li>
    <li id="our_sensor">
      <span></span>
      Our sensor
    </li>
    <li id="other_sensor">
      <span></span>
      Environment Agency sensor
    </li>
  </ul>
</div>
<script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyDXZmuEUWidttMyx2OJOtNy6Vha8WGA47o&callback=initMap" async defer></script>
</body>
</html>
