package com.flood.monitoring;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;


public class UrlToJson {

    private int timeout = 15000;
    private String method = "GET";

    public UrlToJson() {
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getMethod() {
        return this.method;
    }

    private String getResponse(InputStream inputStream) {
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(inputStream));
        StringBuffer response = new StringBuffer();
        String line;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                response.append(line);
            }
            bufferedReader.close();
        } catch (IOException ex) {
            System.out.println(ex.toString());
            response = null;
        }
        return ((response != null) ? response.toString() : null);
    }

    public JSONObject getJson(String strUrl) {
        try {
            URL url = new URL(strUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);
            connection.setConnectTimeout(timeout);
            connection.setReadTimeout(timeout);
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK)
                return null;
            String jsonString = getResponse(connection.getInputStream());
            connection.disconnect();
            return ((jsonString != null) ? new JSONObject(jsonString) : null);
        } catch (IOException | JSONException ex) {
            System.out.println(ex.toString());
        }
        return null;
    }

}

