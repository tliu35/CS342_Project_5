import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ServerHandle extends Thread{
	
	private ServerSocket serverSocket = null;
	private Vector<clientThread> clientThreads = null;
	private int portNum;
	private int userCounter; // an implicit naming convention
	private boolean serverContinue;
	private WorkerThread worker;
	public enum JOBTYPE{ //for job class
		MSG //send message
	}
		
	public ServerHandle(int port) 
	{
		try {
			serverSocket = new ServerSocket(port);
			portNum = serverSocket.getLocalPort();
			System.out.println("Listening on port# " + portNum);
		} catch (IOException e) {
			System.err.println("Could not listen on port" + port);
			System.exit(-1);
		}
		clientThreads = new Vector<clientThread>();
		
		worker = new WorkerThread();
		worker.start();
		
		serverContinue = true;
		userCounter = 0;
		
		this.start();
	}
	
	public void run(){
		System.out.println("Running server thread");
		while(serverContinue){
			try {
				serverSocket.setSoTimeout(10000);
				Socket addSocket = serverSocket.accept(); // blocking read
				userCounter++;
				String addUserName = "User" + Integer.toString(userCounter);
				clientThread toAdd = new clientThread(addSocket, addUserName ,worker);
				clientThreads.add(toAdd);
				//nameToClient.put(toAdd.getUserName(),toAdd);//add userName to lookup
				toAdd.start();
				

				System.out.println("Added user" + toAdd.getUserName());
				
			} catch(SocketTimeoutException ste){
				System.out.println("Socket accept user timed out...");
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}//while
	}
	
	public int getPort(){
		return portNum;
	}
	
	public int getUserCounter()
	{
		return userCounter;
	}

	private class clientThread extends Thread{
		private Socket clientSocket;
		private String userName;
		public ObjectOutputStream out;
		private ObjectInputStream in;
		private WorkerThread worker;
		private Object Namelock = new Object();
		
		public clientThread(Socket r, String name, WorkerThread worker) throws ClassNotFoundException
		{
			clientSocket = r;
			this.userName = name;
			this.worker = worker;
			
			try {
				out = new ObjectOutputStream( clientSocket.getOutputStream());
				in = new ObjectInputStream( clientSocket.getInputStream());
				
				// Get user selected username 
				abstractMessage message = (abstractMessage) in.readObject();
				CallMe msg = (CallMe) message;
				
				userName = msg.getName();
				
				worker.JobQueue.add(new Job(new SchatMessage("Server","Your user name has been set to " + userName), userName));
				out.flush();
			} catch (IOException e) {
				System.err.println("Could not open socket in/out.");
			}
		}
		
		public void run(){
			System.out.println("Starting user thread for " + userName);
			
			//send out the name of everyone
			announceUsers();
			
			while(true)
			{
				//check socket
				try {
					abstractMessage message = (abstractMessage) in.readObject();
					if(message.getType() == abstractMessage.MESSAGETYPE.CCHAT)
					{
						CchatMessage tempMessage = (CchatMessage) message;
						sendMessagesToWorker(tempMessage);
					}
					
					if(message.getType() == abstractMessage.MESSAGETYPE.COMMAND)
					{
						CommandMessage m = (CommandMessage) message;
						
						if (m.command == 'P')
						{
							worker.JobQueue.add(new Job(m, clientThreads.get(m.id).getUserName()));
						}
					}
					
					
				} catch(EOFException e){
					try {
						out.close();
						in.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					clientThreads.remove(this);
					announceUsers();
					break;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			System.out.println("Thread is dying for " + userName);
		}
		
		private void announceUsers(){
			//gather all names
			synchronized(Namelock){
				
				int count = 0;
				
				// Use HTML to add newline characters for JLable 
				String allUsers = new String("<html>");
				for(clientThread t : clientThreads)
				{
					allUsers += t.getUserName() + "<br>";
					count++;
				}
				allUsers+="</html>";
				
				//send out the messages
				for(clientThread t : clientThreads){
					worker.JobQueue.add(new Job(new RespMessage(allUsers, count),t.getUserName()));
					System.out.println("Sending <<" + allUsers + ">> to <<"+t.getUserName()+">>");
				}
			}
		}
		
		private void sendMessagesToWorker(CchatMessage MSG)
		{
			
			// Send message to everyone
			if(MSG.getTo().equals("0"))
			{
				for(clientThread e : clientThreads){
					System.out.println("Sending <<" + MSG.getBody() + ">> to <<" + e.getUserName() + ">>");					
					worker.JobQueue.add(new Job(new SchatMessage(this.getUserName(),MSG.getBody()),
							e.getUserName()));
				}//for each
			}
			
			// Send messaged to specified users
			else
			{
				// Split the string into usernames
				String to[] = MSG.getTo().split("\\s+");
				
				for(clientThread e : clientThreads)
				{
					// Check to see if current username is one of the specified ones 
					for (int i=0; i<to.length; i++)
					{
						if (to[i].equals(e.getUserName()))
						{
							System.out.println("Sending <<" + MSG.getBody() + ">> to <<" + e.getUserName() + ">>");
							worker.JobQueue.add(new Job(new SchatMessage(this.getUserName(),MSG.getBody()), e.getUserName()));
						}
					}
				}
			}
		}
		
		public String getUserName(){
			synchronized(Namelock){
				return userName;
			}
		}
		
		
	}
	
	private class WorkerThread extends Thread{
		public ConcurrentLinkedQueue<Job> JobQueue;
		
		public WorkerThread(){
			JobQueue = new ConcurrentLinkedQueue<Job>();
		}
		
		public void run(){
			System.out.println("Worker queue starting");
			
			while(true){
				Job working = JobQueue.poll();
				if(working != null){
					if(working.getType() == JOBTYPE.MSG){
						sendMessage(working.getMSG(), working.getTo());
					}
				}
			}//while
		}
		
		public void sendMessage(abstractMessage MSG,String to){
			clientThread sendTo = null;
			
			for( clientThread t : clientThreads){
				if(t.getUserName().equals(to)){
					sendTo = t;
					break;
				}
			}
			
			try {
				sendTo.out.writeObject(MSG);
				sendTo.out.flush();
			} catch (IOException e) {
				System.out.println("Unable to send message");
				System.exit(-1);
			} catch(NullPointerException e){
				System.out.println("No User " + to);
			}
		}
	}
	
	private final class Job{
		private final JOBTYPE type;
		private final abstractMessage message;
		private final String to;
		
		/**
		 * 
		 * @param MSG The message to send out
		 * @param sendto The recipient of the message
		 */
		public Job(abstractMessage MSG,String sendto){
			type = JOBTYPE.MSG;
			message = MSG;
			to = sendto;
		}
		
		public JOBTYPE getType()
		{ return type; }
		
		public abstractMessage getMSG()
		{ return message; }
		
		public String getTo()
		{ return to; }
		
	}
}













