package rpc;

import java.io.DataOutputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class KillStaticACtion implements NodeAction{

	
	long actionId;


	String userName;
    
	
	
	public long getActionId() {
		return actionId;
	}

	public void setActionId(long actionId) {
		this.actionId = actionId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Override
	public void sendCommond(ObjectOutputStream dos){
		
	}

}
