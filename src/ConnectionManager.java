import org.postgresql.util.PSQLException;

import java.sql.*;
import java.util.Scanner;

public class ConnectionManager
{
    private static Connection conn;

    public ConnectionManager()
    {
        try {
            Class.forName("org.postgresql.Driver");
            Scanner scan = new Scanner(System.in);
            System.out.println("Input db password");
            String password = scan.nextLine();
            conn = DriverManager.getConnection("jdbc:postgresql://comp421.cs.mcgill.ca:5432/cs421", "cs421g72", password);
            System.out.println("Connection Successful");
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

    public String[] executeStatement(String fullStatement)
    {
        Statement stmt;
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(fullStatement);
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Error with your statement... ");
            closeConnection();
            e.printStackTrace();
        }

        return resultTableToString(fullStatement);
    }

    private String[] resultTableToString(String statement) {
        String[] result = null;

        try
        {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(statement);
            ResultSetMetaData metaData = rs.getMetaData();

            int cols = metaData.getColumnCount();
            result = new String[cols];

            for(int i = 0; i < cols; i++){
                result[0] += metaData.getColumnLabel(i) + " ";
            }

            while (rs.next()) {
                for (int i = 1; i <= cols; i++) {
                    result[i] += rs.getString(i);

                    if(i != cols - 1)
                    {
                        result[i] += ",";
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

    public void closeConnection() {
        try{
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