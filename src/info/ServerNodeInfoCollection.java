package info;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;




public class ServerNodeInfoCollection {
	
	ServerNodeInfo serverNodeInfo;
	
	public ServerNodeInfo getServerNodeInfo() {
		return serverNodeInfo;
	}


	public void setServerNodeInfo(ServerNodeInfo serverNodeInfo) {
		this.serverNodeInfo = serverNodeInfo;
	}


	public ServerNodeInfoCollection()
	{
		this.serverNodeInfo = new ServerNodeInfo();
	}
	
	
	public void  getSCSICardType() {

		String result = null;
		List<String> results= new ArrayList<String>();
		BufferedReader reader = null;
		try {
		   reader = new BufferedReader(
					new FileReader("/proc/scsi/scsi"));
			String line = null;
			
			
				while((line=reader.readLine())!=null)
				{
					//System.out.println(line);
					if(line.contains("Model: ")&(line.contains(" Rev:")))
					{
						result = line.substring(line.indexOf("Model: ")+6
								,line.indexOf(" Rev:"));
						result = result.trim().replaceAll(" ", "_");
						results.add(result);
						
					}
				}
			 
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				if(reader!=null)
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		result=results.get(0);
		for(int i=1;i<results.size();i++)
		{
			result += "@@"+results.get(i);
		}
		serverNodeInfo.setScsiCardType(result);
		//System.out.println(result);
	
	}
	
	public  void getNetCardType() {
		
		Process proc;
		String line = null;
		String result="";
		List<String> results = new ArrayList<String>();
		BufferedReader reader = null;
		try {
			proc = Runtime.getRuntime().exec("lspci");
			reader = new  BufferedReader(
					new InputStreamReader(proc.getInputStream()));
		
			while((line=reader.readLine())!=null)
			{
				
				if(line.contains("Ethernet")&&line.contains("controller"))
				{
					result=line.substring(line.indexOf("controller:")+12);
					result = result.trim().replaceAll(" ", "_");
					results.add(result);
					
				}
				
				
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(reader!=null)
			{
				try {
					reader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		result=results.get(0);
		for(int i=1;i<results.size();i++)
		{
			result += "@@"+results.get(i);
		}
		serverNodeInfo.setNetCardType(result);
		//System.out.println(result);
		
		
	}
	
	public  void getFSAndFSName() {
		
		Process proc;
		String line = null;
		List<String> fsNames = new ArrayList<String>();
		List<String> fsSize = new ArrayList<String>();
		String fsName = null;
		String fsSzieStr = null;
		BufferedReader reader = null;
		String str[] =null;
		try {
			proc = Runtime.getRuntime().exec("df -h");
			reader = new  BufferedReader(
					new InputStreamReader(proc.getInputStream()));
		    line = reader.readLine();
			while((line=reader.readLine())!=null)
			{
				
				StringTokenizer token  = new StringTokenizer(line);
				if(token.hasMoreElements())
				{
					fsNames.add(token.nextToken().trim());
					
				}
				if(token.hasMoreElements())
				{
					
					fsSize.add(token.nextToken().trim());
					
				}
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(reader!=null)
			{
			
				try {
					reader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		fsName = fsNames.get(0);
		for(int i=1;i<fsNames.size();i++)
		{
			fsName += "@@"+fsNames.get(i);
		}
		
		fsSzieStr = fsSize.get(0);
		if(fsSzieStr.endsWith("G")||fsSzieStr.endsWith("g")){
			Double n = (Double.parseDouble(
					fsSzieStr.substring(0,fsSzieStr.length()-1))*1024);
			fsSzieStr =""+n;
		
		}
		for(int i=1;i<fsSize.size();i++)
		{
			if(fsSize.get(i).endsWith("G")||fsSize.get(i).endsWith("g"))
			{
				Double n = (Double.parseDouble(
						fsSize.get(i).substring(0,fsSize.get(i).length()-1)))*1024;
				fsSzieStr += "@@"+n;
				
			}
			fsSzieStr += "@@"+fsSize.get(i).substring(0,fsSize.get(i).length()-1);
			
		}
		//System.out.println(fsName);
		//System.out.println(fsSzieStr);
		serverNodeInfo.setFsNames(fsName);
		serverNodeInfo.setFsSize(fsSzieStr);
	}
	
	public void collection() {
		getSCSICardType();
		
		getNetCardType();
		getFSAndFSName();
	}
	
	public static void main(String[] args) {
		ServerNodeInfoCollection trs =new ServerNodeInfoCollection();
		
		trs.collection();
		System.out.println(trs.serverNodeInfo.getFsNames()+"\n"+
		trs.serverNodeInfo.getFsSize()+"\n"		+
		trs.serverNodeInfo.getScsiCardType()+"\n"+
		trs.serverNodeInfo.getNetCardType()
		
		
		);
		
	}	

}
