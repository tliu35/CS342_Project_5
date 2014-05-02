/**
 * @author Tongtong Liu
 * CS342 Project 5
 */
import java.awt.*;
import java.util.*;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.border.Border;

public class GameGUI extends JFrame implements ActionListener 
{
	private static final long serialVersionUID = 1L;
	private JFrame superFrame;
	private JPanel Panel_East,connectionPanel,Panel_South,Panel_North;
	protected JPanel Panel_Center, melds_Panel, button_Panel;
	private JTextArea messageArea, playArea;
	private JTextArea clientMessageArea;
	private JTextArea gameInteractionArea;
	private JTextField portTextField;
	private JMenu gameMenu;
	private JMenuBar mainMenuBar;
	private JMenuItem exitItem,helpItem, aboutItem;
	private JButton sendButton, drawButton, drawFromDiscardButton, startGameButton;
	private JButton disconnectButton;
	private JButton clientConnectButton;
	private String userName;
	private int serverPort, clientPort;
	private String privateMessagePeople;
	private String privateMessageContent;
	private Client client;
	private ServerHandle server;
	private JButton serverButton;
	private JButton clientButton;
	private JButton serverConnect;
	private JButton privateMessageButton;
	private JButton enterCommand;
	private JPanel firstPanel;
	private JLabel connectedUsers;
		
