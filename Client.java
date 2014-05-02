/**------------------------------------------------------------------------
 * 		@author Adam Socik
 * 		April 2014
 * 		CS 342 Software Design
 * 
 * This class connects to the client GUI to display any new messages and 
 * handles communication with the sever.
 * ------------------------------------------------------------------------*/
import java.io.*;
import java.net.*;
import java.util.Vector;

import javax.swing.JOptionPane;

public class Client extends Thread
{
   	private Socket socket;
	private ObjectOutputStream out = null;
    private ObjectInputStream in = null;
   	private abstractMessage message;
   	private GameGUI gui;
   	private Player player;
   	public int nClients;
   	private int id;
    
   	public Deck deck;
	public Vector<Player> players;
   	
	public Client(int port, GameGUI gui)
	{
		this.gui = gui;
		
		// Connect to server
		try 
		{
			socket = new Socket("127.0.0.1", port);
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());	
			
			this.start();
		} 
		catch (IOException e) 
		{
			JOptionPane.showMessageDialog(null, "Server not responding");
			System.exit(0);
		}
	}
	
	/**
	 * Communicates messages with the server
	 */
	@Override
	public void run() 
	{
		// Send a message to the server to set the username
		try 
		{
			out.writeObject(new CallMe(gui.getUsername()));
			out.flush();
		} 
		catch (IOException e1) { e1.printStackTrace(); }
		
		// Read messages from the server
		while(true)
		{
			try 
			{
				message = (abstractMessage) in.readObject();
				
				// Receive message from the server and output to GUI
				if (message.getType() == abstractMessage.MESSAGETYPE.SCHAT)	 
				{
					SchatMessage serverMessage = (SchatMessage) message;
					gui.getChatWindow().append(serverMessage.getFrom() + ": " + serverMessage.getBody() + "\n");
				}
				
				// Booted by the server
				else if (message.getType() == abstractMessage.MESSAGETYPE.DEAD)	
				{
					disconnect();
				}
				
				// Update the list of users
				else if (message.getType() == abstractMessage.MESSAGETYPE.RESP) 
				{
					RespMessage msg = (RespMessage) message;
					System.out.println(msg.getPayload());
					gui.getConnectWindow().setText(msg.getPayload());
					gui.validate();
					gui.repaint();
					nClients = msg.getnUsers();
				}
				
				else if (message.getType() == abstractMessage.MESSAGETYPE.COMMAND)
				{
					CommandMessage cmd = (CommandMessage) message;
					
					if (cmd.id != 0)
					{
						gui.printToPlayArea(cmd.message);
					}
					
					// Only the host gets these messages
					if (cmd.command == 'P')
					{
						gui.printToPlayArea(players.get(cmd.id).printHand());
					}
					
					
					
				}
				
			} 
			catch (ClassNotFoundException | IOException e) 
			{
				e.printStackTrace();
			}
		}	
	}
	
	/**
	 * Disconnects the user from the server - called from the chatGUI class
	 * 
	 * @throws IOException
	 */
	public void disconnect() throws IOException
	{
		out.writeObject(new CchatMessage("0", " disconnected from the server"));
		out.flush();

		JOptionPane.showMessageDialog(null, "You disconnected from the server");
		System.exit(0);
	}
	
	/**
	 * Sends a message to the server
	 * 
	 * @param message 
	 * @throws IOException 
	 */
	public void send(String to, String body) throws IOException
	{
		out.writeObject(new CchatMessage(to,body));
		out.flush();
	}
	

	
		
	
	public void startGame(int nPlayers) throws IOException
	{
		deck = new Deck();
		players = new Vector<Player>();
		
		
		for (int i=0; i<nPlayers; i++)
			players.add(new Player(deck));
		deck.distribute(players);
		
		send("0", "Game has started, Host's turn");
		
		out.writeObject(new CommandMessage('P', 0, players.get(1).printHand()));
		out.flush();
		
	}
	
	
	
	
	
	
	
	
}


