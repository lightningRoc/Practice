package chat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.AllPermission;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.print.attribute.standard.MediaSize.Other;

/**
 * ���������ң������
 * 
 * @author PC
 *
 */
public class Server {
	//ʹ�ü��ϻ�ʵ��Ⱥ�Ĺ���
	private static CopyOnWriteArrayList<Channel> all=new CopyOnWriteArrayList<>();
	
	public static void main(String[] args) {
		System.out.println("------����������------");
		
		ServerSocket server=null;
		try 
		{
			//ָ��һ������8888�˿ڵ�ServerSocket
			server=new ServerSocket(8888);
		} 
		catch (IOException e) 
		{
			System.out.println("------��������������------");
			e.printStackTrace();
		}
		
		try 
		{
			while(true)
			{
				//����ʽ�������󲢴�����Ӧ�׽���
				Socket client=server.accept();
				System.out.println("------һ���ͻ��˽�������------");
				Channel channel=new Channel(client);
				new Thread(channel).start();
			}
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	//��װһ���ͻ��Ľ�������
	static class Channel implements Runnable
	{
		private DataInputStream dis;
		private DataOutputStream dos;
		private Socket client;
		private boolean isRunning;
		private String name;//��ʶ���ӵ���
		
		public Channel(Socket client)
		{
			try 
			{
				this.client=client;
				this.isRunning=true;
				this.dis=new DataInputStream(client.getInputStream());
				this.dos=new DataOutputStream(client.getOutputStream());
				//��ȡ�û���
				String name=receive();
				this.name=name;
				all.add(this);
				joinIn();
				
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
			
		}
		//������Ϣ
		private String receive()
		{
			String msg="";
			try 
			{
				msg=dis.readUTF();
			} 
			catch (IOException e) 
			{
				//������ͷ���Դ
				release();
				System.out.println("-----��������������ʧ��-----");
				e.printStackTrace();
			}
			return msg;
		}
		
		//����������
		private void joinIn()
		{
			for(Channel channel:all)
			{
				channel.send("ϵͳ��Ϣ����ӭ"+name+"����Ⱥ��");
			}
		}
		
		//�˳�������
		private void dropOut()
		{
			for(Channel channel:all)
			{
				channel.send("ϵͳ��Ϣ��"+name+"�˳�������");
			}
		}
		
		//������Ϣ
		private void sendToOthers(String msg)
		{
			if(msg.startsWith("@"))//ͨ��@name����ʵ��˽�Ĺ���
			{
				int index=msg.indexOf("��");
				String targetName=msg.substring(1,index);
				String content=msg.substring(index+1);
				
				for(Channel channel:all)
				{
					if(channel.getName().equals(targetName))
					{
						channel.send("˽�ģ�"+name+"��"+content);
					}
				}
			}
			else
			{
				for(Channel channel:all)
				{
	//				//�Լ�������
	//				if(channel==this)
	//				{
	//					continue;
	//				}
					channel.send(name+"��"+msg);
				}
			}
		}
		
		//Ⱥ�ķ�ʽ������Ϣ
		public void send(String msg)
		{
			try 
			{
				dos.writeUTF(msg);
				dos.flush();
			} 
			catch (IOException e) 
			{
				release();
				System.out.println("-----��������������ʧ��-----");
				e.printStackTrace();
			}
		}
		
		//�ͷ���Դ
		private void release()
		{
			isRunning=false;
			Utils.close(dis,dos,client);
			//�����߳̽���
			all.remove(this);
		}
		
		
		
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		@Override
		public void run() 
		{
			while(isRunning)
			{
				String msg=receive();
				if(!msg.equals(""))
				{
					sendToOthers(msg);
				}
			}
			dropOut();
		}
	}
}
