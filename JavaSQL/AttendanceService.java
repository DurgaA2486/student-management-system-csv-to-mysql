package JavaSQL;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

public class AttendanceService{
    public static void markAttendance(String roll,String status){

        if(!status.equalsIgnoreCase("Present") && !status.equalsIgnoreCase("Absent")){
            System.out.println("Invalid status! Use Present or Absent.");
            return;
        }

        status= status.substring(0,1).toUpperCase()+status.substring(1).toLowerCase();
        String sql="INSERT INTO attendance (RollNo, Date, Status) VALUES (?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
           PreparedStatement pstmt = con.prepareStatement(sql)){
            pstmt.setString(1,roll);
            pstmt.setDate(2,java.sql.Date.valueOf(LocalDate.now()));
            pstmt.setString(3, status);
            int rows=pstmt.executeUpdate();
            if (rows==0)
                System.out.println("The attendance wasn't recorded !");
            else
                System.out.println(rows+" attendance was recorded successfully !");
        }
        catch (SQLException e){
            if(e.getMessage().contains("Duplicate"))
                System.out.println("Attendance already marked for today!");
            else
                System.out.println("Error marking attendance: "+e.getMessage());
        }
    }

    public static void viewAttendance(String roll){
        String sql = "Select * from attendance where RollNo=?";
        try(Connection con = DBConnection.getConnection();
        PreparedStatement pstmt=con.prepareStatement(sql)){
            pstmt.setString(1, roll);
            ResultSet rs=pstmt.executeQuery();

            System.out.println("\n--- Attendance Record for RollNo: " + roll + " ---");
            boolean found =false;

            while(rs.next()){
                found = true;
                System.out.println("Date: "+rs.getDate("Date")+
                                  "| Status: "+rs.getString("Status"));
            }

            if(!found)
                System.out.println("No attendance records!");
        }

        catch(SQLException e){
            System.out.println("Error viewing attendance: "+e.getMessage());
        }
    }


    public static void generateAttendanceReport(String rollNo){
        String totalSql = "Select count(*) from attendance where RollNo=?";
        String presentSql = "Select count(*) from attendance where RollNo=? and status='Present'";

        try(Connection con= DBConnection.getConnection()){

            int total =0;
            int present =0;

            try(PreparedStatement pstmt =con.prepareStatement(totalSql)){
                pstmt.setString(1, rollNo);
                ResultSet rs=pstmt.executeQuery();
                if(rs.next())
                    total=rs.getInt(1);
            }

            try(PreparedStatement pstmt =con.prepareStatement(presentSql)){
                pstmt.setString(1, rollNo);
                ResultSet rs=pstmt.executeQuery();
                if(rs.next())
                    present=rs.getInt(1);
            }

            if(total==0){
                System.out.println("No attendance records found!");
                return;
            }

            int absent =total-present;
            double percentage =(present*100.0)/total;

            System.out.println("\n--- Attendance Report for RollNo: "+rollNo+"---");
            System.out.println("Total Classes: "+total);
            System.out.println("Present Days: "+present);
            System.out.println("Absent Days: "+absent);
            System.out.printf("Attendance Percentage: %.2f%%\n",percentage);
        }
        catch(SQLException e){
            System.out.println("Error generating report: "+e.getMessage());
        }
    }
}
