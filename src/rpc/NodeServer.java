package rpc;

import info.DynamicInformation;
import info.ServerNodeInfo;
import java.io.*;
import java.net.*;
import java.util.*;

public class NodeServer implements Runnable {
	boolean started = false;
	ServerSocket ss = null;
	int port = 8880;
	List<Client> clients = new ArrayList<Client>();
	
	boolean isSend = false;
	
	Object clientsLock = new Object();
	
	public NodeServer()
	{
		
	}
	
	@Override
	public void run() {
		
		start();
		
	}

	public void start() {
		SendCommond sendcommond  =new SendCommond();
		sendcommond.start();
		try {
			ss = new ServerSocket(port);
			started = true;
			
		} catch (BindException e) {
			
			System.exit(0);
		} catch (IOException e) {
			
			System.exit(0);
		}
		
		try {
			
			while (started) {
				
				System.out.println("等待链接");
				Socket s = ss.accept();
				synchronized (clientsLock) {
					Client c = new Client(s);
					System.out.println("a client connected!");
					new Thread(c).start();
					clients.add(c);
				}
				
				// ois.close();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				ss.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	class SendCommond extends Thread
	{
		public void run() {
			while(true)
			{
				try {
					Thread.sleep(10000);
					System.err.println(clients.size());
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(clients.size()>0)
				{
					
						for(int i=0;i<clients.size();i++)
						{
					        clients.get(i).sendLaunchStaticAction(NodeAction.LAUNCH_STATIC_INFO);						
						}
			
				}
		
			}
	
		}
	}	
	class Client implements Runnable {
		private String hostName = "";
		private String updateTime =""; 
		private NodeAction action = null;
		private Socket s;
		private ObjectInputStream ois = null;
		private ObjectOutputStream oos = null;
		private boolean bConnected = false;
		
        
		boolean isclean = false;
		int num;
		public Client(Socket s) {
			
			this.s = s;
			try {
				ois = new ObjectInputStream(s.getInputStream());
				oos = new ObjectOutputStream(s.getOutputStream());
				bConnected = true;
				
			} catch (IOException e) {
				
				isclean = true;
				System.out.println("Io exception");
				//e.printStackTrace();
			}
		}
		
		
		public void sendLaunchStaticAction(int commond) {
			/*switch (commond) {
			case NodeAction.LAUNCH_STATIC_INFO:
				action = new LaunchDynamicAction();
				action.sendCommond(oos);
				break;

            case NodeAction.KILL_DYNAMIC_INFO:
				action = new KillDynamicAction();
				break;
            case NodeAction.KILL_STATIC_INFO:
				action = new KillStaticACtion();
				break;
			default:
				break;
			}*/
			try {
				oos.writeInt(NodeAction.LAUNCH_STATIC_INFO);
				oos.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}


		public void run() {
			try {
				
			while (bConnected) {
				
						DynamicInformation dynamicInformation;
						int commond = ois.readInt();
						switch (commond) {
						case NodeAction.LAUNCH_DYNAMIC_INFO:
							dynamicInformation = (DynamicInformation)ois.readObject();
							 System.out.println(dynamicInformation.toString());
							break;

						case NodeAction.LAUNCH_STATIC_INFO:
							
							ServerNodeInfo serverifo =(ServerNodeInfo)ois.readObject();
							System.out.println(serverifo.toString());
							break;
						}

					}
			    
			} catch (IOException e) {

				 System.out.println("IOException");
		 
			}catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally{
				
					try {
						if(ois!=null)
						   ois.close();
						if(oos!=null)
							oos.close();
						if(s!=null)
							s.close();
						
						isclean = true;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
			
		}
		
		public String getHostName() {
			return hostName;
		}

		public String getUpdateTime() {
			return updateTime;
		}

		public void setHostName(String hostName) {
			this.hostName = hostName;
		}

		public void setUpdateTime(String updateTime) {
			this.updateTime = updateTime;
		}

	}
public static void main(String[] args) {
	NodeServer server =new NodeServer();
	Thread t =new Thread(server);
	t.start();
	
}	
	
}