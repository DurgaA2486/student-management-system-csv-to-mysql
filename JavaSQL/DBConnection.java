package JavaSQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection{
    
    private static final String URL="jdbc:mysql://localhost:3306/student_details";
    private static final String USER=System.getenv("DB_USER");
    private static final String PASSWORD=System.getenv("DB_PASSWORD");
    
    public static Connection getConnection() throws SQLException{
        if(USER == null || PASSWORD == null)
            throw new SQLException("Environment variables DB_USER OR DB_PASSWORD not set!");

        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}