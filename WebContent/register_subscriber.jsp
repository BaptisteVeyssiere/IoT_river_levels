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
  out.write("Subscribe to flood alerts<br>");
    out.write("Enter your phone number (without +44) <br>");
    out.write("<input type ='number' id='subscriber_number'><br>");
    out.write("Enter your postcode<br>");
    out.write("<input type ='text' id='postcode' ><br>");
    out.write("Add mbed device code to your address.<br>");
    out.write("<input type ='text' id='code' ><br>");
    out.write("<button onclick='myFunction()'>Submit</button><br>");
    out.write("<div id='status'></div>");
out.write("*note using a free twillio account, sms messages will not be sent where the number has not been verified at twillio");


%>
<script>

    function myFunction() {

        $.ajax({
            type: "POST",
            data: {"number" : $('#subscriber_number').val(), "postcode" : $('#postcode').val(),"code" : $('#code').val()},

            url: "http://129.12.44.32/rob/subscribe",
            dataType: "json",
            success: function(data, textStatus) {
                if (data.redirect) {
                    // data.redirect contains the string URL to redirect to
                    window.pauseAll=false;

                    window.location.href = data.redirect;
                }
                if (data.form){

                    // data.form contains the HTML for the replacement form
                    $("#status").html(data.form);
                }
            }
        });
    }




</script>
</body>
</html>