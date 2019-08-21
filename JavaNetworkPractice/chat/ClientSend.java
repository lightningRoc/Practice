package chat;

import java.awt.TexturePaint;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * ʹ�ö��߳�ʵ�ֿͻ����Ͷ�
 * 
 * @author PC
 *
 */
public class ClientSend implements Runnable{

	private BufferedReader console;
	private DataOutputStream dos;
	private Socket client;
	private boolean isRunning;
	
	public ClientSend(Socket client) 
	{
		try 
		{
			this.dos=new DataOutputStream(client.getOutputStream());
			this.client=client;
			this.isRunning=true;
			this.console=new BufferedReader(new InputStreamReader(System.in));
			
		} 
		catch (IOException e) 
		{
			release();
			System.out.println("------�ͻ��˷�������ʧ��-----");
			e.printStackTrace();
		}
		
	}
	
	//ͨ������̨ģ����Ϣ����
	public String getMsgFromConsole()
	{
		String msg="";
		try 
		{
			msg=console.readLine();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		return msg;
	}
	
	//�ͻ��˷�����Ϣ
	public void send(String msg)
	{
		try 
		{
			dos.writeUTF(msg);
			dos.flush();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}		
	}
	
	public void release()
	{
		isRunning=false;
		Utils.close(dos,client);
	}
	
	@Override
	public void run() 
	{
		//�����û���
		System.out.println("�������û���");
		String name=getMsgFromConsole();
		send(name);
		
		while(isRunning)
		{
			String msg=getMsgFromConsole();
			if(!msg.equals(""))
			{
				send(msg);
			}
		}
	}

}
