import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;

public class UDPServer {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		try(DatagramSocket server = new DatagramSocket(8189))
		{
			byte[] message=new byte[1024];
			DatagramPacket receiveMessage = new DatagramPacket(message,message.length);
			server.receive(receiveMessage);
			System.out.println(Arrays.toString(message));
			
			String returnMessage="123";
			byte[] auxMessage=returnMessage.getBytes();
			DatagramPacket sendMessage = new DatagramPacket(auxMessage,auxMessage.length,InetAddress.getLocalHost(),10000);
			server.send(sendMessage);
		}
	}

}
