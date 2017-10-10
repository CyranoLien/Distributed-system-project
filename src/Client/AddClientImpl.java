package Client;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class AddClientImpl extends UnicastRemoteObject implements AddClient {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public static PlayerInfo player_table;
	int serverID = -1;
	int backupID = -1;

	public AddClientImpl(String name) throws RemoteException {
		super();
		player_table = new PlayerInfo();
		// player_table.stubInitial();
	}

	public int setIndex(String username) throws RemoteException {
		int num;

		num = player_table.getTotalplayer();
		player_table.setMessID(player_table.getMessID() + 1);
		player_table.setUsername(num, username);

		System.out.println(num + " " + username);

		player_table.setTotalplayer(num + 1);

		if (serverID == -1) { // updates state
			player_table.setState(num, 1);
			serverID = num;
			player_table.setServerID(serverID);
		} else if (backupID == -1) {
			player_table.setState(num, 2);
			backupID = num;
			player_table.setBackupID(backupID);
		} else
			player_table.setState(num, 3);
		return num;
	}

	public void storePlayerInfo(int num, AddFunc stub) throws RemoteException {
		int i;
		player_table.stubInitial(num);
		player_table.setStub(num, stub);
		i = player_table.getServerID();
		System.out.println(num + "HELPME00000   " + i);
		trakerPrint();
		// player_table.getStub(i).updateTable(player_table); // updates server's table

	}

	public PlayerInfo getPlayerInfo() {
		return player_table;
	}

	public void updateTable(PlayerInfo playertable) throws RemoteException {
		player_table = playertable;
	}

	public void trakerPrint() throws RemoteException {
		System.out.println("--------------------------------");
		for (int i = 0; i < player_table.getTotalplayer(); i++) {
			System.out.println(i + "	" + player_table.getUserName(i) + "	" + player_table.getState(i));
		}
		System.out.println("--------------------------------");
	}

	public void testP(int i) throws RemoteException {
		System.out.println(i);
	}
}