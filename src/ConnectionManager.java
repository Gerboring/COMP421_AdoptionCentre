import org.postgresql.util.PSQLException;

import java.sql.*;
import java.util.Scanner;

public class ConnectionManager
{
    private static Connection conn;
    private ResultSetModel aModel;

    public ConnectionManager(ResultSetModel pModel)
    {
        try
        {
            Class.forName("org.postgresql.Driver");
            Scanner scan = new Scanner(System.in);
            System.out.println("Input db password");
            String password = scan.nextLine();
            conn = DriverManager.getConnection("jdbc:postgresql://comp421.cs.mcgill.ca:5432/cs421", "cs421g72", password);
            System.out.println("Connection Successful");
            aModel = pModel;
        }
        catch (SQLException e)
        {
            closeConnection();
            e.printStackTrace();
        }
        catch (ClassNotFoundException c)
        {
            System.out.println("Class not found for database");
            c.printStackTrace();
        }
    }

    public String executeStatement(String typeOfQuery, String tableName, String fullStatement)
    {
        Statement stmt;
        try
        {
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);

            switch (typeOfQuery)
            {
                case "insert":
                    stmt.executeUpdate("INSERT INTO " + tableName + " VALUES(" + fullStatement + ");");
                    return fullStatement;
                case "query":
                    //complete query, update model, and return string
                    ResultSet result = stmt.executeQuery(fullStatement);
                    aModel.setRS(result);
                    return aModel.toString();
                case "update":
                	//complete update in desired tableName
                	System.out.println("UPDATE " + tableName+ " " + fullStatement + ";");
                	stmt.executeUpdate("UPDATE " + tableName + " " + fullStatement + ";");
                    return fullStatement;
                default:
                    break;
            }
            stmt.close();
        } catch (SQLException e)
        {
            System.out.println("Error with your " + typeOfQuery + ": " + fullStatement);
            closeConnection();
            e.printStackTrace();
        }

        return null;
    }

    /*
    //formats the results of a query with each String in the array being a record
    private String resultTableToString(ResultSet rs) {
        String result = "";

        try
        {
            Statement st = conn.createStatement();
            ResultSetMetaData metaData = rs.getMetaData();

            int cols = metaData.getColumnCount();

            for(int i = 1; i <= cols; i++){
                result += metaData.getColumnLabel(i) + ",";
            }
            
            result += "\n";

            while (rs.next()) {
                for (int i = 1; i <= cols; i++) {
                    result += rs.getString(i);

                    if(i != cols)
                    {
                        result += ",";
                    }else {
                    	result += "\n";
                    }
                }
            }
        }
        catch(SQLException e)
        {
            closeConnection();
            e.printStackTrace();
        }

        return result;
    }
    
    */

    public static void closeConnection() {
        try
        {
            if(conn != null && !conn.isClosed())
            {
                System.out.println("Connection closing... ");
                conn.close();
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }
}