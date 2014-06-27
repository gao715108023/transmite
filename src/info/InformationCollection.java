package info;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;


public class InformationCollection {
	DynamicInformation dynamicInformation;
	
	
	
	private boolean beginIostat = true;
	private boolean beginMeminfo = true;
	private boolean beginTCPUDPIP = true;
	
	

	public InformationCollection(){
		this.dynamicInformation = new DynamicInformation();
		
	} 
	
	
	// 获得主机名
	public  String getHostName() {
		InetAddress ia = null;
		try {
			ia = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		if (ia == null) {
			return "some error...";
		} else {
			return ia.getHostName();
		}
	}
	
	// 系统更新时间
	public String getUpdateTime() {
		Runtime rt = Runtime.getRuntime();
		Process p = null;
		try {
			p = rt.exec("stat /lost+found/");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}// 调用系统的“stat"命令
		String changeTime = null;
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line = in.readLine();
			int num = 0;
			while (line != null) {
				if (7 == ++num) {
					changeTime = line.substring(8, 27);
				}
				line = in.readLine();
			}
			/*
			 * String str = null; LineNumberReader reader=new
			 * LineNumberReader(in); while ((str = reader.readLine()) != null) {
			 * 
			 * System.out.println(str);
			 * 
			 * }
			 */
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return changeTime;

	}
	
	/*
	 * 动态信息的监控线程的启动
	 */

	public void dynamicThreadStart() {
		IostatThread iostatThread = new IostatThread();
		Thread iostatThreadStart = new Thread(iostatThread);
		iostatThreadStart.start();
		MeminfoThread meminfoThread = new MeminfoThread();
		Thread meminfoThreadStart = new Thread(meminfoThread);
		meminfoThreadStart.start();
		TCPUDPIPThread tcpudpipThread = new TCPUDPIPThread();
		Thread tcpudpipThreadStart = new Thread(tcpudpipThread);
		tcpudpipThreadStart.start();
	}
	
	/*
	 * 收集静态信息
	 */
	
	public void staticInformationCollection() {
        
		
		
	}
	
	
	/*
	 * 此线程用来监控 
	 * 
	 * 用户时间百分比 UserTime
	 * NICE时间百分比  NiceTime
	 *  系统时间百分比   SystemTime
	 *  I/O等待时间百分比 IowaitTime
	 *  空闲时间百分比   IdleTime
	 *  平均值的时间间隔内CPU最大利用率 CpuMax
	 * 
	 * 
	 * 
	 */
	class IostatThread implements Runnable
	{
		float userTime=0;
		float niceTime=0;
		float systemTime=0;
		float iowaitTime=0;
		float idleTime=0;
		float cpuMax=0;
		public void iostat() throws IOException{
			Runtime rt = Runtime.getRuntime();
			Process p = rt.exec("iostat");// 调用系统的“iostat"命令
			
			BufferedReader in = null;
			String[] strArray = null;
			try {
				in = new BufferedReader(new InputStreamReader(p.getInputStream()));
				String str=in.readLine();
				String strTrue="";
				int num = 0;
				while (str!=null) {
					if(4==++num){
						str = str.trim().replaceAll("\\s+", " ");
						//System.out.println(str);
						strArray=str.split(" ");
						userTime=Float.parseFloat(strArray[0]);
						niceTime=Float.parseFloat(strArray[1]);
						systemTime=Float.parseFloat(strArray[2]);
						iowaitTime=Float.parseFloat(strArray[3]);
						idleTime=Float.parseFloat(strArray[5]);
						cpuMax=100-idleTime;
						dynamicInformation.setUserTime(userTime);
						dynamicInformation.setNiceTime(niceTime);
						dynamicInformation.setSystemTime(systemTime);
						dynamicInformation.setIowaitTime(iowaitTime);
						dynamicInformation.setIdleTime(idleTime);
						dynamicInformation.setCpuMax(cpuMax);
					}
					str=in.readLine();
				}
				/*
				 * String str = null; LineNumberReader reader=new
				 * LineNumberReader(in); while ((str = reader.readLine()) != null) {
				 * 
				 * System.out.println(str);
				 * 
				 * }
				 */
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				in.close();

			}
		}

		@Override
		public void run() {
			while(beginIostat)
			{
				try {
					iostat();
					convertToIO();
					/*System.out.println("用户时间百分比：" + dynamicInformation.getUserTime() + "%");
					System.out.println("NICE时间百分比：" + dynamicInformation.getNiceTime() + "%");
					System.out.println("系统时间百分比：" + dynamicInformation.getSystemTime() + "%");
					System.out.println("I/O等待时间百分比：" + dynamicInformation.getIowaitTime()+ "%");
					System.out.println("空闲时间百分比：" + dynamicInformation.getIdleTime()+ "%");
					System.out.println("平均值的时间间隔内CPU最大利用率：" + dynamicInformation.getCpuMax()+ "%");
					*/
					Thread.sleep(3000);
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		
	}
	// 内存，总内存、使用内存、空闲内存、总交换区、使用交换区、空闲交换区
	class MeminfoThread implements Runnable
	{

		int MemTotal = 0;
		int MemFree = 0;
		int SwapTotal = 0;
		int SwapFree = 0;
		int SwapUsed = 0;
		int MemUsed = 0;
		public void meminfo() {
			try {
				// read file content from file
				StringBuffer sb = new StringBuffer("");

				FileReader reader = new FileReader("/proc/meminfo");
				BufferedReader br = new BufferedReader(reader);
				
				String str = null;

				while ((str = br.readLine()) != null) {
					/*
					 * int i=str.length(); int j=0; int k=0; char[]
					 * arrayOfChar=str.toCharArray();
					 * while((j<i)&&(arrayOfChar[(k+j)]<=' ')) ++j;
					 * while((j<i)&&(arrayOfChar[(k+i-1)]<=' ')) --i;
		2011-05-24 17:50:25			 * str=((j>0)||(i<str.length()))?str.substring(j,i):str;
					 */
					StringBuilder sbb = new StringBuilder();
					char c = ' ';
					for (int i = 0; i < str.length(); i++) {
						char ch = str.charAt(i);
						if (ch != c) {
							sbb.append(ch);
						}
					}
					str = sbb.toString();
					String a[] = str.split(":");
					if (a[0].equals("MemTotal")) {// ||a[0].equals("MemFree")||a[0].equals("SwapTotal")||a[0].equals("SwapFree")
						Matcher m = Pattern.compile("\\d+").matcher(a[1]);
						while (m.find()) {
							MemTotal = MemTotal + Integer.parseInt(m.group(0));
							MemTotal = MemTotal / 1024;
						}
					}
					if (a[0].equals("MemFree")) {
						Matcher m = Pattern.compile("\\d+").matcher(a[1]);
						while (m.find()) {
							MemFree = MemFree + Integer.parseInt(m.group(0));
							MemFree = MemFree / 1024;
						}
					}
					if (a[0].equals("SwapTotal")) {
						Matcher m = Pattern.compile("\\d+").matcher(a[1]);
						while (m.find()) {
							SwapTotal = SwapTotal + Integer.parseInt(m.group(0));
							SwapTotal = SwapTotal / 1024;
						}
					}
					if (a[0].equals("SwapFree")) {
						Matcher m = Pattern.compile("\\d+").matcher(a[1]);
						while (m.find()) {
							SwapFree = SwapFree + Integer.parseInt(m.group(0));
							SwapFree = SwapFree / 1024;
						}
					}

					SwapUsed = SwapTotal - SwapFree;
					MemUsed = MemTotal - MemFree;

					// System.out.println(str);
				}
				

				br.close();
				reader.close();

			
				dynamicInformation.setTotalMemory(MemTotal);
				dynamicInformation.setUsedMemory(MemUsed);
				dynamicInformation.setIdleMemory(MemFree);
				dynamicInformation.setSwapSize(SwapTotal);
				dynamicInformation.setUsedSwap(SwapUsed);
				dynamicInformation.setIdleSwap(SwapFree);
				
				
				// write string to file
				/*
				 * FileWriter writer = new FileWriter("c:\\test2.txt");
				 * BufferedWriter bw = new BufferedWriter(writer);
				 * bw.write(sb.toString());
				 * 
				 * bw.close(); writer.close();
				 */
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		@Override
		public void run() {
			while(beginMeminfo)
			{
				try {
					
					Thread.sleep(3000);
					meminfo();
				/*	System.out.println("MemTotal" + dynamicInformation.getTotalMemory() + "MB");
					System.out.println("MemUsed" + dynamicInformation.getUsedMemory() + "MB");
					System.out.println("MemFree:" + dynamicInformation.getIdleMemory() + "MB");
					System.out.println("SwapTotal:" + dynamicInformation.getSwapSize() + "MB");
					System.out.println("SwapUsed:" + dynamicInformation.getUsedSwap() + "MB");
					System.out.println("SwapFree:" + dynamicInformation.getIdleSwap() + "MB");*/
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
		}
		
	}
	
	/*
	 * 此线程是用来监控 
	 * inReceivesPs     IP接受包率  包数/秒 
	 * inDeliversPs	    IP回应包率  包数/秒
	 * outRequestsPs    IP请求包率  包数/秒 
	 * inSegsPs;        TCP接受段率 段数/秒
	 * outSegsPs		TCP发送段率  段数/秒 
	 * retransSegsPs	TCP重发段率  段数/秒
	 * inDatagramsPs	UDP接受包率  包数/秒
	 * outDatagramsPs   UDP发送包率  包数/秒
	 * 
	 */
	
class TCPUDPIPThread implements Runnable{

	double inReceivesPs;     //IP接受包率  包数/秒
    double inDeliversPs	;    //IP回应包率  包数/秒
	double outRequestsPs;    //IP请求包率  包数/秒
	double inSegsPs;         //TCP接受段率 段数/秒
	double outSegsPs	;	 //TCP发送段率  段数/秒
	double retransSegsPs;	 //TCP重发段率  段数/秒
	double inDatagramsPs;	 //UDP接受包率  包数/秒
	double outDatagramsPs;   //UDP发送包率  包数/秒
	
		public double[] getCommondTCPUDPIP(String str) {
			double [] mind= new double[8];
			int n=1;
			BufferedReader reader = null;
			
			Process proc = null;
			try {
				proc = Runtime.getRuntime().exec(str);
				reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
				String line = null;
				String num[];
				while((line =reader.readLine()) !=null)
				{
					if(line.contains("total packets received")){
						if(stringToken(line)!=-1)
						{
							mind[0] = stringToken(line);
							
						}
							
					}else if(line.contains("incoming packets delivered")){
						if(stringToken(line)!=-1)
						{
							mind[1] = stringToken(line);
							
						}
							
					}else if(line.contains("requests sent out")){
						if(stringToken(line)!=-1)
						{
							mind[2] = stringToken(line);
							
						}
							
					}else if(line.contains("segments received")){
						if((stringToken(line)!=-1)&n==1)
						{
							n++;
							mind[3] = stringToken(line);
							
						}
							
					}else if(line.contains("segments send out")){
						if(stringToken(line)!=-1)
						{
							mind[4] = stringToken(line);
							
						}
							
					}else if(line.contains("segments retransmited")){
						if(stringToken(line)!=-1)
						{
							mind[5] = stringToken(line);
							
						}
							
					}else if(line.contains("packets received")){
						if(stringToken(line)!=-1)
						{
							mind[6] = stringToken(line);
							
						}
							
					}else if(line.contains("packets sent")){
						if(stringToken(line)!=-1)
						{
							mind[7] = stringToken(line);
							
						}
							
					}
					
				}
				return mind;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
			
		}
		
		private double stringToken(String str)
		{
			StringTokenizer token = new StringTokenizer(str);
			String tmp;
			double result=-1;
			if(token.hasMoreElements())
			{
				tmp = token.nextToken().trim();
			    try {
					result = Double.parseDouble(tmp);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				
			}
			return result;
		}
		
		
		public boolean convertToTCPUDPIP()
		{
			
			double[] bufferB = getCommondTCPUDPIP("netstat -s");
			/*for(double d :bufferB)
				System.out.print(d+ " ");*/
			try {
				Thread.sleep(1000*60);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//System.out.println();
			double[] bufferA = getCommondTCPUDPIP("netstat -s");
			/*for(double d :bufferA)
				System.out.print(d+ " ");*/
			if((bufferB!=null)&(bufferB!=null)){
				DecimalFormat df = new DecimalFormat("000.00");
			    inReceivesPs = Double.parseDouble(df.format((bufferA[0]-bufferB[0])/60));
			    inDeliversPs  = Double.parseDouble(df.format((bufferA[1]-bufferB[1])/60));
			    outRequestsPs = Double.parseDouble(df.format((bufferA[2]-bufferB[2])/60));
			    inSegsPs  = Double.parseDouble(df.format((bufferA[3]-bufferB[3])/60));
			    outSegsPs	 = Double.parseDouble(df.format((bufferA[4]-bufferB[4])/60));
			    retransSegsPs = Double.parseDouble(df.format((bufferA[5]-bufferB[5])/60));
			    inDatagramsPs  = Double.parseDouble(df.format((bufferA[6]-bufferB[6])/60));
			    outDatagramsPs  = Double.parseDouble(df.format((bufferA[7]-bufferB[7])/60));
			    dynamicInformation.setInReceivesPs(inReceivesPs);
			    dynamicInformation.setInDeliversPs(inDeliversPs);
			    dynamicInformation.setOutRequestsPs(outRequestsPs);
			    dynamicInformation.setInSegsPs(inSegsPs);
			    dynamicInformation.setOutSegsPs(outSegsPs);
			    dynamicInformation.setRetransSegsPs(retransSegsPs);
			    dynamicInformation.setInDatagramsPs(inDatagramsPs);
			    dynamicInformation.setOutDatagramsPs(outDatagramsPs);
			    System.out.println("  "+dynamicInformation.getInReceivesPs()+"  "+
			    dynamicInformation.getInDeliversPs()+"  "+
			    dynamicInformation.getOutRequestsPs()+"  "+
			    dynamicInformation.getInSegsPs()+" "+
			    dynamicInformation.getOutSegsPs()+"  "+
			    dynamicInformation.getRetransSegsPs()+ "  "+
			    dynamicInformation.getInDatagramsPs()+"  "+
			    dynamicInformation.getOutDatagramsPs());
			 return true;
			}
			
		  return false;	
		}
		
		@Override
		public void run() {
			while(beginTCPUDPIP)
			{
				convertToTCPUDPIP();
			}
			
		}
		
	}



public  String getCommondIO(String str) {
	BufferedReader buffer= null;
	try {
		Process proc = Runtime.getRuntime().exec(str);
	    buffer = new BufferedReader(
				new InputStreamReader(proc.getInputStream()));
	    StringBuffer strb = new StringBuffer();
	    String line = null;
	    while((line=buffer.readLine())!=null)
	    {
	    	strb.append(line+"\n");
	    	
	    }
	    return new String(strb);
	} catch (IOException e) {
		
		e.printStackTrace();
		
		
	}finally{
		if(buffer!=null)
		{
			try {
				buffer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	return null;

}

/*
 *    次方法用来读取
 *    IoTs 磁盘每秒IO次数
 *    ReadSpeed 磁盘读速度，单位KB
 *    ReadKB  磁盘读字节数，单位KB
 *    WriteSpeed 磁盘写速度，单位KB
 *    WriteKB 磁盘写字节数，单位KB
 * 
 * 
 */
public boolean convertToIO()
{
	double IoTs=0.0;
	double ReadSpeed=0.0;
	double WriteSpeed=0.0;
	double ReadKB=0.0;
	double WriteKB=0.0;
	String buffer = getCommondIO("iostat -d -k");
	if(buffer!=null)
	{
		
		List<String> strArr = new ArrayList<String>();
		StringTokenizer toker = new StringTokenizer(new String(buffer));
		while(toker.hasMoreElements())
		{
			strArr.add(toker.nextToken().trim());
		}
	
		int i = 0;
		for(;i<strArr.size();i++)
		{
			if((strArr.get(i)).equals("tps"))
			        break;		   
		}
		i+=6;
        while(i<strArr.size())
        {
        	try {
				IoTs = IoTs+Double.parseDouble(strArr.get(i));
				ReadSpeed = ReadSpeed+Double.parseDouble(strArr.get(i+1));
				WriteSpeed = WriteSpeed+Double.parseDouble(strArr.get(i+2));
				ReadKB = ReadKB+Double.parseDouble(strArr.get(i+3));
				WriteKB = WriteKB+Double.parseDouble(strArr.get(i+4));
			} catch (Exception e) {
				
				e.printStackTrace();
				return false;
			}
        	i=i+6;
        }
        dynamicInformation.setIoTs((int)IoTs);
        dynamicInformation.setReadKB((int)ReadKB);
        dynamicInformation.setReadSpeed((int)ReadSpeed);
        dynamicInformation.setWriteKB((int)WriteKB);
        dynamicInformation.setWriteSpeed((int)WriteSpeed);
       
		return true;
	}
    return false;
}
public DynamicInformation getDynamicInformation() {
	return dynamicInformation;
}


public void setDynamicInformation(DynamicInformation dynamicInformation) {
	this.dynamicInformation = dynamicInformation;
}

	public static void main(String[] args) throws IOException {
		
	
		InformationCollection in = new InformationCollection();
		
		in.dynamicThreadStart();
		 
	}
	

}
