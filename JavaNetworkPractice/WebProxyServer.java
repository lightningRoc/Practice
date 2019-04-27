import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WebProxyServer {
	
	public static class HttpHead//��������ͷ(������Ӧ����Ϣ)
	{
		private ArrayList<String> totalHead=new ArrayList<String>();
		
		private String method;//���󷽷�
		private String host;//Ŀ������
		private String port;//Ŀ�Ķ˿�
		//Ϊ���㣬д�����ַ�ʽ�ı�ʶ
		private static final String METHOD_GET="GET";
		private static final String METHOD_POST="POST";
		private static final String METHOD_CONNECT="CONNECT";
		
		public HttpHead()
		{
			method=null;
			host=null;
			port=null;
		}
		
		public static HttpHead readHeader(Scanner in) throws IOException 
		{
			HttpHead header = new HttpHead();
			StringBuilder result = new StringBuilder();
			//��ȡ����Э��
			String firstRow=in.nextLine();
			header.totalHead.add(firstRow);
			result.append(firstRow.substring(0,firstRow.indexOf(" ")));
			//ʶ������ķ�ʽ
			if(header.getMethod(result.toString())!=null)
			{
				while(in.hasNextLine())
				{
					String row=in.nextLine();
					header.totalHead.add(row);
					if(row.length()<1)break;
					header.getPostAndPort(row);
				}
			}
			
			return header;
		}
		
		private void getPostAndPort(String str)
		{
			if(str.startsWith("Host")){//���������Ͷ˿�
				String[] hosts= str.split(":");
				host=host=hosts[1].trim();//host:port����port
				if(method.equals(METHOD_CONNECT))
				{
					port=hosts.length==3?hosts[2]:"443";//httpsĬ�϶˿�Ϊ443
				}else if(method.endsWith(METHOD_GET)||method.endsWith(METHOD_POST))
				{
					port=hosts.length==3?hosts[2]:"80";//httpĬ�϶˿�Ϊ80
				}
			}
		}
		
		private String getMethod(String str)
		{
			if(str.startsWith(METHOD_CONNECT)){//https�������������
				method=METHOD_CONNECT;
			}else if(str.startsWith(METHOD_GET)){//http��GET����
				method=METHOD_GET;
			}else if(str.startsWith(METHOD_POST)){//http��POST����
				method=METHOD_POST;
			}
			return method;
		}
		
		public String getHost()
		{
			return host;
		}
		
		public String getPort()
		{
			return port;
		}
		
		public String getMethod()
		{
			return method;
		}
		
		public String getTotalHead()
		{
			StringBuilder result=new StringBuilder();
			for(String str : totalHead){
				result.append(str);
			}
			return result.toString();
		}
	}
	
	public static void response(Socket incoming) throws IOException, InterruptedException
	{
		System.out.println(Thread.currentThread()+":��������");
		//��������
		PrintWriter out=new PrintWriter(incoming.getOutputStream());
		//��ȡ����
		Scanner in=new Scanner(incoming.getInputStream());
		
		//��ȡ����ͷ�ؼ���Ϣ
		HttpHead head = HttpHead.readHeader(in);
		
		if (head.getHost() == null || head.getPort() == null)//����ʧ��
		{
			out.print("HTTP/1.1 500 Connection FAILED\r\n\r\n");
			return;
		}
		
		Socket server=new Socket(head.getHost(), Integer.parseInt(head.getPort()));
		server.setKeepAlive(true);
		
		if (head.getMethod().equals(HttpHead.METHOD_CONNECT)) 
		{
			// �������źŷ��ظ�����ҳ��
			out.print("HTTP/1.1 200 Connection established\r\n\r\n");
			out.flush();
		}else
		{
			//http������Ҫ������ͷ��ת��
			out.print(head.getTotalHead());
			out.flush();
		}
	
		SendThread t1=new SendThread(incoming.getOutputStream(),server.getInputStream(),"t1");
		SendThread t2=new SendThread(server.getOutputStream(),incoming.getInputStream(),"t2");
		t1.start();
		t2.start();
		
		//�ȴ��������
		t1.join();
		t2.join();
		
		System.out.println(Thread.currentThread()+":�������");
	}
	
	public static class SendThread extends Thread
	{
		//�������ݷ���
		OutputStream out;
		InputStream in;
		String name;
		
		public SendThread(OutputStream out,InputStream in,String name)
		{
			this.out=out;
			this.in=in;
			this.name=name;
		}
		
		public void run()
		{
			sendData(out,in,name);
		}
	}
	
	public static void sendData(OutputStream out,InputStream in,String name)
	{
		try//�ֽ�������ʽ��������(in->out)
		{
			byte[] buffer = new byte[4096];
			int len;
			while ((len = in.read(buffer)) != -1) 
			{
				if (len > 0) 
				{
					out.write(buffer, 0, len);
					out.flush();
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		final ExecutorService pool=Executors.newCachedThreadPool();//ʹ���̳߳������д���
		try(ServerSocket s=new ServerSocket(8002))
		{
			System.out.println("���������");
			while(true)//���Ͻ�������
			{
				Socket incoming=s.accept();
				incoming.setKeepAlive(true);
				Runnable r=()->
				{
					try 
					{
						WebProxyServer.response(incoming);
					} catch (Exception e) 
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				};
				pool.execute(r);
			}
		}
	}

}
