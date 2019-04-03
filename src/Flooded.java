import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import jsplink.CustomDataSource;
import jsplink.DBFloodWarnings;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.min;
import static java.lang.StrictMath.max;

@WebServlet("/flooded")
public class Flooded extends HttpServlet {
    private boolean _pointOnVertex = true;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DataSource dataSource = CustomDataSource.getInstance();
        QueryRunner run = new QueryRunner(dataSource);
        double latitude = Double.parseDouble(request.getParameter("lat"));
        double longitude = Double.parseDouble(request.getParameter("lng"));

        PrintWriter out = response.getWriter();
        boolean isFlooded = false;

        ResultSetHandler<List<DBFloodWarnings>> resultSetHandler_floods = new BeanListHandler<>(DBFloodWarnings.class);
        try {
            ArrayList<double[]> polygon = new ArrayList<double[]>();
            List<DBFloodWarnings> flood_warnings = run.query("SELECT * FROM flood_warnings WHERE timestamp > TIME(DATE_SUB(NOW(), INTERVAL 1 HOUR))", resultSetHandler_floods);
            for (DBFloodWarnings warning : flood_warnings) {
                String raw_polygon = warning.getFlood_polygon();
                JsonParser jsonParser = new JsonParser();
                JsonArray json = (JsonArray) jsonParser.parse(raw_polygon);
                for (JsonElement elem : json) {
                    double[] point = {elem.getAsJsonArray().get(1).getAsDouble(), elem.getAsJsonArray().get(0).getAsDouble()};
                    polygon.add(point);
                }
                double[] point = {json.get(0).getAsJsonArray().get(1).getAsDouble(), json.get(0).getAsJsonArray().get(0).getAsDouble()};
                polygon.add(point);
                double[] position = {latitude, longitude};

                isFlooded = pointInPolygon(position, polygon, true) || isFlooded;
            }
            out.append(Boolean.toString(isFlooded));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean pointInPolygon(double[] point, ArrayList<double[]> polygon, boolean pointOnVertex) {
        _pointOnVertex = pointOnVertex;

        if (_pointOnVertex && pointOnVertex(point, polygon)) {
            return true;
        }

        int intersections = 0;
        int vertices_count = polygon.size();

        for (int i = 1; i < vertices_count; i++) {
            double[] vertex1 = polygon.get(i-1);
            double[] vertex2 = polygon.get(i);
            if (vertex1[1] == vertex2[1] && vertex1[1] == point[1] && point[0] > min(vertex1[0], vertex2[0]) && point[0] < max(vertex1[0], vertex2[0])) {
                return true;
            }
            if (point[1] > min(vertex1[1], vertex2[1]) && point[1] <= max(vertex1[1], vertex2[1]) && point[0] <= max(vertex1[0], vertex2[0]) && vertex1[1] != vertex2[1]) {
                double xinters = (point[1] - vertex1[1]) * (vertex2[0] - vertex1[0]) / (vertex2[1] - vertex1[1]) + vertex1[0];
                if (xinters == point[0]) {
                    return true;
                }
                if (vertex1[0] == vertex2[0] || point[0] <= xinters) {
                    intersections++;
                }
            }
        }
        return (intersections % 2) != 0;
    }

    private boolean pointOnVertex(double[] point, ArrayList<double[]> polygon) {
        for (double[] vertex : polygon) {
            if (point[0] == vertex[0] && point[1] == vertex[1]) {
                return true;
            }
        }
        return false;
    }
}
