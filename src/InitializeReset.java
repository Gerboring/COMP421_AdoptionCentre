import org.postgresql.util.PSQLException;

import java.sql.*;
import java.util.Scanner;

public class InitializeReset {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        Scanner scan = new Scanner(System.in);
        System.out.println("Input db password");
        String password = scan.nextLine();
        Connection conn1 = DriverManager.getConnection("jdbc:postgresql://comp421.cs.mcgill.ca:5432/cs421", "cs421g72", password);
        System.out.println("Connection Successful");
        createTables(conn1);
        conn1.close();
    }

    public static void createTables(Connection conn1) throws SQLException {
        createTable(conn1, "Animal", getAnimalStatement());
        createTable(conn1, "Client", getClientStatement());
        createTable(conn1, "Personnel", getPersonnelStatement());
        createTable(conn1, "Veterinarian", getVeterinarianStatement());
        createTable(conn1, "AnimalControlWorker", getAnimalControlWorkerStatement());
        createTable(conn1, "VolunteerCareWorker", getVolunteerCareWorkerStatement());
        createTable(conn1, "Complaint", getComplaintStatement());
        createTable(conn1, "Room", getRoomStatement());
        createTable(conn1, "MedicalRoom", getMedicalRoomStatement());
        createTable(conn1, "HoldingRoom", getHoldingRoomStatement());

    }

/* @todo
* Kennel
* Procedure
* Visits
* Adopts
* Captures
* Surrenders
* Investigates
* ConductsProcedure
* ReceivesProcedure
*/
    public static void createTable(Connection conn, String tableName, String statement) throws SQLException{
        Statement stmt = conn.createStatement();
        try {
            stmt.executeUpdate("DROP TABLE " + tableName);
            System.out.println(tableName + " table already existed");
        } catch (PSQLException e){
            System.out.println(tableName + " table did not previously exist");
        }
        try {
            stmt.executeUpdate(statement);
            System.out.println(tableName + " table created");
        } catch (PSQLException e){
            System.out.println("Error with your statement...");
            e.printStackTrace();
        }
        stmt.close();
    }

    public static String getAnimalStatement(){
        return "CREATE TABLE Animal" +
                "(" +
                " animalID INTEGER NOT NULL," +
                "name VARCHAR(20)," +
                "species VARCHAR(20)," +
                "age INTEGER," +
                "breed VARCHAR(20)," +
                "sex CHAR," +
                "isAdopted BIT NOT NULL DEFAULT 0::bit," +
                "medicalFile VARCHAR(20)," +
                "PRIMARY KEY(animalID)" +
                ")";
    }

    public static String getClientStatement(){
        return "CREATE TABLE Client" +
                "(" +
                "    clientName VARCHAR(20)," +
                "    clientPhone CHAR(10)," +
                "    address VARCHAR(1000)," +
                "    PRIMARY KEY(clientName,clientPhone)" +
                ");";
    }

    public static String getPersonnelStatement(){
        return "CREATE TABLE Personnel" +
                "(" +
                "    staffID INTEGER NOT NULL," +
                "    name VARCHAR(20)," +
                "    phone CHAR(10)," +
                "    address VARCHAR(1000)," +
                "    salary INTEGER," +
                "    PRIMARY KEY(staffID)" +
                ");";
    }

    public static String getVeterinarianStatement(){
        return "CREATE TABLE Veterinarian" +
                "(" +
                " qualifications VARCHAR(500)" +
                ") INHERITS (Personnel);";
    }


    public static String getAnimalControlWorkerStatement(){
        return "CREATE TABLE AnimalControlWorker()\n" +
                "INHERITS (Personnel);";
    }

    public static String getVolunteerCareWorkerStatement(){
        return "CREATE TABLE VolunteerCareWorker()\n" +
                "INHERITS (Personnel);";
    }

    public static String getComplaintStatement(){
        return "CREATE TABLE Complaint" +
                "(" +
                "complaintTime DATE NOT NULL, location VARCHAR(50) NOT NULL," +
                " staffID INTEGER, name VARCHAR(20)," +
                " phone INTEGER, description VARCHAR(50)," +
                " status CHAR NOT NULL DEFAULT ‘O’, PRIMARY KEY(complaintTime, location), " +
                "FOREIGN KEY(staffID) REFERENCES VolunteerCareWorker, " +
                "FOREIGN KEY(name) REFERENCES Client," +
                " FOREIGN KEY(phone) REFERENCES Client" +
                ");";
    }

    public static String getRoomStatement(){
        return "CREATE TABLE Room" +
                "(" +
                "roomNumber INTEGER NOT NULL," +
                "PRIMARY KEY(roomNumber)" +
                ");";
    }

    public static String getMedicalRoomStatement(){
        return "CREATE TABLE MedicalRoom" +
                "(" +
                "equipment VARCHAR(50)" +
                ")" +
                "INHERITS (Room);";
    }

    public static String getHoldingRoomStatement(){
        return "CREATE TABLE HoldingRoom" +
                "(" +
                "maxCapacity INTEGER" +
                ")" +
                "INHERITS (Room);";
    }

}
