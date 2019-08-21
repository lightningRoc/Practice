package chat;

import java.io.Closeable;
import java.io.IOException;

/**
 * ������
 * 
 * @author PC
 *
 */
public class Utils {
	
	//�رն����Դ��ʹ�ÿɱ����
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
