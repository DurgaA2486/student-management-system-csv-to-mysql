# Student Management System (File to SQL Migration)

## Project Overview

This project focuses on implementing a Student Management System in Java which demonstrates the transition from CSV file-based storage to a more structured relational database using MySQL. 

---

## Features

### File-Based Storage System (CSV Version)

In this part of the project, the system takes input from the user on their choice for any of the CRUD operations and also the validation of the input given by the user to prevent any invalid inputs from the user.

We are then storing the details or the outcomes by the user after the operation is done into a CSV file and a temporary file is also used while doing delete and update operations.


### Database-Based System (MySQL Version)

This can be considered as an enhanced version of the file-based by replacing it with a MySQL database using JDBC connectivity.
All CRUD operations are also implemented using prepared statements, improving performance and protecting against SQL injection.

A normalized relational schema is designed using primary keys, foreign keys, and unique constraints to maintain data relationships. The system also includes automated database and table creation using a setup class, making initial configuration simple.

## Modules

#### Role-Based Authentication Module

This module focuses on the role-based authentication of the Admin and staff roles currently. User can log in using credentials stored securely in the database. Environment variables are used to manage sensitive information such as database username and password.

### Attendance Management Module

This module focuses on tracking the attendance of the students which includes to mark a student's attendance, view attendance and also to generate an attendance report.

The reports include total classes conducted, number of days present and absent, and calculated attendance percentage for each student.

---
## Project Structure

```
PROJECT/
├── JavaFile/      # File-based CSV implementation
│   ├── Student.java
│   ├── StudentData.csv
│
├── JavaSQL/       # MySQL database implementation
│   ├── Student.java
│   ├── DBConnection.java
│   ├── DatabaseSetup.java
│   ├── AuthService.java
│   ├── AttendanceService.java
│
└── .vscode/
```

---


## Technologies Used

The following technologies are used in this project:

- Java  
- MySQL  
- JDBC (Java Database Connectivity)  
- SQL  
- Java I/O (File Handling)  


---

## How to Run the Project

### Running File-Based Version (CSV)

1. Navigate to the `JavaFile` directory.
2. Compile the program:

```
javac Student.java
```

3. Run the program:

```
java Student
```

Once the student records are added the StudentData.csv will appear inside the directory.

---

### Running Database-Based Version (MySQL)

1. Make sure MySQL is installed and running.
2. Update database credentials in your code or environment variables.
3. Navigate to the `JavaSQL` directory.
4. Compile all Java files:

```
javac *.java
```

5. Run the main program:

```
java Student
```

The database and required tables will be created automatically if they do not exist.

## Future Improvements

- Add a graphical user interface (GUI) using Java Swing or JavaFX  
- Implement password encryption for improved security  
- Add search and filtering options for student records  
- Export attendance reports as PDF or Excel  
- Add role management with more user levels  