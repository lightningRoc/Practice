
public class GetSystemInfo {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("用户工作目录:"+System.getProperty("user.dir"));
		System.out.println("操作系统:"+System.getProperty("os.name"));
		System.out.println("行结束符:"+System.getProperty("line.separator"));
		System.out.println("文件分隔符:"+java.io.File.separator);
	}

}
