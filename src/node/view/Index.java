package node.view;



import java.awt.*;
import javax.swing.*;

import node.view.tools.*;

public class Index extends JWindow {

	
	MyPaint mp;
	//构造函数
	Index(){
		
		mp=new MyPaint();
		mp.setBounds(0, 0, 400, 250);
		this.add(mp);
		int width=Toolkit.getDefaultToolkit().getScreenSize().width;
		int height=Toolkit.getDefaultToolkit().getScreenSize().height;
		this.setLocation(width/2-200, height/2-125);
	
		this.setSize(400, 250);
		this.setVisible(true);
	}
	//主函数入口，开启一个index线程
	public static void main(String[] args) {
		Index index=new Index();
		//Thread t=new Thread(index);
		//t.start();
	}
	
	/*@Override
	public void run() {
		int time=0;
		while(true){
			time++;
			if(time>30){
				new UserLogin();
				this.dispose();
				break;
			}
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}*/
}


class MyPaint extends JPanel implements Runnable{
	
	
	//用于开启关闭线程的参数
	private Thread t;
	private boolean ifok=true;
	//用于下端的浮动窗格的参数
	private int x=10;
	private int i=0,j=40,k=0,dian=0;
	private String gg="系统正在加载请稍候";
	//用于画字显示的参数
	private int tt=0;
	//用于逐行画字的一些参数
	private int lx=150,ly=40,length=7;
	private int lxwidth=30,lywidth=20;
	private String slogan=" 百年胜利 物探先行 我为祖国献石油 敬业 协作 创新 卓越           ";	
	//用于重新绘图的时候的一些参数包括字体颜色图片等等,避免重复定义
	private Image img;
	private Color color;
	
	//构造方法，其实质就是开启了一个线程
	MyPaint(){
		
		t=new Thread(this);
		t.start();
	}
	
	@Override
	public void run() {
		
		while(ifok){
			//repaint方法首先调用update方法，然后调用paint方法
			//其中paint方法调用的就是重写的paintComponent(Graphics g)方法
			if(x<=380){
				repaint();   
			} else{
				ifok=false;
			}
			try {
				Thread.sleep(50);
				i++;
				j=j-6;
				if(i==4){
					//写下一个字
					tt++;
					//判断浮动窗格是不是到达静态字所在的位置
					if(x>120+k*20 && k<gg.length()-1){
						k++;
					}
					//浮动窗格向前移动一位，以10为基准格，且i,j归回初始值
					x=x+10;
					i=0;
					j=40;
					//此处++，0个点、1个点、2个点、3个点交替出现
					dian++;
					if(dian>3) dian=0;
				}
			} catch (InterruptedException e) {
				System.out.println("线程中断");
			}
		}
	}
	
