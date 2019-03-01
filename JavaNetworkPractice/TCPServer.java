import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		try(ServerSocket s=new ServerSocket(8189))
		{
			System.out.println("·þÎñ¶ËÆô¶¯");
			try(Socket incoming=s.accept())
			{
				OutputStream outSream=incoming.getOutputStream();
				//autoFlush
				PrintWriter out=new PrintWriter(new OutputStreamWriter(outSream,"UTF-8"),true);
				out.println("Hello!This's server");
			}
		}
	}

}
