import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class InterfaceFrame extends javax.swing.JFrame {
    static JTextField textField; //user input
    private JSplitPane splitPane;
    private JPanel topPanel;
    private JPanel bottomPanel;
    private JScrollPane scrollPane;
    private JTextArea resultsTextArea; //For query output
    private JPanel inputPanel; //contains user input text field
    private JButton sendButton; //for sending user input
    
    private ResultSetModel aModel;

    private static int CUR_ANIMAL_ID = 12345;
    private enum QueryOptions { ADDANIMAL, ADOPTANIMAL, SEARCHANIMALBYTYPE, FILECOMPLAINT, SETUPVISIT;}
    private QueryOptions lastOptionPressed;

    public InterfaceFrame(ResultSetModel rsModel) {
        try
        {	
        	aModel = rsModel;
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");

            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    CreateSplitPanelSetUp();
                    FrameSettings();
                    CreateOptions();
                    CreateInputPanel();

                    pack();
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void CreateSplitPanelSetUp()
    {
        splitPane = new JSplitPane();

        topPanel = new JPanel();
        bottomPanel = new JPanel();

        splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
        splitPane.setDividerLocation(200);                    // the initial position of the divider is 200 (our window is 400 pixels high)
        splitPane.setTopComponent(topPanel);
        splitPane.setBottomComponent(bottomPanel);

        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS)); // BoxLayout.Y_AXIS will arrange the content vertically
    }

    private void FrameSettings()
    {
        setPreferredSize(new Dimension(800, 650));
        getContentPane().setLayout(new GridLayout());
        getContentPane().add(splitPane);  // due to the GridLayout, our splitPane will now fill the whole window
        setTitle("Montreal Animal Adoption Center");
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        //setLocationRelativeTo(null);
        setResizable(false);
    }

    /*
    Add animal
    Adopt animal
    Search for a specific type of animal
    File a complaint
    Setting up a visit
    */

    private void CreateOptions()
    {
        JButton addAnimalButton = new JButton("Add animal");
        JButton adoptAnimalButton = new JButton("Adopt animal");
        JButton searchForAnimalButton = new JButton("Search for Animal");
        JButton fileComplaintButton = new JButton("File Complaint");
        JButton setupVisitButton = new JButton("Set Up Visit");
        JButton quitButton = new JButton("Quit");

        addAnimalButton.addActionListener((e) ->
        {
            String s = e.getActionCommand();
            lastOptionPressed = QueryOptions.ADDANIMAL;
            resultsTextArea.setText("Input: name (chars), species (chars), age (int), " +
                    "breed (chars), sex (f or m), medical file location (char)");
        });

        adoptAnimalButton.addActionListener((e) ->
        {
            String s = e.getActionCommand();
            lastOptionPressed = QueryOptions.ADOPTANIMAL;
            resultsTextArea.setText("Input: staffid (int), animalID (int),client name (chars),client phone (int),fee(int),contract(chars)");
        });

        searchForAnimalButton.addActionListener((e) ->
        {
            String s = e.getActionCommand();
            lastOptionPressed = QueryOptions.SEARCHANIMALBYTYPE;
            resultsTextArea.setText("Input: animalSpecies (chars)");
        });

        fileComplaintButton.addActionListener((e) ->
        {
            String s = e.getActionCommand();
            lastOptionPressed = QueryOptions.FILECOMPLAINT;
            resultsTextArea.setText("Input: complaintTime(date),complaintLocation (chars),staffid (int),client name (char)"
            		+ "client phone (char), description (char), status (char)");
        });

        setupVisitButton.addActionListener((e) ->
        {
            String s = e.getActionCommand();
            lastOptionPressed = QueryOptions.SETUPVISIT;
            resultsTextArea.setText("Input: animalID (int), client name (char), client phone (char), visit time (date)");
        });

        quitButton.addActionListener((e) ->
        {
            AdoptionDatabase.exit();
        });

        JPanel optionsPanel = new JPanel();
        optionsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 75)); //prevent resizing issues
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));   // X_Axis will arrange the content horizontally

        optionsPanel.add(addAnimalButton);
        optionsPanel.add(adoptAnimalButton);
        optionsPanel.add(searchForAnimalButton);
        optionsPanel.add(fileComplaintButton);
        optionsPanel.add(setupVisitButton);
        optionsPanel.add(quitButton);
        topPanel.add(optionsPanel);
    }

    //makes a text field for user input and button for submission at bottom of frame
    private void CreateInputPanel()
    {
        inputPanel = new JPanel();
        inputPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 75)); //prevent resizing issues
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.X_AXIS));   // X_Axis will arrange the content horizontally

        scrollPane = new JScrollPane(); //in case the text is too long for the textArea, make it scrollable
        resultsTextArea = new JTextArea();

        bottomPanel.add(scrollPane);
        scrollPane.setViewportView(resultsTextArea); //makes the textArea scrollable
        bottomPanel.add(inputPanel);

        resultsTextArea.setText("Query Results:");
        resultsTextArea.setBackground(new Color(152, 187, 185));
        resultsTextArea.setEditable(false);
        resultsTextArea.setLineWrap(true);
        resultsTextArea.setWrapStyleWord(true);
        //infoTextArea.setMargin(new Insets(5, 5, 5,5));

        textField = new JTextField();  //to input text
        sendButton = new JButton("send"); //to send text

        sendButton.addActionListener((e) -> {
            String s = e.getActionCommand();
            String input = textField.getText();
            
            String results = null;
            String[] params = null;
            switch (lastOptionPressed) {
                case ADDANIMAL:
                    params = input.split(",");

                    if (!isNumeric(params[2]))
                    {
                        results = "Error! The animal id must be an integer.";
                        break;
                    }

                    String stmt = InitializeReset.getInsertAnimalStatement(
                            CUR_ANIMAL_ID++, params[0].trim(), params[1].trim(),
                            Integer.parseInt(params[2]), params[3].trim(), params[4].charAt(0),
                            false, params[5].trim());
                    results = AdoptionDatabase.sendStatement("insert", "Animal", stmt);
                    break;
                case ADOPTANIMAL:
                	//input = (staffid,animalid,client name,client phone, fee, contract)
                	String[] params2 = input.split(",");
                	
                	//create SQL statement looking for employee
                	String getStmt = 
            				"SELECT * "+
            				"FROM VolunteerCareWorker "+
            				"WHERE staffid = "+ params2[0].trim() + ";";
                	
                	AdoptionDatabase.sendStatement("query", null, getStmt);
                	
                	if(aModel.isEmpty()==true) {
                		results += "Error! Staffid does not exist, please enter a different staffid or choose another query";
                		break;
                	}else {
                		results += "Staff Exists\n";
                	}
                	
                	//create SQL statement looking for animal
                	getStmt = 
            				"SELECT * "+
            				"FROM Animal "+
            				"WHERE animalID = "+
            				params2[1].trim() +
            				"AND isadopted='0';";
                	
                	//complete SQL query, update ResultSetModel and update prelim results String
                	AdoptionDatabase.sendStatement("query", null, getStmt);
                	
                	//check if query results are empty (ergo animal does not exist or has already been adopted)
                	if(aModel.isEmpty()==true) {
                		results += "Error! Animal does not exist or has already been adopted, please enter a different animal id or choose another query";
                		break;
                	}else {
                		results += "Animal Exists\n";
                	}
                	
                	//create SQL statement looking for client
                	getStmt = 
            				"SELECT *"+
            				"FROM Client "+
            				"WHERE clientName = "+
            				"'"+params2[2]+"'" +
            				"AND "+"clientPhone= " + "'" + params2[3] +"';";
                	
                	AdoptionDatabase.sendStatement("query", null, getStmt);
                	
                	//check if client exists and continue
                	if(aModel.isEmpty()==true) {
                		results += "Error! client does not exist, please enter a different animal id or choose another query";
                		break;
                	}else {
                		results += "Client Exists\n";
                	}
                	
                	//create insert statement for Adopts and update animalIsAdopted = 'True'
                	String insertStmt = String.format("%s,%s,'%s','%s',%s,'%s'", params2[0],params2[1],params2[2],params2[3],params2[4],params2[5]);
                	results += AdoptionDatabase.sendStatement("insert", "Adopts", insertStmt);
                	
                	//update Animal.isadopted
                	String updateStmt = "SET isadopted = '1' WHERE animalid = " + params2[1];
                	results += AdoptionDatabase.sendStatement("update", "Animal", updateStmt);
                	
                case SEARCHANIMALBYTYPE:
                    //input = (animalSpecies)
                    String param = input.trim().substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();

                    //create SQL statement looking for animal
                    String speciesStmt =
                            "SELECT animalID,isAdopted " +
                                    "FROM Animal " +
                                    "WHERE species = " +
                                    "'" + param + "'" +
                                    ";";

                    //complete SQL query, update ResultSetModel and update prelim results String
                    results = AdoptionDatabase.sendStatement("query", null, speciesStmt);
                    //System.out.println(aModel.toString());

                    //check if animal does not exist and alter output message
                    if (aModel.isEmpty() == true) {
                        results = "No animals of species " + param + ", please enter a different animal species or choose another query";
                        break;
                    }
                    break;

                case FILECOMPLAINT:
                	//process input, size = 7
                	results = "";
                	params = input.split(",");
                	System.out.println(params[2]);
                	
                	//check integer parameters
                	if (!isNumeric(params[2]))
                    {
                        results = "Error! The staff id must be an integer.";
                        break;
                    }
                	
                	//check if staffid exists
                	getStmt = 
            				"SELECT * "+
            				"FROM VolunteerCareWorker "+
            				"WHERE staffid = "+ params[2].trim() + ";";
                	
                	AdoptionDatabase.sendStatement("query", null, getStmt);
                	
                	if(aModel.isEmpty()==true) {
                		results = "Error! Staff id does not exist!";
                		break;
                	}else {
                		results += "Staff exists.\n";
                	}
                	
                	//check if client exists
                	getStmt = 
            				"SELECT *"+
            				"FROM Client "+
            				"WHERE clientName = "+
            				"'"+params[3]+"'" +
            				"AND "+"clientPhone= " + "'" + params[4] +"';";
                	
                	AdoptionDatabase.sendStatement("query", null, getStmt);
                	
                	if(aModel.isEmpty()==true) {
                		results = "Error! Client does not exist!";
                		break;
                	}else {
                		results += "Client exists.\n";
                	}
                	
                	//create Complaint table entry and insert
                	String fileComplaintInsert = String.format("'%s','%s',%s,'%s',%s,'%s','%s'", params[0],params[1],params[2],params[3],params[4],params[5],params[6]);
                	results += AdoptionDatabase.sendStatement("insert", "Complaint", fileComplaintInsert);
                	
                    break;
                case SETUPVISIT:
                    params = input.split(",");

                    if (!isNumeric(params[0]))
                    {
                        results = "Error! The animal id must be an integer.";
                        break;
                    }

                    //create SQL statement looking for visits at that time
                    String  checkVisitsStmt =
                            "SELECT animalID " +
                                    "FROM Visits " +
                                    "WHERE visitTime = " +
                                     "'" + params[3].trim() + "'" +
                                    ";";

                    results = AdoptionDatabase.sendStatement("query", "Visits", checkVisitsStmt);

                    if(!aModel.isEmpty())
                    {
                        results = "Error! There is already a visit appointment at this time.";
                        break;
                    }

                    String visitsStmt = String.format("%d,'%s','%s','%s'",
                            Integer.parseInt(params[0].trim()), params[1].trim(), params[2].trim(), params[3].trim());

                    results = AdoptionDatabase.sendStatement("insert", "Visits", visitsStmt);
                    break;
                default:
                    return; //no option picked yet
            }

            String resultStr = "RESULTS:\n";
            resultStr += results == null ? "Error" : String.join("\n", results);
            resultsTextArea.setText(resultStr);

            textField.setText("");
        });

        inputPanel.add(textField); //on left
        inputPanel.add(sendButton);  //on right
    }

    public static boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }
}
