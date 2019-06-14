package jsplink;
import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;

public class CustomDataSource {
   // JDBC driver name and database URL

   static final String DB_URL = "jdbc:mysql://localhost/co838";
   static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  

   //  Database credentials
   static final String USER = "rob";
   static final String PASS = "";
   private static DataSource datasource;
   private static final BasicDataSource basicDataSource;

   static {
      basicDataSource = new BasicDataSource();
      basicDataSource.setDriverClassName(JDBC_DRIVER);
      basicDataSource.setUsername(USER);
      basicDataSource.setPassword(PASS);
      basicDataSource.setUrl(DB_URL);
   }

   public static DataSource getInstance() {
      return basicDataSource;
   }
}
