import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class TCPClient {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		try(Socket s=new Socket(InetAddress.getLocalHost(),8189);Scanner in=new Scanner(s.getInputStream(),"UTF-8"))
		{//套接字链接服务端测试
			while(in.hasNextLine())
			{
				System.out.println(in.nextLine());
			}
		}
	}

}
