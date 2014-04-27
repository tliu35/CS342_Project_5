/**
 * @author Tongtong Liu
 * CS342 Project 5
 */
package Rummy;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

public class GameGUI implements ActionListener {

	private JFrame superFrame;
	
	private JPanel Panel_East,Panel_West,Panel_South,Panel_North;
	protected JPanel Panel_Center, melds_Panel, button_Panel;

	private JTextArea messageArea, peopleArea, playArea;
	private JTextArea clientMessageArea;
	
	private JTextField portTextField;
	
	private JMenu gameMenu, chatMenu, helpMenu, windowMenu;
	private JMenuBar mainMenuBar;
	private JMenuItem exitItem, startGameItem, startChatItem, exitChat, exitGame,helpItem, aboutItem;
	
	private JButton sendButton, drawButton, discardButton, startGameButton;
	private JButton serverConnectButton,serverDisconnectButton, serverPrivButton;
	private JButton clientConnectButton,clientDisconnectButton, clientPrivButton;
	
	private Player player;
	
	private String userName, portString;
	private int serverPort, clientPort;
	
	private String privateMessagePeople;
	private String privateMessageContent;
	private Client client;
	private ServerHandle server;

/***************************************************/	
	public GameGUI(){
		
		Object[] optionsObjects = {"Client", "Server"};
		int input = JOptionPane.showOptionDialog(null, "Please choose to start a Server or Client window",
				"Starting", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null,
				optionsObjects, optionsObjects[0]);
		//System.out.println(input);
		
		
		if(input == 1){
			// get the port number
			portString = JOptionPane.showInputDialog("Please enter a port number");
			serverPort = Integer.parseInt(portString);
			
			setServerGUI();
		}
		else if(input == 0){	// choose to be Client
			setUsername();	//get user name
			setClientGUI();
		}
		else if(input<0){
			System.exit(0);
		}	
		
	}// GameGUI();
	
	private void setServerGUI(){
		// set up the buttons for server

				drawButton = new JButton("New Card");
				discardButton = new JButton("Discarded Card");
				startGameButton = new JButton("Start Game");
				
				//chat related button
				serverConnectButton = new JButton("Connect");
				serverConnectButton.addActionListener(this);
				
				serverDisconnectButton = new JButton("Disconnect");
				serverDisconnectButton.addActionListener(this);
				
				serverPrivButton = new JButton("Private Chat");
				serverPrivButton.addActionListener(this);
				
				
				button_Panel = new JPanel(new GridLayout(2,3));
				button_Panel.add(drawButton);
				button_Panel.add(discardButton);
				button_Panel.add(startGameButton);
				button_Panel.add(serverConnectButton);
				button_Panel.add(serverDisconnectButton);
				button_Panel.add(serverPrivButton);
				
				String displayString = "Server "+portString;
				
				commonGUI(displayString);
				
		
	}
		
	private void setClientGUI(){
		
		// set up the buttons for server
		drawButton = new JButton("New Card");
		discardButton = new JButton("Discarded Card");
		clientConnectButton = new JButton("Connect");
		clientDisconnectButton = new JButton("Disconnect");
		clientPrivButton = new JButton("Private Chat");
		
		button_Panel = new JPanel(new GridLayout(2,3));
		button_Panel.add(drawButton);
		button_Panel.add(discardButton);
		button_Panel.add(clientConnectButton);
		button_Panel.add(clientDisconnectButton);
		button_Panel.add(clientPrivButton);
		
		commonGUI(userName);
	}
		
