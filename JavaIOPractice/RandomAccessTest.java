import java.io.DataInput;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.time.LocalDate;

public class RandomAccessTest {
	
	public static void writeFixedString(String s,int size,DataOutput out) throws IOException
	{
		//根据指定长度保存字符串，若字符串长度较指定长度短，则用0补齐
		size/=2;
		for(int i=0;i<size;i++)
		{
			char ch=0;
			if(i<s.length())ch=s.charAt(i);
			out.writeChar(ch);
		}
	}
	
	public static String readFixedString(int size,DataInput in) throws IOException
	{
		
		//根据指定长度读取字符串，遇到0则跳过
		size/=2;
		StringBuilder b=new StringBuilder(size);
		int i=0;
		boolean more=true;
		while(more&&i<size)
		{
			char ch=in.readChar();
			i++;
			if(ch==0)more=false;
			else b.append(ch);
		}
		in.skipBytes(2*(size-i));
		return b.toString();
	}
	
	public static void writeData(DataOutput out,Employee e) throws IOException
	{
		writeFixedString(e.getName(),Employee.NAME_SIZE,out);
		out.writeDouble(e.getSalary());
		
		LocalDate hireDay=e.getHireDay();
		out.writeInt(hireDay.getYear());
		out.writeInt(hireDay.getMonthValue());
		out.writeInt(hireDay.getDayOfMonth());
	}
	
	public static Employee readData(DataInput in) throws IOException
	{
		String name=readFixedString(Employee.NAME_SIZE,in);
		double salary=in.readDouble();
		int year=in.readInt();
		int month=in.readInt();
		int day=in.readInt();
		return new Employee(name,salary,year,month,day);
	}

	public static void main(String[] args) throws FileNotFoundException, IOException {
		// TODO Auto-generated method stub
		Employee[] staff=new Employee[3];
		
		staff[0]=new Employee("Carl Cracker",75000,1987,12,15);
		staff[1]=new Employee("Harry Hacker",50000,1989,10,1);
		staff[2]=new Employee("Tony Tester",40000,1990,3,15);
		
		try(DataOutputStream out=new DataOutputStream(new FileOutputStream("employee2.dat")))
		{
			for(Employee e:staff)writeData(out,e);
		}
		
		try(RandomAccessFile in=new RandomAccessFile("employee2.dat","r"))
		{
			int n=(int)(in.length()/Employee.RECORD_SIZE);//获取数据对象个数
			System.out.println(in.length());
			Employee[] newStaff=new Employee[n];
			
			for(int i=n-1;i>=0;i--)//逆序输入以测试随机访问
			{
				in.seek(i*Employee.RECORD_SIZE);
				newStaff[i]=readData(in);
			}
			
			for(Employee e:newStaff)
				System.out.println(e);
		}
	}

}
