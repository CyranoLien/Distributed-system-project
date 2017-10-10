package Client;

import java.rmi.RemoteException;

public class PlayerInfo implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	private int MessID = 0;
	private int totalplayer = 0;
	private int state[] = new int[100];
	private String username[] = new String[100];
	private AddFunc[] stub = new AddFunc[100];
	private int serverID = -1;
	private int backupID = -1;

	public void stubInitial(int i) throws RemoteException {
			stub[i] = new AddFuncImpl("i");
	}

	public int getMessID() {
		return MessID;
	}

	public void setMessID(int messID) {
		MessID = messID;
	}

	public int getTotalplayer() {
		return totalplayer;
	}

	public void setTotalplayer(int totalplayer) {
		this.totalplayer = totalplayer;
	}

	public String getUserName(int i) {
		return username[i];
	}

	public void setUsername(int i, String username) {
		this.username[i] = username;
	}

	public int getState(int i) {
		return state[i];
	}

	public void setState(int i, int state) {
		this.state[i] = state;
	}

	public void setStub(int i, AddFunc stub) {
		this.stub[i] = stub;
	}

	public AddFunc getStub(int i) {
		return stub[i];
	}

	public int getServerID() {
		return serverID;
	}

	public void setServerID(int serverID) {
		this.serverID = serverID;
	}

	public int getBackupID() {
		return backupID;
	}

	public void setBackupID(int backupID) {
		this.backupID = backupID;
	}

}
