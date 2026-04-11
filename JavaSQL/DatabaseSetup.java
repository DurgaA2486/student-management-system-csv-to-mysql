package JavaSQL;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseSetup{
    public static void createDatabaseAndTables(Connection con)throws SQLException{
        String createDatabase="create database if not exists student_details";
        try(Statement stmt=con.createStatement()){
            stmt.executeUpdate(createDatabase);
            String usersTable="create table if not exists student_details.users(Student_id int AUTO_INCREMENT PRIMARY KEY,Username Varchar(50) UNIQUE not null,Password varchar(100) not null,Role varchar(20) default 'admin')";
            String studentsTable="create table if not exists student_details.student(Name Varchar(100) not null,RollNo Varchar(15) primary key, Age int,Standard Varchar(10) not null)";
            String attendanceTable="create table if not exists student_details.attendance(Attendance_id int AUTO_INCREMENT PRIMARY KEY, RollNo varchar(15) not null, Date date not null,Status ENUM('Present','Absent') not null,UNIQUE(RollNo,date),foreign key(RollNo) references student_details.student(RollNo) on delete cascade)";
            stmt.executeUpdate(usersTable);
            stmt.executeUpdate(studentsTable);
            stmt.executeUpdate(attendanceTable);

            String insertAdmin="Insert ignore into student_details.users(Username,Password,Role) values('admin','admin123','admin')";
            stmt.executeUpdate(insertAdmin);

            System.out.println("Database & tables created successfully!");
        }
    }
}