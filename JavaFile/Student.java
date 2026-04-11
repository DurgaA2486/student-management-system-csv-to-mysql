import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.BufferedReader;
class StudentDetails{
    private String name,rollno,standard;
    private int age;
    StudentDetails(String name,String rollno,int age,String standard){
        this.name=name;
        this.rollno=rollno;
        this.age=age;
        this.standard=standard;
    }

    public String getName(){return name;}
    public void setName(String name){this.name=name;}

    public String getRoll(){return rollno;}
    public void setRoll(String rollno){this.rollno=rollno;}

    public int getAge(){return age;}
    public void setAge(int age){this.age=age;}

    public String getStandard(){return standard;}
    public void setStandard(String standard){this.standard=standard;}
}

public class Student{
    static Scanner sc=new Scanner(System.in);
    static File file = new File("StudentData.csv");
    static ArrayList<StudentDetails> studentList=new ArrayList<>();
    static HashMap<String,Integer>nameList=new HashMap<>();

    public static void loadFromFile(File file){
        try(BufferedReader reader=new BufferedReader(new FileReader(file))){
            String line;
            while((line=reader.readLine())!=null){
                //skip header
                if(line.startsWith("NAME"))
                    continue;
                String parts[]=line.split(",");
                if(parts.length<4)
                    continue;
                String name=parts[0];
                String rollno=parts[1];
                int age=Integer.parseInt(parts[2]);
                String standard=parts[3];
                studentList.add(new StudentDetails(name, rollno, age, standard));
                nameList.put(name,nameList.getOrDefault(name,0)+1);
            }
            System.out.println("Student data loaded successfully from file!");
        }
        catch(IOException e){
            System.err.println("Error reading student data from file!");
            e.printStackTrace();
        }
    }

