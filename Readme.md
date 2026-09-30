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

## One-time setup
1. Make sure Docker Desktop is installed and running.
2. Create a .env folder and set the values of the attributes as given :
```
DB_PASSWORD=your_password_here
DB_USER=root
```
3. Download the MySQL Connector/J jar and place it in a lib/ folder in the project root.

## Everytime we want to to run
1. From the project root, start the database:

```
docker compose -d mysql
```
2. Incase any .java file is to be recompiled. The below given command should be run from the project folder:
   
```
javac -cp ".;lib\mysql-connector-j-26.7.0.jar" JavaSQL\*.java
```
3. Run the app: run.ps1 does three things for you automatically, in order:
   Reads your .env file line by line.Sets DB_USER and DB_PASSWORD as environment variables for
   this terminal session. Launches the Java app with the correct classpath (java -cp ".;lib\mysql-connector-j-26.7.0.jar" JavaSQL.Student). So instead of typing three separate commands by hand like before, you just run:

```
.\run.ps1
```

The database and required tables will be created automatically if they do not exist.

## Default login

```
Username: admin
Password: admin123
```
## Stop/Restart the container

-To pause the container, but keep the data

```
docker compose stop mysql
```

-Start it back

```
docker compose up -d mysql
```

-Remove the container, but data is safe in mysql_data volume

```
docker compose down
```

-Remove the container as well as the volume which will result in the data to be permanently to be deleted

```
docker compose down -v
```

## Future Improvements

- Add a graphical user interface (GUI) using Java Swing or JavaFX  
- Implement password encryption for improved security  
- Add search and filtering options for student records  
- Export attendance reports as PDF or Excel  
- Add role management with more user levels  
