import javax.swing.*;
import java.awt.*;

public class InterfaceFrame extends javax.swing.JFrame {
    static JTextField textField; //user input
    private JSplitPane splitPane;
    private JPanel topPanel;
    private JPanel bottomPanel;
    private JScrollPane scrollPane;
    private JTextArea resultsTextArea; //For query output
    private JPanel inputPanel; //contains user input text field
    private JButton sendButton; //for sending user input

    //TODO: better exception handling
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");

            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new InterfaceFrame().setVisible(true);
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    //Construct frame with query results on top and options/input on bottom
    public InterfaceFrame()
    {
        CreateSplitPanelSetUp();
        FrameSettings();
        CreateOptions();
        CreateInputPanel();

        pack();
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

    private void CreateOptions()
    {
        JButton addAnimalButton = new JButton("Add animal");
        JButton adoptAnimalButton = new JButton("Adopt animal");
        JButton quitButton = new JButton("Quit");

        addAnimalButton.addActionListener((e) -> {
            String s = e.getActionCommand();

        });

        adoptAnimalButton.addActionListener((e) -> {
            String s = e.getActionCommand();

        });

        quitButton.addActionListener((e) -> {
            System.exit(0);
        });

        JPanel optionsPanel = new JPanel();
        optionsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 75)); //prevent resizing issues
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));   // X_Axis will arrange the content horizontally

        optionsPanel.add(addAnimalButton);
        optionsPanel.add(adoptAnimalButton);
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
            resultsTextArea.setText(input);
            textField.setText("");
        });

        inputPanel.add(textField); //on left
        inputPanel.add(sendButton);  //on right
    }
}
