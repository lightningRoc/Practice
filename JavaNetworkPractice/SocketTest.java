import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class SocketTest {

	public static void main(String[] args) throws UnknownHostException, IOException {
		// TODO Auto-generated method stub
		try(Socket s=new Socket("time-a.nist.gov",13);Scanner in=new Scanner(s.getInputStream(),"UTF-8"))
		{//�׽��ֲ��ԣ���ȡ�ԭ���Ӽ���ʱ��
			while(in.hasNextLine())
			{
				System.out.println(in.nextLine());
			}
		}
	}

}
