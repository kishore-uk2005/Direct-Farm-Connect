package util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    public static Connection getConnection() throws Exception {
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

        String server = System.getenv("DB_SERVER");
        String database = System.getenv("DB_NAME");

        if (server == null || database == null) {
            throw new IllegalStateException(
                "Set the DB_SERVER and DB_NAME environment variables."
            );
        }

        String url = "jdbc:sqlserver://" + server + ";" +
                     "databaseName=" + database + ";" +
                     "integratedSecurity=true;" +
                     "encrypt=false";

        return DriverManager.getConnection(url);
    }
}
