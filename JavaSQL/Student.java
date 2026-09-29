package JavaSQL;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

class Student{
    static final String user=System.getenv("DB_USER");
    static final String password=System.getenv("DB_PASSWORD");
    static Connection con=null;
    static final Scanner sc=new Scanner(System.in);
    public static void main(String args[]){

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            if(user == null || password == null){
                System.out.println("Error: DB_USER or DB_PASSWORD environment variables not set!");
                return;
            }
            String dbHost = System.getenv().getOrDefault("DB_HOST", "localhost");
            con=DriverManager.getConnection("jdbc:mysql://" + dbHost + ":3306/", user, password);
            DatabaseSetup.createDatabaseAndTables(con);
            con.close();
            
            con=DBConnection.getConnection();
            
            while(true){
                System.out.println("========LOGIN========");
                System.out.println("Enter username : ");
                String uname=sc.nextLine();

                System.out.println("Enter your password : ");
                String pass=sc.nextLine();

                String role=AuthService.login(uname, pass);
                if(role == null){
                    System.out.println("Invalid username or password");
                    return;
                }

                System.out.println("Login Successfull !");
                System.out.println("Login as "+role+" role.");

                if(role.equalsIgnoreCase("admin")){
                    adminMenu();
                }
                else{
                    staffMenu();
                }


                
            }
        }

        catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found!");
            e.printStackTrace();
        } 
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally{
            try{
                if(con!=null)con.close();
            }
            catch(SQLException e){
                e.printStackTrace();
            }
        }
    }
    
    public static void adminMenu() throws SQLException{
        while(true){
            System.out.println("Choose your option : ");

            System.out.println("==========Admin Menu==========");
            System.out.println("1 -> Create a new student record.");
            System.out.println("2 -> Print a new student record.");
            System.out.println("3 -> Update a student record.");
            System.out.println("4 -> Delete a student record.");
            System.out.println("5 -> Mark Attendance");
            System.out.println("6 -> View Attendance");
            System.out.println("7 -> Generate Attendance Report");
            System.out.println("8 -> Exit.");

            int ch=sc.nextInt();
            sc.nextLine();

            switch(ch){
                case 1:
                    addStudent();
                    break;
                case 2:
                    printStudent();
                    break;
                case 3:
                    updateStudent();
                    break;
                case 4:
                    deleteStudent();
                    break;
                case 5:
                    System.out.println("Enter RollNo: ");
                    String roll = sc.nextLine();

                    System.out.println("Enter status (Present/Absent): ");
                    String status=sc.nextLine();
                    
                    AttendanceService.markAttendance(roll, status);
                    break;

                case 6:
                    System.out.println("Enter RollNo: ");
                    String rollView = sc.nextLine();
                    AttendanceService.viewAttendance(rollView);
                    break;
                case 7:
                    System.out.println("Enter RollNo: ");
                    String rollReport = sc.nextLine();
                    AttendanceService.generateAttendanceReport(rollReport);
                    break;
                case 8:
                    System.out.println("Exiting...");
                    System.exit(0);
                    break;
                default:
                    System.out.println("The choice is invalid");
                    break;
            }
        }
    }

    public static void staffMenu() throws SQLException{
        while(true){
            System.out.println("Choose your option : ");
        
            System.out.println("==========Staff Menu==========");
            System.out.println("1 -> Print Students.");
            System.out.println("2 -> Mark Attendance");
            System.out.println("3 -> Exit.");

            int ch=sc.nextInt();
            sc.nextLine();

            switch(ch){
                case 1:
                    printStudent();
                    break;
                case 2:
                    System.out.println("Enter RollNo: ");
                    String roll = sc.nextLine();

                    System.out.println("Enter Status (Present/Absent): ");
                    String status = sc.nextLine();

                    AttendanceService.markAttendance(roll, status);
                    break;
                case 3:
                    System.out.println("Exiting...");
                    System.exit(0);
                    break;
                default:
                    System.out.println("The choice is invalid");
                    break;
            }
        }
    }
    public static void addStudent() throws SQLException{
        System.out.println("Enter the name of the student : ");
        String name=sc.nextLine();
        System.out.println("Enter the roll number of the student :");
        String rollno=sc.nextLine();
        System.out.println("Enter the age of the student : ");
        int age=sc.nextInt();
        sc.nextLine();
        System.out.println("Enter the class of the student : ");
        String standard=sc.nextLine();
        System.out.println("Has the fee been paid? (yes/no): ");
        boolean paid = sc.nextLine().trim().equalsIgnoreCase("yes");
        String sql="Insert into student (name,rollno,age,standard,paid) values (?,?,?,?,?)";
        try(PreparedStatement pstmt=con.prepareStatement(sql)){
            pstmt.setString(1,name);
            pstmt.setString(2,rollno);
            pstmt.setInt(3,age);
            pstmt.setString(4,standard);
            pstmt.setBoolean(5, paid);
            int rows=pstmt.executeUpdate();
            if (rows==0)
                System.out.println("The student record wasn't added successfilly !");
            else
                System.out.println(rows+" student was added successfully !");
        }
    }

    public static void printStudent() throws SQLException{
        System.out.println("Enter your choice of viewing the data : ");
        System.out.println("1. Print all the student details");
        System.out.println("2. Print only the student names");
        System.out.println("3. Print only the roll number of the students");
        System.out.println("4. Print only a particular student details with their roll number");
        System.out.println("5. Print the details of the student or students with a particular name ");
        int ch=sc.nextInt();
        sc.nextLine();
        switch (ch) {
            case 1:
                String sqlForAll="Select * from student";
                try(PreparedStatement pstmt=con.prepareStatement(sqlForAll)){
                    ResultSet rs=pstmt.executeQuery();
                    while(rs.next()){
                        String name =rs.getString("Name");
                        String rollno=rs.getString("RollNo");
                        int age=rs.getInt("age");
                        String standard=rs.getString("Standard");
                        System.out.println(name+" "+rollno+" "+age+" "+standard+" "+(rs.getBoolean("Paid")?"Paid":"Unpaid"));
                    }
                }
                break;

            case 2:
                String sqlForName ="Select Name from student";
                try(PreparedStatement pstmt=con.prepareStatement(sqlForName)){
                    ResultSet rs=pstmt.executeQuery();
                    System.out.println("Name of the students : ");
                    while(rs.next()){
                        String name =rs.getString("Name");
                        System.out.println(name);
                    }

                }
                break;

            case 3:
                String sqlForRoll ="Select RollNo from student";
                try(PreparedStatement pstmt=con.prepareStatement(sqlForRoll)){
                    ResultSet rs=pstmt.executeQuery();
                    System.out.println("RollNo of the students : ");
                    while(rs.next()){
                        String rollno =rs.getString("RollNo");
                        System.out.println(rollno);
                    }
                }
                break;

            case 4:
                String sqlForStud ="Select * from student where RollNo =?";
                System.out.println("Enter the roll number of the student");
                String roll=sc.nextLine();
                try(PreparedStatement pstmt=con.prepareStatement(sqlForStud)){
                    pstmt.setString(1, roll);
                    ResultSet rs=pstmt.executeQuery();
                    while(rs.next()){
                        String name =rs.getString("Name");
                        String rollno=rs.getString("RollNo");
                        int age=rs.getInt("age");
                        String standard=rs.getString("Standard");
                        System.out.println(name+" "+rollno+" "+age+" "+standard+" "+(rs.getBoolean("Paid")?"Paid":"Unpaid"));
                    }
                }
                break;

            case 5:
                String sqlSearch="Select * from student where Name=?";
                try(PreparedStatement pstmt=con.prepareStatement(sqlSearch)){
                    String Searchname=sc.nextLine();
                    pstmt.setString(1,Searchname);
                    ResultSet rs=pstmt.executeQuery();
                    boolean found=false;
                    while(rs.next()){
                        found=true;
                        String name =rs.getString("Name");
                        String rollno=rs.getString("RollNo");
                        int age=rs.getInt("age");
                        String standard=rs.getString("Standard");
                        System.out.println(name+" "+rollno+" "+age+" "+standard+" "+(rs.getBoolean("Paid")?"Paid":"Unpaid"));
                    }
                    if(!found)
                        System.out.println("There is no student with name as "+Searchname);
                }
                break;

            default:
                System.out.println("Invalid choice !");
                break;
        }
    }

    public static void updateStudent() throws SQLException{
        System.out.println("Enter your roll number and then after that your choice of updating the data(Based on RollNo) : ");
        String roll=sc.nextLine();
        System.out.println("1. Update the name of the student");
        System.out.println("2. Update the age of the student");
        System.out.println("3. Update the standard of the student");
        System.out.println("4. Update the paid status of the student");
        int ch=sc.nextInt();
        sc.nextLine();
        switch (ch) {
            case 1:
                System.out.println("Enter the new name of the student");
                String name=sc.nextLine();
                String sqlUpdateName="Update student set Name=? where  RollNo=?";
                try(PreparedStatement pstmt=con.prepareStatement(sqlUpdateName)){
                    pstmt.setString(1,name);
                    pstmt.setString(2, roll);
                    int rows=pstmt.executeUpdate();
                    if(rows==0)
                        System.out.println("The student record wasn't updated successfully !");
                    else
                        System.out.println("Rows affected : "+rows);
                }
                break;

            case 2:
                System.out.println("Enter the new age of the student");
                int age=sc.nextInt();
                sc.nextLine();
                String sqlUpdateAge="Update student set Age=? where  RollNo=?";
                try(PreparedStatement pstmt=con.prepareStatement(sqlUpdateAge)){
                    pstmt.setInt(1,age);
                    pstmt.setString(2, roll);
                    int rows=pstmt.executeUpdate();
                    if(rows==0)
                        System.out.println("The student record wasn't updated successfully !");
                    else
                        System.out.println("Rows affected : "+rows);
                }
                break;

            case 3:
                System.out.println("Enter the new standard of the student");
                String standard=sc.nextLine();
                String sqlUpdateClass="Update student set Standard=? where  RollNo=?";
                try(PreparedStatement pstmt=con.prepareStatement(sqlUpdateClass)){
                    pstmt.setString(1,standard);
                    pstmt.setString(2, roll);
                    int rows=pstmt.executeUpdate();
                    if(rows==0)
                        System.out.println("The student record wasn't updated successfully !");
                    else
                        System.out.println("Rows affected : "+rows);
                }
                break;
            case 4:
        System.out.println("Has the fee been paid now? (yes/no): ");
        boolean paidStatus = sc.nextLine().trim().equalsIgnoreCase("yes");
        String sqlUpdatePaid="Update student set Paid=? where RollNo=?";
        try(PreparedStatement pstmt=con.prepareStatement(sqlUpdatePaid)){
            pstmt.setBoolean(1,paidStatus);
            pstmt.setString(2, roll);
            int rows=pstmt.executeUpdate();
            System.out.println(rows==0 ? "The student record wasn't updated successfully !" : "Rows affected : "+rows);
        }
        break;

            default:
                System.out.println("Invalid Choice !");
                break;
        }
    }

    public static void deleteStudent() throws SQLException{
        System.out.println("Enter the roll number of the student whose record is to be deleted ");
        String roll=sc.nextLine();
        String sql="delete from student where RollNo=?";
        try(PreparedStatement pstmt=con.prepareStatement(sql)){
            pstmt.setString(1,roll);
            int rows=pstmt.executeUpdate();
            if(rows==0)
                System.out.println("The student record with the specified rollno wasn't there !");
            else
                System.out.println("Rows affected : "+rows);
        }

    }
}
