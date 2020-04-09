import java.sql.Connection;
import java.sql.SQLException;

public class AdoptionDatabase
{
    private static InitializeReset initializeDatabaseObj;
    private static ConnectionManager connObj;
    private static InterfaceFrame gui;

    public static void main(String[] args)
    {
        //initialize database
        //initializeDatabaseObj = new InitializeReset();

        //set up database
        connObj = new ConnectionManager();

        //set up gui
        gui = new InterfaceFrame();
        gui.setVisible(true);
    }

    public static void exit()
    {
        connObj.closeConnection();
        System.exit(0);
    }
}
