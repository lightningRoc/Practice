import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.Scanner;

public class TextFileTest {
	
	private static void writeData(Employee[] employees,PrintWriter out)
	{
		//写数据对象的个数
		out.println(employees.length);
		
		for(Employee e:employees)
		{
			writeEmployee(out,e);
		}
	}
	
	public static void writeEmployee(PrintWriter out,Employee e)
	{
		out.println(e.getName()+"|"+e.getSalary()+"|"+e.getHireDay());
	}
	
	private static Employee[] readDate(Scanner in)
	{
		//读取数据对象的长度
		int n=in.nextInt();
		in.nextLine();//消耗掉分行符
		
		Employee[] employees=new Employee[n];
		for(int i=0;i<n;i++)
		{
			employees[i]=readEmployee(in);
		}
		return employees;
	}
	
	public static Employee readEmployee(Scanner in)
	{
		String line=in.nextLine();
		String[] tokens=line.split("\\|");
		String name=tokens[0];
		double salary=Double.parseDouble(tokens[1]);
		LocalDate hireDate=LocalDate.parse(tokens[2]);
		return new Employee(name,salary,hireDate.getYear(),hireDate.getMonthValue(),hireDate.getDayOfMonth());
	}

	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
		// TODO Auto-generated method stub
		Employee[] staff=new Employee[3];
		staff[0]=new Employee("Carl Cracker",75000,1987,12,15);
		staff[1]=new Employee("Harry Hacker",50000,1989,10,1);
		staff[2]=new Employee("Tony Tester",40000,1990,3,15);
		
		//以文本的方式保存所有数据
		try(PrintWriter out=new PrintWriter("employee.dat","UTF-8"))
		{
			writeData(staff,out);
		}
		
		try(Scanner in=new Scanner(new FileInputStream("employee.dat"),"UTF-8"))
		{
			Employee[] newStaff=readDate(in);
			
			for(Employee e:newStaff)
			{
				System.out.println(e);
			}
		}
	}

}
