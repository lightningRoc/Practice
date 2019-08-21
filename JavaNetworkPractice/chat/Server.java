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
 * 在线聊天室：服务端
 * 
 * @author PC
 *
 */
public class Server {
	//使用集合还实现群聊功能
	private static CopyOnWriteArrayList<Channel> all=new CopyOnWriteArrayList<>();
	
	public static void main(String[] args) {
		System.out.println("------服务器启动------");
		
		ServerSocket server=null;
		try 
		{
			//指定一个监听8888端口的ServerSocket
			server=new ServerSocket(8888);
		} 
		catch (IOException e) 
		{
			System.out.println("------服务器启动错误------");
			e.printStackTrace();
		}
		
		try 
		{
			while(true)
			{
				//阻塞式接受请求并创建对应套接字
				Socket client=server.accept();
				System.out.println("------一个客户端建立连接------");
				Channel channel=new Channel(client);
				new Thread(channel).start();
			}
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	//封装一个客户的交互过程
	static class Channel implements Runnable
	{
		private DataInputStream dis;
		private DataOutputStream dos;
		private Socket client;
		private boolean isRunning;
		private String name;//标识连接的人
		
		public Channel(Socket client)
		{
			try 
			{
				this.client=client;
				this.isRunning=true;
				this.dis=new DataInputStream(client.getInputStream());
				this.dos=new DataOutputStream(client.getOutputStream());
				//获取用户名
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
		//接受消息
		private String receive()
		{
			String msg="";
			try 
			{
				msg=dis.readUTF();
			} 
			catch (IOException e) 
			{
				//出错后释放资源
				release();
				System.out.println("-----服务器接受数据失败-----");
				e.printStackTrace();
			}
			return msg;
		}
		
		//加入聊天室
		private void joinIn()
		{
			for(Channel channel:all)
			{
				channel.send("系统消息：欢迎"+name+"加入群聊");
			}
		}
		
		//退出聊天室
		private void dropOut()
		{
			for(Channel channel:all)
			{
				channel.send("系统消息："+name+"退出聊天室");
			}
		}
		
		//发送消息
		private void sendToOthers(String msg)
		{
			if(msg.startsWith("@"))//通过@name：来实现私聊功能
			{
				int index=msg.indexOf("：");
				String targetName=msg.substring(1,index);
				String content=msg.substring(index+1);
				
				for(Channel channel:all)
				{
					if(channel.getName().equals(targetName))
					{
						channel.send("私聊："+name+"："+content);
					}
				}
			}
			else
			{
				for(Channel channel:all)
				{
	//				//自己则跳过
	//				if(channel==this)
	//				{
	//					continue;
	//				}
					channel.send(name+"："+msg);
				}
			}
		}
		
		//群聊方式发送消息
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
				System.out.println("-----服务器发送数据失败-----");
				e.printStackTrace();
			}
		}
		
		//释放资源
		private void release()
		{
			isRunning=false;
			Utils.close(dis,dos,client);
			//接收线程结束
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
