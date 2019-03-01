import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class WebServer {
	
	public static void response(Socket incoming) throws IOException
	{
		//提取文件名
		String filePath="";
		Scanner in=new Scanner(incoming.getInputStream(),"UTF-8");
		String firstRow=in.nextLine();
		filePath=firstRow.substring(firstRow.indexOf(' ')+1);
		filePath=filePath.substring(1,filePath.indexOf(' '));
		System.out.println(filePath);
		
		//获取文件
		File file=new File(filePath);	
		
		//输出http相应有关信息
		PrintWriter out=new PrintWriter(new OutputStreamWriter(incoming.getOutputStream(),"UTF-8"),true);
		DataOutputStream dataOut=new DataOutputStream(incoming.getOutputStream());
		
		//无文件
		if(!filePath.equals("index.html"))
		{
			out.println("HTTP/1.1 404 Not Found");
			incoming.close();
			return;
		}
		
		out.println("HTTP/1.1 200 OK");	
		out.println("Content-Length: "+file.length());
		out.println("Content-Type: text/html");
		out.println();
		sendFile(out,file,dataOut);
		
		//结束
		incoming.close();
	}
	
	public static void sendFile(PrintWriter out,File file,DataOutputStream dataOut) throws IOException
	{
		//发送文件信息(文本方式)
		try(Scanner in=new Scanner(new FileInputStream(file),"UTF-8"))
		{
			while(in.hasNextLine())
			{
				String line=in.nextLine();
				out.println(line);
			}
			out.flush();
		}
		
		//二进制流方式
		/*try(DataInputStream dataIn=new DataInputStream(new FileInputStream(file)))
		{
			for(int i=0;i<file.length();i++)
			{
				byte data=dataIn.readByte();
				dataOut.writeByte(data);
			}
		}*/
	}

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		try(ServerSocket s=new ServerSocket(8189))
		{
			System.out.println("服务端启动");
			while(true)//不断接受请求
			{
				Socket incoming=s.accept();
				Runnable r=()->
				{
					try 
					{
						WebServer.response(incoming);
					} catch (IOException e) 
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				};
				new Thread(r).start();
			}
		}
	}

}
