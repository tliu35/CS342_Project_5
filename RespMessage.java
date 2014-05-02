import java.io.Serializable;

/**
 * @author jmarav3
 *
 */
public final class RespMessage  extends abstractMessage implements Serializable{

	private static final long serialVersionUID = 1L;
	private String Payload;
	private int nUsers;
	
	/**
	 * @param Pay represents the payload to send out
	 */
	public RespMessage(String Pay, int users) 
	{
		super(MESSAGETYPE.RESP);
		Payload = Pay;
		nUsers = users;
	}
	
	public String getPayload()
	{ return Payload; }
	
	public int getnUsers()
	{
		return nUsers;
	}
}
