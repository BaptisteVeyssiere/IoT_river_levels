package jsplink;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@WebServlet(name = "Subscribe")
public class Subscribe extends HttpServlet {
    private final String USER_AGENT = "Mozilla/5.0";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String postcode = request.getParameter("postcode");
    String number = request.getParameter("number");


    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
    // HTTP GET request
    private String[] sendGet(String postcode) throws Exception {

        String url = "hapi.postcodes.io/postcodes/" + postcode;

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        String response_string = response.toString();
        JSONObject jsonObj = new JSONObject(response_string);

        JSONArray array_data = jsonObj.getJSONArray("result");

        for(int j=0; j<array_data.length(); j++) {
            JSONObject json = array_data.getJSONObject(j);
            if (json.getString("longitude") != null) {
                
            }
            }

    }
}