	private void commonGUI(String display){
		//choose server
		
				superFrame = new JFrame(display);
				superFrame.setResizable(false);
				
				mainMenuBar = new JMenuBar();
				superFrame.setJMenuBar(mainMenuBar);
				
				gameMenu = new JMenu("Game(G)");
				gameMenu.setMnemonic('G');
				chatMenu = new JMenu("Chat(C)");
				chatMenu.setMnemonic('C');
				helpMenu = new JMenu("Help(H)");
				helpMenu.setMnemonic('H');
				windowMenu = new JMenu("Window(W)");
				windowMenu.setMnemonic('W');
				
				mainMenuBar.add(gameMenu);
				mainMenuBar.add(chatMenu);
				mainMenuBar.add(helpMenu);
				mainMenuBar.add(windowMenu);
				
				exitItem = new JMenuItem("Exit ALL");
				exitItem.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						//may be need to exit chat and game before exit all
						System.exit(0);
					}
				});
				
				exitChat = new JMenuItem("Exit Chat");
				exitChat.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						
					}
				});
				
				exitGame = new JMenuItem("Exit Game");
				exitGame.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						
					}
				});
				
				startChatItem = new JMenuItem("Start Chat");
				startChatItem.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						
					}
				});
				
				startGameItem = new JMenuItem("New Game");
				startGameItem.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						
					}
				});
				
				helpItem = new JMenuItem("Help(V)");
				helpItem.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						JOptionPane.showMessageDialog(null," Needs Help?");

					}
				});
				
				aboutItem = new JMenuItem("About(A)");
				aboutItem.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						JOptionPane.showMessageDialog(null, "Made by:\n Adam Socik\n Ryan Szymkiewicz\n Joshua Maravelias\n Tongtong Liu");
					}
				});
				
				gameMenu.add(startGameItem);
				gameMenu.add(exitGame);
				
				chatMenu.add(startChatItem);
				chatMenu.add(exitChat);
				
				helpMenu.add(helpItem);
				helpMenu.add(aboutItem);
				
				windowMenu.add(exitItem);
				
				//panels	
				Panel_East=new JPanel();
				Panel_West=new JPanel();
				Panel_South=new JPanel();
				Panel_North=new JPanel();
				Panel_Center=new JPanel();

				superFrame.setLayout(new BorderLayout());
				superFrame.add(Panel_East,BorderLayout.EAST);
				superFrame.add(Panel_West,BorderLayout.WEST);
				superFrame.add(Panel_South,BorderLayout.SOUTH);
				superFrame.add(Panel_North,BorderLayout.NORTH);
				superFrame.add(Panel_Center,BorderLayout.CENTER);
				
				//center panel is the game panel
				Panel_Center.setSize(200, 500);
				
				playArea = new JTextArea();
				JScrollPane playScrollPane = new JScrollPane(playArea,
						JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
						JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				playScrollPane.setPreferredSize(new Dimension(200,200));
				
				Panel_Center.setLayout(new BorderLayout());
				Panel_Center.add(playScrollPane, BorderLayout.SOUTH);
				Panel_Center.add(button_Panel, BorderLayout.NORTH);
			
				
				melds_Panel = new JPanel();
				melds_Panel.setSize(200, 100);
				melds_Panel.setBackground(Color.cyan);
				Panel_Center.add(melds_Panel, BorderLayout.CENTER);
				
				
				//add message display area on the east side
				messageArea = new JTextArea("Chatting\n");
				messageArea.setEditable(false);
		        JScrollPane scrollPane = new JScrollPane(messageArea,
		                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
		                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		        scrollPane.setPreferredSize(new Dimension(300, 500));
		        Panel_East.add(scrollPane);
		        
		        // add online people display area on the west side
				peopleArea = new JTextArea("Online member\n");
				peopleArea.setEditable(false);
				JScrollPane scrollPane2 = new JScrollPane(peopleArea,
						JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
						JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				scrollPane2.setPreferredSize(new Dimension(200,500));
				Panel_West.add(scrollPane2);
				
				//add text typing field on the south side
				//create text area where messages will be typed
				clientMessageArea = new JTextArea();
				Border messageBorder = BorderFactory.createLineBorder(Color.black);
				clientMessageArea.setBorder(messageBorder);
				clientMessageArea.setText("Type Here");
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
	} //common GUI
		

	
	// @author Adam Socik

	/**
	 * Notify player that card was drawn
	 * 
	 * @param from 0 = deck, 1 = discards pile 
	 */
	public void draw(int from) 
	{
		player.drawCard(from);
	}

	public void setDiscardBtnText(String s)
	{
		discardButton.setText("Draw " + s);
	}

	public JTextArea getPlayArea()
	{
		return playArea;
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

			System.out.print(userName);
			System.out.print(clientPort);

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

	public JPanel getConnectWindow(){
		return Panel_West;	//member list
	}

	public JTextArea getChatWindow(){
		return messageArea;
	}

	public String getUsername(){
		return userName;
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

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

	/*****************
	 * This method allows for interaction when buttons are pressed
	 */
/*	public void actionPerformed(ActionEvent e) {

		//if using as server,  after connnect has been pressed,
		//set the port to the class variable and switch panels
		//that displays the port
		if(e.getSource() == serverConnectButton){
			server = new ServerHandle(0);
			String tempPort = portTextField.getText();
			serverPort = Integer.parseInt(tempPort);
			remove(serverPanel);
			serverInfoPanel = new JPanel();
			add(serverInfoPanel);
			JLabel info = new JLabel("You are connected to port: "+ server.getPort());
			serverInfoPanel.add(info);
			System.out.println(server.getPort());
			validate();//change the window
			repaint();
		}

		//exit the window
		else if(e.getSource() == clientExit){
			System.exit(0);
		}

		//connect as a client
		else if(e.getSource() == clientConnect)
		{
			client = new Client(clientPort, this);
		}

		//disconnect as a client
		else if(e.getSource() == clientDisconnect){
			try 
			{
				client.disconnect();
			} 
			catch (IOException e1) 
			{
				e1.printStackTrace();
			}
		}

		//send a message as a client
		else if(e.getSource() == clientSend)
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
		else if(e.getSource() == clientMultipleMessages){
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
		else{
			System.out.println("Problem with button press");
		}

	}
	
	*/
}// end Class
	

