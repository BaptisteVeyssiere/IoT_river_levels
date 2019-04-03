import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import jsplink.CustomDataSource;
import jsplink.DBDevices;
import jsplink.DBKent_mbed;
import jsplink.DBSubscriber;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@WebListener
public class check_flooded implements ServletContextListener {

    final String ACCOUNT_SID = "ACab0ad1496111e30888bde6ca609549ae";
    final String AUTH_TOKEN = "961fb10e3f6bd60a79f922cdf3f5dd89";

    @Override
    public final void contextInitialized(final ServletContextEvent sce) {
        flooded();
    }


    public void flooded() {
        ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
        ses.scheduleAtFixedRate(new Runnable() {
            public void run() {
                DataSource dataSource = CustomDataSource.getInstance();
                QueryRunner run = new QueryRunner(dataSource);
                ResultSetHandler<DBSubscriber> subscriber_results = new BeanHandler<DBSubscriber>(DBSubscriber.class);
                ResultSetHandler<DBDevices> device_results = new BeanHandler<DBDevices>(DBDevices.class);
                ResultSetHandler<DBKent_mbed> kent_mbed_results = new BeanHandler<DBKent_mbed>(DBKent_mbed.class);
                try {
                    List<DBSubscriber> subscribers = (List<DBSubscriber>) run.query(
                            "select * from subscriber ", new BeanListHandler(DBSubscriber.class));
                    for (DBSubscriber subscriber : subscribers) {
                        String result = getHTML ("http://129.12.44.32/baptiste/flooded?lat=" + subscriber.getLatitude() + "&lng=" + subscriber.getLongitude() + "&phone");

                        if (result.contains("true")) {
                            try {
                                System.out.println("text message:" + subscriber.getNumber());

                                Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
                                Message message = Message.creator(

                                        new com.twilio.type.PhoneNumber("+44" + subscriber.getNumber()),
                                        new com.twilio.type.PhoneNumber("+441472732030"),
                                        "Flood warning for your postcode " + subscriber.getPostcode())
                                        .create();
                            } catch (Exception ex) { System.out.println(ex.getMessage()); }
                        }
                    }
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());

                }
            }},0,15,TimeUnit.MINUTES);
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