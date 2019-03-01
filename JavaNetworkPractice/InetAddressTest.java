import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

public class InetAddressTest {

	public static void main(String[] args) throws UnknownHostException {
		// TODO Auto-generated method stub
		InetAddress address=InetAddress.getByName("time-a.nist.gov");
		byte[] addressBytes=address.getAddress();
		System.out.println(address.toString());
		System.out.println(Arrays.toString(addressBytes));//获取主机对应的IP地址
		
		address=InetAddress.getLocalHost();
		System.out.println(address.toString());//本机地址
		
		InetAddress[] addresses=InetAddress.getAllByName("baidu.com");//获取主机名对应的多个IP
		System.out.println(addresses[0].getHostName()+":");
		for(InetAddress a:addresses)
		{
			System.out.println(a.toString());
		}
		
		addresses=InetAddress.getAllByName("google.com");//获取主机名对应的多个IP
		System.out.println(addresses[0].getHostName());
		for(InetAddress a:addresses)
		{
			System.out.println(a.toString());
		}
	}

}
