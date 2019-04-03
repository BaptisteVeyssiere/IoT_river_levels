<%@ page import="org.apache.commons.dbutils.QueryRunner" %>
<%@ page import="jsplink.CustomDataSource" %>
<%@ page import="javax.sql.DataSource" %>
<%@ page import="org.apache.commons.dbutils.handlers.BeanListHandler" %>
<%@ page import="java.util.List" %>
<%@ page import="org.apache.commons.dbutils.ResultSetHandler" %>
<%@ page import="jsplink.DBFloodWarnings" %>
<%@ page import="java.util.ListIterator" %>
<%@ page import="com.google.gson.*" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.TimeZone" %>
<%
    DataSource dataSource = CustomDataSource.getInstance();
    QueryRunner run = new QueryRunner(dataSource);
    String timestamp = request.getParameter("timestamp");

    Date date = new Date(Long.parseLong(timestamp));
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    timestamp = sdf.format(date);
//    sdf.setTimeZone(TimeZone.getTimeZone("Europe/London"));
    try {
//        timestamp = sdf.format(sdf.parse(sdf.format(date)));
        timestamp = sdf.format(date);
    } catch (Exception e) {
        e.printStackTrace();
    }

    ResultSetHandler<List<DBFloodWarnings>> resultSetHandler_floods = new BeanListHandler<>(DBFloodWarnings.class);
    try {
        List<DBFloodWarnings> flood_warnings = run.query("SELECT * FROM flood_warnings WHERE timestamp > DATE_SUB(\"" + timestamp + "\", INTERVAL 1 HOUR) AND timestamp <= \"" + timestamp + "\"", resultSetHandler_floods);
        ListIterator<DBFloodWarnings> iterator = flood_warnings.listIterator();
        while (iterator.hasNext()){
            DBFloodWarnings warning = iterator.next();

            StringBuilder formatted_polygon = new StringBuilder("[");
            String raw_polygon = warning.getFlood_polygon();
            JsonParser jsonParser = new JsonParser();
            JsonArray json = (JsonArray) jsonParser.parse(raw_polygon);
            for (JsonElement elem : json) {
                formatted_polygon.append("{\"lat\":").append(elem.getAsJsonArray().get(1).getAsDouble()).append(", \"lng\": ").append(elem.getAsJsonArray().get(0).getAsDouble()).append("},");
            }
            formatted_polygon.setLength(formatted_polygon.length() - 1);
            formatted_polygon.append("]");

            warning.setFlood_polygon(formatted_polygon.toString());
            iterator.set(warning);
        }
        Gson gson = new Gson();
        String json = gson.toJson(flood_warnings);
%>
<%=json%>
<%
    } catch (Exception e) {
        e.printStackTrace();
    }
%>