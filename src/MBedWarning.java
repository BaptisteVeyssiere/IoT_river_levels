import jsplink.CustomDataSource;
import jsplink.DBSubscriber;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.http.HttpResponse;

@WebServlet("/mbed_floodwarning")
public class MBedWarning extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String mbed_id = request.getParameter("id");
        PrintWriter out = response.getWriter();
        if (mbed_id == null) {
            out.write("No id sent");
            return;
        }

        DataSource dataSource = CustomDataSource.getInstance();
        QueryRunner run = new QueryRunner(dataSource);
        ResultSetHandler<DBSubscriber> subscriber_results = new BeanHandler<DBSubscriber>(DBSubscriber.class);
        try {
            DBSubscriber subscriber = run.query("select * from subscriber where mbed_id = ?", subscriber_results, mbed_id);
            if (subscriber_results != null) {
                out.write("Flood warning for " + subscriber.getPostcode());
            } else {
                out.write("Mbed id not found");
            }
         } catch (Exception ex ) { ex.printStackTrace(out); }

    }
}
