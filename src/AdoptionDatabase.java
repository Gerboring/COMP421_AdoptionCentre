public class AdoptionDatabase
{
    private static InitializeReset initializeDatabaseObj;
    private static ConnectionManager connObj;
    private static InterfaceFrame gui;

    public static void main(String[] args)
    {
        //initialize database
        //initializeDatabaseObj = new InitializeReset();
        //initializeDatabaseObj.ResetData();
    	
    	//create new result set, as M in MVC, connObj is controller, and InterfaceFrame is viewer
    	ResultSetModel rsModel = new ResultSetModel();

        //set up database
        connObj = new ConnectionManager(rsModel);

        //set up gui
        gui = new InterfaceFrame(rsModel);
        gui.setVisible(true);
    }

    public static String sendStatement(String typeOfStatement, String tableName, String statement)
    {
        return connObj.executeStatement(typeOfStatement, tableName, statement);
    }

    public static void exit()
    {
        connObj.closeConnection();
        System.exit(0);
    }
}
