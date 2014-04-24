package Rummy;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ServerGUI {
	
	JFrame serverFrame;
	
	JPanel chatJPanel, listJPanel, messageJPanel ;
	
	JTextArea messageArea, clientLisTextArea;
	JTextField messageJTextField;
	
	JButton sendButton;
		
	public ServerGUI(){
		
		serverFrame = new JFrame("Server");
		serverFrame.setSize(650, 500);
		serverFrame.setResizable(false);
		
		messageJPanel = new JPanel();
		listJPanel = new JPanel();
		chatJPanel = new JPanel();
		
		messageJTextField = new JTextField(40);
		sendButton = new JButton("Send");
		sendButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		messageArea = new JTextArea("Chatting\n");
		messageArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(messageArea,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(400, 500));
        
        clientLisTextArea = new JTextArea("Online member\n");
		clientLisTextArea.setEditable(false);
		JScrollPane scrollPane2 = new JScrollPane(clientLisTextArea,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane2.setPreferredSize(new Dimension(200,500));
		
		serverFrame.setLayout(new BorderLayout());
		serverFrame.add(messageJPanel, BorderLayout.SOUTH);
		serverFrame.add(listJPanel,BorderLayout.WEST);
		serverFrame.add(chatJPanel, BorderLayout.EAST);

		chatJPanel.add(scrollPane);
		listJPanel.add(scrollPane2);
		messageJPanel.add(messageJTextField);
		messageJPanel.add(sendButton);
		
		serverFrame.validate();
		serverFrame.setVisible(true);
	}

}
