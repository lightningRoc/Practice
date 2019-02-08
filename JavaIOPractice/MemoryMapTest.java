import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.zip.CRC32;

public class MemoryMapTest {
	
	//ͨ��CRC32�������ļ�����������
	public static long checksumInputStream(Path filename) throws IOException
	{
		//��ͨ����������
		try(InputStream in=Files.newInputStream(filename))
		{
			CRC32 crc=new CRC32();
			
			int c;
			while((c=in.read())!=-1)
			{
				crc.update(c);
			}
			return crc.getValue();
		}
	}
	
	public static long checksumBufferedInputStream(Path filename) throws IOException
	{
		//�����������
		try(InputStream in=new BufferedInputStream(Files.newInputStream(filename)))
		{
			CRC32 crc=new CRC32();
			
			int c;
			while((c=in.read())!=-1)
			{
				crc.update(c);
			}
			return crc.getValue();
		}
	}
	
	public static long checksumRandomAccessFile(Path filename) throws FileNotFoundException, IOException
	{
		//�ļ��������
		try(RandomAccessFile file=new RandomAccessFile(filename.toFile(),"r"))
		{
			long length=file.length();
			CRC32 crc=new CRC32();
			
			for(long p=0;p<length;p++)
			{
				file.seek(p);
				int c=file.readByte();
				crc.update(c);
			}
			return crc.getValue();
		}
	}
	
	public static long checksumMappedFile(Path filename) throws IOException
	{
		//�ڴ�ӳ�䷽ʽ����
		try(FileChannel channel=FileChannel.open(filename))
		{
			CRC32  crc=new CRC32();
			int length=(int)channel.size();
			MappedByteBuffer buffer=channel.map(FileChannel.MapMode.READ_ONLY, 0, length);
			
			for(int p=0;p<length;p++)
			{
				int c=buffer.get(p);
				crc.update(c);
			}
			return crc.getValue();
		}
	}

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Scanner scan=new Scanner(System.in);
		Path filename=Paths.get(scan.next());
				
		System.out.println("��ͨ������:");
		long start=System.currentTimeMillis();
		long crcValue=checksumInputStream(filename);
		long end=System.currentTimeMillis();
		System.out.println(Long.toHexString(crcValue));
		System.out.println((end-start)+"ms");
		
		System.out.println("������������:");
		start=System.currentTimeMillis();
		crcValue=checksumBufferedInputStream(filename);
		end=System.currentTimeMillis();
		System.out.println(Long.toHexString(crcValue));
		System.out.println((end-start)+"ms");
		
		System.out.println("��������ļ�:");
		start=System.currentTimeMillis();
		crcValue=checksumRandomAccessFile(filename);
		end=System.currentTimeMillis();
		System.out.println(Long.toHexString(crcValue));
		System.out.println((end-start)+"ms");
		
		System.out.println("�ڴ�ӳ���ļ�s:");
		start=System.currentTimeMillis();
		crcValue=checksumMappedFile(filename);
		end=System.currentTimeMillis();
		System.out.println(Long.toHexString(crcValue));
		System.out.println((end-start)+"ms");
	}

}
