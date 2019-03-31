import jsplink.CustomDataSource;
import jsplink.DBDevices;
import jsplink.DBKent_mbed;
import jsplink.DBSubscriber;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import sun.awt.image.BadDepthException;

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
import java.util.List;
import java.util.Random;

@WebServlet("/mbed_floodwarning")
public class MBedWarning extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String mbed_id = request.getParameter("mac");
    String read_sensor = request.getParameter("read_sensor");
    PrintWriter out = response.getWriter();


        DataSource dataSource = CustomDataSource.getInstance();
        QueryRunner run = new QueryRunner(dataSource);
        ResultSetHandler<DBSubscriber> subscriber_results = new BeanHandler<DBSubscriber>(DBSubscriber.class);
        ResultSetHandler<DBDevices> device_results = new BeanHandler<DBDevices>(DBDevices.class);
        ResultSetHandler<DBKent_mbed> kent_mbed_results = new BeanHandler<DBKent_mbed>(DBKent_mbed.class);
        try {

            if (mbed_id != null) {
                DBDevices device = run.query("select * from devices  inner join subscriber on devices.code = subscriber.code where mbed_hex = ?", device_results, mbed_id);

                Random rnd = new Random();
                int number = rnd.nextInt(999999);

                // this will convert any number sequence into 6 character.
                String rnd_string = String.format("%06d", number);


                if (device == null) {
                    run.update("insert into devices values( ?,? )", mbed_id, rnd_string);
                    out.write("mbed code = " + rnd_string);

                } else {

                    String result  = getHTML("http://129.12.44.32/baptiste/flooded?lat=" + device.getLatitude() + "&lng=" + device.getLongitude());

                    if (result.equals("true")) {
                        out.write("flooded");
                    } else {
                        out.write("dry");
                    }


                }
            } else if (read_sensor != null) {

                DBKent_mbed  mbed = run.query("select * from levels where id = ? order by timestamp desc limit 1", kent_mbed_results, read_sensor);
                if (mbed == null) {
                    out.write("No data");
                } else {

                    out.write("Sensor " + mbed_id + " Level: " + mbed.getRiver_levels());
                }
            }

            

         } catch (Exception ex ) { System.out.println(ex.getMessage()); }

    }
    public static String getHTML(String urlToRead) throws Exception {
        StringBuilder result = new StringBuilder();
        URL url = new URL(urlToRead);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        rd.close();
        return result.toString();
    }
}
