package Client;

import java.rmi.RemoteException;
import java.util.Scanner;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class Game implements Runnable {
	public static int size_x = 15;
	public static int size_y = 15;
	public static int num_t = 10;
	public static int num_p = 100;
	public static int now_p;
	public static int index;
	public static String input;
	public static TRStruct[] tr = new TRStruct[num_t];
	public static PLStruct[] pl = new PLStruct[num_p];
	public static MazeStruct[][] maze;
	private static PlayerInfo playertable = new PlayerInfo();

	private static Scanner scan = new Scanner(System.in);;
	private static AddFunc nowserver;
	private static AddFunc nowbackup;
	private static AddClient trac;
	static Display2 display;
	static Thread thread;

	public static void main(String[] args) throws RemoteException, NamingException, InterruptedException {
		// System.out.println("Please input your username:");
		// scan = new Scanner(System.in);
		// String username = scan.next();
		if (args.length != 3) {
			System.out.println("Wrong number of parameters...exiting");
			System.exit(0);
		}
		String username = args[2];
		String url = "rmi://localhost:1099/";
		Game game = new Game();
		thread = new Thread(game);

		try {
			// communicate with tracker
			Context namingContext = new InitialContext(); // 初始化命名内容
			trac = (AddClient) namingContext.lookup(url + "Tracker"); // 获得远程对象的存根对象

			AddFunc function = new AddFuncImpl(username); // 为本地存根创建远程对象
			namingContext.rebind(url + username, function);
			AddFunc func = (AddFunc) namingContext.lookup(url + username); // 本地存根

			index = trac.setIndex(username);
			trac.storePlayerInfo(index, func);
			System.out.println(index);
			playertable = trac.getPlayerInfo();
			nowserver = playertable.getStub(playertable.getServerID());
			System.out.println("Nowserver stub: " + nowserver);

			// communicate with player
			int i = 0;
			while (i != 1) {
				try {
					i = nowserver.checkConnection();
				} catch (RemoteException e) {
					i = 0;
					Thread.sleep(50);
					playertable = trac.getPlayerInfo();
					nowserver = playertable.getStub(playertable.getServerID());
					System.out.println("PPPPPPP");
				}
			}
			nowserver.updateTable(playertable);

			if (index == playertable.getServerID()) {
				nowserver.initialMaze();
				maze = nowserver.returnMaze();
				System.out.println(nowserver.returnTable().getTotalplayer());
				nowserver.initialPlayer(index, username);
				display = new Display2(index, username, maze, size_x, size_y, num_t, num_p);
				display.rebuild(func);

				thread.start();
				letsServer(username);
			} else if (index == playertable.getBackupID()) {
				nowbackup = playertable.getStub(playertable.getBackupID());
				nowbackup.updateTable(playertable);
				maze = nowserver.returnMaze();
				nowserver.initialPlayer(index, username);
				display = new Display2(index, username, maze, size_x, size_y, num_t, num_p);
				display.rebuild(func);

				thread.start();
				letsBackup(username);
			} else {
				// nowbackup = playertable.getStub(playertable.getBackupID());
				i = 0;
				while (i != 1) {
					try {
						maze = nowserver.returnMaze();
						nowserver.initialPlayer(index, username);
						i = nowserver.checkConnection();
						System.out.println("nowserver:" + playertable.getUserName(playertable.getServerID()));
					} catch (RemoteException e) {
						System.out.println("------------line 103");
						playertable = trac.getPlayerInfo();
						nowserver = playertable.getStub(playertable.getServerID());
					}
				}
				func.updateTable(playertable);
				display = new Display2(index, username, maze, size_x, size_y, num_t, num_p);
				display.rebuild(func);

				thread.start();
				letsPlayer(func, username);
			}
		} catch (NamingException | RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void letsServer(String username) throws RemoteException, InterruptedException {
		trac.trakerPrint();
		int server_i = 1, temp;

		System.out.println("***************** Now server is player " + username + " *****************");
		System.out.println("");
		while (true) {
			Thread.sleep(50);

			playertable = nowserver.returnTable();
			int disconn = ping(index, playertable.getTotalplayer(), playertable);
			if (disconn == -1) {
				if (playertable.getBackupID() == -1) {
					temp = chooseNext(index, index, trac);
					System.out.println("Successfully choose " + temp);
					playertable.setBackupID(temp);
					nowbackup = playertable.getStub(temp);
					nowbackup.updateTable(playertable);
					nowserver.updateTable(playertable);
					trac.updateTable(playertable);
					trac.trakerPrint();
					System.out.println("************************** Attention **************************");
					System.out.println("Server has set player " + playertable.getUserName(temp) + " as backup.");
					System.out.println("*********************** Game continues. ***********************");
				}
				// System.out.println("Time " + server_i + ": Server confirms everyone is
				// online.");
			} else if (disconn == index) {
				if (playertable.getTotalplayer() > 2)
					System.out.println("You are alone.");
			} else if (disconn == playertable.getBackupID()) {
				playertable.setState(disconn, -1);
				playertable.setBackupID(-1);

				nowserver.updateTable(playertable);
				trac.updateTable(playertable);
				trac.trakerPrint();
				System.out.println("******** Backup node (player " + playertable.getUserName(disconn)
						+ ") has been disconnected. ********");
				System.out.println("");
			} else {
				playertable.setState(disconn, -1);
				nowserver.updateTable(playertable);
				trac.updateTable(playertable);
				trac.trakerPrint();
				System.out.println("******** Normal node (player " + playertable.getUserName(disconn)
						+ ") has been disconnected. ********");
				System.out.println(
						"Time " + server_i + ": Server has removed player " + playertable.getUserName(disconn) + ".");
				System.out.println("*********************** Game continues. ***********************");
				System.out.println("");
			}
			server_i += 1;
		}
	}

	private static void letsBackup(String username) throws RemoteException, InterruptedException {
		trac.trakerPrint();
		int backup_i = 1;

		System.out.println("***************** Now backup is player " + username + " *****************");
		System.out.println("");
		while (true) {
			Thread.sleep(50);
			playertable = nowbackup.returnTable();
			int disconn = ping(index, playertable.getTotalplayer(), playertable);

			if (disconn == index) {
				System.out.println("Your game has crushed.");
			} else if (disconn == playertable.getServerID()) {
				playertable.setState(disconn, -1);
				System.out.println("******** Server node (player " + playertable.getUserName(disconn)
						+ ") has been disconnected. ********");
				playertable.setServerID(index);
				playertable.setState(index, 1);
				playertable.setBackupID(-1);
				nowserver = playertable.getStub(index);
				nowserver.updateTable(playertable);
				trac.updateTable(playertable);
				for (int i = 0; i < playertable.getTotalplayer(); i++) {
					if ((playertable.getState(i) != -1) && (i != index)) {
						try {
							playertable.getStub(i).updateTable(playertable);
						} catch (RemoteException e) {
						}
					}
				}
				trac.trakerPrint();
				System.out.println(
						"Time " + backup_i + ": Backup sets player " + playertable.getUserName(index) + " as server.");
				System.out.println("*********************** Game continues. ***********************");

				letsServer(username);
			} else {
				nowbackup.updateTable(nowserver.returnTable());
				nowbackup.updateMaze(nowserver.returnMaze());
				nowbackup.updateTreasure(nowserver.returnTR());
				nowbackup.updatePlayer(nowserver.returnPL());
			}
			backup_i += 1;
		}
	}

	private static void letsPlayer(AddFunc func, String username) throws RemoteException, InterruptedException {
		trac.trakerPrint();
		int normal_i = 1, temp;

		System.out.println("***************** Now player " + username + " is normal. *****************");
		System.out.println("");
		while (true) {
			Thread.sleep(50);
			if (index == playertable.getBackupID())
				break;
			// otherwise, remains to be a normal player and do nothing;

			playertable = func.returnTable();
			temp = playertable.getServerID();
			nowserver = playertable.getStub(temp);
			try {
				int i = nowserver.checkConnection();
				System.out.println("Time " + normal_i + " ping " + temp + " and result is " + i);
			} catch (RemoteException e) {
			}
			normal_i += 1;
		}
		// index == backupID
		nowbackup = playertable.getStub(index);
		nowbackup.updateMaze(nowserver.returnMaze());
		nowbackup.updateTreasure(nowserver.returnTR());
		nowbackup.updatePlayer(nowserver.returnPL());
		playertable.setState(index, 2);
		nowserver.updateTable(playertable);
		nowbackup.updateTable(playertable);
		trac.updateTable(playertable);

		letsBackup(username);
	}

	private static int ping(int index, int now_pl, PlayerInfo playertable) throws RemoteException {
		int temp = -1;
		int conn[] = new int[num_p];
		conn[index] = 0;
		for (int i = 0; i < now_pl; i++) {
			if ((playertable.getState(i) != -1) && (i != index)) {
				try {
					conn[i] = playertable.getStub(i).checkConnection();
				} catch (RemoteException e) {
					conn[i] = 0;
				}
				conn[index] |= conn[i];
			}
		}
		for (int i = 0; i < now_pl; i++) {
			if ((playertable.getState(i) != -1) && (conn[i] == 0) && (i != index)) {
				return i;
			}
		}
		if (conn[index] == 0)
			return index;
		// if = -1, no one crushes; if = index, itself crushes;
		// otherwise, the returned one crushes.
		return temp;
	}

	private static int chooseNext(int a, int b, AddClient func) throws InterruptedException, RemoteException {
		int temp, temp2;
		temp = (a > b ? a : b) + 1;
		while (playertable.getState(temp) == -1) {
			temp++;
		}
		while (playertable.getState(temp) != 3) {
			temp2 = func.getPlayerInfo().getState(temp);
			System.out.println("Wait a second, it should be " + temp + " ??? " + temp2);
			playertable = func.getPlayerInfo();
		}
		return temp;
	}

	@Override
	public void run() {
		while (true) {
			if (scan.hasNextLine()) {
				input = scan.nextLine();
				try {
					display.inputListener(input);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
