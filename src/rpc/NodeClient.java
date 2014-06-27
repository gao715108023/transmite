package rpc;


import info.DynamicInformation;
import info.InformationCollection;
import info.ServerNodeInfo;
import info.ServerNodeInfoCollection;
import java.io.*;
import java.net.*;
import java.util.Random;


public class NodeClient implements Runnable{
	Socket s = null;
	ObjectOutputStream oos = null;//向服务器写数据
	ObjectInputStream ois = null; //接受服务器法国来的数据
	private boolean bConnected = false; //是否继续监听server发过来的数据
	String hostip ="127.0.0.1"; //服务器的主机名
	int port = 8880;
    private int  collectionTimeInteval = 3*1000; //控制每隔时间间隔传一次
    
    private boolean isTransimate = false;//控制是否向server传数据
    
    volatile boolean running = true;
    volatile boolean shuttingDown = false;
     
	Object commondLock = new Object();
 	
    String hostName = new String(new Random().nextInt(10000)+"");
    String updateTime ="";
    InformationCollection informateCollection ;//暂时用来采集
    
    ServerNodeInfoCollection serverNodeInfoCellection ; //暂时用来采集servernodeinfo
    
    
   
    
    int commond = NodeAction.LAUNCH_DYNAMIC_INFO;
    
    public  NodeClient(InformationCollection informateCollection,
    		ServerNodeInfoCollection serverNodeInfoCellection) {
    	this.informateCollection = informateCollection;
		informateCollection.dynamicThreadStart();
		this.serverNodeInfoCellection = serverNodeInfoCellection; 
	}

	public void connect() {
		try {
			s = new Socket(hostip, port);
			oos = new ObjectOutputStream(s.getOutputStream());
			ois = new ObjectInputStream(s.getInputStream());
            System.out.println("connected!");
			bConnected = true;
			shuttingDown = true;
			isTransimate = true;
		} catch (UnknownHostException e) {
			shuttingDown=false;
			System.out.println("trying to connect server"+hostip);
			
		} catch (IOException e) {
			shuttingDown=false;
			try {
				Thread.sleep(500);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			System.out.println("trying to connect server"+hostip);
		}
		
	}
	
	public void oisconnect() {
		try {
			if(oos!=null)
			oos.close();
			if(ois!=null)
			ois.close();
			if(s!=null)
			s.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		

	}
	
	//向服务器端传动态数据 
	
	private class DynamicToServer implements Runnable
	{

		public DynamicToServer()
		{  
			
		}
		
		@Override
		public void run() {
			try {
				while (isTransimate) {

					synchronized (informateCollection.getDynamicInformation()) {

						DynamicInformation di = informateCollection
								.getDynamicInformation();

						sendDynamicInformation(di);

					}
					Thread.sleep(collectionTimeInteval);

				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				oisconnect();
			}
			
		}
		
		private synchronized void  sendDynamicInformation(DynamicInformation di) 
		{
			try {
				int n=5;
				synchronized (commondLock) {

					oos.writeInt(commond);
					switch (commond) {
					case NodeAction.LAUNCH_DYNAMIC_INFO:
						di.setNodeName("" +n);
						oos.writeObject(di);
						break;

					case NodeAction.LAUNCH_STATIC_INFO:
						serverNodeInfoCellection.collection();
						ServerNodeInfo serverinfo = serverNodeInfoCellection
								.getServerNodeInfo();
						serverinfo.setScsiCardType(""+n);
						oos.writeObject(serverinfo);
						break;
					}
					oos.flush();
					if (commond != NodeAction.LAUNCH_DYNAMIC_INFO)
						commond = NodeAction.LAUNCH_DYNAMIC_INFO;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	
	/*
	 * 接受服务器信息 包括命令等
	 * 
	 */
	
	private class RecvThreadFromServer implements Runnable {

		
		public void run() {
			try {
				while(bConnected) {
					
						commond = ois.readInt();
					    System.out.println(commond);
					
	
				}
			} catch (SocketException e) {
				e.printStackTrace();
			} catch (EOFException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally{
				oisconnect();
			}
			
		}
		
	}
	/*
	 * 开启接受命令服务RecvThreadFromServer
	 * 准备接受服务器的命令
	 * 
	 * 
	 */
	private void startRecvThreadFromServer(){
		
		RecvThreadFromServer rtffs = new RecvThreadFromServer();
		Thread startrffs = new Thread(rtffs);
		startrffs.start();
		
	}
	
     public static void main(String[] args) {
	   new Thread(new NodeClient(
			   new InformationCollection(),new ServerNodeInfoCollection())).start();
     }
	@Override
	public void run() {
	
	   while (running && !shuttingDown) {
		   connect();
	         
	   } 
	  //开启接受命令线程
	   startRecvThreadFromServer();
	  //开启动态发送信息的线程
	   DynamicToServer dt =new DynamicToServer();
	   Thread startDynamicToServer = new Thread(dt);
	   startDynamicToServer.start();
   }
		
}
