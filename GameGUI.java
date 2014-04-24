/**
 * @author Tongtong Liu
 */
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class GameGUI {

	private JFrame superFrame;
	
	private JPanel Panel_East,Panel_West,Panel_South,Panel_North;
	protected JPanel Panel_Center;

	private JTextArea messageArea, peopleArea;
	private JTextField sendingMessage;
	
	private JMenu gameMenu, chatMenu, helpMenu, windowMenu;
	private JMenuBar mainMenuBar;
	private JMenuItem exitItem, startGameItem, startChatItem, exitChat, exitGame,helpItem, aboutItem;
	
	private JButton sendButton;
	
	public GameGUI(){
		superFrame = new JFrame("Rummy & Chat");
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
		Panel_Center.setBackground(Color.green);
		
		//add message display area on the east side
		messageArea = new JTextArea("Chatting\n");
		messageArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(messageArea,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(200, 500));
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
		sendingMessage = new JTextField(50);
		sendButton = new JButton("Send");
		sendButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		Panel_South.add(sendingMessage);
		Panel_South.add(sendButton);
		
		superFrame.setSize(800, 600);
		superFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		superFrame.validate();
		superFrame.setVisible(true);
	}
	
}
