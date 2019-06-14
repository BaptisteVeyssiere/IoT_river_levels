<%@ page import="javax.sql.DataSource" %>
<%@ page import="jsplink.CustomDataSource" %>
<%@ page import="org.apache.commons.dbutils.QueryRunner" %>
<%@ page import="jsplink.DBStations" %>
<%@ page import="org.apache.commons.dbutils.ResultSetHandler" %>
<%@ page import="java.util.List" %>
<%@ page import="org.apache.commons.dbutils.handlers.BeanListHandler" %>
<%@ page import="java.sql.Timestamp" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="jsplink.DBLevels" %>

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
  <link href='http://ajax.googleapis.com/ajax/libs/jqueryui/1.8/themes/base/jquery-ui.css' rel='stylesheet' type='text/css'>
</head>
<body>
<div id="dashboard">
  <div id="map"></div>
  <div id="levels">
    <div id="selected"></div>
    <div id="slider"></div>
    <div id="date_view"></div>
    <div id="sensor_list">
      <div>
        <%
          ResultSetHandler<List<DBStations>> resultSetHandler_stations = new BeanListHandler<>(DBStations.class);
          try {
            List<DBStations> monitoring_stations = run.query("SELECT * FROM monitoring_stations", resultSetHandler_stations);
            for (DBStations station : monitoring_stations) {
        %>
        <a href="#"><%=station.getName()%></a>
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
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.11.1/jquery-ui.min.js"></script>
<script src="js/events.js"></script>
<script src="js/map.js"></script>
<script async defer src="https://maps.googleapis.com/maps/api/js?key=API_KEY&callback=initMap"></script>
</body>
</html>