    public static void addStudent(){
        String name=getValidName();
        String rollno=getValidRoll();

        int age=getValidAge();
        String standard=getValidStandard();

        studentList.add(new StudentDetails(name, rollno, age, standard));
        nameList.put(name,nameList.getOrDefault(name,0)+1);
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("StudentData.csv", true))) {
            writer.write(name+","+rollno+","+age+","+standard);
            writer.newLine();
        }

        catch(IOException e){
            System.err.println("Unable to write data to the file !");
             e.printStackTrace();
        }

        System.out.println("Student record has been added successfully!");
    }

    public static void printStudent(){

        if(studentList.isEmpty()){
            System.out.println("No student records are available!");
            return;
        }
        
        System.out.println("NAME\tROLLNO\tAGE\tSTANDARD");
        System.out.println("--------------------------------------");
        for(StudentDetails s: studentList){
            System.out.println(s.getName()+"\t"
            +s.getRoll()+"\t"+s.getAge()+"\t"+s.getStandard());
        } 
    }

    public static void updateStudent(){
        System.out.println("Enter the name of the student whose data is to be updated .");
        String searchName=sc.nextLine();
        int index;
        if(nameList.containsKey(searchName) && nameList.get(searchName)>1){
            System.out.println("Enter the roll number of the student to be searched : ");
            String searchRoll=sc.nextLine();
            index=searchStudent(searchName,searchRoll);
        }
        else
            index=searchStudent(searchName);
        if(index==-1){
            System.out.println("Student not found!");
            return;
        }
        else{
            System.out.println("Enter which data to be updated : ");
            System.out.println("1. Update the age of the student.");
            System.out.println("2. Update the standard of the student.");
            String ch=sc.nextLine();
            if(ch.equals("1")){
                System.out.println("Enter the updated age : ");
                int newAge=getValidAge();
                studentList.get(index).setAge(newAge);
                System.out.println("The age has been updated successfully!");
            }
            else if(ch.equals("2")){
                System.out.println("Enter the updated standard : ");
                String newStandard=getValidStandard();
                studentList.get(index).setStandard(newStandard);
                System.out.println("The standard has been updated successfully!");
            }
            else{
                System.out.println("Invalid Choice!");
                return;
            }
            updateFileRecord(studentList.get(index));
            System.out.println("Student Record updated successfully!");
        }
    }

    public static void deleteStudent(){
        System.out.println("Enter the name of the student whose data is to be deleted : ");
        String searchName=sc.nextLine();
        int index;
        StudentDetails studentTodel=null;

        if(nameList.containsKey(searchName) && nameList.get(searchName)>1){
            System.out.println("Enter the roll number of the student to be searched : ");
            String searchRoll=sc.nextLine();
            index=searchStudent(searchName,searchRoll);
        }
        else
            index=searchStudent(searchName);

        if(index==-1){
            System.out.println("Student not found");
            return;
        }
        else{
            studentTodel=studentList.get(index);
            int count=nameList.get(searchName);
            studentList.remove(index);
            if(count>1)
                nameList.put(searchName,count-1);
            else
                nameList.remove(searchName);

        }
        
        deleteFileRecord(studentTodel);
        System.out.println("Student record deleted successfully");
    }

    public static void deleteFileRecord(StudentDetails student){
        File tempFile=new File("temp.csv");
        try(BufferedReader reader=new BufferedReader(new FileReader(file));
            BufferedWriter writer=new BufferedWriter(new FileWriter(tempFile))){
                
            String line;
            while((line=reader.readLine())!=null){
                if(line.startsWith("NAME")){
                    writer.write(line);
                    writer.newLine();
                    continue;
                }

                String parts[]=line.split(",");
                if(parts.length<4)continue;
                    
                String fileName=parts[0],fileRoll=parts[1];

                //skip the record to delete
                if(fileName.equals(student.getName()) &&
                    fileRoll.equals(student.getRoll()))
                    continue;
                    
                writer.write(line);
                writer.newLine();
            }
        }
        catch(IOException e){
            System.err.println("Unable to delete from the file");
            e.printStackTrace();
        }
        file.delete();
        tempFile.renameTo(file);
    }

    public static void updateFileRecord(StudentDetails student){
        File tempFile=new File("temp.csv");
        try(BufferedReader reader=new BufferedReader(new FileReader(file));
            BufferedWriter writer=new BufferedWriter(new FileWriter(tempFile))){
            String line;
            while((line=reader.readLine())!=null){

                //ignores the header
                if(line.startsWith("NAME")){
                    writer.write(line);
                    writer.newLine();
                    continue;
                }

                String parts[]=line.split(",");

                if(parts.length<4){
                    continue;
                }

                String fileName=parts[0],fileRoll=parts[1];

                //if the line is to be updated
                if(fileName.equals(student.getName())
                    && fileRoll.equals(student.getRoll())){

                    writer.write(student.getName()+","+ student.getRoll()+","+
                        student.getAge()+","+student.getStandard());
                    writer.newLine();
                            }
                else{
                    writer.write(line);
                    writer.newLine();
                }
            }
        }
        catch(IOException e){
            System.err.println("Error updating the file!");
            e.printStackTrace();
        }
        file.delete();
        tempFile.renameTo(file);
    }

    public static String getValidName(){
        while(true){
            System.out.println("Enter the student name : ");
            String name=sc.nextLine().trim();

            if(name.isEmpty())
                System.out.println("Name cannot be empty!");
            
            else if(!name.matches("[a-zA-Z ]+"))
                System.out.println("Name must contains only alphabets and spaces!");
            
            else
                return name;
        }
    }

    public static String getValidRoll(){

        while(true){
            System.out.println("Enter roll number : ");
            String roll=sc.nextLine().trim();

            if(roll.isEmpty())
                System.out.println("Roll number cannot be empty!");

            else if(!roll.matches("[A-Za-z0-9]+"))
                System.out.println("Roll number must contain only alphabets and digits!");

            else if(rollExists(roll))
                System.out.println("Roll number already exists! Try another.");

            else
                return roll;
        }

    }

    public static int getValidAge(){

        while(true){
            System.out.println("Enter age : ");

            if(sc.hasNextInt()){
                int age=sc.nextInt();
                sc.nextLine();

                if(age < 3 || age > 30)
                    System.out.println("Age must be between 3 and 30!");
                else
                    return age;
            }
            else{
                System.out.println("Invalid input! Enter numeric age.");
                sc.nextLine();
            }
        }

    }

    public static String getValidStandard(){
        while(true){
            System.out.println("Enter standard/class : ");
            String standard=sc.nextLine().trim();
            if(standard.isEmpty())
                System.out.println("Standard cannot be empty!");
            else
                return standard;
        }
    }

    public static int searchStudent(String searchName){
        for(int i=0;i<studentList.size();i++){
            if(studentList.get(i).getName().equals(searchName))
                return i;
        }
        return -1;
    }

    public static int searchStudent(String searchName,String searchRoll){
        for(int i=0;i<studentList.size();i++){
            if(studentList.get(i).getName().equals(searchName) && studentList.get(i).getRoll().equals(searchRoll))
                return i;
        }
        return -1;
    }

    public static boolean rollExists(String roll){
        for(StudentDetails s: studentList){
            if(s.getRoll().equals(roll))
                return true;
        }
        return false;
    }

    public static void main(String args[]){
        
        try {
            if (file.createNewFile()) {
                System.out.println("The file has been created.");
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                    writer.write("NAME,ROLLNO,AGE,STANDARD");
                    writer.newLine();
                }
            }
            else {
                System.out.println("The file already exists and may contain data.");
            }
        } 
        
        catch (Exception e) {
            System.err.println("Due to some issue, the file was not created!");
            e.printStackTrace();
        }

        loadFromFile(file);

        while(true){

            System.out.println("Choose your option : ");
            System.out.println("1 -> Create a new student record.");
            System.out.println("2 -> Print a new student record.");
            System.out.println("3 -> Update a student record.");
            System.out.println("4 -> Delete a student record.");
            System.out.println("5 -> Exit.");
            String choice=sc.nextLine();

            if(choice.isEmpty())continue;

            switch(choice){

                case "1":
                    addStudent();
                    break;
                
                case "2":
                    printStudent();   
                    break;
                
                case "3":
                    updateStudent();
                    break;  

                case "4":
                    deleteStudent();
                    break;

                case "5":
                    System.out.println("Exiting...");
                    sc.close();
                    return;

                default:
                    System.out.println("The choice is invalid!");
            }
        }
    }
}