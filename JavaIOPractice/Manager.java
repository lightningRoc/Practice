
public class Manager extends Employee{
	
	private double bonus;
	private Employee secretary;
	
	public Manager(String name,double salary,int year,int month,int day)
	{
		super(name,salary,year,month,day);
		bonus=0;
	}
	
	public void setSecretary(Employee e)
	{
		secretary=e;
	}
	
	public double getSalary()
	{
		double baseSalary=super.getSalary();
		return baseSalary+bonus;
	}
	
	public Employee getSecretary()
	{
		return secretary;
	}
	
	public String toString()
	{
		return getClass().getName()+"[name="+super.getName()+",secretary=["+secretary+"],salary="+this.getSalary()+",hireDay="+super.getHireDay()+"]";
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
