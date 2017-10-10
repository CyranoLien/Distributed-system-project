package Client;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class Tracker {
//	private static Scanner scan;
	public static int size_x;
	public static int size_y;
	public static int num_t;

	public static void main(String[] args) {
		try {
			LocateRegistry.createRegistry(1099); // 在本地主机上创建和导出注册表实例，并在指定的端口上接受请求
			AddClient tracker = new AddClientImpl("tracker"); // 创建远程对象
			Context namingContext = new InitialContext(); // 初始化命名内容
			namingContext.rebind("rmi://localhost:1099/Tracker", tracker); // 注册对象，即把对象与一个名字绑定。

		} catch (RemoteException | NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Successfully register a remote object.");
	}
}
