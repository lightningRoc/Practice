package chat;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * ʹ�ö��߳�ʵ�ֿͻ����ն�
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
			System.out.println("------�ͻ��˽�������ʧ��-----");
			e.printStackTrace();
		}
		
	}

	//��ȡ���͵���Ϣ����ӡ
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
