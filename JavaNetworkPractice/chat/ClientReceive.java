package chat;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * 使用多线程实现客户接收端
 * 
 * @author PC
 *
 */
public class ClientReceive implements Runnable{

	private DataInputStream dis;
	private Socket client;
	private boolean isRunning;
	
	public ClientReceive(Socket client) 
	{
		try 
		{
			this.client=client;
			this.isRunning=true;
			this.dis=new DataInputStream(client.getInputStream());
		} 
		catch (IOException e) 
		{
			release();
			System.out.println("------客户端接收数据失败-----");
			e.printStackTrace();
		}
		
	}

	//获取发送的消息并打印
	public String receive()
	{
		String msg="";
		try 
		{
			msg = dis.readUTF();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		return msg;
	}
	
	public void release()
	{
		isRunning=false;
		Utils.close(dis,client);
	}
	
	@Override
	public void run() {
		while(isRunning)
		{
			String msg=receive();
			if(!msg.equals(""))
			{
				System.out.println(msg);
			}
		}
	}

}
