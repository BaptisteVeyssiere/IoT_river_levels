package jsplink;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.*;



@WebServlet("/subscribe")
public class Subscribe extends HttpServlet {
    private final String USER_AGENT = "Mozilla/5.0";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        DataSource dataSource = CustomDataSource.getInstance();
        QueryRunner run = new QueryRunner(dataSource);
        ResultSetHandler<DBSubscriber> level_results = new BeanHandler<DBSubscriber>(DBSubscriber.class);

        PrintWriter out = new PrintWriter(response.getWriter());
        String postcode = URLEncoder.encode(request.getParameter("postcode"),"UTF-8");
        if (postcode == new String()) {

            response.setContentType("application/json");
            // Get the printwriter object from response to write the required json object to
            // the output stream
            JSONObject json2 = new JSONObject();
            json2.put("form", "<font color='red'>Postcode cannot be empty</font>");
            // Assuming your json object is **jsonObject**, perform the following, it will
            // return your json object
            out.print(json2);
            return;
        }
        Pattern pattern = Pattern.compile("\\s");
        Matcher matcher = pattern.matcher(postcode);
        boolean found = matcher.find();

        if (found) {
            response.setContentType("application/json");
            // Get the printwriter object from response to write the required json object to
            // the output stream
            JSONObject json2 = new JSONObject();
            json2.put("form", "<font color='red'>Postcode cannot contain spaces</font>");
            // Assuming your json object is **jsonObject**, perform the following, it will
            // return your json object
            out.print(json2);
            return;
        }
        String number = URLEncoder.encode(request.getParameter("number"),"UTF-8");
        if (number == new String()) {
            response.setContentType("application/json");
            // Get the printwriter object from response to write the required json object to
            // the output stream
            JSONObject json2 = new JSONObject();
            json2.put("form", "<font color='red'>Postcode cannot contain spaces</font>");
            // Assuming your json object is **jsonObject**, perform the following, it will
            // return your json object
            out.print(json2);
            return;
        }
    try {
        URL url = new URL("https://api.postcodes.io/postcodes/" + postcode);

        StringBuilder result = new StringBuilder();

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        String data = result.toString();
        rd.close();
        float latitude = 0;
        float longitude = 0;
        //print result

        JSONObject obj = new JSONObject(data);
        JSONObject jsonObject = obj.getJSONObject("result");


        latitude = jsonObject.getFloat("latitude");
        longitude = jsonObject.getFloat("longitude");

        run.update("replace into subscriber values ( ?,  ?,?)",number,latitude,longitude);

        response.setContentType("application/json");
        // Get the printwriter object from response to write the required json object to
        // the output stream
        JSONObject json2 = new JSONObject();
        json2.put("form", "number updated");
        // Assuming your json object is **jsonObject**, perform the following, it will
        // return your json object
        out.print(json2);
        return;

    } catch (Exception ex) {
        ex.printStackTrace(out);
    }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

}

