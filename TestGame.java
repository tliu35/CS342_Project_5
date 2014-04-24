
import javax.swing.UIManager;

public class TestGame 
{
	public static void main(String[] args)
	{
		GameHandle gameHandle = new GameHandle(5); // for testing
		
		try{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}catch(Exception e){
			System.out.print("The reason for failer is: "+e);
		}
		new GameGUI();
	}
	
}
