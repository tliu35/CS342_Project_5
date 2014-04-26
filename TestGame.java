
import javax.swing.JOptionPane;
import javax.swing.UIManager;

public class TestGame 
{
	public static void main(String[] args)
	{
		try{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}catch(Exception e){
			System.out.print("The reason for failer is: "+e);
		}
		Object[] optionsObjects = {"Client", "Server"};
		int input = JOptionPane.showOptionDialog(null, "Please choose to start a Server or Client window",
				"Starting", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null,
				optionsObjects, optionsObjects[0]);
		System.out.println("input is "  + input);
		if(input == 1){
			new ServerGUI();
		}
		else{
			GameGUI gui =  new GameGUI();
			//new GameHandle(5, gui); // for testing
		}
	}
	
}
