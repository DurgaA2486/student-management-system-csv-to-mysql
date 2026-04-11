package JavaSQL;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.PreparedStatement;

public class AuthService{
    public static String login(String uname,String pass){
        String sql="Select * from users where Username=? AND Password=?";
        try(Connection con=DBConnection.getConnection();
            PreparedStatement pstmt = con.prepareStatement(sql)){
                pstmt.setString(1, uname);
                pstmt.setString(2, pass);

                ResultSet rs=pstmt.executeQuery();

                if (rs.next()){
                    return rs.getString("Role");
                }
        }
        catch(SQLException e){
            System.out.println("Login failed : "+e.getMessage());
        }
        return null;
    }
}