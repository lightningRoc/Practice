import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

public class InetAddressTest {

	public static void main(String[] args) throws UnknownHostException {
		// TODO Auto-generated method stub
		InetAddress address=InetAddress.getByName("time-a.nist.gov");
		byte[] addressBytes=address.getAddress();
		System.out.println(address.toString());
		System.out.println(Arrays.toString(addressBytes));//��ȡ������Ӧ��IP��ַ
		
		address=InetAddress.getLocalHost();
		System.out.println(address.toString());//������ַ
		
		InetAddress[] addresses=InetAddress.getAllByName("baidu.com");//��ȡ��������Ӧ�Ķ��IP
		System.out.println(addresses[0].getHostName()+":");
		for(InetAddress a:addresses)
		{
			System.out.println(a.toString());
		}
		
		addresses=InetAddress.getAllByName("google.com");//��ȡ��������Ӧ�Ķ��IP
		System.out.println(addresses[0].getHostName());
		for(InetAddress a:addresses)
		{
			System.out.println(a.toString());
		}
	}

}