	public void paintComponent(Graphics g){
		//创建背景图片
		img=Toolkit.getDefaultToolkit().getImage("images/wt.png");
		g.drawImage(img, 0, 0, this.getWidth(),200, this);
		//设置画笔的字体
		g.setFont(MyFont.fli);
		//创建一个随机的颜色
		int r=(int)(Math.random()*255);
		int b=(int)(Math.random()*255);
		int y=(int)(Math.random()*255);
		color=new Color(r,b,y);
		
		//白色填充框,x方向位置和大小由x决定,y方向固定为210,宽度为30;
		g.setColor(MyColor.white);
		g.fillRect(x,210,390-x,30);
		/*
		 *第一次（j=40）：使用一个随机的颜色绘制：位置205-245
		 *第二次（j=34）：使用一个随机的颜色绘制：位置208-242
		 *第三次（i=2，j=28):使用一个随机的颜色绘制：位置211-239
		 *第四次（j=22）：使用一个固定的颜色绘制：位置214-236
		 *此外位置小于214或者是大于236并且小于239的地方，重新填充为白色
		 */
		if(j>25) {
			g.setColor(color);
		} else {
			g.setColor(MyColor.white);
			g.fillRect(x,205, 10,40);
			g.setColor(MyColor.cyan);
		}
		g.fillRect(x, 225-j/2, 10, j);
		//画边框
		g.setColor(MyColor.cyan);
		g.drawRect(10,210,380,30);
		
		if(x<120){
			g.setColor(MyColor.black);
			//填加静态字
			for(int rr=0;rr<gg.length();rr++){
				g.drawString(gg.substring(rr, rr+1),120+rr*20,230);
			}
			
		} else{
			//当方格运行到字的位置时，字变一种颜色;后面字体的颜色不变
			g.setColor(MyColor.changecolor);
			g.drawString(gg.substring(k,k+1), 120+k*20, 230);
			g.setColor(MyColor.black);
			for(int rr=k+1;rr<gg.length();rr++){
				g.drawString(gg.substring(rr, rr+1),120+rr*20,230);
			}
			
		}
		
		//逐行写
		if(tt<length){
			//使用随机的颜色绘制字体
			g.setColor(color);
			for(int rr=0;rr<=tt;rr++){
				g.drawString(slogan.substring(rr, rr+1), 
						(int) (lx-lxwidth*Math.floor(rr/length)),ly+(rr%length)*lywidth);
			}
		} else if(tt<length*2){
			//先绘制已经完成的字体，使用finish color绘制
			g.setColor(MyColor.finishcolor);
			for(int rr=0;rr<length;rr++){
				g.drawString(slogan.substring(rr, rr+1), 
						(int) (lx-lxwidth*Math.floor(rr/length)),ly+(rr%length)*lywidth);
			}
			//然后使用随机的颜色绘制正在显示的字体
			g.setColor(color);
			for(int rr=length;rr<=tt;rr++){
				g.drawString(slogan.substring(rr, rr+1), 
						(int) (lx-lxwidth*Math.floor(rr/length)),ly+(rr%length)*lywidth);
			}
		} else if(tt<length*3){
			//原理同上，前两排字体先绘制，后绘制正在显示的字体
			g.setColor(MyColor.finishcolor);
			for(int rr=0;rr<length*2;rr++){
				g.drawString(slogan.substring(rr, rr+1), 
						(int) (lx-lxwidth*Math.floor(rr/length)),ly+(rr%length)*lywidth);
			}
			g.setColor(color);
			for(int rr=length*2;rr<=tt;rr++){
				g.drawString(slogan.substring(rr, rr+1), 
						(int) (lx-lxwidth*Math.floor(rr/length)),ly+(rr%length)*lywidth);
			}
		} else if(tt<length*4){
			//原理同上，前三排字体先绘制，后绘制正在显示的字体
			g.setColor(MyColor.finishcolor);
			for(int rr=0;rr<length*3;rr++){
				g.drawString(slogan.substring(rr, rr+1), 
						(int) (lx-lxwidth*Math.floor(rr/length)),ly+(rr%length)*lywidth);
			}
			g.setColor(color);
			for(int rr=length*3;rr<=tt;rr++){
				g.drawString(slogan.substring(rr, rr+1), 
						(int) (lx-lxwidth*Math.floor(rr/length)),ly+(rr%length)*lywidth);
			}
		} else if(tt<length*5){
			//原理同上，前四排字体先绘制，后绘制正在显示的字体
			g.setColor(MyColor.finishcolor);
			for(int rr=0;rr<length*4;rr++){
				g.drawString(slogan.substring(rr, rr+1), 
						(int) (lx-lxwidth*Math.floor(rr/length)),ly+(rr%length)*lywidth);
			}
			g.setColor(color);
			for(int rr=length*4;rr<=tt;rr++){
				g.drawString(slogan.substring(rr, rr+1), 
						(int) (lx-lxwidth*Math.floor(rr/length)),ly+(rr%length)*lywidth);
			}	
		} else{
			//完成时候的样子
			g.setColor(MyColor.finishcolor);
			for(int rr=0;rr<length*5;rr++){
				g.drawString(slogan.substring(rr, rr+1), 
						(int) (lx-30*Math.floor(rr/length)),ly+(rr%length)*20);
			}
		}
	}
}
