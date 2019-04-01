package com.flood.monitoring;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class FloodDataCollector {

    // TODO set the url
    private String SQL_URL = "jdbc:mysql://localhost/co838";
    private String user = "ilyas";
    private String password = "portishead";
    private String MONITORING_STATION_URL = "https://environment.data.gov.uk/flood-monitoring/id/stations?riverName=Great+Stour";
    private String FLOOD_URL = "https://environment.data.gov.uk/flood-monitoring/id/floods";
    private String FLOOD_AREA_URL = "https://environment.data.gov.uk/flood-monitoring/id/floodAreas?riverOrSea=Great%20Stour,%20Stour";
    private String POLYGON_PREFIX_URL = "https://environment.data.gov.uk/flood-monitoring/id/floodAreas/";



    private UrlToJson urlToJson;


    public FloodDataCollector() {
        urlToJson = new UrlToJson();
    }

    private JSONObject getMeasureItem(JSONObject item) {

        Object measure = item.get("measures");
        if (measure instanceof JSONArray) {
            for (int i = 0; i < ((JSONArray) measure).length(); i++) {
                JSONObject obj = ((JSONArray) measure).getJSONObject(i);
                Object latestReading = obj.get("latestReading");
                if (latestReading instanceof JSONObject) {
                    if (((JSONObject) latestReading).has("value"))
                        return obj;
                }
            }
        } else if (measure instanceof JSONObject) {
            Object latestReading = ((JSONObject) measure).get("latestReading");
            if (latestReading instanceof JSONObject && ((JSONObject) latestReading).has("value")) {
                return (JSONObject) measure;
            }
        }
        return null;
    }

    private int getLevelWarning(JSONObject stationItem, double level) {
        JSONObject stageScale = stationItem.getJSONObject("stageScale");
        double low = stageScale.getDouble("typicalRangeLow");
        double high = stageScale.getDouble("typicalRangeHigh");
        return ((level < low) ? 0 : ((level > high) ? 3 : ((level + 0.3 > high) ? 2 : 1)));
    }

    private void collectLevelAndStation(DBHelper dbHelper, List<String> stationList, JSONObject station) {
        String error = null;
        // TODO maybe make some data verification to avoid unintended behaviour

        String stationUrl = station.getString("@id");
        JSONObject stationData = urlToJson.getJson(stationUrl);
        JSONObject stationItem = stationData.getJSONObject("items");
        String stationId = stationItem.getString("notation");
        double lat = stationItem.getDouble("lat");
        double lon = stationItem.getDouble("long");
        String name = stationItem.getString("label");
        double level = 0;
        int floodWarning = -1;
        JSONObject measures = getMeasureItem(stationItem);
        if (measures != null) {
            JSONObject lastMeasure = measures.getJSONObject("latestReading");
            level = lastMeasure.getDouble("value");
            floodWarning = getLevelWarning(stationItem, level);
        } else {
            error = "Unable to find level value";
        }
        long time = new Date().getTime();
        if (!stationList.contains(stationId)) {
            dbHelper.insertStationData(stationId, lat, lon, name, "ENV_API");
        }
        dbHelper.insertLevelData(stationId, time, level, floodWarning, error);
    }

    private String getPolygon(String floodArea) {
        JSONObject polygonObj = urlToJson.getJson(POLYGON_PREFIX_URL + floodArea + "/polygon");
        JSONArray features = polygonObj.getJSONArray("features");
        for (int i = 0; i < features.length(); i++) {
            JSONObject feature = features.getJSONObject(i);
            if (feature.has("geometry")) {
                JSONObject geometry = feature.getJSONObject("geometry");
                if (geometry.has("type") && geometry.getString("type").equals("Polygon") &&
                        geometry.has("coordinates")) {
                    JSONArray coordinates = geometry.getJSONArray("coordinates");
                    return coordinates.toString();
                }
            }
        }
        return null;
    }

    private void checkFloods(DBHelper dbHelper) {
        JSONObject floodData = urlToJson.getJson(FLOOD_URL);
        JSONArray floodItems = floodData.getJSONArray("items");
        if (!floodItems.isEmpty()) {
            JSONObject areaData = urlToJson.getJson(FLOOD_AREA_URL);
            JSONArray areaItems = areaData.getJSONArray("items");
            List<String> areaIdList = new LinkedList<>();
            for (int i = 0; i < areaItems.length(); i++) {
                JSONObject areaItem = areaItems.getJSONObject(i);
                areaIdList.add((areaItem.getString("notation")));
            }
            for (int i = 0; i < floodItems.length(); i++) {
                JSONObject floodItem = floodItems.getJSONObject(i);
                String floodArea = floodItem.getString("floodAreaID");
                if (areaIdList.contains(floodArea)) {
                    int severity = floodItem.getInt("severityLevel");
                    String polygon = getPolygon(floodArea);
                    long time = new Date().getTime();
                    dbHelper.insertWarningData(floodArea, severity, time, polygon);
                }
            }
        }
    }

    public void collectAndSaveData() {
        JSONObject data = urlToJson.getJson(MONITORING_STATION_URL);
        JSONArray items = data.getJSONArray("items");
        DBHelper dbHelper = new DBHelper(SQL_URL, user, password);
        dbHelper.connect();
        List<String> stationList = dbHelper.getStationList();
        for (int i = 0; i < items.length(); i++) {
            JSONObject station = items.getJSONObject(i);
            collectLevelAndStation(dbHelper, stationList, station);
        }
        checkFloods(dbHelper);
        dbHelper.disconnect();
    }
}
