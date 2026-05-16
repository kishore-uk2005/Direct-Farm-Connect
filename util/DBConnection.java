package util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    public static Connection getConnection() throws Exception {

        String url =
                "jdbc:sqlserver://KISHORE_UK:1433;databaseName=direct_farm_db;integratedSecurity=true;encrypt=false";

        return DriverManager.getConnection(url);
    }
}
