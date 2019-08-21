package chat;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * ���������ң��ͻ���
 * 
 * @author PC
 *
 */
public class Client {
	public static void main(String[] args) {
		System.out.println("------�ͻ�������------");
		try 
		{
			//���Ӧ�������������
			Socket client=new Socket("localhost",8888);
			
			
			//����������Ϣ���߳�
			new Thread(new ClientSend(client)).start();
			//����������Ϣ���߳�
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
