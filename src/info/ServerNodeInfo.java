package info;

import java.io.Serializable;

public class ServerNodeInfo implements Serializable{
	
	private String scsiCardType ;

	private String netCardType ;
	
	private String fsNames;
	
	private String fsSize;

	public String getScsiCardType() {
		return scsiCardType;
	}

	public String getNetCardType() {
		return netCardType;
	}

	public String getFsNames() {
		return fsNames;
	}

	public String getFsSize() {
		return fsSize;
	}

	public void setScsiCardType(String scsiCardType) {
		this.scsiCardType = scsiCardType;
	}

	public void setNetCardType(String netCardType) {
		this.netCardType = netCardType;
	}

	public void setFsNames(String fsNames) {
		this.fsNames = fsNames;
	}

	public void setFsSize(String fsSize) {
		this.fsSize = fsSize;
	}
	public String toString()
	{
		return scsiCardType +"     "+

		 netCardType  +"     "+
		
		 fsNames +"     "+
		
		 fsSize;
		
	}

}
