<%@ page import="org.apache.commons.dbutils.QueryRunner" %>
<%@ page import="jsplink.CustomDataSource" %>
<%@ page import="javax.sql.DataSource" %>
<%@ page import="org.apache.commons.dbutils.handlers.BeanListHandler" %>
<%@ page import="java.util.List" %>
<%@ page import="org.apache.commons.dbutils.ResultSetHandler" %>
<%@ page import="jsplink.DBFloodWarnings" %>
<%@ page import="java.util.ListIterator" %>
<%@ page import="com.google.gson.*" %>
<%
    DataSource dataSource = CustomDataSource.getInstance();
    QueryRunner run = new QueryRunner(dataSource);
    double latitude = Double.parseDouble(request.getParameter("lat"));
    double longitude = Double.parseDouble(request.getParameter("lng"));

    ResultSetHandler<List<DBFloodWarnings>> resultSetHandler_floods = new BeanListHandler<>(DBFloodWarnings.class);
    try {
        List<DBFloodWarnings> flood_warnings = run.query("SELECT * FROM flood_warnings WHERE timestamp > TIME(DATE_SUB(NOW(), INTERVAL 1 HOUR))", resultSetHandler_floods);
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
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
    <meta name="viewport" content="initial-scale=1.0">
</head>
<body>
    <div id="result"></div>
    <div id="map" style="display: none"></div>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script>
        var map;
        var polygon_list = [];
        function isPolygonContaining(polygon, position) {
            return (google.maps.geometry.poly.containsLocation(position, polygon));
        }

        function drawPolygon(flood_area) {
            var polygon = new google.maps.Polygon({
                paths: flood_area
            });
            polygon_list.push(polygon);
        }

        function initMap() {
            var myLatLng = {lat: 51.296711, lng: 1.068117};

            map = new google.maps.Map(document.getElementById('map'), {
                center: myLatLng,
                zoom: 12
            });

            var warnings = <%=json%>;
            warnings.forEach(function (warning) {
                drawPolygon(JSON.parse(warning.flood_polygon));
            });
        }
        setTimeout(function() {
            var flooded = false;
            var position = new google.maps.LatLng({lat: <%=latitude%>, lng: <%=longitude%>});
            polygon_list.forEach(function (polygon) {
                if (isPolygonContaining(polygon, position)) {
                    flooded = true;
                }
            });
            $('#result').html(flooded.toString());
        }, 200);
    </script>
    <%
        } catch (Exception e) {
            e.printStackTrace();
        }
    %>
    <script async defer src="https://maps.googleapis.com/maps/api/js?key=AIzaSyDXZmuEUWidttMyx2OJOtNy6Vha8WGA47o&callback=initMap"></script>
</body>
</html>