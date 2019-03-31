<%@ page import="javax.sql.DataSource" %>
<%@ page import="jsplink.CustomDataSource" %>
<%@ page import="org.apache.commons.dbutils.QueryRunner" %>
<%@ page import="jsplink.DBLevels" %>
<%@ page import="org.apache.commons.dbutils.ResultSetHandler" %>
<%@ page import="org.apache.commons.dbutils.handlers.BeanListHandler" %>
<%@ page import="java.util.List" %>
<%@ page import="jsplink.DBStations" %>
<%@ page import="java.util.Date" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    DataSource dataSource = CustomDataSource.getInstance();
    QueryRunner run = new QueryRunner(dataSource);
    String name = request.getParameter("name");
    String timestamp = request.getParameter("timestamp");

    Date date = new java.util.Date(Long.parseLong(timestamp));
    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    timestamp = sdf.format(date);
    String[] warning_levels = {"low", "medium", "high", "veryhigh"};

    ResultSetHandler<List<DBStations>> resultSetHandler_station = new BeanListHandler<>(DBStations.class);
    try {
        List<DBStations> stations = run.query("SELECT * FROM monitoring_stations WHERE name LIKE \"" + name + "\"", resultSetHandler_station);
        if (stations.size() > 0) {
            DBStations station = stations.get(0);
            ResultSetHandler<List<DBLevels>> resultSetHandler_level = new BeanListHandler<>(DBLevels.class);
            List<DBLevels> levels = run.query("SELECT * FROM levels WHERE timestamp > TIME(DATE_SUB(\"" + timestamp + "\", INTERVAL 1 HOUR)) AND timestamp <= \"" + timestamp + "\" AND station_id LIKE \"" + station.getStation_id() + "\" ORDER BY timestamp DESC LIMIT 1", resultSetHandler_level);
            if (levels.size() > 0) {
                DBLevels level = levels.get(0);

%>
<div class="sensor_name"><span style="font-weight: bold"><%=station.getName()%></span> sensor</div>
<div class="sensor_water_level">Water level: <span style="font-weight: bold"><%=level.getLevel()%>m</span></div>
<div class="sensor_level_indicator">Alert level: <span class="<%=warning_levels[level.getFlood_warning()]%>"></span></div>
<div class="sensor_timestamp">At time: <%=level.getTimestamp()%></div>
<div class="sensor_source">Source: <%=station.getType()%></div>
<%
            } else {
%>
<div class="sensor_name"><span style="font-weight: bold"><%=station.getName()%></span> sensor</div>
<div class="sensor_water_level">Water level: <span style="font-weight: bold">No data</span></div>
<div class="sensor_level_indicator">Alert level: <span class="nodata"></span></div>
<div class="sensor_timestamp">At time: -</div>
<div class="sensor_source">Source: <%=station.getType()%></div>
<%
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
%>