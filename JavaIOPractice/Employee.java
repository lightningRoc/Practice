import java.io.Serializable;
import java.time.LocalDate;

public class Employee implements Serializable{//≤‚ ‘ ˝æ›
	
	public static final int NAME_SIZE=80;
	public static final int SALARY_SIZE=8;
	public static final int HIREDAY_SIZE=12;
	public static final int RECORD_SIZE=NAME_SIZE+SALARY_SIZE+HIREDAY_SIZE;
	
	private String name;
	private double salary;
	private LocalDate hireDay;
	
	public Employee(String name,double salary,int year,int month,int day)
	{
		this.name=name;
		this.salary=salary;
		hireDay=LocalDate.of(year, month, day);
	}
	
	public void raiseSalary(int m)
	{
		salary+=m;
	}
	
	public String getName()
	{
		return name;
	}
	
	public double getSalary()
	{
		return salary;
	}
	
	public LocalDate getHireDay()
	{
		return hireDay;
	}
	
	public String toString()
	{
		return getClass().getName()+"[name="+name+",salary="+salary+",hireDay="+hireDay+"]";
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Employee e=new Employee("Tony",5000,1987,12,15);
		System.out.println(e.getHireDay());
	}

}
