/**
 * @author Adam Socik
 */

import java.io.Serializable;

public final class CommandMessage  extends abstractMessage implements Serializable{

	private static final long serialVersionUID = 1L;
	public char command;
	public String message;
	public int id;
	
	/**
	 * @param Pay represents the payload to send out
	 */
	public CommandMessage(char command, int id, String message) 
	{
		super(MESSAGETYPE.COMMAND);
		this.command = command;
		this.id = id;
		this.message = message;
	}
}