package rpc;


import java.io.ObjectOutputStream;


public  interface NodeAction {

	
	    /** 开始收集静态信息 */
	    public static final int  LAUNCH_STATIC_INFO = 0 ;
	    
	    /** 开始收集动态信息 */
	    public static final int  LAUNCH_DYNAMIC_INFO=1;
	    
	    /** 停止静态信息收集 */
	    public static final int  KILL_STATIC_INFO = 2;
	    
	    /** 停止动态信息收集 */
	    public static final int  KILL_DYNAMIC_INFO =3;  
	    
	    public static final int NON=4;
	  


	public void sendCommond(ObjectOutputStream dos);
	  
}