	/**
	 * Constructs the basic GUI that asks the user if they are a server or client 
	 */
	public GameGUI()
	{
		super("Chat Client");
		setSize(200,100);
		firstPanel = new JPanel();

		// Ask if the user is a server or client
		firstPanel.setLayout(new FlowLayout());
		JLabel welcomeLabel = new JLabel("Are you a server or client? ");
		serverButton = new JButton("Server");
		serverButton.addActionListener(this);
		clientButton = new JButton("Client");
		clientButton.addActionListener(this);

		firstPanel.add(welcomeLabel);
		firstPanel.add(serverButton);
		firstPanel.add(clientButton);

		add(firstPanel);
		setLocationRelativeTo(null);
		setVisible(true);
		
		clientButton.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				setDefaultGUI(1);
			}
		});
		
		serverButton.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				userName = "Host";
				setServerGUI();
			}
		});	
	}
	
	/**
	 * Sets up the initial server GUI to connect to a port
	 */
	private void setServerGUI()
	{
		remove(firstPanel); //Remove the first panel
		setSize(300,200);

		// Initialize the new panel
		JPanel serverPanel = new JPanel(new GridLayout(3,1));
		add(serverPanel);

		JLabel serverWelcome = new JLabel("Enter the port you will connect to");
		serverPanel.add(serverWelcome,SwingConstants.CENTER);

		// Show the port being connected to. Currently set to 0 (default)
		portTextField = new JTextField();
		portTextField.setText("0");
		serverPanel.add(portTextField);

		//connect button....this will bring up a new screen if clicked
		serverConnect = new JButton("Connect");
		
		serverPanel.add(serverConnect);
		serverConnect.addActionListener(this);

		validate();//change the window
		repaint();	
	}
		
	/**
	 * Sets up the default game GUI
	 * 
	 * @param type	0 = server, 1 = client
	 */
	private void setDefaultGUI(int type)
	{
		
		if (type == 1)
		{
			remove(firstPanel);
			setUsername(); //set username if client
		}
		
		//---------------------------------------------------------
		// Set up the buttons for the window
		//---------------------------------------------------------
		drawButton = new JButton("Draw Card");
		drawFromDiscardButton = new JButton("Draw Discarded");
		startGameButton = new JButton("Start Game");
		clientConnectButton = new JButton("Connect");
		disconnectButton = new JButton("Disconnect");
		privateMessageButton = new JButton("Private Message");
	
		// Set up the action listeners
		drawButton.addActionListener(this);
		drawFromDiscardButton.addActionListener(this);
		privateMessageButton.addActionListener(this);
		startGameButton.addActionListener(this);
		clientConnectButton.addActionListener(this);
		disconnectButton.addActionListener(this);
		
		// Add the buttons to a Panel
		button_Panel = new JPanel(new GridLayout(2,3));
		button_Panel.add(drawButton);
		button_Panel.add(drawFromDiscardButton);
		button_Panel.add(privateMessageButton);
		if (type == 0)
			button_Panel.add(startGameButton);
		else
			button_Panel.add(clientConnectButton);
		button_Panel.add(disconnectButton);

		JLabel portLabel = new JLabel();
		portLabel.setHorizontalAlignment(SwingConstants.CENTER);
		if (type == 0)
			portLabel.setText("Port: " + serverPort);
		else
			portLabel.setText("Port: " + clientPort);
		button_Panel.add(portLabel);
		
		//---------------------------------------------------------
		// Set up the menu bar and the window
		//---------------------------------------------------------
		superFrame = new JFrame();
		superFrame.setResizable(false);
		
		if (type == 0)
			superFrame.setTitle("Host");
		else 
			superFrame.setTitle(userName);
		
		mainMenuBar = new JMenuBar();
		superFrame.setJMenuBar(mainMenuBar);
		
		gameMenu = new JMenu("Game");		
		mainMenuBar.add(gameMenu);

		exitItem = new JMenuItem("Exit");
		aboutItem = new JMenuItem("About");
		helpItem = new JMenuItem("Help");
		
		exitItem.addActionListener(this);
		aboutItem.addActionListener(this);
		helpItem.addActionListener(this);
		
		gameMenu.add(helpItem);
		gameMenu.add(aboutItem);
		gameMenu.add(exitItem);
		
		
		//---------------------------------------------------------
		// Set up the rest of the various GUI features
		//---------------------------------------------------------
		Panel_East=new JPanel();
		connectionPanel=new JPanel();
		Panel_South=new JPanel();
		Panel_North=new JPanel();
		Panel_Center=new JPanel();

		superFrame.setLayout(new BorderLayout());
		superFrame.add(Panel_East,BorderLayout.EAST);
		superFrame.add(connectionPanel,BorderLayout.WEST);
		superFrame.add(Panel_South,BorderLayout.SOUTH);
		superFrame.add(Panel_North,BorderLayout.NORTH);
		superFrame.add(Panel_Center,BorderLayout.CENTER);
		
		//center panel is the game panel
		Panel_Center.setSize(200, 500);
		
		playArea = new JTextArea();
		playArea.setEditable(false);
		JScrollPane playScrollPane = new JScrollPane(playArea);
		playScrollPane.setPreferredSize(new Dimension(200,200));
		
		Panel_Center.setLayout(new BorderLayout());
		Panel_Center.add(button_Panel, BorderLayout.NORTH);
		Panel_Center.add(playScrollPane, BorderLayout.SOUTH);
		
		melds_Panel = new JPanel();
		melds_Panel.setSize(200, 100);
		
		enterCommand = new JButton("Enter Command");
		enterCommand.addActionListener(this);
		
		JPanel gamePanel = new JPanel(new BorderLayout());
		gameInteractionArea = new JTextArea("Type Commands Here");
		JScrollPane scroll = new JScrollPane(gameInteractionArea);
		
		JPanel commandPanel = new JPanel(new BorderLayout());
		commandPanel.add(enterCommand, BorderLayout.EAST);
		commandPanel.add(scroll, BorderLayout.CENTER);
		
		gamePanel.add(commandPanel, BorderLayout.SOUTH);
		gamePanel.add(melds_Panel, BorderLayout.CENTER);
		Panel_Center.add(gamePanel, BorderLayout.CENTER);
		
		//add message display area on the east side
		messageArea = new JTextArea("Messages:\n");
		messageArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(messageArea,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(300, 500));
        Panel_East.add(scrollPane);
        
        //create a new JPanel to keep track of current connections
		connectionPanel.setPreferredSize(new Dimension(200,500));
		Border connectionPanelBorder = BorderFactory.createTitledBorder("Connected Users");
		connectionPanel.setBorder(connectionPanelBorder);
		connectionPanel.setBackground(Color.WHITE);
		connectedUsers = new JLabel();
		connectionPanel.add(connectedUsers);
        
		//add text typing field on the south side
		//create text area where messages will be typed
		clientMessageArea = new JTextArea();
		Border messageBorder = BorderFactory.createLineBorder(Color.black);
		clientMessageArea.setBorder(messageBorder);
		clientMessageArea.setText("Type Message Here");
		JScrollPane scrollPane3 = new JScrollPane(clientMessageArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS
				, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane3.setPreferredSize(new Dimension(800,25));
		
		sendButton = new JButton("Send");
		sendButton.addActionListener(this);
		Panel_South.add(scrollPane3);
		Panel_South.add(sendButton);
		
		superFrame.setSize(1000, 600);
		superFrame.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width-superFrame.getSize().width)/2,
				(Toolkit.getDefaultToolkit().getScreenSize().height-superFrame.getSize().height)/2);
			
		superFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		superFrame.validate();
		superFrame.setVisible(true);
	}
	
	/**
	 * Implements all the various actions in from the GUI
	 */
	public void actionPerformed(ActionEvent e) 
	{
		if (e.getSource() == exitItem) { System.exit(0);}
		
		if (e.getSource() == aboutItem)
		{
			JOptionPane.showMessageDialog(null, "Authors:\n Adam Socik\n Ryan Szymkiewicz\n Joshua Maravelias\n Tongtong Liu");
		}
		
		// Show rules
		if (e.getSource() == helpItem)
		{
			String message = "To play enter a command and the number(s) of the card\n"
					+ "to be acted upon separated by a space. Examples:\n\n"
					+ "Hand: 0   1   2   3   4\n"
					+ "          3C 4S 4H 4C 7D\n\n"
					+ "Commands:\n"
					+ "M = meld\tExample: \"M 2 3 4\"\n"
					+ "L = lay off\tExample: \"L a 4\"\n"
					+ "                                    ^meld to add to if there is one\n"
					+ "D = discard\tExample: \"D 0\"\n"
					+ "Discard 1 card to end your turn\n\n";
			
			JOptionPane.showMessageDialog(null, message);
		}
		
		if (e.getSource() == enterCommand)
		{
			
		}
		
		// Start up server
		if (e.getSource() == serverConnect)
		{
			String tempPort = portTextField.getText();
			serverPort = Integer.parseInt(tempPort);
			server = new ServerHandle(serverPort);
			System.out.println(server.getPort());	
			serverPort = server.getPort();
			remove(this);
			setDefaultGUI(0);
			client = new Client(serverPort, this);
		}
		
		// Send a message as a client
		if (e.getSource() == sendButton)
		{
			String message = clientMessageArea.getText();
			
			try 
			{
				client.send("0", message);
				clientMessageArea.setText("");	// Clears out old text
			} 
			catch (IOException e1)
			{
				e1.printStackTrace();
			}
			
		}
		
		// Disconnect as a client
		if(e.getSource() == disconnectButton)
		{
			try 
			{
				client.disconnect();
			} 
			catch (IOException e1) 
			{
				e1.printStackTrace();
			}
		}
		
		// Connect as a client
		if(e.getSource() == clientConnectButton)
		{
			client = new Client(clientPort, this);
		}
		
		if (e.getSource() == startGameButton)
		{
			try {
				client.startGame(server.getUserCounter());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			
		}
		
		//private message as a client
		//this code was modified from ideas found at:
		//http://stackoverflow.com/questions/6555040/
		//multiple-input-in-joptionpane-showinputdialog
		//The fields are initialized as JTextFields that are placed into
		//a JPanel
		//This panel has a gridLayout and the previous fields are added to the
		//panel.  Then JOptionPane is passed this panel and a 
		//window with the fields and "Cancel" and "OK" appears.
		//If text is entered and OK is pressed,  a private message should be sent
		//this code was modified from the original source
		//through the different text areas that are needed and the way
		//that the panel was set up.
		if(e.getSource() == privateMessageButton)
		{
			JTextField people = new JTextField(25);
			JTextField message = new JTextField(50);
			JLabel peopleLabel = new JLabel("Enter the people you wish to \n"
					+ "chat with separated with a space\n.");
			JLabel messageLabel = new JLabel("Enter your message \n");

			JPanel privateMessagePanel = new JPanel(new GridLayout(4,1));
			privateMessagePanel.add(peopleLabel);
			privateMessagePanel.add(people);

			privateMessagePanel.add(messageLabel);
			privateMessagePanel.add(message);

			int check = JOptionPane.showConfirmDialog(
							null,
							privateMessagePanel,
							"Enter your message and recipients",
							JOptionPane.OK_CANCEL_OPTION);
			if(check == JOptionPane.OK_OPTION){
				privateMessagePeople=people.getText();
				privateMessageContent=message.getText();
				
				privateMessagePeople += " " + userName; // Add so sender can also see message
				
				try { client.send(privateMessagePeople, privateMessageContent); } 
				catch (IOException e1) { e1.printStackTrace(); }
			}
		}
	}
	
	/**
	 * Prints the string s to play area - prints any game relevant information
	 * 
	 * @param s
	 */
	public void printToPlayArea(String s)
	{
		playArea.append(s);
	}
	
	/**
	 * Notify player that card was drawn
	 * 
	 * @param from 0 = deck, 1 = discards pile 
	 */
	public void draw(int from) 
	{
		//player.drawCard(from);
	}

	public void setDiscardBtnText(String s)
	{
		drawFromDiscardButton.setText("Draw " + s);
	}

	public JTextArea getPlayArea()
	{
		return playArea;
	}

	public JTextArea getGameInteractionArea()
	{
		return gameInteractionArea;
	}
	
	/***************************
	 * The setUsername method sets the username for a client based
	 * on what they have entered or generates a random username if 
	 * they do not select one
	 */
	private void setUsername()
	{
		//initilize the labels and panel,  add the items
		JLabel nameLabel = new JLabel("Enter your username");
		JTextField nameEnter = new JTextField();
		JLabel portLabel = new JLabel("Enter the port");
		JTextField portEnter = new JTextField();
		JPanel namePanel = new JPanel(new GridLayout(4,1));
		namePanel.add(nameLabel);
		namePanel.add(nameEnter);
		namePanel.add(portLabel);
		namePanel.add(portEnter);

		//use JOptionPane to get the username with an "OK-Cancel" popup
		int option =
				JOptionPane.showConfirmDialog(
						null,
						namePanel,
						"Enter your username and port",
						JOptionPane.OK_CANCEL_OPTION);

		String tUserName = nameEnter.getText();//temporarily set the username
		String tPort = portEnter.getText();//get temporary port

		if(option == JOptionPane.OK_OPTION && !tUserName.isEmpty() &&
				!tPort.isEmpty()){

			userName = tUserName;//if user pressed OK and field is valid,  set name
			clientPort = Integer.parseInt(tPort);//set port
		}

		//if the user pressed cancel or did not enter a name,
		//generate a random username
		else if(option == JOptionPane.CANCEL_OPTION || tUserName.isEmpty()){

			int num1,num2,num3;

			//three random integers from 0-99 are obtained.
			//There is an extremely small chance that two users could
			//have the same name,  but since this chat should not use more than
			//~5 clients,  the risk is very low.
			Random generator = new Random();
			num1 = generator.nextInt(99);
			num2 = generator.nextInt(99);
			num3 = generator.nextInt(99);

			//set the username with User followed by the 3 numbers
			userName = "User"+
					Integer.toString(num1)+
					Integer.toString(num2)+
					Integer.toString(num3);

			System.out.println(userName);
		}
		else if(tPort.isEmpty()){
			JOptionPane.showMessageDialog(namePanel, this,"Port Not Selected.  Quitting", option);
			System.exit(0);
		}
		namePanel.setName(userName);
	}

	/*****************
	 * These getters are used by the server and client
	 * portions of the program
	 * 
	 */
	public int getServerPort(){
		return serverPort;
	}

	public JLabel getConnectWindow(){
		return connectedUsers;	//member list
	}

	public JTextArea getChatWindow(){
		return messageArea;
	}

	public String getUsername(){
		return userName;
	}

	public void setMeldsPanel(JPanel newJPanel)
	{
		melds_Panel = newJPanel;
	}
	
	public JTextArea getCurrentLine(){
		return clientMessageArea;
	}

	public String getPrivateMessagePoeple(){
		String tempPeople = privateMessagePeople;
		privateMessagePeople=null;
		return tempPeople;
	}

	public String getPrivateMessageContents(){
		String tempMessage = privateMessageContent;
		privateMessageContent = null;
		return tempMessage;
	}
	
	
	public static void main(String[] args)
	{
		GameGUI gui = new GameGUI();
		gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE );
	}
	
}// end Class
	

