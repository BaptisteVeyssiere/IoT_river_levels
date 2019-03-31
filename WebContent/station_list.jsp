<%@ page import="org.apache.commons.dbutils.QueryRunner" %>
<%@ page import="jsplink.CustomDataSource" %>
<%@ page import="javax.sql.DataSource" %>
<%@ page import="jsplink.DBStations" %>
<%@ page import="org.apache.commons.dbutils.handlers.BeanListHandler" %>
<%@ page import="java.util.List" %>
<%@ page import="org.apache.commons.dbutils.ResultSetHandler" %>
<%@ page import="com.google.gson.Gson" %>
<%
    DataSource dataSource = CustomDataSource.getInstance();
    QueryRunner run = new QueryRunner(dataSource);

    ResultSetHandler<List<DBStations>> resultSetHandler_station = new BeanListHandler<>(DBStations.class);
    try {
        List<DBStations> stations = run.query("SELECT * FROM monitoring_stations", resultSetHandler_station);
        Gson gson = new Gson();
        String json = gson.toJson(stations);
%>
<%=json%>
<%
    } catch (Exception e) {
        e.printStackTrace();
    }
%>