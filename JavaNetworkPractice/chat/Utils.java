package chat;

import java.io.Closeable;
import java.io.IOException;

/**
 * 工具类
 * 
 * @author PC
 *
 */
public class Utils {
	
	//关闭多个资源，使用可变参数
	public static void close(Closeable... resources)
	{
		for(Closeable resource:resources)
		{
			try 
			{
				if(resource!=null)
				{
					resource.close();
				}				
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
	}
}
