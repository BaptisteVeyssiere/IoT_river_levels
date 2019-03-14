<%@page import="java.sql.Timestamp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="javax.servlet.ServletException"%>
<%@ page import="javax.servlet.annotation.WebServlet"%>
<%@ page import="javax.servlet.http.HttpServletRequest"%>
<%@ page import="javax.servlet.http.HttpServletResponse"%>

<%@ page import="org.knowm.xchart.CategoryChart"%>
<%@ page import="org.knowm.xchart.CategoryChartBuilder"%>
<%@ page import="org.knowm.xchart.Histogram"%>
<%@ page import="org.knowm.xchart.SwingWrapper"%>
<%@ page import="org.knowm.xchart.style.Styler.LegendPosition"%>
<%@ page import="org.knowm.xchart.BitmapEncoder"%>
<%@ page import="org.knowm.xchart.BitmapEncoder.BitmapFormat"%>

<%@ page import="java.io.*"%>
<%@ page import="java.util.*"%>
<%@ page import="org.knowm.xchart.XYSeries"%>
<%@ page import="org.apache.commons.dbutils.QueryRunner"%>
<%@ page import="org.apache.commons.dbutils.ResultSetHandler"%>
<%@ page import="org.apache.commons.dbutils.handlers.BeanHandler"%>
<%@ page import="org.apache.commons.dbutils.handlers.BeanListHandler"%>
<%@ page import="javax.sql.DataSource"%>
<%@ page import="jsplink.*"%>
<%@ page import="javax.imageio.*"%>
<%@ page import="java.awt.image.BufferedImage"%>



<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>co838</title>
<script
  src="http://code.jquery.com/jquery-3.3.1.js"
  integrity="sha256-2Kok7MbOyxpgUVvAk/HJ2jigOSYS2auK4Pfzbm7uH60="
  crossorigin="anonymous"></script>
</head>
<body>
<%
GetRiverLevels river_levels_class = new GetRiverLevels();

int history_size = 1;
String base_station = "E3951";
boolean panic = false;
if (request.getParameter("days") != null) {
	if (! request.getParameter("days").matches("-?\\d+(\\.\\d+)?")) {
		out.write("Days is not an integer");
		return;
	}
	history_size = Integer.parseInt(request.getParameter("days"));
}
if (request.getParameter("station_id") != null) {
	
	base_station = request.getParameter("station_id");
}

if (request.getParameter("polygon") != null && request.getParameter("polygon") != new String()) {
	String polygon = request.getParameter("polygon");
	Timestamp timestamp = new Timestamp(System.currentTimeMillis());
	river_levels_class.panic(base_station, timestamp, polygon);

}



DataSource dataSource = (DataSource) CustomDataSource.getInstance();
QueryRunner run = new QueryRunner();
ResultSetHandler<DBLevels> contributor_results = new BeanHandler<DBLevels>(DBLevels.class);

int numCharts = 1;
// Create Chart
CategoryChart chart = new CategoryChartBuilder().width(800).height(600).title("River levels").xAxisTitle("Mean").yAxisTitle("Count").build();

// Customize Chart
chart.getStyler().setLegendPosition(LegendPosition.InsideNW);
chart.getStyler().setAvailableSpaceFill(.96);
chart.getStyler().setLegendVisible(true);


List<DBLevels> river_levels_list = river_levels_class.getLevels(history_size,base_station);
List<String> xAxis = new ArrayList<String>();
List<Double> yAxis = new ArrayList<Double>();
for (int i = 0; i < river_levels_list.size(); i ++) {
	xAxis.add(river_levels_list.get(i).getTimestamp());
	yAxis.add(river_levels_list.get(i).getLevel());
}


 
// Series
chart.addSeries("River levels from previous days", xAxis,yAxis);
byte[] img = BitmapEncoder.getBitmapBytes(chart, BitmapFormat.PNG);

byte[] encodeBase64 = Base64.getEncoder().encode(img);
String encoded = new String(encodeBase64, "UTF-8");
out.println("<img src='data:image/png;base64," + encoded + "'><br>");

out.write("Days of previous data to view <input type='text' name='history_size' id='days'><br>");
out.write("<input type=\"checkbox\" id=\"panic\" value=\"Panic\">Panic?<br>");
out.write("Flood polygon <input type='text' id='polygon'><br>");
		

out.write("List by base station<br>");

List<DBStations> stations_list = river_levels_class.getStations();
for(int i = 0; i < stations_list.size(); i++) {
	out.write("<button onclick=\"myFunction(this.id)\" id='" + stations_list.get(i).getStation_id() + "'>" + stations_list.get(i).getStation_id() + "</button>");
	if (stations_list.get(i).getFlood_warning() == 1) {
		out.write("<font color='red'>Flood warning</font><br>");
	}
	
}
%>
<script>
function myFunction(the_id) {
var days = $("#days").val();
var polygon = $("#polygon").val();
if ($("#panic").prop('checked')== false){ 
	polygon = "";
}
$.ajax({
    type: "POST",
    url: "#",
    data: {"station_id" : the_id, "days" : days, "polygon" : polygon},
    dataType: "json",
    success: function(data, textStatus) {
        
    }
});
}

</script>
</body>
</html>