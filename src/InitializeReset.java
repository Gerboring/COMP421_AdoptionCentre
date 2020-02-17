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
       createTable(conn1,"personnel",getPersonnelStatement());
       conn1.close();
    }

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

}
