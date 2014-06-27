package info;

import java.io.Serializable;


/*
 *此类是采集信息的基类，各个字段的意思详见物探院的数据字典 
 * 
 */

public class DynamicInformation implements Serializable{
	 
	private float oneMinsProcs ;     //	FLOAT(6,2)	
	private float fiveMinsProcs ;    //	FLOAT(6,2)
	private float fifteenMinsProcs	;//FLOAT(6,2)	
	private float userTime	;        //FLOAT(6,2)	
	private float niceTime	;        //FLOAT(6,2)	
	private float systemTime;	     //FLOAT(6,2)	
	private float iowaitTime;        //	FLOAT(6,2)	
	private float idleTime	;        //FLOAT(6,2)	
	private float cpuMax	;        //FLOAT(6,2)	
	private int averageTimeInterval; //	MEDIUMINT	
	private int totalMemory;         //MEDIUMINT	
	private int usedMemory ;         //	MEDIUMINT	
	private int idleMemory;          //	MEDIUMINT	
	private int swapSize ;	         //INT	
	private int usedSwap ;	         //INT	
	private int idleSwap ;	         //INT
	
	private double inReceivesPs;     //DOUBLE(12,2)	
	private double inDeliversPs;     //DOUBLE(12,2)	
	private double outRequestsPs;	 //DOUBLE(12,2)	
	private double inSegsPs	;        //DOUBLE(12,2)	
	private double outSegsPs ;	     //DOUBLE(12,2)	
	private double retransSegsPs;	 //DOUBLE(12,2)	
	private double inDatagramsPs;	 //DOUBLE(12,2)	
	private double outDatagramsPs;	 //DOUBLE(12,2)	
	
	
	private String nodeName;	     //节点名称 
	private String updateTime;		 //更新时间 格式 ‘2003-12-31 23:59:59’ 

	private int ioTs ;               //	MEDIUMINT	
	private int readSpeed;           //	MEDIUMINT	
	private int readKB	;            //MEDIUMINT	
	private int writeSpeed;          //MEDIUMINT	
	private int writeKB	;            //MEDIUMINT
	
	
	public float getOneMinsProcs() {
		return oneMinsProcs;
	}
	public void setOneMinsProcs(float oneMinsProcs) {
		this.oneMinsProcs = oneMinsProcs;
	}
	public float getFiveMinsProcs() {
		return fiveMinsProcs;
	}
	public void setFiveMinsProcs(float fiveMinsProcs) {
		this.fiveMinsProcs = fiveMinsProcs;
	}
	public float getFifteenMinsProcs() {
		return fifteenMinsProcs;
	}
	public void setFifteenMinsProcs(float fifteenMinsProcs) {
		this.fifteenMinsProcs = fifteenMinsProcs;
	}
	public float getUserTime() {
		return userTime;
	}
	public void setUserTime(float userTime) {
		this.userTime = userTime;
	}
	public float getNiceTime() {
		return niceTime;
	}
	public void setNiceTime(float niceTime) {
		this.niceTime = niceTime;
	}
	public float getSystemTime() {
		return systemTime;
	}
	public void setSystemTime(float systemTime) {
		this.systemTime = systemTime;
	}
	public float getIowaitTime() {
		return iowaitTime;
	}
	public void setIowaitTime(float iowaitTime) {
		this.iowaitTime = iowaitTime;
	}
	public float getIdleTime() {
		return idleTime;
	}
	public void setIdleTime(float idleTime) {
		this.idleTime = idleTime;
	}
	public float getCpuMax() {
		return cpuMax;
	}
	public void setCpuMax(float cpuMax) {
		this.cpuMax = cpuMax;
	}
	public int getAverageTimeInterval() {
		return averageTimeInterval;
	}
	public void setAverageTimeInterval(int averageTimeInterval) {
		this.averageTimeInterval = averageTimeInterval;
	}
	public int getTotalMemory() {
		return totalMemory;
	}
	public void setTotalMemory(int totalMemory) {
		this.totalMemory = totalMemory;
	}
	public int getUsedMemory() {
		return usedMemory;
	}
	public void setUsedMemory(int usedMemory) {
		this.usedMemory = usedMemory;
	}
	public int getIdleMemory() {
		return idleMemory;
	}
	public void setIdleMemory(int idleMemory) {
		this.idleMemory = idleMemory;
	}
	public int getSwapSize() {
		return swapSize;
	}
	public void setSwapSize(int swapSize) {
		this.swapSize = swapSize;
	}
	public int getUsedSwap() {
		return usedSwap;
	}
	public void setUsedSwap(int usedSwap) {
		this.usedSwap = usedSwap;
	}
	public int getIdleSwap() {
		return idleSwap;
	}
	public void setIdleSwap(int idleSwap) {
		this.idleSwap = idleSwap;
	}
	
