package Rummy;

import javax.swing.UIManager;

public class TestGame {
	
	public static void main(String[] args){
		try{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}catch(Exception e){
			System.out.print("The reason for failer is: "+e);
		}
		new GameGUI();
	}
	
}
