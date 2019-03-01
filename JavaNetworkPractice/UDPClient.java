import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;

public class UDPClient {
	
	

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		try(DatagramSocket client=new DatagramSocket(10000))
		{
			String message="012";
			byte[] auxMessage=message.getBytes();
			DatagramPacket sendMessage=new DatagramPacket(auxMessage,auxMessage.length,InetAddress.getLocalHost(),8189);
			client.send(sendMessage);
			
			byte[] confirm=new byte[1024];
			DatagramPacket receiveMessage = new DatagramPacket(confirm,confirm.length);
			client.receive(receiveMessage);
			System.out.println(Arrays.toString(confirm));
		}
	}

}
