package chat;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * 在线聊天室：客户端
 * 
 * @author PC
 *
 */
public class Client {
	public static void main(String[] args) {
		System.out.println("------客户端启动------");
		try 
		{
			//向对应服务端请求连接
			Socket client=new Socket("localhost",8888);
			
			
			//启动发送消息的线程
			new Thread(new ClientSend(client)).start();
			//启动接收消息的线程
			new Thread(new ClientReceive(client)).start();
		} 
		catch (UnknownHostException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
}
