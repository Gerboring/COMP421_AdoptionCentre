import org.postgresql.util.PSQLException;

import java.io.IOException;
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

        //TODO: further test
        //Add animal csv inserts
        /*try
        {
            String [] csvAnimalInsertStmts = ParseCSV.getAnimalCSVStatements();
            for (String stmt: csvAnimalInsertStmts)
            {

                insert(conn1, "Animal", stmt);
            }
        }
        catch(IOException e){
            System.out.println("IO Error with Animal CSV: ");
            e.printStackTrace();
        }*/

        //finally, close connection
        conn1.close();
    }

    public static void createTables(Connection conn1) throws SQLException {
        String[] tables = {"Animal", "Client", "Veterinarian", "AnimalControlWorker", "Personnel",
                            "VolunteerCareWorker", "Complaint", "MedicalRoom", "HoldingRoom", "Room",
                            "Kennel", "Procedure", "Visits", "Adopts"};
        dropAllTables(conn1, tables);


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
        createTable(conn1, "Kennel", getKennelStatement());
        createTable(conn1, "Procedure", getProcedureStatement());
        createTable(conn1, "Visits", getVisitsStatement());
        createTable(conn1, "Adopts", getAdoptsStatement());
    }

    /* @todo
     * Captures
     * Surrenders
     * Investigates
     * ConductsProcedure
     * ReceivesProcedure
     */

    public static void dropAllTables(Connection conn, String[] tableNames) throws SQLException {
        Statement stmt = conn.createStatement();
        String curTable = "";
        try {
            for (String tableName: tableNames ) {
                curTable = tableName;
                stmt.executeUpdate("DROP TABLE IF EXISTS " + curTable + " CASCADE;");
            }
        } catch (PSQLException e) {
            System.out.println(curTable + " table did not previously exist");
            e.printStackTrace();
        }
    }
    public static void createTable(Connection conn, String tableName, String statement) throws SQLException{
        Statement stmt = conn.createStatement();
        try {
            stmt.executeUpdate(statement);
            System.out.println(tableName + " table created");
        } catch (PSQLException e){
            System.out.println("Error with your statement...");
            e.printStackTrace();
        }
        stmt.close();
    }

    public static void insert(Connection conn, String tableName, String statement)
    {
        try
        {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("INSERT INTO " + tableName + " VALUES(" + statement + ");");
            stmt.close();
        }
        catch (SQLException e)
        {
            System.out.println("Error with your insert: " + statement);
            e.printStackTrace();
        }
    }

    public static String getAnimalStatement(){
        return "CREATE TABLE Animal" +
                "(" +
                "animalID INTEGER NOT NULL," +
                "name VARCHAR(20)," +
                "species VARCHAR(20)," +
                "age INTEGER," +
                "breed VARCHAR(20)," +
                "sex CHAR," +
                "isAdopted BIT NOT NULL DEFAULT 0::bit," +
                "medicalFile VARCHAR(20)," +
                "PRIMARY KEY(animalID)" +
                ");";
    }

    //TODO: do insert for all other entities
    public static String getInsertAnimalStatement(int animalID, String name,
                                      String species, int age,
                                      String breed, char sex,
                                      Boolean isAdopted, String medicalFile)
    {
        return String.format("INSERT INTO Animal VALUES(%d, %s, %s, %d, %s, %c, %b, %s);",
                                        animalID, name, species, age, breed, sex, isAdopted, medicalFile);
    }

    public static String getClientStatement(){
        return "CREATE TABLE Client" +
                "(" +
                "clientName VARCHAR(20)," +
                "clientPhone CHAR(10)," +
                "address VARCHAR(1000)," +
                "PRIMARY KEY(clientName,clientPhone)" +
                ");";
    }

    public static String getInsertClient(String clientName, int clientPhone, String address)
    {
        return String.format("INSERT INTO Client VALUES(%s, %d, %s);",
                clientName, clientPhone, address);
    }

    public static String getPersonnelStatement(){
        return "CREATE TABLE Personnel" +
                "(" +
                "staffID INTEGER NOT NULL," +
                "name VARCHAR(20)," +
                "phone CHAR(10)," +
                "address VARCHAR(1000)," +
                "salary INTEGER," +
                "PRIMARY KEY(staffID)" +
                ");";
    }

    public static String getVeterinarianStatement(){
        return "CREATE TABLE Veterinarian" +
                "(" +
                "LIKE Personnel INCLUDING INDEXES," +
                " qualifications VARCHAR(500)" +
                ") INHERITS (Personnel);";
    }


    public static String getAnimalControlWorkerStatement(){
        return "CREATE TABLE AnimalControlWorker(" +
                "LIKE Personnel INCLUDING INDEXES)" +
                "INHERITS (Personnel);";
    }

    public static String getVolunteerCareWorkerStatement(){
        return "CREATE TABLE VolunteerCareWorker(" +
                "LIKE Personnel INCLUDING INDEXES)" +
                "INHERITS (Personnel);";
    }

    public static String getComplaintStatement(){
        return "CREATE TABLE Complaint" +
                "(" +
                "complaintTime DATE NOT NULL, location VARCHAR(50) NOT NULL," +
                "staffID INTEGER, name VARCHAR(20)," +
                "phone CHAR(10), description VARCHAR(50)," +
                "status CHAR DEFAULT '0' NOT NULL," +
                "PRIMARY KEY(complaintTime, location), " +
                "FOREIGN KEY(staffID) REFERENCES VolunteerCareWorker(staffID), " +
                "FOREIGN KEY(name, phone) REFERENCES Client(clientName, clientPhone)" +
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
                "LIKE Room INCLUDING INDEXES," +
                "equipment VARCHAR(50)" +
                ")" +
                "INHERITS (Room);";
    }

    public static String getHoldingRoomStatement(){
        return "CREATE TABLE HoldingRoom" +
                "(" +
                "LIKE Room INCLUDING INDEXES," +
                "maxCapacity INTEGER" +
                ")" +
                "INHERITS (Room);";
    }

    public static String getKennelStatement(){
        return "CREATE TABLE Kennel" +
                "(" +
                "roomNumber INTEGER," +
                "kennelNumber INTEGER," +
                "size INTEGER DEFAULT 1," +
                "FOREIGN KEY(roomNumber) REFERENCES HoldingRoom," +
                "PRIMARY KEY (roomNumber, kennelNumber)" +
                ");";
    }

    public static String getProcedureStatement(){
        return "CREATE TABLE Procedure" +
                "(" +
                "roomNumber INTEGER," +
                "startTime TIMESTAMP," +
                "duration INTEGER," +
                "type CHAR," +
                "FOREIGN KEY(roomNumber) REFERENCES MedicalRoom ," +
                "PRIMARY KEY (roomNumber, startTime)" +
                ");";
    }

    public static String getVisitsStatement(){
        return "CREATE TABLE Visits" +
                "(" +
                "animalID INTEGER NOT NULL," +
                "clientName VARCHAR(20)," +
                "clientPhone CHAR(10)," +
                "visitTime TIMESTAMP," +
                "FOREIGN KEY(animalID) REFERENCES Animal," +
                "FOREIGN KEY(clientName, clientPhone) REFERENCES Client(clientName, clientPhone)," +
                "PRIMARY KEY(animalId, clientName, clientPhone)" +
                ");";
    }

    public static String getAdoptsStatement(){
        return "CREATE TABLE Adopts" +
                "(" +
                "staffID INTEGER," +
                "animalID INTEGER," +
                "clientName VARCHAR(20)," +
                "clientPhone CHAR(10)," +
                "Fee INTEGER," +
                "contract VARCHAR(65000)," +
                "FOREIGN KEY(animalID) REFERENCES Animal," +
                "FOREIGN KEY(staffID) REFERENCES VolunteerCareWorker," +
                "FOREIGN KEY(clientName, clientPhone) REFERENCES Client(clientName, clientPhone)," +
                "PRIMARY KEY(staffID,animalID,clientName,clientPhone)" +
                ");";
    }

    public static String getCapturesStatement(){
        return "CREATE TABLE Captures" +
                "(" +
                "animalID INTEGER," +
                "complaintTime TIMESTAMP," +
                "complaintLocation VARCHAR(100)," +
                "staffID INTEGER," +
                "captureTime TIMESTAMP," +
                "captureLocation VARCHAR(100)," +
                "FOREIGN KEY(staffID) REFERENCES AnimalControlWorker," +
                "FOREIGN KEY(complaintTime, complaintLocation) REFERENCES Complaint(complaintTime, complaintLocation)," +
                "PRIMARY KEY(animalID, complaintTime, complaintLocation)" +
                ");";
    }
}
