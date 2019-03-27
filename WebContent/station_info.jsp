<%@ page import="javax.sql.DataSource" %>
<%@ page import="jsplink.CustomDataSource" %>
<%@ page import="org.apache.commons.dbutils.QueryRunner" %>
<%@ page import="jsplink.DBLevels" %>
<%@ page import="org.apache.commons.dbutils.ResultSetHandler" %>
<%@ page import="org.apache.commons.dbutils.handlers.BeanListHandler" %>
<%@ page import="java.util.List" %>
<%@ page import="jsplink.DBStations" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    DataSource dataSource = CustomDataSource.getInstance();
    QueryRunner run = new QueryRunner(dataSource);
    String name = request.getParameter("name");
    String timestamp = request.getParameter("timestamp");

    ResultSetHandler<List<DBLevels>> resultSetHandler_level = new BeanListHandler<>(DBLevels.class);
    try {
        List<DBLevels> levels = run.query("SELECT * FROM levels WHERE timestamp LIKE " + timestamp + "AND name LIKE " + name, resultSetHandler_level);
        if (levels.size() > 0) {
            var level = levels.get(0);
            ResultSetHandler<List<DBStations>> resultSetHandler_station = new BeanListHandler<>(DBStations.class);
            List<DBStations> stations = run.query("SELECT * FROM monitoring_stations WHERE station_id == " + level.getStation_id(), resultSetHandler_station);
            if (stations.size() > 0) {
                var station = stations.get(0);
%>
<div class="sensor_name"><span style="font-weight: bold"><%=station.getName()%></span> sensor</div>
<div class="sensor_water_level">Water level: <span style="font-weight: bold"><%=level.getLevel()%>m</span></div>
<div class="sensor_level_indicator">Alert level: <span></span></div>
<div class="sensor_timestamp">At time: <%=timestamp%></div>
<div class="sensor_source">Source: Modif Environment Agency</div>
<%
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
%>