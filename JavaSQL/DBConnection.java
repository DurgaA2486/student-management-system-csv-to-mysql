package JavaSQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection{

    // Defaults to "localhost" so nothing breaks if DB_HOST isn't set
    // (e.g. running the app directly on your machine against the
    // Dockerized MySQL, since its port is published to localhost:3306).
    // Set DB_HOST=mysql when the app itself runs inside docker-compose.
    private static final String HOST = System.getenv().getOrDefault("DB_HOST", "localhost");
    private static final String URL = "jdbc:mysql://" + HOST + ":3306/student_details";
    private static final String USER = System.getenv("DB_USER");
    private static final String PASSWORD = System.getenv("DB_PASSWORD");

    public static Connection getConnection() throws SQLException{
        if(USER == null || PASSWORD == null)
            throw new SQLException("Environment variables DB_USER OR DB_PASSWORD not set!");

        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}