	public double getInReceivesPs() {
		return inReceivesPs;
	}
	public void setInReceivesPs(double inReceivesPs) {
		this.inReceivesPs = inReceivesPs;
	}
	public double getInDeliversPs() {
		return inDeliversPs;
	}
	public void setInDeliversPs(double inDeliversPs) {
		this.inDeliversPs = inDeliversPs;
	}
	public double getOutRequestsPs() {
		return outRequestsPs;
	}
	public void setOutRequestsPs(double outRequestsPs) {
		this.outRequestsPs = outRequestsPs;
	}
	public double getInSegsPs() {
		return inSegsPs;
	}
	public void setInSegsPs(double inSegsPs) {
		this.inSegsPs = inSegsPs;
	}
	public double getOutSegsPs() {
		return outSegsPs;
	}
	public void setOutSegsPs(double outSegsPs) {
		this.outSegsPs = outSegsPs;
	}
	public double getRetransSegsPs() {
		return retransSegsPs;
	}
	public void setRetransSegsPs(double retransSegsPs) {
		this.retransSegsPs = retransSegsPs;
	}
	public double getInDatagramsPs() {
		return inDatagramsPs;
	}
	public void setInDatagramsPs(double inDatagramsPs) {
		this.inDatagramsPs = inDatagramsPs;
	}
	public double getOutDatagramsPs() {
		return outDatagramsPs;
	}
	public void setOutDatagramsPs(double outDatagramsPs) {
		this.outDatagramsPs = outDatagramsPs;
	}
	
	
	public String getNodeName() {
		return nodeName;
	}
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public int getIoTs() {
		return ioTs;
	}
	public void setIoTs(int ioTs) {
		this.ioTs = ioTs;
	}
	public int getReadSpeed() {
		return readSpeed;
	}
	public void setReadSpeed(int readSpeed) {
		this.readSpeed = readSpeed;
	}
	public int getReadKB() {
		return readKB;
	}
	public void setReadKB(int readKB) {
		this.readKB = readKB;
	}
	public int getWriteSpeed() {
		return writeSpeed;
	}
	public void setWriteSpeed(int writeSpeed) {
		this.writeSpeed = writeSpeed;
	}
	public int getWriteKB() {
		return writeKB;
	}
	public void setWriteKB(int writeKB) {
		this.writeKB = writeKB;
	}

	public String toString()
	{
		return  nodeName+"        "+	      
		 updateTime+"        "+		  
		 oneMinsProcs +"        "+     
		 fiveMinsProcs +"        "+    
		 fifteenMinsProcs	+"        "+	
		 userTime	+"        "+        	
		 niceTime	+"        "+        	
		 systemTime+"        "+	     	
		 iowaitTime+"        "+        	
		 idleTime	+"        "+        	
		 cpuMax	+"        "+        	
		 averageTimeInterval+"        "+ 	
		 totalMemory+"        "+         
		 usedMemory +"        "+         	
		 idleMemory+"        "+          	
		 swapSize +"        "+	         	
		 usedSwap +"        "+	         	
		 idleSwap +"        "+	         
		
		 inReceivesPs+"        "+     	
		 inDeliversPs+"        "+     	
		 outRequestsPs+"        "+	 	
		 inSegsPs	+"        "+        	
		 outSegsPs +"        "+	     	
		 retransSegsPs+"        "+	 	
		 inDatagramsPs+"        "+	 	
		 outDatagramsPs+"        "+	 	
		
		
		

		 ioTs +"        "+               	
		 readSpeed+"        "+           	
		 readKB	+"        "+            	
		 writeSpeed+"        "+          	
		 writeKB;       
		
	}
	
}
